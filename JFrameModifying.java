import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class JFrameModifying implements ActionListener {

	// declaring main GUI components to be used
	JFrame fr;
	JButton btQuit,btSave;
	JLabel title,lbDt,lbAmt,lbCat,lbPayMtd,lbDesc,selectedRecord,lbSelected;
	JTextField tfDt,tfAmt,tfDesc;
	JComboBox cbCat,cbPayMtd;
	String number; // the record no. to be modified
	String userDataFileName; // the file name of client's expense and income record based on the username in current login session
	
	// designing the GUI by managing the frame layout and displaying the components
	public JFrameModifying(String number,String userDataFileName) throws IOException {
		this.userDataFileName = userDataFileName;
		this.number = number;
		
		// adding frame title and image icon
		fr = new JFrame("Modify Record");
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// adding text fields and button for modifying record
		btQuit = new JButton("Cancel");
		btQuit.addActionListener(this);
		btSave = new JButton("Save");
		btSave.addActionListener(this);
		lbDt = new JLabel("Date (dd/mm/yy): ");
		lbCat = new JLabel("Category: ");
		lbPayMtd = new JLabel("Payment Method: ");
		lbDesc = new JLabel("Description: ");
		lbAmt = new JLabel("Amount (RM): ");
		
		// initializing the variable for each of the record details
		String date,amt,cat,payMtd,desc,type;
		type = "-";
		date = "-";
		amt = "-";
		cat = "-";
		payMtd = "-";
		desc = "-";
		File file = new File(userDataFileName);
		Scanner inputFile = new Scanner(file);
		while(inputFile.hasNext()) {
			String record = inputFile.nextLine();
			String[]parts = record.split("\\|");
			String numberOfData = parts[0].trim();
			if (numberOfData.equals(number)) { // assign the value for each variable
				type = parts[1].trim();
				date = parts[5].trim();
				amt = parts[4].trim();
				cat = parts[2].trim();
				payMtd = parts[3].trim();;
				desc = parts[6].trim();;
			}
		}
		inputFile.close();
		
		// adding components for displaying the details of selected record
		JPanel tempBoxPanel = new JPanel();
		tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS));
		title = new JLabel("Modify Record");
		title.setFont(new Font("SansSerif", Font.BOLD, 22));
		JPanel tempPanel = new JPanel();
		tempPanel.add(title);
		tempBoxPanel.add(tempPanel);
		lbSelected = new JLabel(String.format("Selected Record:  No. %s", number));
		lbSelected.setFont(new Font("SansSerif", Font.BOLD, 18));
		tempPanel = new JPanel();
		tempPanel.add(lbSelected);
		tempBoxPanel.add(tempPanel);
		JPanel tempGridPanel = new JPanel(new GridLayout(2,6,15,2));
		tempGridPanel.add(new JLabel("Type"));
		tempGridPanel.add(new JLabel("Date"));
		tempGridPanel.add(new JLabel("Amount (RM)"));
		tempGridPanel.add(new JLabel("Category"));
		tempGridPanel.add(new JLabel("Payment Method"));
		tempGridPanel.add(new JLabel("Description"));
		tempGridPanel.add(new JLabel(type));
		tempGridPanel.add(new JLabel(date));
		tempGridPanel.add(new JLabel(amt));
		tempGridPanel.add(new JLabel(cat));
		tempGridPanel.add(new JLabel(payMtd));
		tempGridPanel.add(new JLabel(desc));
		tempBoxPanel.add(tempGridPanel);
		tempBoxPanel.add(new JLabel(" "));
		
		// adding text fields and combo box
		tfDt = new JTextField(date);
		tfAmt = new JTextField(amt);
		tfDesc = new JTextField(desc);
		if (type.equals("Income")) {
			String [] typeList = {"Full-Time","Part-Time","Pocket Money","Allowance","Bonus","Other"};
			String [] payMtdList = {"-"};
			cbCat = new JComboBox(typeList);
			cbPayMtd = new JComboBox(payMtdList);
		}
		else {
			String [] catList = {"Food","Social Life","Pets","Transport","Culture","Household","Apparel","Beauty","Health","Education","Gift","Other"};
			String [] payMtdList = {"Cash","Bank","E-Wallet"};
			cbCat = new JComboBox(catList);
			cbPayMtd = new JComboBox(payMtdList);
		}
		cbCat.setSelectedItem(cat);
		cbPayMtd.setSelectedItem(payMtd);
		
		// adding the components onto the frame
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(5,2,4,4));
		JPanel p2 = new JPanel();
		p1.add(lbDt);
		p1.add(tfDt);
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
		JPanel p3 = new JPanel();
		p3.add(tempBoxPanel);
		fr.getContentPane().add(p3,BorderLayout.NORTH);
		fr.getContentPane().add(p1, BorderLayout.CENTER);
		fr.getContentPane().add(p2,BorderLayout.SOUTH);
		fr.setSize(700,400);
		BudgetTracker.centerFrame(fr);
		BudgetTracker.confirmCloseFrame(fr);
		fr.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btSave) {
			if (cbPayMtd.getSelectedItem().equals("-")) { // check if the selected record is an income type
			
				String date,description;
				double amount;
				IncomeType type = new IncomeType("Initialisation");
				date = tfDt.getText();
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
							// add '20' to the front of the year if the year is entered in only 2 digits
							throw new IllegalArgumentException("Please enter the date in (dd/mm/yyyy) format.");
						}
						else if ( length == 2) {
							date = date.substring(0, index+1) + "20" + date.substring(index+1);
						}
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
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
					if (i.getType() == cbCat.getSelectedItem()) { // set the income type according to the one that has been selected in combo box
						type = i;
					}
				}
			
				Incomes inc1 = new Incomes(date,type,amount,description); // create an object of income with all the details received
				try {
					inc1.modifyIncomes(this.number,userDataFileName); // modify and save the income into record
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				// close the current 'Modifying' frame and redirects to main menu
				fr.dispose();
				try {
					new JFrameOverview(userDataFileName);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				// display a message to inform that the income record has been modified
				ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
				JOptionPane.showMessageDialog(null, "The income record has been modified successfully", "Message", JOptionPane.INFORMATION_MESSAGE, img);
			
			}
			else { // the selected record is expense type
				
				String date = tfDt.getText();
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
							throw new IllegalArgumentException("Please enter the date in (dd/mm/yyyy) format.");
						}
						else if ( length == 2) {
							// add '20' to the front of the year if the year is entered in only 2 digits
							date = date.substring(0, index+1) + "20" + date.substring(index+1);
						}
					}
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
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

				Category[] availablecat= Category.getArray(); // get an array of available expenses category
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
				
				Expenses e1 = new Expenses(cat1,pay1,amount,date,description); // create an object of expense with all the details received
				try {
					e1.modifyExpenses(this.number,userDataFileName); // modify and save the expense into record
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
				// close the current 'Modifying' frame and redirects to main menu
				fr.dispose();
				try {
					new JFrameOverview(userDataFileName);
				} catch (IOException e3) {
					e3.printStackTrace();
				}
				
				// display a message to inform that the expense record has been modified
				ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
				JOptionPane.showMessageDialog(null, "The expense record has been modified successfully", "Message", JOptionPane.INFORMATION_MESSAGE, img);
				
			}
		}
		else if (e.getSource() == btQuit) {
			// close the current 'Modifying' frame and redirects to main menu
			fr.dispose();
			try {
				new JFrameOverview(userDataFileName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
