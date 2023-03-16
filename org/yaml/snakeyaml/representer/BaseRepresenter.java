/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public abstract class BaseRepresenter
/*     */ {
/*  39 */   protected final Map<Class<?>, Represent> representers = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected Represent nullRepresenter;
/*     */ 
/*     */   
/*  46 */   protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap<>();
/*     */   
/*  48 */   protected DumperOptions.ScalarStyle defaultScalarStyle = null;
/*  49 */   protected DumperOptions.FlowStyle defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
/*  50 */   protected final Map<Object, Node> representedObjects = new IdentityHashMap<Object, Node>() {
/*     */       private static final long serialVersionUID = -5576159264232131854L;
/*     */       
/*     */       public Node put(Object key, Node value) {
/*  54 */         return (Node)super.put(key, new AnchorNode(value));
/*     */       }
/*     */     };
/*     */   
/*     */   protected Object objectToRepresent;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils = false;
/*     */   
/*     */   public Node represent(Object data) {
/*  63 */     Node node = representData(data);
/*  64 */     this.representedObjects.clear();
/*  65 */     this.objectToRepresent = null;
/*  66 */     return node;
/*     */   }
/*     */   protected final Node representData(Object data) {
/*     */     Node node;
/*  70 */     this.objectToRepresent = data;
/*     */     
/*  72 */     if (this.representedObjects.containsKey(this.objectToRepresent)) {
/*  73 */       node = this.representedObjects.get(this.objectToRepresent);
/*  74 */       return node;
/*     */     } 
/*     */ 
/*     */     
/*  78 */     if (data == null) {
/*  79 */       node = this.nullRepresenter.representData(null);
/*  80 */       return node;
/*     */     } 
/*     */ 
/*     */     
/*  84 */     Class<?> clazz = data.getClass();
/*  85 */     if (this.representers.containsKey(clazz)) {
/*  86 */       Represent representer = this.representers.get(clazz);
/*  87 */       node = representer.representData(data);
/*     */     } else {
/*     */       
/*  90 */       for (Class<?> repr : this.multiRepresenters.keySet()) {
/*  91 */         if (repr != null && repr.isInstance(data)) {
/*  92 */           Represent representer = this.multiRepresenters.get(repr);
/*  93 */           node = representer.representData(data);
/*  94 */           return node;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  99 */       if (this.multiRepresenters.containsKey(null)) {
/* 100 */         Represent representer = this.multiRepresenters.get(null);
/* 101 */         node = representer.representData(data);
/*     */       } else {
/* 103 */         Represent representer = this.representers.get(null);
/* 104 */         node = representer.representData(data);
/*     */       } 
/*     */     } 
/* 107 */     return node;
/*     */   }
/*     */   
/*     */   protected Node representScalar(Tag tag, String value, DumperOptions.ScalarStyle style) {
/* 111 */     if (style == null) {
/* 112 */       style = this.defaultScalarStyle;
/*     */     }
/* 114 */     return (Node)new ScalarNode(tag, value, null, null, style);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Node representScalar(Tag tag, String value) {
/* 119 */     return representScalar(tag, value, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Node representSequence(Tag tag, Iterable<?> sequence, DumperOptions.FlowStyle flowStyle) {
/* 124 */     int size = 10;
/* 125 */     if (sequence instanceof List) {
/* 126 */       size = ((List)sequence).size();
/*     */     }
/* 128 */     List<Node> value = new ArrayList<>(size);
/* 129 */     SequenceNode node = new SequenceNode(tag, value, flowStyle);
/* 130 */     this.representedObjects.put(this.objectToRepresent, node);
/* 131 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 132 */     for (Object item : sequence) {
/* 133 */       Node nodeItem = representData(item);
/* 134 */       if (!(nodeItem instanceof ScalarNode) || !((ScalarNode)nodeItem).isPlain()) {
/* 135 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 137 */       value.add(nodeItem);
/*     */     } 
/* 139 */     if (flowStyle == DumperOptions.FlowStyle.AUTO) {
/* 140 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 141 */         node.setFlowStyle(this.defaultFlowStyle);
/*     */       } else {
/* 143 */         node.setFlowStyle(bestStyle);
/*     */       } 
/*     */     }
/* 146 */     return (Node)node;
/*     */   }
/*     */   
/*     */   protected Node representMapping(Tag tag, Map<?, ?> mapping, DumperOptions.FlowStyle flowStyle) {
/* 150 */     List<NodeTuple> value = new ArrayList<>(mapping.size());
/* 151 */     MappingNode node = new MappingNode(tag, value, flowStyle);
/* 152 */     this.representedObjects.put(this.objectToRepresent, node);
/* 153 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 154 */     for (Map.Entry<?, ?> entry : mapping.entrySet()) {
/* 155 */       Node nodeKey = representData(entry.getKey());
/* 156 */       Node nodeValue = representData(entry.getValue());
/* 157 */       if (!(nodeKey instanceof ScalarNode) || !((ScalarNode)nodeKey).isPlain()) {
/* 158 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 160 */       if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
/* 161 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 163 */       value.add(new NodeTuple(nodeKey, nodeValue));
/*     */     } 
/* 165 */     if (flowStyle == DumperOptions.FlowStyle.AUTO) {
/* 166 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 167 */         node.setFlowStyle(this.defaultFlowStyle);
/*     */       } else {
/* 169 */         node.setFlowStyle(bestStyle);
/*     */       } 
/*     */     }
/* 172 */     return (Node)node;
/*     */   }
/*     */   
/*     */   public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
/* 176 */     this.defaultScalarStyle = defaultStyle;
/*     */   }
/*     */   
/*     */   public DumperOptions.ScalarStyle getDefaultScalarStyle() {
/* 180 */     if (this.defaultScalarStyle == null) {
/* 181 */       return DumperOptions.ScalarStyle.PLAIN;
/*     */     }
/* 183 */     return this.defaultScalarStyle;
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
/* 187 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public DumperOptions.FlowStyle getDefaultFlowStyle() {
/* 191 */     return this.defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 195 */     this.propertyUtils = propertyUtils;
/* 196 */     this.explicitPropertyUtils = true;
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 200 */     if (this.propertyUtils == null) {
/* 201 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 203 */     return this.propertyUtils;
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 207 */     return this.explicitPropertyUtils;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\representer\BaseRepresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */