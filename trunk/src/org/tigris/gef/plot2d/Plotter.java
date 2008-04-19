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
package org.tigris.gef.plot2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * @author Bob
 */
public interface Plotter {
    /** Paint this line object. */
    public abstract void drawLine(Object graphicsContext, int lineWidth,
            Color lineColor, int x1, int y1, int x2, int y2,
            boolean dashed, float[] dashes,
            int dashPeriod);

    public abstract int drawDashedLine(
            Object graphicsContext, 
            int lineWidth, 
            int x1, int y1, int x2, int y2, 
            int phase,
            float[] dashes, int dashPeriod);
    
    public abstract void drawOval(
            Object graphicsContext,
            boolean filled,
            Color fillColor,
            Color lineColor,
            int lineWidth,
            boolean dashed,
            int x,
            int y,
            int w,
            int h);
    
    public abstract void drawRect(
            Object graphicsContext, 
            boolean filled, 
            Color fillColor, 
            int lineWidth, 
            Color lineColor, 
            int x, 
            int y, 
            int w, 
            int h, 
            boolean dashed, 
            float dashes[], 
            int dashPeriod);
    
    public abstract void drawCube(
            Object graphicContext, 
            Color lineColor, 
            Color fillColor, 
            int x, 
            int y, 
            int w, 
            int h, 
            int d);
    
    public abstract void drawPoly(
            Object graphicsContext, 
            boolean filled, Color fillColor, 
            int lineWidth, Color lineColor, 
            int pointCount, int xPoints[], int yPoints[], 
            boolean dashed, float dashes[], int dashPeriod);
    
    public abstract void drawCurve(Object g, Polygon curve, boolean filled, Color fillColor, Color lineColor, int npoints, int xKnots[], int yKnots[]);
    public abstract void drawStraight(Object g, Color lineColor, int xKnots[], int yKnots[]) ;  
}