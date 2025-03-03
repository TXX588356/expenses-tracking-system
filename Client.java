import java.io.*;
import java.util.Scanner;

public class Client extends User{
	
	private String email;
	
	//these static final variables are used to initialize constant value and to be used in checkExistingClient() method below
	//refers to checkExistingClient() for more information above its usage
	public static final int EmailExist = 0;  
	public static final int UsernameExist = 1;
	public static final int AccountExist = 2;
	public static final int AccountNotExist = 3;
	
	public Client(String username,String password,String email) {
		super(username,password); //call the parent class constructor to initialize username and password
		this.email = email;
	}
	
	//this method is used to verify the validity of the client (whether it is a valid client with the correct password/username or not)
	@Override
	public boolean verifyUser() {
		boolean validity = false;   
		File file = new File("userData.txt"); //the file contains all the information of all client (including all username and password of all client)
		//looping the client data file to compare the username and password entered by the client with the username and password from the data file
		if (file.exists()) {
			Scanner inputFile = null;
			try {
				inputFile = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(inputFile.hasNext()) {
				String line = inputFile.nextLine();
				String[]  parts = line.split("\\|"); //split the string into parts with delimiter ("|")
	            String correctUsername =parts[0].trim();  //get the correct username from the string (input from the file)
	            String correctPassword = parts[1].trim(); //get the correct password from the string (input from the file)
	            if (super.getUsername().equals(correctUsername)) {   //compare the username and password from the file with what is entered by the client
	            	if(super.getPassword().equals(correctPassword)) {
	            		validity = true;   //if the username and password entered by the client matched with the username and password input from the data file means the user is valid
	            	}
	            }
			}
			inputFile.close();
		}
		return validity; 
	}
	
	//this method is used to check whether the username or password exist in the data file, this is mainly used when client are creating account
	public int checkExistingClient() throws IOException {
		File file = new File("userData.txt");
		if(file.exists()) {
			Scanner inputFile = new Scanner(file);
			while(inputFile.hasNext()) {
				String line = inputFile.nextLine();
				String[]  parts = line.split("\\|");
				String existingUsername =parts[0].trim();
				String existingEmail = parts[2].trim();
				if (email.equals(existingEmail) && super.getUsername().equals(existingUsername)) {   //if the username and email both exists in the data file we then return that the account exist (which will be printed out by jframe) to inform the client
					return AccountExist;
				}
				else if (email.equals(existingEmail)) {  //if the only email existed we then return that the email is existed in the data file (which will then be printed in jframe) to inform the client to reenter another email
					return EmailExist;
				}
				else if (super.getUsername().equals(existingUsername)) { //if the only username existed we then return that the username is existed in the data file (which will then be printed in jframe) to inform the client to reenter another username
					return UsernameExist;
				}
			}
			inputFile.close();
		}
		return AccountNotExist; //if both email and username does not exists, inform client that their account are succesfully created
	}
	
	
	//method used to create account for client
	public void createAccount() throws IOException {
		File file = new File("userData.txt"); //the file used to store all client data (including password email username)
		//if the file exists we will update the file with new client information
		if(file.exists()) {
			FileWriter fw = new FileWriter("userData.txt", true);
			PrintWriter outputFile = new PrintWriter(fw);
			outputFile.printf("%s|%s|%s\n",super.getUsername(), super.getPassword(), email);
			outputFile.close();
		}
		//if the file does not exists, we will then create an new text file to store the new client information
		else {
			PrintWriter outputFile = new PrintWriter("userData.txt");
			outputFile.printf("%s|%s|%s\n", super.getUsername(), super.getPassword(), email);
			outputFile.close();
		}
		//the record file for the specific user, this file is used to store the budget tracker(expenses and income)data for respective user
		PrintWriter newRecordFile = new PrintWriter(new File(String.format("%s_ExpenseAndIncomeRecord.txt", super.getUsername())));  
		newRecordFile.close();
		
	}
	
}
