package fjr.tool;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddImageView extends Application{

	Group root ; 
	double width = 600;
	double height = 600; 
	int num = 6;
	ImageView imageView[] = new ImageView[num]; 
	Button[] btn = new Button[num]; 
	
	
	@Override
	public void start(Stage stage) throws Exception {
		root  = new Group();
		Scene sc = new Scene(root , width, height); 
		stage.setScene(sc); 
		initComponent();
		stage.show();
	}
	
	public void initComponent(){
		final VBox box = new VBox(){{
			for(int i=0; i< num; i++){
				getChildren().add(
						imageView[i] = new ImageView(){{
							
						}} 
						); 
			}
		}}; 
		root.getChildren().add(new HBox(){{
			getChildren().addAll(box); 
		}}
		); 
	}
	

	public static void main(String[] args){
		launch(args);
	}
	
}
