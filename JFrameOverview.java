import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.*;

public class JFrameOverview implements ActionListener {
	
	// declaring main GUI components to be used
	JFrame fr;
	JButton btAddExp,btAddInc,btDelete,btModify,btFilter,btStats,btLogOut,btReset;
	JTextField tfNo,tfDate;
	JLabel title, usernameLabel, blank1,blank2,blank3,blank4,blank5,blank6;
	String[] typeFilter = {"All","Expense","Income"};
	JComboBox typeExpInc;
	JScrollPane allDisplay;
	JPanel displayPanel; 
	String userDataFileName; // the file name of client's expense and income record based on the username in current login session
	String username; // the client's username for the current login session
	
	private int numberOfData; // number of record available in the expense and income record

	// designing the GUI by managing the frame layout and displaying the components
	public JFrameOverview(String userDataFileName) throws IOException {	
		this.userDataFileName = userDataFileName;
		
		// adding frame title and image icon
		fr = new JFrame("Main Menu");
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// display the buttons on the main menu that serve different functionalities
		btAddExp = new JButton("Add Expense");
		btAddExp.addActionListener(this);
		btAddInc = new JButton("Add Income");
		btAddInc.addActionListener(this);
		btDelete = new JButton("Delete");
		btDelete.addActionListener(this);
		btModify = new JButton("Modify");
		btModify.addActionListener(this);
		btFilter = new JButton("Filter");
		btFilter.addActionListener(this);
		btStats = new JButton("View Statistic Information");
		btStats.addActionListener(this);
		btLogOut = new JButton("Log Out");
		btLogOut.setForeground(Color.RED);
		btLogOut.addActionListener(this);
		btReset = new JButton("Reset All Record");
		btReset.addActionListener(this);
		
		// extract the client's username 
		username = userDataFileName.split("\\_")[0].trim();
		title = new JLabel("Budget Tracker");
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		
		// display the main title and a welcome banner with the client's username of the current login session
		JPanel tempPanel = new JPanel();
		tempPanel.add(title);
		JPanel tempBoxPanel = new JPanel();
		tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS));
		tempBoxPanel.add(tempPanel);
		usernameLabel = new JLabel(String.format("Welcome %s!", username));
		usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		tempPanel = new JPanel();
		tempPanel.add(usernameLabel);
		tempBoxPanel.add(tempPanel);
		
		// adding text fields, combo boxes, and labels
		blank1 = new JLabel("Enter No. to Modify/Delete:");
		blank2 = new JLabel();
		blank3 = new JLabel();
		blank4 = new JLabel("Enter Date to Filter (optional):");
		blank5 = new JLabel();
		blank6 = new JLabel();
		tfNo = new JTextField();
		tfDate = new JTextField();
		typeExpInc = new JComboBox(typeFilter);
		displayPanel = new JPanel();
		
		// calculating the number of record available in the expense or income record for the current user
		File file = new File(userDataFileName);
		numberOfData = 0;
		if(file.exists()) {
			Scanner inputFile = new Scanner(file);
			while (inputFile.hasNext()) {
				inputFile.nextLine();
	            numberOfData++;
			}
			inputFile.close();		
		}
		
		// display the details of each record if the record exists
		if (numberOfData > 0 ) {
			displayPanel.setLayout(new GridLayout(0, 7, 25, 12));
			displayPanel.add(new JLabel("No."));
			displayPanel.add(new JLabel("Type"));
			displayPanel.add(new JLabel("Category"));
			displayPanel.add(new JLabel("Payment Method"));
			displayPanel.add(new JLabel("Amount (RM)"));
			displayPanel.add(new JLabel("Date"));
			displayPanel.add(new JLabel("Description"));
			
			Scanner inputFile = new Scanner(file);
			while(inputFile.hasNext())
			{
				String Line = inputFile.nextLine();
				String[]  parts = Line.split("\\|");
				for (int i = 0; i <= 6; i++) {
					displayPanel.add(new JLabel(parts[i]));
				}
			}
			inputFile.close();
		} else {
			// display a message to inform that there is no record currently and prompt the client to add it
			displayPanel.add(new JLabel("No income and expenses records found. Please add new entries to track your income and expenses."));
		}
		
		// add the components onto the frame
		allDisplay = new JScrollPane(displayPanel);
		allDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(5,3,6,6));
		p1.add(btAddExp);
		p1.add(btAddInc);
		p1.add(btStats);
		p1.add(blank1);
		p1.add(blank2);
		p1.add(blank3);
		p1.add(tfNo);
		p1.add(btModify);
		p1.add(btDelete);
		p1.add(blank4);
		p1.add(blank5);
		p1.add(blank6);
		p1.add(tfDate);
		p1.add(typeExpInc);
		p1.add(btFilter);
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.add(tempBoxPanel, BorderLayout.NORTH);
		p3.add(btLogOut, BorderLayout.EAST);
		p3.add(btReset, BorderLayout.WEST);
		fr.getContentPane().add(p3, BorderLayout.NORTH);
		fr.getContentPane().add(allDisplay);
		fr.getContentPane().add(p1, BorderLayout.SOUTH);
		
		// set the frame size of the main menu
		if (numberOfData < 4) {
			fr.setSize(840, 420);
		} else if (numberOfData < 8) {
			fr.setSize(840, 560);
		} else {
			fr.setSize(840, 700);
		}
		
		BudgetTracker.centerFrame(fr);
		BudgetTracker.confirmCloseFrame(fr);
		fr.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btReset) {
			if (numberOfData == 0) { // display a message to notify the client if there is no record available 
				JOptionPane.showMessageDialog(null, "No existing records available for resetting. Please add expense or income records.", 
						"Reminder", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			// display a confirmation dialog to make sure whether client wants to delete all record or not
			int userInput = JOptionPane.showConfirmDialog(null, "Are you confirm to delete all the existing income and expense records?", "Confirmation", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (userInput == JOptionPane.YES_OPTION) { 
				// delete all the record if 'Yes' button is clicked
				try {
					PrintWriter outputFile = new PrintWriter(userDataFileName);
					outputFile.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			
				// act as a tool to refresh the main menu for showing all record has been cleared
				fr.dispose();
				try {
					new JFrameOverview(userDataFileName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		else if (e.getSource() == btLogOut) {
			// display a confirmation dialog to make sure whether client wants to log out or not 
			int userInput = JOptionPane.showConfirmDialog(null, "Are you sure to log out from Username: " + username + " ?", "Confirmation", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (userInput == JOptionPane.YES_OPTION) {
				// close the main menu and directs to login page if 'Yes' button is clicked
				fr.dispose();
				new JFrameLogin();
			}
		}
		
		else if (e.getSource() == btAddExp) {
			// close the main menu and directs to 'Add Expense' frame
			fr.dispose();
			new JFrameExpenses(userDataFileName);
		}
		
		else if (e.getSource() == btAddInc) {
			// close the main menu and directs to 'Add Income' frame
			fr.dispose();
			new JFrameIncomes(userDataFileName);
		}
		
		else if (e.getSource() == btFilter) {
			String date = tfDate.getText();
			if (!tfDate.getText().isBlank()) { // check the validity of date if the date is entered
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
			}
			
			try {
				// opens a 'Filtering' frame to display the filtered expense and income records
				new JFrameFiltering(date, typeFilter[typeExpInc.getSelectedIndex()],userDataFileName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if (e.getSource() == btStats) {
			if (numberOfData == 0) {
				// display a message to inform the client there is currently no records of expense and income 
				JOptionPane.showMessageDialog(null, "No existing records available for statistics viewing. Please add expense or income records.", 
						"Reminder", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			new JFrameStatistics(userDataFileName); // opens a 'View Statistical Info' frame 
		}
		
		else if (e.getSource() == btModify || e.getSource() == btDelete) {
			if (numberOfData == 0) {
				// display a message to inform the client there is currently no records of expense and income 
				JOptionPane.showMessageDialog(null, "No existing records available for modification or deletion. Please add expense or income records.", 
						"Reminder", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			if (tfNo.getText().trim().isEmpty()) {
				// display an error message to inform the client that the record no. is blank 
				JOptionPane.showMessageDialog(null, "The number of record is blank! Please enter the record number to modify or delete.", 
						"Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// input validation for the record no.
			try {
				Integer.parseInt(tfNo.getText());
			}
			catch (NumberFormatException ex){ 
				// display error message if number entered is invalid
				JOptionPane.showMessageDialog(null, "The number of record entered is invalid! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (Integer.parseInt(tfNo.getText()) > numberOfData || Integer.parseInt(tfNo.getText()) <= 0 ) {
				// display error message if number entered is out of the range of available records
				JOptionPane.showMessageDialog(null, "The number of record entered is out of range! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (e.getSource() == btModify) {
				// close the main menu frame and directs to 'Modify Record' page
				fr.dispose(); 
				try {
					new JFrameModifying(tfNo.getText(),userDataFileName); 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} 
			
			else if (e.getSource() == btDelete) {
				// display a confirmation dialog to make sure whether the client wants to delete the entered record or not
				int userInput = JOptionPane.showConfirmDialog(null, "Are you confirm to delete record No." + tfNo.getText() + "?", "Confirmation", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if (userInput == JOptionPane.YES_OPTION) { // delete the entered record if 'Yes' button is clicked
					File file = new File(userDataFileName);
					if (file.exists()) {
						try {
							String target = tfNo.getText();
							ArrayList<String> availableRecord = new ArrayList<String>(); 
							Scanner inputFile = new Scanner(file);
							while (inputFile.hasNext()) {
								String record =inputFile.nextLine();
								String[] parts = record.split("\\|");
								String numbering = parts[0].trim(); 

								// get an ArrayList of all the records available except the one that matches the entered record no.
								if (!(numbering.equals(target))) { 
									if (Integer.parseInt(numbering)  < Integer.parseInt(target)) {
										availableRecord.add(record);
									}
									else {
										// reduce all the no. in the record by 1 if the record no. is larger than the entered number 
										// for making sure a consistency numbering  
										parts[0] = Integer.toString(Integer.parseInt(numbering) - 1);
										record = String.join("|", parts);
										availableRecord.add(record);
									}	
								}
							}
							inputFile.close();
							
							// rewrite each of the record into the file and for the record that matches the record no. will not be included
							PrintWriter outputFile = new PrintWriter(userDataFileName); 
							for(String i : availableRecord) {
								outputFile.println(i);
							}
							outputFile.close();
							// entered record is successfully erased
							
							// refresh the main menu to show that the record is deleted
							fr.dispose();
							try {
								new JFrameOverview(userDataFileName);
								// display a message to inform that the record entered has been deleted
								ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
								JOptionPane.showMessageDialog(null, "The record has been successfully deleted.", "Message", JOptionPane.INFORMATION_MESSAGE, img);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} 
						catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}	
	}	
}
