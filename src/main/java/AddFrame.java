import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Klasa reprezentująca okno dodawania nowego wpisu
 * @author Ghost
 *
 */
public class AddFrame extends JFrame implements ActionListener{
	
	private JButton bCancel_;
	private JButton bOK_;
	private JFrame mainFrame_;
	private JTextField tfImie_;
	private JTextField tfNazw_;
	private JTextField tfAdres_; 
	private JTextField tfTelefon_; 
	
	
	public AddFrame(JFrame mainFrame) {
		super("Dodaj kontakt");
		
		this.mainFrame_ = mainFrame;
	}
	public void buildGUI()
	{
	    final int JTXF_WIDTH = 10;
	    
	    JPanel panel = new JPanel();
	    panel.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    
	    JLabel lImie = new JLabel("Imie:");
	    c.insets = new Insets(10, 10, 0, 0);
	    panel.add(lImie,c);
	    
	    tfImie_ = new JTextField(JTXF_WIDTH);
	    c.insets = new Insets(10, 10, 0, 10);
	    c.gridx = 1;
	    panel.add(tfImie_,c);
	    
	    JLabel lNazwisko = new JLabel("Nazwisko:");
	    c.insets = new Insets(10, 10, 0, 0);
	    c.gridx = 0;
	    c.gridy = 1;
	    panel.add(lNazwisko,c);
	    
	    tfNazw_ = new JTextField(JTXF_WIDTH);
	    c.insets = new Insets(10, 10, 0, 10);
	    c.gridx = 1;
	    c.gridy = 1;
	    panel.add(tfNazw_,c);
	    
	    JLabel lAdres = new JLabel("Adres: ");
	    c.insets = new Insets(10, 10, 0, 0);
	    c.gridx = 0;
	    c.gridy = 2;
	    panel.add(lAdres,c);
	    
	    tfAdres_ = new JTextField(JTXF_WIDTH);
	    c.insets = new Insets(10, 10, 0, 10);
	    c.gridx = 1;
	    c.gridy = 2;
	    panel.add(tfAdres_,c);
	    
	    JLabel lTelefon = new JLabel("Telefon: ");
	    c.insets = new Insets(10, 10, 0, 0);
	    c.gridx = 0;
	    c.gridy = 3;
	    panel.add(lTelefon,c);
	    
	    tfTelefon_ = new JTextField(JTXF_WIDTH);
	    c.insets = new Insets(10, 10, 0, 10);
	    c.gridx = 1;
	    c.gridy = 3;
	    panel.add(tfTelefon_,c);
	    
	    bCancel_ = new JButton("Anuluj");
	    bCancel_.addActionListener(this);
	    c.insets = new Insets(10, 10, 10, 10);
	    c.gridx = 1;
	    c.gridy = 4;
	    panel.add(bCancel_,c);
	    
	    bOK_ = new JButton("OK");
	    bOK_.addActionListener(this);
	    c.insets = new Insets(10, 10, 10, 0);
	    c.gridx = 0;
	    c.gridy = 4;
	    panel.add(bOK_,c);
	      
	    this.add(panel);
	    this.pack();
	    this.setResizable(false);
	    this.setLocation(mainFrame_.getLocation().x,mainFrame_.getLocation().y + mainFrame_.getHeight());
	    this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == bCancel_)
		{
			this.dispose();
		}
		else if( e.getSource() == bOK_)
		{
			
			String numer =  tfTelefon_.getText();
			try
			{
				numer = unifiNumber(numer);
		
				Osoba nOsoba = new Osoba(tfImie_.getText(),tfNazw_.getText(), unifiNumber(numer), tfAdres_.getText());
				PhoneBook pb = PhoneBook.getInstance();
				
				pb.addContact(nOsoba);
				PhoneBookTableModel.getInstance().updateSource(pb);
				
				this.dispose();
			}catch( Exception er)
			{
				JOptionPane.showMessageDialog(null,"Błędny numer telefonu! Wpisz poprawny numer, trójki\n" +
				 		" cyfr mogą być rozdzielone spacją bądź myślnikiem", "Blad",
							JOptionPane.ERROR_MESSAGE);
			}
				 
		}
	}
	public static String unifiNumber(String _number ) throws Exception
	{
		String pattern = "^[0-9]{3}([ ]|[-])?[0-9]{3}([ ]|[-])?[0-9]{3}";
		String pattern1 = "^[0-9]{9}";
		String pattern2 = "^[0-9]{3}[-]{1}[0-9]{3}[-]{1}[0-9]{3}";
		
		if( !_number.matches(pattern))
			throw new Exception("WrongNumber");
		
		if(_number.matches(pattern1) )
		{
			_number = _number.substring(0,3)+ " " + _number.substring(3,6)+ " "+ _number.substring(6,9);
		}
		else if(_number.matches(pattern2))
		{
			_number = _number.replace('-', ' ');
		}
		return _number;
	}
}
