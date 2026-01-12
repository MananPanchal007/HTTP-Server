import socket

def start_server():
    # 1. Create a socket object
    # AF_INET specifies the address family (IPv4)
    # SOCK_STREAM specifies the socket type (TCP)
    # This conceptually represents the endpoint for communication
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # Allow reuse of the address/port immediately after the server stops
    # This prevents the "Address already in use" error when restarting quickly
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    # 2. Bind the socket to a specific address and port
    # 'localhost' means the server is only accessible from this machine
    # 8080 is the port number we are listening on
    host = 'localhost'
    port = 8080
    server_socket.bind((host, port))
    print(f"Server started on http://{host}:{port}")

    # 3. Listen for incoming connections
    # The argument 5 is the backlog - the number of unaccepted connections that the system will allow before refusing new ones
    server_socket.listen(5)

    while True:
        try:
            # 4. Accept a connection
            # This blocks execution until a client connects
            # client_socket: a new socket object to communicate with this specific client
            # address: the IP address and port of the client
            print("Waiting for connection...")
            client_socket, address = server_socket.accept()
            print(f"Connection from {address}")

            # 5. Receive data from the client
            # We receive up to 1024 bytes. In a real server, we would loop until we get the full request.
            # TCP is a stream, so we receive bytes.
            request_data = client_socket.recv(1024).decode('utf-8')
            print(f"Received request:\n{request_data}")

            # If no data is received, close connection and continue
            if not request_data:
                client_socket.close()
                continue
            
            # --- HTTP PROTOCOL EXPLANATION ---
            # An HTTP Request looks like this:
            # Method Path Version (e.g., "GET / HTTP/1.1")
            # Headers (Key: Value)
            # Empty Line
            # Body (optional)
            
            # Simple parsing to get the first line (Request Line)
            request_lines = request_data.split('\r\n')
            request_line = request_lines[0]
            print(f"Request Line: {request_line}")

            # 6. Prepare the HTTP Response
            # An HTTP Response looks like this:
            # Version Status_Code Status_Text (e.g., "HTTP/1.1 200 OK")
            # Headers (Key: Value)
            # Empty Line
            # Body
            
            http_response_body = "<html><body><h1>Hello from Python Raw Socket Server!</h1><p>This is how HTTP works under the hood.</p></body></html>"
            
            http_response = (
                "HTTP/1.1 200 OK\r\n"                  # Status Line
                "Content-Type: text/html\r\n"          # Header: Type of content
                f"Content-Length: {len(http_response_body)}\r\n" # Header: Length of body
                "Connection: close\r\n"                # Header: Close connection after response
                "\r\n"                                 # Empty line separating headers and body
                f"{http_response_body}"                # The actual content
            )

            # 7. Send the response back to the client
            # We must encode the string back to bytes
            client_socket.sendall(http_response.encode('utf-8'))
            print("Response sent")

            # 8. Close the connection
            client_socket.close()
            
        except KeyboardInterrupt:
            print("\nServer stopping...")
            break
        except Exception as e:
            print(f"Error: {e}")
            if 'client_socket' in locals():
                client_socket.close()

    server_socket.close()

if __name__ == "__main__":
    start_server()
