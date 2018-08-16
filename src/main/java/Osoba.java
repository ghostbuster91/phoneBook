/******
 * Klasa reprezentująca pojedyńczy kontakt w książce telefonicznej
 * @author Ghost
 */
public class Osoba implements Comparable<Osoba>, java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imie_;
	private String nazwisko_;
	private String nrTelefonu_;
	private String adres_;
	static public final int attrCount_ = 4;
	
	public Osoba( String _imie, String _nazwisko, String _nr, String _adres )
	{
		this.imie_ = _imie;
		this.nazwisko_ = _nazwisko;
		this.nrTelefonu_ = _nr;
		this.adres_ = _adres;
	}
	public String getImie_() {
		return imie_;
	}

	public void setImie_(String imie_) {
		this.imie_ = imie_;
	}

	public String getNazwisko_() {
		return nazwisko_;
	}

	public void setNazwisko_(String nazwisko_) {
		this.nazwisko_ = nazwisko_;
	}

	public String getNrTelefonu_() {
		return nrTelefonu_;
	}

	public void setNrTelefonu_(String nrTelefonu_) {
		this.nrTelefonu_ = nrTelefonu_;
	}

	public String getAdres_() {
		return adres_;
	}

	public void setAdres_(String adres_) {
		this.adres_ = adres_;
	}
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("");
		sb.append(nazwisko_ + " " + imie_ + " " + adres_ + " " + nrTelefonu_);
		return sb.toString();
	}
	@Override
	public int compareTo(Osoba _arg0) {
		// TODO Auto-generated method stub
		return (new String(nazwisko_ + imie_).compareToIgnoreCase(new String( _arg0.nazwisko_ + _arg0.imie_)));
	}
}
