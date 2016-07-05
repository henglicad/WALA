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
import java.util.List;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;

public class IntraproceduralCFGs {
  AnalysisScope scope;
  List<IMethod> methods;
  
  public IntraproceduralCFGs(AnalysisScope scope) throws ClassHierarchyException {
    this.scope = scope;
    this.methods = getAllMethods();
  }
  
  public List<IMethod> getAllMethods() throws ClassHierarchyException {
    List<IMethod> methods = new ArrayList<IMethod>();
    IClassHierarchy cha = ClassHierarchy.make(scope);
    for (IClass cl : cha) {
      /* filtering out non-application classes */
      if (cl.getClassLoader().getReference().equals(ClassLoaderReference.Application)) {
        for (IMethod m : cl.getAllMethods()) {
          /* filtering out methods of applications classes but which are declared in non-application classes */
          if (m.getDeclaringClass().getClassLoader().getReference().equals(ClassLoaderReference.Application)) {
            methods.add(m);
            //System.out.println(m.getSignature());
          }
          
        }
      }
    }
    return methods;
  }
  
  public void printAllMethods() {
    for (IMethod m : methods) {
      System.out.println(m.getSignature());
    }
  }
  
  

}
