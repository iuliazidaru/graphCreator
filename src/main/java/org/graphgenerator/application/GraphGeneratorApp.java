package org.graphgenerator.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.graphgenerator.interactor.ActionType;
import org.graphgenerator.interactor.Edge;
import org.graphgenerator.interactor.ImageInteractor;
import org.graphgenerator.interactor.ModelChangedEvent;
import org.graphgenerator.interactor.ModelChangedListener;
import org.graphgenerator.interactor.Node;


public class GraphGeneratorApp extends Application implements ModelChangedListener{

	private static final int EDGE_STROKE = 2;
	private static final float NODE_RADIUS = 5.0f;
	private static final double NODE_RADIUS_SELECTED = 8.0f;
	
	private File file;
	private Button btnOpenBgFile = new Button();
	private Label lblBgFile = new Label();
	private Label lblDebug = new Label();
	private VBox vBoxMenu = new VBox();
	private Pane clickableArea= new Pane(); 
	private ScrollPane scrClickableArea = new ScrollPane();
	
	private ToggleGroup tbgActions = new ToggleGroup();
	private ToggleButton tbCreateGraph = new ToggleButton("GRAPH");
	private ToggleButton tbSelect = new ToggleButton("SELECT");
	private ToggleButton tbCancel = new ToggleButton("CANCEL");
	
	
	
	private ImageInteractor interactor = new ImageInteractor();
	private Circle circleSelectedNode;
    private Node selectedNode;
	private Button btnSave;
	
	@Override
	public void start(final Stage primaryStage) {
		interactor.addModelChangedListener(this);
		primaryStage.setTitle("Graph generator application");
		
		initializebtnOpenBgFile();
   	    initializeBtnSave(primaryStage);
   	    initializeToggles();
		initializevBoxMenu();
		
		
    	initialieClickableArea();
    	

    	HBox root = new HBox();
        root.setPadding(new Insets(0, 0, 0, 0));
        root.getChildren().add(vBoxMenu);
        root.getChildren().add(scrClickableArea);
        root.setStyle("-fx-background-color: blue;");
        
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        
        enableSelectFile();        
	}

	private void enableSelectFile() {
		btnOpenBgFile.setDisable(false);
		tbCreateGraph.setDisable(true);
		tbSelect.setDisable(true);	
		tbCancel.setDisable(true);
		btnSave.setDisable(true);		
	}
	
	private void enableActions() {
		btnOpenBgFile.setDisable(true);
		tbCreateGraph.setDisable(false);
		tbSelect.setDisable(false);		
		tbCancel.setDisable(false);
		tbgActions.selectToggle(tbCreateGraph);
		btnSave.setDisable(false);
	}

	private void initializeToggles() {
		tbCreateGraph.setId("GRAPH");
    	tbCreateGraph.setToggleGroup(tbgActions);
    	tbSelect.setId("SELECT");
    	tbSelect.setToggleGroup(tbgActions);   
    	tbCancel.setId("CANCEL");
    	tbCancel.setToggleGroup(tbgActions); 
    	tbgActions.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			public void changed(ObservableValue<? extends Toggle> ov,
    	        Toggle toggle, Toggle new_toggle) {
    	          if(new_toggle == tbCreateGraph){
    	        	  interactor.prepareAction(ActionType.ADD_MULTIPLE_EDGES);
    	        	  if(circleSelectedNode != null){
    	        		  interactor.addGraphNode(selectedNode);
    	        	  }
    	          }
    	          if(new_toggle == tbSelect){
    	        	  interactor.prepareAction(ActionType.SELECT_NODE);
    	          }
    	          if(new_toggle == tbCancel){
    	        	  interactor.cancelAction();
    	        	  removeSelectedNode();
    	          }
    	         }
    	});
	}

	private void initializeBtnSave(final Stage primaryStage) {
		btnSave = ButtonBuilder.create()
		         .text("Save")
		         .build();
		  
		 btnSave.setOnAction(new EventHandler<ActionEvent>() {

		   @Override
		   public void handle(ActionEvent event) {
		       FileChooser fileChooser = new FileChooser();

		       //Set extension filter
		       FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		       fileChooser.getExtensionFilters().add(extFilter);
		       
		       //Show save file dialog
		       File file = fileChooser.showSaveDialog(primaryStage);
		       
		       if(file != null){
		           saveFile(interactor.getEdgeListAsString(), file);
		       }
		   }
   });
	}

	private void saveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
             
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
           System.out.println("GraphGeneratorApp.saveFile()" + ex);
        }
         
    }
	
	protected void initialieClickableArea() {
		clickableArea.setPadding(new Insets(0, 0, 0, 0));
    	clickableArea.setStyle("-fx-background-color: black;");

		scrClickableArea.setPrefWidth(800);
		scrClickableArea.setPrefHeight(600);

	    clickableArea.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				interactor.addGraphNode(new Node((int)event.getX(), (int)event.getY()));
				
			}

			
		});
	    
	    scrClickableArea.setContent(clickableArea);
	}

	protected void initializevBoxMenu() {
		vBoxMenu.getChildren().addAll(lblBgFile, btnOpenBgFile, tbCreateGraph, tbSelect, tbCancel, lblDebug, btnSave);
		vBoxMenu.setPrefWidth(150);
		vBoxMenu.setMaxWidth(150);
		vBoxMenu.setMinWidth(150);
	}

	protected void initializebtnOpenBgFile() {
		btnOpenBgFile.setText("Open background file");
        
        btnOpenBgFile.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
   
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                fileChooser.getExtensionFilters().add(extFilter);
               
                //Show open file dialog
                file = fileChooser.showOpenDialog(null);
                if(file != null){
	                lblBgFile.setText(file.getPath());
	                
	                Image img1;
					try {
						img1 = new Image(new FileInputStream(file.getPath()));
						clickableArea.setMaxHeight(img1.getHeight());
						clickableArea.setMaxWidth(img1.getWidth());
						clickableArea.setPrefWidth(img1.getWidth());
						clickableArea.setPrefHeight(img1.getHeight());
						scrClickableArea.setPrefWidth(img1.getWidth());
						scrClickableArea.setPrefHeight(img1.getHeight());
						ImageView imgView = new ImageView(img1);
						imgView.relocate(0, 0);
						clickableArea.getChildren().add(imgView);
						
						enableActions();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
               }
            }
        });
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void modelChanged(ModelChangedEvent event) {
		switch (event.getType()) {
		case NODE_ADDED:
			removeSelectedNode();
			paintNewNode(event.getNode());
			break;
		case EDGE_ADDED:
			removeSelectedNode();
			paintNewEdge(event.getEdge());
			break;
		case NODE_SELECTED:
			removeSelectedNode();
			selectedNode = event.getNode();
			paintSelectedNode(selectedNode);
			break;

		default:
			break;
		}
		
	}
	
	private void removeSelectedNode() {
		if(circleSelectedNode != null){
			clickableArea.getChildren().remove(circleSelectedNode);
			circleSelectedNode = null;
			selectedNode = null;
		}
	}

	private void paintNewNode(Node node) {
		Circle circle = new Circle();
		circle.setFill(Color.RED);
		circle.setCenterX(node.getX());
		circle.setCenterY(node.getY());
		circle.setRadius(NODE_RADIUS);
		
		clickableArea.getChildren().add(circle);
		lblDebug.setText("Added node: " + node.getX() + " " + node.getY());
	}
	
	private void paintSelectedNode(Node node) {
		circleSelectedNode = new Circle();
		circleSelectedNode.setFill(Color.GREEN);
		circleSelectedNode.setCenterX(node.getX());
		circleSelectedNode.setCenterY(node.getY());
		circleSelectedNode.setRadius(NODE_RADIUS_SELECTED);
		
		clickableArea.getChildren().add(circleSelectedNode);
		lblDebug.setText("Selected node: " + node.getX() + " " + node.getY());
	}
	
	private void paintNewEdge(Edge edge) {
		Line line = new Line();
		line.setFill(Color.RED);
		line.setStartX(edge.getNode1().getX());
		line.setStartY(edge.getNode1().getY());
		line.setEndX(edge.getNode2().getX());
		line.setEndY(edge.getNode2().getY());
		line.setStrokeWidth(EDGE_STROKE);
		
		clickableArea.getChildren().add(line);

	}
}
