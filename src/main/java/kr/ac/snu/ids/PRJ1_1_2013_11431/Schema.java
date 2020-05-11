package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

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
  // To preserve the order of tables, LinkedHashMap has been used.
  private LinkedHashMap<String, Table> tables;
  private Environment databaseEnvironment;
  private Database db;
  
  public Schema () {
    this.tables = new LinkedHashMap<String, Table>();
    setupDatabase();
    this.loadTables();
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
    return table;
  }

  // Add a table to the database.
  public void createTable(Table t) {
    this.addTableToDatabase(t);
    this.tables.put(t.getName(), t);
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
  public void dropTable(Table t) {
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
    String s[] = tables.keySet().toArray(new String[tables.keySet().size()]);
    return String.join("\n", s);
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
}
