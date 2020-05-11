package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

public class Table implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  // The order of columns should be preserved. Therefore LinkedHashMap has been used.
  private LinkedHashMap<String, Column> columns;
  private HashSet<Column> primaryKeys;
  private HashSet<Column> foreignKeys;
  private HashSet<Column> referencedBy;
  
  public Table(String name) {
    this.name = name;
    this.columns = new LinkedHashMap<String, Column>();
    this.primaryKeys = new HashSet<Column>();
    this.foreignKeys = new HashSet<Column>();
    this.referencedBy = new HashSet<Column>();
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean columnExists(String name) {
    return this.columns.containsKey(name);
  }
  
  public boolean isReferenced() {
    return !this.referencedBy.isEmpty();
  }
  
  public void addColumn(Column c) {
    this.columns.put(c.getName(), c);
  }
  
  public Column getColumn(String name) {
    if(this.columnExists(name)) {
      return this.columns.get(name);
    }
    return null;
  }
  
  public HashSet<Column> getPrimaryKeys() {
    return this.primaryKeys;
  }
  
  public boolean primaryKeyExists(Column c) {
    return this.primaryKeys.contains(c);
  }
  
  public void addPrimaryKey(Column c) {
    c.setPrimary();
    this.primaryKeys.add(c);
  }
  
  @Override
  public String toString() {
    String lines = "----------------------------------------------------------------------";
    String res = lines + "\n";
    res += "table_name [" + this.name +"]\n";
    String format = "%-25.25s" + " " + "%-20.20s" + " " + "%-10.10s" + " " + "%-10.10s";
    res += String.format(format, "column_name", "type", "null", "key") + "\n";
    for (Entry<String, Column> entry: columns.entrySet()) {
      res += entry.getValue().toString() + "\n";
    }
    res += lines;
    return res;
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
