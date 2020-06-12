package kr.ac.snu.ids.PRJ1_1_2013_11431;

// Class representing ColumnInTable.
public class ColumnInTable implements Where.CompOperand {
  private Column column;
  private int index;
  
  public ColumnInTable(Column column, int index) {
    this.column = column;
    this.index = index;
  }
  
  public Column getColumn() {
    return this.column;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  @Override
  public String toString() {
    return this.index + "." + this.column.getName(); 
  }
}
