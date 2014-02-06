/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fjr.kernel;

import org.fjr.particle.Particle;

/**
 *
 * @author fajar
 */
public interface Kernel {

    public double getKernel();

    public double[] getDerivativeKernel();

    public boolean isCloser(Particle p1, Particle p2);
}
