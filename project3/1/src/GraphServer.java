import java.io.PrintStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.io.PushbackInputStream;

public class GraphServer {

    // Auxillary Class to deal with reading xml data
    static GraphReader reader; 
    // Socket for server communication
    static ServerSocket server;
    // Connection via TCP/IP
    static Socket connection;
    // Port number to connect to 
    static final Integer PORT = 5432;
    // Output stream to write to
    static PrintStream output;
    // Input stream to get data
    static PushbackInputStream input;
    // Tool used for debugging our code
    static Boolean DEBUG = false;	


    GraphServer() {
        this.reader = new GraphReader(this);
    }

    // Initialize server connectin 
    private static void initializeServer() {
        try {
            server = new ServerSocket(PORT);

            if(DEBUG) { System.out.println("Awaiting connection...."); }

        } catch (NullPointerException e) {
            if(DEBUG) { System.out.println("Error connecting. Check to make sure port isn't in use."); }
        } catch (IOException e) {
            if(DEBUG) { System.out.println("Error getting streams."); }
        } 
    }

    // Waits for connection and initializes streams
    private static void getConnection() {
        try {
            connection = server.accept();

            if(DEBUG) { System.out.println("....Connected!"); }

            input = new PushbackInputStream(connection.getInputStream());
            output = new PrintStream(connection.getOutputStream());
        } catch (IOException e) {
            if(DEBUG) { System.out.println("Error getting streams."); }
        } catch (NullPointerException e) {
            if(DEBUG) { System.out.println("Error connecting. Check to make sure port isn't in use."); }
        }
    }

    // Runs the server
    public static void main(String[] args) {
        GraphServer gs = new GraphServer();
        GraphReader gr = new GraphReader(gs);
        BufferedReader reader;
        String msg; 
        String response, xml;

        initializeServer();

        while(true) {
            if (DEBUG) {System.out.println("Loop!"); }
            getConnection();   
            try {
                reader = new BufferedReader(new InputStreamReader(input));
                while(GraphServer.isConnected()) {
                    msg = GraphServer.getMessage(reader);
                    xml = "<gs>" + msg + "</gs>";
                    if (DEBUG) { System.out.println("client:    " + msg); }
                    response = gr.processXML(xml);
                    output.println(response);
                    if (DEBUG) { System.out.println("response:  " + response); }
                }
            } catch (NullPointerException e) {
                System.out.println("Error. Check to make sure port is not in use.");
                System.exit(-1);
            }
            if (DEBUG) {System.out.println("Restarting loop!"); }
        }
    }

    // Checks to see if client is still connected
    private static boolean isConnected() {

        try {
            int b = input.read();
            if (b == -1) {
                return false;
            } else {
                input.unread(b);
                return true;
            }
        } catch (IOException e) {
            if (DEBUG) { System.out.println("Error reading byte."); } 
            return false;
        }
    }


    // Gets the message the client sends
    private static String getMessage(BufferedReader reader) {
        String message = "";
        String line;
        try {
            while (reader.ready()) {
                if (DEBUG) { System.out.println("Ready?"); }
                if (DEBUG) { System.out.println(reader.ready()); }
                line = reader.readLine();
                if (DEBUG) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Whoops.");
                    }
                }

                if (DEBUG) { System.out.println(input.available()); }
                if (DEBUG) { System.out.println(line); }
                if (line != null) {
                    message += line;
                }
            }
        } catch (IOException e) {
            System.exit(-1);
        } 
        return message;
    }

}

