package kr.ac.snu.ids.PRJ1_1_2013_11431;

public class Message {
  public static final int SYNTAX_ERROR = 0;
  public static final int CREATE_TABLE = 1;
  public static final int DROP_SUCCESS = 2;
  public static final int DESC = 3;
  public static final int SELECT = 4;
  public static final int INSERT = 5;
  public static final int DELETE = 6;
  public static final int SHOW_TABLES = 7;
  public static final int TABLE_EXISTENCE_ERROR = 8;
  public static final int NO_SUCH_TABLE = 9;
  public static final int SHOW_TABLES_NO_TABLE = 10;
  public static final int DROP_REFERENCED_TABLE_ERROR = 11;
  
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
      case CREATE_TABLE:
        return "'CREATE TABLE' requested";
      case DESC:
        return "'DESC' requested";
      case SELECT:
        return "'SELECT' requested";
      case INSERT:
        return "'INSERT' requested";
      case DELETE:
        return "'DELETE' requested";
      case SHOW_TABLES:
        return "'SHOW TABLES' requested";
      case TABLE_EXISTENCE_ERROR:
        return "Create table has failed: table with the same name already exists";
      case NO_SUCH_TABLE:
        return "No such table";
      case SHOW_TABLES_NO_TABLE:
        return "There is no table";
      default:
        return "Undefined Message";
    }
  }
  
  public static String getMessage(int q, String name) {
    switch(q)
    {
      case DROP_SUCCESS:
        return "'[" + name + "]' table is dropped";
      case DROP_REFERENCED_TABLE_ERROR:
        return "Drop table has failed: '[" + name + "'] is referenced by other table";
      default:
        return "Undefined Message";
    }
  }
}
