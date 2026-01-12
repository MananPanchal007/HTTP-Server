import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple HTTP Server from scratch using Java Sockets.
 * Use this to understand the low-level details of HTTP TCP connections.
 */
public class JavaServer {

    public static void main(String[] args) {
        int port = 8081; // Using a different port than Python (8080) to allow running both

        // 1. Create a ServerSocket
        // This listens on the specified port on the local machine.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Java Server started on http://localhost:" + port);

            while (true) {
                // 2. Accept incoming connections
                // This method blocks (waits) until a client tries to connect.
                // When a client connects, it returns a 'Socket' object for communicating with ONLY that client.
                System.out.println("Waiting for client connection...");
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // 3. Handle the client request
                    handleClient(clientSocket);

                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        // Use try-with-resources to ensure streams close automatically
        try (
            // Input stream to read what the client sent (The HTTP Request)
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Output stream to send data back to the client (The HTTP Response)
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true) // PrintWriter for easier text writing
        ) {
            
            // --- READING THE REQUEST ---
            // HTTP Request Structure:
            // Line 1: Method Path Version (e.g., GET /index.html HTTP/1.1)
            // Lines 2-N: Headers (Key: Value)
            // Empty Line
            // Body (optional)
            
            String requestLine = reader.readLine();
            if (requestLine == null) return;
            
            System.out.println("Request Line: " + requestLine);

            // Read headers (we just print them here to show them)
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                System.out.println("Header: " + headerLine);
            }

            // --- SENDING THE RESPONSE ---
            // HTTP Response Structure:
            // Line 1: Protocol Status_Code Status_Text
            // Headers
            // Empty Line
            // Body

            String responseBody = "<html><body><h1>Hello from Java Raw Socket Server!</h1><p>Java Sockets in action.</p></body></html>";
            
            // 4. Construct the response
            // It's critical to follow the HTTP format strictly.
            
            // Status Line
            writer.print("HTTP/1.1 200 OK\r\n"); 
            
            // Headers
            writer.print("Content-Type: text/html\r\n");
            writer.print("Content-Length: " + responseBody.length() + "\r\n");
            writer.print("Connection: close\r\n"); // Close socket after sending
            
            // Empty line (Crucial! This tells the browser the headers are done)
            writer.print("\r\n");
            
            // Body
            writer.print(responseBody);
            writer.flush(); // Ensure data is sent immediately
            
            System.out.println("Response sent to client.");
            
        } finally {
            // 5. Close the socket
            // This tears down the TCP connection.
            clientSocket.close();
        }
    }
}
