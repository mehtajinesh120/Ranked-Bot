package gnu.trove.stack;

public interface TDoubleStack {
  double getNoEntryValue();
  
  void push(double paramDouble);
  
  double pop();
  
  double peek();
  
  int size();
  
  void clear();
  
  double[] toArray();
  
  void toArray(double[] paramArrayOfdouble);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\stack\TDoubleStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */