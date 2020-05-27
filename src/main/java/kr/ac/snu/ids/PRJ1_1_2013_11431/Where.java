package kr.ac.snu.ids.PRJ1_1_2013_11431;

import java.util.ArrayList;

public class Where {
  
  public static class BooleanValueExpression {
    private ArrayList<BooleanTerm> booleanTerms;
    
    public BooleanValueExpression() {
      this.booleanTerms = new ArrayList<BooleanTerm>();
    }
    
    public void addBooleanTerm(BooleanTerm booleanTerm) {
      this.booleanTerms.add(booleanTerm);
    }
    
    public int eval(Pair<Table, Record> pair) throws ParseException {
      // Default boolean for 'OR'
      int res = ThreeValuedLogic.FALSE;
      
      for (BooleanTerm booleanTerm: booleanTerms) {
        res = ThreeValuedLogic.or(res, booleanTerm.eval(pair));
      }
      
      return res;
    }
    
    @Override
    public String toString() {
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
    
    public int eval(Pair<Table, Record> pair) throws ParseException {
      // Default boolean for 'AND'
      int res = ThreeValuedLogic.TRUE;
      
      for (BooleanFactor booleanFactor: booleanFactors) {
        res = ThreeValuedLogic.and(res, booleanFactor.eval(pair));
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
    
    public int eval(Pair<Table, Record> pair) throws ParseException {
      int res = booleanTest.eval(pair);
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
    public int eval(Pair<Table, Record> pair) throws ParseException;
  }
  
  
  public static class ParenthesizedBooleanValueExpression implements BooleanTest {
    private BooleanValueExpression booleanValueExpression;
    
    public ParenthesizedBooleanValueExpression() {
      this.booleanValueExpression = null;
    }
    
    public void setBooleanValueExpression(BooleanValueExpression booleanValueExpression) {
      this.booleanValueExpression = booleanValueExpression;
    }
    
    public int eval(Pair<Table, Record> pair) throws ParseException {
      return this.booleanValueExpression.eval(pair);
    }
    
    @Override
    public String toString() {
      return "(" + booleanValueExpression.toString() + ")";
    }
  }
  
  
  public static interface Predicate extends BooleanTest {
    public int eval(Pair<Table, Record> pair) throws ParseException;
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
    
    public int eval(Pair<Table, Record> pair) throws ParseException {
      Value rightOperand;
      String tableName = pair.first().getName();
      Record record = pair.second(); 
          
      // Check the cases for the right hand side operand.
      if (right instanceof Value) {
        rightOperand = (Value) right;
      }
      else {
        ColumnInTable rightColumn = (ColumnInTable) right;
        
        if (rightColumn.getTableName() != null && !rightColumn.getTableName().equals(tableName)) {
          throw new ParseException(Message.getMessage(Message.WHERE_TABLE_NOT_SPECIFIED));
        }
        
        rightOperand = record.getValue(rightColumn.getColumnName());
        if (rightOperand == null) {
          throw new ParseException(Message.getMessage(Message.WHERE_COLUMN_NOT_EXIST));
        }
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
    
    public int eval(Pair<Table, Record> pair) throws ParseException {
      Record record = pair.second();
      String tableName = pair.first().getName();
      
      if (left.getTableName() != null && !left.getTableName().equals(tableName)) {
        throw new ParseException(Message.getMessage(Message.WHERE_TABLE_NOT_SPECIFIED));
      }
      
      Value leftOperand = record.getValue(left.getColumnName());
  
      if (leftOperand == null) {
        throw new ParseException(Message.getMessage(Message.WHERE_COLUMN_NOT_EXIST));
      }
      
      // On null operation cases.
      if (this.isNullOperation) {
        Value v = record.getValue(left.getColumnName());
        if (v.isNull()) {
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
        rightOperand = record.getValue(rightColumn.getColumnName());
        if (rightOperand == null) {
          throw new ParseException(Message.getMessage(Message.WHERE_TABLE_NOT_SPECIFIED));
        }
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
