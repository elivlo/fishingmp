package fishClientconsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;





public class FishClient {
  
  static boolean akzeptanz = false;
  static Socket socket;
  static Scanner inputsc;
  
    public static void main(String[] args) throws IOException {
                                                                               
    try {
      socket = new Socket("127.0.0.1",8080);
        
      BufferedReader inputsc = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream()); 
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
      String nickname;
      
      System.out.print("Nickname: ");
      nickname = inputsc.readLine() + "\n";
      out.write(nickname);
      out.flush();  
      
      System.out.println("FISH, BUY, SELL, STATS, leave");
      System.out.println("----------------------------");
      
      
      new Thread(new serverInput()).start();
      new Thread(new serverOutput()).start();
      
      
      

      
    } catch(Exception e) {
      e.printStackTrace();
    }
    
       
        
        
    }
    
    static class serverOutput implements Runnable{
        public void run() {
          String[] input;
            String toserver;
          try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream()); 
        while (true) {
              
              toserver = in.readLine();
              
              if (toserver=="leave"){
                  out.close();
                  System.exit(0);
              } else{
                out.write(toserver+"\n");
                out.flush();
              }

              
            }
        
      } catch (IOException e) {
        e.printStackTrace();
      }
        }
    }
    
    static class serverInput implements Runnable{
        public void run() {
          try {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String output;
        
        while(true){
          output = in.readLine();
          System.out.println(output);
          
          
        }
        
      } catch (IOException e) {
        e.printStackTrace();
      }
        }
    }
   
}




