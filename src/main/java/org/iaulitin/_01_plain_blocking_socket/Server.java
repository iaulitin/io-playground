package org.iaulitin._01_plain_blocking_socket;

import org.apache.commons.lang3.StringUtils;
import org.iaulitin.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {

            System.out.println(">>> Blocking waiting for a connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println(">>> Someone connected to the socket");

            doServerWork(clientSocket);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080");
        }
    }

    public static void doServerWork(Socket clientSocket) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            System.out.println(">>> Opened I/O streams");
            String inMessage;
            while ((inMessage = reader.readLine()) != null) {
                if ("END".equals(inMessage)) {
                    System.out.println(">>> Received a command to terminate the connection from client: \"" + inMessage + "\"");
                    break;
                }

                System.out.println(">>> Received a message from client: \"" + inMessage + "\"");
                String outMessage = StringUtils.reverse(inMessage);
                writer.println(outMessage);
            }
        }
    }
}
