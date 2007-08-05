// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: SwingMouseEventWrapper.java
// Classes: SwingMouseEventWrapper
// Original Author: johnnycoding@gmail.com
// $Id$

package org.tigris.gef.swt;

import java.awt.Point;

import org.tigris.gef.graph.presentation.GraphInternalPane;

/**
 * A class which implements org.tigris.gef.base.MouseEvent interface and wraps
 * the functions of java.awt.event.MouseEvent
 * <p>
 * 
 * It defines a private java.awt.event.MouseEvent variable and set 
 * it to the passed-in java.awt.event.MouseEvent parameter in the constructor 
 * method.   
 * <p>
 * 
 * Then for each implemented function, it calls the corresponding function in 
 * the private java.awt.event.MouseEvent variable.
 * 
 * @see java.awt.event.MouseEvent
 * @see orgtigris.gef.base.MouseEvent
 */
public class SwingWTMouseEventWrapper implements org.tigris.gef.base.MouseEvent {

    private swingwt.awt.event.MouseEvent event;
    private GraphInternalPane _jComponent = null;

    //set the private event to the awt MouseEvent
    public SwingWTMouseEventWrapper(swingwt.awt.event.MouseEvent me) {
        this.event = me;
        _jComponent = (GraphInternalPane)me.getComponent();
    }

    /*
     * @see org.tigris.gef.base.MouseEvent#getButton()
     */
    public int getButton() {
        return event.getButton();
    }

    /*
     * @see org.tigris.gef.base.MouseEvent#getClickCount()
     */
    public int getClickCount() {
        return event.getClickCount();
    }

    /*
x     * @see org.tigris.gef.base.MouseEvent#getPoint()
     */
    public Point getPoint() {
        return new Point(event.getX(), event.getY());
    }

    /*
     * @see org.tigris.gef.base.MouseEvent#getX()
     */
    public int getX() {
        return event.getX();
    }

    /*
     * @see org.tigris.gef.base.MouseEvent#getY()
     */
    public int getY() {
        return event.getY();
    }

    /*
     * @see org.tigris.gef.base.MouseEvent#isPopupTrigger()
     */
    public boolean isPopupTrigger() {
        return event.isPopupTrigger();
    }

    /*
     * @see org.tigris.gef.base.MouseEvent#translatePoint(int, int)
     */
    public void translatePoint(int x, int y) {
        event.translatePoint(x, y);
    }

    /*
     * @see org.tigris.gef.base.InputEvent#consume()
     */
    public void consume() {
        event.consume();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#getID()
     */
    public int getID() {
        return event.getID();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#getModifiers()
     */
    public int getModifiers() {
        return event.getModifiers();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#getWhen()
     */
    public long getWhen() {
        return event.getWhen();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#isAltDown()
     */
    public boolean isAltDown() {
        return event.isAltDown();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#isAltGraphDown()
     */
    public boolean isAltGraphDown() {

        return event.isAltGraphDown();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#isConsumed()
     */
    public boolean isConsumed() {
        return event.isConsumed();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#isControlDown()
     */
    public boolean isControlDown() {
        return event.isControlDown();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#isMetaDown()
     */
    public boolean isMetaDown() {
        return event.isMetaDown();
    }

    /*
     * @see org.tigris.gef.base.InputEvent#isShiftDown()
     */
    public boolean isShiftDown() {
        return event.isShiftDown();
    }
    
    /*
     * @see org.tigris.gef.base.InputEvent#getComponent()
     */
    public GraphInternalPane getComponent() {
        // TODO Auto-generated method stub
        return _jComponent;
    }

    /*
     * @see org.tigris.gef.base.InputEvent#getComponent()
     */
   public void setComponent(GraphInternalPane gip) {
        _jComponent = gip;        
    }
}