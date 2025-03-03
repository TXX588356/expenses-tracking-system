import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Expenses {
	
	private Category cat; 
	private PaymentMethod payMet; 
	private double amount; 
	private String date, desc; 
	
	//accessor or getter method
	public Category getCat() {
		return cat;
	}
	public PaymentMethod getPayMet() {
		return payMet;
	}
	public double getAmount() {
		return amount;
	}
	public String getDate() {
		return date;
	}
	public String getDesc() {
		return desc;
	}
	
	public Expenses(Category cat, PaymentMethod payMet, double amount, String date, String desc) {
		this.cat = cat;
		this.payMet = payMet;
		this.amount = amount;
		this.date = date;
		this.desc = desc;
	}
	
	//used to add expenses to the file storing all the record for that specific client
	public void addExpenses(String userDataFileName) throws IOException {  
		File file = new File(userDataFileName);
		int numberOfData = 0; //the specific number of data assigned to each record and used to assign number to each record added
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
		
		if (numberOfData == 0) {  //if the client record file does not exists, we create it and store the record into it
			PrintWriter outputFile = new PrintWriter(userDataFileName);
			outputFile.printf("%d|Expense|%s|%s|%.2f|%s|%s\n",numberOfData+1,this.cat.getType(),this.payMet.getType(),this.amount,this.date,this.desc); //storing the record
			outputFile.close();
		}
		else { //if the client record file exists, we update/append the record 
			FileWriter fw = new FileWriter(userDataFileName,true);
			PrintWriter outputFile = new PrintWriter(fw);
			outputFile.printf("%d|Expense|%s|%s|%.2f|%s|%s\n",numberOfData+1,this.cat.getType(),this.payMet.getType(),this.amount,this.date,this.desc); //storing the record
			outputFile.close();
		}

	}
	
	//this method is used to modify existing expenses
	public void modifyExpenses(String number,String userDataFileName) throws IOException{
		File file = new File(userDataFileName);
		Scanner inputFile = new Scanner(file);
		ArrayList<String> record = new ArrayList<String>(); //using ArrayList to temporary store the data from the .txt file in case to modify incomes/expenses
		while(inputFile.hasNext()) {
			String Line = inputFile.nextLine(); 
			String[]  parts = Line.split("\\|");
            String numberOfData = parts[0].trim();  //getting the number associated with each record
            if (numberOfData.equals(number)) {  //if the number associated with the record matched the record that client want to modify we will then update the record
            	record.add(String.format("%s|Expense|%s|%s|%.2f|%s|%s",number,this.cat.getType(),this.payMet.getType(),this.amount,this.date,this.desc)); //add the updated record to the temporary arraylist
            }
            else {
            	record.add(Line); //we will add the record that are not matched to what the client want to modify into the arraylist
            }
		}
		inputFile.close();
		PrintWriter outputFile = new PrintWriter(userDataFileName);
		//looping the ArrayList to save all the record including the modified record back to the data .txt file
		for(String i:record) {
			outputFile.println(i);
		}
		outputFile.close();
	}
}
