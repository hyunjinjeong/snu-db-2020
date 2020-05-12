package kr.ac.snu.ids.PRJ1_1_2013_11431;

public class Message {
  public static final int SYNTAX_ERROR = 0;
  public static final int CREATE_TABLE_SUCCESS = 1;
  public static final int DROP_SUCCESS = 2;
  public static final int SELECT = 4;
  public static final int INSERT = 5;
  public static final int DELETE = 6;
  public static final int SHOW_TABLES_NO_TABLE = 7;
  public static final int TABLE_EXISTENCE_ERROR = 8;
  public static final int NO_SUCH_TABLE = 9;
  public static final int DUPLICATE_COLUMN_DEF_ERROR = 10;
  public static final int DROP_REFERENCED_TABLE_ERROR = 11;
  public static final int CHAR_LENGTH_ERROR = 12;
  public static final int NON_EXISTING_COLUMN_ERROR = 13;
  public static final int DUPLICATE_PRIMARY_KEY_ERROR = 14;
  public static final int REFERENCE_TABLE_EXISTENCE_ERROR = 15;
  public static final int REFERENCE_COLUMN_EXISTENCE_ERROR = 16;
  public static final int REFERENCE_NON_PRIMARY_KEY_ERROR = 17;
  public static final int REFERENCE_TYPE_ERROR = 18;
  
  public static void print(String msg) {
    System.out.println(msg);
  }
  
  public static void print(int q) {
    print(getMessage(q));
  }

  public static void printPrompt()
  {
    System.out.print("DB_2013-11431> ");
  }
  
  public static String getMessage(int q)
  {
    switch(q)
    {
      case SYNTAX_ERROR:
        return "Syntax error";
      case SELECT:
        return "'SELECT' requested";
      case INSERT:
        return "'INSERT' requested";
      case DELETE:
        return "'DELETE' requested";
      case TABLE_EXISTENCE_ERROR:
        return "Create table has failed: table with the same name already exists";
      case NO_SUCH_TABLE:
        return "No such table";
      case SHOW_TABLES_NO_TABLE:
        return "There is no table";
      case DUPLICATE_COLUMN_DEF_ERROR:
        return "Create table has failed: column definition is duplicated";
      case CHAR_LENGTH_ERROR:
        return "Char length should be over 0";
      case DUPLICATE_PRIMARY_KEY_ERROR:
        return "Create table has failed: primary key definition is duplicated";
      case REFERENCE_TABLE_EXISTENCE_ERROR:
        return "Create table has failed: foreign key references non existing table";
      case REFERENCE_COLUMN_EXISTENCE_ERROR:
        return "Create table has failed: foreign key references non existing column";
      case REFERENCE_NON_PRIMARY_KEY_ERROR:
        return "Create table has failed: foreign key references non primary key column";
      case REFERENCE_TYPE_ERROR:
        return "Create table has failed: foreign key references wrong type";
      default:
        return "Undefined Message";
    }
  }
  
  // Messages that support a name variable.
  public static String getMessage(int q, String name) {
    switch(q)
    {
      case CREATE_TABLE_SUCCESS:
        return "[" + name + "] table is created";
      case DROP_SUCCESS:
        return "[" + name + "] table is dropped";
      case DROP_REFERENCED_TABLE_ERROR:
        return "Drop table has failed: [" + name + "] is referenced by other table";
      case NON_EXISTING_COLUMN_ERROR:
        return "Create table has failed: [" + name + "] does not exist in column definition";
      default:
        return "Undefined Message";
    }
  }
}
