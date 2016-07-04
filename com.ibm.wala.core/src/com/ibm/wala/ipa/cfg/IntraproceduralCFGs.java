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

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;

public class IntraproceduralCFGs {
    
  public void getAllMethods(AnalysisScope  scope) throws ClassHierarchyException {
    IClassHierarchy cha = ClassHierarchy.make(scope);
    for (IClass cl : cha) {
      if (cl.getClassLoader().getReference().equals(ClassLoaderReference.Application)) {
        for (IMethod m : cl.getAllMethods()) {
          System.out.println(m.getSignature());
        }
      }
    }
  }
  
  

}
