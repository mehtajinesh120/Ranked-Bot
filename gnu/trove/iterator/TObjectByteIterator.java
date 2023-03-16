package gnu.trove.iterator;

public interface TObjectByteIterator<K> extends TAdvancingIterator {
  K key();
  
  byte value();
  
  byte setValue(byte paramByte);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\iterator\TObjectByteIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */