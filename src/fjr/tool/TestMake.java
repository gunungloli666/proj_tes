package fjr.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestMake extends Application{

	int num  = 2; 
	Group[] groupCollection = new Group[num];
	ImageView view[] = new ImageView[num]; 
	
	String url[] = new String[]{
			"E:/testimage/1.jpg", 
			"E:/testimage/2.jpg"
	};
	
	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group(); 
		stage.setScene(new Scene(root, 500, 400)); 	
		
		HBox box = new HBox(){{
			setSpacing(10); 
		}};
		
	
		
		box.getChildren().addAll(new VBox(){{
			setSpacing(5); 
			getChildren().addAll(new  Button("ADD LABEL"){{
				setPrefWidth(100);
				setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {	
						int yy = 0 ; 
						for(int i=0; i< num; i++){
							double width = view[i].getImage().getWidth(); 
							Text text = new Text("AA"); 
							text.setTranslateX(width - 20);
							text.setTranslateY(yy); 
							groupCollection[i].getChildren().add(text); 
							yy += view[i].getImage().getHeight(); 
						}
					}
				});
			}}, new Button("REMOVE LABEL"){{
				setPrefWidth(100); 
				setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {	
						for(int i=0; i< num;i++){
							Group g = groupCollection[i]; 
							List<Node> listnode = new ArrayList<>(); 
							for(Node node : g.getChildren()){
								if(node instanceof Text){
									listnode.add(node);
								}
							}
							g.getChildren().removeAll(listnode); 
						}
					}
				});
			}}); 
		}}, 
		
		new ScrollPane(){{
			setPrefSize(370, 300);
			setContent(	new VBox(){{
				setSpacing(5);
				for(int i=0; i< num; i++){
					final int j = i; 
					groupCollection[i] = new Group(view[i] = new ImageView() {{
						setImage(new Image(new File(url[j]).toURI().toString()));
					}}); 
					getChildren().add(groupCollection[i]); 
				}
						
			}});
		}}
				); 
		
		root.getChildren().add(box); 
		stage.show(); 
	}
	
	public static void main(String[] args){
		launch(args); 
	}
}
