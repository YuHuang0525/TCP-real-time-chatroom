package com.tcp.study;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientDemo {
    public static void main(String[] args) throws Exception {

        System.out.println("Client starts --- ");

        Socket socket = new Socket("127.0.0.1", 9999);

        // client wait for new message comming in
        new ClientReaderThread(socket).start();


        // client writes new message to server
        OutputStream os = socket.getOutputStream();

        PrintStream ps = new PrintStream(os);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Please say: ");
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();
        }

    }

}

class ClientReaderThread extends Thread {
    private Socket socket;
    public ClientReaderThread(Socket socket) { this.socket = socket; }

    @Override
    public void run() {

        try {
            InputStream is = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Receive message: " +  line);
            }

        } catch (Exception e) {
            System.out.println("Removed by the server.");
        }

    }

}

