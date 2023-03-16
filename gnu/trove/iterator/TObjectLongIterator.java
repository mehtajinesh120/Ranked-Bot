package gnu.trove.iterator;

public interface TObjectLongIterator<K> extends TAdvancingIterator {
  K key();
  
  long value();
  
  long setValue(long paramLong);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\iterator\TObjectLongIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */