package org.fjr.neighboor;

import org.fjr.particle.Particle;

public interface InteractionType<T extends Particle> {
	public void calculate(T p1, T p2);
//	public void correct(); 
 }
