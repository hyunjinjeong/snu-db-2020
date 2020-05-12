package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class Table implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  // The order of columns should be preserved. Therefore LinkedHashMap has been used.
  private LinkedHashMap<String, Column> columns;
  private TreeSet<String> primaryKeys;
  // colName -> (tableName, colName)
  private TreeMap<String, Pair> foreignKeys;
  // (tableName, colName) -> colName
  private TreeMap<Pair, String> referencedBy;
  
  public Table(String name) {
    this.name = name;
    this.columns = new LinkedHashMap<String, Column>();
    this.primaryKeys = new TreeSet<String>();
    this.foreignKeys = new TreeMap<String, Pair>();
    this.referencedBy = new TreeMap<Pair, String>(); 
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
    this.columns.put(c.getName(), new Column(c));
  }
  
  public Column getColumn(String name) {
    if(this.columnExists(name)) {
      return this.columns.get(name);
    }
    return null;
  }
  
  public LinkedHashMap<String, Column> getAllColumns() {
    return this.columns;
  }
  
  public TreeSet<String> getPrimaryKeys() {
    return this.primaryKeys;
  }
  
  public boolean primaryKeyExists(String colName) {
    return this.primaryKeys.contains(colName);
  }
  
  public void addPrimaryKey(String colName) {
    this.getColumn(colName).setPrimary();
    this.primaryKeys.add(colName);
  }
  
  public TreeMap<String, Pair> getForeignKeys() {
    return this.foreignKeys;
  }
  
  public void addForeignKey(String refColName, String refedTableName, String refedColName) {
    this.foreignKeys.put(refColName, new Pair(refedTableName, refedColName));
  }
  
  public TreeMap<Pair, String> getReferencedBy() {
    return this.referencedBy;
  }
  
  public void addReferencedBy(String refTableName, String refColName, String refedColName) {
    this.referencedBy.put(new Pair(refTableName, refColName), refedColName);
  }
  
  public void removeReferencedBy(String refTableName, String refColName) {
    this.referencedBy.remove(new Pair(refTableName, refColName));
  }
  
  /*
   * Util Functions
   */
  
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
