package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class FishServer {
    int port = 8080;
    static boolean listening = true;

    Market market1;
    static FishPond pond;
    static List <FishServerThread> playerThread = new ArrayList();
    static PrintWriter [] writer = new PrintWriter[100];

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
        		playerThread.add(new FishServerThread(socket, server));
                playerThread.get(threadCount).start();
                writer[threadCount] = new PrintWriter(socket.getOutputStream());
                threadCount++;
                
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } 
    }

    static class ConsoleController implements Runnable{
        @Override
        public void run() {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
                String input;
                while(true){
                	input = bufferedReader.readLine();
                	if(Objects.equals(input, "stop")){
                        FishServer.listening = false;
                        System.out.println("stop");
                        //Server schickt meldung an clients
                        Thread.sleep(5000);
                        System.out.println("Server closed!");
                        break;
                    }
                    else if(Objects.equals(input, "fish")){
                        fish();
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