# HTTP Server from Scratch Walkthrough

This walkthrough demonstrates the simple HTTP servers built in Python and Java. These servers use raw sockets to handle HTTP requests, helping you understand the protocol "behind the scenes".

## 1. Python Server

### Running the Server
Run the python server using the `py` (or `python` if configured) command:
```bash
py python_server.py
```
You should see:
```text
Server started on http://localhost:8080
Waiting for connection...
```

### Verifying
Open your browser to `http://localhost:8080`, or use curl:
```bash
curl http://localhost:8080
```

**Expected Server Output:**
```text
Connection from ('127.0.0.1', 60123)
Received request:
GET / HTTP/1.1
...
Request Line: GET / HTTP/1.1
Response sent
```

## 2. Java Server

### Compiling and Running
First, compile the Java file:
```bash
javac JavaServer.java
```

Then run it:
```bash
java JavaServer
```
You should see:
```text
Java Server started on http://localhost:8081
Waiting for client connection...
```

### Verifying
Open your browser to `http://localhost:8081`, or use curl:
```bash
curl http://localhost:8081
```

**Expected Server Output:**
```text
Client connected: /0:0:0:0:0:0:0:1
Request Line: GET / HTTP/1.1
...
Response sent to client.
```

## Key Concepts Demonstrated
- **Sockets**: Both servers explicitly create a socket, bind it to a port, and listen.
- **Protocol**: You can see the raw HTTP request strings (e.g., `GET / HTTP/1.1`) and how the response is manually constructed string-by-string (e.g., `HTTP/1.1 200 OK`).
- **Control**: No frameworks (like Flask or Spring Boot) are hiding the details. You control the bytes sent and received.
