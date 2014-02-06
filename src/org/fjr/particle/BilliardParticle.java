package org.fjr.particle;

public class BilliardParticle extends Particle {
	double ax = 0.0;
	double ay = 0.0;
	double az = 0.0;

	public BilliardParticle(double x, double y, double z, double vx, double vy,
			double vz) {
		super(x, y, z, vx, vy, vz);
	}

	@Override
	public void setVelocityChange(double dx, double dy, double dz) {
		this.ax = dx;
		this.ay = dy;
		this.az = dz;
	}

	@Override
	public void moveParticle() {
	}

	@Override
	public void updatePosition(double dt) {
		x += vx * dt;
		y += vy * dt;
		z += vz * dt; 
	}

	@Override
	public void updateVelocity(double dt) {
		// TODO Auto-generated method stub
		vx += ax * dt;
		vy += ay * dt;
	}

	@Override
	public void setVelocityChange(double dvx, double dvy) {
		// TODO Auto-generated method stub

	}

}