/*     */ package org.jsoup.select;
/*     */ 
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NodeTraversor
/*     */ {
/*     */   public static void traverse(NodeVisitor visitor, Node root) {
/*  21 */     Validate.notNull(visitor);
/*  22 */     Validate.notNull(root);
/*  23 */     Node node = root;
/*  24 */     int depth = 0;
/*     */     
/*  26 */     while (node != null) {
/*  27 */       Node parent = node.parentNode();
/*  28 */       int origSize = (parent != null) ? parent.childNodeSize() : 0;
/*  29 */       Node next = node.nextSibling();
/*     */       
/*  31 */       visitor.head(node, depth);
/*  32 */       if (parent != null && !node.hasParent()) {
/*  33 */         if (origSize == parent.childNodeSize()) {
/*  34 */           node = parent.childNode(node.siblingIndex());
/*     */         } else {
/*  36 */           node = next;
/*  37 */           if (node == null) {
/*  38 */             node = parent;
/*  39 */             depth--;
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */       }
/*  45 */       if (node.childNodeSize() > 0) {
/*  46 */         node = node.childNode(0);
/*  47 */         depth++; continue;
/*     */       } 
/*     */       while (true) {
/*  50 */         assert node != null;
/*  51 */         if (node.nextSibling() != null || depth <= 0)
/*  52 */           break;  visitor.tail(node, depth);
/*  53 */         node = node.parentNode();
/*  54 */         depth--;
/*     */       } 
/*  56 */       visitor.tail(node, depth);
/*  57 */       if (node == root)
/*     */         break; 
/*  59 */       node = node.nextSibling();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void traverse(NodeVisitor visitor, Elements elements) {
/*  70 */     Validate.notNull(visitor);
/*  71 */     Validate.notNull(elements);
/*  72 */     for (Element el : elements) {
/*  73 */       traverse(visitor, (Node)el);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NodeFilter.FilterResult filter(NodeFilter filter, Node root) {
/*  83 */     Node node = root;
/*  84 */     int depth = 0;
/*     */     
/*  86 */     while (node != null) {
/*  87 */       NodeFilter.FilterResult result = filter.head(node, depth);
/*  88 */       if (result == NodeFilter.FilterResult.STOP) {
/*  89 */         return result;
/*     */       }
/*  91 */       if (result == NodeFilter.FilterResult.CONTINUE && node.childNodeSize() > 0) {
/*  92 */         node = node.childNode(0);
/*  93 */         depth++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       while (true) {
/*  98 */         assert node != null;
/*  99 */         if (node.nextSibling() != null || depth <= 0)
/*     */           break; 
/* 101 */         if (result == NodeFilter.FilterResult.CONTINUE || result == NodeFilter.FilterResult.SKIP_CHILDREN) {
/* 102 */           result = filter.tail(node, depth);
/* 103 */           if (result == NodeFilter.FilterResult.STOP)
/* 104 */             return result; 
/*     */         } 
/* 106 */         Node node1 = node;
/* 107 */         node = node.parentNode();
/* 108 */         depth--;
/* 109 */         if (result == NodeFilter.FilterResult.REMOVE)
/* 110 */           node1.remove(); 
/* 111 */         result = NodeFilter.FilterResult.CONTINUE;
/*     */       } 
/*     */       
/* 114 */       if (result == NodeFilter.FilterResult.CONTINUE || result == NodeFilter.FilterResult.SKIP_CHILDREN) {
/* 115 */         result = filter.tail(node, depth);
/* 116 */         if (result == NodeFilter.FilterResult.STOP)
/* 117 */           return result; 
/*     */       } 
/* 119 */       if (node == root)
/* 120 */         return result; 
/* 121 */       Node prev = node;
/* 122 */       node = node.nextSibling();
/* 123 */       if (result == NodeFilter.FilterResult.REMOVE) {
/* 124 */         prev.remove();
/*     */       }
/*     */     } 
/* 127 */     return NodeFilter.FilterResult.CONTINUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void filter(NodeFilter filter, Elements elements) {
/* 136 */     Validate.notNull(filter);
/* 137 */     Validate.notNull(elements);
/* 138 */     for (Element el : elements) {
/* 139 */       if (filter(filter, (Node)el) == NodeFilter.FilterResult.STOP)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\NodeTraversor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */