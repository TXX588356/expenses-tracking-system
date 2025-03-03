import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;

public class JFrameFiltering implements ActionListener{

	private String date;
	private String type;
	
	// declaring main GUI components to be used
	JFrame fr;
	JButton backBt;
	JScrollPane allDisplay;
	JPanel displayPanel;
	String userDataFileName; // the file name of client's expense and income record based on the username in current login session
	
	// designing the GUI by managing the frame layout and displaying the components
	public JFrameFiltering (String date, String type,String userDataFileName) throws FileNotFoundException {
		this.userDataFileName = userDataFileName;
		this.date = date;
		this.type = type;
		
		// adding frame title and image icon
		fr = new JFrame("Filtering Record");
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// calculate total number of record available in expense and income record
		File file = new File(userDataFileName);
		int numberOfData = 0;
		if(file.exists()) {
			Scanner inputFile = new Scanner(file);
			while (inputFile.hasNext()) {
				inputFile.nextLine();
	            numberOfData++;
			}
			inputFile.close();		
		}
		
		// adding the components to display panel
		displayPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(0, 7, 26, 8));
		displayPanel.add(new JLabel("No."));
		displayPanel.add(new JLabel("Type"));
		displayPanel.add(new JLabel("Category"));
		displayPanel.add(new JLabel("Payment Method"));
		displayPanel.add(new JLabel("Amount"));
		displayPanel.add(new JLabel("Date"));
		displayPanel.add(new JLabel("Description"));
		
		boolean isDisplayed = false;
		
		if (numberOfData > 0 ) { // adding data into the display panel based on different case according to input received from user
	
			Scanner inputFile = new Scanner(file);
	
			if (date.isBlank() && type.equals("All")) { // case 1
				while(inputFile.hasNext())
				{
					String Line = inputFile.nextLine();
					String[]  parts = Line.split("\\|");
					for (int i = 0; i <= 6; i++) {
						displayPanel.add(new JLabel(parts[i]));
						isDisplayed = true;
					}
				}
			}
			
			else if (!date.isBlank() && !type.equals("All")) { // case 2
				while(inputFile.hasNext())
				{
					String Line = inputFile.nextLine();
					String[]  parts = Line.split("\\|");
					if (parts[5].equals(date)  && parts[1].equals(type)) {
						for (int i = 0; i <= 6; i++) {
							displayPanel.add(new JLabel(parts[i]));
							isDisplayed = true;
						}
					}
				}
			}
			
			else if (!date.isBlank() && type.equals("All")) { // case 3
				while(inputFile.hasNext())
				{
					String Line = inputFile.nextLine();
					String[]  parts = Line.split("\\|");
					if (parts[5].equals(date)) {
						for (int i = 0; i <= 6; i++) {
							displayPanel.add(new JLabel(parts[i]));
							isDisplayed = true;
						}
					}	
				}
			}
			
			else if (date.isBlank() && !type.equals("All")) { // case 4
				while(inputFile.hasNext())
				{
					String Line = inputFile.nextLine();
					String[]  parts = Line.split("\\|");
					if (parts[1].equals(type)) {
						for (int i = 0; i <= 6; i++) {
							displayPanel.add(new JLabel(parts[i]));
							isDisplayed = true;
						}
					}	
				}
			}
			
			inputFile.close();
		}
		
		if (!isDisplayed) { // display a message to notify user if no record is found
			JOptionPane.showMessageDialog(null, "No record is found.", "Message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		allDisplay = new JScrollPane(displayPanel);
		allDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// adding components onto the frame
		JPanel tempPanel;
		tempPanel = new JPanel();
		backBt = new JButton("Back");
		backBt.addActionListener(this);
		tempPanel.add(backBt);
		fr.getContentPane().add(tempPanel, BorderLayout.SOUTH);
		fr.getContentPane().add(allDisplay);
		fr.pack();
		BudgetTracker.centerFrame(fr);
		BudgetTracker.setCloseCurrentFrame(fr);
		fr.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backBt) {
			fr.dispose(); // close the current 'Filtering' frame
		}
	}

}
