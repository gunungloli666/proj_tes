package org.fjr.particle;

import org.fjr.main.MainProperty;

public enum TypeFluid {
	
	WATER(MainProperty.density , 
			MainProperty.sphStiff, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens), 
	OLI(MainProperty.density,
			MainProperty.sphStiff, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens), 
	GLICERIN(MainProperty.density,
			MainProperty.sphStiff, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens);
	
	double densitas;
	double sphStiff; 
	double sphStiffNear; 
	double sphRestDensity; 
	
	TypeFluid(double density, double sphStiff, 
			double sphStiffNear, double sphRestDensity){
		this.densitas = density; 
		this.sphStiff = sphStiff; 
		this.sphStiffNear = sphStiffNear;
		this.sphRestDensity = sphRestDensity; 
	}
	
	public double getDensitas(){
		return densitas; 
	}
	
	public double getSPHStiff(){
		return sphStiff; 
	}
	
	public double getSPHStiffNear(){
		return sphStiffNear;
	}
	
	public double getRestDensity(){
		return sphRestDensity;
	}
}
