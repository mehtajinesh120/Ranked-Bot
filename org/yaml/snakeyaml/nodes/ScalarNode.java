/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import org.yaml.snakeyaml.DumperOptions;
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
/*     */ public class ScalarNode
/*     */   extends Node
/*     */ {
/*     */   private final DumperOptions.ScalarStyle style;
/*     */   private final String value;
/*     */   
/*     */   public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
/*  32 */     this(tag, true, value, startMark, endMark, style);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
/*  37 */     super(tag, startMark, endMark);
/*  38 */     if (value == null) {
/*  39 */       throw new NullPointerException("value in a Node is required.");
/*     */     }
/*  41 */     this.value = value;
/*  42 */     if (style == null) {
/*  43 */       throw new NullPointerException("Scalar style must be provided.");
/*     */     }
/*  45 */     this.style = style;
/*  46 */     this.resolved = resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, Character style) {
/*  58 */     this(tag, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
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
/*     */   public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, Character style) {
/*  71 */     this(tag, resolved, value, startMark, endMark, DumperOptions.ScalarStyle.createStyle(style));
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
/*     */   public Character getStyle() {
/*  84 */     return this.style.getChar();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DumperOptions.ScalarStyle getScalarStyle() {
/*  95 */     return this.style;
/*     */   }
/*     */ 
/*     */   
/*     */   public NodeId getNodeId() {
/* 100 */     return NodeId.scalar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 109 */     return this.value;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 113 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
/*     */   }
/*     */   
/*     */   public boolean isPlain() {
/* 117 */     return (this.style == DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\nodes\ScalarNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */