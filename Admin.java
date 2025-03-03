import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User{
	
	public Admin(String username,String password) {
		super(username,password);  //call the parent constructor to initialize username and password 
	}
	
	@Override
	public boolean verifyUser() {   //this method is used to verify validity of the user whether they have to correct username and password 
		return (super.getUsername().equals("admin") && super.getPassword().equals("admin"));   //default password and username for admin user is "admin" since we only have username and password for admin
	}
	
	//this is used to loop through user data file to get all record/data of all available user
	public ArrayList<String> checkAvailableUser() throws IOException{  		
		File file = new File("userData.txt");
		ArrayList<String> record = new ArrayList<String>();
		if (file.exists()) {
			Scanner inputFile = new Scanner(file);
			while(inputFile.hasNext()) {
				String line = inputFile.nextLine();
				record.add(line);
			}
			inputFile.close();
		}
		return record; //returning an ArrayList that contain all user data or available user
	}
	
	//this method is created used to reset password of user, this is used by admin
	public void helpResetPassword(String userNewPassword,String userUsername,String userEmail) throws IOException { 
		File file = new File("userData.txt");
		Scanner inputFile = new Scanner(file);
		ArrayList<String> record = new ArrayList<String>(); //this ArrayList is used to temporary store all user data in order to update the password for a specific user
		while(inputFile.hasNext()) {
			String line = inputFile.nextLine();
			String[]  parts = line.split("\\|");  //split the string input from the file by the delimiter "|" into an array
            String correctUsername =parts[0].trim(); //get the username from the array
            String correctEmail = parts[2].trim(); //get the password from the array
            if (userUsername.equals(correctUsername)) { //used to check whether the input username from the file is equals to what admin has entered
            	super.setPassword(userNewPassword);   //since username equals to what admin has entered we set the new password for the username and add it into the arrayList
            	String newRecord = String.format("%s|%s|%s", userUsername, super.getPassword(), userEmail);
        		record.add(newRecord);
            }
            else {   //if the input username from the file is not equals to what admin has entered it will be added into the arraylist
            	record.add(line);
            }
		}
		inputFile.close();
		
		PrintWriter outputFile = new PrintWriter("userData.txt");       
		//we loop the arraylist that contain all user data with the updated password user to rewrite all user data into the userData.txt file
		for(String i:record) {    
			outputFile.println(i);
		}
		outputFile.close();
		
	}
	
	
	//this is used to delete particular user in the user data file/to remove particular available user
	public void deleteUser(String userUsername,String userEmail) throws IOException{
		File file = new File("userData.txt");
		Scanner inputFile = new Scanner(file);
		ArrayList<String> record = new ArrayList<String>();
		while(inputFile.hasNext()) {
			String Line = inputFile.nextLine();
			String[]  parts = Line.split("\\|"); //we split the string input from the file by the delimiter "|" into an array
            String correctUsername =parts[0].trim(); //get the username from the array
            String correctEmail = parts[2].trim(); //get the email from the array
            //if the username and email from the file matched what admin has entered we will delete it
            if (userUsername.equals(correctUsername) && userEmail.equals(correctEmail)) {
            		String userData = String.format("%s_ExpenseAndIncomeRecord.txt",userUsername);
            		File userDataFile = new File(userData);
            		userDataFile.delete();

            }
            
            //if the username and email from the file does not matched we will just add it into the temporary array list and save it back to the file later
            else {
            	record.add(Line);
            }
		}
		inputFile.close();	
		
		PrintWriter outputFile = new PrintWriter("userData.txt");
		//we loop the arraylist that contain user data without the deleted user to update the userData.txt file
		for(String i: record) {
			outputFile.println(i);
		}
		outputFile.close();
	}

}

