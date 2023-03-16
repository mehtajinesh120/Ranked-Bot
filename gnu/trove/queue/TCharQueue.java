package gnu.trove.queue;

import gnu.trove.TCharCollection;

public interface TCharQueue extends TCharCollection {
  char element();
  
  boolean offer(char paramChar);
  
  char peek();
  
  char poll();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\queue\TCharQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */