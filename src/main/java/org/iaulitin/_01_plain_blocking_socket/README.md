In this example client and server do a blocking communication.

The server:
- is blocked when it opens a socket and waits for a connection (when `accept` is called)
- is blocked while waiting for the data to be sent (when `readLine` is called)

The client:
- is blocked while waiting for the server responses to be sent (when `readLine()` is called)