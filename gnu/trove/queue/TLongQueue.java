package gnu.trove.queue;

import gnu.trove.TLongCollection;

public interface TLongQueue extends TLongCollection {
  long element();
  
  boolean offer(long paramLong);
  
  long peek();
  
  long poll();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\queue\TLongQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */