


package org.fjr.particle;

/**
 *
 * @author fajar
 */
public class SPhysicsParticle extends  Particle{
    
    double pressure = 0D; 
    double density = 0D; 
    double massa = 0D;
    double radius = 0D;
    double speed_of_sound = 0D;
    double energy = 0D; 
    
    TypeParticle type ; 
    
    double xdot, ydot, udot, vdot, rhodot,  edot;
    
    double volume = 0D;  
    
    
    double xcor , ycor ; 
    
    double pr ; 
    
    static final  double B =  280285.72 ;
    static final double i_gamma = 7D ;
    public static final double rho0 =  1000.0000 ; 
    static final double cs0 =  Math.sqrt(i_gamma *B 
    		/ rho0);
    
    
    double dt2 = 0D; 
    
    double x0 , y0, u0 , v0 , pressure0, density0, energy0; 
    
    public SPhysicsParticle(double x, double y, double vx,
            double vy, double radius,
            double density, double massa, double pressure, 
            TypeParticle type) {
        super(x, y, vx, vy);
        this.density = density;
        this.pressure = pressure;
        this.type = type;
        this.radius = radius;
        this.massa = massa; 
    }
    
    
    
    public void resetAcceleration(){
        xdot = 0D; 
        ydot = 0D; 
        udot = 0D; 
        vdot = 0D; 
        rhodot = 0D; 
        edot = 0D; 
        xcor = 0D; 
        ycor = 0D; 
        
        pr = pressure /(density * density); 
    }
    
   
    public void setDensity(double density){this.
    	density = density; }
    public void resetDensity(){this.density = rho0; }
    
    public double getPressureOverDensitySquare(){
        return pr;
    }
    
    
    public void taitsEquationOfStates(){
        pressure = B * (Math.pow(( density/ rho0 ), 
        		i_gamma - 1.0 )); 
        speed_of_sound = cs0 * (Math.pow( density 
        		/ rho0 , 3)); 
    }
    
    public void changeVelocityBy(double changex , 
    		double changey){
        udot += changex; 
        vdot += changey; 
    }
    
    public void changeEnergyBy(double change){
        edot += change; 
    }
    
    public void changeDensityBy(double change){
        rhodot += change; 
    }
    
    public void correctPositionBy(double changeX,
    		double changeY){
        xcor += changeX; 
        ycor += changeY; 
    }
    
    public void tuningPositionCorrection(double tuning){
        xcor *= tuning;
        ycor *= tuning; 
    }
    
    public void correctPositionChange(){
        xdot = udot + xcor; 
        ydot = vdot + xcor; 
    }
    
    
    static double gravity = 9.8D; 
    static double accx  = 0D; 
    public void correctVelocityChange(){
        if(type != TypeParticle.Boundaries){
            udot += gravity ;
            vdot += accx; 
        }
    }
    
    public void backupMovingProperty(){
        x0 = x; 
        y0 = y; 
        u0 = vx; 
        v0 = vy; 
        pressure0 = pressure;
        density0 = density; 
        energy0 = energy;
    }
    
    public void setVariabelTimeStep( double dt2){
        this.dt2 = dt2; 
    }
    
    public void changeDensity(){
        density  = density0  + dt2 * rhodot;
    }
    
    public void changeEnergy(){
        energy = energy0 + dt2 * edot; 
    }
    
    public void setupVolume(){
        volume = massa / density; 
    }
    
    public void changePoisition(){
        x = x0 + dt2 * xdot; 
        y = x0 + dt2 * ydot;
    }
    
    public void changeVelocity(){
        vx = v0 + dt2 * vdot; 
        vy = u0 + dt2 * udot; 
    }
    
    public void finalChangeVelocity(){
       vx = 2.0 * vx - u0;
       vy = 2.0 * vy - v0;   
    }
    
    public void finalChangePosition(){
        x = 2.0 * x - x0; 
        y = 2.0 * y - y0; 
    }
    
    public void finalChangeDensity(){
        density = 2.0 * density - density0;
    }
    
    public void finalChangeEnergy(){
        energy = 2.0 * energy - energy0; 
    }
    
    public double getMassa(){return massa; }
    public double getSpeedOfSound(){return speed_of_sound;}
    public double getDensity(){return density; }
    
    @Override
    public void setVelocityChange(double dvx, double dvy){
    }

    @Override
    public void setVelocityChange(double dvx, double dvy,
    		double dvz) {
    }

    @Override
    public void moveParticle() {
    }

    @Override
    public void updatePosition(double dt) {
        throw new UnsupportedOperationException("Not supported yet.");     
    }

    @Override
    public void updateVelocity(double dt) {
        throw new UnsupportedOperationException("Not supported yet.");    
    }
}
