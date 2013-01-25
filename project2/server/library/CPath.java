
public class CPath implements Path {
	
	Node head;
	CPath tail;
	
	public CPath(Node head, CPath tail)
	{
		this.head = head;
		this.tail = tail;
	}
	
	public void setTail(CPath tail)
	{
		if (this.tail == null)
		{
			this.tail = tail;
		}
		else
		{
			this.tail.setTail(tail);
		}
	}

	@Override
	public Double getCost() {
		// TODO Auto-generated method stub
		if (this.tail != null)
		{
			return ((CNode)this.tail.head).distance;
		}
		else
		{
			return null;
		}
	}

}
