package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;

public class Pair implements Comparable<Pair>, Serializable {
  private static final long serialVersionUID = 1L;
  
  private String first;
  private String second;
  
  public Pair(String first, String second) {
    this.first = first;
    this.second = second;
  }
  
  public String first() {
    return first;
  }
  
  public String second() {
    return second;
  }

  @Override
  public int compareTo(Pair o) {
    if (this.first.equals(o.first()) && this.second.equals(o.second())) {
      return 0;
    }
    return 1;
  }
  
  @Override
  public String toString() {
    return "(" + this.first + ", " + this.second + ")";
  }
}