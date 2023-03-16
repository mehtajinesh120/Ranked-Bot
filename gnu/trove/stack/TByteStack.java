package gnu.trove.stack;

public interface TByteStack {
  byte getNoEntryValue();
  
  void push(byte paramByte);
  
  byte pop();
  
  byte peek();
  
  int size();
  
  void clear();
  
  byte[] toArray();
  
  void toArray(byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\stack\TByteStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */