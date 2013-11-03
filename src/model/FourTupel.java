package model;
import java.util.ArrayList;

/**
 * Ein 4-Tupel, dass pro Zeile 4 String-Werte aufnehmen kann.
 * @author David
 */
public class FourTupel <Type1, Type2, Type3, Type4>
{
	/**
	 * Es folgen 4 ArrayList, deren Indizes in direkter Beziehung zueinander stehen. ArrayLists können durchsucht 
	 * werden, was in dieser Klasse benötigt wird.
	 * 
	 * Durch die Beziehung s1.get(0) <=> s2.get(0) <=> s3.get(0) <=> s4.get(0) wird definiert, dass die zusammengehörenden
	 * Elemente jeweils die gleichen Indizes in den jeweiligen ArrayLists haben.
	 */
	private ArrayList<Type1> s1 = new ArrayList<Type1>(); 
	private ArrayList<Type2> s2 = new ArrayList<Type2>();
	private ArrayList<Type3> s3 = new ArrayList<Type3>();
	private ArrayList<Type4> s4 = new ArrayList<Type4>();
	
	/**
	 * Fügt ein neues 4-Tupel hinzu.
	 * @param string1
	 * @param string2
	 * @param string3
	 * @param string4
	 * @throws Exception 
	 */
	public void add(Type1 string1, Type2 string2, Type3 string3, Type4 string4)
	{		
		s1.add(string1);
		s2.add(string2);
		s3.add(string3);
		s4.add(string4);
	}	
	
	public ArrayList<Type1> getS1()
	{
		return s1;
	}
	
	public ArrayList<Type2> getS2()
	{
		return s2;
	}
	
	public ArrayList<Type3> getS3()
	{
		return s3;
	}
	
	public ArrayList<Type4> getS4()
	{
		return s4;
	}
}
