package org.fjr.main;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import org.fjr.neighboor.DoubleInteraction;
import org.fjr.neighboor.InteractionType;
import org.fjr.neighboor.ParallelInteraction;
import org.fjr.particle.Particle;
import org.fjr.particle.SPHParticle;
import org.fjr.particle.TypeFluid;
import org.fjr.tree.Node;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import org.fjr.kernel.Kernel;
import org.fjr.kernel.WendlandKernel;
import org.fjr.particle.SPhysicsParticle;
import org.fjr.particle.TypeParticle;

import signalprocesser.voronoi.VPoint;
import signalprocesser.voronoi.VoronoiAlgorithm;
import signalprocesser.voronoi.representation.triangulation
				.TriangulationRepresentation;
import signalprocesser.voronoi.representation.triangulation.VHalfEdge;
import fjr.savetoimage.SaveCanvasToImage;

public class Process {

    private ArrayList<SPHParticle> listFluid;
    private ArrayList<Circle> listCircle;

    private static double smoothingLength = 3.8
            * MainProperty.g_radius;
    
    private double xwidth = 500.0;
    private double ywidth = 500.0;
    
    private double canvasWidth  = xwidth ; 
    private double canvasHeight = ywidth - 150.0 ; 
    
    private DecimalFormat format = new 
    		DecimalFormat("###.##");
    
    private double xMax = 3.0;
    private double xMin = 0.0;
    private double yMax = 3.0;
    private double yMin = 0.0;

    private double xMaxLinkedList = xMax; 
    private double yMaxLinkedList = yMax; 
    private double xMinLinkedList = xMin; 
    private double yMinLinkedList = yMin; 
    
    private static double timeStep = 1.5;

    public MarchingType marchingtype
            = MarchingType.Fluids_Javascript;
    
    private Timeline animation;
    private Button buttonPause;
    private Button buttonPlay;
    private Button buttonRestart;
    private Button buttonGenerateOil;

    private double dtFrame = 0.0;
    
    private double g_SubStep = 1.0;
    
    private double drawingFactorX = (xwidth - 0.0) 
    		/ (xMax - xMin);
    
    private double drawingFactorY = (ywidth - 0.0) 
    		/ (yMax - yMin);
    
    private final GraphicsContext gc;

    private double maxRange = 1.0 * smoothingLength;

    private ArrayList<SPHParticle> listBoundary
            = new ArrayList<>();
    public ArrayList<SPHParticle> allParticle;

    ArrayList<SPhysicsParticle> allsphysicsParticle
            = new ArrayList<>();
    ArrayList<SPhysicsParticle> fluidsphysics=
    		new ArrayList<>();
    ArrayList<SPhysicsParticle> boundarysphysicsq
            = new ArrayList<>();
    ArrayList<SPhysicsParticle> oilsphysics =
    		new ArrayList<>();

    // step posisi awal 
    private double step;
    
    private boolean usingStaggeredGrid = true;
    private boolean usingBoundaryParticle = false;
    int cycleCount = Timeline.INDEFINITE;

    final Canvas canvas;
    final Group root;
    ForkJoinPool pool;
    ParallelInteraction<SPHParticle> paralelProcess;
    private double yDomainOfFluida = yMax * 0.33;
    private double xDomainOfFluida = xMax * 0.33;

    double sceneWidth = 700, sceneheight = 500;

    private ArrayList<SPHParticle> listOilParticle;

    public enum TypeDrawer{sphere, circle, canvas}

    public enum TypeSimulasi{singlePhase, twoPhase;}

    TypeSimulasi typeSimulasi = TypeSimulasi.twoPhase;
    
    private TypeDrawer typeDrawer;

    TypeInteraction typeInteraction = TypeInteraction
    		.parallel;

    public double getWidth() {
        return xwidth;
    }

    public double getHeight() {
        return ywidth;
    }

    public TypeDrawer getTypeDrawer() {
        return typeDrawer;
    }

    // untuk controller simulasi 
    Slider sphStiffSlider, sphStiffNearSlider,
            sphRestDenstSlider,
            timeMultSlider;
    Text textStiff, textStiffNear, textRestDenst,
            textTimeMult;
    Text keteranganStiff, keteranganStiffNear,
            keteranganRestDenst,
            keteranganTimeMult;

    ToggleGroup tipeInteraksi;
    RadioButton radioButton1, radioButton2, radioButton3;

    ToggleGroup toggleGroupTotalAnimation;
    RadioButton animasi_50, animasi_100, animasi_inf;

    ToggleGroup toggleGroupTypeAnimation;
    ToggleButton toggleButtonFastAnimation,
            toggleButtonSlowAnimation;
    
    ComboBox<Integer> comboJumlahSimulasi;

    // untuk memilih kecepatan animasi 
    RadioButton slowAnimasi, fastAnimasi,
            medium_speed_animasi;

    // untuk menggambar perimeter dan convex hull
    CheckBox usingConvexHull;
    
    CheckBox usingPerimeter;

    final double textX = -50;

    public boolean usingSphere() {
        if (typeDrawer == TypeDrawer.sphere) {
            return true;
        }
        return false;
    }

    public int gridX;
    public int gridY;

    ArrayList<SPhysicsParticle> listSPHysicsParticle
            = new ArrayList<>();
    SPhysicsParticle[] particleSPH;

    Kernel kernel;

    final String ANIMATION_INFINITY = "INFINITY";

    enum Animation_Type {SLOW_ANIMASI, 
    	FAST_ANIMASI, MEDIUM_SPEED}

    Animation_Type type_animasi = 
    		Animation_Type.SLOW_ANIMASI;
    ArrayList<Integer> numberIterasi =
    		new ArrayList<>();

    SaveCanvasToImage save;
    TextField fieldSnapshootName;

    boolean saveToFile = false;
    boolean drawPerimeter = false;
    boolean drawConvexHull = false;

    CheckBox saveCheckBox;
    
    int iterasiCount = 0;

    AnchorPane anchorpaneController;

    ArrayList<SPHParticle> listWaterParticle
            = new ArrayList<SPHParticle>();

    ArrayList<Point> oilConvexHull, waterConvexHUll,
            unionConvexHull;

    FastConvexHull convexHull;

    CheckBox checkBoxPerimeter, checkBoxConvexHull;
    
    ComboBox<String> comboBoxPerimeter,
            comboBoxConvexHull;

    CheckBox checkBoxConcavHull; 
    
    final String perimeterWater = "WATER";
    final String perimeterOil = "OIL";
    final String perimeterBoth = "BOTH";
    final String perimeterUnion = "UNION";

	final String convexHullWater = "WATER";
	final String convexHullOil = "OIL";
	final String convexHullBoth = "BOTH";
	final String convexHullUnion = "UNION";

    enum PerimeterStates {WATER, OIL, BOTH, UNION}

    enum ConvexHullStates {WATER, OIL, BOTH, UNION}

    String perimeterState = perimeterWater;
    String convexHullState = convexHullOil;

    PerimeterStates perimeterStates = 
    		PerimeterStates.OIL;
    ConvexHullStates convexHullStates = 
    		ConvexHullStates.OIL;

    Text textIterasi ; 
    
	final EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			animasi();
		}
	};

	public void animasi() {
		defaultStep();
		if (drawPerimeter) {
			createPerimeter();
		}
		if (drawConvexHull) {
			createConvexHull();
		}
		if (draWConcaveHull) {
			createConcavHull();
		}
		redrawCircle();
		iterasiCount++;
		setKeteranganIterasi();
	}
         
            public void setKeteranganIterasi(){
            	gc.setStroke(Color.BLACK);
            	gc.strokeText(("ITERASI: "+Integer.toString
            			(iterasiCount) ), 10 , 150 );
            }
            
    ComboBox<TypeFluid> comboBoxFluidaKedua;
    
    TypeFluid fluidaKedua = TypeFluid.OLI;
    
    String fluidaJenisOli = "OLI";
    
    String fluidaJenisGliserin = "GLISERIN";
    
    String tipeFluidaKedua = fluidaJenisOli;
            
    boolean runningState = false; 
                
    // untuk membuat concavHull... 
    TriangulationRepresentation triangular;

    Thread thread; 
    boolean runningThread = true; 
    
    
    Button snapshotButton; 
    
    @SuppressWarnings("unchecked")
    public Process() throws Exception {
		triangular = new TriangulationRepresentation();
		triangular.setCalcCutOff(new 
				TriangulationRepresentation.CalcCutOff() {
			@Override
			public int calculateCutOff(
					TriangulationRepresentation 
					representation) {
				return 12; // kayaknya ini nilai ideal...
			}
		});
		
        kernel = new WendlandKernel();
        this.typeDrawer = TypeDrawer.canvas;
        pool = new ForkJoinPool();
        root = new Group();
        canvas = new Canvas( canvasWidth,
        			canvasHeight );
        
//        canvas.setStyle("-fx-background-color: BLACK");
        
        canvas.setStyle("-fx-background-color: transparent;");
        
        
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
        		new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e){
					}
				}); 
        
        gc = canvas.getGraphicsContext2D();
        listFluid = new ArrayList<>();
        listOilParticle = new ArrayList<>();
        listCircle = new ArrayList<>();
        allParticle = new ArrayList<>();

        gridX = (int) ((xMax - xMin) /
        		(2.0 * smoothingLength));
        gridY = (int) ((yMax - yMin) / 
        		(2.0 * smoothingLength));
        
        hash = new ArrayList[gridX][gridY];

        step = 0.9 * smoothingLength;

        for (int i = 0; i < hash.length; i++) {
            for (int j = 0; j < hash[0].length; j++) {
                hash[i][j] = new ArrayList<>();
            }
        }

        save = new SaveCanvasToImage(canvas);
        
        save.setMainFolder("hasil hasil"); 
        
        fieldSnapshootName = new TextField() {{
                setPrefSize(120,30);
                setText("test");
            }};

        switch (typeInteraction) {
            case singleGrid:
            case doubleGrid:
            case parallel:
                this.initLinkedList();
                break;
            case tree:
                initTree();
                break;
        }

        animation = new Timeline();
        animation.getKeyFrames().addAll(
                new KeyFrame(changeSpeed(), event));
        
        thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(runningThread){
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try{
								Thread.sleep(500);
							}catch(Exception e){
								System.out.println("error"
										+ " thread"); 
							}
						}
					});
				}
			}
		});
        
        buttonPlay = new Button(){{
                setText("PLAY");
                setPrefWidth(100);
                setOnAction(new
                		EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        play();
                    }
                });
            }};

        buttonPause = new Button(){{
                setText("PAUSE");
                setPrefWidth(100);
                setOnAction(new 
                		EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        animation.pause();
                    }
                });
            }};

        buttonRestart = new Button() {{
                setText("RESTART");
                setPrefWidth(100);
                setOnAction(new 
                		EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent arg0) {
                        restart();
                    }
                });
            }};

        buttonGenerateOil = new Button() {{
                setText("ADD POLUTAN");
                setPrefWidth(100);
            }};

        if (typeDrawer == TypeDrawer.canvas) {
         }
        
        root.getChildren().add(
        		new BorderPane() {{
        		setStyle("-fx-background-color: BLACK;");
                getChildren().addAll(canvas);
            }}
        );


        FlowPane flow = new FlowPane();
        flow.setOrientation(Orientation.VERTICAL);
        flow.setVgap(5);
        flow.setTranslateX(10);
        flow.setTranslateY(10);

        flow.getChildren().addAll(buttonPlay,
                buttonPause,
                buttonRestart,
                buttonGenerateOil);
        
        root.getChildren().add(flow);

        convexHull = new FastConvexHull();

        initProperty();

        AnchorPane pane = getControlPane();
        root.getChildren().add(pane);
        pane.setTranslateX(xwidth - 220);

        animation.setCycleCount(cycleCount);
        animation.setAutoReverse(false);

        setKeteranganIterasi(); 
    }

    double deltaX, deltaY, stepPerimeter;
    double margin;

    public void initPerimeter() {
        deltaX = xMax - xMin;
        deltaY = yMax - yMin;
        stepPerimeter = deltaX / numberPerimeter;
        double m = yMin;
        for (int i = 0; i < xIntervalOfWater.length; i++) {
            xIntervalOfWater[i] = m;
            m += stepPerimeter;
        }
        margin = stepPerimeter / 2.5;
    }

    // ini digunakan untuk plot perimeter
    int numberPerimeter = 100;
    
    double[] xIntervalOfWater = 
    		new double[numberPerimeter],
        yIntervalOfWater = new double[numberPerimeter],
    xIntervalOfWaterPlot = new double[numberPerimeter];

    double[] xIntervalOfOilPlot = 
    		new double[numberPerimeter];
    double[] xIntervalOfOil = 
    		new double[numberPerimeter];
    double[] yIntervalOfOil = 
    		new double[numberPerimeter];

    double xMinimumOli, xMaximumOli;
    double xMinimumWater, xMaximumWater;

    ArrayList<Double> xPerimeterWater = new ArrayList<>();
    ArrayList<Double> yPerimeterWater = new ArrayList<>();

    ArrayList<Double> xPerimeterOil = new ArrayList<>();
    ArrayList<Double> yPerimeterOil = new ArrayList<>();

    Slider sliderNumberIterasi; 
    Text textNumberIterasi; 

    boolean usingMaksimumValue = true; 
    boolean usingMedianValue = true; 
    
    public void createPerimeter() {
    	
        xMinimumOli = 10000.0;
        xMaximumOli = -10000.0;
        xMaximumWater = -10000.0;
        xMinimumWater = 100000.0;

        xPerimeterWater.clear();
        yPerimeterWater.clear();
        xPerimeterOil.clear();
        yPerimeterOil.clear();

		for (int i = 0; i < listOilParticle.size(); i++) {
			SPHParticle p = listOilParticle.get(i);
			if (p.getX() < xMinimumOli) {
				xMinimumOli = p.getX();
			} else if (p.getX() > xMaximumOli) {
				xMaximumOli = p.getX();
			}
		}

		for (int i = 0; i < listWaterParticle.size(); i++) {
			SPHParticle p = listWaterParticle.get(i);
			if (p.getX() < xMinimumWater) {
				xMinimumWater = p.getX();
			} else if (p.getX() > xMaximumWater) {
				xMaximumWater = p.getX();
			}
		}

		double deltaOil = xMaximumOli - xMinimumOli;
		double oilInterval = deltaOil / numberPerimeter;
	
		double oilMargin = oilInterval * 0.3;

		double deltaWater = xMaximumWater - xMinimumWater;
		double waterInterval = deltaWater / numberPerimeter;
		double waterMargin = waterInterval * 0.5;

		if (usingMaksimumValue) {
			double waterStep = xMinimumWater;
			for (int i = 0; i < numberPerimeter; i++) {
				double max = -1000;
				boolean change = false;
				for (int j = 0; j < listWaterParticle.size(); j++) {
					SPHParticle p = listWaterParticle.get(j);
					if (p.getX() < (waterStep + waterMargin)
							&& p.getX() >= (waterStep
									-waterMargin)) {
						if (p.getY() > max) {
							max = p.getY();
							change = true;
						}
					}
				}
				// jika nilai max berubah
				if (change) {
					xPerimeterWater.add(waterStep);
					yPerimeterWater.add(max);
				}
				waterStep += waterInterval;
			}

			double oilStep = xMinimumOli;
			for (int i = 0; i < numberPerimeter; i++) {
				double max = -1000;
				boolean change = false;
				for (int j = 0; j < listOilParticle.size(); j++) {
					SPHParticle p = listOilParticle.get(j);
					if (p.getX() < (oilStep + oilMargin)
							&& p.getX() >= (oilStep-
									oilMargin)) {
						if (p.getY() > max) {
							max = p.getY();
							change = true;
						}
					}
				}
				/* jika nilai max berubah... 
				 * karena bisa jadi terdapat jarak yang 
				 * cukup jauh antara partikel yang satu 
				 * dengan partikel lain sehingga nilai ini 
				 * tidak berubah.
				 * */
				
				if (change) {
					xPerimeterOil.add(oilStep);
					yPerimeterOil.add(max);
				}
				oilStep += oilInterval;
			}
		} else if (usingMedianValue) {
			double waterStep = xMinimumWater;
			for(int i=0; i< numberPerimeter; i++){
				double sum = 0.; 
				boolean change = false;
				int count = 0; 
				for(int j=0; j < listWaterParticle. size(); j++){
					SPHParticle p = listWaterParticle.get(j); 
					if(p.getX() < (waterStep + waterMargin )
							&&(p.getX() >= (waterStep-
									waterMargin))){
						sum+= p.getY();
						count++; 
						change = true; 
					}
				}
				if(change){
					sum = sum/count; 
					xPerimeterWater.add(waterStep);
					yPerimeterWater.add(sum);
				}
				waterStep+= waterInterval; 
			}
			double oilStep = xMinimumOli;
			for(int i=0; i<numberPerimeter;i++){
				double sum = 0.; 
				boolean change = false; 
				int count= 0; 
				for(int j=0; j< listOilParticle.size(); j++){
					SPHParticle p = listOilParticle.get(j); 
					if(p.getX() <(oilStep + oilMargin)&&
						p.getX() >= (oilStep - oilMargin)	){
						sum += p.getY(); 
						change = true;
						count ++; 
					}
				}
				if(change){
					sum = sum/count; 
					xPerimeterOil.add(oilStep);
					yPerimeterOil.add(sum);
				}
				oilStep+= oilInterval; 
			}
		}
	}
    
    ArrayList<VPoint> listPoint = new ArrayList<VPoint>(); 
    boolean draWConcaveHull = false;
	VHalfEdge outeredge;
	
    public void  createConcavHull(){
    	listPoint.clear();
    	for(int i=0; i< listOilParticle.size(); i++){
    		SPHParticle p = listOilParticle.get(i);
    		int x = (int) (p.getX() * drawingFactorX);
    		int y = (int) ( p.getY()* drawingFactorY); 
    		listPoint.add(triangular.createPoint(x,y));
    	}

//     outeredge = triangular.getOuterEdge();
    }
    
    public void createConvexHull() {
        oilConvexHull = convexHull.
                execute(listOilParticle);
        waterConvexHUll = convexHull.
                execute(listWaterParticle);
        unionConvexHull = convexHull.
                execute(allParticle);
    }
    
	public void drawConcavHull(GraphicsContext gc, Color c) {
		if (triangular == null)
			System.out.println("triangular null..  ");
		VoronoiAlgorithm.generateVoronoi(triangular, listPoint);
		VHalfEdge outeredge = triangular.getOuterEdge();
		if (outeredge == null || outeredge.next == null) {
			System.out.println("outer edge null... ");
			return;
		}
		gc.setStroke(c);
		VHalfEdge curredge = outeredge;
		do {
			int x1 = curredge.getX();
			int y1 = curredge.getY();
			int x2 = curredge.next.getX();
			int y2 = curredge.next.getY();
			y1 = ((int) canvasHeight) - y1;
			y2 = ((int) canvasHeight) - y2;
			gc.strokeLine(x1, y1, x2, y2);
		} while ((curredge = curredge.next).next != null
				&& curredge != outeredge);
	}
	
    boolean plotParameter = true;

    // simpan canvas ke file png
    public void saveCanvas(int number) {
        String s = Integer.toString(number);
        save.save(s);
    }
    
    public void saveCanvas(String a){
    	String s = "" + a; 
    	save.save(s); 
    }

    Duration durasi = Duration.millis(100);

    public Duration changeSpeed() {
        switch (type_animasi) {
            case FAST_ANIMASI:
                durasi = Duration.millis(20);
                break;
            case SLOW_ANIMASI:
                durasi = Duration.millis(500);
                break;
            case MEDIUM_SPEED:
                durasi = Duration.millis(300);
                break;
        }
        animation.getKeyFrames().setAll(
                new KeyFrame(durasi,
                        event));
        return durasi;
    }

    public Group getRoot(){return root;}
    
    public void pause() {
        animation.pause();
//    	runningThread  = false;
    }

    public void play() {
    	runningState = true; 
//      save.setMainFolder(fieldSnapshootName.getText());
//    	save.setMainFolder("hasil akhir");
        animation.play();
//        thread.start();
    }

    private void initProperty() {
        generateParticle();
        if (usingBoundaryParticle){generateBoundary();}
        if (drawPerimeter) {createPerimeter();}
        if (drawConvexHull) {createConvexHull();}
        if(draWConcaveHull){createConcavHull();}
        redrawCircle();
    }

    public void restart() {
    	runningState = false; 
        animation.stop();
        listFluid.clear();
        allParticle.clear();
        listBoundary.clear();
        listCircle.clear();
        listFluid.clear();
        listOilParticle.clear();
        listWaterParticle.clear();
        gc.setFill(Color.WHITE);
        gc.fillRect(0.0,0.0,xwidth,ywidth);
        iterasiCount = 0;
        animation.getKeyFrames().setAll(
                new KeyFrame(changeSpeed(),
                        event));
        initProperty();
        initNeighboor();
        setKeteranganIterasi();
    }

    public void initNeighboor() {
        switch (typeInteraction) {
            case singleGrid:
            case doubleGrid:
            case parallel:
                this.initLinkedList();
                break;
            case tree:
                initTree();
                break;
        }
    }
    
    // when boundary particle not used
    private void checkBoundary() {
        double f = 0.8;
        for (int i = 0; i < listFluid.size(); i++) {
            Particle p = listFluid.get(i);
            if (p.getX() > xMax - MainProperty.g_radius){
                p.setX(xMax - MainProperty.g_radius);
                double vx = -Math.abs(p.getVx()) * f;
                p.setVx(vx);
            }
            if (p.getX() < xMin + MainProperty.g_radius){
                p.setX(xMin + MainProperty.g_radius);
                double vx = Math.abs(p.getVx()) * f;
                p.setVx(vx);
            }
            if (p.getY() > yMax - MainProperty.g_radius){
                p.setY(yMax - MainProperty.g_radius);
                double vy = -Math.abs(p.getVy()) * f;
                p.setVy(vy);
            }
            if (p.getY() < yMin + MainProperty.g_radius){
                p.setY(yMin + MainProperty.g_radius);
                double vy = Math.abs(p.getVy()) * f;
                p.setVy(vy);
            }
        }
    }

    private void generateBoundary() {
        // batas samping kanan
        double y1 = yMax - (step / 2.0);
        double y2 = yMax;

        double x1 = xMin;
        double x2 = xMin + (step / 2.0);

        while (y2 >= 0) {
            SPHParticle p = new SPHParticle(x2,y2,
            		0.0,0.0,0.0,0.0, 0.0,
                    TypeParticle.Boundaries);
            listBoundary.add(p);
            allParticle.add(p);

            y2 -= step;
        }

        while (y1 >= 0) {
            SPHParticle p = new SPHParticle(x1,
                    y1,0.0,0.0,0.0,0.0, 0.0,
                    TypeParticle.Boundaries);
            listBoundary.add(p);
            allParticle.add(p);
            y1 -= step;
        }

        // batas bawah
        y2 = yMin + (step / 2.0);
        x2 = xMin + (step / 2.0);

        y1 = yMin;
        x1 = xMin + step;

        while (x2 <= xMax) {
            SPHParticle p = new SPHParticle(x2,
                    y2,0.0,0.0,0.0,0.0,0.0,
                    TypeParticle.Boundaries);
            listBoundary.add(p);
            allParticle.add(p);
            x2 += step;
        }

        while (x1 <= (xMax)) {
            SPHParticle p = new SPHParticle(x1,
                    y1,0.0,0.0,0.0,0.0,0.0,
                    TypeParticle.Boundaries);
            listBoundary.add(p);
            allParticle.add(p);
            x1 += step;
        }

        // batas sebelah kanan
        x2 = xMax - step;
        y2 = yMin;

        x1 = xMax - step / 2.0;
        y1 = yMin + step / 2.0;

        while (y2 <= yMax) {
            SPHParticle p = new SPHParticle(x2,
                    y2,0.0,0.0,0.0,0.0,0.0,
                    TypeParticle.Boundaries);
            listBoundary.add(p);
            allParticle.add(p);
            y2 += step;
        }

        while (y1 <= yMax) {
            SPHParticle p = new SPHParticle(x1,y1,0.0,
                    0.0,0.0,0.0,0.0,
                    TypeParticle.Boundaries);
            listBoundary.add(p);
            allParticle.add(p);
            y1 += step;
        }
    }

    private void variableTimeStep() {
        dtFrame = 0.005;
        dtFrame = dtFrame * MainProperty.g_timeMult;
        timeStep = dtFrame / g_SubStep;
    }

    private void defaultStep() {
        variableTimeStep();
        for (int i = 0; i < listFluid.size(); i++) {
            SPHParticle p = listFluid.get(i);
            p.backUpVelocity(timeStep);
        }
        // gravity correction
        for (int i = 0; i < listFluid.size(); i++) {
            SPHParticle p = listFluid.get(i);
            
            p.gravityCorrection(timeStep);
            p.initPressure();
        }
        moveParticle();
        if (!usingBoundaryParticle) {
            checkBoundary();
        }
        maxRange = smoothingLength * 2;

        switch (typeInteraction) {
            case tree:
                initTree();
                break;
            case singleGrid:
            case doubleGrid:
            case parallel:
                initLinkedList();
                break;
        }

		InteractionType<SPHParticle> firstInteraction =
				new InteractionType<SPHParticle>() {
			@Override
			public void calculate(SPHParticle p1,
					SPHParticle p2) {
				double dx = p1.getX() - p2.getX();
				double dy = p1.getY() - p2.getY();
				if (isCLoser(dx, dy)) {
					double distance = Math.sqrt(dx * dx 
							+ dy * dy);
					if (distance == 0) {
						distance = 1.0;
					}
					// perhitungan berdasarkan kernel
					double q = 1.0 - distance / maxRange;
					if (q < 0) {
						q = 0;
					}
					p1.setKernel(q);
					p2.setKernel(q);

					TypeInteraction t = typeInteraction;
					switch (t) {
					case singleGrid:
						p1.updateDensitas();
						p2.updateDensitas();
						// biar setara
						p1.updateDensitas();
						p2.updateDensitas();
						break;
					case doubleGrid:
					case parallel:
					case tree:
						p1.updateDensitas();
						p2.updateDensitas();
						break;
					}
				}
			}
		};

        switch (typeInteraction) {
            case singleGrid:
                singleInteraction(firstInteraction);
                break;
            case doubleGrid:
                doubleInteraction(firstInteraction);
                break;
            case tree:
                treeInteractions(firstInteraction);
                break;
            case parallel:
                paralellInteraction(firstInteraction);
                break;
        }

        // perhitungan tekanan
        double g_avgDensity = 0.0;
        for (int i = 0; i < allParticle.size(); i++) {
            SPHParticle p = allParticle.get(i);
            if (p.getSPHdens() > 0) {
                g_avgDensity = g_avgDensity + 
                		p.getSPHdens()
                        / allParticle.size();
            }
            p.updatePressure();
            if (p.type.equals(TypeParticle.Boundaries)) {
                p.multiplyPressure(0.8,
                        0.5);
            }
        }

		// perhitungan viskositas
		InteractionType<SPHParticle> secondInteraction =
				new InteractionType<SPHParticle>() {
			@Override
			public void calculate(SPHParticle p1,
					SPHParticle p2) {

				double dx = p1.getX() - p2.getX();
				double dy = p1.getY() - p2.getY();

				if (isCLoser(dx, dy)) {
					double pressure = p1.getPressure() + 
							p2.getPressure();
					double pressN = p1.getPressureNear() 
							+ p2.getPressureNear();

					double distance = 
							Math.sqrt(dx * dx + dy * dy);
					if (distance == 0) {
						distance = 1.0;
					}
					// perhitungan berdasarkan kernel
					double q = 1.0 - distance / maxRange;
					if (q < 0) {
						q = 0;
					}

					double qpangkatdua = q * q;
					double qpangkattiga = q * q * q;

					p1.setKernel(q);
					p2.setKernel(q);

					double fDisp = (pressure * 
							qpangkatdua + pressN
							* qpangkattiga)
							* timeStep * timeStep;

					double a2bNx = (p2.getX() -
							p1.getX()) / distance;
					double a2bNy = (p2.getY() - 
							p1.getY()) / distance;

					double vdispX = a2bNx * (-fDisp);
					double vdispY = a2bNy * (-fDisp);
					
					double dispX = vdispX
//							/ 
//							(p1.getInversMassa() +
//									p2.getInversMassa())	
									;
					double dispY = vdispY
//							/ 
//							(p1.getInversMassa() +
//									p2.getInversMassa())
									;

					// jangan koreksi boundary partikel
					if (p1.type == TypeParticle.Water){
						p1.increasePosition(dispX, 
								dispY);
					}
					if (p2.type == TypeParticle.Water) {
						p2.decreasePosition(dispX,
								dispY);
					}
					if (p1.type == TypeParticle.Second) {
						p1.increasePosition(dispX,
								dispY);
					}
					if (p2.type == TypeParticle.Second) {
						p2.decreasePosition(dispX,
								dispY);
					}

					// koreksi double counting
					if (typeInteraction == 
							TypeInteraction.singleGrid) {
						dispX = -dispX;
						dispY = -dispY;
						if (p1.type == 
								TypeParticle.Water){
							p1.decreasePosition(dispX,
									dispY);
						}
						if (p2.type ==
								TypeParticle.Water) {
							p2.increasePosition(dispX,
									dispY);
						}
						if (p1.type ==
								TypeParticle.Second) {
							p1.decreasePosition(dispX, 
									dispY);
						}
						if (p2.type == 
								TypeParticle.Second) {
							p2.increasePosition(dispX,
									dispY);
						}
					}
				}
			}
		};

        switch (typeInteraction) {
            case singleGrid:
                singleInteraction(secondInteraction);
                break;
            case doubleGrid:
                doubleInteraction(secondInteraction);
                break;
            case tree:
                treeInteractions(secondInteraction);
                break;
            case parallel:
                paralellInteraction(secondInteraction);
        }
    }

    public void changeListToArray() {
        particleSPH = new 
        		SPhysicsParticle[listSPHysicsParticle
                .size()];
        for (int i = 0; i < listSPHysicsParticle.size();
                i++) {
            particleSPH[i] = listSPHysicsParticle.
                    get(i);
        }
    }

    public boolean isCLoser(double dx,
            double dy) {
        if (dx > maxRange || dx < -maxRange) {
            return false;
        }
        if (dy > maxRange || dy < -maxRange) {
            return false;
        }
        double d2 = dx * dx + dy * dy;
        double m2 = maxRange * maxRange;
        return d2 < m2;
    }

    public double g_velDamping = 0.2;

    private void moveParticle() {
        for (int i = 0; i < listFluid.size(); i++) {
            SPHParticle p = listFluid.get(i);
            double dmp = 0.0;
            if (g_velDamping > 0) {
                dmp = g_velDamping * timeStep;
                if (dmp > 1) {
                    dmp = 0.18;
                }
                p.applyDampingToVelocity(dmp);
            }
            p.absmin();
            p.updatePosition(timeStep);
        }
    }

    // digunakan untuk menambah efek partikel yang 
    // lagi jatuh 
    public void addFallingParticle(){
    	double x = yMaxLinkedList; 
    	
    }
    
    private void redrawCircle() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0, canvasWidth, canvasHeight);
        gc.setStroke(Color.MAGENTA); 
        gc.strokeRect(0, 0, canvasWidth, canvasHeight);
        gc.setStroke(Color.MEDIUMSPRINGGREEN);
        for (int i = 0; i < allParticle.size();
                i++) {
            SPHParticle p = allParticle.get(i);
            double scala = MainProperty.g_radius
                    * drawingFactorX;
            double x = p.getX() * drawingFactorX;
            double y = p.getY() * drawingFactorY;
            y = canvasHeight  - y; // inverting axis
//            y = y - 200; 
            Color color = null;
            switch (p.type) {
                case Water:
                    color = Color.BLUE;
                    break;
                case Boundaries:
                    color = Color.RED;
                    break;
                case Second:
                	color = Color.MEDIUMSPRINGGREEN;
                    break;
            }
            gc.setFill(color);
            gc.fillOval(x,y,scala,scala);
        }

        if (drawPerimeter) {
            if (perimeterState.
            		equals(perimeterWater)) {
            			drawPerimeter(gc,Color.GREEN,
                        xPerimeterWater,
                        yPerimeterWater);
            } else if (perimeterState.
            		equals(perimeterOil)){
            			drawPerimeter(gc,
                        Color.MEDIUMORCHID,
                        xPerimeterOil,
                        yPerimeterOil);
            } else if (perimeterState.
                    equals(perimeterBoth)) {
            			drawPerimeter(gc,
                        Color.GREEN,
                        xPerimeterWater,
                        yPerimeterWater);
            			drawPerimeter(gc,
                        Color.MEDIUMORCHID,
                        xPerimeterOil,
                        yPerimeterOil);
            } else if (perimeterStates.
                    equals(perimeterUnion)) {
            }
        }

        if (drawConvexHull) {
            if (convexHullState.
                    equals(convexHullOil)) {
                drawConvexHull(gc,
                        Color.MAROON,
                        oilConvexHull);
            } else if (convexHullState.
                    equals(convexHullWater)) {
                drawConvexHull(gc,
                        Color.MAGENTA,
                        waterConvexHUll);
            } else if (convexHullState
                    .equals(convexHullBoth)) {
                drawConvexHull(gc,
                        Color.MAROON,
                        oilConvexHull);
                drawConvexHull(gc,
                        Color.MAGENTA,
                        waterConvexHUll);
            }
        }
        
        if(draWConcaveHull){
        	drawConcavHull(gc, Color.GREEN);
        }
    }

    public void drawPerimeter(GraphicsContext gc,
            Color color,
            ArrayList<Double> listX,
            ArrayList<Double> listY) {
        gc.setStroke(color);
        gc.beginPath();
        gc.setLineWidth(1);
        double x = listX.get(0) * drawingFactorX;
        double y = listY.get(0) * drawingFactorY;
        y = canvasHeight - y;
        gc.moveTo(x,  y);
        gc.stroke();
        for (int i = 0; i < listX.size(); i++) {
            x = listX.get(i) * drawingFactorX;
            y = listY.get(i) * drawingFactorY;
            y = canvasHeight - y;
            gc.lineTo(x,
                    y);
            gc.stroke();
        }
    }

    public <T extends Point> void
            drawConvexHull(GraphicsContext gc,
                    Color color,
                    ArrayList<T> list) {
        gc.setStroke(color);
        gc.beginPath();
        gc.setLineWidth(1);
        double x = list.get(0).getX() * drawingFactorX;
        double y = list.get(0).getY() * drawingFactorY;
        y = canvasHeight  - y;
        gc.moveTo(x,
                y);
        gc.stroke();
        for (int i = 0; i < list.size(); i++) {
            x = list.get(i).getX() * drawingFactorX;
            y = list.get(i).getY() * drawingFactorY;
            y = canvasHeight - y;
            gc.lineTo(x,
                    y);
            gc.stroke();
        }
        x = list.get(0).getX() * drawingFactorX;
        y = list.get(0).getY() * drawingFactorY;
        y = canvasHeight - y;
        gc.lineTo(x,
                y);
        gc.stroke();
    }

    public void removeOverlap
    (ArrayList<SPHParticle> list) {
        for (int i = 0; i < list.size(); i++) {
            SPHParticle p1 = list.get(i);
            for (int j = list.size() - 1; j > i; j--) {
                SPHParticle p2 = list.get(j);
                if (isOverlap(p1,
                        p2)) {
                    list.remove(j);
                }
            }
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            SPHParticle p1 = list.get(i);
            if (p1.getX() > xMax - MainProperty.g_radius
                    || p1.getX()
                    < xMin + MainProperty.g_radius
                    || p1.getY() > yMax - 
                    MainProperty.g_radius
                    || p1.getY()
                    < yMin + MainProperty.g_radius) {
                list.remove(i);
            }
        }
    }

    private boolean isOverlap(SPHParticle p1,
            SPHParticle p2) {
        double deltax = p1.getX() - p2.getX();
        double deltay = p1.getY() - p2.getY();
        if (deltax * deltax + deltay * deltay
                < 4 * MainProperty.g_radius
                * MainProperty.g_radius) {
            return true;
        }
        return false;
    }

    boolean upper = true;

    private void generateParticle() {
        double x1 = xDomainOfFluida;
        double y1 = yDomainOfFluida;
        double xcenter = (xDomainOfFluida - xMin) * 0.5;
        double ycenter = (yDomainOfFluida - yMin) * 0.5;
        double radius_ = 1.3 * smoothingLength;

        Circle_ circle = new Circle_(xcenter,
                ycenter,
                radius_);
        if (!usingStaggeredGrid) {
            while (y1 >= yMin) {
                while (x1 >= xMin) {
                    if (isInCircle(new Point_(x1,
                            y1),
                            circle)) {
                        SPHParticle p = new 
                        		SPHParticle(x1,
                                y1,0D,0D,
                                TypeFluid.WATER,
                                TypeParticle.Second);
                        listFluid.add(p);
                        allParticle.add(p);
                        listOilParticle.add(p);
                    } else {
                        SPHParticle p = 
                        		new SPHParticle(x1,
                                y1,0D, 0D,
                                fluidaKedua,
                                TypeParticle.Water);
                        listFluid.add(p);
                        allParticle.add(p);
                    }
                    x1 -= step;
                }
                x1 = xDomainOfFluida;
                y1 -= step;
            }
        } else {  //using staggered grid
            while (x1 > xMin) {
                while (y1 > yMin) {
                    if (!isInCircle(new Point_(x1,
                            y1),
                            circle)) {
                        SPHParticle p =
                        		new SPHParticle(x1,
                                y1,0D,0D,
                                TypeFluid.WATER,
                                TypeParticle.Water);
                        listFluid.add(p);
                        allParticle.add(p);
                        listWaterParticle.add(p);
                    } else {
                        SPHParticle p = new 
                        		SPHParticle(x1,
                                y1,0D,0D,
                                fluidaKedua,
                                TypeParticle.Second);
                        listFluid.add(p);
                        allParticle.add(p);
                        listOilParticle.add(p);
                    }
                    y1 -= step;
                }
                if (upper) {
                    y1 = yDomainOfFluida + step / 2.0;
                    upper = false;
                } else {
                    y1 = yDomainOfFluida;
                    upper = true;
                }
                x1 -= step / 2.0;
            }
        }
    }

    private boolean isInCircle(Point_ p,
            Circle_ c) {
        double deltax = p.getX() - c.getX();
        double deltay = p.getY() - c.getY();
        if (deltax * deltax + deltay * deltay 
        		<= c.getRadius()) {
            return true;
        }
        return false;
    }

    private class Point_ {
        double x, y;
        public Point_(double x,double y) {
        	this.x = x;
            this.y = y;}

        public double getX() {return x;}
        public double getY() {return y;}
    }

    private class Circle_ {

        double x;
        double y;
        double radius;

        public Circle_(double x,
                double y,
                double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getRadius() {
            return radius;
        }
    }

    public void paralellInteraction(
            InteractionType<SPHParticle> interaction) {
        paralelProcess = new ParallelInteraction(this,
                interaction,
                0,
                allParticle.size());
        pool.invoke(paralelProcess);
    }

    DoubleInteraction doubleInteraksi;

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public ArrayList<Integer>[][] getHash() {
        return hash;
    }

    public <T extends Particle> ArrayList<T>
            getAllParticle() {
        if (marchingtype == MarchingType.SPHysics) {
            return (ArrayList<T>) allsphysicsParticle;
        } else {
            return (ArrayList<T>) allParticle;
        }
    }

    public void treeInteractions(
            InteractionType<SPHParticle> interaction) {
        for (int i = 0; i < allParticle.size(); i++) {
            SPHParticle p1 = allParticle.get(i);
            treeInteraction(p1,
                    noderoot,
                    interaction);
        }
    }

    private void treeInteraction(SPHParticle particle,
            Node current_node,
            InteractionType<SPHParticle> interaction) {
        if (current_node.getNumberParticle() > 1) {
            if (current_node.getBottomLeftNode()
                    != null) {
                Node node = current_node.
                        getBottomLeftNode();
                if (isNodeAndParticleOverlap(node,
                        particle)) {
                    treeInteraction(particle,
                            node,
                            interaction);
                }
            }
            if (current_node.getBottomRightNode()
                    != null) {
                Node node = current_node.
                        getBottomRightNode();
                if (isNodeAndParticleOverlap(node,
                        particle)) {
                    treeInteraction(particle,
                            node,
                            interaction);
                }
            }
            if (current_node.getTopLeftNode() != null) {
                Node node = current_node.getTopLeftNode();
                if (isNodeAndParticleOverlap(node,
                        particle)) {
                    treeInteraction(particle,
                            node,
                            interaction);
                }
            }
            if (current_node.getTopRightNode() != null) {
                Node node = current_node.
                        getTopRightNode();
                if (isNodeAndParticleOverlap(node,
                        particle)) {
                    treeInteraction(particle,
                            node,
                            interaction);
                }
            }
        } else if (current_node.
                getNumberParticle() == 1) {
            if (current_node.getParticle() != particle) {
                interaction.calculate(current_node.
                        getParticle(),
                        particle);
            }
        }
    }

    public boolean isNodeAndParticleOverlap(Node node,
            SPHParticle particle) {
        if (particle.getLeftSearchBox() < node.
        		getRightMargin()
                && particle.getRightSearchBox() > node.
                getLeftMargin()
                && particle.getBottomSearchBox()
                < node.getTopMargin()
                && particle.getTopSearchBox()
                > node.getBottomMargin()) {
            return true;
        }
        return false;
    }

    Node noderoot;

    public void initTree() {
        System.out.println("Lagi init tree... !!!");
        noderoot = new Node(0.0,
                yMax,
                xMax,
                0.0);
        for (int i = 0; i < allParticle.size(); i++) {
            SPHParticle p = allParticle.get(i);
            noderoot.insertParticle(p);
            p.setSearchBoxLength(smoothingLength * 2.5);
        }
    }

    public void singleInteraction(
            InteractionType<SPHParticle> interaction) {
        for (int ny = 0; ny < gridY; ny++) {
            for (int nx = 0; nx < gridX; nx++) {
                if (hash[nx][ny].size() > 0) {
                    for (int a = 0; a < hash[nx][ny].
                    		size(); a++) {
                        int aa = hash[nx][ny].
                        		get(a).intValue();
                        // calculate influence
                        // on the same cell
                        for (int b = 0; b < a; b++) {
                            int bb = hash[nx][ny].get(b).
                                    intValue();
                            // int p2 = bb;
                            interaction.
                            calculate(allParticle.get(aa),
                                    allParticle.get(bb));
                        }
                        // calculate influence
                        // on east cell
                        if (nx + 1 < gridX && 
                        		hash[nx + 1][ny].
                                size()
                                > 0) {
                            for (int b = 0; b < 
                        		hash[nx + 1][ny].size();
                                    b++) {
                                int bb = hash[nx + 1]
                                		[ny].get(b)
                                        .intValue();
                                interaction.
                                calculate(allParticle.
                                        get(aa),
                                    allParticle.get(bb));
                            }
                        }
                        // calculate influence on
                        // north-east cell
                        if (nx + 1 < gridX && ny + 
                        		1 < gridY
                                && hash[nx + 1][ny + 1]
                                		.size() > 0) {
                            for (int b = 0; b < 
                            		hash[nx + 1][ny+ 1].
                                    size();b++) {
                                int bb = hash[nx + 1]
                                		[ny + 1].get(b)
                                        .intValue();
                                interaction.
                                calculate(allParticle.
                                        get(aa),
                                        allParticle.
                                        get(bb));
                            }
                        }
                        // calculate influence 
                        // on north cell
                        if (ny + 1 < gridY && 
                        		hash[nx][ny + 1].
                                size() > 0) {
                            for (int b = 0; b < 
                            		hash[nx][ny + 1].
                                    size();
                                    b++) {
                                int bb = hash[nx]
                                		[ny + 1].get(b).
                                        intValue();
                                interaction.calculate
                                (allParticle.
                                        get(aa),
                                        allParticle.
                                        get(bb));
                            }
                        }
                        // calculate influence on 
                        // north-west cell
                        if (nx - 1 >= 0 && ny + 1 
                        		< gridY
                                && hash[nx - 1][ny + 1]
                                		.size() > 0) {
                            for (int b = 0; b < 
                            		hash[nx - 1][ny
                                    + 1].
                                    size();
                                    b++) {
                                int bb = hash[nx - 1]
                                		[ny + 1].get(
                                        b)
                                        .intValue();
                                interaction.calculate(
                                		allParticle.
                                        get(aa),
                                        allParticle.
                                        get(bb));
                            }
                        }
                    }
                }
            }
        }
    }

    final void initLinkedList() {

        initExtremeValue();
        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                hash[i][j].clear();
            }
        }
        for (int i = 0; i < allParticle.size(); i++) {
            int icell = checkX(allParticle.get(i).getX());
            int kcell = checkY(allParticle.get(i).getY());
            hash[icell][kcell].add(new Integer(i));
        }
    }

    /*
     * digunakan untuk menyesuaikan Linked list dalam
     *  arah sumbu y
     */
    private void initExtremeValue() {
        for (int i = 0; i < listFluid.size(); i++) {
            SPHParticle p = listFluid.get(i);
            if (p.getY() > yMaxLinkedList) {
                yMaxLinkedList = p.getY();
            }
            if (p.getY() < xMinLinkedList) {
                yMinLinkedList = p.getY();
            }
        }
    }

    public int checkX(double x) {
        if (x >= xMaxLinkedList - 0.001) {
            x = xMaxLinkedList - 0.001;
        }
        double dx = (x - xMinLinkedList) / 
        		(xMaxLinkedList - xMinLinkedList);
        double icelld = 0 + dx * (gridX - 0.001);
        int icell = (int) icelld;
        return icell;
    }

    public int checkY(double y) {
        if (y >= yMaxLinkedList - 0.001) {
            y = yMaxLinkedList - 0.001;
        }
        double dy = (y - yMinLinkedList) / 
        		(yMaxLinkedList - yMinLinkedList);
        double kcelld = 0 + dy * (gridY - 0.001);
        int kcell = (int) kcelld;
        return kcell;
    }

    private void variabelController() {
		sphStiffSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? 
							extends Number> arg0,
							Number arg1, Number arg2) {
						MainProperty.sphStiff = 
								sphStiffSlider.getValue();
						keteranganStiff.setText(format
								.format(MainProperty.
										sphStiff));
					}
				});

		sphStiffNearSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<?
							extends Number> arg0,
							Number valueLama,
							Number valueBaru) {

						MainProperty.sphStiffNear =
								sphStiffNearSlider
								.getValue();
						keteranganStiffNear.setText(format
								.format(MainProperty.
										sphStiffNear));
					}
				});

		sphRestDenstSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? 
							extends Number> arg0,
							Number arg1, Number arg2) {
						MainProperty.sphRestDens = 
								sphRestDenstSlider
								.getValue();
						keteranganRestDenst.setText(format
					.format(MainProperty.sphRestDens));
					}
				});

		timeMultSlider.valueProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<?
							extends Number> arg0,
							Number arg1, Number arg2) {
						MainProperty.g_timeMult = 
								timeMultSlider.getValue();
						keteranganTimeMult.setText(format
						.format(MainProperty.g_timeMult));
					}
				});
		
		comboBoxFluidaKedua.valueProperty().
		addListener(new ChangeListener<TypeFluid>() {
			@Override
			public void changed(ObservableValue<?
					extends TypeFluid> arg0,
					TypeFluid arg1, TypeFluid now) {
				if(!runningState){
					fluidaKedua = now;
					restart();
				}
			}
		});

	}

	private void animasiController() {

		tipeInteraksi.selectedToggleProperty().
		addListener(
				new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? 
							extends Toggle> arg0,
							Toggle arg1, Toggle toggle) {
						RadioButton radioButton = 
								(RadioButton) toggle;
						String text = radioButton.
								getText();
						if (text.equals("parallel")) {
							typeInteraction = 
									TypeInteraction.
									parallel;
						} else if (text.equals("tree")) {
							typeInteraction = 
									TypeInteraction.tree;
						} else if (text.equals("single")) 
						{
							typeInteraction = 
									TypeInteraction.
									singleGrid;
						}
					}
				});

		toggleGroupTotalAnimation.selectedToggleProperty()
		.addListener(
				new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? 
							extends Toggle> arg0,
							Toggle lama, Toggle baru) {
						RadioButton radioButton = 
								(RadioButton) baru;
						int cycle = (Integer) radioButton
								.getUserData();
						animation.setCycleCount(cycle);
					}
				});

		toggleGroupTypeAnimation.selectedToggleProperty()
		.addListener(
				new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<?
							extends Toggle> arg0,
							Toggle arg1, Toggle toggle) {
						RadioButton toggleButton = 
								(RadioButton) toggle;
						Animation_Type type =
								(Animation_Type)
								toggleButton
								.getUserData();
						type_animasi = type;
						changeSpeed();
					}
				});

		checkBoxConvexHull.setOnAction(new
				EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				drawConvexHull = checkBoxConvexHull.
						isSelected();
			}
		});

		checkBoxPerimeter.setOnAction(new 
				EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				drawPerimeter = checkBoxPerimeter
						.isSelected();
			}
		});

		checkBoxConcavHull.setOnAction(new 
				EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				draWConcaveHull = checkBoxConcavHull.
						isSelected(); 
			}
		});
		
		comboBoxConvexHull.valueProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? 
							extends String> arg0,
							String arg1, String value) {
						convexHullState = value;
					}
				});

		comboBoxPerimeter.valueProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<?
							extends String> arg0,
							String arg1, String value) {
						perimeterState = value;
					}
				});
		
		sliderNumberIterasi.valueProperty().
		addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? 
					extends Number> arg0,
					Number arg1, Number nilai) {
				int a = (int)sliderNumberIterasi.getValue();
				textNumberIterasi.setText(format.
						format(a));
				animation.setCycleCount(a);
			}
		});
    }

    private void setControl() {
        variabelController();
        animasiController();
    }

    private void addVariabelController() {

        textStiffNear = new Text() {{
                setText("SPH Stiff Near");
        }};

        sphStiffNearSlider = new Slider(){{
                setPrefHeight(20);
                setPrefWidth(90);
                setMin(0.0);
                setMax(200);
                setValue(MainProperty.sphStiffNear);
                setOrientation(Orientation.HORIZONTAL);
                setBlockIncrement(.1);
        }};

        keteranganStiffNear = new Text() {{
                setText(Double.toString(
                		MainProperty.sphStiffNear));
        }};

        textStiff = new Text() {{
                setText("SPH StiffNess");
        }};

        sphStiffSlider = new Slider() {{
                setPrefHeight(20);
                setPrefWidth(90);
                setMin(0.0);
                setMax(500);
                setValue(MainProperty.sphStiff);
                setOrientation(Orientation.HORIZONTAL);
        }};

        keteranganStiff = new Text() {{
                setText(Double.toString(
                		MainProperty.sphStiff));
        }};

        textRestDenst = new Text() {{
                setText("SPH Rest Densitas");
        }};

        sphRestDenstSlider = new Slider(){{
                setPrefHeight(20);
                setPrefWidth(90);
                setMin(0.0);
                setMax(1.0);
                setOrientation(Orientation.HORIZONTAL);
                setValue(MainProperty.sphRestDens);
        }};

        keteranganRestDenst = new Text() {{
                setText(Double.toString(
                		MainProperty.sphRestDens));
            }};

        textTimeMult = new Text() {{
            setText("SPH Time Step");
        }};

        timeMultSlider = new Slider() {{
                setPrefHeight(20);
                setPrefWidth(90);
                setMin(0.0);
                setMax(0.1);
                setValue(MainProperty.g_timeMult);
                setOrientation(Orientation.HORIZONTAL);
        }};

        keteranganTimeMult = new Text() { {
                setText(Double.toString(
                		MainProperty.g_timeMult));
        }};

		GridPane gridPane = new GridPane();
		gridPane.setVgap(2);
		gridPane.setHgap(5);
		gridPane.add(textRestDenst, 0, 0);
		gridPane.add(sphRestDenstSlider, 1, 0);
		gridPane.add(keteranganRestDenst, 2, 0);
		gridPane.add(textTimeMult, 0, 1);
		gridPane.add(timeMultSlider, 1, 1);
		gridPane.add(keteranganTimeMult, 2, 1);
		gridPane.add(textStiff, 0, 2);
		gridPane.add(sphStiffSlider, 1, 2);
		gridPane.add(keteranganStiff, 2, 2);
		gridPane.add(textStiffNear, 0, 3);
		gridPane.add(sphStiffNearSlider, 1, 3);
		gridPane.add(keteranganStiffNear, 2, 3);

        HBox box = new HBox(){{
                setSpacing(10);
                getChildren().addAll(
                new Label("Tipe Fluida Kedua"),
                comboBoxFluidaKedua = 
                new ComboBox<TypeFluid>(){{
                    getItems().addAll(
                    		TypeFluid.SAME_REST_DENS_1,
                    		TypeFluid.SAME_REST_DENS_2, 
                    		TypeFluid.SAME_REST_DENS_3, 
                    		TypeFluid.SAME_STIFF_1, 
                    		TypeFluid.SAME_STIFF_2, 
                    		TypeFluid.SAME_STIFF_3, 
                    		TypeFluid.SAME_STIFFNEAR_1, 
                    		TypeFluid.SAME_STIFFNEAR_2, 
                    		TypeFluid.SAME_STIFFNEAR_3,
                    		TypeFluid.OLI,
                    		TypeFluid.GLICERIN, 
                    		TypeFluid.MADU);
                    setValue(TypeFluid.OLI);
                }}
            );
        }};

        additionalController.getChildren().add(box);
        additionalController.getChildren().add(gridPane);
    }

    private void addAnimasiController() {

		FlowPane flowPane = new FlowPane();
		flowPane.setOrientation(Orientation.VERTICAL);
		flowPane.setVgap(10);

		tipeInteraksi = new ToggleGroup();
		
		radioButton1 = new RadioButton("single"){{
				setToggleGroup(tipeInteraksi);
		}};

		radioButton2 = new RadioButton("parallel"){{
				setToggleGroup(tipeInteraksi);
		}};

		radioButton3 = new RadioButton("tree") {{
				setToggleGroup(tipeInteraksi);
			}};

		switch (typeInteraction) {
		case singleGrid:
			radioButton1.setSelected(true);
			break;
		case parallel:
			radioButton2.setSelected(true);
			break;
		case tree:
			radioButton3.setSelected(true);
			break;
		default:
			break;
		}

		HBox boxTypeInteraction = new HBox(){{
				setSpacing(5);
				getChildren().
				addAll(radioButton1,radioButton2, 
						radioButton3);

		}};

		toggleGroupTotalAnimation = new ToggleGroup();

		HBox boxNumberTimeStep = new HBox() {{
				setSpacing(5);
				getChildren().addAll(animasi_50 = 
						new RadioButton("50") {{
						setToggleGroup
						(toggleGroupTotalAnimation);
						setUserData(50);
					}},
					animasi_100 = new 
					RadioButton("100"){{
						setToggleGroup
						(toggleGroupTotalAnimation);
						setUserData(100);
					}}, 
					animasi_inf = new 
					RadioButton("INF") {{
						setToggleGroup
						(toggleGroupTotalAnimation);
						setUserData(Animation.INDEFINITE);
					}}, 
					sliderNumberIterasi = new 
					Slider(50, 1000,50){{
						setMaxWidth(70);
						setSnapToTicks(true); 
						setBlockIncrement(20);
						setMajorTickUnit(10);
						setMinorTickCount(10);
					}},
					textNumberIterasi = new Text("10")
				);
			}};

		switch (cycleCount) {
		case 10:
			animasi_50.setSelected(true);
			break;
		case 100:
			animasi_100.setSelected(true);
			break;
		case Animation.INDEFINITE:
			animasi_inf.setSelected(true);
			break;
		}

		toggleGroupTypeAnimation = new ToggleGroup();

		HBox boxAnimationSpeed = new HBox() {{
				setSpacing(10);
				getChildren().addAll(fastAnimasi = 
						new RadioButton("FAST") {{
				setToggleGroup(toggleGroupTypeAnimation);
				setUserData(Animation_Type.FAST_ANIMASI);
					}},

				slowAnimasi = new RadioButton("SLOW"){{
						setToggleGroup
						(toggleGroupTypeAnimation);
						setUserData(Animation_Type.
								SLOW_ANIMASI);
					}}, 
					medium_speed_animasi = 
					new RadioButton("MEDIUM") {{
				setToggleGroup(toggleGroupTypeAnimation);
				setUserData(Animation_Type.MEDIUM_SPEED);
					}});
			}};

		switch (type_animasi) {
		case SLOW_ANIMASI:
			slowAnimasi.setSelected(true);
			break;
		case FAST_ANIMASI:
			fastAnimasi.setSelected(true);
			break;
		case MEDIUM_SPEED:
			medium_speed_animasi.setSelected(true);
			break;
		}

		HBox box = new HBox() {{
				setSpacing(10);
				getChildren().addAll(fieldSnapshootName, 
					snapshotButton = new Button(){{
						setText("TAKE A SNAP");
						setPrefWidth(100);
						setOnAction(new 
								EventHandler
								<ActionEvent>() {
							@Override
							public void handle
							(ActionEvent arg0) {
								saveCanvas(
								fieldSnapshootName
								.getText());
							}
						});
					}}
				);
			}};

		additionalController.getChildren().add(box);

		checkBoxPerimeter = new CheckBox() {{
				setText("using perimeter");
				if (drawPerimeter) {
					setSelected(true);
				}
			}};

		comboBoxPerimeter = new ComboBox<String>() {{
				setValue(perimeterBoth);
				getItems().addAll(perimeterWater, 
						perimeterOil, perimeterBoth);
			}};

		checkBoxConvexHull = new CheckBox() {{
				setText("using convex hull");
				if (drawConvexHull) {
					setSelected(true);
				}
			}};

		comboBoxConvexHull = new ComboBox<String>() {{
				setValue(convexHullOil);
				getItems().addAll(convexHullWater,
						convexHullOil,
						convexHullBoth, convexHullUnion);
			}};
			
		checkBoxConcavHull = new CheckBox(){{
			setText("using concav-hull");
			if(draWConcaveHull){
				setSelected(true); 
			}
		}};  
		
		GridPane pane = new GridPane() {{
				setVgap(10);
				setHgap(10);
				add(checkBoxPerimeter, 0, 0);
				add(comboBoxPerimeter, 1, 0);
				add(checkBoxConvexHull, 0, 1);
				add(comboBoxConvexHull, 1, 1);
				add(checkBoxConcavHull, 0, 2);
			}}
		;

        flowPane.getChildren().addAll(boxTypeInteraction,
                boxNumberTimeStep,
                boxAnimationSpeed);

        additionalController.getChildren().addAll(pane,
                flowPane);
    }

    VBox additionalController;
    
    public ArrayList<Integer>[][] hash;

    private AnchorPane getControlPane() {
        anchorpaneController = new AnchorPane();
        additionalController = new VBox();
        additionalController.setTranslateX(textX);
        additionalController.setTranslateY(10);
        additionalController.setSpacing(10);
        addVariabelController();
        addAnimasiController();
        setControl();
        anchorpaneController.getChildren().
                add(additionalController);
        return anchorpaneController;
    }

    public <T extends Particle> void doubleInteraction(
            InteractionType<T> interaction) {
        ArrayList<T> iterationParticle
                = (ArrayList<T>) allParticle;
        for (int ny = 0; ny < gridY; ny++) {
            for (int nx = 0; nx < gridX; nx++) {
                if (getHash()[nx][ny].size() > 0) {
                    for (int a = 0; a < hash[nx][ny].
                            size(); a++) {
                        int aa = hash[nx][ny].get(a).
                                intValue();
                        for (int xc = -1; xc < 2; xc++) {
                            for (int yc = -1; yc < 2;
                                    yc++) {
                                int xx = nx + xc;
                                int yy = ny + yc;
                                if (xx > -1 && xx < gridX
                                        && yy > -1
                                        && yy < gridY
                                        && hash[xx][yy].
                                        size() > 0) {
                                    for (int b = 0; b
                                        < hash[xx][yy].
                                         size(); b++){
                                int bb = hash[xx][yy].
                                		get(b).intValue();
                                if (bb != aa) {
                                    interaction.
                                           calculate(
                                    iterationParticle.
                                    get(aa),
                                    iterationParticle.
                                    get(bb));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
