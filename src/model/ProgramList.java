package model;

import view.GUI;
import controller.ErrorLevel;

/**
 * ProgramList erweitert ThreeTupel um Methoden, die einen einfachen Zugriff auf die Daten der Programmliste
 * ermöglichen. Z. B. kann sehr einfach eine Abkürzung gesucht werden, wodurch man Zugriff auf den passenden
 * Programmpfad erhält.
 * 
 * @author David
 * 
 */
public class ProgramList extends FourTupel<String, String, String, String>
{
	private boolean isShortcut = false;
	private GUI gui = null;
	
	public ProgramList(GUI gui)
	{
		this.gui = gui;
	}

	/**
	 * @param searchString
	 *            Dieser Suchstring kann entweder den Shortcut, oder den Programmnamen enthalten. Der Pfad wird
	 *            automatisch ermittelt.<br/>
	 * <br/>
	 *            Aus dem Suchstring wird automatisch der <u>Zeilenumbruch</u> abgeschnitten, wenn nötig.
	 * @return
	 */
	public String getPath(String searchString)
	{
		int indexShortcut;
		int indexProgramName;
		int length;
		String path = null;

		// Falls sich "\r\n" (= ENTER) am Ende von searchString befindet, wird es abgeschnitten.
		// Das tritt immer dann auf, wenn ich ein Shortcut aus den Eingabefeld einlese und als searchString übergebe und
		// es tritt nicht auf, wenn ich den Shortcut direkt als String übergebe, z. B. wenn ich den Browser in
		// DesktopLauncher.setProgramList() über programs.getPath("ff") aufrufe.
		if (searchString.contains("\r\n"))
		{
			length = searchString.length() - 2;
			searchString = searchString.substring(0, length);
		}

		indexShortcut = getS1().indexOf(searchString);

		if (indexShortcut > -1)
		{
			path = getS3().get(indexShortcut);
			isShortcut = true;
		} else
		{
			indexProgramName = getS2().indexOf(searchString);
			isShortcut = false; // Es ist höchstens der Name oder eine ungültige Eingabe.

			if (indexProgramName > -1)
			{
				path = getS3().get(indexProgramName);
			} else
			{
				path = null;
			}
		}

		return path;
	}

	public boolean isShortcut()
	{
		return isShortcut;
	}

	/**
	 * Änderung: Wenn der Schlüssel (string1) bereits existiert, dann soll eine Fehlermeldung erscheinen.
	 * 
	 * @throws Exception
	 */
	@Override
	public void add(String string1, String string2, String string3, String string4)
	{
		// string1 gilt als Schlüssel, deshalb darf er nur einmal vorkommen.
		if (!getS1().contains(string1))
		{
			super.add(string1, string2, string3, string4);
		} else
		{
			// Fehlermeldung, dass string1 bereits existiert.
			try
			{
				throw new Exception("Der Parameterwert string1 = '" + string1
						+ "' darf nur einmal vorkommen und existiert bereits!");
			} catch (Exception e)
			{
				e.printStackTrace();
				gui.setWarnLabel(ErrorLevel.ERROR);
			}
		}
	}

	public String getPredefinedBrowser(String shortcutOrName)
	{
		int indexShortcut;

		if (isShortcut())
		{
			indexShortcut = getS1().indexOf(shortcutOrName);
		} else
		{
			indexShortcut = getS2().indexOf(shortcutOrName);
		}

		if (indexShortcut != -1)
		{
			return getS4().get(indexShortcut);
		} else
		{
			return "";
		}
	}
}
