package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.util.ArrayList;

/*
 *  Class to save the structure of the WHERE clause.
 *  The structure is like below.
 *  NOTE: The Predicate structure was modified in Project 1-1. 
 *  
 *  BooleanValueExpression ::= BooleanTerm (or BooleanTerm)*
 *  BooleanTerm ::= BooleanFactor (and BooleanFactor)*
 *  BooleanFactor ::= [not] BooleanTest
 *  BooleanTest ::= Predicate | ParenthesizedBooleanExpression
 *  Predicate ::= ComparableValuePredicate | ColumnInTablePredicate
 *  ComparableValuePredicate ::= Value CompOp CompOperand
 *  ColumnInTablePredicate ::= ColumnInTable ((CompOp CompOperand) | NullOperation)
 *  ParenthesizedBooleanExpression ::= BooleanValueExpression
 *  
 */
public class Where {
  
  public static class BooleanValueExpression implements BooleanTest {
    private ArrayList<BooleanTerm> booleanTerms;
    
    public BooleanValueExpression() {
      this.booleanTerms = new ArrayList<BooleanTerm>();
    }
    
    public void addBooleanTerm(BooleanTerm booleanTerm) {
      this.booleanTerms.add(booleanTerm);
    }
    
    private boolean hasNoExpression() {
      return this.booleanTerms.isEmpty();
    }
    
    public int eval(ArrayList<Record> records) throws ParseException {
      // No expression equals TRUE.
      if (this.hasNoExpression()) {
        return ThreeValuedLogic.TRUE;
      }
      
      // Default boolean for 'OR'
      int res = ThreeValuedLogic.FALSE;
      
      for (BooleanTerm booleanTerm: booleanTerms) {
        res = ThreeValuedLogic.or(res, booleanTerm.eval(records));
      }
      
      return res;
    }
    
    @Override
    public String toString() {
      if (booleanTerms.isEmpty()) {
        return "()";
      }
      String res = "(" + booleanTerms.get(0).toString();
      for (int i = 1; i < booleanTerms.size(); i++) {
        res += " OR " + booleanTerms.get(i).toString();
      }
      res += ")";
      return res;
    }
  }
  
  
  public static class BooleanTerm {
    private ArrayList<BooleanFactor> booleanFactors;
    
    public BooleanTerm() {
      this.booleanFactors = new ArrayList<BooleanFactor>();
    }

    public void addBooleanFactor(BooleanFactor booleanFactor) {
      this.booleanFactors.add(booleanFactor);
    }
    
    public int eval(ArrayList<Record> records) throws ParseException {
      // Default boolean for 'AND'
      int res = ThreeValuedLogic.TRUE;
      
      for (BooleanFactor booleanFactor: booleanFactors) {
        res = ThreeValuedLogic.and(res, booleanFactor.eval(records));
      }
      
      return res;
    }
    
    @Override
    public String toString() {
      String res = "(" + booleanFactors.get(0).toString();
      for (int i = 1; i < booleanFactors.size(); i++) {
        res += " AND " + booleanFactors.get(i).toString();
      }
      res += ")";
      return res;
    }
  }
  
  
  public static class BooleanFactor {
    private BooleanTest booleanTest;
    private boolean isNot;
    
    public BooleanFactor() {
      booleanTest = null;
      isNot = false;
    }
    
    public void setBooleanTest(BooleanTest booleanTest) {
      this.booleanTest = booleanTest;
    }
    
    public void setIsNot() {
      this.isNot = true;
    }
    
    public int eval(ArrayList<Record> records) throws ParseException {
      int res = booleanTest.eval(records);
      return isNot ? ThreeValuedLogic.not(res) : res;
    }
    
    @Override
    public String toString() {
      if (isNot) {
        return "(NOT " + booleanTest.toString() + ")"; 
      }
      return booleanTest.toString();
    }
  }
  
  
  public static interface BooleanTest {
    public int eval(ArrayList<Record> records) throws ParseException;
  }
  
  
  public static interface Predicate extends BooleanTest {
    public int eval(ArrayList<Record> records) throws ParseException;
  }
  
  
  public static class ComparableValuePredicate implements Predicate {
    private Value left;
    private CompOperand right;
    private String op;
    
    public ComparableValuePredicate() {
      left = null;
      right = null;
      op = null;
    }
    
    public void setLeft(Value left) {
      this.left = left;
    }
    
    public void setRight(CompOperand right) {
      this.right = right;
    }
    
    public void setOp(String op) {
      this.op = op;
    }
    
    public int eval(ArrayList<Record> records) throws ParseException {
      Value rightOperand;
          
      // Check the cases for the right hand side operand.
      if (right instanceof Value) {
        rightOperand = (Value) right;
      }
      else {
        ColumnInTable rightColumn = (ColumnInTable) right;
        rightOperand = records.get(rightColumn.getIndex()).getValue(rightColumn.getColumn().getName());
      }
      
      if (!left.canCompare(rightOperand)) {
        throw new ParseException(Message.getMessage(Message.WHERE_INCOMPARABLE_ERROR));
      }
      
      // Comparison with NULL values leads to UNKNOWN.
      if (left.getType().isNull() || rightOperand.getType().isNull()) {
        return ThreeValuedLogic.UNKNOWN;
      }
      
      int comparisonResult = left.compare(rightOperand);
      boolean res = false;
      
      switch (op) {
        case ">=":
          res = comparisonResult >= 0;
          break;
        case "<=":
          res = comparisonResult <= 0;
          break;
        case "=":
          res = comparisonResult == 0;
          break;
        case "<":
          res = comparisonResult < 0;
          break;
        case ">":
          res = comparisonResult > 0;
          break;
        case "!=":
          res = comparisonResult != 0;
          break;
        default:
          res = false;
          break;
      }
      
      return res ? ThreeValuedLogic.TRUE : ThreeValuedLogic.FALSE; 
    }
    
    @Override
    public String toString() {
      Value rightOperand;
      
      if (right instanceof Value) {
        rightOperand = (Value) right;
        return "(" + left.toString() + " " + op + " " + rightOperand.toString() + ")";
      }
      else {
        ColumnInTable rightColumn = (ColumnInTable) right;
        return "(" + left.toString() + " " + op + " " + rightColumn.toString() + ")";
      }
    }
  }
  
  
  public static class ColumnInTablePredicate implements Predicate {
    private ColumnInTable left;
    private CompOperand right;
    private String op;
    private boolean isNot;
    private boolean isNullOperation;
    
    public ColumnInTablePredicate() {
      this.left = null;
      this.right = null;
      this.op = null;
      this.isNot = false;
      this.isNullOperation = false;
    }
    
    public void setLeft(ColumnInTable left) {
      this.left = left;
    }
    
    public void setRight(CompOperand right) {
      this.right = right;
    }
    
    public void setOp(String op) {
      this.op = op;
    }
    
    public void setNullOperation() {
      this.isNullOperation = true;
    }
    
    public void setIsNot() {
      this.isNot = true;
    }
    
    public int eval(ArrayList<Record> records) throws ParseException {
      Value leftOperand = records.get(left.getIndex()).getValue(left.getColumn().getName());
  
      // On null operation cases.
      if (this.isNullOperation) {
        if (leftOperand.isNull()) {
          return isNot? ThreeValuedLogic.FALSE : ThreeValuedLogic.TRUE;
        }
        return isNot? ThreeValuedLogic.TRUE : ThreeValuedLogic.FALSE;
      }
          
      Value rightOperand;
      
      // Check the cases for the right hand side operand.
      if (right instanceof Value) {
        rightOperand = (Value) right;
      }
      else {
        ColumnInTable rightColumn = (ColumnInTable) right; 
        rightOperand = records.get(rightColumn.getIndex()).getValue(rightColumn.getColumn().getName());
      }
      
      if (!leftOperand.canCompare(rightOperand)) {
        throw new ParseException(Message.getMessage(Message.WHERE_INCOMPARABLE_ERROR));
      }
      
      // Comparison with NULL values leads to UNKNOWN.
      if (leftOperand.getType().isNull() || rightOperand.getType().isNull()) {
        return ThreeValuedLogic.UNKNOWN;
      }
      
      int comparisonResult = leftOperand.compare(rightOperand);
      boolean res = false;
      
      switch (op) {
        case ">=":
          res = comparisonResult >= 0;
          break;
        case "<=":
          res = comparisonResult <= 0;
          break;
        case "=":
          res = comparisonResult == 0;
          break;
        case "<":
          res = comparisonResult < 0;
          break;
        case ">":
          res = comparisonResult > 0;
          break;
        case "!=":
          res = comparisonResult != 0;
          break;
        default:
          res = false;
          break;
      }
      
      return res ? ThreeValuedLogic.TRUE : ThreeValuedLogic.FALSE;
    }
    
    @Override
    public String toString() {
      if (this.isNullOperation) {
        return isNot ? "(" + left.toString() + " is not null)" : "(" + left.toString() + " is null)";
      }
      
      Value rightOperand;
      
      if (right instanceof Value) {
        rightOperand = (Value) right;
        return "(" + left.toString() + " " + op + " " + rightOperand.toString() + ")";
      }
      else {
        ColumnInTable rightColumn = (ColumnInTable) right;
        return "(" + left.toString() + " " + op + " " + rightColumn.toString() + ")";
      }
    }
  }
  
  
  public static interface CompOperand {}
}
