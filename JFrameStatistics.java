import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;

public class JFrameStatistics implements ActionListener{

	// declaring main GUI components to be used
	JFrame fr;
	JComboBox month;
	JTextField year;
	JButton findBt, backBt;
	JScrollPane allDisplay;
	JPanel displayPanel;
	String userDataFileName; // the file name of client's expense and income record based on the username in current login session
	String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"}; // an array of months
	boolean hasRecord; // to identify whether expense and income record exist for a selected month and year
	
	public JFrameStatistics(String userDataFileName) { // designing the GUI by managing the frame layout and displaying the components
		this.userDataFileName=userDataFileName;
		
		// adding frame title and image icon
		fr = new JFrame("Statistics Information");
		fr.setIconImage(new ImageIcon("logo.png").getImage());
		
		// adding a combo box, a text field and a button for filtering record
		month = new JComboBox(months);
		year = new JTextField(8);
		findBt = new JButton("Find");
		findBt.addActionListener(this);
		JPanel tempPanel = new JPanel();
		tempPanel.add(new JLabel("Month: "));
		tempPanel.add(month);
		tempPanel.add(new JLabel("Year: "));
		tempPanel.add(year);
		tempPanel.add(findBt);
		fr.getContentPane().add(tempPanel, BorderLayout.NORTH);
		
		// adding back button to main menu
		backBt = new JButton("Back");
		backBt.addActionListener(this);
		tempPanel = new JPanel();
		tempPanel.add(backBt);
		fr.getContentPane().add(tempPanel, BorderLayout.SOUTH);	
		
		fr.setSize(400,250);
		BudgetTracker.centerFrame(fr);
		BudgetTracker.setCloseCurrentFrame(fr);
		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == findBt) {
			if (allDisplay != null) {
				allDisplay.setVisible(false); // hide the display panel if it has already been defined
			}
			try {
				this.displayStatistics(); // display calculated statistics
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// add the display panel to the frame
			allDisplay = new JScrollPane(displayPanel); 
			allDisplay.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			displayPanel.setVisible(true);
			fr.getContentPane().add(allDisplay);
			fr.pack();	
			BudgetTracker.centerFrame(fr);
			
			if (!hasRecord) { // display a message to inform that no record is found for the selected month and year
				JOptionPane.showMessageDialog(null, "No record is found.", "Message", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		else if (e.getSource() == backBt) {
			fr.dispose(); // close the current 'Statistical Information' frame
		}
	}
	
	// this method is used to add all related components to display panel after calculating the statistical info
	public void displayStatistics() throws IOException {
		
		// create an object of 'MonthlyStats' using polymorphism with a 'Statistics' reference
		Statistics monthlyData = new MonthlyStats(months[month.getSelectedIndex()], year.getText(),userDataFileName);
		
		try {
			monthlyData.calculateStatistics(); // calculate the necessary statistical info based on the selected month
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		// adding components to the display panel
		displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		JPanel tempPanel = new JPanel();
		JLabel title1 = new JLabel("Monthly Statistics");
		title1.setFont(new Font("SansSerif", Font.BOLD, 25));
		tempPanel.add(title1);
		displayPanel.add(tempPanel);
		JPanel tempBoxPanel = new JPanel();
		tempBoxPanel.setLayout(new BoxLayout(tempBoxPanel, BoxLayout.Y_AXIS));
		
		// overall statistics for the selected month
		tempBoxPanel.add(new JLabel(String.format("Total Incomes: RM%.2f", monthlyData.totalIncomes)));
		tempBoxPanel.add(new JLabel(String.format("Total Expenses: RM%.2f", monthlyData.totalExpenses)));
		tempBoxPanel.add(new JLabel(String.format("Net Balance: RM%.2f", monthlyData.netBal)));
		tempBoxPanel.add(new JLabel(String.format("Daily Average Incomes: RM%.2f", monthlyData.dailyAverageInc)));
		tempBoxPanel.add(new JLabel(String.format("Daily Average Expenses: RM%.2f", monthlyData.dailyAverageExp)));
		tempBoxPanel.add(new JLabel(" "));
		tempPanel = new JPanel();
		tempPanel.add(tempBoxPanel);
		displayPanel.add(tempPanel);
		
		// income breakdown by category for the selected month
		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Total Incomes By: "));
		displayPanel.add(tempPanel);
		tempPanel = new JPanel();
		JPanel tempGridPanel = new JPanel (new GridLayout(2,6,15,0));
		tempGridPanel.add(new JLabel("Full-Time"));
		tempGridPanel.add(new JLabel("Part-Time"));
		tempGridPanel.add(new JLabel("Allowance"));
		tempGridPanel.add(new JLabel("Pocket Money"));
		tempGridPanel.add(new JLabel("Bonus"));
		tempGridPanel.add(new JLabel("Other"));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.fulltimeInc)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.parttimeInc)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.allowanceInc)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.pocketInc)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.bonusInc)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.otherInc)));
		tempPanel.add(tempGridPanel);		
		displayPanel.add(tempPanel);
		displayPanel.add(new JLabel(" "));
		
		// expenses breakdown by category for the selected month
		tempPanel = new JPanel();
		tempPanel.add(new JLabel("Total Expenses By: "));
		displayPanel.add(tempPanel);
		tempPanel = new JPanel();
		tempGridPanel = new JPanel (new GridLayout(2,12,20,0));
		tempGridPanel.add(new JLabel("Food"));
		tempGridPanel.add(new JLabel("Social Life"));
		tempGridPanel.add(new JLabel("Pets"));
		tempGridPanel.add(new JLabel("Transport"));
		tempGridPanel.add(new JLabel("Culture"));
		tempGridPanel.add(new JLabel("Household"));
		tempGridPanel.add(new JLabel("Apparel"));
		tempGridPanel.add(new JLabel("Beauty"));
		tempGridPanel.add(new JLabel("Health"));
		tempGridPanel.add(new JLabel("Education"));
		tempGridPanel.add(new JLabel("Gift"));
		tempGridPanel.add(new JLabel("Other"));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.foodExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.socialExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.petsExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.transportExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.cultureExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.householdExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.apparelExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.beautyExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.healthExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.educationExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.giftExp)));
		tempGridPanel.add(new JLabel(String.format("RM%.2f", monthlyData.otherExp)));
		tempPanel.add(tempGridPanel);		
		displayPanel.add(tempPanel);
		
		// get an ArrayList with the records available for the selected month
		File file = new File(userDataFileName);
		Scanner inputFile = new Scanner(file);
		ArrayList<String> records = new ArrayList<String>();
		while (inputFile.hasNext()) {
			String line = inputFile.nextLine();
			String[] parts = line.split("\\|");
			String date = parts[5];

			if (months[month.getSelectedIndex()].equals(date.substring(3, 5)) && year.getText().equals(date.substring(6))) {
				records.add(line);
			}
		}
		inputFile.close();
		
		// sort the ArrayList based on the date in each record
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Define the date format used in the date
        
        Collections.sort(records, new Comparator<String>() {  
        	// Sort the records ascending based on the date (e.g. 1/1/2023,2/1/2023,3/1/2023 and so on)
        	
            @Override
            public int compare(String record1, String record2) {
                String date1 = record1.split("\\|")[5];
                String date2 = record2.split("\\|")[5];
                
                LocalDate localDate1 = LocalDate.parse(date1, formatter);
                LocalDate localDate2 = LocalDate.parse(date2, formatter);
                
                return localDate1.compareTo(localDate2);
            }
        });
        
        // get an ArrayList of the date that has been sorted
        ArrayList<String> availableDate = new ArrayList<String>();
        for (int i=0; i < records.size(); i++) {
        	String[] line = records.get(i).split("\\|");
        	String date = line[5];
        	if (!(availableDate.contains(date))) {
        		availableDate.add(date);
        	}
        }
        
        // check whether there are any records for the selected month
        if (records.size() == 0) {
        	hasRecord = false;
		}
        else {
        	// if yes, display the title of 'Daily Statistics'
        	hasRecord = true;
        	tempPanel = new JPanel();
        	tempPanel.add(new JLabel(" "));
        	displayPanel.add(tempPanel);
        	tempPanel = new JPanel();
    		title1 = new JLabel("Daily Statistics");
    		title1.setFont(new Font("SansSerif", Font.BOLD, 25));
    		tempPanel.add(title1);
    		displayPanel.add(tempPanel);
        }
   	
        for(String i : availableDate) {
        	
        	// create an object of 'DailyStats' using polymorphism with a 'Statistics' reference
    		Statistics dailyData = new DailyStats(i.substring(0,2),months[month.getSelectedIndex()], year.getText(),userDataFileName);
    		
    		// calculate the necessary statistical info based on the selected month based on each date available
    		dailyData.calculateStatistics(); 
    		
    		// display the particular date
    		tempPanel = new JPanel();
    		tempGridPanel = new JPanel(new GridLayout(2,1,15,0));
    		tempGridPanel.add(new JLabel("    Date:"));
    		tempGridPanel.add(new JLabel(i));
    		tempPanel.add(tempGridPanel);		
    		displayPanel.add(tempPanel);
    		
    		// display statistics breakdown by income category for the particular date
    		tempPanel = new JPanel();
    		tempGridPanel = new JPanel (new GridLayout(2,8,15,0));
    		tempGridPanel.add(new JLabel("Total Income"));
    		tempGridPanel.add(new JLabel("Breakdown: "));	
    		tempGridPanel.add(new JLabel("Full-Time"));
    		tempGridPanel.add(new JLabel("Part-Time"));
    		tempGridPanel.add(new JLabel("Allowance"));
    		tempGridPanel.add(new JLabel("Pocket Money"));
    		tempGridPanel.add(new JLabel("Bonus"));
    		tempGridPanel.add(new JLabel("Other"));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.totalIncomes)));
    		tempGridPanel.add(new JLabel(" "));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.fulltimeInc)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.parttimeInc)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.allowanceInc)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.pocketInc)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.bonusInc)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.otherInc)));
    		tempPanel.add(tempGridPanel);		
    		displayPanel.add(tempPanel);
    		displayPanel.add(new JLabel(" "));
    		
    		// display the statistics breakdown by expense category for the particular date
    		tempPanel = new JPanel();
    		tempGridPanel = new JPanel (new GridLayout(2,14,20,0));
    		tempGridPanel.add(new JLabel("Total Expense"));
    		tempGridPanel.add(new JLabel("Breakdown: "));	
    		tempGridPanel.add(new JLabel("Food"));
    		tempGridPanel.add(new JLabel("Social Life"));
    		tempGridPanel.add(new JLabel("Pets"));
    		tempGridPanel.add(new JLabel("Transport"));
    		tempGridPanel.add(new JLabel("Culture"));
    		tempGridPanel.add(new JLabel("Household"));
    		tempGridPanel.add(new JLabel("Apparel"));
    		tempGridPanel.add(new JLabel("Beauty"));
    		tempGridPanel.add(new JLabel("Health"));
    		tempGridPanel.add(new JLabel("Education"));
    		tempGridPanel.add(new JLabel("Gift"));
    		tempGridPanel.add(new JLabel("Other"));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.totalExpenses)));
    		tempGridPanel.add(new JLabel(" "));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.foodExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.socialExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.petsExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.transportExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.cultureExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.householdExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.apparelExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.beautyExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.healthExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.educationExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.giftExp)));
    		tempGridPanel.add(new JLabel(String.format("RM%.2f", dailyData.otherExp)));
    		tempPanel.add(tempGridPanel);		
    		displayPanel.add(tempPanel);
    		
    		// add a dividing or separating line between the data for each date
    		tempPanel = new JPanel();
    		String dividingLine = "-".repeat(350);
    		tempPanel.add(new JLabel(dividingLine));
    		displayPanel.add(tempPanel);
        }
	}
}
