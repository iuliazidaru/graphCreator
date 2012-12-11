package org.graphgenerator.interactor;

public class ModelChangedEvent {

	private Node node;
	private ModelChangedEventType type;
	private Edge edge;

	public ModelChangedEventType getType() {		
		return this.type;
	}

	public Node getNode() {
		return this.node;
	}

	public ModelChangedEvent(ModelChangedEventType type, Edge edge) {
		super();
		this.edge = edge;
		this.type = type;
	}

	public ModelChangedEvent(ModelChangedEventType type, Node node) {
		super();
		this.node = node;
		this.type = type;
	}

	public Edge getEdge() {
		return edge;
	}
	

}
