package org.graphgenerator.interactor;

import java.util.ArrayList;
import java.util.List;

public class ImageInteractor {
	
	
	public static class ImageInteractorData {
		public List<Node> nodeList;
		public List<Edge> edgeList;

		public ImageInteractorData(List<Node> nodeList, List<Edge> edgeList) {
			this.nodeList = nodeList;
			this.edgeList = edgeList;
		}
	}

	private ImageInteractorData data = new ImageInteractorData(new ArrayList<Node>(),
			new ArrayList<Edge>());
	
	private Action currentAction;

	private List<ModelChangedListener> listnerList = new ArrayList<ModelChangedListener>();

	public void addGraphNode(Node node) {
		currentAction.addNode(node);
	}

	public List<Node> getNodeList() {
		return data.nodeList;
	}


	public List<Edge> getEdgeList() {
		return data.edgeList;
	}

	public void cancelAction() {
		this.currentAction.rollback();
		this.currentAction = null;
		
	}

	public void prepareAction(ActionType actionType) {
		switch (actionType) {
			case ADD_MULTIPLE_EDGES:
				closePreviousAction();
				currentAction = new AddMultiEdgeAction(data, listnerList);
			break;
			case ADD_NODE:
				closePreviousAction();
				currentAction = new AddNodeAction(data, listnerList);
			break;
			case ADD_EDGE:
					closePreviousAction();
					currentAction = new NewEdgeAction(data, listnerList);
				break;
			case SELECT_NODE:
					closePreviousAction();
					currentAction = new SelectAction(data, listnerList);
				break;
		default:
			break;
		}
		
	}

	private void closePreviousAction() {
		if(currentAction != null && !currentAction.isFinnished()){
			currentAction.rollback();					
		}
	}

	public void addModelChangedListener(
			ModelChangedListener modelChangedListener) {
		listnerList.add(modelChangedListener);
	}

	public String getEdgeListAsString() {
		StringBuilder builder = new StringBuilder();
		for(Edge edge : data.edgeList){
			builder.append(edge.getNode1().getX() + " " + edge.getNode1().getY() + "  " +
					edge.getNode2().getX() + " " + edge.getNode2().getY() + "\r\n");
		}
		return builder.toString();
	}

}
