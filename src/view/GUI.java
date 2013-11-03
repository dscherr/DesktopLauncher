package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;

import controller.ErrorLevel;

/**
 * One Window with quit-button, text field and tooltip.
 */

public class GUI
{
	private final Shell shell;
	private Text text;
	private Label warnLabel;
	private Button add;
	private Display display;
	private TrayListener trayListener;
	private Tray tray;
	private TrayItem item;
	private Image img;
	/**
	 * Legt die möglichen Hintergrundfarben für das Eingabefeld der GUI fest, die je nach Zustand der Eingabe variieren.
	 */
	private final Color colorShortcutNotExists = new Color(display, 248, 152, 167);
	private final Color colorShortcutExists = new Color(display, 255, 255, 255);

	/**
	 * Erstellt das Fenster mit dem Button und dem Eingabefeld.
	 * 
	 * @param display
	 *            Adapter zwischen SWT und OS-UI
	 */
	public GUI()
	{
		display = Display.getDefault();
		tray = display.getSystemTray();
		item = new TrayItem(tray, 0);
		img = new Image(display, "tray.png");
		
		shell = new Shell(display, SWT.NONE);
		shell.setText("Desktop Launcher");
		shell.setLocation(2, display.getClientArea().height - 32);
		shell.setSize(152, 30);

		item.setToolTipText("Desktop Launcher");
		item.setImage(img);

		// createQuitButton();
		// createAddButton();
		createWarnLabel();
		createTextInput();
		
		trayListener = new TrayListener(display);
		//tray.addListener(SWT.MenuDetect, trayListener.getInstance());
		item.addListener(SWT.MenuDetect, trayListener.getInstance());

		shell.open();
	}

	private void createWarnLabel()
	{
		warnLabel = new Label(shell, SWT.WRAP | SWT.BORDER);
		warnLabel.setBounds(2, 2, 5, 5);
		warnLabel.setBackground(new Color(display, 0, 255, 0));
	}

	public void setWarnLabel(ErrorLevel errorLevel)
	{
		if (errorLevel == ErrorLevel.ERROR) {
			warnLabel.setBackground(new Color(display, 255, 0, 0));
		} else if (errorLevel == ErrorLevel.NO_PROBLEM) {
			warnLabel.setBackground(new Color(display, 0, 255, 0));
		}
	}

	public Text getTextfield()
	{
		return text;
	}

	/**
	 * Eingabefeld wird erstellt + FocusListener, MouseListener hinzugefügt.
	 * 
	 * @param shell
	 *            Fenster
	 */
	private void createTextInput()
	{
		text = new Text(shell, SWT.WRAP | SWT.BORDER);
		text.setBounds(10, 5, 130, 20);
		text.setText("notepad++");
		FocusListener focusListener = new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				Text t = (Text) e.widget;
				t.selectAll();
			}

			public void focusLost(FocusEvent e)
			{
				Text t = (Text) e.widget;
				if (t.getSelectionCount() > 0)
				{
					t.clearSelection();
				}
			}
		};

		text.addFocusListener(focusListener);
		text.addMouseListener(new MouseAdapter()
		{
			public void mouseDown(final MouseEvent e)
			{
				Text t = (Text) e.widget;
				t.selectAll();
			}
		});

		text.setFocus();
	}

	/**
	 * Quit-Button wird erstellt zum Beenden des Programms + SelectionListener hinzugefügt.
	 * 
	 * @param shell
	 *            Fenster
	 */
	public void createQuitButton()
	{
		Button quit = new Button(shell, SWT.PUSH);
		quit.setText("quit");
		quit.setBounds(10, 10, 50, 25);

		quit.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				display.dispose();
				System.exit(0);
			}
		});
	}

	public void createAddButton()
	{
		add = new Button(shell, SWT.PUSH);
		add.setText("add...");
		add.setBounds(65, 10, 50, 25);
	}

	public Shell getShell()
	{
		return shell;
	}

	public TrayItem getItem()
	{
		return item;
	}

	public Display getDisplay()
	{
		return display;
	}

	public void markInputAsValid()
	{
		text.setBackground(colorShortcutExists);
	}

	public void markInputAsNotValid()
	{
		text.setBackground(colorShortcutNotExists);
	}
}
