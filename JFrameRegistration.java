import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class JFrameRegistration implements ActionListener {
	
	// declaring main GUI components to be used
	JFrame fr;
	JLabel title;
	JTextField username, email;
	JPasswordField password;
	JButton createBt, backBt;
	
	public JFrameRegistration() { // designing the GUI by managing the frame layout and displaying the components
		// adding frame title and image icon
		fr = new JFrame("Budget Tracker");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// adding title
		title = new JLabel("Budget Tracker Account Registration");
		title.setFont(new Font("SansSerif", Font.BOLD, 25));
		JPanel tempPanel = new JPanel();
		tempPanel.add(title);		
		fr.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		// adding text fields and button for account registration
		email = new JTextField(20);
		username = new JTextField(20);
		password = new JPasswordField(20);
		JPanel tempGridPanel = new JPanel(new GridLayout(3,2,20,10));
		tempGridPanel.add(new JLabel("Email: "));
		tempGridPanel.add(email);
		tempGridPanel.add(new JLabel("Username: "));
		tempGridPanel.add(username);
		tempGridPanel.add(new JLabel("Password: "));
		tempGridPanel.add(password);
		tempPanel = new JPanel();
		tempPanel.add(tempGridPanel);
		JPanel tempBoxPanel = new JPanel();
		tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS));
		tempBoxPanel.add(tempPanel);
		createBt = new JButton("Create Account");
		createBt.addActionListener(this);
		backBt = new JButton("Back");
		backBt.addActionListener(this);
		tempPanel = new JPanel();
		tempPanel.add(backBt);
		tempPanel.add(createBt);
		tempBoxPanel.add(tempPanel);
		fr.getContentPane().add(tempBoxPanel);
		
		fr.pack();
		BudgetTracker.centerFrame(fr);
		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createBt) { 
			if (username.getText().isBlank() || new String(password.getPassword()).isBlank() || email.getText().isBlank()) {
				// display error message if username, password, and/or email is blank
				JOptionPane.showMessageDialog(null, "Please enter a valid email, username and password!", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!(email.getText().contains("@") && email.getText().contains(".com"))) {
				// display error message if email entered is invalid
				JOptionPane.showMessageDialog(null, "Please enter a valid email! (Hint: Contains '@' and '.com')", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (email.getText().contains("|") || username.getText().contains("|") || new String(password.getPassword()).contains("|")) {
				// prevent the character '|' to be stored in the file record to avoid future data retrieval error
				JOptionPane.showMessageDialog(null, "The character '|' is not allowed! Please try again", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (username.getText().contains("_")) {
				// prevent the character '_' to be included in the username to avoid future data retrieval error from file
				JOptionPane.showMessageDialog(null, "The character '_' is not allowed in username! Please try again", "Reminder", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// create an object of client with the username, password and email entered
			Client user1 = new Client(username.getText(), new String(password.getPassword()), email.getText());
			try {
				if (user1.checkExistingClient() == Client.EmailExist) {
					// display message to inform that email has been taken
					JOptionPane.showMessageDialog(null, "Email has already been taken! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if (user1.checkExistingClient() == Client.UsernameExist) {
					// display message to inform that username has been taken
					JOptionPane.showMessageDialog(null, "Username has already been taken! Please try again.", "Reminder", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if (user1.checkExistingClient() == Client.AccountNotExist) {
					user1.createAccount(); // create an account if email and username has not been taken
					
					// display a message to inform that account is created
					ImageIcon img = new ImageIcon(new ImageIcon("success.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
					JOptionPane.showMessageDialog(null, "Account successfully created", "Message", JOptionPane.INFORMATION_MESSAGE, img);
					
					// close the current 'Registration' frame and redirects to login page
					fr.dispose();
					new JFrameLogin();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == backBt) {
			// close the current 'Registration' frame and redirects to login page
			fr.dispose();
			new JFrameLogin();
		}
		
	}
}
