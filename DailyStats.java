import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DailyStats extends Statistics {

	private String day;
	private String month;
	private String year;
	String userDataFileName; //this userDataFileName contains the name of the data record .txt file for the specific user that store record of expenses and incomes
	
	public DailyStats(String day,String month, String year,String userDataFileName) {
		this.userDataFileName = userDataFileName;
		this.day = day;
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
			String[] parts = line.split("\\|"); //split the string from the input file by the delimiter ("|")
			String date = parts[5]; //taking the date out from the split string
			
			//this is used to filter the record saved, in order to only get the record on that specific date but not other date
			if (day.equals(date.substring(0,2)) && month.equals(date.substring(3, 5)) && year.equals(date.substring(6))) {
				records.add(line);
			}
		}
		inputFile.close();
    
		//looping the ArrayList that contain only record for that specific date to calculate the statistical information
        for (int i=0; i < records.size(); i++) {
        	String[] line = records.get(i).split("\\|");
        	String type = line[1];
        	String category = line[2];
        	double amount = Double.parseDouble(line[4]);
        	String date = line[5];
        	
        	//getting and saving all statistical information
        	if (type.equals("Income")) {
        		totalIncomes += amount;
        		//to save specific details for income category (used for the breakdown of the total incomes)
        		//(e.g. whether the total incomes are come from full-time work, part-time or bonus and etc)
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
        		//to save specific details for expense category (used for the breakdown of the total expenses)
        		//(e.g. whether the total Expenses are come from food, social life or pets and etc)
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
	}

}
