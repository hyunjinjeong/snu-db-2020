package kr.ac.snu.ids.PRJ1_1_2013_11431;

public class Message {
  public static final int SYNTAX_ERROR = 0;
  public static final int CREATE_TABLE_SUCCESS = 1;
  public static final int DROP_SUCCESS = 2;
  public static final int INSERT_RESULT = 5;
  public static final int DELETE_RESULT = 6;
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
  public static final int INSERT_TYPE_MISMATCH_ERROR = 19;
  public static final int INSERT_COLUMN_EXISTENCE_ERROR = 20;
  public static final int INSERT_COLUMN_NON_NULLABLE_ERROR = 21;
  public static final int INSERT_DUPLICATE_PRIMARY_KEY_ERROR = 22;
  public static final int INSERT_REFERENTIAL_INTEGRITY_ERROR = 23;
  public static final int WHERE_INCOMPARABLE_ERROR = 24;
  public static final int WHERE_TABLE_NOT_SPECIFIED = 25;
  public static final int WHERE_COLUMN_NOT_EXIST = 26;
  public static final int DELETE_REFERENTIAL_INTEGRITY_PASSED = 27;
  public static final int SELECT_TABLE_EXISTENCE_ERROR = 28;
  public static final int SELECT_DUPLICATE_TABLE_ALIAS_ERROR = 29;
  public static final int SELECT_COLUMN_RESOLVE_ERROR = 30;
  public static final int WHERE_COLUMN_AMBIGUOUS_REFERENCE = 31;
  
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
      case INSERT_RESULT:
        return "The row is inserted";
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
      case INSERT_TYPE_MISMATCH_ERROR:
        return "Insertion has failed: Types are not matched";
      case INSERT_DUPLICATE_PRIMARY_KEY_ERROR:
        return "Insertion has failed: Primary key duplication";
      case INSERT_REFERENTIAL_INTEGRITY_ERROR:
        return "Insertion has failed: Referential integrity violation";
      case WHERE_INCOMPARABLE_ERROR:
        return "Where clause try to compare incomparable values";
      case WHERE_TABLE_NOT_SPECIFIED:
        return "Where clause try to reference tables which are not specified";
      case WHERE_COLUMN_NOT_EXIST:
        return "Where clause try to reference non existing column";
      case WHERE_COLUMN_AMBIGUOUS_REFERENCE:
        return "Where clause contains ambiguous reference";
      default:
        return "Undefined Message";
    }
  }
  
  // Messages that support a name variable.
  public static String getMessage(int q, String name) {
    switch(q)
    {
      case CREATE_TABLE_SUCCESS:
        return "'" + name + "' table is created";
      case DROP_SUCCESS:
        return "'" + name + "' table is dropped";
      case DROP_REFERENCED_TABLE_ERROR:
        return "Drop table has failed: '" + name + "' is referenced by other table";
      case NON_EXISTING_COLUMN_ERROR:
        return "Create table has failed: '" + name + "' does not exist in column definition";
      case INSERT_COLUMN_EXISTENCE_ERROR:
        return "Insertion has failed: '" + name + "' does not exist";
      case INSERT_COLUMN_NON_NULLABLE_ERROR:
        return "Insertion has failed: '" + name + "' is not nullable";
      case SELECT_TABLE_EXISTENCE_ERROR:
        return "Selection has failed: '" + name + "' does not exist";
      case SELECT_DUPLICATE_TABLE_ALIAS_ERROR:
        return "Selection has failed: Not unique table/alias '" + name + "'";
      case SELECT_COLUMN_RESOLVE_ERROR:
        return "Selection has failed: fail to resolve '" + name + "'";
      default:
        return "Undefined Message";
    }
  }
  
  // Messages supporting an integer argument.
  public static String getMessage(int q, int number) {
    switch(q)
    {
      case DELETE_RESULT:
        return Integer.toString(number) + " row(s) are deleted";
      case DELETE_REFERENTIAL_INTEGRITY_PASSED:
        return Integer.toString(number) + " row(s) are not deleted due to referential integrity";
      default:
        return "Undefined MEssage";
    }
  }
}
