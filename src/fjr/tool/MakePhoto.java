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
import javafx.scene.paint.Color;
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

	int num  = 10; 
	
	Button[] buttons = new Button[num];
	File[] fileImages = new File[num]; 

	Button snapshotButton; 
	Button mergeImageButton ; 
	Button buttonReset ; 
	Button overlay; 
	
	VBox box;
	
	VBox nodeGraphics; 
	
	
	
	ImageView views[] = new ImageView[num]; 
	
	String[] huruf = {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", 
			"U", "V", "W", "X", "Y", "Z" 
	}; 
	
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
		primaryStage.setScene(new Scene(root, width + 150, height + 200 ));
		primaryStage.show();
		iniGUI();
	}
	
    BufferedImage finalImage ; 
    BufferedImage[] tempImage; 

	private void iniGUI() {
		tempImage = new BufferedImage[num];
		
		nodeGraphics = new VBox(){{
			setSpacing(5);
			}}; 
				
		snapshotButton = new Button("SNAPSHOT"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {	
					wim = new WritableImage(((int) imageWidth) , 
							((int) imageHeight ));
					BufferedImage image ; 
					try{
						image = SwingFXUtils.
								fromFXImage(wim, null );
						 FileChooser fileChooser = new FileChooser();
						 
			              fileChooser.getExtensionFilters().add(filter2);
			              fileChooser.setInitialDirectory(f);
			              File ff = fileChooser.showSaveDialog(stage);     
			              if(ff != null) {
			            		ImageIO.write(image, "png",
			        					ff);
			              }
					}catch(Exception e){
						
					}
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
					
					FileChooser fileChooser = new FileChooser();
		            fileChooser.getExtensionFilters().add(filter2);
		            fileChooser.setInitialDirectory(f);            
		            
		            wim = new WritableImage(imageWidth, imageHeight);
					nodeGraphics.snapshot(null, wim); 
					BufferedImage image;
					try {
						image = SwingFXUtils.fromFXImage(wim, null);
						File ff = fileChooser.showSaveDialog(stage);
						if (ff != null) {
							ImageIO.write(image, "png", ff);
						}
					} catch (Exception e) {}
				}
			});
		}}; 
		
		box = new VBox() {{
			setSpacing(10);
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
		
		// tambahkan control operasi
		box.getChildren().addAll(snapshotButton,
				mergeImageButton, overlay, 
				buttonReset 
				); 
		
		// tambahkan imageview ke box
		for(int i=0; i< num; i++){
			nodeGraphics.getChildren().add(
						views[i] = new ImageView()
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
	
	
	private void enableButton(Button b ){
		b.setDisable(false);
	}
	
	private void disableButton(Button b){
		b.setDisable(true); 
	}
	
	
	int image_iter = 0; 

	private void  addGraphicsToGroup(File f, ImageView view){
		Image im = new Image(f.toURI().toString()); 
		view.setImage(im); 
		imageHeight += im.getHeight(); 
	}
	
	private void removeImage(ImageView view ){
		if(view != null){
			int height = (int)view.getImage().getHeight(); 
			imageHeight -= height;
			view.setImage(null);
		}
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
	
	private void calculateMaximumSize(){
		imageWidth = 0; 
		imageHeight = 0;
		for(int i=0; i< num; i++){
			Image im = views[i].getImage();
			if(im!= null){
				if(im.getWidth() > imageWidth)
					imageWidth = (int) im.getWidth(); 
				imageHeight+= im.getHeight(); 
			}
		}
	}
	
	double y = 0 ; 
	double x = 0; 
	int iter = 0; 
	Image[] ims = new Image[num]; 
	public void addGraphicsToNode(File f){
		Image im = new Image(f.toURI().toString());
		ims[iter] = im; 
		try{
			tempImage[iter] = ImageIO.read(f); 
			
		}catch(Exception e){}
		gc.drawImage(im,0, y ,  im.getWidth(), im.getHeight());
		y +=   im.getHeight(); 
		if(x < im.getWidth() )
			x = im.getWidth() ; 
		iter++; 
	}

}
