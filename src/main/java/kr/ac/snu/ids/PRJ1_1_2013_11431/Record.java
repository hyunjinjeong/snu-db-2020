package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.HashMap;

public class Record implements Serializable {
  private static final long serialVersionUID = 1L;

  private String tableName;
  private HashMap<String, Value> value;
  
  public Record(String tableName) {
    this.tableName = tableName;
    this.value = new HashMap<String, Value>();
  }
  
  public String getTableName() { return this.tableName; }
  public Value getValue(String colName) { return this.value.get(colName); }
  
  public void addValue(String colName, Value v) { this.value.put(colName, v); }
}
