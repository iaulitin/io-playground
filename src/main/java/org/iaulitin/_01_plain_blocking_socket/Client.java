package org.iaulitin._01_plain_blocking_socket;

import org.iaulitin.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client {

    private static final List<String> MESSAGES = List.of("Hello,", "this is client,", "nice to meet you.", "END");
    private static final int SOCKET_TIMEOUT_MS = 2000;

    /**
     * The client will send messages one by one, expecting a response after every message.
     * The server logic will return a reversed response for every message except "END"
     *
     * As the response for the last message will not be returned, a SocketTimeoutException will be thrown after 2 seconds of waiting.
     * This is configured by
     *
     * @param args
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", Constants.SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            socket.setSoTimeout(2000);

            for (String message : MESSAGES) {
                writer.println(message);
                System.out.println(">>> Sent to server: \"" + message + "\"");
                System.out.println(">>> Blocking while waiting for a server response...");
                System.out.println(">>> Received a response from server: \"" + reader.readLine() + "\"");
                Thread.sleep(1000);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
