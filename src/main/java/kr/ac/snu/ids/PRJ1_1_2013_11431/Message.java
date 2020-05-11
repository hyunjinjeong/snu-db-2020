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

  public static void printPrompt()
  {
    System.out.print("DB_2013-11431> ");
  }
  
  public static void printMessage(int q)
  {
    switch(q)
    {
      case PRINT_SYNTAX_ERROR:
        System.out.println("Syntax error");
        break;
      case PRINT_CREATE_TABLE:
        System.out.println("'CREATE TABLE' requested");
        break;
      case PRINT_DROP_TABLE:
        System.out.println("'DROP TABLE' requested");
        break;
      case PRINT_DESC:
        System.out.println("'DESC' requested");
        break;
      case PRINT_SELECT:
        System.out.println("'SELECT' requested");
        break;
      case PRINT_INSERT:
        System.out.println("'INSERT' requested");
        break;
      case PRINT_DELETE:
        System.out.println("'DELETE' requested");
        break;
      case PRINT_SHOW_TABLES:
        System.out.println("'SHOW TABLES' requested");
        break;
    }
  }
}
