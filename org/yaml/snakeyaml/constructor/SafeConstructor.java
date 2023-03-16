/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
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
/*     */ public class SafeConstructor
/*     */   extends BaseConstructor
/*     */ {
/*  45 */   public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
/*     */   
/*     */   public SafeConstructor() {
/*  48 */     this(new LoaderOptions());
/*     */   }
/*     */   
/*     */   public SafeConstructor(LoaderOptions loadingConfig) {
/*  52 */     super(loadingConfig);
/*  53 */     this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
/*  54 */     this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
/*  55 */     this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
/*  56 */     this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
/*  57 */     this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
/*  58 */     this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
/*  59 */     this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
/*  60 */     this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
/*  61 */     this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
/*  62 */     this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
/*  63 */     this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
/*  64 */     this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
/*  65 */     this.yamlConstructors.put(null, undefinedConstructor);
/*  66 */     this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
/*  67 */     this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
/*  68 */     this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
/*     */   }
/*     */   
/*     */   protected void flattenMapping(MappingNode node) {
/*  72 */     flattenMapping(node, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flattenMapping(MappingNode node, boolean forceStringKeys) {
/*  77 */     processDuplicateKeys(node, forceStringKeys);
/*  78 */     if (node.isMerged()) {
/*  79 */       node.setValue(mergeNode(node, true, new HashMap<>(), new ArrayList<>(), forceStringKeys));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processDuplicateKeys(MappingNode node) {
/*  85 */     processDuplicateKeys(node, false);
/*     */   }
/*     */   
/*     */   protected void processDuplicateKeys(MappingNode node, boolean forceStringKeys) {
/*  89 */     List<NodeTuple> nodeValue = node.getValue();
/*  90 */     Map<Object, Integer> keys = new HashMap<>(nodeValue.size());
/*  91 */     TreeSet<Integer> toRemove = new TreeSet<>();
/*  92 */     int i = 0;
/*  93 */     for (NodeTuple tuple : nodeValue) {
/*  94 */       Node keyNode = tuple.getKeyNode();
/*  95 */       if (!keyNode.getTag().equals(Tag.MERGE)) {
/*  96 */         if (forceStringKeys) {
/*  97 */           if (keyNode instanceof ScalarNode) {
/*  98 */             keyNode.setType(String.class);
/*  99 */             keyNode.setTag(Tag.STR);
/*     */           } else {
/* 101 */             throw new YAMLException("Keys must be scalars but found: " + keyNode);
/*     */           } 
/*     */         }
/* 104 */         Object key = constructObject(keyNode);
/* 105 */         if (key != null && !forceStringKeys && 
/* 106 */           keyNode.isTwoStepsConstruction()) {
/* 107 */           if (!this.loadingConfig.getAllowRecursiveKeys()) {
/* 108 */             throw new YAMLException("Recursive key for mapping is detected but it is not configured to be allowed.");
/*     */           }
/*     */           
/*     */           try {
/* 112 */             key.hashCode();
/* 113 */           } catch (Exception e) {
/* 114 */             throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple
/* 115 */                 .getKeyNode().getStartMark(), e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 121 */         Integer prevIndex = keys.put(key, Integer.valueOf(i));
/* 122 */         if (prevIndex != null) {
/* 123 */           if (!isAllowDuplicateKeys()) {
/* 124 */             throw new DuplicateKeyException(node.getStartMark(), key, tuple
/* 125 */                 .getKeyNode().getStartMark());
/*     */           }
/* 127 */           toRemove.add(prevIndex);
/*     */         } 
/*     */       } 
/* 130 */       i++;
/*     */     } 
/*     */     
/* 133 */     Iterator<Integer> indices2remove = toRemove.descendingIterator();
/* 134 */     while (indices2remove.hasNext()) {
/* 135 */       nodeValue.remove(((Integer)indices2remove.next()).intValue());
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
/*     */   private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values, boolean forceStringKeys) {
/* 150 */     Iterator<NodeTuple> iter = node.getValue().iterator();
/* 151 */     while (iter.hasNext()) {
/* 152 */       NodeTuple nodeTuple = iter.next();
/* 153 */       Node keyNode = nodeTuple.getKeyNode();
/* 154 */       Node valueNode = nodeTuple.getValueNode();
/* 155 */       if (keyNode.getTag().equals(Tag.MERGE)) {
/* 156 */         MappingNode mn; SequenceNode sn; List<Node> vals; iter.remove();
/* 157 */         switch (valueNode.getNodeId()) {
/*     */           case mapping:
/* 159 */             mn = (MappingNode)valueNode;
/* 160 */             mergeNode(mn, false, key2index, values, forceStringKeys);
/*     */             continue;
/*     */           case sequence:
/* 163 */             sn = (SequenceNode)valueNode;
/* 164 */             vals = sn.getValue();
/* 165 */             for (Node subnode : vals) {
/* 166 */               if (!(subnode instanceof MappingNode)) {
/* 167 */                 throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping for merging, but found " + subnode
/* 168 */                     .getNodeId(), subnode
/* 169 */                     .getStartMark());
/*     */               }
/* 171 */               MappingNode mnode = (MappingNode)subnode;
/* 172 */               mergeNode(mnode, false, key2index, values, forceStringKeys);
/*     */             } 
/*     */             continue;
/*     */         } 
/* 176 */         throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode
/*     */             
/* 178 */             .getNodeId(), valueNode
/* 179 */             .getStartMark());
/*     */       } 
/*     */ 
/*     */       
/* 183 */       if (forceStringKeys) {
/* 184 */         if (keyNode instanceof ScalarNode) {
/* 185 */           keyNode.setType(String.class);
/* 186 */           keyNode.setTag(Tag.STR);
/*     */         } else {
/* 188 */           throw new YAMLException("Keys must be scalars but found: " + keyNode);
/*     */         } 
/*     */       }
/* 191 */       Object key = constructObject(keyNode);
/* 192 */       if (!key2index.containsKey(key)) {
/* 193 */         values.add(nodeTuple);
/*     */         
/* 195 */         key2index.put(key, Integer.valueOf(values.size() - 1)); continue;
/* 196 */       }  if (isPreffered)
/*     */       {
/*     */         
/* 199 */         values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
/*     */       }
/*     */     } 
/*     */     
/* 203 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 208 */     flattenMapping(node);
/* 209 */     super.constructMapping2ndStep(node, mapping);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 214 */     flattenMapping(node);
/* 215 */     super.constructSet2ndStep(node, set);
/*     */   }
/*     */   
/*     */   public class ConstructYamlNull
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 222 */       if (node != null) {
/* 223 */         SafeConstructor.this.constructScalar((ScalarNode)node);
/*     */       }
/* 225 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 229 */   private static final Map<String, Boolean> BOOL_VALUES = new HashMap<>();
/*     */   
/*     */   static {
/* 232 */     BOOL_VALUES.put("yes", Boolean.TRUE);
/* 233 */     BOOL_VALUES.put("no", Boolean.FALSE);
/* 234 */     BOOL_VALUES.put("true", Boolean.TRUE);
/* 235 */     BOOL_VALUES.put("false", Boolean.FALSE);
/* 236 */     BOOL_VALUES.put("on", Boolean.TRUE);
/* 237 */     BOOL_VALUES.put("off", Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public class ConstructYamlBool
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 244 */       String val = SafeConstructor.this.constructScalar((ScalarNode)node);
/* 245 */       return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlInt
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 253 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).replaceAll("_", "");
/* 254 */       if (value.isEmpty()) {
/* 255 */         throw new ConstructorException("while constructing an int", node.getStartMark(), "found empty value", node
/* 256 */             .getStartMark());
/*     */       }
/* 258 */       int sign = 1;
/* 259 */       char first = value.charAt(0);
/* 260 */       if (first == '-') {
/* 261 */         sign = -1;
/* 262 */         value = value.substring(1);
/* 263 */       } else if (first == '+') {
/* 264 */         value = value.substring(1);
/*     */       } 
/* 266 */       int base = 10;
/* 267 */       if ("0".equals(value))
/* 268 */         return Integer.valueOf(0); 
/* 269 */       if (value.startsWith("0b"))
/* 270 */       { value = value.substring(2);
/* 271 */         base = 2; }
/* 272 */       else if (value.startsWith("0x"))
/* 273 */       { value = value.substring(2);
/* 274 */         base = 16; }
/* 275 */       else if (value.startsWith("0"))
/* 276 */       { value = value.substring(1);
/* 277 */         base = 8; }
/* 278 */       else { if (value.indexOf(':') != -1) {
/* 279 */           String[] digits = value.split(":");
/* 280 */           int bes = 1;
/* 281 */           int val = 0;
/* 282 */           for (int i = 0, j = digits.length; i < j; i++) {
/* 283 */             val = (int)(val + Long.parseLong(digits[j - i - 1]) * bes);
/* 284 */             bes *= 60;
/*     */           } 
/* 286 */           return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
/*     */         } 
/* 288 */         return SafeConstructor.this.createNumber(sign, value, 10); }
/*     */       
/* 290 */       return SafeConstructor.this.createNumber(sign, value, base);
/*     */     }
/*     */   }
/*     */   
/* 294 */   private static final int[][] RADIX_MAX = new int[17][2];
/*     */   
/*     */   static {
/* 297 */     int[] radixList = { 2, 8, 10, 16 };
/* 298 */     for (int radix : radixList) {
/* 299 */       (new int[2])[0] = 
/* 300 */         maxLen(2147483647, radix); (new int[2])[1] = maxLen(Long.MAX_VALUE, radix);
/*     */       RADIX_MAX[radix] = new int[2];
/*     */     } 
/*     */   }
/*     */   private static int maxLen(int max, int radix) {
/* 305 */     return Integer.toString(max, radix).length();
/*     */   }
/*     */   
/*     */   private static int maxLen(long max, int radix) {
/* 309 */     return Long.toString(max, radix).length();
/*     */   }
/*     */   private Number createNumber(int sign, String number, int radix) {
/*     */     Number result;
/* 313 */     int len = (number != null) ? number.length() : 0;
/* 314 */     if (sign < 0) {
/* 315 */       number = "-" + number;
/*     */     }
/* 317 */     int[] maxArr = (radix < RADIX_MAX.length) ? RADIX_MAX[radix] : null;
/* 318 */     if (maxArr != null) {
/* 319 */       boolean gtInt = (len > maxArr[0]);
/* 320 */       if (gtInt) {
/* 321 */         if (len > maxArr[1]) {
/* 322 */           return new BigInteger(number, radix);
/*     */         }
/* 324 */         return createLongOrBigInteger(number, radix);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 329 */       result = Integer.valueOf(number, radix);
/* 330 */     } catch (NumberFormatException e) {
/* 331 */       result = createLongOrBigInteger(number, radix);
/*     */     } 
/* 333 */     return result;
/*     */   }
/*     */   
/*     */   protected static Number createLongOrBigInteger(String number, int radix) {
/*     */     try {
/* 338 */       return Long.valueOf(number, radix);
/* 339 */     } catch (NumberFormatException e1) {
/* 340 */       return new BigInteger(number, radix);
/*     */     } 
/*     */   }
/*     */   
/*     */   public class ConstructYamlFloat
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 348 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).replaceAll("_", "");
/* 349 */       if (value.isEmpty()) {
/* 350 */         throw new ConstructorException("while constructing a float", node.getStartMark(), "found empty value", node
/* 351 */             .getStartMark());
/*     */       }
/* 353 */       int sign = 1;
/* 354 */       char first = value.charAt(0);
/* 355 */       if (first == '-') {
/* 356 */         sign = -1;
/* 357 */         value = value.substring(1);
/* 358 */       } else if (first == '+') {
/* 359 */         value = value.substring(1);
/*     */       } 
/* 361 */       String valLower = value.toLowerCase();
/* 362 */       if (".inf".equals(valLower))
/* 363 */         return Double.valueOf((sign == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY); 
/* 364 */       if (".nan".equals(valLower))
/* 365 */         return Double.valueOf(Double.NaN); 
/* 366 */       if (value.indexOf(':') != -1) {
/* 367 */         String[] digits = value.split(":");
/* 368 */         int bes = 1;
/* 369 */         double val = 0.0D;
/* 370 */         for (int i = 0, j = digits.length; i < j; i++) {
/* 371 */           val += Double.parseDouble(digits[j - i - 1]) * bes;
/* 372 */           bes *= 60;
/*     */         } 
/* 374 */         return Double.valueOf(sign * val);
/*     */       } 
/* 376 */       Double d = Double.valueOf(value);
/* 377 */       return Double.valueOf(d.doubleValue() * sign);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlBinary
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 387 */       String noWhiteSpaces = SafeConstructor.this.constructScalar((ScalarNode)node).replaceAll("\\s", "");
/* 388 */       byte[] decoded = Base64Coder.decode(noWhiteSpaces.toCharArray());
/* 389 */       return decoded;
/*     */     }
/*     */   }
/*     */   
/* 393 */   private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
/*     */ 
/*     */   
/* 396 */   private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
/*     */   
/*     */   public static class ConstructYamlTimestamp
/*     */     extends AbstractConstruct {
/*     */     private Calendar calendar;
/*     */     
/*     */     public Calendar getCalendar() {
/* 403 */       return this.calendar;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       TimeZone timeZone;
/* 408 */       ScalarNode scalar = (ScalarNode)node;
/* 409 */       String nodeValue = scalar.getValue();
/* 410 */       Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
/* 411 */       if (match.matches()) {
/* 412 */         String str1 = match.group(1);
/* 413 */         String str2 = match.group(2);
/* 414 */         String str3 = match.group(3);
/* 415 */         this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 416 */         this.calendar.clear();
/* 417 */         this.calendar.set(1, Integer.parseInt(str1));
/*     */         
/* 419 */         this.calendar.set(2, Integer.parseInt(str2) - 1);
/* 420 */         this.calendar.set(5, Integer.parseInt(str3));
/* 421 */         return this.calendar.getTime();
/*     */       } 
/* 423 */       match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
/* 424 */       if (!match.matches()) {
/* 425 */         throw new YAMLException("Unexpected timestamp: " + nodeValue);
/*     */       }
/* 427 */       String year_s = match.group(1);
/* 428 */       String month_s = match.group(2);
/* 429 */       String day_s = match.group(3);
/* 430 */       String hour_s = match.group(4);
/* 431 */       String min_s = match.group(5);
/*     */       
/* 433 */       String seconds = match.group(6);
/* 434 */       String millis = match.group(7);
/* 435 */       if (millis != null) {
/* 436 */         seconds = seconds + "." + millis;
/*     */       }
/* 438 */       double fractions = Double.parseDouble(seconds);
/* 439 */       int sec_s = (int)Math.round(Math.floor(fractions));
/* 440 */       int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
/*     */       
/* 442 */       String timezoneh_s = match.group(8);
/* 443 */       String timezonem_s = match.group(9);
/*     */       
/* 445 */       if (timezoneh_s != null) {
/* 446 */         String time = (timezonem_s != null) ? (":" + timezonem_s) : "00";
/* 447 */         timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
/*     */       } else {
/*     */         
/* 450 */         timeZone = TimeZone.getTimeZone("UTC");
/*     */       } 
/* 452 */       this.calendar = Calendar.getInstance(timeZone);
/* 453 */       this.calendar.set(1, Integer.parseInt(year_s));
/*     */       
/* 455 */       this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 456 */       this.calendar.set(5, Integer.parseInt(day_s));
/* 457 */       this.calendar.set(11, Integer.parseInt(hour_s));
/* 458 */       this.calendar.set(12, Integer.parseInt(min_s));
/* 459 */       this.calendar.set(13, sec_s);
/* 460 */       this.calendar.set(14, usec);
/* 461 */       return this.calendar.getTime();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlOmap
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 472 */       Map<Object, Object> omap = new LinkedHashMap<>();
/* 473 */       if (!(node instanceof SequenceNode)) {
/* 474 */         throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a sequence, but found " + node
/* 475 */             .getNodeId(), node.getStartMark());
/*     */       }
/* 477 */       SequenceNode snode = (SequenceNode)node;
/* 478 */       for (Node subnode : snode.getValue()) {
/* 479 */         if (!(subnode instanceof MappingNode)) {
/* 480 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a mapping of length 1, but found " + subnode
/* 481 */               .getNodeId(), subnode
/* 482 */               .getStartMark());
/*     */         }
/* 484 */         MappingNode mnode = (MappingNode)subnode;
/* 485 */         if (mnode.getValue().size() != 1) {
/* 486 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a single mapping item, but found " + mnode
/* 487 */               .getValue().size() + " items", mnode
/* 488 */               .getStartMark());
/*     */         }
/* 490 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 491 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 492 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 493 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 494 */         omap.put(key, value);
/*     */       } 
/* 496 */       return omap;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlPairs
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 506 */       if (!(node instanceof SequenceNode)) {
/* 507 */         throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node
/* 508 */             .getNodeId(), node.getStartMark());
/*     */       }
/* 510 */       SequenceNode snode = (SequenceNode)node;
/* 511 */       List<Object[]> pairs = new ArrayList(snode.getValue().size());
/* 512 */       for (Node subnode : snode.getValue()) {
/* 513 */         if (!(subnode instanceof MappingNode)) {
/* 514 */           throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode
/* 515 */               .getNodeId(), subnode
/* 516 */               .getStartMark());
/*     */         }
/* 518 */         MappingNode mnode = (MappingNode)subnode;
/* 519 */         if (mnode.getValue().size() != 1) {
/* 520 */           throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode
/* 521 */               .getValue().size() + " items", mnode
/* 522 */               .getStartMark());
/*     */         }
/* 524 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 525 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 526 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 527 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 528 */         pairs.add(new Object[] { key, value });
/*     */       } 
/* 530 */       return pairs;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSet
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 538 */       if (node.isTwoStepsConstruction()) {
/* 539 */         return SafeConstructor.this.constructedObjects.containsKey(node) ? SafeConstructor.this.constructedObjects.get(node) : SafeConstructor.this
/* 540 */           .createDefaultSet(((MappingNode)node).getValue().size());
/*     */       }
/* 542 */       return SafeConstructor.this.constructSet((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 549 */       if (node.isTwoStepsConstruction()) {
/* 550 */         SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 552 */         throw new YAMLException("Unexpected recursive set structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlStr
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 561 */       return SafeConstructor.this.constructScalar((ScalarNode)node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSeq
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 569 */       SequenceNode seqNode = (SequenceNode)node;
/* 570 */       if (node.isTwoStepsConstruction()) {
/* 571 */         return SafeConstructor.this.newList(seqNode);
/*     */       }
/* 573 */       return SafeConstructor.this.constructSequence(seqNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object data) {
/* 580 */       if (node.isTwoStepsConstruction()) {
/* 581 */         SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
/*     */       } else {
/* 583 */         throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlMap
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 592 */       MappingNode mnode = (MappingNode)node;
/* 593 */       if (node.isTwoStepsConstruction()) {
/* 594 */         return SafeConstructor.this.createDefaultMap(mnode.getValue().size());
/*     */       }
/* 596 */       return SafeConstructor.this.constructMapping(mnode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 603 */       if (node.isTwoStepsConstruction()) {
/* 604 */         SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/*     */       } else {
/* 606 */         throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConstructUndefined
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 615 */       throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node
/* 616 */           .getTag(), node.getStartMark());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */