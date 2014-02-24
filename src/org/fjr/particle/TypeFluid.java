package org.fjr.particle;

import org.fjr.main.MainProperty;

public enum TypeFluid {
	WATER(MainProperty.density , 
			MainProperty.sphStiff, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens), 
	OLI(MainProperty.density * 0.8,
			MainProperty.sphStiff * (20./3.), 
			MainProperty.sphStiffNear * 0.5, 
			MainProperty.sphRestDens), 
	GLICERIN(MainProperty.density ,
			MainProperty.sphStiff * 7.6, 
			MainProperty.sphStiffNear*2., 
			MainProperty.sphRestDens), 
	MADU(MainProperty.density * 1.5,
			MainProperty.sphStiff * (20./3.), 
			MainProperty.sphStiffNear  * 1.8, 
			MainProperty.sphRestDens),
	SAME_STIFF_1(MainProperty.density ,
			MainProperty.sphStiff * 0.5, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens), 
	SAME_STIFF_2(MainProperty.density ,
			MainProperty.sphStiff * 1.5, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens), 
	SAME_STIFF_3(MainProperty.density ,
			MainProperty.sphStiff * 2.0, 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens), 
	SAME_STIFFNEAR_1(MainProperty.density ,
			MainProperty.sphStiff , 
			MainProperty.sphStiffNear* 0.5, 
				MainProperty.sphRestDens),
	SAME_STIFFNEAR_2(MainProperty.density ,
			MainProperty.sphStiff , 
			MainProperty.sphStiffNear * 1.5, 
			MainProperty.sphRestDens), 
	SAME_STIFFNEAR_3(MainProperty.density ,
			MainProperty.sphStiff , 
			MainProperty.sphStiffNear * 2.0, 
			MainProperty.sphRestDens), 
	SAME_REST_DENS_1(MainProperty.density ,
			MainProperty.sphStiff , 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens * 0.5), 
	SAME_REST_DENS_2(MainProperty.density ,
			MainProperty.sphStiff , 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens * 1.5), 
	SAME_REST_DENS_3(MainProperty.density ,
			MainProperty.sphStiff , 
			MainProperty.sphStiffNear, 
			MainProperty.sphRestDens * 2.0)
	;
		
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
