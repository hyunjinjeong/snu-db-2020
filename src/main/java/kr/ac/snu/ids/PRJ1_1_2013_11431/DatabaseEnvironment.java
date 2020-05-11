package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.io.UnsupportedEncodingException;
import java.io.File;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class DatabaseEnvironment {
  private Environment databaseEnvironment;
  private Database db;
  
  public DatabaseEnvironment() {
    // Initialize to null
    databaseEnvironment = null;
    db = null;
  }
  
  public void setup(File envHome) {
    // Setup the environment and the database.
    EnvironmentConfig envConfig = new EnvironmentConfig();
    envConfig.setAllowCreate(true);
    databaseEnvironment = new Environment(envHome, envConfig);
    
    DatabaseConfig dbConfig = new DatabaseConfig();
    dbConfig.setAllowCreate(true);
    dbConfig.setSortedDuplicates(true);
    db = databaseEnvironment.openDatabase(null, "sampleDatabase", dbConfig);
  }
  
  public Environment getEnvironment() {
    return this.databaseEnvironment;
  }
  
  public Database getDatabase() {
    return this.db;
  }
  
  public void close() {
    if (db != null) db.close();
    if (databaseEnvironment != null) databaseEnvironment.close();
  }
  
  public void todo() {
    // Save it for later use.
    //
    //    Cursor cursor = null;
    //    DatabaseEntry key;
    //    DatabaseEntry value;
    //
    //    try {
    //      cursor = db.openCursor(null, null);
    //      key = new DatabaseEntry("key".getBytes("UTF-8"));
    //      value = new DatabaseEntry("value".getBytes("UTF-8"));
    //    }
    //    catch (DatabaseException de) {
    //    }
    //    catch (UnsupportedEncodingException e) {
    //      e.printStackTrace();
    //    }
    //    finally {
    //      cursor.close();
    //    }
    //
    //    // Find keys & values.
    //    DatabaseEntry foundKey;
    //    DatabaseEntry foundValue;
    //    try {
    //      cursor = db.openCursor(null, null);
    //      foundKey = new DatabaseEntry("key".getBytes("UTF-8"));
    //      foundValue = new DatabaseEntry("value".getBytes("UTF-8"));
    //      cursor.getFirst(foundKey, foundValue, LockMode.DEFAULT);
    //      do {
    //        String keyString = new String(foundKey.getData(), "UTF-8");
    //        String valueString = new String(foundValue.getData(), "UTF-8");
    //        Message.print(keyString);
    //      } while (cursor.getNext(foundKey, foundValue, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    //    }
    //    catch (DatabaseException de) {
    //    }
    //    catch (UnsupportedEncodingException e) {
    //      e.printStackTrace();
    //    }
    //    finally {
    //      cursor.close();
    //    }
  }
}
