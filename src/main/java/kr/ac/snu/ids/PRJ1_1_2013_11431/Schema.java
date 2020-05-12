package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class Schema {
  private static Schema schema;
  // To preserve the order of tables, LinkedHashMap has been used.
  private LinkedHashMap<String, Table> tables;
  private Environment databaseEnvironment;
  private Database db;
  
  public Schema () {
    this.tables = new LinkedHashMap<String, Table>();
    setupDatabase();
    loadTables();
  }
  
  public Schema getSchema() {
    if (schema == null) {
      schema = new Schema();
    }
    return schema;
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
    catch (DatabaseException de) {
      Message.print("Error accessing database." + de);
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      cursor.close();
    }
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
    for (Entry<Pair, String> entry: table.getReferencedBy().entrySet()) {
      Message.print(entry.getKey().toString());
    }
    return table;
  }

  // Create a table and add it to DB.
  public void createTable(Table t) {
    this.addTableToDatabase(t);
    this.tables.put(t.getName(), t);
  }
  
  public boolean isTableReferenced(String tableName) {
    Table t = this.getTable(tableName);
    return t.isReferenced();
  }
  
  private void addTableToDatabase(Table t) {
    Cursor cursor = null;
    
    try {
      cursor = db.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry(t.getName().getBytes("UTF-8"));
      DatabaseEntry data = new DatabaseEntry(serializeTable(t));
      cursor.put(key, data);
    }
    catch (DatabaseException de) {
      Message.print("Error accessing database." + de);
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      cursor.close();
    }
  }
  
  // Drop a table.
  public void dropTable(String tableName) {
    Table t = this.getTable(tableName);
    for (Entry<String, Pair> entry: t.getForeignKeys().entrySet()) {
      Table refedTable = this.getTable(entry.getValue().first());
      refedTable.removeReferencedBy(t.getName(), entry.getKey());
    }
    this.dropTableFromDatabase(t);
    this.tables.remove(t.getName());
  }
  
  private void dropTableFromDatabase(Table t) {
    Cursor cursor = null;
    
    try {
      cursor = db.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry(t.getName().getBytes("UTF-8"));
      DatabaseEntry data = new DatabaseEntry();
      cursor.getSearchKey(key, data, LockMode.DEFAULT);
      
      if (cursor.count() > 0) {
        cursor.delete();
      }
    }
    catch (DatabaseException de) {
      Message.print("Error accessing database." + de);
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    finally {
      cursor.close();
    }
  }
  
  public void closeDatabase() {
    if (db != null) {
      db.close();
    }
    if (databaseEnvironment != null) {
      databaseEnvironment.close();
    }
  }
  
  public boolean isEmpty() {
    return tables.isEmpty();
  }
  
  public String getAllTableNames() {
    String lines = "----------------";
    String res = lines + "\n";
    String s[] = tables.keySet().toArray(new String[tables.keySet().size()]);
    res += String.join("\n", s) + "\n";
    res += lines;
    return res;
  }
  
  public Table getTable(String name) {
    if (this.tableExists(name)) {
      return tables.get(name);
    }
    return null;
  }
  
  public boolean tableExists(String name) {
    return tables.containsKey(name);
  }
  
  public void addColumn(Table t, String colName) {
    t.addColumn(new Column(colName, t.getName()));
  }
  
  public void setType(Table t, String colName, String type) {
    t.getColumn(colName).setType(type);
  }
  
  public void setNotNull(Table t, String colName) {
    t.getColumn(colName).setNotNull();
  }
  
  public void addPrimary(Table t, String colName) {
    t.getColumn(colName).setPrimary();
    t.addPrimaryKey(colName);
  }
  
  public boolean isReferencingPrimaryKeys(ArrayList<String> referencedList, Table t) {
    if (referencedList.size() != t.getPrimaryKeys().size()) {
      return false;
    }
    
    for (String colName: referencedList) {
      if (!t.primaryKeyExists(colName)) {
        return false;
      }
    }
    
    return true;
  }
  
  public boolean isReferencingValidTypes(ArrayList<String> referencing, ArrayList<String> referenced,
      Table refTable, Table refedTable) {
    for (int i = 0; i < referencing.size(); i++) {
      String refColName = referencing.get(i);
      String refedColName = referenced.get(i);
      
      Column refCol = refTable.getColumn(refColName);
      Column refedCol = refedTable.getColumn(refedColName);
      
      if (!refCol.getType().equals(refedCol.getType())) {
        return false;
      }
    }
    return true;
  }

  public void addForeignKeys(ArrayList<String> referencing, ArrayList<String> referenced, Table refTable, Table refedTable) {
    for (int i = 0; i < referencing.size(); i++) {
      String refColName = referencing.get(i);
      String refedColName = referenced.get(i);
      refTable.getColumn(refColName).setForeign();
      refTable.addForeignKey(refColName, refedTable.getName(), refedColName);
      refedTable.addReferencedBy(refTable.getName(), refColName, refedColName);
    }    
  }
}
