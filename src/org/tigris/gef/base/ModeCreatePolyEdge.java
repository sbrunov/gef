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

// File: ModeCreatePolyEdge.java
// Classes: ModeCreateEdge
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.tigris.gef.base;

import java.awt.*;
import java.awt.event.*;

import org.apache.log4j.Logger;
import org.tigris.gef.graph.*;
import org.tigris.gef.presentation.*;

/** A Mode to interpret user input while creating an edge.  Basically
 *  mouse down starts creating an edge from a source port Fig, mouse
 *  motion paints a rubberband line, mouse up finds the destination port
 *  and finishes creating the edge and makes an FigEdge and sends
 *  it to the back of the Layer.
 *
 *  The argument "edgeClass" determines the type if edge to suggest
 *  that the Editor's GraphModel construct.  The GraphModel is
 *  responsible for acutally making an edge in the underlying model
 *  and connecting it to other model elements. */

public class ModeCreatePolyEdge extends ModeCreate {
    ////////////////////////////////////////////////////////////////
    // instance variables

    /** The NetPort where the arc is paintn from */
    private Object _startPort;

    /** The Fig that presents the starting NetPort */
    private Fig _startPortFig;

    /** The FigNode on the NetNode that owns the start port */
    private FigNode _sourceFigNode;

    /** The new NetEdge that is being created */
    private Object _newEdge;

    /** The number of points added so far. */
    protected int _npoints = 0;
    protected int _lastX, _lastY, _startX, _startY;
    protected Handle _handle = new Handle(-1);

    private static final Logger LOG = Logger.getLogger(ModeCreatePolyEdge.class);
    
    ////////////////////////////////////////////////////////////////
    // constructor

    public ModeCreatePolyEdge() {
        super();
        if (LOG.isDebugEnabled()) LOG.debug("Created ModeCreatePolyEdge");
    }
    public ModeCreatePolyEdge(Editor par) {
        super(par);
        if (LOG.isDebugEnabled()) LOG.debug("Created ModeCreatePolyEdge for Editor");
    }

    ////////////////////////////////////////////////////////////////
    // Mode API

    public String instructions() {
        return "Drag to define an edge to another port";
    }

    ////////////////////////////////////////////////////////////////
    // ModeCreate API

    /** Create the new item that will be drawn. In this case I would
     *  rather create the FigEdge when I am done. Here I just
     *  create a rubberband FigLine to show during dragging. */
    public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
        FigPoly p = new FigPoly(snapX, snapY);
        p.setLineColor(Globals.getPrefs().getRubberbandColor());
        p.setFillColor(null);
        p.addPoint(snapX, snapY); // add the first point twice
        _startX = _lastX = snapX;
        _startY = _lastY = snapY;
        _npoints = 2;
        return p;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /** On mousePressed determine what port the user is dragging from.
     *  The mousePressed event is sent via ModeSelect. */
    public void mousePressed(MouseEvent me) {
        if (me.isConsumed()) {
            if (LOG.isDebugEnabled()) LOG.debug("MousePressed detected but rejected as already consumed");
            return;
        }
        int x = me.getX(), y = me.getY();
        //Editor editor = Globals.curEditor();
        Fig underMouse = editor.hit(x, y);
        if (underMouse == null) {
            //System.out.println("bighit");
            underMouse = editor.hit(x - 16, y - 16, 32, 32);
        }
        if (underMouse == null && _npoints == 0) {
            done();
            me.consume();
            if (LOG.isDebugEnabled()) LOG.debug("MousePressed detected but nothing under mouse - consumed anyway");
            return;
        }
        if (!(underMouse instanceof FigNode) && _npoints == 0) {
            done();
            me.consume();
            if (LOG.isDebugEnabled()) LOG.debug("MousePressed detected but not on a FigNode - consumed anyway");
            return;
        }
        if (_sourceFigNode == null) { //_npoints == 0) {
            _sourceFigNode = (FigNode) underMouse;
            _startPort = _sourceFigNode.deepHitPort(x, y);
        }
        if (_startPort == null) {
            if (LOG.isDebugEnabled()) LOG.debug("MousePressed detected but not on a port - consumed anyway");
            done();
            me.consume();
            return;
        }
        _startPortFig = _sourceFigNode.getPortFig(_startPort);

        if (_npoints == 0) {
            super.mousePressed(me);
        }
        if (LOG.isDebugEnabled()) LOG.debug("MousePressed detected and processed by ancestor - consumed");
        me.consume();
    }

    /** On mouseReleased, find the destination port, ask the GraphModel
     *  to connect the two ports.  If that connection is allowed, then
     *  construct a new FigEdge and add it to the Layer and send it to
     *  the back. */
    public void mouseReleased(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        if (_sourceFigNode == null) {
            done();
            me.consume();
            return;
        }

        int x = me.getX(), y = me.getY();
        Fig f = editor.hit(x, y);
        if (f == null) {
            f = editor.hit(x - 16, y - 16, 32, 32);
        }
        GraphModel graphModel = editor.getGraphModel();
        if (!(graphModel instanceof MutableGraphModel)) {
            f = null;
        }
        
        MutableGraphModel mutableGraphModel = (MutableGraphModel) graphModel;
        // needs-more-work: potential class cast exception

        if (f instanceof FigNode) {
            FigNode destFigNode = (FigNode) f;
            // If its a FigNode, then check within the
            // FigNode to see if a port exists
            Object foundPort = destFigNode.deepHitPort(x, y);

            if (foundPort == _startPort && _npoints < 4) {
                // user made a false start
                done();
                me.consume();
                return;
            }

            if (foundPort != null) {
                Fig destPortFig = destFigNode.getPortFig(foundPort);
                FigPoly p = (FigPoly) _newItem;
                if (foundPort == _startPort && _npoints >= 4) {
                    p.setSelfLoop(true);
                }
                //_npoints = 0;
                editor.damageAll();
                //editor.getSelectionManager().select(p);
                p._isComplete = true;

                Class edgeClass = (Class) getArg("edgeClass");
                if (edgeClass != null)
                    _newEdge = mutableGraphModel.connect(_startPort, foundPort, edgeClass);
                else
                    _newEdge = mutableGraphModel.connect(_startPort, foundPort);

                // Calling connect() will add the edge to the GraphModel and
                // any LayerPersectives on that GraphModel will get a
                // edgeAdded event and will add an appropriate FigEdge
                // (determined by the GraphEdgeRenderer).

                if (null == _newEdge) {
                    LOG.warn("MutableGraphModel connect() returned null");
                } else {
                    LayerManager lm = editor.getLayerManager();
                    _sourceFigNode.damage();
                    destFigNode.damage();
                    Layer lay = editor.getLayerManager().getActiveLayer();
                    FigEdge fe = (FigEdge) lay.presentationFor(_newEdge);
                    _newItem.setLineColor(Color.black);
                    fe.setFig(_newItem);
                    fe.setSourcePortFig(_startPortFig);
                    fe.setSourceFigNode(_sourceFigNode);
                    fe.setDestPortFig(destPortFig);
                    fe.setDestFigNode(destFigNode);

                    if (fe != null)
                        editor.getSelectionManager().select(fe);
                    editor.damageAll();

                    // if the new edge implements the MouseListener interface it has to receive the mouseReleased() event
                    if (fe instanceof MouseListener)
                         ((MouseListener) fe).mouseReleased(me);

                    // set the new edge in place
                    if (_sourceFigNode != null)
                        _sourceFigNode.updateEdges();
                    if (destFigNode != null)
                        destFigNode.updateEdges();
                }
                done();
                me.consume();
                return;
            }
        }
        if (!nearLast(x, y)) {
            editor.damageAll();
            Point snapPt = new Point(x, y);
            editor.snap(snapPt);
            ((FigPoly) _newItem).addPoint(snapPt.x, snapPt.y);
            _npoints++;
            editor.damageAll();
        }
        _lastX = x;
        _lastY = y;
        me.consume();
    }

    public void mouseMoved(MouseEvent me) {
        mouseDragged(me);
    }

    public void mouseDragged(MouseEvent me) {
        if (me.isConsumed())
            return;
        int x = me.getX(), y = me.getY();
        if (_npoints == 0) {
            me.consume();
            return;
        }
        if (_newItem == null) {
            me.consume();
            return;
        }
        FigPoly p = (FigPoly) _newItem;
        editor.damageAll(); // startTrans?
        Point snapPt = new Point(x, y);
        editor.snap(snapPt);
        _handle.index = p.getNumPoints() - 1;
        p.moveVertex(_handle, snapPt.x, snapPt.y, true);
        editor.damageAll(); // endTrans?
        me.consume();
    }

    /** Internal function to see if the user clicked twice on the same spot. */
    protected boolean nearLast(int x, int y) {
        return x > _lastX - Editor.GRIP_SIZE
            && x < _lastX + Editor.GRIP_SIZE
            && y > _lastY - Editor.GRIP_SIZE
            && y < _lastY + Editor.GRIP_SIZE;
    }

    public void done() {
        super.done();
        if (_newItem != null)
            editor.damageAll();
        _newItem = null; // use this as the fig for the new FigEdge
        _npoints = 0;
        _sourceFigNode = null;
        _startPort = null;
        _startPortFig = null;
    }

    ////////////////////////////////////////////////////////////////
    // key events

    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) { // escape
            done();
            ke.consume();
        }
    }
} /* end class ModeCreatePolyEdge */
