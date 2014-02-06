package org.fjr.particle;

import java.awt.Point;

public abstract  class Particle  extends Point{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected double x = 0.0;
	protected double y = 0.0;
	protected double z = 0.0; 
	protected double vx = 0.0;
	protected double vy = 0.0;
	protected double vz = 0.0; 
	
	private  double left = 0.0; 
	private double right = 0.0; 
	private double top = 0.0;
	private double bottom = 0.0; 
	
	double searchBoxTop = 0.0; 
	double searchBoxLeft = 0.0; 
	double searchBoxRight = 0.0; 
	double searchBoxBottom = 0.0; 
	
	public Particle(double x, double y, double vx, double vy){
		this.x = x;
		this.y = y; 
		this.vx = vx;
		this.vy = vy;
		super.setLocation(x, y);
	}
        
	
	public Particle(double x, double y, double z, double vx, double vy,
			double vz) {
		this.x = x;
		this.y = y; 
		this.z = z;
		this.vx = vx; 
		this.vy = vy; 
		this.vz = vz; 
	}
	
	/*
	 * digunakan untuk menentukan lebar kotak pencari
	 */
	public void setSearchBoxLength(double searchMargin){
		this.searchBoxLeft = this.x - searchMargin; 
		this.searchBoxRight  = this.x + searchMargin; 
		this.searchBoxBottom  = this.y - searchMargin; 
		this.searchBoxTop  = this.y + searchMargin; 
	}
	
	public double getLeftSearchBox(){return searchBoxLeft;}
	public double getRightSearchBox(){return searchBoxRight;}
	public double getBottomSearchBox(){return searchBoxBottom;}
	public double getTopSearchBox(){return searchBoxTop;}
	
	public abstract void  setVelocityChange(double dvx , double dvy);
	public abstract void  setVelocityChange(double dvx , double dvy, double dvz);
		
	public abstract void moveParticle(); 
	
	public abstract void updatePosition(double dt); 
	public abstract void updateVelocity(double dt); 
	
	
	public void setPosition(double x, double y){
		this.x = x; 
		this.y = y; 
	}
	
	public void setPosition(double x, double y, double z){
		this.x = x; 
		this.y = y ; 
		this.z = z ;
	}
	
	public void setVelocity(double vx , double vy){
		this.vx = vx; 
		this.vy = vy; 
	}
	
	public void setVelocity(double vx , double vy , double vz){
		this.vx = vx; 
		this.vy = vy; 
		this.vz = vz; 
	}
	
	public void setX(double x){this.x = x;}
	public void setY(double y){this.y = y ;}
	public void setVx(double vx){this.vx = vx;}
	public void setVy(double vy){this.vy = vy;}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public double getZ(){return z;}
	
	public double getVx(){return vx;}
	public double getVy(){return vy;}
	public double getVz(){return vz;}
	
	public double getLeft(){return left; }
	public double getRight(){return right; }
	public double getTop(){return top; }
	public double getBottom(){return bottom; }
	
	/*
	 * digunakan untuk menggambar grid dari node untuk kasus barnes-hut algorithm
	 * 
	 */
	public void setGridLine(double left, double top, 
			double right,double bottom) {
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
	}
	
}
