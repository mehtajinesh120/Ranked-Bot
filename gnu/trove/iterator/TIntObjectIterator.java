package gnu.trove.iterator;

public interface TIntObjectIterator<V> extends TAdvancingIterator {
  int key();
  
  V value();
  
  V setValue(V paramV);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\iterator\TIntObjectIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */