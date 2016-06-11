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
  boolean isLogged = false;
  
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
  
  public String toString() {
    BasicBlockInContext<T> firstBB = getFirstBasicBlock();
    
    /*
    try {
      String firstInst = firstBB.getFirstInstruction().toString();
      return "FirstInst[" + firstInst + "]";
    } catch(ArrayIndexOutOfBoundsException ex) {
      String firstInst = "No Instruction";
      return "FirstInst[" + firstInst + "]";
    } catch (NullPointerException ex) {
      String firstInst = "Null Instruction";
      return "FirstInst[" + firstInst + "]";
    }
    */
    return "Leader[SSA index:" + firstBB.getFirstInstructionIndex() + "]" + " - "
        + firstBB.getMethod().getSignature();
  }

}
