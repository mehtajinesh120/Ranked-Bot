package gnu.trove.stack;

public interface TIntStack {
  int getNoEntryValue();
  
  void push(int paramInt);
  
  int pop();
  
  int peek();
  
  int size();
  
  void clear();
  
  int[] toArray();
  
  void toArray(int[] paramArrayOfint);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\stack\TIntStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */