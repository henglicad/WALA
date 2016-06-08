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

import java.util.Iterator;

import com.ibm.wala.ipa.cfg.AggregatedBasicBlockInContext;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.util.graph.NumberedGraph;
import com.ibm.wala.util.graph.impl.SlowSparseNumberedGraph;
import com.ibm.wala.util.intset.BitVector;
import com.ibm.wala.util.intset.BitVectorIntSet;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.MutableIntSet;

/**
 * Control flow graph where each node is a AggregatedBasicBlockInContext
 * @author heli
 *
 */
public class AggregatedCFG<T extends ISSABasicBlock> {
  
  /**
   * We store the graph data in a NumberedGraph data structure, each node is a AggregatedBasicBlockInContext
   */
  final private NumberedGraph<AggregatedBasicBlockInContext> aggg = 
      new SlowSparseNumberedGraph<AggregatedBasicBlockInContext>(2);
  
  /**
   * The original CFG whether each node is a BasicBlockInContext
   */
   private AbstractInterproceduralCFG<T> orig;
  /**
   * Whether a node is leader in the original CFG
   */
  private final BitVector isLeader = new BitVector();
  
  public AggregatedCFG(AbstractInterproceduralCFG<T> cfg) {
    this.orig = cfg;
  }
  
  private void findLeaders() {
    for (Iterator<BasicBlockInContext<T>> bbs = orig.iterator(); bbs.hasNext();) {
      BasicBlockInContext<T> bb = bbs.next();
      
      /*
       * a node is a leader if it has no predecessor or has 2 or more predecessors
       */
      if (orig.getPredNodeCount(bb) != 1) {
        isLeader.set(orig.getNumber(bb));
        continue;
      }
      
      /*
       * a node is a leader if its predecessor is a jump/goto
       */
      for (Iterator<BasicBlockInContext<T>> preds = orig.getPredNodes(bb); preds.hasNext();) {
        BasicBlockInContext<T> pred = preds.next();
        if (orig.getSuccNodeCount(pred) >= 2) {
          isLeader.set(orig.getNumber(bb));
          continue;
        }
      }
    }
  }
  private void buildCFG() {
    
  }

  public void make() {
    findLeaders();
    buildCFG();
  }

}
