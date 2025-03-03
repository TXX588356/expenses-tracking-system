import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class MonthlyStats extends Statistics {

	private String month;
	private String year;
	String userDataFileName; //this userDataFileName contains the name of the data record .txt file for the specific user that store record of expenses and incomes
	
	public MonthlyStats(String month, String year,String userDataFileName) {
		this.userDataFileName = userDataFileName;
		this.month = month;
		this.year = year;
	}
	
	//this method is used to calculate all the statistic information (e.g. which include total incomes,total expenses for that specific date)
	@Override
	public void calculateStatistics() throws FileNotFoundException {
		File file = new File(userDataFileName);
		Scanner inputFile = new Scanner(file);
		ArrayList<String> records = new ArrayList<String>();
		while (inputFile.hasNext()) {
			String line = inputFile.nextLine();
			String[] parts = line.split("\\|"); //splt the string from the input file by the delimiter ("|")
			String date = parts[5]; //taking the date out from the splitted string
			
			//this is used to filter the record saved, in order to only get the record on that specific month but not other month
			if (month.equals(date.substring(3, 5)) && year.equals(date.substring(6))) {
				records.add(line);
			}
		}
		inputFile.close();
		
		// sort the ArrayList based on the date in each record
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Define the date format used in the file
        
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
        

        //number of days in each month (january is in position 0,february in position 1 and so on)
        int[] numOfDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
      //looping the arraylist that contain only record for that specific month to calculate the statistical information
        for (int i=0; i < records.size(); i++) {
        	String[] line = records.get(i).split("\\|");
        	String type = line[1];
        	String category = line[2];
        	double amount = Double.parseDouble(line[4]);
        	
        	//getting and saving all statistical information
        	if (type.equals("Income")) {
        		totalIncomes += amount;
        		//to save specific details for income category (used for the breakdown of the total incomes)(e.g. whether the total incomes are come from full-time work, part-time or bonus and etc)
        		if (category.equals("Full-Time")){
        			fulltimeInc += amount;
        		} else if (category.equals("Part-Time")) {
        			parttimeInc += amount;
        		} else if (category.equals("Allowance")) {
        			allowanceInc += amount;
        		} else if (category.equals("Pocket Money")) {
        			pocketInc += amount;
        		} else if (category.equals("Bonus")) {
        			bonusInc += amount;
        		} else if (category.equals("Other")) {
        			otherInc += amount;
        		}
        	} else if (type.equals("Expense")) {
        		totalExpenses += amount;
        		//to save specific details for expense category (used for the breakdown of the total expenses)(e.g. whether the total Expenses are come from food, social life or pets and etc)
        		if (category.equals("Food")) {
        			foodExp += amount;
        		} else if (category.equals("Social Life")) {
        			socialExp += amount;
        		} else if (category.equals("Pets")) {
        			petsExp += amount;
        		} else if (category.equals("Transport")) {
        			transportExp += amount;
        		} else if (category.equals("Culture")) {
        			cultureExp += amount;
        		} else if (category.equals("Household")) {
        			householdExp += amount;
        		} else if (category.equals("Apparel")) {
        			apparelExp += amount;
        		} else if (category.equals("Beauty")) {
        			beautyExp += amount;
        		} else if (category.equals("Health")) {
        			healthExp += amount;
        		} else if (category.equals("Education")) {
        			educationExp += amount;
        		} else if (category.equals("Gift")) {
        			giftExp += amount;
        		} else if (category.equals("Other")) {
        			otherExp += amount;
        		}
        	}
        }
		
        //calculating the daily average incomes and expenses based on the specific month by dividing the total of incomes/expenses with the total days in that specific months
        dailyAverageInc = totalIncomes / numOfDays[Integer.parseInt(month)-1];
        dailyAverageExp = totalExpenses / numOfDays[Integer.parseInt(month)-1];
        netBal = totalIncomes - totalExpenses; //nett balance means total incomes minus total expenses
	}
}
