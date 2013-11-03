package view;

import model.ProgramList;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

import controller.Commander;

/**
 * Das Präfix "DL" steht für DesktopLauncher, denn KeyListener existiert bereits und es gibt keinen sinnvollen Namen,
 * der für diesen speziellen KeyListener zusätzliche Informationen beinhaltet.
 * 
 * @author David
 */
public class DLKeyListener
{
	private GUI gui = null;
	private ProgramList programs = null;
	private Commander commander = null;

	public DLKeyListener(Commander commander, ProgramList programs, GUI gui)
	{
		this.commander = commander;
		this.programs = programs;
		this.gui = gui;
	}

	public KeyListener getInstance()
	{
		return new KeyListener()
		{
			public void keyReleased(KeyEvent keyEvent)
			{
				String text = programs.getPath(gui.getTextfield().getText());
				
				// roter Hintergrund des Eingabefelds, wenn der eingegebene Shortcut nicht existiert
				if (text == null)
				{
					gui.markInputAsNotValid();
				} else
				{
					gui.markInputAsValid();
				}

				if (keyEvent.keyCode == 13)
				{
					gui.markInputAsNotValid();
					gui.getTextfield().setText("");
				}
			}

			public void keyPressed(KeyEvent keyEvent)
			{
				String text = gui.getTextfield().getText();

				if (keyEvent.keyCode == 13)
				{
					if (programs.getPath(text) != null)
					{
						commander.start(text);
					} else
					{
						commander.runCommand(text);
					}
				}
			}
		};
	}
}
