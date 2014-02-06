package fjr.savetoimage;

import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

public class SaveCanvasToImage {
	
	String namaRoot = "E:/gambar tesis/"; 
	String nameFolder ; 
	File file = null ;
	Canvas  node; 
	final WritableImage wim; 
	double width , height ; 
	
	File dir; 
	
	public SaveCanvasToImage(Canvas node){
		width = node.getWidth(); 
		height = node.getHeight(); 
		this.node = node ; 
		wim = new WritableImage((int) width, (int) height);
	}
	
	public void setMainFolder(String root){
		this.nameFolder = root ; 
		dir = new File(namaRoot + nameFolder); 
		if(!dir.exists()){
			dir.mkdir(); 
		}
	}
	
	public void save(String name){
		String fileName = namaRoot + nameFolder + "/" + name+ ".png"; 
		File file = new File(fileName); 
		node.snapshot(null, wim);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png",
					file);
		} catch (Exception s) {
		}
	}
	
}
