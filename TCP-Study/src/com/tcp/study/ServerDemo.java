package com.tcp.study;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerDemo {

    public static List<Socket> allOnlineSockets = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server starts --- ");

        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {

            Socket socket = serverSocket.accept();

            System.out.println(socket.getRemoteSocketAddress() + " connects");
            allOnlineSockets.add(socket);
            new ServerReaderThread(socket).start();

        }


    }
}

class ServerReaderThread extends Thread {
    private Socket socket;
    public ServerReaderThread(Socket socket) { this.socket = socket; }

    @Override
    public void run() {

        try {
            InputStream is = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(socket.getRemoteSocketAddress() + "sends message: " + line);

                // sends the message that just received to all connected sockets
                sendMsgToAll(line);

            }

        } catch (Exception e) {
            System.out.println(socket.getRemoteSocketAddress() + " removed by the server.");
            ServerDemo.allOnlineSockets.remove(socket);
        }

    }

    private void sendMsgToAll(String msg) throws Exception {

        for (Socket socket : ServerDemo.allOnlineSockets) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println(msg);
            ps.flush();
        }

    }

}
