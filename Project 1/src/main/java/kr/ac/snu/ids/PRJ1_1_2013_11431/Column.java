package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.HashSet;

// Class to save columns
public class Column implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  private Type type;
  private boolean isPrimary;
  private boolean isForeign;
  private boolean isNotNull;
  private ForeignKey referencing;
  private HashSet<ForeignKey> referenced;
  
  public Column() {
    this.name = null;
    this.type = null;
    this.isPrimary = false;
    this.isForeign = false;
    this.isNotNull = false;
    this.referencing = null;
    this.referenced = new HashSet<ForeignKey>();
  }
  
  public String getName() { return this.name; }
  
  public void setName(String name) { this.name = name; }
  
  
  public Type getType() { return this.type; }
  
  public void setType(Type type) { this.type = type; }
  
  
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
    this.referencing = new ForeignKey(key.getReferencingTableName(), key.getReferencingColName(),
        key.getReferencedTableName(), key.getReferencedColName());
    this.isForeign = true;
  }
  
  
  public HashSet<ForeignKey> getReferenced() { return this.referenced; }
  
  public void addReferenced(ForeignKey key) { this.referenced.add(key); }
  
  public void removeReferenced(ForeignKey key) { this.referenced.remove(key); }
  
  
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
}
