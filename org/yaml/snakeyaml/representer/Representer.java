/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
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
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*  43 */   protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();
/*     */   
/*     */   public Representer() {
/*  46 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public Representer(DumperOptions options) {
/*  50 */     super(options);
/*  51 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public TypeDescription addTypeDescription(TypeDescription td) {
/*  55 */     if (Collections.EMPTY_MAP == this.typeDefinitions) {
/*  56 */       this.typeDefinitions = new HashMap<>();
/*     */     }
/*  58 */     if (td.getTag() != null) {
/*  59 */       addClassTag(td.getType(), td.getTag());
/*     */     }
/*  61 */     td.setPropertyUtils(getPropertyUtils());
/*  62 */     return this.typeDefinitions.put(td.getType(), td);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/*  67 */     super.setPropertyUtils(propertyUtils);
/*  68 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/*  69 */     for (TypeDescription typeDescription : tds)
/*  70 */       typeDescription.setPropertyUtils(propertyUtils); 
/*     */   }
/*     */   
/*     */   protected class RepresentJavaBean
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/*  77 */       return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
/*     */     }
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
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
/*  92 */     List<NodeTuple> value = new ArrayList<>(properties.size());
/*     */     
/*  94 */     Tag customTag = this.classTags.get(javaBean.getClass());
/*  95 */     Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
/*     */     
/*  97 */     MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
/*  98 */     this.representedObjects.put(javaBean, node);
/*  99 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 100 */     for (Property property : properties) {
/* 101 */       Object memberValue = property.get(javaBean);
/* 102 */       Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
/*     */       
/* 104 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/* 105 */       if (tuple == null) {
/*     */         continue;
/*     */       }
/* 108 */       if (!((ScalarNode)tuple.getKeyNode()).isPlain()) {
/* 109 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 111 */       Node nodeValue = tuple.getValueNode();
/* 112 */       if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
/* 113 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 115 */       value.add(tuple);
/*     */     } 
/* 117 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 118 */       node.setFlowStyle(this.defaultFlowStyle);
/*     */     } else {
/* 120 */       node.setFlowStyle(bestStyle);
/*     */     } 
/* 122 */     return node;
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
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
/* 136 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 138 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 140 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 142 */     if (propertyValue != null && !hasAlias) {
/* 143 */       NodeId nodeId = nodeValue.getNodeId();
/* 144 */       if (customTag == null) {
/* 145 */         if (nodeId == NodeId.scalar) {
/*     */           
/* 147 */           if (property.getType() != Enum.class && 
/* 148 */             propertyValue instanceof Enum) {
/* 149 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/*     */           
/* 153 */           if (nodeId == NodeId.mapping && 
/* 154 */             property.getType() == propertyValue.getClass() && 
/* 155 */             !(propertyValue instanceof Map) && 
/* 156 */             !nodeValue.getTag().equals(Tag.SET)) {
/* 157 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 162 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 167 */     return new NodeTuple((Node)nodeKey, nodeValue);
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
/*     */   protected void checkGlobalTag(Property property, Node node, Object object) {
/* 181 */     if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 186 */     if (arguments != null) {
/* 187 */       if (node.getNodeId() == NodeId.sequence) {
/*     */         
/* 189 */         Class<? extends Object> t = (Class)arguments[0];
/* 190 */         SequenceNode snode = (SequenceNode)node;
/* 191 */         Iterable<Object> memberList = Collections.EMPTY_LIST;
/* 192 */         if (object.getClass().isArray()) {
/* 193 */           memberList = Arrays.asList((Object[])object);
/* 194 */         } else if (object instanceof Iterable) {
/*     */           
/* 196 */           memberList = (Iterable<Object>)object;
/*     */         } 
/* 198 */         Iterator<Object> iter = memberList.iterator();
/* 199 */         if (iter.hasNext()) {
/* 200 */           for (Node childNode : snode.getValue()) {
/* 201 */             Object member = iter.next();
/* 202 */             if (member != null && 
/* 203 */               t.equals(member.getClass()) && 
/* 204 */               childNode.getNodeId() == NodeId.mapping) {
/* 205 */               childNode.setTag(Tag.MAP);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 211 */       else if (object instanceof Set) {
/* 212 */         Class<?> t = arguments[0];
/* 213 */         MappingNode mnode = (MappingNode)node;
/* 214 */         Iterator<NodeTuple> iter = mnode.getValue().iterator();
/* 215 */         Set<?> set = (Set)object;
/* 216 */         for (Object member : set) {
/* 217 */           NodeTuple tuple = iter.next();
/* 218 */           Node keyNode = tuple.getKeyNode();
/* 219 */           if (t.equals(member.getClass()) && 
/* 220 */             keyNode.getNodeId() == NodeId.mapping) {
/* 221 */             keyNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 225 */       } else if (object instanceof Map) {
/* 226 */         Class<?> keyType = arguments[0];
/* 227 */         Class<?> valueType = arguments[1];
/* 228 */         MappingNode mnode = (MappingNode)node;
/* 229 */         for (NodeTuple tuple : mnode.getValue()) {
/* 230 */           resetTag((Class)keyType, tuple.getKeyNode());
/* 231 */           resetTag((Class)valueType, tuple.getValueNode());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetTag(Class<? extends Object> type, Node node) {
/* 241 */     Tag tag = node.getTag();
/* 242 */     if (tag.matches(type)) {
/* 243 */       if (Enum.class.isAssignableFrom(type)) {
/* 244 */         node.setTag(Tag.STR);
/*     */       } else {
/* 246 */         node.setTag(Tag.MAP);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Property> getProperties(Class<? extends Object> type) {
/* 259 */     if (this.typeDefinitions.containsKey(type)) {
/* 260 */       return ((TypeDescription)this.typeDefinitions.get(type)).getProperties();
/*     */     }
/* 262 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */