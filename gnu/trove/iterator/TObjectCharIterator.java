package gnu.trove.iterator;

public interface TObjectCharIterator<K> extends TAdvancingIterator {
  K key();
  
  char value();
  
  char setValue(char paramChar);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\iterator\TObjectCharIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */