package org.graphgenerator.interactor;

import java.util.List;

import org.graphgenerator.interactor.ImageInteractor.ImageInteractorData;

public abstract class Action {
	
	private boolean actionFinished = false;
	protected ImageInteractorData data;
	protected List<ModelChangedListener> listnerList;

	
	public Action(ImageInteractorData data,
			List<ModelChangedListener> listnerList) {
		super();
		this.data = data;
		this.listnerList = listnerList;
	}

	public abstract void rollback();

	public abstract void addNode(Node node);
	
	

	 void finishAction() {
		actionFinished = true;
	}

	protected void checkAllowed() {
		if(actionFinished){
			throw new ActionFinishedException();
		}		
	}


	public boolean isFinnished() {
		return actionFinished;
	}
	
	
	protected void fireEvent(ModelChangedEvent event){
		for(ModelChangedListener listener : listnerList){
			listener.modelChanged(event);
		}
	}
}
