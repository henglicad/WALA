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

import java.util.HashMap;
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
  final private NumberedGraph<AggregatedBasicBlockInContext<T>> aggg = 
      new SlowSparseNumberedGraph<AggregatedBasicBlockInContext<T>>(2);
  
  private boolean constructedAggregatedCFG;
  
  /**
   * The original CFG whether each node is a BasicBlockInContext
   */
   private AbstractInterproceduralCFG<T> orig;
  /**
   * Whether a node is leader in the original CFG
   */
  private final BitVector isLeader = new BitVector();
  
  /*
   * A map from an original basic block (BasicBlockInContext) # to an aggregated basic block (AggregatedBasicBlockInContext) # 
   */
  private HashMap bb2abb = new HashMap<Integer, Integer>();
  
  public AggregatedCFG(AbstractInterproceduralCFG<T> cfg) {
    this.orig = cfg;
    constructAggregatedCFG();
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
    addNodes();
    System.out.println("bb2abb: " + bb2abb.toString());
    addEdges();
  }
  
  private void addNodes() {
    for (Iterator<BasicBlockInContext<T>> bbs = orig.iterator(); bbs.hasNext();) {
      BasicBlockInContext<T> bb = bbs.next();
      if (isLeader.get(orig.getNumber(bb))) {
        AggregatedBasicBlockInContext<T> abb = new AggregatedBasicBlockInContext<T>();
        aggg.addNode(abb);
        abb.addBasicBlock(bb);
        bb2abb.put(orig.getNumber(bb), aggg.getNumber(abb));
        if (orig.getSuccNodes(bb).hasNext()) {
          BasicBlockInContext<T> succbb = orig.getSuccNodes(bb).next();
          while (!isLeader.get(orig.getNumber(succbb))) {
            abb.addBasicBlock(succbb);
            bb2abb.put(orig.getNumber(succbb), aggg.getNumber(abb));
            if (orig.getSuccNodes(succbb).hasNext()) {
              succbb = orig.getSuccNodes(succbb).next();
            } else {
              break;
            }
          }
        }
      }
    }
  }
  
  /**
   * An aggregated basic block A is the successor of an aggregated basic block B iff
   * the first basic block of A is a successor of the last basic block of B in the original cfg.
   */
  private void addEdges() {
    for (Iterator<AggregatedBasicBlockInContext<T>> abbs = aggg.iterator(); abbs.hasNext();) {
      AggregatedBasicBlockInContext<T> abb = abbs.next();
      BasicBlockInContext<T> lbb = abb.getLastBasicBlock();
      for (Iterator<BasicBlockInContext<T>> succbbs = orig.getSuccNodes(lbb); succbbs.hasNext();){
        BasicBlockInContext<T> succbb = succbbs.next();
        AggregatedBasicBlockInContext<T> succabb = aggg.getNode((int)bb2abb.get(orig.getNumber(succbb)));
        aggg.addEdge(abb, succabb);
      }
    }
  }

  public NumberedGraph<AggregatedBasicBlockInContext<T>> constructAggregatedCFG() {
    if (!constructedAggregatedCFG) {
      findLeaders();
      System.out.println("Number of leaders: " + isLeader.toString());
      buildCFG();
      constructedAggregatedCFG = true;
      System.out.println("Number of aggregated basic blocks: " + aggg.getNumberOfNodes() + "\n");
    }
    return this.aggg;
  }

  public Iterator<AggregatedBasicBlockInContext<T>> iterator() {
    if (!constructedAggregatedCFG) {
      constructAggregatedCFG();
    }
    return this.aggg.iterator();
  }
  
  public NumberedGraph<AggregatedBasicBlockInContext<T>> getGraph() {
    return this.aggg;
  }
}
