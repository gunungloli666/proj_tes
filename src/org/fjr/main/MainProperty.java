package org.fjr.main;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Circle;

import org.fjr.kernel.WendlandKernel;
import org.fjr.main.Process.TypeDrawer;
import org.fjr.particle.SPHParticle;

import fjr.savetoimage.SaveCanvasToImage;

public class MainProperty {
	
	public static double sphRestDens = .2;
	public static double sphStiff = 3.0;
	public static double sphStiffNear = 10.0;
	public static double g_timeMult = 0.40;
	public static final double gravity = -9.8D;
//	public static double g_sphRestDensity = 4.5;
	public static double g_radius = 0.01;
	public static double density = 0.01;
	public static double massa_air = 10.0;
	public static double massa_oil_ternormalisir = 8.0;

}
