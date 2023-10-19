                  Dictionary Server-Client Application Report
Problem Context:
<img width="736" alt="image" src="https://github.com/shuyu0619/Distribute_system_A1/assets/101319610/4d4d4a7f-2f3e-4501-b74e-2c5c2ecdc72f">
In this endeavor, I completed a multi-threaded dictionary server along with a user-friendly client interface. The server does more than just storing words and their meanings; it enables CRUD operations—Create, Read, Update, and Delete—in a seamless manner. Not to stop there, I also integrated a UI on the server side that keeps real-time tabs on activity, allows quick word look-ups, and even provides some analytics on what words are being queried the most.

Components of the System:
Client.java: Manages the client-side networking. It establishes a socket connection with the server and handles the reading and writing of data.
1. Socket Connection: The class initializes a TCP socket connection to the server. This is the primary channel for IPC (Inter-Process Communication), allowing data to flow between the client and server.
2. Buffer Management: This class uses Java's BufferedReader and BufferedWriter classes to facilitate the reading and writing of UTF-8 encoded data streams over the established socket connection.
3. Request Queue: The class employs a LinkedBlockingQueue to enqueue client requests. This data structure ensures that the client's requests are organized and sent to the server in a first- come, first-served manner.
4. Response Handling: An integral part is the getResp(), designed to read the server's response from the input stream. Then guides the client-side application on how to proceed further.
  
UI.java (Client UI): Renders the client-side UI using Java Swing. It provides a simple, interactive way to interact with the dictionary server and integrates closely with the Client.java for communication.
1. Action Buttons: Houses four action buttons each linked to an action listener for server communication.
2. Event Handling: Contains methods like handleUpdate() for button-event processing.
3. Result Feedback: Displays server responses in txtResult for user feedback.
4. Validation: Ensures necessary fields are filled before initiating server communication.
Server.java: The main of the project, responsible for initializing server properties, managing the client connections, and maintaining the dictionary.
1. Server Initialization: Sets up server properties like port number and reads the initial dictionary from disk.
2. Socket Management: Manages incoming client connections through a server socket.
3. Dictionary Management: Reads and writes the dictionary data to and from the disk.
4. User Count & Query Count: Keeps track of the number of connected users and query counts
for analytics.
Connection.java: Handles the actual logic and I/O processes between client and erver.
1. Thread Management: Manages multiple client connections concurrently by extending Java's
Thread class.
2. Buffer Management: Utilizes buffered streams for I/O operations.
3. Command Handling: Processes client commands and interacts with the dictionary.
4. Concurrency Safety: Ensures thread-safe operations when multiple clients interact with the
dictionary.
Server_UI.java: Adds a UI for the server, allowing real-time monitoring and administrative functionalities.
1. Real-Time Monitoring: Displays the current number of connected users.
2. Quick Search: Enables quick search for words in the dictionary.
3. Most Queried Words: Displays analytics on the top three most queried words, offering insights
into user behavior.

UML and interaction diagram:
<img width="712" alt="image" src="https://github.com/shuyu0619/Distribute_system_A1/assets/101319610/a1603e11-df8f-43e2-878a-065fb14c8750">
<img width="983" alt="image" src="https://github.com/shuyu0619/Distribute_system_A1/assets/101319610/35166943-ac41-4e36-a573-677dbf77fe7d">

Critical analysis:
Multi-Thread Architecture Employed: Thread-per-Connection:
In the Thread-per-Connection architecture, each client that connects to the server is assigned a dedicated thread (via the Connection class) for the duration of its connection. This thread handles all incoming and outgoing messages for that particular client. This model is different from Thread-per- Request, which spawns a new thread for each client request, and Thread-per-Servant(object), which assigns a thread to each type of service rather than each client [1].
Advantages:
1. Resource Amortization: The cost of spawning a new thread is amortized over the lifetime of
the client connection, which is particularly beneficial for long-lived connections.
2. Simplicity: The architecture is straightforward to implement, as each client connection has its
own dedicated resources and processing thread.
3. Load Prediction: Since each client has its own thread, it's easier to predict the load on the server
if the number of clients and their behavior are known.
4. No Context Switching Overhead: Compared to Thread-per-Request, there is no need to
continually spawn and destroy threads, thereby reducing context-switching overheads.
Disadvantages:
1. Resource Intensive: Each connection requires its own thread, which could consume significant system resources if the number of simultaneous connections is large.
2. Load Balancing: The architecture does not inherently support effective load balancing, especially if some clients are more active than others.
3. Short-lived Connections: For clients that only make a brief connection to request a small amount of data, the thread-per-connection model can be less efficient compared to a thread pool or thread-per-request model.
Reason for Choosing Thread-per-Connection:
The primary reason for choosing the Thread-per-Connection model was its suitability for long-duration conversations with multiple clients. Given that the dictionary server is expected to have clients that stay connected to perform multiple operations (like add, delete, query, and update), this model provides a good balance between resource utilization and performance.
Additionally, this model is easier to implement and debug since each client has its own isolated environment in the form of a dedicated thread. This makes it easier to maintain the state and handle client-specific logic.
ConcurrentHashMap:
1. ConcurrentHashMap is a thread-safe variant of HashMap in Java. It allows to perform retrieval operations without blocking: multiple threads can read the map concurrently. This makes it highly suitable for multi-threaded environments like the server-side application where multiple client connections may try to modify or read the dictionary at the same time [2].
2. Internal Working: It achieves concurrency by segmenting the map into different parts and locking only a particular segment during write operations. This allows a high level of concurrency and improves performance.
LinkedBlockingQueue:
In the project, the LinkedBlockingQueue serves as a thread-safe, blocking queue that elegantly handles request queuing between the UI and the network thread. One of its key features is its use of separate locks for put and take operations. This means that while one thread is putting an element into the queue, another can concurrently take an element from it, thereby improving throughput and making the system more efficient. This dual-lock mechanism significantly optimizes the queue's performance, and it works exceptionally well in multi-threaded environments [3].
The queue's blocking nature ensures a seamless flow of data between the UI and network threads. For instance, the queue can wait when it's empty and can also be configured to wait for space to become available. This design eliminates the need for additional synchronization mechanisms and ensures that when the UI thread enqueues a request, the networking thread can safely dequeue and process it.
By using LinkedBlockingQueue in this manner, I've added a layer of thread safety and have optimized the system for better performance and reliability [3].
Protocol Design:
1. Simplicity: My protocol is text-based and very straightforward, following a pattern like "ADD#word#meaning" or "UPDATE#word#meaning". This kind of protocol is easy to debug and log, which can significantly ease the development and maintenance phases.
2. Flexibility: The protocol is flexible enough to allow for various operations like ADD, DELETE, UPDATE, and QUERY. This makes it quite extensible; if you need to add more features or operations later, you can easily do so.
3. Statelessness: Each request from the client to the server is independent. This is generally a good practice in client-server architectures as it simplifies the server design, making it easier to scale and maintain.
Area for Improvement:

Data Storage Mechanism:
• The server currently uses a text file (dictionary.txt) to store and manage the dictionary data.
• Improvement: Use a more advanced data storage solution, such as MySQL database
• Benefits:
1. Scalability: As the dictionary grows, a database will be better equipped to handle large datasets
2. Data Integrity: Databases have built-in mechanisms to ensure data consistency and integrity.
3. Concurrent Access: In the current model, handling multiple read and write requests simultaneously can be cumbersome. A database is built to manage this efficiently, aligning perfectly with the multi-threaded nature of my server.
4. Query Capability: Using a database would allow for more complex queries and data manipulation techniques that are not possible with a text file.
5. Security: Databases offer better security features, including access controls and encryption.
Client-Side:
Logging: I've realized that debugging could be a lot smoother with a better logging mechanism. So, I'm exploring the integration of Log4J or SLF4J frameworks into the client-side code. This would not only make it easier to debug issues but also provide valuable data that could help optimize the application.
Server-Side:
Protocol and Communication: The server currently communicates with the client using simple numerical codes ("ADD#word#meaning" or "UPDATE#word#meaning"), which may not be sufficiently expressive for complex interactions.
Improvement: The adoption of a structured communication protocol such as JSON or XML.
Benefits: Utilizing a structured data format enhances the system's extensibility, allowing for the easy addition of new commands or the transmission of more complex data types.

References:
[1]Schmidt, D. C. (1998). Evaluating architectures for multithreaded object request
brokers. Communications of the ACM, 41(10), 54-60.
https://dl.acm.org/doi/pdf/10.1145/286238.286248
[2]Goetz, B. (2003). Java theory and practice: Concurrent collections classes. IBM Developer Works.
https://www.immagic.com/eLibrary/ARCHIVES/GENERAL/IBM/I030723G.pdf
[3]Meijer, J. Formal Specification of LinkedBlockingQueue Using Concurrent Separation Logic.
https://citeseerx.ist.psu.edu/document?repid=rep1&type=pdf&doi=58b0b21de63df02f2ef8277c3bc0 b0be62d4825f

