package org.iaulitin._03_socket_channels;

import org.iaulitin.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer {

    public static final ExecutorService es = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            int count = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection #" + count++);
                CompletableFuture.supplyAsync(() -> {
                    try {
                        org.iaulitin._01_plain_blocking_socket.Server.doServerWork(clientSocket);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    return null;
                }, es);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
