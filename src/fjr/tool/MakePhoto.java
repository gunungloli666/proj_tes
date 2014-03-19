package fjr.tool;

//import java.awt.Canvas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MakePhoto extends Application {

	Canvas canvas;
	GraphicsContext gc;
	BufferedImage buffer;
	double width = 400, height = 400;
	double canvasHeight = 5000;
	double canvasWidth = 800; 
	
	int imageWidth = 0, imageHeight =0;  
	Group root;
	int num  = 7; 
	Button[] buttons = new Button[num];
	File[] fileImages = new File[num]; 

	Button snapshotButton; 
	Button mergeImageButton ; 
	Button buttonReset ; 
	Button overlay; 
	
	Button addLabel, removeLabel; 
	
	VBox box;
	VBox nodeGraphics; 

	ImageView views[] = new ImageView[num]; 
	
	String[] huruf = {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
			"U", "V", "W", "X", "Y", "Z" 
	}; 
	
	Group[] collectObject = new Group[num]; 
	
	File f = new File("E:/TESIS/final TESIS/gambar tesis/"); 
	WritableImage wim;
	FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(
			"Image Files", "*.png", "*.jpg");
	FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(
			"PNG FIle Files", "*.png");
	Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage; 
		root = new Group();
		primaryStage.setScene(new Scene(root, width + 150, height + 100 ));
		primaryStage.show();
		iniGUI();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				if(secondStage != null ){
					secondStage.close();
				}
			}
		});
	}
	
    BufferedImage finalImage ; 
    BufferedImage[] tempImage; 

	private void iniGUI() {
		tempImage = new BufferedImage[num];
		nodeGraphics = new VBox();
		nodeGraphics.setSpacing(0);
			
		snapshotButton = new Button("SNAPSHOT");
		snapshotButton.setPrefWidth(100); 
		snapshotButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {	
				calculateMaximumSize();
				createSnapshoot(nodeGraphics);
			}
		});
		
		buttonReset = new Button("RESET");
		buttonReset.setPrefWidth(100); 
		buttonReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {	
				resetImage(); 
			}
			
		});
		
		overlay = new Button("OVERLAY");
		overlay.setPrefWidth(100); 
		overlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				calculatePrefferedSize(); 
				Group tempNode = new Group(); 
				for(int i=0; i< num; i++){
					if(( views[i].getImage()) != null){
						ImageView tempView = new ImageView(); 
						tempView.setImage(views[i].getImage());
						tempView.setBlendMode(BlendMode.DARKEN);
						tempNode.getChildren().add(tempView) ;  
					}
				}
				createSnapshoot(tempNode); 			
			}
		});
		
		box = new VBox();
		box.setSpacing(5);
		box.setTranslateY(10);
	    
		for (int i = 0; i < buttons.length; i++) {
			final int j = i; 
			buttons[i] = new Button("ADD");
			buttons[i].setPrefWidth(60);
			buttons[i].setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.getExtensionFilters().add(filter1);
					fileChooser.setInitialDirectory(f);
					File ff = fileChooser.showOpenDialog(stage);
					if(ff != null){
						addGraphicsToGroup(ff, views[j]);
						disableButton(buttons[j]);
					}
				}
			});
				
				
			final Button b = new Button("CLEAR");
			b.setPrefWidth(60);
			b.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					removeImage(views[j]);
					enableButton(buttons[j]);
				}
			});
		   
			HBox boxH = new HBox();
			boxH.setSpacing(5); 
			boxH.getChildren().addAll(buttons[j],b); 
				
			box.getChildren().add(boxH);
		}
		
		addLabel = new Button("LABEL");
		addLabel.setPrefWidth(100);
		addLabel.setOnAction(new EventHandler<ActionEvent >() {
			@Override
			public void handle(ActionEvent arg0) {
				addLabeltoImage();
			}
		});
		
		
		removeLabel = new Button("REMOVE");
		removeLabel.setPrefWidth(100); 
		removeLabel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeLabelfromImage(); 
			}
		});
		
		// tambahkan control operasi
		box.getChildren().addAll(snapshotButton, overlay, 
				buttonReset , addLabel, removeLabel); 
		
		// tambahkan imageview ke box
		for (int i = 0; i < num; i++) {
			ImageView view = new ImageView();
			Group gr = new Group();
			gr.getChildren().add(view);
			collectObject[i] = gr;
			views[i] = view;
			nodeGraphics.getChildren().add(collectObject[i]);
		}
		
		// tambahkan box ke scrollpane
		HBox box11 = new HBox();
		box11.setSpacing(10);
		
		ScrollPane scrollPane11 = new ScrollPane(); 
		scrollPane11.setPrefSize(width, height);
		scrollPane11.setContent(nodeGraphics);
	
		box11.getChildren().addAll(scrollPane11, box); 
		root.getChildren().add(box11);
	}
	
	Stage secondStage; 
	
	private void createSnapshoot(Node  node){
		secondStage = new Stage();
		Group root  = new Group(); 
		VBox box = new VBox(); 
		box.setSpacing(5);
	
		int numofexistingimage = 0;
		
		for(int i=0; i< num;i++){
			if(views[i].getImage() != null){
				numofexistingimage++; 
			}
		}
		if(numofexistingimage == 0 ){
			return; 
		}
		
		wim = new WritableImage(((int) imageWidth) , 
				((int) imageHeight ));
		
		node.snapshot(null, wim); 
		
		ImageView imageView = new ImageView();
		imageView.setImage(wim);
		
		
		ScrollPane scrollPaneImageView = new ScrollPane(); 
		scrollPaneImageView.setPrefSize(400, 400);
		scrollPaneImageView. setContent(imageView);
		
		box.getChildren().addAll( scrollPaneImageView); 
		
		final BufferedImage image = 
				SwingFXUtils.fromFXImage(wim, null);
		
		HBox boxSave = new HBox();
		boxSave.setSpacing(0);
		
		Button buttonSave = new  Button("SAVE"); 
		buttonSave.setPrefWidth(100);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				saveImage(image);
			}
		});
		
		boxSave.getChildren().addAll(buttonSave);
		box.getChildren().add(boxSave);
		
		root.getChildren().add(box); 
		secondStage.setScene(new Scene(root));
		secondStage.show(); 
	}
	
	private void saveImage(BufferedImage image){
		FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter2);
        fileChooser.setInitialDirectory(f);
		try {
			File ff = fileChooser.showSaveDialog(secondStage);
			if (ff != null) {
				ImageIO.write(image, "png", ff);
				secondStage.close();
			}
		} catch (Exception e) {}
	}
	
	private void enableButton(Button b ){
		b.setDisable(false);
	}
	
	private void disableButton(Button b){
		b.setDisable(true); 
	}

	private void  addGraphicsToGroup(File f, ImageView view){
		Image im = new Image(f.toURI().toString()); 
		view.setImage(im); 
		imageHeight += im.getHeight(); 
	}
	
	private void removeImage(ImageView view ){
		if(view != null && view.getImage() != null ){
			int height = (int)view.getImage().getHeight(); 
			imageHeight -= height;
			view.setImage(null);
		}
	}
	
	private void resetImage(){
		for(int i=0; i< num; i++){
			removeImage(views[i]);
		}
		removeLabelfromImage();
	}
	
	private void calculatePrefferedSize(){
		imageWidth = 0; 
		imageHeight = 0;
		for(int i=0; i< num; i++){
			Image im = views[i].getImage();
			if(im!= null){
				if(im.getWidth() > imageWidth)
					imageWidth = (int) im.getWidth(); 
				if(im.getHeight() > imageHeight)
					imageHeight = (int) im.getHeight(); 
			}
		}
	}
	
	private void addLabeltoImage(){
		int iterlabel = 0; 
		for(int i=0; i< num; i++){
			if(views[i].getImage() != null){
				double width = views[i].getImage().getWidth(); 
				Text text = new Text(huruf[iterlabel++]);
				text.setFont(Font.font(null, FontWeight.BOLD, 18)) ;
				text.setTranslateX(width - 20); 
				text.setTranslateY(20);
				collectObject[i].getChildren().add(text); 
			}
		}
	}
	
	private void removeLabelfromImage(){
		for(int i=0; i< num ;i++){
			List<Node> listNode = new ArrayList<>();   
			for(Node node: collectObject[i].getChildren()){
				if(node instanceof Text){
					listNode.add(node); 
				}
			}
			collectObject[i].getChildren().removeAll(listNode);
		}
	}
	
	private void calculateMaximumSize(){
		imageWidth = 0; 
		imageHeight = 0;
		for(int i=0; i< num; i++){
			Image im = views[i].getImage();
			if(im!= null){
				if(im.getWidth() > imageWidth)
					imageWidth = (int) im.getWidth(); 
				imageHeight += im.getHeight(); 
			}
		}
	}
	
}
