package controller;

/**
 * Logging-Funktionalität spezifisch für dieses Projekt
 * 
 * @author Dave
 */
public class TestMyLogger
{
	private boolean isLogging;
	
	/**
	 * Konstruktor
	 * 
	 * @param isLogging schaltet den Logger ein/aus
	 */
	public TestMyLogger(boolean isLogging) {
		this.setLogging(isLogging);
	}
	
	public void log(String string)
	{
		if (isLogging == true)
		{
			System.out.println(string);
		}
	}
	
	public void setLogging(boolean isLogging) {
		this.isLogging = isLogging;
	}
}
