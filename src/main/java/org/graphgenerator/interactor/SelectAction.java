package org.graphgenerator.interactor;

import java.util.List;

import org.graphgenerator.interactor.ImageInteractor.ImageInteractorData;

public class SelectAction extends Action {

	public SelectAction(ImageInteractorData data,
			List<ModelChangedListener> listnerList) {
		super(data, listnerList);
	}

	@Override
	public void rollback() {
		//do nothing
	}

	@Override
	public void addNode(Node node) {
		double minDist = Integer.MAX_VALUE;
		Node choosen = null;
		for(Node existingNode : data.nodeList){
			double dist = Math.sqrt(Math.abs(existingNode.getX() - node.getX()) * Math.abs(existingNode.getX() - node.getX()) + 
					Math.abs(existingNode.getY() - node.getY()) * Math.abs(existingNode.getY() - node.getY()));			
			if(dist < minDist){
				minDist = dist;
				choosen = existingNode;
			}
		}

		fireEvent(new ModelChangedEvent(ModelChangedEventType.NODE_SELECTED, choosen));
		finishAction();		
	}

	

}
