import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/****
 * Główna klasa zawierająca metodę main
 * oraz tworząca podstawowy GUI
 * @author Ghost
 *
 */
public class Test implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PhoneBook phoneBook_ = null;
	private JFrame frame_ = null;
	private JButton bAdd_ = null;
	private JButton bDel_ = null;
	private JTable table_ = null ;
	private TableColumn tcID_ = null;
	private final String serFile_ = "phoneBook.ser";
	private JTextField tfNazwFilter_ = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 new Test().buildGUI();
            }
        });
	}
	
	public void buildGUI()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);  
	    frame_ = new JFrame("Ksi��ka telefoniczna");  

	    initSystem();
	    initComponents(frame_);
	    
	    frame_.pack();  
	    frame_.setMinimumSize(frame_.getSize()); 
	    frame_.setLocationRelativeTo(null);  
	    frame_.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
	    
	    // dodanie serializacji obiektu PhoneBook "onExit"
	    frame_.addWindowListener(new WindowAdapter() {
	    	 public void windowClosing( WindowEvent e ) { 
	    		 serializePB();
	    		 frame_.dispose();
	    	 }
		});
	    
	    frame_.setVisible(true);
	    showEffect();
	}
	private void initComponents(JFrame f)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		PhoneBookTableModel.source_ = PhoneBook.getInstance();
		
		table_ = new JTable(PhoneBookTableModel.getInstance());

		//[1] Start: hiding "id" column
		TableColumnModel tcm = table_.getColumnModel();
		tcID_ = tcm.getColumn(4);
		tcm.removeColumn(tcID_);
		//[1] End
		
		TableRowSorter<PhoneBookTableModel> sorter_ = new TableRowSorter<PhoneBookTableModel>(PhoneBookTableModel.getInstance());
		table_.setRowSorter(sorter_);
	    table_.setPreferredScrollableViewportSize(new Dimension(400, 100));
	    table_.setFillsViewportHeight(true);
	    table_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    table_.setAutoCreateRowSorter(true);
	   
	    //Create the scroll pane and add the table to it.
	    JScrollPane scrollPane = new JScrollPane(table_);

	    //Add the scroll pane to this panel.
	    panel.add(scrollPane,BorderLayout.CENTER);
	    
	    JPanel panelForButtons = new JPanel();
	    panelForButtons.setLayout(new GridBagLayout());
	    
	    bAdd_ = new JButton("Dodaj");
	    bAdd_.addActionListener(this);
	    GridBagConstraints c = new GridBagConstraints();
	    c.insets = new Insets(10, 10, 0, 10);
	    c.gridx = 0;
	    c.gridy = 0;
	    panelForButtons.add(bAdd_,c);
	    
	    bDel_ = new JButton("Usu�");
	    bDel_.addActionListener(this);
	    c.gridx = 0;
	    c.gridy = 1;
	    panelForButtons.add(bDel_,c);

	    panel.add( panelForButtons, BorderLayout.EAST);
	    
	    JPanel panelForFilters = new JPanel();
	    panelForFilters.setLayout(new GridBagLayout());
	    
	    JLabel lNazwFilter = new JLabel("Nazwisko:");
	    c.gridx = 0;
	    c.gridy = 0;
	    c.insets = new Insets(10, 10, 10, 0);
	    panelForFilters.add(lNazwFilter,c);
	    
	    final int JTXF_WIDTH = 10;
	    tfNazwFilter_ = new JTextField(JTXF_WIDTH);
	    c.gridx = 1;
	    panelForFilters.add( tfNazwFilter_, c );
	    
	    tfNazwFilter_.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				newFilter();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				newFilter();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				newFilter();
			}
		});
	    
	    JButton bClearFilter = new JButton("Wyczyść");
	    c.gridx = 2;
	    panelForFilters.add(bClearFilter);
	    bClearFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				tfNazwFilter_.setText("");
			}
		});
	    
	    panel.add(panelForFilters,BorderLayout.SOUTH );
	    
		f.add(panel);
		
	}
	private void initSystem()
	{
		deserializePB();
		phoneBook_ = PhoneBook.getInstance();
	}
	/****
	 * Metoda odpowiadająca za efekt pojawiania się głównego okna
	 */
	private void showEffect()
	{
		final int timeInterval = 150;
		frame_.setOpacity(0.0f);
		
		final Timer timer1 = new Timer();
		timer1.schedule (new TimerTask() {
			
			float opacity_ = 0.0f;
			@Override
			public void run() {
				// TODO Auto-generated method stub

				opacity_ += 0.1f;
				if( opacity_ >= 1.0f )
				{
					frame_.setOpacity(1.0f);
					timer1.cancel();
					timer1.purge();
					return;
				}
				frame_.setOpacity(opacity_);
				
			}
		}, 0, timeInterval);
	}
	/****
	 * Metoda odpowiadająca za serializację obiektu klasy PhoneBook
	 */
	private void serializePB()
	{
		FileOutputStream fos = null;
		 ObjectOutputStream oos = null;

		 try {
		   fos= new FileOutputStream(serFile_); 
		   oos = new ObjectOutputStream(fos); 
		 
		   oos.writeObject(phoneBook_); 
		 
		 } catch (FileNotFoundException e) {
		   e.printStackTrace();
		 } catch (IOException e) {
		   e.printStackTrace();
		 } finally {

		   try {
		     if (oos != null) oos.close();
		   } catch (IOException e) {}
		   try {
		     if (fos != null) fos.close();
		   } catch (IOException e) {}
		 }
	}
	/****
	 * Metoda odpowiadająca za deserializację obiektu klasy PhoneBook
	 */
	private void deserializePB()
	{
		 FileInputStream fis = null;
		 ObjectInputStream ois = null;
		 try {
		   fis = new FileInputStream(serFile_); //utworzenie strumienia wej�ciowego  
		   ois = new ObjectInputStream(fis); 
		 
		   phoneBook_ = (PhoneBook) ois.readObject(); //deserializacja obiektu
		   PhoneBook.setInstance(phoneBook_);
		 
		 } catch (FileNotFoundException e) {
			 JOptionPane.showMessageDialog(null,"Pierwsze uruchomienie programu: utworzono nową książkę.", "Blad",
						JOptionPane.INFORMATION_MESSAGE);
		 } catch (IOException e) {
		   e.printStackTrace();
		 } catch (ClassNotFoundException e) {
			 JOptionPane.showMessageDialog(null,"Pik bazy jest uszkodzony. Utworzono nową bazę.", "Blad",
						JOptionPane.ERROR_MESSAGE);
		 } finally {
		   try {
		     if (ois != null) ois.close();
		   } catch (IOException e) {}
		   try {
		     if (fis != null) fis.close();
		   } catch (IOException e) {}
		 }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if( source == bAdd_ )
		{
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                 new AddFrame(frame_).buildGUI();
	            }
	        });
		}
		else if( source == bDel_)
		{
			int idx = table_.getSelectedRow();
			if (idx == -1)
				JOptionPane.showMessageDialog(null,"Nie zaznaczono pozycji", "Blad",
												JOptionPane.ERROR_MESSAGE);
			else
			{
				TableColumnModel tcm = table_.getColumnModel();
				tcm.addColumn(tcID_);
				
				phoneBook_.delContact( Integer.parseInt(table_.getValueAt(idx, 4).toString() ));
				tcm.removeColumn(tcID_);
				
				PhoneBookTableModel.getInstance().updateSource(phoneBook_);
				
			}
		}
	}
	/****
	 * Metoda tworząca filtr dla JTable na podstawie JTextBox tbNazwFiltr_
	 */
	@SuppressWarnings("unchecked")
	private void newFilter() {
        RowFilter<TableModel, Object> rf = null;
        
        try {
            rf = RowFilter.regexFilter(tfNazwFilter_.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        TableRowSorter<PhoneBookTableModel> sorter = (TableRowSorter<PhoneBookTableModel>) table_.getRowSorter();
        sorter.setRowFilter(rf);
    }

	

}
