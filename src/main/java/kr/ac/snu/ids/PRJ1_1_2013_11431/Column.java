package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashSet;

public class Column implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final String IntType = "int";
  public static final String CharType = "char";
  public static final String DateType = "date";
  
  private String name;
  private String type;
  private boolean isPrimary;
  private boolean isForeign;
  private boolean isNotNull;
  private String tableName;
  private HashSet<Column> referencingColumns;
  
  public Column(String name, String tableName) {
    this.name = name;
    this.type = null;
    this.isPrimary = false;
    this.isForeign = false;
    this.isNotNull = false;
    this.tableName = tableName;
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
  
  // Int, Date
  public static String typeToAssign(String type) {
    return type;
  }
  
  // Char
  public static String typeToAssign(String type, int length) {
    return type + "(" + Integer.toString(length) + ")";
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
  public String toString() {
    String nullable = "Y";
    String constraints = "";
    
    if (this.isNotNull) {
      nullable = "N";
    }
    
    if (this.isPrimary && this.isForeign) {
      constraints = "PRI/FOR";
    }
    else if (this.isForeign) {
      constraints = "FOR";
    }
    else if (this.isPrimary){
      constraints = "PRI";
    }
    
    String format = "%-25.25s" + " " + "%-20.20s" + " " + "%-10.10s" + " " + "%-10.10s";
    return String.format(format, this.name, this.type, nullable, constraints);
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Column) {
      Column c = (Column) obj;
      return c.getName().equals(this.name) && c.getTableName().equals(this.tableName);
    }
    else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {  
    return this.name.hashCode() + this.tableName.hashCode();
  }
}
