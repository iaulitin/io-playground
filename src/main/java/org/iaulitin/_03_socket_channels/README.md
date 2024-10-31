This example adds an improvement:
- It uses a thread pool to process incoming requests

Advantages:
- Better elasticity - thread amounts cannot exceed the thread pool size limit
- No cost of starting a thread for every request

However there are still disadvantages
- the I/O remained blocking