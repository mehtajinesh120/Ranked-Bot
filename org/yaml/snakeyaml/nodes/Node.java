/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.yaml.snakeyaml.comments.CommentLine;
/*     */ import org.yaml.snakeyaml.error.Mark;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Node
/*     */ {
/*     */   private Tag tag;
/*     */   private final Mark startMark;
/*     */   protected Mark endMark;
/*     */   private Class<? extends Object> type;
/*     */   private boolean twoStepsConstruction;
/*     */   private String anchor;
/*     */   private List<CommentLine> inLineComments;
/*     */   private List<CommentLine> blockComments;
/*     */   private List<CommentLine> endComments;
/*     */   protected boolean resolved;
/*     */   protected Boolean useClassConstructor;
/*     */   
/*     */   public Node(Tag tag, Mark startMark, Mark endMark) {
/*  52 */     setTag(tag);
/*  53 */     this.startMark = startMark;
/*  54 */     this.endMark = endMark;
/*  55 */     this.type = Object.class;
/*  56 */     this.twoStepsConstruction = false;
/*  57 */     this.resolved = true;
/*  58 */     this.useClassConstructor = null;
/*  59 */     this.inLineComments = null;
/*  60 */     this.blockComments = null;
/*  61 */     this.endComments = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag getTag() {
/*  72 */     return this.tag;
/*     */   }
/*     */   
/*     */   public Mark getEndMark() {
/*  76 */     return this.endMark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract NodeId getNodeId();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mark getStartMark() {
/*  88 */     return this.startMark;
/*     */   }
/*     */   
/*     */   public void setTag(Tag tag) {
/*  92 */     if (tag == null) {
/*  93 */       throw new NullPointerException("tag in a Node is required.");
/*     */     }
/*  95 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 103 */     return super.equals(obj);
/*     */   }
/*     */   
/*     */   public Class<? extends Object> getType() {
/* 107 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Class<? extends Object> type) {
/* 111 */     if (!type.isAssignableFrom(this.type)) {
/* 112 */       this.type = type;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTwoStepsConstruction(boolean twoStepsConstruction) {
/* 117 */     this.twoStepsConstruction = twoStepsConstruction;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTwoStepsConstruction() {
/* 136 */     return this.twoStepsConstruction;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 141 */     return super.hashCode();
/*     */   }
/*     */   
/*     */   public boolean useClassConstructor() {
/* 145 */     if (this.useClassConstructor == null) {
/*     */ 
/*     */       
/* 148 */       if (!this.tag.isSecondary() && this.resolved && !Object.class.equals(this.type) && !this.tag.equals(Tag.NULL)) {
/* 149 */         return true;
/*     */       }
/* 151 */       return this.tag.isCompatible(getType());
/*     */     } 
/*     */     
/* 154 */     return this.useClassConstructor.booleanValue();
/*     */   }
/*     */   
/*     */   public void setUseClassConstructor(Boolean useClassConstructor) {
/* 158 */     this.useClassConstructor = useClassConstructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isResolved() {
/* 171 */     return this.resolved;
/*     */   }
/*     */   
/*     */   public String getAnchor() {
/* 175 */     return this.anchor;
/*     */   }
/*     */   
/*     */   public void setAnchor(String anchor) {
/* 179 */     this.anchor = anchor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CommentLine> getInLineComments() {
/* 190 */     return this.inLineComments;
/*     */   }
/*     */   
/*     */   public void setInLineComments(List<CommentLine> inLineComments) {
/* 194 */     this.inLineComments = inLineComments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CommentLine> getBlockComments() {
/* 203 */     return this.blockComments;
/*     */   }
/*     */   
/*     */   public void setBlockComments(List<CommentLine> blockComments) {
/* 207 */     this.blockComments = blockComments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CommentLine> getEndComments() {
/* 219 */     return this.endComments;
/*     */   }
/*     */   
/*     */   public void setEndComments(List<CommentLine> endComments) {
/* 223 */     this.endComments = endComments;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\nodes\Node.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */