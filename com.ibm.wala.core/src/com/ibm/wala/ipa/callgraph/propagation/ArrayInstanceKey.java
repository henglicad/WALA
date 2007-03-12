/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.ipa.callgraph.propagation;

import com.ibm.wala.classLoader.ArrayClass;
import com.ibm.wala.classLoader.IClass;

/**
 * A pointer key which represents the contents of an array instance.
 */
public final class ArrayInstanceKey extends AbstractFieldPointerKey implements FilteredPointerKey {
  public ArrayInstanceKey(InstanceKey instance) {
    super(instance);
  }

  public boolean equals(Object obj) {
    if (obj instanceof ArrayInstanceKey) {
      ArrayInstanceKey other = (ArrayInstanceKey) obj;
      return instance.equals(other.instance);
    } else {
      return false;
    }
  }

  public int hashCode() {
    return 1061 * instance.hashCode();
  }

  public String toString() {
    return "[" + instance + "[]]";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.wala.ipa.callgraph.propagation.PointerKey#getTypeFilter()
   */
  public TypeFilter getTypeFilter() {
    return new SingleClassFilter(((ArrayClass) instance.getConcreteType()).getElementClass());
  }
}
