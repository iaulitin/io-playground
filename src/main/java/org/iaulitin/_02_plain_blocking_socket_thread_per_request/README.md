This example adds an improvement:
- For every incoming request a new thread is created

Advantages comparing to previous implementation:
- we can accept multiple clients simultaneously

This solution still has disadvantages:
- the I/O remained blocking
- this solution lacks elasticity - it spawns too many threads, enlarges the heap and causing a lot of context switching
  - once the CPU gets overloaded it can stop receiving requests