package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;

// Class for types.
public class Type implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final String IntType = "int";
  public static final String CharType = "char";
  public static final String DateType = "date";
  public static final String NullType = "null";
  
  private String type;
  private int length;
  
  public Type(String type) {
    this.type = type;
    this.length = 0;
  }
  
  public Type(String type, int length) {
    this.type = type;
    this.length = length;
  }
  
  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }
  
  public int getLength() { return this.length; }
  public void setLength(int length) { this.length = length; }
  
  public boolean isNull() { return this.type.equals(NullType); }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Type) {
      Type f = (Type) obj;
      if (f.getType().equals(NullType)) {
        return true;
      }
      if (f.getType().equals(CharType)) {
        return f.getLength() == this.length;
      }
      return f.getType().equals(this.type);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {  
    return this.toString().hashCode();
  }
  
  @Override
  public String toString() {
    if (this.type.equals(CharType)) {
      return this.type + "(" + Integer.toString(length) + ")";
    }
    return this.type;
  }
}
