package gnu.trove.stack;

public interface TCharStack {
  char getNoEntryValue();
  
  void push(char paramChar);
  
  char pop();
  
  char peek();
  
  int size();
  
  void clear();
  
  char[] toArray();
  
  void toArray(char[] paramArrayOfchar);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\stack\TCharStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */