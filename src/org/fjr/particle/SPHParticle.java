package org.fjr.particle;

import gnu.trove.list.TLinkable;

import java.util.ArrayList;

import org.fjr.main.MainProperty;

public class SPHParticle extends Particle 
	implements TLinkable<SPHParticle>{
	
	// untuk test gnu trove 
	public SPHParticle(){
		super(0, 0, 0, 0);
	}

    private double oldx = 0.0;
    private double oldy = 0.0;
    private double sphDens = 0.0;
    private double sphDensNear = 0.0;
    private double impulses = 0.0;
    private double pressure = 0.0;
    private double pressureNear = 0.0;
    private double rho = 0.0;
    private double radius = 0.0;
    private double massa = 0.0;
    private double kernel = 0.0;
    private double kernelPangkatDua = 0.0;
    private double kernelPangkatTiga = 0.0;

    @Override
    public void updateVelocity(double dt2) {
    }

    @Override
    public void updatePosition(double timeStep) {
        x += vx * timeStep;
        y += vy * timeStep;
    }
    
    public TypeParticle type;
    public ArrayList<SPHParticle> listNeighbor = 
    		new ArrayList<>();
    double cs = 0D; // speed of sound

    public SPHParticle(double x, double y, double vx,
    		double vy, double radius,
            double density, double massa,
            TypeParticle type) {
        super(x, y, vx, vy);
        this.oldx = x;
        this.oldy = y;
        this.rho = density;
        this.radius = radius;
        this.type = type;
        this.massa = massa;
    }

    double sphStiff = 0D; 
    double sphStiffNear = 0D; 
    double sphRestDens = 0D; 
    
    
    double inversMassa = 0D; 
    
    public SPHParticle(double x, double y ,double vx,
    	   double vy, TypeFluid typeFluid, 
    	   TypeParticle type){
    	   super(x, y, vx, vy);
    	   this.oldx = x; 
    	   this.oldy = y; 
    	   this.sphStiff = typeFluid.getSPHStiff(); 
    	   this.sphRestDens = typeFluid.
    			   getRestDensity();
    	   this.sphStiffNear = typeFluid.
    			   getSPHStiffNear(); 
    	   this.type = type; 
    	   this. massa = typeFluid.getDensitas() 
    			   * 1.2 ; 
    	   this.inversMassa = massa > 0? 1./ inversMassa
    			   : 0.; 
    }
    
    public double getInversMassa(){
    	return inversMassa; 
    }
    

    public SPHParticle(double x, double y, double vx,
    		double vy, double radius,
            double density, double massa, double pressure, 
            TypeParticle type) {
        super(x, y, vx, vy);
        this.oldx = x;
        this.oldy = y;
        this.rho = density;
        this.radius = radius;
        this.type = type;
        this.massa = massa;
        this.pressure = pressure;
        this.rho = density;
    }

    @Override
    public void setVelocityChange(double dvx,
    		double dvy) {
    }

    public double getMass() {
        return massa;
    }
    
    @Override
    public void moveParticle() {
    }

    public void applyDampingToVelocity(double damping) {
        vx -= (vx * damping);
        vy -= (vy * damping);
    }

    public void backUpVelocity(double dt) {
        vx = (x - oldx) / dt;
        vy = (y - oldy) / dt;
        oldx = x;
        oldy = y;
    }
    
    public static double max_vel = 5000; 
    
    public void absmin(){
    	if(vx < 0.){
    		vx = -1.0 * Math.min(Math.abs(vx), max_vel); 
    	}else{
    		vx = 1. * Math.min(Math.abs(vx), max_vel);
    	}
    	if(vy < 0.){
    		vy = -1.0 * Math.min(Math.abs(vy), max_vel); 
    	}else{
    		vy = 1. * Math.min(Math.abs(vy), max_vel);
    	}
    }

    public void gravityCorrection(double timeStep) {
        vy += MainProperty.gravity  * timeStep
        		*MainProperty.massa_air;
    }
    
    // perbaiki kembali yang dimaksud di sini density
    public void initPressure() {
        sphDens = 0.0;
        sphDensNear = 0.0;
        impulses = 0.0;
    }

    public double getRadius() {
        return radius;
    }

    public void setKernel(double kernel) {
        this.kernel = kernel;
        kernelPangkatDua = kernel * kernel;
        kernelPangkatTiga = kernel * kernel * kernel;
    }

    public double getSPHdens() {
        return sphDens;
    }

    public double getDensity() {
        return rho;
    }

    
    public void updateDensitas() {
        sphDens = sphDens + kernelPangkatDua;
        sphDensNear = sphDensNear + kernelPangkatTiga;
    }

    public void updatePressure() {
        pressure = sphStiff
                * (sphDens - sphRestDens);
        pressureNear = sphStiffNear *
        		sphDensNear;
    }

    public void multiplyPressure(double vPressure,
    		double vPressNear) {
        pressure *= vPressure;
        pressureNear *= vPressNear;
    }

    public void increasePosition(double dx, double dy) {
        x += (dx 
//        		* inversMassa 
        		);
        y += (dy
//        		* 
//        		inversMassa
        		);
    }

    public void decreasePosition(double dx, double dy) {
        x -=  (dx
//        		* inversMassa 
        		)
        		;
        y -= (dy 
//        		* inversMassa
        		);
    }

    public double getPressure() {
        return pressure;
    }

    public double getPressureNear() {
        return pressureNear;
    }

    @Override
    public void setVelocityChange(double dvx, double dvy, 
    		double dvz) {
    }

    
    SPHParticle currentParticle, prevParticle, 
    nextParticle;
    
    
	@Override
	public SPHParticle getNext() {
		return nextParticle;
	}

	@Override
	public SPHParticle getPrevious() {
		return prevParticle;
	}

	@Override
	public void setNext(SPHParticle particle) {
		nextParticle = particle; 
	}

	@Override
	public void setPrevious(SPHParticle particle) {
		prevParticle = particle; 
	}
}
