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
	Group root;

	int num  = 5; 
	
	Button[] buttons = new Button[num];
	
	File[] fileImages = new File[num]; 

	Button snapshotButton; 
	Button mergeImageButton ; 
	Button buttonReset ; 
	Button overlay; 
	
	VBox box;
	
	File f = new File("E:/gambar tesis/"); 
	
	WritableImage wim;
	
	FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(
			"Image Files", "*.png", "*.jpg");
	
	FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(
			"PNG FIle Files", "*.png");
	Stage stage;
	
    BufferedImage finalImage ; 

    BufferedImage[] tempImage  ; 
	public static void main(String[] args) {
		launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage; 
		root = new Group();
		primaryStage.setScene(new Scene(root, width + 150, height));
		primaryStage.show();
		iniGUI();
	}


	public void iniGUI() {
		
		snapshotButton = new Button("SNAPSHOT"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {	
					wim = new WritableImage(((int) x ) , 
							((int) y ));
					canvas.snapshot(null, wim); 
					BufferedImage bufferedImage = new 
							BufferedImage((int) x, (int) y,
							BufferedImage.TYPE_INT_ARGB); 
					BufferedImage image ; 
					try{
						image = SwingFXUtils.
								fromFXImage(wim, bufferedImage);
						 FileChooser fileChooser = new FileChooser();
			              //Set extension filter
			            
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
					y = 0; 
					x = 0; 
					
					iter = 0; 
					for(int i=0; i< buttons.length; i++){
						buttons[i].setDisable(false); 
						buttons[i].setText("..."); 
					}
					
					gc.setFill(Color.WHITE);
					gc.fillRect(0, 0, canvasWidth, canvasHeight);
 				}
				
			});
		}}; 
		
	overlay = new Button("OVERLAY"){{
			setPrefWidth(100); 
			setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					
					FileChooser fileChooser = new FileChooser();
		            fileChooser.getExtensionFilters().add(filter2);
		            fileChooser.setInitialDirectory(f);
		            
		            Group group = new Group(); 
		            
		            int x = 0; 
		            Group temp = new Group(); 
		            // tempImage is array of buffered Images
		            for(int i=0; i < tempImage.length ;i++){
		            	  if(ims[i] != null ){  

		            		 ImageView view = new ImageView();
		            		 
		            		 view.setImage(ims[i]);
		            		 
		            		 group.getChildren().add(view);
		            		 
		            		 ImageView view2 = new ImageView(); 
		            		 view2.setImage(ims[i]);
		            		 
		            		 view.setTranslateX(x);
		            		 
		            		 temp.getChildren().add(view2); 
		            		 x+= ims[i].getWidth(); 
		            	  }
		              }
		            
		            

		            Stage ss = new Stage();
		            ss.setScene(new Scene(temp  , x , tempImage[0].getHeight())); 
		            ss.show();
		            
		              
		        	wim = new WritableImage(((int) tempImage[0].getWidth() ) , 
							((int) tempImage[0].getHeight() ));
		        	
					box.snapshot(null, wim); 
					
					BufferedImage image;
					try {
						image = SwingFXUtils.fromFXImage(wim, null);
						File ff = fileChooser.showSaveDialog(stage);
						if (ff != null) {
							ImageIO.write(image, "png", ff);
						}
					} catch (Exception e) {
					}
					
		  		  
				}
			});
		}}; 
		
		
		box = new VBox() {{
				setSpacing(10);
				setTranslateY(10);
		}};	
	    
		for (int i = 0; i < buttons.length; i++) {
			final int j = i; 
			buttons[i] = new Button("...") {{
					setPrefWidth(100);
					setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							   FileChooser fileChooser = new FileChooser();
					              fileChooser.getExtensionFilters().add(filter1);
					              fileChooser.setInitialDirectory(f);
					              fileImages[j] = fileChooser.showOpenDialog(stage);         
					          if(fileImages[j] != null ){
					        	    addGraphicsToNode(fileImages[j]) ;
					        	    setText("SUDAH");
					        	    setDisable(true);
					          }
						}
					});
				}};
				
			box.getChildren().add(buttons[i]);
		}
		
		box.getChildren().addAll(snapshotButton,
				mergeImageButton, overlay, 
				buttonReset ); 
		canvas = new Canvas(canvasWidth, canvasHeight);
		gc = canvas.getGraphicsContext2D();

		root.getChildren().add(
				new HBox() {{
				setSpacing(10);
				getChildren().addAll(new ScrollPane() {{
						setPrefSize(width, height);
						setContent(canvas);
					}}, 
					box);
			}}
		);
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);
	}
	
	double y = 0 ; 
	double x = 0; 
	int iter = 0; 
	
	Image[] ims = new Image[num]; 
	
	public void addGraphicsToNode(File f){
		Image im = new Image(f.toURI().toString());
		ims[iter] = im; 
		try{
			tempImage[iter++] = ImageIO.read(f); 
			
		}catch(Exception e){}
		gc.drawImage(im,0, y ,  im.getWidth(), im.getHeight());
		y +=   im.getHeight(); 
		if(x < im.getWidth() )
			x = im.getWidth() ; 
	}
	
	
	public void addGraphicsToGroup(Node node , File f){
//	Ima
	}

}
