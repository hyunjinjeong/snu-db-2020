package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.HashSet;

public class Column implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  private String type;
  private boolean isPrimary;
  private boolean isForeign;
  private boolean isNotNull;
  private String tableName;
  private HashSet<Column> referencingColumns;
  
  public Column(String name) {
    this.name = name;
    this.type = null;
    this.isPrimary = false;
    this.isForeign = false;
    this.isNotNull = false;
    this.tableName = null;
    this.referencingColumns = new HashSet<Column>();
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getTableName() {
    return this.tableName;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public boolean isNotNull() {
    return this.isNotNull;
  }

  public void setNotNull() {
    this.isNotNull = true;
  }
  
  public boolean isPrimary() {
    return this.isPrimary;
  }
  
  public void setPrimary() {
    this.setNotNull();
    this.isPrimary = true;
  }
  
  public boolean isForeign() {
    return this.isForeign;
  }
  
  public void setForeign() {
    this.isForeign = true;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Column) {
      Column c = (Column) obj;
      return c.getTableName().equals(this.tableName) && c.getName().equals(this.name);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {  
    return this.tableName.hashCode() + this.name.hashCode();
  }
}
