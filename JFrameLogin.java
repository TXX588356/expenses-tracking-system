import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class JFrameLogin implements ActionListener{

	// declaring main GUI components to be used
	JFrame fr;
	JLabel title;
	JTextField username;
	JPasswordField password;
	JButton loginBt, adminBt, createBt;
	String userDataFileName;  // the file name of client's expense and income record based on the username in current login session
	
	public JFrameLogin () { // designing the GUI by managing the frame layout and displaying the components
		// adding frame title and image icon
		fr = new JFrame("Budget Tracker");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// adding title
		title = new JLabel("Budget Tracker Login");
		title.setFont(new Font("SansSerif", Font.BOLD, 30));
		JPanel tempPanel = new JPanel();
		tempPanel.add(title);		
		fr.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		// adding text fields and button for login
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
		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Forgot Password? Please contact admin at budgetTracker@gmail.com"));
		tempBoxPanel.add(tempPanel);
		tempBoxPanel.add(new JLabel(" "));
		fr.getContentPane().add(tempBoxPanel);
		
		// adding buttons
		adminBt = new JButton("Login as Admin");
		adminBt.addActionListener(this);
		createBt = new JButton("Create new account");
		createBt.addActionListener(this);
		tempGridPanel = new JPanel(new GridLayout(1,2,80,0));
		tempGridPanel.add(adminBt);
		tempGridPanel.add(createBt);
		fr.getContentPane().add(tempGridPanel, BorderLayout.SOUTH);
		
		fr.pack();
		BudgetTracker.centerFrame(fr);
		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == loginBt) {
			// display error message if username and/or password field is blank
			if (username.getText().isBlank() || new String(password.getPassword()).isBlank()) {
				JOptionPane.showMessageDialog(null, "Please enter a valid username and password!", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// create an object of client with the username and password entered
			Client user1 = new Client(username.getText(), new String(password.getPassword()), " ");
			if (user1.verifyUser()) { // check if the client username and password entered is valid and correct
				try { // close the login frame and directs to main menu
					userDataFileName = String.format("%s_ExpenseAndIncomeRecord.txt",username.getText());
					new JFrameOverview(userDataFileName);
					fr.dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// display a message to inform client that login is successful
				ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
				JOptionPane.showMessageDialog(null, "Login successful!", "Message", JOptionPane.INFORMATION_MESSAGE, img);
			}
			else {
				
				try {
					if (user1.checkExistingClient() == Client.AccountNotExist) { // check if the account exist or not
						// display error message to inform that username has not been registered
						JOptionPane.showMessageDialog(null, "Username entered has not been registered! Please try again or create a new account.", 
								"Reminder", JOptionPane.ERROR_MESSAGE);
						return;
					} else { 
						// display error message to inform that password entered is incorrect if username entered exist
						JOptionPane.showMessageDialog(null, "Incorrect password entered for Username: " + username.getText(), 
								"Reminder", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		else if (e.getSource() == adminBt) {
			// close the current login frame and directs to admin login page
			fr.dispose();
			new JFrameAdmin();
		}
		else if (e.getSource() == createBt) {
			// close the current login frame and directs to account registration page
			fr.dispose();
			new JFrameRegistration();
		}
	}
	
}
