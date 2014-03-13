package fjr.tool;

//import java.awt.Canvas;

import java.awt.image.BufferedImage;
import java.io.File;

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
	}
	
    BufferedImage finalImage ; 
    BufferedImage[] tempImage; 

	private void iniGUI() {
		tempImage = new BufferedImage[num];
		nodeGraphics = new VBox(){{
			setSpacing(0);
			}}; 
			
		snapshotButton = new Button("SNAPSHOT"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {	
					calculateMaximumSize();

					createSnapshoot(nodeGraphics);
				}
			});
		}}; 
		
		buttonReset = new Button("RESET"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {	
					
				}
				
			});
		}}; 
		
		overlay = new Button("OVERLAY"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
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
		}}; 
		
		box = new VBox() {{
			setSpacing(5);
			setTranslateY(10);
		}};	
	    
		for (int i = 0; i < buttons.length; i++) {
			final int j = i; 
			buttons[i] = new Button("ADD") {{
					setPrefWidth(60);
					setOnAction(new EventHandler<ActionEvent>() {
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
				}};
				
				
		   final Button b = new Button("CLEAR"){{
			   setPrefWidth(60); 
			   setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					removeImage(views[j]);
					enableButton(buttons[j]); 
				}
			});
		   }}; 
		   
			HBox boxH = new HBox(){{
				setSpacing(5); 
				getChildren().addAll(buttons[j],b); 

			}}; 
				
			box.getChildren().add(boxH);
		}
		
		addLabel = new Button("LABEL"){{
			setPrefWidth(100);
			setOnAction(new EventHandler<ActionEvent >() {
				@Override
				public void handle(ActionEvent arg0) {
					addLabeltoImage();
				}
			});
		}}; 
		
		removeLabel = new Button("REMOVE"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					removeLabelfromImage(); 
				}
			});
		}}; 
		
		// tambahkan control operasi
		box.getChildren().addAll(snapshotButton, overlay, 
				buttonReset , addLabel, removeLabel 
				); 
		
		// tambahkan imageview ke box
		for(int i=0; i< num; i++){
			final int j = i; 
			nodeGraphics.getChildren().add(
						collectObject[i] = new 
						Group(){{
							getChildren().add(views[j] = new ImageView());
						}} 
					); 
		}
		
		// tambahkan box ke scrollpane
		root.getChildren().add(
				new HBox() {{
				setSpacing(10);
				getChildren().addAll(
						new ScrollPane() {{
						setPrefSize(width, height);
						setContent(nodeGraphics);
					}}, 
					box);
			}}
		);
	}
	
	Stage secondStage; 
	
	private void createSnapshoot(Node  node){
		secondStage = new Stage();
		Group root  = new Group(); 
		VBox box = new VBox(); 
		box.setSpacing(5);
		
		wim = new WritableImage(((int) imageWidth) , 
				((int) imageHeight ));
		
		node.snapshot(null, wim); 
		
		box.getChildren().addAll(
				new ScrollPane(){{
					setPrefSize(400, 400);
					setContent(
							new ImageView(){{
								setImage(wim); 
							}}); 
				}}); 
		
		final BufferedImage image = 
				SwingFXUtils.fromFXImage(wim, null);
		
		box.getChildren().add(
				new HBox(){{
					setSpacing(0);
					getChildren().addAll(
							new Button("SAVE"){{
								setPrefWidth(100);
								setOnAction(new
										EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0) {
										saveImage(image);
									}
								});
							}});
				}}); 
		
		root.getChildren().add(box); 
		secondStage.setScene(new Scene(root));
		secondStage.show(); 
	}
	
	private void saveImage(BufferedImage image){
		FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(filter2);
        fileChooser.setInitialDirectory(f);
		try {
			File ff = fileChooser.showSaveDialog(stage);
			if (ff != null) {
				ImageIO.write(image, "png", ff);
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
	
	int a; 

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
		for(int i=0; i< num; i++){
			if(views[i].getImage() != null){
				double width = views[i].getImage().getWidth(); 
				Text text = new Text(huruf[i]);
				text.setFont(Font.font(null, FontWeight.BOLD, 18)) ;
				text.setTranslateX(width - 20); 
				text.setTranslateY(20);
				collectObject[i].getChildren().add(text); 
			}
		}
	}
	
	private void removeLabelfromImage(){
		for(int i=0; i< num ;i++){
			for(Node node: collectObject[i].getChildren()){
				if(node instanceof Text){
					System.out.println(((Text) node).getText()); 
				}
			}
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
