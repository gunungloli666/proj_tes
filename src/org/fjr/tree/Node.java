package org.fjr.tree;

import java.util.ArrayList;
import java.util.Random;

import org.fjr.particle.BilliardParticle;
import org.fjr.particle.Particle;
import org.fjr.particle.SPHParticle;

public class Node {

	int depth ;
	static final int MAX_DEPTH = 30;
    BilliardParticle[] listParticle;
    SPHParticle particle;
    Node nodeTopLeft, nodeTopRight, nodeBottomLeft, nodeBottomRight;
    double topMargin = 0.0;
    double bottomMargin = 0.0;
    double leftMargin = 0.0;
    double rightMargin = 0.0;
    double CenterOfMass[] = new double[3];
    int numberOfParticle;
    static Random rand = new Random();
    
    boolean isLeaf = false; 
    
    ArrayList<Node> allSubNode = new ArrayList<>(); 
    
    ArrayList<SPHParticle> particleInNode  = new ArrayList<>(50);

    public Node(double left, double top, double right, double bottom, int depth) {
        leftMargin = left;
        rightMargin = right;
        topMargin = top;
        bottomMargin = bottom;
        numberOfParticle = 0;
        nodeBottomLeft = nodeBottomRight =
        		nodeTopLeft = nodeTopRight = null;
        this.depth = depth; 
    }
    
    
    public void setLeaf(boolean leaf){
    	this.isLeaf = leaf; 
    }
    
    public boolean isLeaf(){return isLeaf;}

	public void insertParticle(SPHParticle p ) {
		if(depth >  MAX_DEPTH ) {
			setParticle(p);
			this.setLeaf(true);
			return; 
		}
		QUAD q;
		if (numberOfParticle > 1) {
			q = getQuadran(p);
			insertParticleToQuadran(q, p); 
		} else if (numberOfParticle == 1) {
			q = getQuadran(particle);
			insertParticleToQuadran(q,particle);
			q = getQuadran(p);
			insertParticleToQuadran(q,p);
		} else if (numberOfParticle == 0) {
			setParticle(p);
			p.setGridLine(leftMargin, topMargin, rightMargin,
					bottomMargin);
		}
		numberOfParticle++;
	}
    
    public double getLeftMargin() {return leftMargin; }
    public double getRightMargin() {return rightMargin; }
    public double getTopMargin() {return topMargin ; }
    public double getBottomMargin() {return bottomMargin; }
    
    
    public Node getTopLeftNode(){ return nodeTopLeft; }
    public Node getTopRightNode() { return nodeTopRight; }
    public Node getBottomRightNode(){return nodeBottomRight; }
    public Node getBottomLeftNode(){return nodeBottomLeft; }
   
    
    public void setParticle(SPHParticle p) {
    	this.particle = p;
    	particleInNode.add(p); 
    }
    
    public ArrayList<SPHParticle> getParticleInNode(){
    	return particleInNode;
    }
    
    public double getNumberParticle() {return numberOfParticle;} 
    public SPHParticle getParticle() {return this.particle;}


    
    public void checkOverlap(SPHParticle particle) {
        if ((particle.getLeftSearchBox() > leftMargin && particle
                .getLeftSearchBox() < rightMargin)
                || (particle.getRightSearchBox() < rightMargin && particle
                .getRightSearchBox() > leftMargin)) {

            if ((particle.getTopSearchBox() > bottomMargin && particle
                    .getTopSearchBox() < topMargin)
                    || (particle.getBottomSearchBox() > bottomMargin && particle
                    .getBottomSearchBox() < topMargin)) {

                if (numberOfParticle > 1) {
                    if (nodeTopLeft != null) {
                        nodeTopLeft.checkOverlap(particle);
                    }
                    if (nodeTopRight != null) {
                        nodeTopRight.checkOverlap(particle);
                    }
                    if (nodeBottomLeft != null) {
                        nodeBottomLeft.checkOverlap(particle);
                    }
                    if (nodeBottomRight != null) {
                        nodeBottomRight.checkOverlap(particle);
                    }
                } else if (numberOfParticle == 1) {
                    if (particle != this.particle) {
                        particle.listNeighbor.add(this.particle);
                    }
                }
            }
        }
    }
    
    
    private void insertParticleToQuadran(QUAD q, SPHParticle particle) {
        switch (q) {
            case topLeft:
                insertTopLeftNode(particle);
                break;
            case topRight:
                insertTopRightNode(particle);
                break;
            case bottomLeft:
                insertBottomLeftNode(particle);
                break;
            case bottomRight:
                insertBottomRightNode(particle);
                break;
        }
    }

    private void insertTopRightNode(SPHParticle p) {
        double leftMargin_ = (leftMargin + rightMargin) / 2.0;
        double rightMargin_ = rightMargin;
        double topMargin_ = topMargin;
        double bottomMargin_ = (bottomMargin + topMargin) / 2.0;
        if (nodeTopRight == null) {
            nodeTopRight = new Node(leftMargin_, topMargin_, rightMargin_,
                    bottomMargin_ ,  depth++);
        }
        nodeTopRight.insertParticle(p);
    }

    private void insertTopLeftNode(SPHParticle p) {
        double leftMargin_ = leftMargin;
        double topMargin_ = topMargin;
        double rightMargin_ = (leftMargin + rightMargin) / 2.0;
        double bottomMargin_ = (bottomMargin + topMargin) / 2.0;
        if (nodeTopLeft == null) {
            nodeTopLeft = new Node(leftMargin_, topMargin_, rightMargin_,
                    bottomMargin_, depth++);
        }
        nodeTopLeft.insertParticle(p);
    }

    private void insertBottomRightNode(SPHParticle p) {
        double leftMargin_ = (leftMargin + rightMargin) / 2.0;
        double rightMargin_ = rightMargin;
        double bottomMargin_ = bottomMargin;
        double topMargin_ = (bottomMargin + topMargin) / 2.0;
        if (nodeBottomRight == null) {
            nodeBottomRight = new Node(leftMargin_, topMargin_, 
            		rightMargin_,
                    bottomMargin_, depth++);
        }
        nodeBottomRight.insertParticle(p);
    }

    private void insertBottomLeftNode(SPHParticle p) {
        double leftMargin_ = leftMargin;
        double topMargin_ = (topMargin + bottomMargin) / 2.0;
        double rightMargin_ = (rightMargin + leftMargin) / 2.0;
        double bottomMargin_ = bottomMargin;
        if (nodeBottomLeft == null) {
            nodeBottomLeft = new Node(leftMargin_, topMargin_, rightMargin_,
                    bottomMargin_, depth++);
        }
        nodeBottomLeft.insertParticle(p);
    }

    public QUAD getQuadran(Particle particle) {
        double mx = (leftMargin + rightMargin) / 2.0;
        double my = (bottomMargin + topMargin) / 2.0;
        if (particle.getX() < mx) {
            if (particle.getY() < my) {
                return QUAD.bottomLeft;
            } else {
                return QUAD.topLeft;
            }
        } else {
            if (particle.getY() < my) {
                return QUAD.bottomRight;
            } else {
                return QUAD.topRight;
            }
        }
    }
}
