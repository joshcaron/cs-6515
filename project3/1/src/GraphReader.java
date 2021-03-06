
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
			return this.xmlError("Invalid XML data.");
		}

		// This is the gs node
		Element root = xml.getDocumentElement();
		String elem;

		this.stripTextNodes(root);
		if (root.getFirstChild() == null) {
			return this.xmlError("Invalid xml data.");
		}
		
		for(org.w3c.dom.Node child = root.getFirstChild(); 
				child != null; 
				child = child.getNextSibling()) {
			elem = child.getNodeName();
			if (elem.equals("#text")) {
				continue;
			}

			if (elem.equals("join")) {
				response += this.joinGraph(child);
				if (DEBUG) { System.out.println("Graphs joined!"); }
			} else if (elem.equals("path")) {
				response += this.findPath(child);
				if (DEBUG) { System.out.println("Path made!"); }
			} else if (elem.equals("nodes")) {
				response += this.getNodes(child);
				if (DEBUG) { System.out.println("Nodes found!"); }
			} else if (elem.equals("new")) {
				response += this.getNewGraph(child);
				if (DEBUG) { System.out.println("Graph made!"); }
			} else if (elem.equals("edges")) {
				response += this.getEdges(child);
				if (DEBUG) { System.out.println("Edges made!"); }
			} else if (elem.equals("add")) {
				response += this.addNewEdge(child);
				if (DEBUG) { System.out.println("Edge handled!"); }
			} else {
				return this.xmlError("Unrecognized request.");
			}
		}
		
		return response;
	}

	// Strips the root element of all text nodes
	private void stripTextNodes(org.w3c.dom.Node root) {
		org.w3c.dom.Node nextNode = null, n = root.getFirstChild();
		while (n != null) {
			if (n.getNodeName().equals("#text")) {
				nextNode = n.getNextSibling();
				root.removeChild(n);
				n = nextNode;
			} else {
				stripTextNodes(n);
				n = n.getNextSibling();
			}
		}
	}



	// Retuns an XML representation of a new empty graph
	private String getNewGraph(org.w3c.dom.Node root) {
		if (root.hasChildNodes()) {
			return this.xmlError("New request cannot contain other XML.");
		}

		Double low;
		Double high;
		Graph g;

		try {
			low = Double.parseDouble(root.getAttributes().getNamedItem("low").getNodeValue());
			high = Double.parseDouble(root.getAttributes().getNamedItem("high").getNodeValue());
		} catch (NullPointerException e) {
			return this.xmlError("New request must provide a low and high cost interval.");
		} catch (NumberFormatException e) {
			return this.xmlError("Low and high attributes must be properly formatted doubles.");
		} 
		
		g = new GraphExample(low, high);

		if (low < 0 || high < 0) {
			return this.xmlError("Low and high must both be positive real numbers.");
		}
		if (low > high) {
			return this.xmlError("Low value must not be greater than the high value.");
		}

		return this.getGraphDescription(g);
	}

	// Returns an XML representation of all the nodes in the given graph extracted from
	// the org.w3c.dom.Node
	private String getNodes(org.w3c.dom.Node root) {
		Graph g;

		try {
			g = this.makeGraph(root.getFirstChild());	
		} catch (IllegalArgumentException e) {
			return this.xmlError(e.getMessage());
		}
		
		if (root.getFirstChild().getNextSibling() != null) {
			return this.xmlError("Nodes request can only contain one graph.");
		}

		String response = "<nodes>";
		Vector<Node> nodes = g.getNodes();
		for (Node n : nodes) {
			response += this.getNodeDescription(n);
		}

		return response + "</nodes>";
	}	

	// Returns an XML representation of all the nodes in the given graph extracted from
	// the org.w3c.dom.Node
	private String getEdges(org.w3c.dom.Node root) {
		Graph g;

		try {
			g = this.makeGraph(root.getFirstChild());	
		} catch (IllegalArgumentException e) {
			return this.xmlError(e.getMessage());
		}
		
		if (root.getFirstChild().getNextSibling() != null) {
			return this.xmlError("Edges request can only contain one graph.");
		}

		String response = "<edges>";
		Vector<Edge> edges = g.getEdges();
		for (Edge e : edges) {
			response += this.getEdgeDescription(e);
		}

		return response + "</edges>";
	}


	// Creates a graph from the xml data and returns it
	private Graph makeGraph(org.w3c.dom.Node root) {
		NamedNodeMap attributes;
		Double cost;
		String from;
		String to;
		String childName;
		NodeList edges = root.getChildNodes();
		Double low;
		Double high;

		try {
			low = Double.parseDouble(root.getAttributes().getNamedItem("low").getNodeValue());
			high = Double.parseDouble(root.getAttributes().getNamedItem("high").getNodeValue());
		} catch(NumberFormatException e) {
			if (DEBUG) { System.out.println("Error getting low/high cost."); }
			throw e;
		}

		if (low < 0 || high < 0) {
			throw new IllegalArgumentException("Low and high must be positive real numbers.");
		}

		if (low > high) {
			throw new IllegalArgumentException("High boundary must be greater than low boundary.");
		}

		if (DEBUG) { System.out.println(edges.getLength()); }

		Graph graph = new GraphExample(low, high);

		for(org.w3c.dom.Node child = root.getFirstChild(); 
			child != null; 
			child = child.getNextSibling()) {
			
			childName = child.getNodeName();
			if( childName.equals("#text")) {
				continue;
			}
			
			if( !child.getNodeName().equals("edge") ) {
				throw new IllegalArgumentException("Graph child is not an edge.");
			}
			
			attributes = child.getAttributes();
			
			try {
				cost = Double.parseDouble(attributes.getNamedItem("cost").getNodeValue());
				from = attributes.getNamedItem("from").getNodeValue();
				to = attributes.getNamedItem("to").getNodeValue();
				
				if(DEBUG) { System.out.println(cost); }
				
				this.addEdgeToGraph(graph, from, to, cost);

			} catch(NumberFormatException e) {
				throw new IllegalArgumentException("Cost attribute in edge must be a valid double.");
			} catch(NullPointerException e) {
				throw new IllegalArgumentException("All values in edge must be non-null and present.");
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid edge.");
			}
		}
		
		
		return graph;
	}

	// Adds an edge generated from the given XML to the given graph from the XML
	// so long as it does not violate triangular inequality
	private String addNewEdge(org.w3c.dom.Node root) {
		String from;
		String to;
		Double cost;
		Graph g;
		NamedNodeMap attributes = root.getAttributes();
		try {
			from = attributes.getNamedItem("from").getNodeValue();
			to = attributes.getNamedItem("to").getNodeValue();
			cost = Double.parseDouble(attributes.getNamedItem("cost").getNodeValue());
			g = this.makeGraph(root.getFirstChild());	
		} catch (NullPointerException e) {
			return this.xmlError("From, to, and cost attributes must be present and non-null.");
		} catch (NumberFormatException e) {
			return this.xmlError("Cost attribute must be a positive real number.");
		} catch (IllegalArgumentException e) {
			return this.xmlError(e.getMessage());
		}
		
		if (root.getFirstChild().getNextSibling() != null) {
			return this.xmlError("Add request can only add edge to one graph.");
		}

		try {
			this.addEdgeToGraph(g, from, to, cost);
		} catch (IllegalArgumentException e) {
			return this.xmlError(e.getMessage());
		}
		return this.getGraphDescription(g);

	}

	// Join the two graphs specified by the xml together. Return a String that will 
	// be returned to the user. The message is an empty string if successful, or an 
	// error message describing what went wrong.
	private String joinGraph(org.w3c.dom.Node root) {
		if (DEBUG) {
			System.out.println("In the join");
		}

		Graph add;
		Graph to;
		Graph joined;
		Integer count = 0;
		org.w3c.dom.Node child = root.getFirstChild();

		try {
			to = this.makeGraph(child);
			child = child.getNextSibling();
			add = this.makeGraph(child);
			child = child.getNextSibling();
			if (child != null) {
				return this.xmlError("Can only join two graphs.");
			}
		} catch (IllegalArgumentException e) {
			return this.xmlError(e.getMessage());
		} catch (NullPointerException e) {
			return this.xmlError("There must be two graphs in a join request.");
		}

		try {
			joined = to.joinGraph(add);
		} catch (IllegalArgumentException e) {
			return this.xmlError("Graphs must operate on the same cost interval.");
		}
		return this.getGraphDescription(joined);
	}


	private String getGraphDescription(Graph g) {
		String response = "<graph ";
		response += "low=\"" + g.getLowCostInterval() + "\" ";
		response += "high=\"" + g.getHighCostInterval() + "\">";

		Vector<Edge> edges = g.getEdges();

		for (Edge e : edges) {
			response += this.getEdgeDescription(e);
		}

		return response + "</graph>";
	}

	private String getNodeDescription(Node n) {
		return "<node name=\"" + n.getLabel() + "\" />";
	}
	
	
	// Finds a path specified by the xml. Returns a String that will be returned
	// to the user. The message is a PathDescription if the path exists, <false />
	// if the path does not exist, or an error message describing what went wrong.
	private String findPath(org.w3c.dom.Node root) {

		NamedNodeMap attributes = root.getAttributes();
		String graphName = "";
		String from = "";
		String to = "";
		Node origin;
		Node dest;
		Graph graph;
		Double cost;
		
		try {
			from = attributes.getNamedItem("from").getNodeValue();
			to = attributes.getNamedItem("to").getNodeValue();
			graph = this.makeGraph(root.getFirstChild());	
		} catch (NullPointerException e) {
			return this.xmlError("From and to attributes must be present and non-null.");
		} catch (IllegalArgumentException e) {
			return this.xmlError(e.getMessage());
		}
		
		if (root.getFirstChild().getNextSibling() != null) {
			return this.xmlError("Path request can only accept one graph.");
		}

		try {
			origin = graph.getNode(from);
			dest = graph.getNode(to);
		} catch (RuntimeException e) {
			return this.xmlError("Nodes are not present in the graph.");
		}
		
		if (!graph.pathExists(origin, dest)) {
			return "<false />";
		} else {
			Path path = graph.calculatePath(origin, dest);
			cost = path.getTotalCost();
			return this.getPathDescription(cost, path);
		}
	}

	private String getPathDescription(Double cost, Path p) {
		String response = "<path cost=\"" + cost + "\">";
		while (p.getTail() != null) {
			response += this.getEdgeDescription(p.getHead(), p.getTail().getHead(), p.getCostToNext());
			p = p.getTail();
		}
		response += "</path>";
		return response;
	}

	private String getEdgeDescription(Node head, Node tail, Double cost) {
		String headName = head.getLabel();
		String tailName = tail.getLabel();
		
		return "<edge cost=\"" + cost + "\" from=\"" + headName + "\" to=\"" + tailName + "\" />";
	}

	private String getEdgeDescription(Edge e) {
		return "<edge cost=\"" + e.getCost() + "\" from=\"" + e.getOrigin().getLabel() + "\" to=\"" + e.getDest().getLabel() + "\" />";
	}


	// Adds an edge to the Graph from the node labeled from to the node with the label
	// to with the given cost. Adds Nodes to the graph if necessary.
	private void addEdgeToGraph(Graph graph, String from, String to, Double cost) {
		
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
			graph.addEdge(from, to, cost);
			if (DEBUG) {System.out.println("Edge added!"); }
		} catch (IllegalArgumentException e) {
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









