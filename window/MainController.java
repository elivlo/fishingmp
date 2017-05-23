package window;

import main.ClientThread;
import main.Main;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {
	
	public Socket socket;

	public TextField chatInput;
	public TextField inputField;
	public TextArea chatOutput;
	public Label wrongSyntax;
	public Label statsLabel;
	
	static PrintWriter out;
	
	
	public MainController() {
		Task<Void> task = new Task<Void>() {
		    @Override
		    public Void call() {
		        
		    	
		    	try {
			        BufferedReader in = new BufferedReader(new InputStreamReader(Main.socket.getInputStream()));
			        PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			        String output;
			        String [] clientInput;
			        
			        
			        while(true){
			          
			          output = in.readLine();
			          clientInput = output.split("~");
			          
			          switch (clientInput[0]) {
			          
			          	case "SAY":
			          		chatOutput.setText(chatOutput.getText() +clientInput[1]+"\n");
			          		break;
			          	
			        	case "DIALOG":
			        		wrongSyntax.setText(""+clientInput[1]+"\n");
			          		break;
			          		
			        	case "STATSS":
			        		statsLabel.setText(""+clientInput[1]);
			          		break;
			        	  
			          }
			         
			          out.write("STATS \n");
			          out.flush();
			          
			          
			          
			        }
			        
			      } catch (IOException e) {
			        e.printStackTrace();
			      }
		    	
		    return null;	
		    }
		};
		
		
		
		
		
		new Thread(task).start();
    }
	
	
	
	
	
	public static void setPrintWriter(){
		try {
			PrintWriter out1 = new PrintWriter(Main.socket.getOutputStream());
			out=out1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	//MenuBar mainframe
	
	
	
	//Buttons mainframe

	public void sendChat(){
			out.write("SAY "+ chatInput.getText() +"\n");
			out.flush();
			chatInput.clear();
		
	}
	

	public void fishAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("FISH "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buyAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("BUY "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sellGAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("SELL "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sellNAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("SELL "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mineAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("MINE "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("STATS \n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pauseAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("PAUSE "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unpauseAction(){
		try {
			PrintWriter out = new PrintWriter(Main.socket.getOutputStream());
			out.write("UNPAUSE "+ chatInput.getText() +"\n");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	
	
	
	

}
