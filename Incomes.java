import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Incomes {
	
	private String date; 
	private IncomeType type;
	private double amount; 
	private String desc; 
	
	//accessor or getter method
	public String getDate() {
		return date;
	}
	public IncomeType getType() {
		return type;
	}
	public double getAmount() {
		return amount;
	}
	public String getDesc() {
		return desc;
	}
	
	public Incomes(String date, IncomeType type, double amount, String desc) {
		this.date = date;
		this.type = type;
		this.amount = amount;
		this.desc = desc;
	}
	
	//used to add incomes to the file storing all the record for that specific client
	public void addIncomes(String userDataFileName) throws IOException { //userDataFileName store the client record file .txt file name (the file store all the expenses and incomes record)
		File file = new File(userDataFileName);
		int numberOfData = 0; //number of data(means the specific number of data assigned to each record) are used assign number to each record added
		if(file.exists()) {
			Scanner inputFile = new Scanner(file);
				while(inputFile.hasNext()) {
					String Line = inputFile.nextLine();
					String[]  parts = Line.split("\\|");
		            numberOfData = Integer.parseInt(parts[0].trim()); //take the number of the last record it will then +1 to assign a number to the new record to be added
				}
		}
		else {
			numberOfData = 0;
		}

		if (numberOfData == 0) { //if the client record file does not exists, we create it and store the record into it
			PrintWriter outputFile = new PrintWriter(userDataFileName);
			outputFile.printf("%d|Income|%s|-|%.2f|%s|%s\n",numberOfData+1,this.type.getType(),this.amount,this.date,this.desc); //storing the record
			outputFile.close();
		}
		else { //if the client record file exists, we update/append the record 
			FileWriter fw = new FileWriter(userDataFileName,true);
			PrintWriter outputFile = new PrintWriter(fw);
			outputFile.printf("%d|Income|%s|-|%.2f|%s|%s\n",numberOfData+1,this.type.getType(),this.amount,this.date,this.desc); //storing the record
			outputFile.close();
		}

		
	}
	
	//this method is used to modify existing incomes
	public void modifyIncomes(String number,String userDataFileName) throws IOException{
		File file = new File(userDataFileName);
		Scanner inputFile = new Scanner(file);
		ArrayList<String> record = new ArrayList<String>(); //using arraylist to temporary store the data from the .txt file in case to modify incomes/expenses
		while(inputFile.hasNext()) {
			String Line = inputFile.nextLine();
			String[]  parts = Line.split("\\|"); //splitting the string input from the file by delimiter ("|") into String array
            String numberOfData = parts[0].trim(); //getting the number associated with each record
            if (numberOfData.equals(number)) { //if the number associated with the record matched the record that client want to modify we will then update the record
            	record.add(String.format("%s|Income|%s|-|%.2f|%s|%s",number,this.type.getType(),this.amount,this.date,this.desc)); //add the updated record to the temporary arraylist
            }
            else {
            	record.add(Line); //we will add the record that are not matched to what the client want to modify into the arraylist
            }
		}
		inputFile.close();
		PrintWriter outputFile = new PrintWriter(userDataFileName);
		//looping the arraylist to save all the record including the modified record back to the data .txt file
		for(String i:record) {
			outputFile.println(i);
		}
		outputFile.close();
	}

}
