package org.jsoup.select;

import org.jsoup.nodes.Node;

public interface NodeVisitor {
  void head(Node paramNode, int paramInt);
  
  default void tail(Node node, int depth) {}
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\NodeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */