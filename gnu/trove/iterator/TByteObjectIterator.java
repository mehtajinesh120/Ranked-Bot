package gnu.trove.iterator;

public interface TByteObjectIterator<V> extends TAdvancingIterator {
  byte key();
  
  V value();
  
  V setValue(V paramV);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\iterator\TByteObjectIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */