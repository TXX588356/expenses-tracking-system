import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class JFrameAdmin implements ActionListener{
	
	// declaring main GUI components to be used
	JFrame fr, frAdmin, frCheck, frReset, frDelete;
	JLabel title1, title2, title3, title4, title5;
	JTextField username, usernameReset, emailReset, usernameDelete, emailDelete;
	JPasswordField password, passwordReset;
	JButton loginBt, userBt, checkBt, resetBt, resetBt1, deleteBt, deleteBt1, logOutBt, backBt1, backBt2, backBt3;
	JScrollPane displayPanel1, displayPanel2, displayPanel3;
	
	public JFrameAdmin () { // designing the GUI by managing the frame layout and displaying the components
		
		// admin login frame
		fr = new JFrame("Budget Tracker Admin");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// adding title for admin login frame
		title1 = new JLabel("Admin Login");
		title1.setFont(new Font("SansSerif", Font.BOLD, 30));
		JPanel tempPanel = new JPanel();
		tempPanel.add(title1);		
		fr.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		// adding text fields and login button for admin login frame
		username = new JTextField(20);
		password = new JPasswordField(20);
		JPanel tempGridPanel = new JPanel(new GridLayout(2,2,20,10));
		tempGridPanel.add(new JLabel("Username: "));
		tempGridPanel.add(username);
		tempGridPanel.add(new JLabel("Password: "));
		tempGridPanel.add(password);
		tempPanel = new JPanel();
		tempPanel.add(tempGridPanel);
		JPanel tempBoxPanel = new JPanel();
		tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS));
		tempBoxPanel.add(tempPanel);
		loginBt = new JButton("Login");
		loginBt.addActionListener(this);
		tempPanel = new JPanel();
		tempPanel.add(loginBt);
		tempBoxPanel.add(tempPanel);
		tempBoxPanel.add(new JLabel("(Hint: Username & Password is 'admin')"));
		tempBoxPanel.add(new JLabel(" "));
		fr.getContentPane().add(tempBoxPanel);
		
		// adding button to return to client login page
		userBt = new JButton("Login as Client");
		userBt.addActionListener(this);
		tempPanel = new JPanel();
		tempPanel.add(userBt);
		fr.getContentPane().add(tempPanel, BorderLayout.SOUTH);
		
		fr.pack();
		BudgetTracker.centerFrame(fr);
		fr.setVisible(true);
		
		// administrator menu frame
		frAdmin = new JFrame("Budget Tracker Admin");
		frAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frAdmin.setIconImage(new ImageIcon("logo.png").getImage());
		
		// adding title for administrator menu frame
		title2 = new JLabel("        Budget Tracker Admin        ");
		title2.setFont(new Font("SansSerif", Font.BOLD, 30));
		tempPanel = new JPanel();
		tempPanel.add(title2);		
		frAdmin.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		// adding buttons for administrator menu frame
		tempGridPanel = new JPanel(new GridLayout(4,1,0,20));
		checkBt = new JButton("Check Client Data");
		checkBt.addActionListener(this);
		resetBt = new JButton("Reset Client Password");
		resetBt.addActionListener(this);
		deleteBt = new JButton("Delete Client Account");
		deleteBt.addActionListener(this);
		logOutBt = new JButton("Log Out From Admin");
		logOutBt.setForeground(Color.RED);
		logOutBt.addActionListener(this);
		tempGridPanel.add(checkBt);
		tempGridPanel.add(resetBt);
		tempGridPanel.add(deleteBt);
		tempGridPanel.add(logOutBt);
		tempPanel = new JPanel();
		tempPanel.add(tempGridPanel);
		frAdmin.getContentPane().add(tempPanel);
		frAdmin.pack();
		BudgetTracker.centerFrame(frAdmin);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginBt) { 
			if (username.getText().isBlank() || new String(password.getPassword()).isBlank()) { // display error message when text fields are blank
				JOptionPane.showMessageDialog(null, "Please enter a valid username and password!", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// create an object of admin with the details entered in the login field
			Admin admin1 = new Admin(username.getText(), new String(password.getPassword())); 
			if (admin1.verifyUser()) { // verify whether admin login info is valid
				frAdmin.setVisible(true);
				fr.dispose();
				// notification message to inform successful login
				ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
				JOptionPane.showMessageDialog(null, "Login successful!", "Message", JOptionPane.INFORMATION_MESSAGE, img); 
			}
			else { // display error message when admin login info is incorrect
				JOptionPane.showMessageDialog(null, "Incorrect username or password! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		}
		
		else if (e.getSource() == userBt) { 
			fr.dispose();
			new JFrameLogin(); // direct user back to login page
		}
		
		else if (e.getSource() == checkBt) {
			// create an object of admin with the details entered in the login field
			Admin admin1 = new Admin(username.getText(), new String(password.getPassword()));
			
			// adding title to check client data frame
			frCheck = new JFrame("Budget Tracker Admin");
			frCheck.setIconImage(new ImageIcon("logo.png").getImage());
			title3 = new JLabel("      Available Client Data      ");
			title3.setFont(new Font("SansSerif", Font.BOLD, 30));
			JPanel tempPanel = new JPanel();
			tempPanel.add(title3);		
			frCheck.getContentPane().add(tempPanel, BorderLayout.NORTH);
			
			// adding a display panel on check client data frame to display registered client available
			JPanel tempGridPanel = new JPanel(new GridLayout(0,3,50,10));
			tempGridPanel.add(new JLabel("Email"));
			tempGridPanel.add(new JLabel("Username"));
			tempGridPanel.add(new JLabel("Password"));
			try {
				ArrayList<String> userRecord = admin1.checkAvailableUser(); // get an ArrayList of data of available client
				
				for (String line: userRecord) { // adding the client's data onto the display panel
					String[] parts = line.split("\\|");
					tempGridPanel.add(new JLabel(parts[2]));
					tempGridPanel.add(new JLabel(parts[0]));
					tempGridPanel.add(new JLabel(parts[1]));
				}
				
				if (userRecord.size() == 0) { // message to inform admin that no client data available
					JOptionPane.showMessageDialog(null, "No client record is found.", "Message", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			displayPanel1 = new JScrollPane(tempGridPanel);
			displayPanel1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			frCheck.getContentPane().add(displayPanel1);
			
			// adding a back button to return to administrator menu
			backBt1 = new JButton("Back");
			backBt1.addActionListener(this);
			tempPanel = new JPanel();
			tempPanel.add(backBt1);
			frCheck.getContentPane().add(tempPanel, BorderLayout.SOUTH);
			frCheck.pack();
			BudgetTracker.centerFrame(frCheck);
			frCheck.setVisible(true);
			BudgetTracker.setCloseCurrentFrame(frCheck);
		}
		
		else if (e.getSource() == backBt1) {
			frCheck.dispose();
			frAdmin.setVisible(true); // set the administrator menu visible
		}
		
		else if (e.getSource() == resetBt) {
			// create an object of admin with the details entered in the login field
			Admin admin1 = new Admin(username.getText(), new String(password.getPassword()));
			
			// adding title to reset client password frame
			frReset = new JFrame("Budget Tracker Admin");
			frReset.setIconImage(new ImageIcon("logo.png").getImage());
			title4 = new JLabel("Reset Client Password");
			title4.setFont(new Font("SansSerif", Font.BOLD, 30));
			JPanel tempPanel = new JPanel();
			tempPanel.add(title4);		
			frReset.getContentPane().add(tempPanel, BorderLayout.NORTH);
			
			// adding a display panel to display the client's information
			JPanel tempGridPanel = new JPanel(new GridLayout(0,3,35,10));
			tempGridPanel.add(new JLabel("Email"));
			tempGridPanel.add(new JLabel("Username"));
			tempGridPanel.add(new JLabel("Password"));
			try {
				ArrayList<String> userRecord = admin1.checkAvailableUser(); // get the ArrayList of data of registered client available
				
				for (String line: userRecord) { // adding the data onto the display panel
					String[] parts = line.split("\\|");
					tempGridPanel.add(new JLabel(parts[2]));
					tempGridPanel.add(new JLabel(parts[0]));
					tempGridPanel.add(new JLabel(parts[1]));
				}
				
				if (userRecord.size() == 0) { // display message when no client record is found
					JOptionPane.showMessageDialog(null, "No client record is found.", "Message", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			displayPanel2 = new JScrollPane(tempGridPanel);
			displayPanel2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			// adding text fields to get client information for resetting password
			JPanel tempBoxPanel = new JPanel();
			tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS)); 
			tempBoxPanel.add(displayPanel2);
			emailReset = new JTextField(20);
			usernameReset = new JTextField(20);
			passwordReset = new JPasswordField(20);
			tempGridPanel = new JPanel(new GridLayout(3,2,10,10));
			tempGridPanel.add(new JLabel("Email: "));
			tempGridPanel.add(emailReset);
			tempGridPanel.add(new JLabel("Username: "));
			tempGridPanel.add(usernameReset);
			tempGridPanel.add(new JLabel("New Password: "));
			tempGridPanel.add(passwordReset);
			tempPanel = new JPanel();
			tempPanel.add(tempGridPanel);
			tempBoxPanel.add(tempPanel);
			
			// adding back button to return to administrator menu
			tempPanel = new JPanel();
			backBt2 = new JButton("Back");
			backBt2.addActionListener(this);
			tempPanel.add(backBt2);
			resetBt1 = new JButton("Reset Password");
			resetBt1.addActionListener(this);
			tempPanel.add(resetBt1);
			tempBoxPanel.add(tempPanel);
			frReset.getContentPane().add(tempBoxPanel);

			frReset.pack();
			BudgetTracker.centerFrame(frReset);
			frReset.setVisible(true);
			BudgetTracker.setCloseCurrentFrame(frReset);
			
		}
		
		else if (e.getSource() == resetBt1) {
			// create an object of admin with the details entered in the login field
			Admin admin1 = new Admin(username.getText(), new String(password.getPassword()));
			
			// create an object of client with the username and email entered in the text fields in reset client password frame
			Client user1 = new Client(usernameReset.getText(), " ", emailReset.getText());
			try {
				if (user1.checkExistingClient() == Client.AccountExist) { // check if the account exist for the details entered and reset a new password
					admin1.helpResetPassword(new String(passwordReset.getPassword()), usernameReset.getText(), emailReset.getText());
				}
				else { // display an error message to notify no client account is found
					JOptionPane.showMessageDialog(null, "User not found! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (new String(passwordReset.getPassword()).contains("|")) { // prevent the character '|' stored in password to avoid future data retrieval error
				JOptionPane.showMessageDialog(null, "The character '|' is not allowed! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (new String(passwordReset.getPassword()).isBlank()) { // error message when password field is blank
				JOptionPane.showMessageDialog(null, "Password cannot be blank! Please fill in the password.", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			frReset.dispose();
			resetBt.doClick(); // acting as a tool to refresh the current frame to show the client data with new password
			
			// notification message to inform user that password has been reset
			ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
			JOptionPane.showMessageDialog(null, "The user password has been reset successfully", "Message", JOptionPane.INFORMATION_MESSAGE, img);
		}
		
		else if (e.getSource() == backBt2) {
			frReset.dispose();
			frAdmin.setVisible(true); // set the administrator menu frame visible to return to the menu
		}
		
		else if (e.getSource() == deleteBt) {
			Admin admin1 = new Admin(username.getText(), new String(password.getPassword()));
			
			// adding title to the delete client account frame
			frDelete = new JFrame("Budget Tracker Admin");
			frDelete.setIconImage(new ImageIcon("logo.png").getImage());
			title5 = new JLabel("Delete Client Account");
			title5.setFont(new Font("SansSerif", Font.BOLD, 30));
			JPanel tempPanel = new JPanel();
			tempPanel.add(title5);		
			frDelete.getContentPane().add(tempPanel, BorderLayout.NORTH);
			
			// adding a display panel to show client account data
			JPanel tempGridPanel = new JPanel(new GridLayout(0,3,35,10));
			tempGridPanel.add(new JLabel("Email"));
			tempGridPanel.add(new JLabel("Username"));
			tempGridPanel.add(new JLabel("Password"));
			try {
				ArrayList<String> userRecord = admin1.checkAvailableUser(); // get an ArrayList of data of registered client
				
				for (String line: userRecord) { // adding client data into display panel
					String[] parts = line.split("\\|");
					tempGridPanel.add(new JLabel(parts[2]));
					tempGridPanel.add(new JLabel(parts[0]));
					tempGridPanel.add(new JLabel(parts[1]));
				}
				
				if (userRecord.size() == 0) { // message to inform admin if no client record is found
					JOptionPane.showMessageDialog(null, "No client record is found.", "Message", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			displayPanel3 = new JScrollPane(tempGridPanel);
			displayPanel3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			//adding text fields to get client data for deleting their associated account
			JPanel tempBoxPanel = new JPanel();
			tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS)); 
			tempBoxPanel.add(displayPanel3);
			emailDelete = new JTextField(20);
			usernameDelete = new JTextField(20);
			tempGridPanel = new JPanel(new GridLayout(2,2,10,10));
			tempGridPanel.add(new JLabel("Email: "));
			tempGridPanel.add(emailDelete);
			tempGridPanel.add(new JLabel("Username: "));
			tempGridPanel.add(usernameDelete);
			tempPanel = new JPanel();
			tempPanel.add(tempGridPanel);
			tempBoxPanel.add(tempPanel);
			
			// adding a back button to return to administrator menu
			tempPanel = new JPanel();
			backBt3 = new JButton("Back");
			backBt3.addActionListener(this);
			tempPanel.add(backBt3);
			deleteBt1 = new JButton("Delete User");
			deleteBt1.addActionListener(this);
			tempPanel.add(deleteBt1);
			tempBoxPanel.add(tempPanel);
			frDelete.getContentPane().add(tempBoxPanel);
			
			frDelete.pack();
			BudgetTracker.centerFrame(frDelete);
			frDelete.setVisible(true);
			BudgetTracker.setCloseCurrentFrame(frDelete);
		}
		
		else if (e.getSource() == deleteBt1) {
			// create an object of admin with the details entered in the login field					
			Admin admin1 = new Admin(username.getText(), new String(password.getPassword()));
			
			// create an object of client with the username and email entered in the text fields in reset client password frame
			Client user1 = new Client(usernameDelete.getText(), " ", emailDelete.getText());
			try {
				if (user1.checkExistingClient() == Client.AccountExist) { // check if the client account exist and delete the client account
					admin1.deleteUser(usernameDelete.getText(), emailDelete.getText());
				}
				else { // message to notify admin if client account is not found
					JOptionPane.showMessageDialog(null, "User not found! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frDelete.dispose();
			deleteBt.doClick(); // acting as a tool to refresh the frame to show the client data has been removed
			
			// display a message to notify admin that account has been deleted
			ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
			JOptionPane.showMessageDialog(null, "The user record has been successfully deleted", "Message", JOptionPane.INFORMATION_MESSAGE, img);
		}
		
		else if (e.getSource() == backBt3) {
			frDelete.dispose();
			frAdmin.setVisible(true); // set the administrator menu frame visible
		}
		
		else if (e.getSource() == logOutBt) { // closes the admin frame and redirects user to main login page
			frAdmin.dispose();
			new JFrameLogin(); 
		}
	}

}
