package model;

import java.io.FileNotFoundException;
import java.io.IOException;

import view.GUI;

import com.csvreader.CsvReader;

import controller.ErrorLevel;

public abstract class ReadCSV
{
	private static ProgramList programs = null;
	private static GUI gui = null;
	private static String pathProgramFiles = System.getenv("ProgramFiles");
	private static String pathUserProfile = System.getenv("UserProfile");
	private static String pathWinDir = System.getenv("windir");

	public static void init(ProgramList pl, GUI dlGui) {
		programs = pl;
		gui = dlGui;
	}
	
	/**
	 * Test für gerade und ungerade Zahlen.
	 * 
	 * @param n
	 * @return true|false wenn n eine gerade|ungerade Zahl ist.
	 */
	public static boolean isEven(int n)
	{
		return ((n % 2) == 0) ? true : false;
	}

	/**
	 * Die Programmliste wird als CSV-Datei eingelesen.
	 */
	public static void readCSVProgramList()
	{
		// CSV einlesen: [Shortcut],[Name],[Verzeichnis/Link/EXE/SCR/Dokument],[Shortcut des bevorzugten Browsers]
		CsvReader reader;
		try
		{
			reader = new CsvReader("programs.csv");
			reader.readHeaders();

			while (reader.readRecord())
			{
				String shortcut = reader.get("shortcut");
				String name = reader.get("name");
				String target = reader.get("target");
				String openLinkInBrowser = reader.get("open-link-in-browser");

				// Übersetzungen von Umgebungsvariablen, Doppelbackslashes werden in start() dynamisch integriert, da
				// benutzerdefinierte Variablen zu einer Ersetzung in Form von program.getPath("[shortcut]") führen,
				// wodurch vorher integrierte Doppelbackslashes durch eine weitere Ersetzung am Ende zu
				// Vierfachbackslashes werden würden, die dann wieder entfernt werden müssten. Das ist kein Problem
				// aber unelegant, weshalb ich diese Ersetzung erst in start() vornehme. Eine weitere unelegante
				// Möglichkeit wäre eine 2. Schleife, die noch einmal alle (fertig übersetzten) targets mit
				// Doppelbackslashes ausrüstet.

				// ersetze %programfiles%, %userprofile%, %windir%
				target = target.replace("%programfiles%", pathProgramFiles);
				target = target.replace("%windir%", pathWinDir);
				target = target.replace("%userprofile%", pathUserProfile);

				// Prüfe, ob %[shortcut]% vorkommen, indem "%" gesucht wird, denn alle System-Umgebungsvariablen sind
				// bereits oben ersetzt worden, es können also nur noch benutzerdefinierte Variable sein.
				if (target.contains("%"))
				{
					// Extrahiere alle Shortcuts der Form "%[shortcut]%" innerhalb von target.
					String[] split = target.split("%");

					for (int i = 0; i < split.length; i++)
					{
						// Nur ungerade Indizes enthalten Shortcuts, wie das folgende Beispiel zeigt:
						// target = "%shortcut%\\...\\...\\%shortcut%"
						// split[0] = ""
						// split[1] = "shortcut" TREFFER
						// split[2] = "\\...\\...\\"
						// split[3] = "shortcut" TREFFER
						// split[4] = ""
						if (!isEven(i))
						{
							// Nur wenn der Shortcut existiert, ...
							if (programs.getPath(split[i]) != null)
							{
								// ...kann die Ersetzung stattfinden.
								target = target.replace("%" + split[i] + "%", programs.getPath(split[i]));
							} else
							{
								try
								{
									throw new Exception("Der Shortcut '%" + split[i] + "%' ist noch nicht definiert!");
								} catch (Exception e)
								{
									e.printStackTrace();
									gui.setWarnLabel(ErrorLevel.ERROR);
								}
							}
							// System.out.println(target);
						}
					}
				}

				programs.add(shortcut, name, target, openLinkInBrowser);
			}
			reader.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			gui.setWarnLabel(ErrorLevel.ERROR);
		} catch (IOException e)
		{
			e.printStackTrace();
			gui.setWarnLabel(ErrorLevel.ERROR);
		}
	}

}
