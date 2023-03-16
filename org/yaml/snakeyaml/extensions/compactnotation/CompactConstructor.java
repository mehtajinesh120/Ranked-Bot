/*     */ package org.yaml.snakeyaml.extensions.compactnotation;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.constructor.Construct;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
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
/*     */ public class CompactConstructor
/*     */   extends Constructor
/*     */ {
/*  39 */   private static final Pattern GUESS_COMPACT = Pattern.compile("\\p{Alpha}.*\\s*\\((?:,?\\s*(?:(?:\\w*)|(?:\\p{Alpha}\\w*\\s*=.+))\\s*)+\\)");
/*  40 */   private static final Pattern FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");
/*     */   
/*  42 */   private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");
/*     */   private Construct compactConstruct;
/*     */   
/*     */   protected Object constructCompactFormat(ScalarNode node, CompactData data) {
/*     */     try {
/*  47 */       Object obj = createInstance(node, data);
/*  48 */       Map<String, Object> properties = new HashMap<>(data.getProperties());
/*  49 */       setProperties(obj, properties);
/*  50 */       return obj;
/*  51 */     } catch (Exception e) {
/*  52 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
/*  57 */     Class<?> clazz = getClassForName(data.getPrefix());
/*  58 */     Class<?>[] args = new Class[data.getArguments().size()];
/*  59 */     for (int i = 0; i < args.length; i++)
/*     */     {
/*  61 */       args[i] = String.class;
/*     */     }
/*  63 */     Constructor<?> c = clazz.getDeclaredConstructor(args);
/*  64 */     c.setAccessible(true);
/*  65 */     return c.newInstance(data.getArguments().toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setProperties(Object bean, Map<String, Object> data) throws Exception {
/*  70 */     if (data == null) {
/*  71 */       throw new NullPointerException("Data for Compact Object Notation cannot be null.");
/*     */     }
/*  73 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/*  74 */       String key = entry.getKey();
/*  75 */       Property property = getPropertyUtils().getProperty(bean.getClass(), key);
/*     */       try {
/*  77 */         property.set(bean, entry.getValue());
/*  78 */       } catch (IllegalArgumentException e) {
/*  79 */         throw new YAMLException("Cannot set property='" + key + "' with value='" + data.get(key) + "' (" + data
/*  80 */             .get(key).getClass() + ") in " + bean);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompactData getCompactData(String scalar) {
/*  86 */     if (!scalar.endsWith(")")) {
/*  87 */       return null;
/*     */     }
/*  89 */     if (scalar.indexOf('(') < 0) {
/*  90 */       return null;
/*     */     }
/*  92 */     Matcher m = FIRST_PATTERN.matcher(scalar);
/*  93 */     if (m.matches()) {
/*  94 */       String tag = m.group(1).trim();
/*  95 */       String content = m.group(3);
/*  96 */       CompactData data = new CompactData(tag);
/*  97 */       if (content.length() == 0) {
/*  98 */         return data;
/*     */       }
/* 100 */       String[] names = content.split("\\s*,\\s*");
/* 101 */       for (int i = 0; i < names.length; i++) {
/* 102 */         String section = names[i];
/* 103 */         if (section.indexOf('=') < 0) {
/* 104 */           data.getArguments().add(section);
/*     */         } else {
/* 106 */           Matcher sm = PROPERTY_NAME_PATTERN.matcher(section);
/* 107 */           if (sm.matches()) {
/* 108 */             String name = sm.group(1);
/* 109 */             String value = sm.group(2).trim();
/* 110 */             data.getProperties().put(name, value);
/*     */           } else {
/* 112 */             return null;
/*     */           } 
/*     */         } 
/*     */       } 
/* 116 */       return data;
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   private Construct getCompactConstruct() {
/* 122 */     if (this.compactConstruct == null) {
/* 123 */       this.compactConstruct = createCompactConstruct();
/*     */     }
/* 125 */     return this.compactConstruct;
/*     */   }
/*     */   
/*     */   protected Construct createCompactConstruct() {
/* 129 */     return (Construct)new ConstructCompactObject();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Construct getConstructor(Node node) {
/* 134 */     if (node instanceof MappingNode) {
/* 135 */       MappingNode mnode = (MappingNode)node;
/* 136 */       List<NodeTuple> list = mnode.getValue();
/* 137 */       if (list.size() == 1) {
/* 138 */         NodeTuple tuple = list.get(0);
/* 139 */         Node key = tuple.getKeyNode();
/* 140 */         if (key instanceof ScalarNode) {
/* 141 */           ScalarNode scalar = (ScalarNode)key;
/* 142 */           if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
/* 143 */             return getCompactConstruct();
/*     */           }
/*     */         } 
/*     */       } 
/* 147 */     } else if (node instanceof ScalarNode) {
/* 148 */       ScalarNode scalar = (ScalarNode)node;
/* 149 */       if (GUESS_COMPACT.matcher(scalar.getValue()).matches()) {
/* 150 */         return getCompactConstruct();
/*     */       }
/*     */     } 
/* 153 */     return super.getConstructor(node);
/*     */   }
/*     */   public class ConstructCompactObject extends Constructor.ConstructMapping { public ConstructCompactObject() {
/* 156 */       super(CompactConstructor.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 161 */       MappingNode mnode = (MappingNode)node;
/* 162 */       NodeTuple nodeTuple = mnode.getValue().iterator().next();
/*     */       
/* 164 */       Node valueNode = nodeTuple.getValueNode();
/*     */       
/* 166 */       if (valueNode instanceof MappingNode) {
/* 167 */         valueNode.setType(object.getClass());
/* 168 */         constructJavaBean2ndStep((MappingNode)valueNode, object);
/*     */       } else {
/*     */         
/* 171 */         CompactConstructor.this.applySequence(object, CompactConstructor.this.constructSequence((SequenceNode)valueNode));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object construct(Node node) {
/*     */       ScalarNode tmpNode;
/* 181 */       if (node instanceof MappingNode) {
/*     */         
/* 183 */         MappingNode mnode = (MappingNode)node;
/* 184 */         NodeTuple nodeTuple = mnode.getValue().iterator().next();
/* 185 */         node.setTwoStepsConstruction(true);
/* 186 */         tmpNode = (ScalarNode)nodeTuple.getKeyNode();
/*     */       } else {
/*     */         
/* 189 */         tmpNode = (ScalarNode)node;
/*     */       } 
/*     */       
/* 192 */       CompactData data = CompactConstructor.this.getCompactData(tmpNode.getValue());
/* 193 */       if (data == null) {
/* 194 */         return CompactConstructor.this.constructScalar(tmpNode);
/*     */       }
/* 196 */       return CompactConstructor.this.constructCompactFormat(tmpNode, data);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applySequence(Object bean, List<?> value) {
/*     */     try {
/* 203 */       Property property = getPropertyUtils().getProperty(bean.getClass(), getSequencePropertyName(bean.getClass()));
/* 204 */       property.set(bean, value);
/* 205 */     } catch (Exception e) {
/* 206 */       throw new YAMLException(e);
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
/*     */   protected String getSequencePropertyName(Class<?> bean) {
/* 218 */     Set<Property> properties = getPropertyUtils().getProperties(bean);
/* 219 */     for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext(); ) {
/* 220 */       Property property = iterator.next();
/* 221 */       if (!List.class.isAssignableFrom(property.getType())) {
/* 222 */         iterator.remove();
/*     */       }
/*     */     } 
/* 225 */     if (properties.size() == 0)
/* 226 */       throw new YAMLException("No list property found in " + bean); 
/* 227 */     if (properties.size() > 1) {
/* 228 */       throw new YAMLException("Many list properties found in " + bean + "; Please override getSequencePropertyName() to specify which property to use.");
/*     */     }
/*     */     
/* 231 */     return ((Property)properties.iterator().next()).getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\extensions\compactnotation\CompactConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */