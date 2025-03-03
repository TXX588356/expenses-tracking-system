import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class JFrameExpenses implements ActionListener {
	
	// declaring main GUI components to be used
	JFrame fr;
	JButton btSave,btQuit;
	JLabel title,lbDate,lbAmt,lbCat,lbPayMtd,lbDesc;
	JComboBox cbCat,cbPayMtd;
	JTextField tfDate,tfAmt,tfDesc;
	String userDataFileName; // the file name of client's expense and income record based on the username in current login session
	
	public JFrameExpenses(String userDataFileName) {  // designing the GUI by managing the frame layout and displaying the components
		this.userDataFileName = userDataFileName;
		
		// add frame title and image icon
		fr= new JFrame("Add Expense");
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// add text fields and button
		btSave = new JButton("Save");
		btQuit = new JButton("Back");
		lbDate = new JLabel("Date (dd/MM/yy):");
		lbAmt = new JLabel("Amount (RM):");
		lbCat = new JLabel("Category:");
		lbPayMtd= new JLabel("Payment Method:");
		lbDesc = new JLabel("Description:");
		String [] catList = {"Food","Social Life","Pets","Transport","Culture","Household","Apparel","Beauty","Health","Education","Gift","Other"};
		String [] payMtdList = {"Cash","Bank","E-Wallet"};
		cbCat = new JComboBox(catList);
		cbPayMtd = new JComboBox(payMtdList);
		tfDate = new JTextField();
		tfAmt = new JTextField();
		tfDesc = new JTextField();
		JPanel p1 = new JPanel();
		btSave.addActionListener(this);
		btQuit.addActionListener(this);
		
		// add title
		title = new JLabel("Add Expense");
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		
		// add the components onto the frame
		p1.setLayout(new GridLayout(5,2,0,6));
		JPanel p2 = new JPanel();
		p1.add(lbDate);
		p1.add(tfDate);
		p1.add(lbAmt);
		p1.add(tfAmt);
		p1.add(lbCat);
		p1.add(cbCat);
		p1.add(lbPayMtd);
		p1.add(cbPayMtd);
		p1.add(lbDesc);
		p1.add(tfDesc);
		p2.add(btQuit);
		p2.add(btSave);
		JPanel tempPanel = new JPanel();
		tempPanel.add(title);
		fr.getContentPane().add(tempPanel, BorderLayout.NORTH);
		fr.getContentPane().add(p1, BorderLayout.CENTER);
		fr.getContentPane().add(p2,BorderLayout.SOUTH);
		fr.setSize(520, 300);
		BudgetTracker.centerFrame(fr);
		BudgetTracker.confirmCloseFrame(fr);
		fr.setVisible(true);
	}
	
	public void actionPerformed	(ActionEvent e) {
		if (e.getSource() == btSave) {
			String date = tfDate.getText();
			String description = tfDesc.getText();
			double amount;
			Category cat1 = new Category("Initialisation");
			PaymentMethod pay1 = new PaymentMethod("Initialisation");
			
			//input validation for empty date and amount
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
			catch (NumberFormatException ex){  // display error message when amount entered is invalid
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

			Category[] availablecat = Category.getArray(); // get an array of available expenses category
			for (Category i : availablecat) {
				  if(cbCat.getSelectedItem().equals(i.getType())) {
					  cat1 = i; // set the expenses category according to the one that has been selected in combo box
				  }
				}
			
			PaymentMethod[] availableMethod = PaymentMethod.getArray(); // get an array of available types of payment method
			for (PaymentMethod i : availableMethod) {
				  if(cbPayMtd.getSelectedItem().equals(i.getType())) {
					  pay1 = i; // set the payment method according to the one that has been selected in combo box
				  }
				}
			
			Expenses expenses = new Expenses(cat1,pay1,amount,date,description); // create an object of expense with all the details received
			try {
				expenses.addExpenses(userDataFileName);	//add and save the expense into record
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			// set all input data fields to blank after an expense has been added
			tfDate.setText("");
			tfAmt.setText("");
			cbCat.setSelectedIndex(0);
			cbPayMtd.setSelectedIndex(0);
			tfDesc.setText("");
			
			// displaying a message to inform that the expense has been added
			ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
			JOptionPane.showMessageDialog(null, "The expense has been added into the record successfully!\nYou can continue adding the record or press 'Back' button to main menu.", 
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
