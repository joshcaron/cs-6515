import java.io.PrintStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GraphServer {
	
	// Represents the named graphs stored in memory that have already been 
	// read into and processed by the server
	HashMap<String, Graph> graphs; 
	GraphReader reader; // Auxillary Class to deal with reading xml data
    ServerSocket server;
    Socket connection; // Connection via TCP/IP
    static final Integer PORT = 9999;
    static InputStream input;
    static PrintStream output;
    static Boolean DEBUG = true;	
	
	GraphServer() {
		this.graphs = new HashMap<String, Graph>();
		this.reader = new GraphReader(this);
        this.initializeServer();
	}
	
    // Initialize server and await connection, 
    // intialize input/output stream
    void initializeServer() {
        try {
            server = new ServerSocket(PORT);

            if(DEBUG) { System.out.println("Awaiting connection...."); }

            connection = server.accept();

            if(DEBUG) { System.out.println("....Connected!"); }

            input = new DataInputStream(connection.getInputStream());
            output = new PrintStream(connection.getOutputStream());
        } catch (IOException e) {
            if(DEBUG) { System.out.println("Error getting streams."); }
        }
    }

	// Returns the graph associated with the name
	// Throws exception if it doesn't exist
	Graph getGraph(String name) {
		if (this.hasGraph(name)) {
			return this.graphs.get(name);
		} else {
			throw new RuntimeException("Graph not found!");
		}
	}
	
	// Checks to see if the graph exists
	boolean hasGraph(String name) {
		return this.graphs.containsKey(name);
	}
	
	// Adds a graph to the data set
	void addGraph(String name, Graph graph) {
		graphs.put(name, graph);
	}
	
	void removeGraph(String name) {
		this.graphs.remove(name);
	}
	
	
	
	public static void main(String[] args) {
		GraphServer gs = new GraphServer();
		GraphReader gr = new GraphReader(gs);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String msg; 
        String response, xml;

        try {
            msg = reader.readLine();
            while(!msg.equals("bye")) {
                xml = "<gs>" + msg + "</gs>";
                System.out.println("client> " + msg);
                response = gr.processXML(xml);
                output.println(response);

                msg = reader.readLine();
            }
        } catch(IOException e) {
            System.out.println("Error getting input. Exiting.");
            System.exit(-1);
        }

        /*
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String xml = "<gs>";
		String line = "";	
		do {
			try {
				line = stdin.readLine();
			} catch (IOException e) {
				System.out.println("<error msg=\"JavaIO error. Try again.\" />");
			}
			if (line != null) {
				xml += line;
			}
		} while (line != null);
		xml += "</gs>";
		String response = gr.processXML(xml);
		System.out.println(response);
		*/
	}

}

