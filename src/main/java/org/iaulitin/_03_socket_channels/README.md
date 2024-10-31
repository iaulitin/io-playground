This example adds an improvement:
- It uses a thread pool to process incoming requests
This is similar to what Tomcat does under the hood. The difference is that it starts with smaller amount of threads, but it can scale the pool if needed.

Advantages:
- Better elasticity - thread amounts cannot exceed the thread pool size limit
- No cost of starting a thread for every request

However, there are still disadvantages
- the I/O remained blocking
- If all the threads from the pool are busy (including blocking on I/O), new connection attempts are rejected