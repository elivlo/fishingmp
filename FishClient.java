package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Joachim on 02.04.2017.
 */
public class FishClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",10101);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        out.write("FISH 5");
        in.readLine();

        out.write("SELL 20");
        double amount = Double.parseDouble(in.readLine());
        System.out.println("Got "+ amount + "€!!!");
    }
}
