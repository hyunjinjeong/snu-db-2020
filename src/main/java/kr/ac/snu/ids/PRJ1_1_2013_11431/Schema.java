package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class Schema {
  private static Schema schema;
  // LinkedHashMap has been used in order to preserve the order of tables.
  private LinkedHashMap<String, Table> tables;
  private Environment databaseEnvironment;
  private Database db;
  
  public static Schema getSchema() {
    if (schema == null) {
      schema = new Schema();
    }
    return schema;
  }
  
  // Use a singleton class
  private Schema () {
    this.tables = new LinkedHashMap<String, Table>();
    setupDatabase();
    loadTables();
  }
  
  // Setup the environment and the database.
  private void setupDatabase() {
    EnvironmentConfig envConfig = new EnvironmentConfig();
    envConfig.setAllowCreate(true);
    this.databaseEnvironment = new Environment(new File("db/"), envConfig);
    
    DatabaseConfig dbConfig = new DatabaseConfig();
    dbConfig.setAllowCreate(true);
    this.db = databaseEnvironment.openDatabase(null, "sampleDatabase", dbConfig);
  }
  
  public void closeDatabase() {
    if (db != null) {
      db.close();
    }
    if (databaseEnvironment != null) {
      databaseEnvironment.close();
    }
  }
  
  // Load tables from the database.
  private void loadTables() {
    Cursor cursor = null;
    
    try {
      cursor = db.openCursor(null, null);
      DatabaseEntry foundKey = new DatabaseEntry();
      DatabaseEntry foundData = new DatabaseEntry();
      
      while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
        Table table = deserializeTable(foundData.getData());
        this.tables.put(table.getName(), table);
      }
    }
    catch (Exception e) {
    }
    finally {
      cursor.close();
    }
  }
  
  public boolean isTableReferenced(String tableName) {
    Table t = this.getTable(tableName);
    return t.isReferenced();
  }

  // Create a table and add it to DB.
  public void createTable(Table t) throws ParseException {
    if (this.tables.containsKey(t.getName())) {
      throw new ParseException(Message.getMessage(Message.TABLE_EXISTENCE_ERROR));
    }
    this.addTable(t);
  }
  
  public void addTable(Table t) {
    this.tables.put(t.getName(), t);
    
    Cursor cursor = null;
    
    try {
      cursor = db.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry(t.getName().getBytes("UTF-8"));
      DatabaseEntry data = new DatabaseEntry(serializeTable(t));
      cursor.put(key, data);
    }
    catch (Exception e) {
    }
    finally {
      cursor.close();
    }
  }
  
  // SHOW TABLES query.
  public String showTables() throws ParseException {
    if (this.tables.isEmpty()) {
      throw new ParseException(Message.getMessage(Message.SHOW_TABLES_NO_TABLE));
    }
    
    String lines = "----------------";
    String res = lines + "\n";
    String s[] = tables.keySet().toArray(new String[tables.keySet().size()]);
    res += String.join("\n", s) + "\n";
    res += lines;
    return res;
  }
  
  // DESC #t query.
  public String desc(String t) throws ParseException {
    if (!this.tables.containsKey(t)) {
      throw new ParseException(Message.getMessage(Message.NO_SUCH_TABLE));
    }
    
    return this.tables.get(t).toString();
  }
  
  // Drop a table and remove it from DB.
  public void dropTable(String tableName) throws ParseException {
    if (!this.tables.containsKey(tableName)) {
      throw new ParseException(Message.getMessage(Message.NO_SUCH_TABLE));
    }
    
    Table t = this.tables.get(tableName);
    
    if (t.isReferenced()) {
      throw new ParseException(Message.getMessage(Message.DROP_REFERENCED_TABLE_ERROR, tableName));
    }
    
    ArrayList<Table> refedTables = new ArrayList<Table>();
    
    for (Column foreignKey: t.getForeignKeys()) {
      ForeignKey fk = foreignKey.getReferencing();
      String refedTableName = fk.getTableName();
      String refedColName = fk.getColName();
      Table refedTable = this.getTable(refedTableName);
      Column refedColumn = refedTable.getColumn(refedColName);
      
      // Remove foreign key relations of referenced columns.
      refedColumn.removeReferenced(new ForeignKey(tableName, foreignKey.getName()));
      refedTables.add(refedTable);
    }
    
    for (Table refedTable: refedTables) {
      // Check if tables are still referenced by some columns. If not, make them dereferenced.
      if (!refedTable.stillHasReferences()) {
        refedTable.setDereferenced();
      }
      this.addTable(refedTable);
    }
        
    Cursor cursor = null;
    try {  
      cursor = db.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry(tableName.getBytes("UTF-8"));
      DatabaseEntry data = new DatabaseEntry();
      cursor.getSearchKey(key, data, LockMode.DEFAULT);
      
      if (cursor.count() > 0) {
        cursor.delete();
        this.tables.remove(tableName);
      }
    }
    catch (Exception e) {
    }
    finally {
      cursor.close();
    }
  }
  
  // insert into ... queries
  public void insertColumn() {
    
  }
  
  // delete from ... queries
  public void deleteColumn() {
    
  }
  
  // select from ... queries
  public void selectColumn() {
    
  }
  
  public String getType(String type) {
    if (type.equals(Column.IntType)) {
      return Column.IntType;
    }
    return Column.DateType;
  }
  
  public String getType(String type, int length) throws ParseException {
    if (length < 1) {
      throw new ParseException(Message.getMessage(Message.CHAR_LENGTH_ERROR));
    }
    return Column.typeToAssign(type, length);
  }
  
  public boolean isEmpty() {
    return tables.isEmpty();
  }
  
  public Table getTable(String name) {
    if (this.tables.containsKey(name)) {
      return tables.get(name);
    }
    return null;
  }
  
  public void addColumn(Table t, Column c) throws ParseException {
    if (t.getAllColumns().containsKey(c.getName())) {
      throw new ParseException(Message.getMessage(Message.DUPLICATE_COLUMN_DEF_ERROR));
    }
    t.addColumn(c);;
  }
  
  public void setType(Table t, String colName, String type) {
    t.getColumn(colName).setType(type);
  }
  
  public void setNotNull(Table t, String colName) {
    t.getColumn(colName).setNotNull();
  }
  
  public void addPrimaryKeys(Table t, ArrayList<String> colList) throws ParseException {
    if (t.hasPrimaryKey()) {
      throw new ParseException(Message.getMessage(Message.DUPLICATE_PRIMARY_KEY_ERROR));
    }
    
    HashSet<String> keysToBePrimary = new HashSet<String>();
    LinkedHashMap<String, Column> allColumns = t.getAllColumns();
    
    for (String name: colList) {
      if (!allColumns.containsKey(name)) {
        throw new ParseException(Message.getMessage(Message.NON_EXISTING_COLUMN_ERROR, name)); 
      }
      if (keysToBePrimary.contains(name)) {
        throw new ParseException(Message.getMessage(Message.DUPLICATE_PRIMARY_KEY_ERROR));
      }
      keysToBePrimary.add(name);
    }
    t.addPrimaryKeys(colList);
  }
  
  public void addForeignKeys(ArrayList<String> referencing, ArrayList<String> referenced, Table refTable, String refedTableName) throws ParseException {
    String invalidColName = usesNonExistingColumn(referencing, refTable);
    if (invalidColName != null) throw new ParseException(Message.getMessage(Message.NON_EXISTING_COLUMN_ERROR, invalidColName));
    if (!this.tables.containsKey(refedTableName)) throw new ParseException(Message.getMessage(Message.REFERENCE_TABLE_EXISTENCE_ERROR));
    
    Table refedTable = this.getTable(refedTableName);
    
    if (referencesNonExistingColumn(referenced, refedTable)) throw new ParseException(Message.getMessage(Message.REFERENCE_COLUMN_EXISTENCE_ERROR));
    if (isReferencingInvalidTypes(referencing, referenced, refTable, refedTable)) throw new ParseException(Message.getMessage(Message.REFERENCE_TYPE_ERROR));
    if (isReferencingNonPrimaryKeys(referenced, refedTable)) throw new ParseException(Message.getMessage(Message.REFERENCE_NON_PRIMARY_KEY_ERROR));
    
    refTable.addForeignKeys(referencing, referenced, refedTableName);
  }
  
  // Check if referencing columns do not exist.
  private String usesNonExistingColumn(ArrayList<String> referencing, Table refTable) {
    for (String colName: referencing) {
      if (!refTable.getAllColumns().containsKey(colName)) {
        return colName;  
      }
    }
    return null;
  }
  
  // Check if referenced columns do not exist.
  private boolean referencesNonExistingColumn(ArrayList<String> referenced, Table refedTable) {
    for (String refedColName: referenced) {
      if (!refedTable.getAllColumns().containsKey(refedColName)) {
        return true;
      }
    }
    return false;
  }
  
  // Check if: 1. Both arrays have the same number of elements, 2. Each element has the same type. 
  private boolean isReferencingInvalidTypes(ArrayList<String> referencing, ArrayList<String> referenced,
      Table refTable, Table refedTable) {
    if (referencing.size() != referenced.size()) {
      return true;
    }
    
    for (int i = 0; i < referencing.size(); i++) {
      String refColName = referencing.get(i);
      String refedColName = referenced.get(i);
      
      Column refCol = refTable.getColumn(refColName);
      Column refedCol = refedTable.getColumn(refedColName);
      
      if (!refCol.getType().equals(refedCol.getType())) {
        return true;
      }
    }
    return false;
  }
  
  // Check if there is non-primary keys among referenced columns.
  private boolean isReferencingNonPrimaryKeys(ArrayList<String> referencedList, Table t) {
    if (referencedList.size() != t.getPrimaryKeys().size()) {
      return true;
    }
    
    for (String colName: referencedList) {
      if (!t.getPrimaryKeys().contains(colName)) {
        return true;
      }
    }
    
    return false;
  }
  
  
  private static byte[] serializeTable(Table t) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(t);
    return baos.toByteArray();
  }
  
  private static Table deserializeTable(byte[] t) throws ClassNotFoundException, IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(t);
    ObjectInputStream ois = new ObjectInputStream(bais);
    Object objectMember = ois.readObject();
    Table table = (Table) objectMember;
    return table;
  }
}
