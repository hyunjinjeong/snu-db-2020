package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;

// Class to save foreign key relations.
public class ForeignKey implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String tableName;
  private String colName;
  
  public ForeignKey(String tableName, String colName) {
    this.tableName = tableName;
    this.colName = colName;
  }
  
  public String getTableName() { return this.tableName; }
  
  public String getColName() { return this.colName; }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof ForeignKey) {
      ForeignKey f = (ForeignKey) obj;
      return f.getTableName().equals(this.tableName) && f.getColName().equals(this.colName);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return this.tableName.hashCode() + this.colName.hashCode();
  }
}
