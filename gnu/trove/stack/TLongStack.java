package gnu.trove.stack;

public interface TLongStack {
  long getNoEntryValue();
  
  void push(long paramLong);
  
  long pop();
  
  long peek();
  
  int size();
  
  void clear();
  
  long[] toArray();
  
  void toArray(long[] paramArrayOflong);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\stack\TLongStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */