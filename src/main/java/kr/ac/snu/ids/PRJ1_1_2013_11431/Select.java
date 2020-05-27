package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.util.ArrayList;
import java.util.Map.Entry;

// Util class for select and where.
public class Select {
  private ArrayList<Pair<String, String>> fromTables = new ArrayList<Pair<String, String>>();
  private ArrayList<Pair<String, String>> selectedColumns = new ArrayList<Pair<String, String>>();
  
  public ArrayList<Pair<String, String>> getFromTables() {
    return this.fromTables;
  }
  
  public void addFromTables(String tName, String alias) {
    this.fromTables.add(new Pair<String, String>(tName, alias));
  }
  
  public ArrayList<Pair<String, String>> getSelectedColumns() {
    return this.selectedColumns;
  }
  
  public void addSelectedColumns(String colName, String alias) {
    this.selectedColumns.add(new Pair<String, String>(colName, alias));
  }
  
  public ArrayList<Integer> getIndices(String tName) {
    ArrayList<Integer> indices = new ArrayList<>();

    for (int i = 0; i < fromTables.size(); i++) {
      Pair<String, String> nameAliasPair = fromTables.get(i);
      if (nameAliasPair.second().equals(tName) || nameAliasPair.first().equals(tName)) {
        indices.add(i);
      }
    }

    return indices;
  }
  
  public ArrayList<ColumnInTable> getAllColumns() {
    Schema schema = Schema.getSchema();
    ArrayList<ColumnInTable> columns = new ArrayList<ColumnInTable>();
    
    for (int i = 0; i < fromTables.size(); i++) {
      Table t = schema.getTable(fromTables.get(i).first());
      for (Entry<String, Column> entry: t.getAllColumns().entrySet()) {
        columns.add(new ColumnInTable(entry.getValue(), i));
      }
    }
    
    return columns;
  }
  
  public ColumnInTable getColumn(String colName) throws ParseException {
    String[] cit = colName.split("\\.");
    String tName = null;
    String columnName;
    ColumnInTable res = null;
    
    if (cit.length == 1) {
      columnName = cit[0];
    }
    else {
      tName = cit[0];
      columnName = cit[1];
    }
    
    if (tName == null) {
      res = getColumn(null, columnName);
    }
    else {
      ArrayList<Integer> indices = getIndices(cit[0]);
      if (indices.isEmpty()) {
        throw new ParseException(Message.getMessage(Message.WHERE_TABLE_NOT_SPECIFIED));
      }
      res = getColumn(indices, cit[1]);
    }
    
    if (res == null) {
      throw new ParseException(Message.getMessage(Message.WHERE_COLUMN_NOT_EXIST));
    }
    
    return res;
  }
  
  public ColumnInTable getColumn(ArrayList<Integer> indices, String colName) throws ParseException {
    Schema schema = Schema.getSchema();
    ColumnInTable cit = null;
    Column c = null;
    
    if (indices == null) {
      indices = new ArrayList<Integer>();
      for (int i = 0; i < fromTables.size(); i++) {
        indices.add(i);
      }
    }
    
    for (int index: indices) {
      Table t = schema.getTable(fromTables.get(index).first());
      if (t == null) {
        continue;
      }
      
      c = t.getColumn(colName);
      
      if (c != null) {
        if (cit != null) {
          throw new ParseException(Message.getMessage(Message.WHERE_COLUMN_AMBIGUOUS_REFERENCE));
        }
        cit = new ColumnInTable(c, index);
      }
    }
    
    return cit;
  }
}
