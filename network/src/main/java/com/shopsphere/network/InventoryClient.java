package com.shopsphere.network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class InventoryClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 5555);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner kb = new Scanner(System.in)) {

            System.out.println("[Client] Connected. Type: GET <id> | STOCK <id> | QUIT");
            while (true) {
                System.out.print("> ");
                String line = kb.nextLine();
                if (line == null || line.isBlank()) continue;
                out.println(line);
                String response = in.readLine();
                System.out.println("< " + response);
                if (response != null && response.equalsIgnoreCase("BYE")) break;
            }
        }
    }
}
