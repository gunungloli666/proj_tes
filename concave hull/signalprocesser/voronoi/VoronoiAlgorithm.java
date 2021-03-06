package signalprocesser.voronoi;
import signalprocesser.voronoi.representation.*;
import signalprocesser.voronoi.eventqueue.*;
import signalprocesser.voronoi.statusstructure.*;

import java.util.*;
import java.awt.Graphics2D;

public class VoronoiAlgorithm {
    
    /* ********************************************************* */
    // Test Main Function
    
    public static void main(String[] args) {
        VoronoiTest.main(args);
    }
    
    private VoronoiAlgorithm() { }
    
    /* ********************************************************* */
    // Generation Algorithm
    
    public static void generateVoronoi(RepresentationInterface datainterface, Collection<VPoint> points) {
        generateVoronoi(datainterface, points, null, null, -1);
    }
    protected static void generateVoronoi(RepresentationInterface datainterface, 
    		Collection<VPoint> points, Graphics2D g, VPoint attentiontopoint, int attentiontopos) {
        // Initialise event queue with all site events
        EventQueue eventqueue = new EventQueue(); // di sini untuk menampung... 
        VSiteEvent attentiontositeevent = null;
        for ( VPoint point : points ) {
            VSiteEvent newsiteevent = new VSiteEvent( point );
            if ( point==attentiontopoint ) {
                attentiontositeevent = newsiteevent;
            }
            // pair dan key nilainya sama..... 
            eventqueue.addEvent( newsiteevent );
        }
        
        // Initialise the data interface
        datainterface.beginAlgorithm(points);

        // Reset Debug/Userfriendly ID's debug to newly created nodes/events
        VEvent.uniqueid = 1;
        signalprocesser.voronoi.statusstructure.binarysearchtreeimpl.BSTStatusStructure.uniqueid = 1;
        
        // Initialise an empty status structure
        AbstractStatusStructure statusstructure = AbstractStatusStructure.createDefaultStatusStructure();
        
        // While the queue is not empty
        VEvent event = null;
        boolean printcalled = false;
        while (!( eventqueue.isEventQueueEmpty() )) {
            // Remove the event with the largest y coord
            event = eventqueue.getAndRemoveFirstEvent();
            
            // Debug Code (note: can just set g to null to parse over)
            if ( g!=null && attentiontositeevent==null && attentiontopos>=0 && printcalled==false ) { // ini g masuk
                if ( event!=null && event.getY()>=attentiontopos ) {
                    printcalled = true;
                    statusstructure.print(g, null, attentiontopos);
                    
//                    System.out.println("Lagi Masuk... "); 
                    // Close the data interface and return
                    datainterface.endAlgorithm(points, event.getY(), statusstructure.getHeadNode());
                    return;
                }
            }
            
            // If site event, otherwise circle event
            if ( event.isSiteEvent() ) { // ini masuk......  // node-nodenya diatur di sini 
                VSiteEvent siteevent = (VSiteEvent) event; // rala: ketika titiknya belum sampai tiga.... 
                											// dia masuknya di sini.... 
//                System.out.println("masuk site event preet., ");
                
//                System.out.println("is site event... "); 
                // Debug Code (note: can just set g to null to parse over)
                if ( g!=null && siteevent==attentiontositeevent ) { // ini g masuk... 
                    statusstructure.print(g, siteevent, siteevent.getY());
//                    System.out.println("masuk... "); 
                    // Close the data interface and return
                    datainterface.endAlgorithm(points, event.getY(), statusstructure.getHeadNode());
                    return;
                }
                
                // If status structure is empty, insert so that the status
                //  structure consists of a single leaf storing the site event
                if ( statusstructure.isStatusStructureEmpty() ) { // ini satu kali masuk saja,.... setelah itu g ada..... 
                    // Set the root node
//                	System.out.println("masuk... "); 
                    statusstructure.setRootNode( siteevent );
                    
                    // Also: check for the degrading case (equal y values between
                    //  first and second nodes of queue)
                    VEvent nextevent = eventqueue.getFirstEvent(); 
                    if ( nextevent!=null && event.getY()==nextevent.getY() ) {
                        // Increment original event by minus one pixel to fix error
//                        System.out.println("Please note: easy fix done to prevent degrading case");
                        siteevent.getPoint().y--;
                        
                        /*// Move remove entirely from queue and re-add - changing
                        //  something which the Comparator dependents on results
                        //  in unexpected behaviour
                        nextevent = eventqueue.getAndRemoveFirstEvent();
                        ((VSiteEvent)nextevent).getPoint().y++;
                        eventqueue.addEvent(nextevent);*/
                    }
                    continue;
                }
                
                // Search the status structure for leaf which represents
                //  the arc directly above the site event
                VLinkedNode leafabove = statusstructure.getNodeAboveSiteEvent( siteevent , siteevent.getY() );
                
                // Delete any circle events
                leafabove.removeCircleEvents(eventqueue);
                
                // Insert the new node
                VLinkedNode newnode = statusstructure.insertNode(leafabove, siteevent);
                VLinkedNode prevnode = newnode.getPrev();
                VLinkedNode nextnode = newnode.getNext();
                
                // Determine circle events
                if ( prevnode!=null ) prevnode.addCircleEvent(eventqueue); // perhatikan yang ini..... 
                if ( nextnode!=null ) nextnode.addCircleEvent(eventqueue);
                
                // Record event now that new node has been inserted into the tree
                datainterface.siteEvent(
                        prevnode ,
                        newnode ,
                        nextnode ); //  untuk triangular representation ini g dipake..... 
            } else if ( event.isCircleEvent() ) { // ini terakhr kali.... 
                VCircleEvent circleevent = (VCircleEvent) event;
//                System.out.println("masuk circle event"); 
                // Get linked nodes
                VLinkedNode currnode = circleevent.leafnode;
                VLinkedNode prevnode = currnode.getPrev();
                VLinkedNode nextnode = currnode.getNext();
                
                // Record event before center node is deleted from the tree
                datainterface.circleEvent(
                        prevnode ,
                        currnode ,
                        nextnode ,
                        circleevent.getX(), circleevent.getCenterY()
                        );
                
                // Remove any circle events, before removing node
                currnode.removeCircleEvents(eventqueue);
                
                // Remove event from structure
                statusstructure.removeNode(eventqueue, currnode);
                
                // Remove Circle Events for prev (pi) and next (pk)
                prevnode.removeCircleEvents(eventqueue);
                nextnode.removeCircleEvents(eventqueue);
                
                // Determine circle events for p1,pi,(pjremoved),pk and pi,(pjremoved),pk,p2
                if ( prevnode!=null ) prevnode.addCircleEvent(eventqueue);
                if ( nextnode!=null ) nextnode.addCircleEvent(eventqueue);
            } else {
                throw new RuntimeException("Unknown event; " + event.getClass().getName());
            }
        }
        
        // Debug Code (note: can just set g to null to parse over)
        if ( g!=null && attentiontositeevent==null && attentiontopos>=0 && printcalled==false ) { // ini g masuk..... 
            printcalled = true;
//            System.out.println(164);   
            statusstructure.print(g, null, attentiontopos);
        }
        
        // Close the data interface
        if ( event==null ) { // ini dipanggil ketika pertama kali masuk.... 
//        	System.out.println(170); 
            datainterface.endAlgorithm(points, -1, statusstructure.getHeadNode());
        } else { // ini dipanggil sesudahnya.... 
//        	System.out.println(172); 
            datainterface.endAlgorithm(points, event.getY(), statusstructure.getHeadNode());
        }
    }
    
    /* ********************************************************* */
}