

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GraphServer {
	
	// Represents the named graphs stored in memory that have already been 
	// read into and processed by the server
	HashMap<String, Graph> graphs; 
	GraphReader reader;
    static Boolean DEBUG = true;	
	
	GraphServer() {
		this.graphs = new HashMap<String, Graph>();
		this.reader = new GraphReader(this);
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
		
	}

}

