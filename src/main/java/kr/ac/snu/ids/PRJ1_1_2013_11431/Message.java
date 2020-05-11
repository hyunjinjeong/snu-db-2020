package kr.ac.snu.ids.PRJ1_1_2013_11431;

public class Message {
  public static final int PRINT_SYNTAX_ERROR = 0;
  public static final int PRINT_CREATE_TABLE = 1;
  public static final int PRINT_DROP_TABLE = 2;
  public static final int PRINT_DESC = 3;
  public static final int PRINT_SELECT = 4;
  public static final int PRINT_INSERT = 5;
  public static final int PRINT_DELETE = 6;
  public static final int PRINT_SHOW_TABLES = 7;
  
  public static void print(String msg) {
    System.out.println(msg);
  }
  
  public static void print(int q) {
    printMessage(q);
  }

  public static void printPrompt()
  {
    System.out.print("DB_2013-11431> ");
  }
  
  private static void printMessage(int q)
  {
    switch(q)
    {
      case PRINT_SYNTAX_ERROR:
        print("Syntax error");
        break;
      case PRINT_CREATE_TABLE:
        print("'CREATE TABLE' requested");
        break;
      case PRINT_DROP_TABLE:
        print("'DROP TABLE' requested");
        break;
      case PRINT_DESC:
        print("'DESC' requested");
        break;
      case PRINT_SELECT:
        print("'SELECT' requested");
        break;
      case PRINT_INSERT:
        print("'INSERT' requested");
        break;
      case PRINT_DELETE:
        print("'DELETE' requested");
        break;
      case PRINT_SHOW_TABLES:
        print("'SHOW TABLES' requested");
        break;
    }
  }
}
