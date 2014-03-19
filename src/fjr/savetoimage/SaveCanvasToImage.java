package fjr.savetoimage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Translate;

public class SaveCanvasToImage {
	
	String namaRoot = "E:/gambar tesis/"; 
	String nameFolder  = "hasil hasil"; 
	File file = null ;
	Canvas  node; 
	final WritableImage wim; 
	double width , height ; 
	
	File dir;

	SnapshotParameters parameter;
	Graphics2D im;
	BufferedImage bufferedImage;

	public SaveCanvasToImage(Canvas node){
		width = node.getWidth(); 
		height = node.getHeight(); 
		this.node = node ; 
		parameter = new SnapshotParameters(); 
		parameter.setTransform(new Translate(0, 200));
		wim = new WritableImage(((int) width) , 
				((int) height));
	    bufferedImage = new BufferedImage((int ) width, 
				 (int) height,
					BufferedImage.TYPE_INT_ARGB); 
	}
	
	public void setMainFolder(String root){
		this.nameFolder = root ; 
		dir = new File(namaRoot + nameFolder); 
		if(!dir.exists()){
			dir.mkdir(); 
		}
	}
	
	public void save(String name){
		String fileName = namaRoot + nameFolder 
				+ "/" + name+ ".png"; 
		File file = new File(fileName); 
		BufferedImage image; 
		node.snapshot(parameter, wim);
		try {
			image = SwingFXUtils.
					fromFXImage(wim, bufferedImage);
			Graphics2D gd = (Graphics2D) image.getGraphics();
			gd.translate(0,200 );
			ImageIO.write(image, "png",
					file);
		} catch (Exception s) {
		}
	}
	
}
