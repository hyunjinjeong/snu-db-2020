package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.TreeSet;

public class Column implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final String IntType = "int";
  public static final String CharType = "char";
  public static final String DateType = "date";
  
  public static String typeToAssign(String type) { return type; }
  public static String typeToAssign(String type, int length) {
    return type + "(" + Integer.toString(length) + ")";
  }
  
  private String name;
  private String type;
  private boolean isPrimary;
  private boolean isForeign;
  private boolean isNotNull;
  private referencing;
  private HashSet<ForeignKey> referenced;
  
  public Column(String name) {
    this.name = name;
    this.type = null;
    this.isPrimary = false;
    this.isForeign = false;
    this.isNotNull = false;
    this.referencing = null;
    this.referenced = new HashSet<ForeignKey>();
  }
  
  public String getName() { return this.name; }
  
  public String getType() { return this.type; }
  public void setType(String type) { this.type = type; }
  
  public boolean isNotNull() { return this.isNotNull; }
  public void setNotNull() { this.isNotNull = true; }
  
  public boolean isPrimary() { return this.isPrimary; }
  public void setPrimary() {
    this.setNotNull();
    this.isPrimary = true;
  }
  
  public boolean isForeign() { return this.isForeign; }
  public ForeignKey getReferencing() { return this.referencing; }
  public void setForeign(ForeignKey key) {
    this.referencing = new ForeignKey(key);
    this.isForeign = true;
  }
  
  public HashSet<ForeignKey> getReferenced() {
    return this.referenced;
  }
  
  @Override
  public String toString() {
    String nullable = "Y";
    String constraints = "";
    
    if (this.isNotNull) {
      nullable = "N";
    }
    
    if (this.isPrimary && this.isForeign) {
      constraints = "PRI/FOR";
    }
    else if (this.isForeign) {
      constraints = "FOR";
    }
    else if (this.isPrimary){
      constraints = "PRI";
    }
    
    String format = "%-25.25s" + " " + "%-20.20s" + " " + "%-10.10s" + " " + "%-10.10s";
    return String.format(format, this.name, this.type, nullable, constraints);
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Column) {
      Column c = (Column) obj;
      return c.getName().equals(this.name) && c.getType().equals(this.type);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return this.name.hashCode() + this.type.hashCode();
  }
}
