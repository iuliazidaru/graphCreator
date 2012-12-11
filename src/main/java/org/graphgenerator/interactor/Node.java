package org.graphgenerator.interactor;

public class Node {

	private int x;
	private int y;
	
	public Node(int x, int y) {
		super();
		this.setX(x);
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getX();
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (getX() != other.getX())
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node [x=").append(x).append(", y=").append(y)
				.append("]");
		return builder.toString();
	}
	
	
}
