package gnu.trove.queue;

import gnu.trove.TIntCollection;

public interface TIntQueue extends TIntCollection {
  int element();
  
  boolean offer(int paramInt);
  
  int peek();
  
  int poll();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\queue\TIntQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */