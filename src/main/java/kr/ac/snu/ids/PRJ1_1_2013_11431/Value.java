package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;

public class Value implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Type type;
  private int intValue;
  private String stringValue;
  
  public Value(Type type) { this.type = type; }
  public Value(Type type, int intValue) { this.type = type; this.intValue = intValue; }
  public Value(Type type, String stringValue) { this.type = type; this.stringValue = stringValue; }
  
  public Type getType() { return this.type; }
  public void setType(Type type) { this.type = type; }
  
  public boolean isNull() { return this.type.getType().equals(Type.NullType); }
  
  public int getIntValue() { return this.intValue; }
  public void setIntValue(int intValue) { this.intValue = intValue; } 
  public String getStringValue() { return this.stringValue; }
  public void setStringValue(String stringValue) { this.stringValue = stringValue; }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Value) {
      Value f = (Value) obj;
      if (f.getType().getType().equals(Type.IntType)) {
        return f.getIntValue() == this.intValue;
      }
      return f.getStringValue().equals(this.stringValue);
    }
    else {
      return false;
    }
  }

  @Override
  public String toString() {
    if (this.type.getType().equals(Type.IntType)) {
      return Integer.toString(this.intValue);
    }
    return this.stringValue;
  }
}