package gnu.trove.queue;

import gnu.trove.TByteCollection;

public interface TByteQueue extends TByteCollection {
  byte element();
  
  boolean offer(byte paramByte);
  
  byte peek();
  
  byte poll();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\queue\TByteQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */