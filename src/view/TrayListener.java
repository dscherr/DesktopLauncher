package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class TrayListener
{
	private Display display;

	public TrayListener(Display display)
	{
		this.display = display;
	}

	public Listener getInstance() {
		return new Listener()
		{
			public void handleEvent(org.eclipse.swt.widgets.Event event)
			{
				final Shell shell = new Shell(event.display);
				final Menu menu = new Menu(shell, SWT.POP_UP);		
				MenuItem exit = new MenuItem(menu, SWT.NONE);
				exit.setText("Goodbye!");
				
				exit.addListener(SWT.Selection, new Listener()
				{
					@Override
					public void handleEvent(Event event)
					{
						display.dispose();
						System.exit(0);
					}
				});
				
				menu.setVisible(true);
			};
		};
	}
}
