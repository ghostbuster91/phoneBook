import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/***
 * Singleton
 * Klasa reprezentuje książki telefoniczne składające się z obiektów klasy Osoba
 * @author Ghost
 *
 */
public class PhoneBook implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static PhoneBook instance_ = null; 
	
	private transient int idCounter_ = 0;
	
	private Map<Integer,Osoba> contacts_;
	
	private PhoneBook()
	{
		contacts_ = new HashMap<Integer,Osoba>();
	}
	public static synchronized PhoneBook getInstance()
	{
		if( instance_ == null )
			instance_ = new PhoneBook();
		return instance_;
	}
	public void addContact( Osoba _nOsoba )
	{
		contacts_.put(idCounter_++, _nOsoba);
	}
	public void delContact( int idx )
	{
		contacts_.remove(idx);
	}
	public Object[][] toArray()
	{  
		Object[][] toRet = new Object[contacts_.size()][Osoba.attrCount_+1];
		int c = 0;
		for( Integer i : contacts_.keySet() )
		{
			toRet[c][0] = contacts_.get(i).getNazwisko_();
			toRet[c][1] = contacts_.get(i).getImie_();
			toRet[c][2] = contacts_.get(i).getAdres_();
			toRet[c][3] = contacts_.get(i).getNrTelefonu_();
			toRet[c][4] = i;
			c++;
		}
		return toRet;
	}
	public void updateBook( Object[][] _nBook )
	{
		for( int i = 0; i < _nBook.length; i++ )
		{
			int key = Integer.parseInt(_nBook[4].toString() );
			contacts_.get(key).setNazwisko_(_nBook[i][0].toString());
			contacts_.get(key).setImie_(_nBook[i][1].toString());
			contacts_.get(key).setAdres_(_nBook[i][2].toString());
			contacts_.get(key).setNrTelefonu_(_nBook[i][3].toString());
		}
	}
	public void updateCell( Object _value, int _key, int _col ) throws Exception
	{
		switch( _col )
		{
			case 0:
				contacts_.get(_key).setNazwisko_(_value.toString());
				break;
			case 1:
				contacts_.get(_key).setImie_(_value.toString());
				break;
			case 2:
				contacts_.get(_key).setAdres_(_value.toString() );
				break;
			case 3:
				contacts_.get(_key).setNrTelefonu_(_value.toString());
				break;
			default:
				throw new Exception("No such column");
		}
	}
	public void PrintString()
	{
		for( Integer i : contacts_.keySet() )
		{
			System.out.println(contacts_.get(i));
		}
	}
	public static void setInstance( PhoneBook pb ) //  w celach deserializacji
	{
		if( instance_ == null )
		{
			instance_ = pb;
			try
			{
				instance_.idCounter_ = Collections.max(instance_.contacts_.keySet() ) + 1;
			}catch( java.util.NoSuchElementException e)
			{
				instance_.idCounter_ = 0;
			}
		}
	}
}
