/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.ipa.cfg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.ibm.wala.ssa.ISSABasicBlock;

/*
 * A aggregated basic block is group of basic blocks on a simple path
 * The basic blocks are stored in order
 */

public class AggregatedBasicBlockInContext<T extends ISSABasicBlock> {
  ArrayList<BasicBlockInContext<T>> abb;
  boolean isLogged = false; // a block is logged
  //boolean isVisible = false; // a block is not logged but its execution is determined by logs in other blocks
  boolean isFakeRoot = false;
  int nodeId = -1000; // unique id in a CFG; -10000 indicate the node id is not set 
  
  public AggregatedBasicBlockInContext() {
    abb = new ArrayList<BasicBlockInContext<T>>();
  }
  
  public AggregatedBasicBlockInContext(BasicBlockInContext<T> bb) {
    abb = new ArrayList<BasicBlockInContext<T>>();
    addBasicBlock(bb);
  }
  
  public void addBasicBlock(BasicBlockInContext<T> bb) {
    abb.add(bb);
    if (bb.getIsLogged()) {
      setIsLogged(true);
    }
  }
  
  public int getNumberOfBasicBlocks() {
    return abb.size();
  }
  
  public Iterator<BasicBlockInContext<T>> iterator() {
    return abb.iterator();
  }
  public ListIterator<BasicBlockInContext<T>> listIterator() {
    return abb.listIterator();
  }
  
  public BasicBlockInContext<T> getFirstBasicBlock() {
    return abb.get(0);
  }
  public BasicBlockInContext<T> getLastBasicBlock() {
    return abb.get(abb.size()-1);
  }
  
  private void setIsLogged(boolean isLoggedOrNot) {
    isLogged = isLoggedOrNot;
  }
  
  public boolean getIsLogged() {
    return isLogged;
  }
  /*
  public void setIsVisible(boolean isVisibleOrNot) {
    isVisible = isVisibleOrNot;
  }
  
  public boolean getIsVisible() {
    return isVisible;
  }
  */
  public void setIsFakeRoot(boolean isFakeRootOrNot) {
    isFakeRoot = isFakeRootOrNot;
  }
  
  public boolean isFakeRoot() {
    return isFakeRoot;
  }
  
  public void setNodeId(int id) {
    nodeId = id;
  }
  
  public int getNodeId() {
    return nodeId;
  }
  
  public String toString() {
    
    /*
    if (this.isFakeRoot()) {
      return "FakeRoot";
    }
    
    BasicBlockInContext<T> firstBB = getFirstBasicBlock();
    
    return "Leader[SSA index:" + firstBB.getFirstInstructionIndex() + "]" + " - "
        + firstBB.getMethod().getSignature();
    */
    
    if (this.isFakeRoot()) {
      return "0-F";
    }
    
    return (nodeId) + "-" + (isLogged? "T":"F");
  }

}
