package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class FishServer {
  int port = 8080;
  static boolean listening = true;
  
  static Market market1;
  static FishPond pond;
  static List <FishServerThread> playerThread = new ArrayList();
  static PrintWriter [] writer = new PrintWriter[100];
  static Player [] players = new Player[100];
    

  static ConcurrentMap<Player, Double> fishingRates = new ConcurrentHashMap<>();
  static int playerCount = 0;

  public FishServer(Market market, FishPond pond) {
    this.market1 = market;
    this.pond = pond;
  }

  void addFishing(Player player, double amount){
    fishingRates.put(player,amount);
  }

  static void fish(){
    pond.fish(fishingRates);
    fishingRates.clear();
  }

  public static void main(String[] args) {
    
    System.out.println("Server gestartet!");
    
    FishPond pond = new FishPond(100, 2000);
    Market market1 = new Market(10, 1);
    
    FishServer server = new FishServer(market1,pond);
    
    
    if(args.length >1){
      System.err.println("Usage: java FishServer [<port number>]");
      System.exit(1);
    }
    if(args.length == 1){
      server.port = Integer.parseInt(args[0]);
    }
    
    new Thread(new ConsoleController()).start();
    new Thread(new fishFlusher()).start();
    
    try(ServerSocket serverSocket = new ServerSocket(server.port)){
      int threadCount = 0;
      while(server.listening){
        Socket socket = serverSocket.accept();
        players[playerCount] = new Player("Player "+threadCount, 100, 0);
        playerThread.add(new FishServerThread(socket, server, players[playerCount]));
        playerThread.get(threadCount).start();
        writer[threadCount] = new PrintWriter(socket.getOutputStream());
        threadCount++;
        
      }
    }  catch (IOException e) {
      e.printStackTrace();
    } 
  }
  public static void save(String name) throws IOException {
    File dir = new File(name);
    if (dir.mkdir()) {
      System.out.println("Save could be created!");
      saveDats(name);
    }
    else {
      System.out.println("Save already exists");
      System.out.println("Do you want to overide it? (y/n): ");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      String input = bufferedReader.readLine();
      if (input.equals("y")) {
        saveDats(name);
      } // end of if
    }
  }
  public static void saveDats(String name) throws IOException {
    File out [] = new File[100];
    BufferedWriter [] saver = new BufferedWriter[100];
    String files;
    int count = 0;
    for (int a=0;a<playerCount;a++) {
      files = (name + "/" + "Player" + count + ".txt");
      out[count] = new File(files);
      out[count].createNewFile();
      saver[count] = new BufferedWriter(new FileWriter(files));
      saver[count].write(players[a].name + "\r\n");
      saver[count].write(Double.toString(players[a].cash) + "\r\n");
      saver[count].write(Double.toString(players[a].fish) + "\r\n");
      saver[count].flush();
      count++;
    }
    File marketS = new File (name + "/Market.txt");
    File pondS = new File (name + "/Pond.txt");
    marketS.createNewFile();
    pondS.createNewFile();
    BufferedWriter bwM = new BufferedWriter(new FileWriter(name + "/Market.txt"));
    BufferedWriter bwP = new BufferedWriter(new FileWriter(name + "/Pond.txt"));
    
    bwM.write(market1.requestRate + "\r\n");
    bwP.write(pond.population + "\r\n");
    bwP.write(pond.growthFactor + "\r\n");
    bwP.write(pond.targetPopulation + "\r\n");
    bwM.flush();
    bwP.flush();
  }
  public static void load(String name) throws IOExeption {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String input = bufferedReader.readLine();
    for (int i=0;i<playerCount;i++) {
      if (players[i].name.equals(input)) {
        
      } // end of if
    } // end of for
  }  
    
  static class ConsoleController implements Runnable{
    @Override
    public void run() {
      try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
        String input;
        String [] ins = new String [2];
        while(true){
          input = bufferedReader.readLine();
          ins = input.split(" ");
          if(Objects.equals(ins[0], "stop")){
            FishServer.listening = false;
            System.out.println("Server stopped!");
            for (int b=0;b<playerThread.size();b++){
              
              writer[b].write(" Server will be closed in 5 seconds!\n");
              writer[b].flush();
            }
            Thread.sleep(5000);
            System.out.println("Server closed!");
            break;
          }
          else if(Objects.equals(ins[0], "fish")){
            fish();
          }
          else if(Objects.equals(ins[0], "save")){
            if (ins.length>1) {
              save(ins[1]);
            }
            else {
              System.out.println("Wrong syntax. Usage: save [SAVENAME]");
            }
          }
          else if(Objects.equals(ins[0], "load")){
            if (ins.length>1) {
              load(ins[1]);
            }
            else {
              System.out.println("Wrong syntax. Usage: load [LOADNAME]");
            }
          }
          System.exit(0);
        } catch (IOException e) {
        listening = false;
        e.printStackTrace();
        } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
        }
      }
      static class fishFlusher implements Runnable{
      @Override
      public void run() {
      while (true) {
      if (fishingRates.size()==playerCount && playerCount>0) {
      
      System.out.println(playerThread);
      
      for (int a=5;a>=0;a--){
      
      for (int b=0;b<playerThread.size();b++){
      
      writer[b].write("Fishing starts in " + a + " seconds!\n");
      writer[b].flush();
      }
      try {
      Thread.sleep(1000);
      } catch (InterruptedException e) {
      e.printStackTrace();
      }
      
      }
      fish();
      }
      }
      }
      }
    }