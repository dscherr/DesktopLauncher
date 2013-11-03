package controller;

import java.io.IOException;
import view.GUI;
import model.ProgramList;

public class Commander
{
	private ProgramList programs = null;
	private GUI gui = null;

	public Commander(ProgramList programs, GUI gui)
	{
		this.programs = programs;
		this.gui = gui;
	}

	public void runCommand(String text)
	{
		// Kommandos beginnen mit "-"

		// exit
		if (text.equals("-ex"))
		{
			gui.getShell().getDisplay().dispose();
			System.exit(0);
		}
		// show shortcuts
		else if (text.equals("-ss"))
		{

		}
		// restart
		else if (text.equals("-re"))
		{
			start("dl");
			gui.getShell().getDisplay().dispose();
			System.exit(0);
		}
	}

	/**
	 * Startet das Programm mit dem angegebenen Programmfad.
	 * 
	 * @param programPath
	 *            Pfad des zu startenden Programms.
	 */
	public void start(String text)
	{
		String programPath = programs.getPath(text);

		if ((programPath != null) && !programPath.equals(""))
		{
			try
			{
				programPath = sanitizeProgramPath(text);

				Runtime.getRuntime().exec(programPath);
				// Process process = Runtime.getRuntime().exec(programPath); process.getErrorStream().
			} catch (IOException e)
			{
				e.printStackTrace();
				gui.setWarnLabel(ErrorLevel.ERROR);
			}
		} else
		{
			System.out.println("Das Programm existiert nicht!");
		}
	}

	private String sanitizeProgramPath(String text)
	{
		String programPath = programs.getPath(text);

		// Automatische Entscheidung mit welchem Programm gestartet werden soll:
		// 'cmd /C start', 'programs.getPath("ex")', 'Bildschirmschoner.scr /S'
		if (programPath.contains(".exe"))
		{
			// nichts ändern, aber muss stehen bleiben, damit das nicht im else-Fall behandelt wird
		} else if (programPath.contains("/"))
		{
			// Position: vor "Dokumente: Notepad++"
			// Link

			// Wenn kein Browser vordefiniert ist...
			if (programs.getPredefinedBrowser(text).isEmpty())
			{
				// und es sich um ein Link handelt...
				// ([^ ]/ soll "cmd /c start" und " /s" (Bildschirmschoner) ausschließen)
				if (programPath.matches(".*(http://|www.|[^ ]/|[.]de|[.]com|[.]org|[.]net).*"))
				{
					// dann im Standard-Browser starten.
					programPath = programs.getPath("ff") + " " + programPath;
					// System.out.println(target);
				}
			}
		} else if (programPath.matches(".*([.]xls|[.]doc|[.]msc).*"))
		{
			// Dokumente: Default-Programm benutzen
			programPath = "cmd /c start " + programPath;
		} else if (programPath.matches(".*([.]css|[.]ini|[.]htm|[.]html|[.]txt|[.]csv).*"))
		{
			// Dokumente: Notepad++
			programPath = programs.getPath("notepad++") + " " + programPath;
		} else if (programPath.contains(".scr"))
		{
			programPath = programPath + " /s";
		} else if (programPath.contains(".jar"))
		{
			programPath = "java.exe -jar " + programPath;
		} else if (programPath.contains("\\"))
		{
			// Es ist ein Verzeichnis
			programPath = "explorer " + programPath;
			System.out.println(programPath);
		}

		// Ansonsten mit angegebenen Browser starten.
		String browser = programs.getPredefinedBrowser(text);

		if (!browser.isEmpty())
		{
			programPath = programs.getPath(browser) + " " + programPath;
		}

		return programPath;
	}
}
