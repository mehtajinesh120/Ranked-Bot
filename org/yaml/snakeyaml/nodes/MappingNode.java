/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.util.List;
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
/*     */ public class MappingNode
/*     */   extends CollectionNode<NodeTuple>
/*     */ {
/*     */   private List<NodeTuple> value;
/*     */   private boolean merged = false;
/*     */   
/*     */   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
/*  33 */     super(tag, startMark, endMark, flowStyle);
/*  34 */     if (value == null) {
/*  35 */       throw new NullPointerException("value in a Node is required.");
/*     */     }
/*  37 */     this.value = value;
/*  38 */     this.resolved = resolved;
/*     */   }
/*     */   
/*     */   public MappingNode(Tag tag, List<NodeTuple> value, DumperOptions.FlowStyle flowStyle) {
/*  42 */     this(tag, true, value, (Mark)null, (Mark)null, flowStyle);
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
/*     */   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, Boolean flowStyle) {
/*  55 */     this(tag, resolved, value, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
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
/*     */   public MappingNode(Tag tag, List<NodeTuple> value, Boolean flowStyle) {
/*  67 */     this(tag, value, DumperOptions.FlowStyle.fromBoolean(flowStyle));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NodeId getNodeId() {
/*  73 */     return NodeId.mapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NodeTuple> getValue() {
/*  82 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(List<NodeTuple> mergedValue) {
/*  86 */     this.value = mergedValue;
/*     */   }
/*     */   
/*     */   public void setOnlyKeyType(Class<? extends Object> keyType) {
/*  90 */     for (NodeTuple nodes : this.value) {
/*  91 */       nodes.getKeyNode().setType(keyType);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTypes(Class<? extends Object> keyType, Class<? extends Object> valueType) {
/*  96 */     for (NodeTuple nodes : this.value) {
/*  97 */       nodes.getValueNode().setType(valueType);
/*  98 */       nodes.getKeyNode().setType(keyType);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     StringBuilder buf = new StringBuilder();
/* 106 */     for (NodeTuple node : getValue()) {
/* 107 */       buf.append("{ key=");
/* 108 */       buf.append(node.getKeyNode());
/* 109 */       buf.append("; value=");
/* 110 */       if (node.getValueNode() instanceof CollectionNode) {
/*     */         
/* 112 */         buf.append(System.identityHashCode(node.getValueNode()));
/*     */       } else {
/* 114 */         buf.append(node);
/*     */       } 
/* 116 */       buf.append(" }");
/*     */     } 
/* 118 */     String values = buf.toString();
/* 119 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMerged(boolean merged) {
/* 126 */     this.merged = merged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMerged() {
/* 133 */     return this.merged;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\nodes\MappingNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */