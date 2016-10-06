package JavaUtil;

import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.Comparator;

/**
 * Does exactally what is says on the tin.
 *
 * @author Jonathan Wilkins
 * @version 1.0 <br>history -><pre>
 *  [1.0 JW]   10/05/2002    Created.
 * </pre>
 */

public class AlphaSortedEnumeration implements Enumeration, Comparator {
  private List list = new ArrayList();
  private int count = 0;

  public AlphaSortedEnumeration(Enumeration enum1) {
    for(; enum1.hasMoreElements(); list.add(enum1.nextElement()));
      Collections.sort(list,this);
  }

  public AlphaSortedEnumeration(Collection coll) {
    if(!(coll instanceof List)) {
      for(Iterator it = coll.iterator(); it.hasNext(); list.add(it.next()));
    } else {
      list = (List)coll;
    }
    Collections.sort(list,this);
  }

  /**
   * Tests if this enumeration contains more elements.
   * @return  true if and only if this enumeration object contains at least one more element to provide;
   * false otherwise.
   */
  public boolean hasMoreElements() {
    return count!=list.size();
  }

  /**
   * Returns the next element of this enumeration if this enumeration object has at least one more
   * element to provide, elements are sorted in alphabetical order.
   * @return the next element of this enumeration.
   */
  public Object nextElement() {
    return list.get(count++);
  }

  public int compare(Object o1, Object o2) {
    if (o1 instanceof Number && o2 instanceof Number) {
      double d1 = ((Number)o1).doubleValue();
      double d2 = ((Number)o2).doubleValue();
      return d1 == d2 ? 0 : d1 < d2 ? -1 : 1;
    } else {
      String o1s = o1.toString();
      String o2s = o2.toString();
      return o1s.compareToIgnoreCase(o2s);
    }
  }

  /**
   * This is not used
   */
  public boolean equals(Object obj) {
    return false;
  }

}
