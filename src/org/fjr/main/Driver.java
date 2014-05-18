package org.fjr.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Driver extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		
		final Process sph = new Process();
		
		Scene scene = new Scene(sph.getRoot(), sph.getWidth(), sph.getHeight(), true);
		
		primaryStage.setScene(scene);
		
		
		if (sph.usingSphere()) {
			scene.setFill(Color.rgb(10, 10, 40));
		}
		
		primaryStage.setTitle("SPH Test");
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent arg0) {	
				sph.closeWriter(sph.getWriter());
			}
		});
		
		
		primaryStage.show();
		
	}

	public static void main(String[] args){launch(args);}
}
