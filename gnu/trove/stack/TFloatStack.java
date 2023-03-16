package gnu.trove.stack;

public interface TFloatStack {
  float getNoEntryValue();
  
  void push(float paramFloat);
  
  float pop();
  
  float peek();
  
  int size();
  
  void clear();
  
  float[] toArray();
  
  void toArray(float[] paramArrayOffloat);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\stack\TFloatStack.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */