import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BudgetTracker {
	
	//the main method which is used to call the JFrameLogin (this first interface/GUI presented to user)
	public static void main (String[] args) {
		new JFrameLogin();  
	}
	
	
	//this method is used to ensure that the GUI Frame are displayed in the center of the screen
	public static void centerFrame(JFrame fr) {
		
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize(); //get screen size of user's device
		int screenWidth = screenSize.width; 
		int screenHeight = screenSize.height;
		//using x and y to locate the position to display the frame on the center of the screen
		int x = (screenWidth - fr.getWidth()) / 2;
		int y = (screenHeight - fr.getHeight()) / 2;
		//setting the display frame to display on the center of the screen
		fr.setLocation(x, y);
		
	}
	
	// this method is used to make sure a confirmation dialog is displayed when user clicks on the 'X' button on top right corner
	public static void confirmCloseFrame(JFrame fr) {
		
		fr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                
            	// confirm with user whether to close the application
                int userInput = JOptionPane.showConfirmDialog(fr, 
                    "Are you sure you want to exit the application?", "Confirm Exit", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                // close the window if the user clicks on 'Yes'
                if (userInput == JOptionPane.YES_OPTION) {
                	fr.dispose();  // close the window
                }
            }
        });
		
	}
	
	// this method is use to close a frame without terminating the whole system
	public static void setCloseCurrentFrame(JFrame fr) {
		
		fr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fr.dispose(); // close the current window
            }
        });
		
	}
	
}




