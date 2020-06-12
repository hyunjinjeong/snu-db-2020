package kr.ac.snu.ids.PRJ1_1_2013_11431;

// Class to evaluate Three Valued Logic.
public class ThreeValuedLogic {
  public static final int TRUE = 0;
  public static final int FALSE = 1;
  public static final int UNKNOWN = 2;
  
  public static int or(int e1, int e2) {
    /* 
     * UNKNOWN or TRUE = TRUE
     * UNKNOWN or FALSE = UNKNOWN
     * UNKNOWN or UNKNOWN = UNKNOWN
     */
    if (e1 == TRUE || e2 == TRUE) {
      return TRUE;
    }
    if (e1 == UNKNOWN || e2 == UNKNOWN) {
      return UNKNOWN;
    }
    return FALSE;
  }
  
  public static int and(int e1, int e2) {
    /*
     * TRUE and UNKNOWN = UNKNOWN
     * FALSE and UNKNOWN = FALSE
     * UNKNOWN and UNKNOWN = UNKNOWN
     */
    if (e1 == FALSE || e2 == FALSE) {
      return FALSE;
    }
    if (e1 == UNKNOWN || e2 == UNKNOWN) {
      return UNKNOWN;
    }
    return TRUE;
  }
  
  public static int not(int e) {
    /*
     * NOT UNKNOWN = NOT
     */
    if (e == TRUE) return FALSE;
    if (e == FALSE) return TRUE;
    return UNKNOWN;
  }
  
  public static boolean eval(int e) {
    if (e == TRUE) return true;
    return false;
  }
}
