
public class PathExample implements Path {
	Node head;
	Path tail;
	Double cost;
	
	PathExample(Node head, Path tail, Double cost) {
		this.head = head;
		this.tail = tail;
		this.cost = cost;
	}
	
	PathExample() {
		this.head = null;
		this.tail = null;
		this.cost = null;
	}
	
	public void setHead(Node n) {
		this.head = n;
	}

	public Node getHead() {
		return this.head;
	}

	public void setTail(Path p) { 
		this.tail = p;
	}

	public Path getTail() {
		return this.tail;
	}

	public Boolean hasTail() {
		return this.tail != null;
	}

	public void setCostToNext(Double cost) {
		this.cost = cost;
	}

	public Double getCostToNext() {
		return this.cost;
	}

	public Double getTotalCost() {
		if(this.tail == null) {
			return 0.0;
		} else {
			return this.cost + ((PathExample) tail).getTotalCost();
		}
	}

}
