package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Table implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  // The order of columns should be preserved, so LinkedHashSet has been used.
  private LinkedHashSet<Column> columns;
  private HashSet<Column> primaryKeys;
  private HashSet<Column> foreignKeys;
  private HashSet<Column> referencedBy;
  
  public Table(String name) {
    this.name = name;
    this.columns = new LinkedHashSet<Column>();
    this.primaryKeys = new HashSet<Column>();
    this.foreignKeys = new HashSet<Column>();
    this.referencedBy = new HashSet<Column>();
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean isReferenced() {
    return !this.referencedBy.isEmpty();
  }
  
  public void addColumn(String columnName) {
    Column c = new Column(columnName);
    this.columns.add(c);
  }
  
  @Override
  public String toString() {
    return "[TABLE] " + this.name;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Table) {
      Table t = (Table) obj;
      return t.getName().equals(this.name);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {  
    return this.name.hashCode();
  }
}
