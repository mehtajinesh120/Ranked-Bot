package gnu.trove.iterator;

public interface TLongObjectIterator<V> extends TAdvancingIterator {
  long key();
  
  V value();
  
  V setValue(V paramV);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\iterator\TLongObjectIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */