package controller;

import java.io.IOException;

import model.ProgramList;
import model.ReadCSV;

import view.GUI;
import view.DLKeyListener;

/**
 * Startet Programme durch die Eingabe über ein Eingabfeld. Dabei sind Eingaben von definierten Abkürzungen oder des
 * vollständigen Programmnamens möglich. Zusätzlich kann das Label direkt angeklickt werden, um das damit verknüpfte
 * Programm zu starten.
 * 
 * @author David
 * @since 11.12.2010 <a
 *        href="http://localhost/mediawiki-1.16.0/index.php/Software-Projekt:_DesktopLauncher#.7B.7B0.7D.7D_Erstversion"
 *        >Software-Projekt: DesktopLauncher</a>
 * 
 * @todo Die Zuständigkeiten im DLKeyListener, sowie im TrayListener müssen noch inhaltlich korrekt getrennt werden
 *       bezogen auf den Controller
 * @todo Es existieren momentan 2 Shells: eine für die GUI selbst und eine für das Tray.
 */

public class DesktopLauncher
{
	/**
	 * Liste der Programme, die gestartet werden können. Ein 3-Tupel mit folgenden Werten: 1. Abkürzung (Alias) des
	 * Programms, z. B. "n" 2. Vollständiger Name des Programms, z. B. "notepad++" (falls ich die Abkürzung nicht zur
	 * Verfügung habe, möchte ich dennoch das Programm mit seinem Namen starten können). 3. Pfad des Programms.
	 */
	private ProgramList programs;
	private DLKeyListener keyListener;
	private GUI gui;
	private Commander commander;
	private static DesktopLauncher instance = null; // Singleton

	/**
	 * Singleton: privater Konstruktor
	 */
	private DesktopLauncher() {
		
	}
	
	/**
	 * Singleton
	 * 
	 * @return DesktopLauncher-Objekt, das nur neu erzeugt wird, wenn es noch nicht existiert.
	 */
	public static DesktopLauncher getInstance()
	{
		if (instance == null)
		{
			instance = new DesktopLauncher();
		}
		return instance;
	}

	/**
	 * Hauptprogramm
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void launch()
	{
		gui = new GUI();
		programs = new ProgramList(gui);
		ReadCSV.init(programs, gui);
		ReadCSV.readCSVProgramList();
				
		commander = new Commander(programs, gui);
		keyListener = new DLKeyListener(commander, programs, gui);
		gui.getTextfield().addKeyListener(keyListener.getInstance());

		// setShortcutsAsTooltip(); // Wird ab einer gewissen Größe unübersichtlich und flackert

		while (!gui.getShell().isDisposed())
		{
			if (!gui.getDisplay().readAndDispatch())
			{
				gui.getDisplay().sleep();
			} else
			{

			}
		}
		gui.getDisplay().dispose();
	}

	/**
	 * Legt sämtliche registrierte Shortcuts und ihre Namen als Inhalt für den Hauptfenster-Tooltip fest.
	 */
	@SuppressWarnings("unused")
	private void setShortcutsAsTooltip()
	{
		String tooltipText = new String();
		String shortcut = new String();
		String programName = new String();
		String format = "%1$-6s\t - %2$-1s\n";

		for (int i = 0; i < programs.getS1().size(); i++)
		{
			shortcut = programs.getS1().get(i);
			programName = programs.getS2().get(i);

			tooltipText += String.format(format, shortcut, programName);
		}
		// gui.getLabel().setText(labelText);
		gui.getShell().setToolTipText(tooltipText);
	}

}
