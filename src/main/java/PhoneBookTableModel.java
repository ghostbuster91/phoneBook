import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 * Singleton
 * Klasa reprezętuje model danych z klasy PhoneBook dla JTable
 * @author Ghost
 *
 */
public class PhoneBookTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String[] columnNames_ = { "Nazwisko","Imie","Adres","Nr tel", "id" };
	private Object[][] data_;
	
	static PhoneBook source_ = null; // only needed to create new instance
	
	private static PhoneBookTableModel instance_ = null;
	
	public static synchronized PhoneBookTableModel getInstance()
	{
		if(instance_ == null )
			instance_ = new PhoneBookTableModel(source_);
		return instance_;
	}
	
	private PhoneBookTableModel( PhoneBook pb )
	{
		this.data_ = pb.toArray();
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames_.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		int toRet = 0;
		if( data_ != null )
			toRet = data_.length;
		return toRet;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames_[col];
    }

	@Override
	public Object getValueAt(int _row, int _col) {
		// TODO Auto-generated method stub
		return data_[_row][_col];
	}
	@Override
	public boolean isCellEditable(int row, int col){ 
		return true; 
	}
	@Override
	public void setValueAt(Object value, int row, int col) {
		if( getColumnName(col).equals("Nr tel") )
		{
			String number = value.toString();
			try {
				number = AddFrame.unifiNumber(number);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,"Błędny numer telefonu! Wpisz poprawny numer, trójki\n" +
				 		" cyfr mogą być rozdzielone spacją bądź myślnikiem", "Blad",
							JOptionPane.ERROR_MESSAGE);
				return;
			}
			value = (Object)number;
		}
		data_[row][col] = value;
		fireTableCellUpdated(row, col);
		
		try {
			source_.updateCell(value, Integer.parseInt(data_[row][4].toString() ),col );

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateSource( PhoneBook pb )
	{
		data_ = pb.toArray();
		fireTableDataChanged();
	}
}
