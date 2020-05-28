package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

// Class to save table schema.
public class Table implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  private boolean isReferenced;
  private boolean hasPrimaryKey;
  // LinkedHashMap has been used in order to keep the order of columns.
  private LinkedHashMap<String, Column> columns;
  
  public Table() {
    this.name = null;
    this.isReferenced = false;
    this.hasPrimaryKey = false;
    this.columns = new LinkedHashMap<String, Column>();
  }
  
  public String getName() { return this.name; }
  public void setName(String name) { this.name = name; }
  
  public boolean isReferenced() { return this.isReferenced; }
  public void setReferened() { this.isReferenced = true; }
  public void setDereferenced() { this.isReferenced = false; }
  // Check reference status after dropping tables.
  public boolean stillHasReferences() {
    for (Entry<String, Column> entry: columns.entrySet()) {
      Column c = entry.getValue();
      if (!c.getReferenced().isEmpty()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasColumn(String name) {
    return this.columns.containsKey(name);
  }
  
  public Column getColumn(String name) {
    if(this.columns.containsKey(name)) {
      return this.columns.get(name);
    }
    return null;
  }
  
  public LinkedHashMap<String, Column> getAllColumns() { return this.columns; }
  
  public void addColumn(Column c) { this.columns.put(c.getName(), c); }
  
  
  public boolean hasPrimaryKey() { return this.hasPrimaryKey; }
  
  public HashSet<String> getPrimaryKeys() {
    HashSet<String> primaryKeys = new HashSet<String>();
    for (Entry<String, Column> entry: columns.entrySet()) {
      if (entry.getValue().isPrimary()) {
        primaryKeys.add(entry.getKey());
      }
    }
    return primaryKeys;
  }
  
  public void addPrimaryKeys(ArrayList<String> colNames) {
    for (String name: colNames) {
      Column c = this.columns.get(name);
      c.setPrimary();
    }
    this.hasPrimaryKey = true;
  }
  
  
  public HashSet<Column> getForeignKeys() {
    HashSet<Column> foreignKeys = new HashSet<Column>();
    for (Entry<String, Column> entry: columns.entrySet()) {
      if (entry.getValue().isForeign()) {
        foreignKeys.add(entry.getValue());
      }
    }
    return foreignKeys;
  }
  
  public void addForeignKeys(ArrayList<String> colNames, ArrayList<String> refedColNames, String refedTableName) {
    Schema schema = Schema.getSchema();
    Table refedTable = schema.getTable(refedTableName);
    
    for (int i = 0; i < colNames.size(); i++) {
      Column refCol = this.columns.get(colNames.get(i));
      Column refedCol = refedTable.getColumn(refedColNames.get(i));
      refCol.setForeign(new ForeignKey(this.name, refCol.getName(), refedTableName, refedCol.getName()));
      refedCol.addReferenced(new ForeignKey(this.name, refCol.getName(), refedTableName, refedCol.getName()));
    }
    
    refedTable.setReferened();
    schema.addTable(refedTable);
  }
  
  public HashSet<ForeignKey> getReferenced() {
    HashSet<ForeignKey> referenced = new HashSet<ForeignKey>();
    for (Entry<String, Column> entry: this.columns.entrySet()) {
      Column c = entry.getValue();
      referenced.addAll(c.getReferenced());
    }
    return referenced;
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
