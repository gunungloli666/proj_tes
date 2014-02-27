package org.fjr.main;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Driver extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		Process sph = new Process();
		Scene scene = new Scene(sph.getRoot(),
				sph.getWidth(), sph.getHeight(),
				true);
		primaryStage.setScene(scene);
		if (sph.usingSphere()) {
			scene.setFill(Color.rgb(10, 10, 40));
		}
		primaryStage.show();
		primaryStage.setTitle("SPH Test");
	}

	public static void main(String[] args){launch(args);}
}
