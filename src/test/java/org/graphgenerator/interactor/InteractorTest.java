package org.graphgenerator.interactor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InteractorTest {

	@Test
	public void canAddGraphNode(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_NODE);		
		final Node node = new Node(1, 2);
		
		final ListenerCalledInspector inspector = new ListenerCalledInspector();
		imageInteractor.addModelChangedListener(new ModelChangedListener(){
			public void modelChanged(ModelChangedEvent event){
				assertThat(event.getType(), is(ModelChangedEventType.NODE_ADDED));
				assertThat(event.getNode(), is(node));
				inspector.listenerCalled = true;
			
			}
		});
		
		imageInteractor.addGraphNode(node);
		
		assertThat(imageInteractor.getNodeList().contains(node), is(true));
		assertThat(inspector.listenerCalled, is(true));
	}
	
	
	@Test
	public void canAddFirstNodeInEdge(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_EDGE);
		final Node firstNode = new Node(1, 2);
		
		final ListenerCalledInspector inspector = new ListenerCalledInspector();
		imageInteractor.addModelChangedListener(new ModelChangedListener(){
			public void modelChanged(ModelChangedEvent event){
				assertThat(event.getType(), is(ModelChangedEventType.NODE_ADDED));
				assertThat(event.getNode(), is(firstNode));
				inspector.listenerCalled = true;
			
			}
		});
		
		imageInteractor.addGraphNode(firstNode);
		
		assertThat(imageInteractor.getNodeList().contains(firstNode), is(true));
		assertThat(inspector.listenerCalled, is(true));
	}
	
	@Test
	public void canAddSecondNodeInEdge(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_EDGE);
		
		final Node firstPoint = new Node(1, 2);
		final Node secondPoint = new Node(2, 3);	
		
		final ListenerCalledInspector listenerCalledForFirst = new ListenerCalledInspector();
		final ListenerCalledInspector listenerCalledForSecond = new ListenerCalledInspector();
		final ListenerCalledInspector listenerCalledForEdge = new ListenerCalledInspector();
		
		imageInteractor.addModelChangedListener(new ModelChangedListener(){
			public void modelChanged(ModelChangedEvent event){
				if(event.getType() == ModelChangedEventType.NODE_ADDED
						&& event.getNode().equals(firstPoint)){
					listenerCalledForFirst.listenerCalled = true;
				}
				if(event.getType() == ModelChangedEventType.NODE_ADDED
						&& event.getNode().equals(secondPoint)){
					listenerCalledForSecond.listenerCalled = true;
				}
				if(event.getType() == ModelChangedEventType.EDGE_ADDED){
					assertThat(event.getEdge(), is(new Edge(firstPoint, secondPoint)));
					listenerCalledForEdge.listenerCalled = true;
				}
			
			}
		});
		

		imageInteractor.addGraphNode(firstPoint);
		imageInteractor.addGraphNode(secondPoint);
		
		
		assertThat(imageInteractor.getNodeList().contains(firstPoint), is(true));
		assertThat(imageInteractor.getNodeList().contains(secondPoint), is(true));
		assertThat(imageInteractor.getEdgeList().contains(new Edge(firstPoint, secondPoint)), is(true));
		assertThat(listenerCalledForFirst.listenerCalled, is(true));
		assertThat(listenerCalledForSecond.listenerCalled, is(true));
		assertThat(listenerCalledForEdge.listenerCalled, is(true));
	}
	

	@Test
	public void canCancelAddingSecondEdge(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_EDGE);
		final Node firstPoint = new Node(1, 2);

		
		final ListenerCalledInspector listenerCalledForFirst = new ListenerCalledInspector();
		final ListenerCalledInspector listenerCalledForDelete = new ListenerCalledInspector();
		imageInteractor.addModelChangedListener(new ModelChangedListener(){
			public void modelChanged(ModelChangedEvent event){
				if(event.getType() == ModelChangedEventType.NODE_ADDED
						&& event.getNode().equals(firstPoint)){
					listenerCalledForFirst.listenerCalled = true;
				}
				if(event.getType() == ModelChangedEventType.NODE_REMOVED
						&& event.getNode().equals(firstPoint)){
					listenerCalledForDelete.listenerCalled = true;
				}			
			}
		});
		
		
		imageInteractor.addGraphNode(firstPoint);		
		imageInteractor.cancelAction();
		
		assertThat(imageInteractor.getNodeList().contains(firstPoint), is(false));	
		assertThat(listenerCalledForFirst.listenerCalled, is(true));
		assertThat(listenerCalledForDelete.listenerCalled, is(true));
		
	}
	
	
	@Test(expected = ActionFinishedException.class)
	public void failWhenAddSecondNodeInEdgeTwice(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_EDGE);
		Node firstPoint = new Node(1, 2);
		imageInteractor.addGraphNode(firstPoint);
		
		Node secondPoint = new Node(2, 3);
		imageInteractor.addGraphNode(secondPoint);
		
		Node secondPoint2 = new Node(22, 32);
		imageInteractor.addGraphNode(secondPoint2);		
	}
	
	class ListenerCalledInspector{
		boolean listenerCalled = false;
	}
	
	@Test
	public void canSelectPoinNearOtherPoint(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_NODE);
		final Node point1 = new Node(1, 2);
		imageInteractor.addGraphNode(point1);
		
		Node point2 = new Node(10, 20);
		imageInteractor.addGraphNode(point2);

		Node point3 = new Node(100, 200);
		imageInteractor.addGraphNode(point3);
		
		imageInteractor.prepareAction(ActionType.SELECT_NODE);
		
		final ListenerCalledInspector inspector = new ListenerCalledInspector();
		imageInteractor.addModelChangedListener(new ModelChangedListener(){
			public void modelChanged(ModelChangedEvent event){
				if(event.getType() == ModelChangedEventType.NODE_SELECTED){
					assertThat(event.getNode(), is(point1));
					inspector.listenerCalled = true;
				}
			}
		});
		
		imageInteractor.addGraphNode(new Node(3, 4));
		assertThat(inspector.listenerCalled, is(true));
		
	}
	
	@Test
	public void canAddMultipleEdges(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_MULTIPLE_EDGES);
		final Node firstPoint = new Node(1, 2);
		final Node point2 = new Node(2, 3);
		final Node point3 = new Node(3, 4);
		

		
		final ListenerCalledInspector listenerCalledForFirst = new ListenerCalledInspector();
		final ListenerCalledInspector listenerCalledForSecond = new ListenerCalledInspector();
		final ListenerCalledInspector listenerCalledForEdge = new ListenerCalledInspector();
		
		imageInteractor.addModelChangedListener(new ModelChangedListener(){
			public void modelChanged(ModelChangedEvent event){
				if(event.getType() == ModelChangedEventType.NODE_ADDED
						&& event.getNode().equals(firstPoint)){
					listenerCalledForFirst.listenerCalled = true;
				}
				if(event.getType() == ModelChangedEventType.NODE_ADDED
						&& event.getNode().equals(point2)){
					listenerCalledForSecond.listenerCalled = true;
				}
				if(event.getType() == ModelChangedEventType.EDGE_ADDED){
					listenerCalledForEdge.listenerCalled = true;
				}
			
			}
		});
		
		imageInteractor.addGraphNode(firstPoint);		
		imageInteractor.addGraphNode(point2);		
		imageInteractor.addGraphNode(point3);
		
		
		assertThat(imageInteractor.getNodeList().contains(firstPoint), is(true));
		assertThat(imageInteractor.getNodeList().contains(point2), is(true));
		assertThat(imageInteractor.getNodeList().contains(point3), is(true));
		assertThat(imageInteractor.getEdgeList().contains(new Edge(firstPoint, point2)), is(true));
		assertThat(imageInteractor.getEdgeList().contains(new Edge(point2, point3)), is(true));
		
		assertThat(listenerCalledForFirst.listenerCalled, is(true));
		assertThat(listenerCalledForSecond.listenerCalled, is(true));
		assertThat(listenerCalledForEdge.listenerCalled, is(true));
	}
	
	
	@Test
	public void prepareActionFinnishesStartedAction(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_EDGE);
		
		Node point1 = new Node(1, 2);
		imageInteractor.addGraphNode(point1);
		
		imageInteractor.prepareAction(ActionType.ADD_MULTIPLE_EDGES);
		
		assertThat(imageInteractor.getNodeList().contains(point1), is(false));
	}

	@Test
	public void canobtainTheEdgeList(){
		ImageInteractor imageInteractor = new ImageInteractor();
		imageInteractor.prepareAction(ActionType.ADD_MULTIPLE_EDGES);
		
		imageInteractor.addGraphNode(new Node(1, 2));
		imageInteractor.addGraphNode(new Node(3, 4));
		imageInteractor.addGraphNode(new Node(5, 6));
		
		assertThat(imageInteractor.getEdgeListAsString(), is("1 2  3 4\r\n3 4  5 6\r\n"));
	}  
}
