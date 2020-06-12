package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;

// Class to save foreign key relations.
public class ForeignKey implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String refTableName;
  private String refColName;
  private String refedTableName;
  private String refedColName;
  
  public ForeignKey(String refTableName, String refColName, String refedTableName, String refedColName) {
    this.refTableName = refTableName;
    this.refColName = refColName;
    this.refedTableName = refedTableName;
    this.refedColName = refedColName;
  }
  
  public String getReferencingTableName() { return this.refTableName; }
  public String getReferencingColName() { return this.refColName; }
  
  public String getReferencedTableName() { return this.refedTableName; }
  public String getReferencedColName() { return this.refedColName; }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof ForeignKey) {
      ForeignKey f = (ForeignKey) obj;
      return f.getReferencingTableName().equals(this.refTableName) && f.getReferencingColName().equals(this.refColName)
          && f.getReferencedTableName().equals(this.refedTableName) && f.getReferencedColName().equals(this.refedColName);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return this.refTableName.hashCode() + this.refColName.hashCode() + this.refedTableName.hashCode() + this.refedColName.hashCode();
  }
  
  @Override
  public String toString() {
    return "Referencing: " + "(table: " + this.refTableName + ", column: " + this.refColName
        + "), Referenced: " + "(table: " + this.refedTableName + ", column: " + this.refedColName + ")";
  }
}
