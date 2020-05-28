package kr.ac.snu.ids.PRJ1_1_2013_11431;

// Pair Class to save two data with different types.  
public class Pair<K, V> {
  private K first;
  private V second;

  public Pair(K first, V second) {
    this.first = first;
    this.second = second;
  }
  public void setFirst(K first) {
    this.first = first;
  }

  public void setSecond(V second) {
    this.second = second;
  }

  public K first() {
    return first;
  }

  public V second() {
    return second;
  }
  
  @Override
  public String toString() {
    return "(" + first.toString() + ", " + second.toString() + ")";
  }
}