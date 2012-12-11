package org.graphgenerator.interactor;

import java.util.List;

import org.graphgenerator.interactor.ImageInteractor.ImageInteractorData;


public class AddMultiEdgeAction extends Action {
	private Node firstNode;

	public AddMultiEdgeAction(ImageInteractorData data,
			List<ModelChangedListener> listnerList) {
		super(data, listnerList);
	}


	@Override
	public void rollback() {
		checkAllowed();
		finishAction();
	}

	@Override
	public void addNode(Node node) {
		checkAllowed();
		if(firstNode != null){
			Edge edge = new Edge(firstNode, node);
			data.edgeList.add(edge);
			fireEvent(new ModelChangedEvent(ModelChangedEventType.EDGE_ADDED, edge));
		}
		if(!data.nodeList.contains(node)){
			data.nodeList.add(node);
			fireEvent(new ModelChangedEvent(ModelChangedEventType.NODE_ADDED, node));
		}
		firstNode = node;
	}

}
