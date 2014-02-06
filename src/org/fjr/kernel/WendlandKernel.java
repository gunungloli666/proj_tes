package org.fjr.kernel;

import org.fjr.constant.MainConstant;
import org.fjr.particle.Particle;

/**
 *
 * @author fajar
 */
public class WendlandKernel implements  Kernel{
    
    Particle p1, p2; 
    double drx, dry, dr2, dr ;
    
    
    public WendlandKernel(){
    }
    
       
    @Override
    public   double  getKernel(){
        dr = Math.sqrt(dr2);
        
        return  0; 
    }
    
    
    @Override
    public double[] getDerivativeKernel(){
        return null; 
    }
    
  
    
    @Override
    public boolean isCloser(Particle p1  , Particle  p2){
          // Pertama hitung selisih posisi 
        drx = p1.getX() - p2.getX();
        dry = p1.getY() - p2.getY();

        dr2 = drx * drx + dry * dry;
        
        if(dr2 < MainConstant.fourH  && dr2 > 
                MainConstant.minimumTolerance ){
            return true; 
        }
        return false; 
    }
    
}
