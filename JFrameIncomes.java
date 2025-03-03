import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;


public class JFrameIncomes implements ActionListener {
	
	// declaring main GUI components to be used
	JFrame fr;
	JButton btSave,btQuit;
	JLabel title,lbDate,lbAmt,lbType,lbDesc;
	JComboBox cbType;
	JTextField tfDate,tfDesc;
	JTextField tfAmt;
	String userDataFileName; // the file name of client's expense and income record based on the username in current login session
	
	public JFrameIncomes(String userDataFileName) { // designing the GUI by managing the frame layout and displaying the components
		this.userDataFileName = userDataFileName;
		
		// set frame title and frame image icon
		fr= new JFrame("Add Income");
		fr.setIconImage(new ImageIcon("logo.png").getImage()); 
		
		// adding text fields and button
		btSave = new JButton("Save");
		btQuit = new JButton("Back");
		lbDate = new JLabel("Date (dd/MM/yy):");
		lbAmt = new JLabel("Amount (RM): ");
		lbType = new JLabel("Category: ");
		lbDesc = new JLabel("Description: ");
		String [] typeList = {"Full-Time","Part-Time","Pocket Money","Allowance","Bonus","Other"};
		cbType = new JComboBox(typeList);
		tfDate = new JTextField();
		tfAmt = new JTextField();
		tfDesc = new JTextField();
		
		// add title
		title = new JLabel("Add Income");
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		
		// add the components onto the frame
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(4,2,0,6));
		btSave.addActionListener(this);
		btQuit.addActionListener(this);
		p1.add(lbDate);
		p1.add(tfDate);
		p1.add(lbAmt);
		p1.add(tfAmt);
		p1.add(lbType);
		p1.add(cbType);
		p1.add(lbDesc);
		p1.add(tfDesc);
		JPanel p2 = new JPanel();
		p2.add(btQuit);
		p2.add(btSave);
		JPanel tempPanel = new JPanel();
		tempPanel.add(title);
		fr.getContentPane().add(tempPanel, BorderLayout.NORTH);
		fr.getContentPane().add(p1, BorderLayout.CENTER);
		fr.getContentPane().add(p2,BorderLayout.SOUTH);
		fr.setSize(520, 275);
		BudgetTracker.centerFrame(fr);
		BudgetTracker.confirmCloseFrame(fr);
		fr.setVisible(true);
	}
	
	public void actionPerformed	(ActionEvent e) {
		if (e.getSource()==btSave) {
			String date, description;
			double amount;
			IncomeType type = new IncomeType("Initialisation");
			date = tfDate.getText(); 
			description = tfDesc.getText(); 
			
			if (tfAmt.getText().trim().isEmpty() || date.trim().isEmpty()) { // display error message when amount and/or date is blank
				JOptionPane.showMessageDialog(null, "The text field cannot be blank! Please fill in the amount and date.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//input validation for date
			try {
				int index = date.indexOf("/", 3); 
				if (index != -1) {
					int length = date.substring(index+1).length();
					if (!(length == 2 || length == 4)) {
						throw new IllegalArgumentException("Please enter the date in (dd/MM/yyyy) format.");
					}
					else if ( length == 2) {
						// add '20' to the front of the year if the year is entered in only 2 digits
						date = date.substring(0, index+1) + "20" + date.substring(index+1); 
					}
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				dateFormat.setLenient(false);
				Date aDate = dateFormat.parse(date);
				date = dateFormat.format(aDate);
			}
			catch(IllegalArgumentException ex) { // display error message when format of the date entered is invalid
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			catch (ParseException ex){ // display error message when date entered is invalid
				JOptionPane.showMessageDialog(null, "The date entered is invalid! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//input validation for amount
			try {
				amount = Double.parseDouble(tfAmt.getText());
			}
			catch (NumberFormatException ex){ // display error message when amount entered is invalid
				JOptionPane.showMessageDialog(null, "The amount entered is invalid! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//input validation for description
			if (description.contains("|")) { // prevent the character '|' to be stored in the file record to avoid future data retrieval error
				JOptionPane.showMessageDialog(null, "The character \'|\' is not allowed! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (description.isBlank()) { // set the description by default if no description is entered
				description = "-";
			}
			
			IncomeType[] allType = IncomeType.getArray(); // get an array of available types of income
			for(IncomeType i : allType) {
				if (i.getType() == cbType.getSelectedItem()) {
					type = i; // set the income type according to the one that has been selected in combo box
				}
			}
		
			Incomes income = new Incomes(date,type,amount,description); // create an object of income with all the details received
			try {
				income.addIncomes(userDataFileName); // add and save the income into record
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// set all input data fields to blank after an income has been added
			tfDate.setText("");
			tfAmt.setText("");
			cbType.setSelectedIndex(0);
			tfDesc.setText("");
			
			// displaying a message to inform that the income has been added
			ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
			JOptionPane.showMessageDialog(null, "The income has been added into the record successfully!\nYou can continue adding the record or press 'Back' button to main menu.", 
					"Message", JOptionPane.INFORMATION_MESSAGE, img);
		
		}
		else if(e.getSource()==btQuit) { // close the current frame and redirects to main menu
			fr.dispose();
			try {
				new JFrameOverview(userDataFileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	

}
	
