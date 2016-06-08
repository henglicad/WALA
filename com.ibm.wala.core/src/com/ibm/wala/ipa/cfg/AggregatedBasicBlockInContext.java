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

public class AggregatedBasicBlockInContext {
  ArrayList<BasicBlockInContext> abb;
  boolean isLogged = false;
  
  public AggregatedBasicBlockInContext() {
    abb = new ArrayList<BasicBlockInContext>();
  }
  
  public void addBasicBlock(BasicBlockInContext bb) {
    abb.add(bb);
    if (bb.getIsLogged()) {
      setIsLogged(true);
    }
  }
  
  public int getNumberOfBasicBlocks() {
    return abb.size();
  }
  
  public Iterator<BasicBlockInContext> iterator() {
    return abb.iterator();
  }
  public ListIterator<BasicBlockInContext> listIterator() {
    return abb.listIterator();
  }
  
  public BasicBlockInContext getFirstBasicBlock() {
    return abb.get(0);
  }
  public BasicBlockInContext getLastBasicBlock() {
    return abb.get(abb.size()-1);
  }
  
  private void setIsLogged(boolean isLoggedOrNot) {
    isLogged = isLoggedOrNot;
  }
  
  public boolean getIsLogged() {
    return isLogged;
  }
  
  public String toString() {
    BasicBlockInContext firstBB = getFirstBasicBlock();
    return "FirstInst[" + firstBB.getFirstInstruction().toString() + "]";
    /*
    return "BB[SSA:" + getFirstInstructionIndex() + ".." + getLastInstructionIndex() + "]" + getNumber() + " - "
        + method.getSignature();
    */
  }

}
