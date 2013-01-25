
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;




//TODO
// add comments
// refactor some methods
// Add debug info for displaying objects in memory
// Implement our client's interface
// Implement our test examples fully so we can debug





public class GraphReader {
	private final boolean DEBUG = false;
	GraphServer server; // The server this GraphReader is tied to
	
	GraphReader(GraphServer server) {
		this.server = server;
	}
	
	
	// Take the String of xml Data and create a DOM representation of it.
	// Return the DOM document.
	private Document getDocument(String xmlData) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document xml = null;
		
		// Set dbf to ignore whitespace and comments within the xml
		dbf.setIgnoringComments(true);
		dbf.setIgnoringElementContentWhitespace(true);
		
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			if (DEBUG) { System.out.println("Error making Document Builder"); }
			throw e;
		}
		
		// Reads the xmlData
		StringReader reader = new StringReader(xmlData);
		
		// Reads the xmlData as an input Source
		InputSource is = new InputSource(reader);
		
		// Parse the XML from a string to a Document
		try {
			xml = db.parse(is);
		} catch (SAXException e) {
			if (DEBUG) { System.out.println("Error parsing XML"); }
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (DEBUG) { System.out.println("Error parsing XML 2"); }
			throw e;
		}
		// XML document was built successfully
		if (DEBUG) { System.out.println("XML built"); }
		
		// First node in the XML doc
		if (DEBUG) { System.out.println(xml.getDocumentElement().toString()); }
		
		return xml;
	}
	
	// Create a document from the String of xmlData and perform the correct
	// operation based on the beginning element. Return an error message if
	// the beginning element is not valid
	String processXML(String xmlData) {
		String response = "";
		Document xml = null;
		try {
			xml = this.getDocument(xmlData);
		} catch (Exception e) {
			return this.xmlError("Invalid XML data");
		}
		
		// This is the gs node
		Element root = xml.getDocumentElement();
		String elem;
		
		for(org.w3c.dom.Node child = root.getFirstChild(); 
				child != null; 
				child = child.getNextSibling()) {
			elem = child.getNodeName();
			if (elem.equals("#text")) {
				continue;
			}
			if (elem.equals("graph")) {
				response += this.addGraph(child);
				if (DEBUG) { System.out.println("Graph made!"); }
			} else if (elem.equals("join")) {
				response += this.joinGraph(child);
				if (DEBUG) { System.out.println("Graphs joined!"); }
			} else if (elem.equals("path")) {
				response += this.findPath(child);
				if (DEBUG) { System.out.println("Well, we need to implement path..."); }
			} else {
				response = this.xmlError("Unrecognized GraphDescription");
			}
		}
		
		
		return response;
	}
	
	CostInterval getCostInterval(Element root) {
		NamedNodeMap attributes;
		String childName;
		Double cost;
		Vector<Double> costs = new Vector<Double>();
		
		
		return new CostInterval(costs);
	}
	
	// Creates a graph from the xml data and adds it to the storage in 
	// server. Returns the response to be printed as output, which is 
	// empty if it succeeded. Otherwise, the output is an error message
	// describing what went wrong.
	String addGraph(org.w3c.dom.Node root) {
		
		NodeList edges = root.getChildNodes();
		String graphName = root.getAttributes().getNamedItem("name").getNodeValue();
		if (DEBUG) { System.out.println(edges.getLength()); 
					 System.out.println(graphName);
		}
		
		if(graphName.equals("")) {
			return this.xmlError("Graph name not specified.");
		}
		
		// TODO 
		// Class name will change based upon library implementation
		GraphFactory gf = new CGraphFactory();
		Graph graph;
		NamedNodeMap attributes;
		Double cost;
		CostInterval ci;
		String from;
		String to;
		String childName;
		Vector<Double> costs = new Vector<Double>();
		
		for(org.w3c.dom.Node child = root.getFirstChild(); 
			child != null; 
			child = child.getNextSibling()) {
			
			childName = child.getNodeName();
			if( childName.equals("#text")) {
				continue;
			}
			
			if( !child.getNodeName().equals("edge") ) {
				return this.xmlError("Graph child is not an edge.");
			}
			
			attributes = child.getAttributes();
			
			try {
				cost = Double.parseDouble(attributes.getNamedItem("cost").getNodeValue());
				costs.add(cost);
			} catch(NumberFormatException e) {
				return this.xmlError("Cost attribute does not have the correct format.");
			} catch(NullPointerException e) {
                return this.xmlError("cost attribute in edge must be present");
            // TODO: ADDED
            }
		}
		
		ci = new CostInterval(costs);
		
		graph = gf.createGraph(ci.getMin(), ci.getMax());
		
		for(org.w3c.dom.Node child = root.getFirstChild(); 
			child != null; 
			child = child.getNextSibling()) {
			
			childName = child.getNodeName();
			if( childName.equals("#text")) {
				continue;
			}
			
			if( !child.getNodeName().equals("edge") ) {
				return this.xmlError("Graph child is not an edge.");
			}
			
			attributes = child.getAttributes();
			
			try {
				cost = Double.parseDouble(attributes.getNamedItem("cost").getNodeValue());
				from = attributes.getNamedItem("from").getNodeValue();
				to = attributes.getNamedItem("to").getNodeValue();
				
				if(DEBUG) { System.out.println(cost); }
				
				this.addEdgeToGraph(graph, from, to, cost);

			} catch(NumberFormatException e) {
				return this.xmlError("cost attribute in edge must be a valid double.");
			} catch(NullPointerException e) {
				return this.xmlError("all values in edge must be non-null and present");
			} catch (Exception e) {
				return this.xmlError("invalid edge");
			}
		}
		
		this.server.addGraph(graphName, graph);
		
		return "";
	}
	
	// Join the two graphs specified by the xml together. Return a String that will 
	// be returned to the user. The message is an empty string if successful, or an 
	// error message describing what went wrong.
	String joinGraph(org.w3c.dom.Node root) {
		if (DEBUG) {
			System.out.println("In the join");
		}
		
		String add = "";
		String to = "";
		NamedNodeMap attributes = root.getAttributes();
		add = attributes.getNamedItem("add").getNodeValue();
		to = attributes.getNamedItem("to").getNodeValue();
		
		if (add.equals("")) {
			return this.xmlError("add must be present and not empty");
		}
		if (to.equals("")) {
			return this.xmlError("to must be present and not empty");
		}
		
		String hasGraphs = this.hasGraphs(add, to);
		
		if (!hasGraphs.equals("")) {
			return hasGraphs;
		} 

		try {
			Graph graph1 = this.server.getGraph(add);
			Graph graph2 = this.server.getGraph(to);
			
			graph2.joinGraph(graph1);
			
			this.server.addGraph(to, graph2);
			this.server.removeGraph(add);
		} catch (Exception e) {
			return this.xmlError("graphs exist but could not be joined.");
		}
		
		return "";
	}
	
	private String hasGraphs(String add, String to) {
		if (!this.server.hasGraph(to)) {
			return this.xmlError("to must be previously defined");
		}
		if (!this.server.hasGraph(add)) {
			return this.xmlError("add must be previously defined");
		}
		
		return "";
	}
	
	// Finds a path specified by the xml. Returns a String that will be returned
	// to the user. The message is a PathDescription if the path exists, <false />
	// if the path does not exist, or an error message describing what went wrong.
	String findPath(org.w3c.dom.Node root) {

		NamedNodeMap attributes = root.getAttributes();
		String graphName = "";
		String from = "";
		String to = "";
		Node origin;
		Node dest;
		Graph graph;
		Double cost;
		
		graphName = attributes.getNamedItem("graph").getNodeValue();
		from = attributes.getNamedItem("from").getNodeValue();
		to = attributes.getNamedItem("to").getNodeValue();
		
		if (graphName.equals("")) {
			return this.xmlError("graph must be present and non empty");
		}
		if (from.equals("")) {
			return this.xmlError("from must be present and non empty");
		}
		if (to.equals("")) {
			return this.xmlError("to must be present and non empty");
		}
		
		if (!this.server.hasGraph(graphName)) {
			return this.xmlError("graph not found in server");
		}
		
		graph = this.server.getGraph(graphName);

		if (DEBUG) { System.out.println("From node: " + from + "\n To node: " + to);
		}
		if (!graph.hasNode(from)) {
			return this.xmlError("from not found in graph");
		}
		if (!graph.hasNode(to)) {
			return this.xmlError("to not found in graph");
		}
		
		origin = graph.getNode(from);
		dest = graph.getNode(to);
		
		if (!graph.pathExists(origin, dest)) {
			return "<false />";
		} else {
			Path path = graph.getPath(origin, dest);
			cost = graph.pathCost(origin, dest);
			return this.getPathDescription(cost, path);
		}
	}

	private String getPathDescription(Double cost, Path path) {
		CPath p = (CPath) path;
		String response = "<path cost=\"" + cost + "\">";
		while (p.tail != null) {
			response += this.getEdgeDescription(p.head, p.tail.head, p.getCost());
			p = p.tail;
		}
		response += "</path>";
		return response;
	}

	private String getEdgeDescription(Node head, Node tail, Double cost) {
		String headName = head.getLabel();
		String tailName = tail.getLabel();
		
		return "<edge cost=\"" + cost + "\" from=\"" + headName + "\" to=\"" + tailName + "\" />";
	}


	// Adds an edge to the Graph from the node labeled from to the node with the label
	// to with the given cost. Adds Nodes to the graph if necessary.
	private void addEdgeToGraph(Graph graph, String from, String to, Double cost) throws Exception {
		
		if (DEBUG) { System.out.println("Adding edges..."); }
		
		if( !graph.hasNode(from) ) {
			if (DEBUG) {System.out.println("Added the from node! " + from); }
			graph.addNode(from);
		} 
		if( !graph.hasNode(to) ) {
			if (DEBUG) {System.out.println("Added the to node! " + to); }
			graph.addNode(to);
		}
		
		try {
			if (DEBUG) {System.out.println("Trying to an edge..."); }
			graph.addEdge(graph.getNode(from), graph.getNode(to), cost);
			if (DEBUG) {System.out.println("Edge added!"); }
		} catch (Exception e) {
			if (DEBUG) {System.out.println("Adding edge failed!"); }
			if (DEBUG) {System.out.println(e.getMessage()); }
			throw e;
		}
	}

	// Generates an xml-formatted error with the given error message
	private String xmlError(String msg) {
		return "<error msg=\"" + msg + "\" />";
	}
}









