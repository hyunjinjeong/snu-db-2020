package kr.ac.snu.ids.PRJ1_1_2013_11431;

public class ColumnInTable implements Where.CompOperand {
  private String tableName;
  private String columnName;
  
  public ColumnInTable() {
    this.tableName = null;
    this.columnName = null;
  }
  
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  
  public String getColumnName() {
    return this.columnName;
  }
  
  public String getTableName() {
    return this.tableName;
  }
  
  @Override
  public String toString() {
    return this.tableName != null ? this.tableName + "." + this.columnName : this.columnName; 
  }
}
