package org.iaulitin._05_plain_nio_server_1_thread;

import org.iaulitin.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleThreadNioServer implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private AtomicInteger clientIdSequence = new AtomicInteger(0);

    public SingleThreadNioServer() throws IOException {
        // open NIO socket channel
        this.serverSocketChannel = ServerSocketChannel.open();
        // should be non-blocking, this have to be set to FALSE explicitly
        this.serverSocketChannel.configureBlocking(false);
        // bind socket channel to server socket
        this.serverSocketChannel.socket().bind(new InetSocketAddress("localhost", Constants.SERVER_PORT));
        // open selector
        this.selector = Selector.open();
        // register selector for ACCEPT events
        // we don't register selector for other events, as we will add them on per-client basis for CLIENT channels
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            while (this.serverSocketChannel.isOpen()) {
                System.out.println(">>> Blocking on selector.select()");
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                System.out.println(">>> Found keys for processing : " + keys.size());

                Iterator<SelectionKey> keysIter = keys.iterator();
                while (keysIter.hasNext()) {
                    SelectionKey key = keysIter.next();
                    keysIter.remove();
                    if (key.isAcceptable()) {
                        int clientId = clientIdSequence.incrementAndGet();
                        handleAccept(key, clientId);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleAccept(SelectionKey key, int clientId) {
        try {
            String clientName = "client-" + clientId;
            SocketChannel clientSocketChannel = ((ServerSocketChannel) key.channel()).accept();
            clientSocketChannel.configureBlocking(false);
            clientSocketChannel.register(this.selector, SelectionKey.OP_READ, clientName);
            System.out.println(">>> Connected new client: " + clientName);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleRead(SelectionKey key) {
        try {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = new byte[1024];
            int read = 0;
            while ((read = clientChannel.read(buffer)) > 0) {
                // channel was used for writing by the client (server socket channel) previously,
                // but we now want to read all the significant data from it by server (client socket channel)
                buffer.flip();
                buffer.get(bytes, 0, buffer.remaining());
                // we build a string taking into account only significant bytes
                stringBuilder.append(new String(bytes, 0, read));
                buffer.clear();
            }
            // < 0 means the input was ended and the client disconnected
            if (read < 0) {
                System.out.println(">>>> " + key.attachment() + " disconnected");
                clientChannel.close();
            } else {
                System.out.println(">>>> message from " + key.attachment() + " : " + stringBuilder);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        SingleThreadNioServer server = new SingleThreadNioServer();
        server.run();
    }
}
