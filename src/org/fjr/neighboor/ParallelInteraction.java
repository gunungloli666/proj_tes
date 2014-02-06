package org.fjr.neighboor;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

import org.fjr.main.Process;
import org.fjr.particle.Particle;
import org.fjr.particle.SPHParticle;

public class ParallelInteraction<T extends  Particle>
extends RecursiveAction {

    private static final long serialVersionUID = 1L;
    Process process;
    int numgridX;
    int numGridY;
    ArrayList<Integer> hash[][];
    ArrayList<T > allParticle;
    int length;
    int batasBawah;
    InteractionType<T> interaction;

    public ParallelInteraction(Process process,
            InteractionType<T> interaction,
            int batasBawah,
            int length) {
        this.process = process;
        this.interaction = interaction;
        numgridX = process.getGridX();
        numGridY = process.getGridY();
        hash = process.getHash();
        allParticle = process.getAllParticle();
        this.batasBawah = batasBawah;
        this.length = length;
    }

    private void doubleInteraction(InteractionType<T>
    interaction)
    {
        for (int i = batasBawah; i < batasBawah + 
        		length; i++) {
            int hcell = process.checkX(
            		allParticle.get(i).getX());
            int vcell = process.checkY(
            		allParticle.get(i).getY());
            for (int nx = -1; nx < 2; nx++) {
                for (int ny = -1; ny < 2; ny++) {
                    int xc = hcell + nx;
                    int yc = vcell + ny;
                    if (xc > -1 && xc < numgridX && yc > 
                    -1 && yc < numGridY
                            && hash[xc][yc].size() > 0) {
                        for (int a = 0; a < hash[xc][yc]
                        		.size(); a++) {
                            Integer ne = (Integer) 
                            		hash[xc][yc].get(a);
                            if (ne != null && ne.
                            		intValue() != i) {
                                interaction.
                                calculate(
                                		allParticle.
                                		get(i),
                                        allParticle.
                                        get(ne.intValue()
                                        		));
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void compute() {
        int bagi = length / 2;
        if (length < 100) {
            doubleInteraction(interaction);
            return;
        }
        ParallelInteraction p1 = new
        		ParallelInteraction(process, 
        		interaction,
                batasBawah, bagi);
        ParallelInteraction p2 = 
        		new ParallelInteraction(process, 
        		interaction,
                batasBawah + bagi, length - bagi);
        invokeAll(p1, p2);
    }
}
