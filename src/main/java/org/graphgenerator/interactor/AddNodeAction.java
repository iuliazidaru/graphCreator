package org.graphgenerator.interactor;

import java.util.List;

import org.graphgenerator.interactor.ImageInteractor.ImageInteractorData;

public class AddNodeAction extends Action {

	public AddNodeAction(ImageInteractorData data,
			List<ModelChangedListener> listnerList) {
		super(data, listnerList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void rollback() {

	}

	@Override
	public void addNode(Node node) {
		data.nodeList.add(node);
		fireEvent(new ModelChangedEvent(ModelChangedEventType.NODE_ADDED, node));
		finishAction();
	}

}
