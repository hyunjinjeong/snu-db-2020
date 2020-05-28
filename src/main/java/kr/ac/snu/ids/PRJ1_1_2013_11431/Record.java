package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

// Class to save records.
public class Record implements Serializable {
  private static final long serialVersionUID = 1L;

  private String tableName;
  private HashMap<String, Value> value;
  // To support duplicate values, generate a random long number.
  private long _id;
  
  public Record(String tableName) {
    this.tableName = tableName;
    this.value = new HashMap<String, Value>();
    this._id = (long) (System.currentTimeMillis() + Math.random());
  }
  
  public String getTableName() { return this.tableName; }
  public Value getValue(String colName) { return this.value.get(colName); }
  
  public void addValue(String colName, Value v) { this.value.put(colName, v); }
  public void setNull(String colName) { this.value.get(colName).setType(new Type(Type.NullType)); }
  
  public long getId() { return this._id; }
  
  @Override
  public String toString() {
    String res = "[" + tableName + "] - ";
    for (Entry<String, Value> entry: value.entrySet()) {
      res += entry.getKey() + ": ";
      res += entry.getValue().toString() + ", ";
    }
    return res;
  }
}
