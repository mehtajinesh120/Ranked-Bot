/*      */ package org.apache.commons.collections4;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.SortedMap;
/*      */ import java.util.TreeMap;
/*      */ import org.apache.commons.collections4.map.AbstractMapDecorator;
/*      */ import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
/*      */ import org.apache.commons.collections4.map.FixedSizeMap;
/*      */ import org.apache.commons.collections4.map.FixedSizeSortedMap;
/*      */ import org.apache.commons.collections4.map.LazyMap;
/*      */ import org.apache.commons.collections4.map.LazySortedMap;
/*      */ import org.apache.commons.collections4.map.ListOrderedMap;
/*      */ import org.apache.commons.collections4.map.MultiValueMap;
/*      */ import org.apache.commons.collections4.map.PredicatedMap;
/*      */ import org.apache.commons.collections4.map.PredicatedSortedMap;
/*      */ import org.apache.commons.collections4.map.TransformedMap;
/*      */ import org.apache.commons.collections4.map.TransformedSortedMap;
/*      */ import org.apache.commons.collections4.map.UnmodifiableMap;
/*      */ import org.apache.commons.collections4.map.UnmodifiableSortedMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MapUtils
/*      */ {
/*   87 */   public static final SortedMap EMPTY_SORTED_MAP = UnmodifiableSortedMap.unmodifiableSortedMap(new TreeMap<Object, Object>());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String INDENT_STRING = "    ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> V getObject(Map<? super K, V> map, K key) {
/*  112 */     if (map != null) {
/*  113 */       return map.get(key);
/*      */     }
/*  115 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> String getString(Map<? super K, ?> map, K key) {
/*  129 */     if (map != null) {
/*  130 */       Object answer = map.get(key);
/*  131 */       if (answer != null) {
/*  132 */         return answer.toString();
/*      */       }
/*      */     } 
/*  135 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Boolean getBoolean(Map<? super K, ?> map, K key) {
/*  154 */     if (map != null) {
/*  155 */       Object answer = map.get(key);
/*  156 */       if (answer != null) {
/*  157 */         if (answer instanceof Boolean) {
/*  158 */           return (Boolean)answer;
/*      */         }
/*  160 */         if (answer instanceof String) {
/*  161 */           return Boolean.valueOf((String)answer);
/*      */         }
/*  163 */         if (answer instanceof Number) {
/*  164 */           Number n = (Number)answer;
/*  165 */           return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
/*      */         } 
/*      */       } 
/*      */     } 
/*  169 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Number getNumber(Map<? super K, ?> map, K key) {
/*  187 */     if (map != null) {
/*  188 */       Object answer = map.get(key);
/*  189 */       if (answer != null) {
/*  190 */         if (answer instanceof Number) {
/*  191 */           return (Number)answer;
/*      */         }
/*  193 */         if (answer instanceof String) {
/*      */           try {
/*  195 */             String text = (String)answer;
/*  196 */             return NumberFormat.getInstance().parse(text);
/*  197 */           } catch (ParseException e) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  203 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Byte getByte(Map<? super K, ?> map, K key) {
/*  217 */     Number answer = getNumber(map, key);
/*  218 */     if (answer == null) {
/*  219 */       return null;
/*      */     }
/*  221 */     if (answer instanceof Byte) {
/*  222 */       return (Byte)answer;
/*      */     }
/*  224 */     return Byte.valueOf(answer.byteValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Short getShort(Map<? super K, ?> map, K key) {
/*  238 */     Number answer = getNumber(map, key);
/*  239 */     if (answer == null) {
/*  240 */       return null;
/*      */     }
/*  242 */     if (answer instanceof Short) {
/*  243 */       return (Short)answer;
/*      */     }
/*  245 */     return Short.valueOf(answer.shortValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Integer getInteger(Map<? super K, ?> map, K key) {
/*  259 */     Number answer = getNumber(map, key);
/*  260 */     if (answer == null) {
/*  261 */       return null;
/*      */     }
/*  263 */     if (answer instanceof Integer) {
/*  264 */       return (Integer)answer;
/*      */     }
/*  266 */     return Integer.valueOf(answer.intValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Long getLong(Map<? super K, ?> map, K key) {
/*  280 */     Number answer = getNumber(map, key);
/*  281 */     if (answer == null) {
/*  282 */       return null;
/*      */     }
/*  284 */     if (answer instanceof Long) {
/*  285 */       return (Long)answer;
/*      */     }
/*  287 */     return Long.valueOf(answer.longValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Float getFloat(Map<? super K, ?> map, K key) {
/*  301 */     Number answer = getNumber(map, key);
/*  302 */     if (answer == null) {
/*  303 */       return null;
/*      */     }
/*  305 */     if (answer instanceof Float) {
/*  306 */       return (Float)answer;
/*      */     }
/*  308 */     return Float.valueOf(answer.floatValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Double getDouble(Map<? super K, ?> map, K key) {
/*  322 */     Number answer = getNumber(map, key);
/*  323 */     if (answer == null) {
/*  324 */       return null;
/*      */     }
/*  326 */     if (answer instanceof Double) {
/*  327 */       return (Double)answer;
/*      */     }
/*  329 */     return Double.valueOf(answer.doubleValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Map<?, ?> getMap(Map<? super K, ?> map, K key) {
/*  344 */     if (map != null) {
/*  345 */       Object answer = map.get(key);
/*  346 */       if (answer != null && answer instanceof Map) {
/*  347 */         return (Map<?, ?>)answer;
/*      */       }
/*      */     } 
/*  350 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> V getObject(Map<K, V> map, K key, V defaultValue) {
/*  368 */     if (map != null) {
/*  369 */       V answer = map.get(key);
/*  370 */       if (answer != null) {
/*  371 */         return answer;
/*      */       }
/*      */     } 
/*  374 */     return defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> String getString(Map<? super K, ?> map, K key, String defaultValue) {
/*  390 */     String answer = getString(map, key);
/*  391 */     if (answer == null) {
/*  392 */       answer = defaultValue;
/*      */     }
/*  394 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Boolean getBoolean(Map<? super K, ?> map, K key, Boolean defaultValue) {
/*  410 */     Boolean answer = getBoolean(map, key);
/*  411 */     if (answer == null) {
/*  412 */       answer = defaultValue;
/*      */     }
/*  414 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Number getNumber(Map<? super K, ?> map, K key, Number defaultValue) {
/*  430 */     Number answer = getNumber(map, key);
/*  431 */     if (answer == null) {
/*  432 */       answer = defaultValue;
/*      */     }
/*  434 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Byte getByte(Map<? super K, ?> map, K key, Byte defaultValue) {
/*  450 */     Byte answer = getByte(map, key);
/*  451 */     if (answer == null) {
/*  452 */       answer = defaultValue;
/*      */     }
/*  454 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Short getShort(Map<? super K, ?> map, K key, Short defaultValue) {
/*  470 */     Short answer = getShort(map, key);
/*  471 */     if (answer == null) {
/*  472 */       answer = defaultValue;
/*      */     }
/*  474 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Integer getInteger(Map<? super K, ?> map, K key, Integer defaultValue) {
/*  490 */     Integer answer = getInteger(map, key);
/*  491 */     if (answer == null) {
/*  492 */       answer = defaultValue;
/*      */     }
/*  494 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Long getLong(Map<? super K, ?> map, K key, Long defaultValue) {
/*  510 */     Long answer = getLong(map, key);
/*  511 */     if (answer == null) {
/*  512 */       answer = defaultValue;
/*      */     }
/*  514 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Float getFloat(Map<? super K, ?> map, K key, Float defaultValue) {
/*  530 */     Float answer = getFloat(map, key);
/*  531 */     if (answer == null) {
/*  532 */       answer = defaultValue;
/*      */     }
/*  534 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Double getDouble(Map<? super K, ?> map, K key, Double defaultValue) {
/*  550 */     Double answer = getDouble(map, key);
/*  551 */     if (answer == null) {
/*  552 */       answer = defaultValue;
/*      */     }
/*  554 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> Map<?, ?> getMap(Map<? super K, ?> map, K key, Map<?, ?> defaultValue) {
/*  570 */     Map<?, ?> answer = getMap(map, key);
/*  571 */     if (answer == null) {
/*  572 */       answer = defaultValue;
/*      */     }
/*  574 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> boolean getBooleanValue(Map<? super K, ?> map, K key) {
/*  595 */     return Boolean.TRUE.equals(getBoolean(map, key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> byte getByteValue(Map<? super K, ?> map, K key) {
/*  609 */     Byte byteObject = getByte(map, key);
/*  610 */     if (byteObject == null) {
/*  611 */       return 0;
/*      */     }
/*  613 */     return byteObject.byteValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> short getShortValue(Map<? super K, ?> map, K key) {
/*  627 */     Short shortObject = getShort(map, key);
/*  628 */     if (shortObject == null) {
/*  629 */       return 0;
/*      */     }
/*  631 */     return shortObject.shortValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> int getIntValue(Map<? super K, ?> map, K key) {
/*  645 */     Integer integerObject = getInteger(map, key);
/*  646 */     if (integerObject == null) {
/*  647 */       return 0;
/*      */     }
/*  649 */     return integerObject.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> long getLongValue(Map<? super K, ?> map, K key) {
/*  663 */     Long longObject = getLong(map, key);
/*  664 */     if (longObject == null) {
/*  665 */       return 0L;
/*      */     }
/*  667 */     return longObject.longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> float getFloatValue(Map<? super K, ?> map, K key) {
/*  681 */     Float floatObject = getFloat(map, key);
/*  682 */     if (floatObject == null) {
/*  683 */       return 0.0F;
/*      */     }
/*  685 */     return floatObject.floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> double getDoubleValue(Map<? super K, ?> map, K key) {
/*  699 */     Double doubleObject = getDouble(map, key);
/*  700 */     if (doubleObject == null) {
/*  701 */       return 0.0D;
/*      */     }
/*  703 */     return doubleObject.doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> boolean getBooleanValue(Map<? super K, ?> map, K key, boolean defaultValue) {
/*  726 */     Boolean booleanObject = getBoolean(map, key);
/*  727 */     if (booleanObject == null) {
/*  728 */       return defaultValue;
/*      */     }
/*  730 */     return booleanObject.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> byte getByteValue(Map<? super K, ?> map, K key, byte defaultValue) {
/*  746 */     Byte byteObject = getByte(map, key);
/*  747 */     if (byteObject == null) {
/*  748 */       return defaultValue;
/*      */     }
/*  750 */     return byteObject.byteValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> short getShortValue(Map<? super K, ?> map, K key, short defaultValue) {
/*  766 */     Short shortObject = getShort(map, key);
/*  767 */     if (shortObject == null) {
/*  768 */       return defaultValue;
/*      */     }
/*  770 */     return shortObject.shortValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> int getIntValue(Map<? super K, ?> map, K key, int defaultValue) {
/*  786 */     Integer integerObject = getInteger(map, key);
/*  787 */     if (integerObject == null) {
/*  788 */       return defaultValue;
/*      */     }
/*  790 */     return integerObject.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> long getLongValue(Map<? super K, ?> map, K key, long defaultValue) {
/*  806 */     Long longObject = getLong(map, key);
/*  807 */     if (longObject == null) {
/*  808 */       return defaultValue;
/*      */     }
/*  810 */     return longObject.longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> float getFloatValue(Map<? super K, ?> map, K key, float defaultValue) {
/*  826 */     Float floatObject = getFloat(map, key);
/*  827 */     if (floatObject == null) {
/*  828 */       return defaultValue;
/*      */     }
/*  830 */     return floatObject.floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> double getDoubleValue(Map<? super K, ?> map, K key, double defaultValue) {
/*  846 */     Double doubleObject = getDouble(map, key);
/*  847 */     if (doubleObject == null) {
/*  848 */       return defaultValue;
/*      */     }
/*  850 */     return doubleObject.doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Properties toProperties(Map<K, V> map) {
/*  870 */     Properties answer = new Properties();
/*  871 */     if (map != null) {
/*  872 */       for (Map.Entry<K, V> entry2 : map.entrySet()) {
/*  873 */         Map.Entry<?, ?> entry = entry2;
/*  874 */         Object key = entry.getKey();
/*  875 */         Object value = entry.getValue();
/*  876 */         answer.put(key, value);
/*      */       } 
/*      */     }
/*  879 */     return answer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> toMap(ResourceBundle resourceBundle) {
/*  890 */     Enumeration<String> enumeration = resourceBundle.getKeys();
/*  891 */     Map<String, Object> map = new HashMap<String, Object>();
/*      */     
/*  893 */     while (enumeration.hasMoreElements()) {
/*  894 */       String key = enumeration.nextElement();
/*  895 */       Object value = resourceBundle.getObject(key);
/*  896 */       map.put(key, value);
/*      */     } 
/*      */     
/*  899 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void verbosePrint(PrintStream out, Object label, Map<?, ?> map) {
/*  923 */     verbosePrintInternal(out, label, map, new ArrayDeque<Map<?, ?>>(), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void debugPrint(PrintStream out, Object label, Map<?, ?> map) {
/*  945 */     verbosePrintInternal(out, label, map, new ArrayDeque<Map<?, ?>>(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void verbosePrintInternal(PrintStream out, Object label, Map<?, ?> map, Deque<Map<?, ?>> lineage, boolean debug) {
/*  975 */     printIndent(out, lineage.size());
/*      */     
/*  977 */     if (map == null) {
/*  978 */       if (label != null) {
/*  979 */         out.print(label);
/*  980 */         out.print(" = ");
/*      */       } 
/*  982 */       out.println("null");
/*      */       return;
/*      */     } 
/*  985 */     if (label != null) {
/*  986 */       out.print(label);
/*  987 */       out.println(" = ");
/*      */     } 
/*      */     
/*  990 */     printIndent(out, lineage.size());
/*  991 */     out.println("{");
/*      */     
/*  993 */     lineage.addLast(map);
/*      */     
/*  995 */     for (Map.Entry<?, ?> entry : map.entrySet()) {
/*  996 */       Object childKey = entry.getKey();
/*  997 */       Object childValue = entry.getValue();
/*  998 */       if (childValue instanceof Map && !lineage.contains(childValue)) {
/*  999 */         verbosePrintInternal(out, (childKey == null) ? "null" : childKey, (Map<?, ?>)childValue, lineage, debug);
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */       
/* 1006 */       printIndent(out, lineage.size());
/* 1007 */       out.print(childKey);
/* 1008 */       out.print(" = ");
/*      */       
/* 1010 */       int lineageIndex = IterableUtils.indexOf(lineage, PredicateUtils.equalPredicate(childValue));
/*      */ 
/*      */       
/* 1013 */       if (lineageIndex == -1) {
/* 1014 */         out.print(childValue);
/* 1015 */       } else if (lineage.size() - 1 == lineageIndex) {
/* 1016 */         out.print("(this Map)");
/*      */       } else {
/* 1018 */         out.print("(ancestor[" + (lineage.size() - 1 - lineageIndex - 1) + "] Map)");
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1024 */       if (debug && childValue != null) {
/* 1025 */         out.print(' ');
/* 1026 */         out.println(childValue.getClass().getName()); continue;
/*      */       } 
/* 1028 */       out.println();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1033 */     lineage.removeLast();
/*      */     
/* 1035 */     printIndent(out, lineage.size());
/* 1036 */     out.println(debug ? ("} " + map.getClass().getName()) : "}");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printIndent(PrintStream out, int indent) {
/* 1045 */     for (int i = 0; i < indent; i++) {
/* 1046 */       out.print("    ");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
/* 1068 */     Map<V, K> out = new HashMap<V, K>(map.size());
/* 1069 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/* 1070 */       out.put(entry.getValue(), entry.getKey());
/*      */     }
/* 1072 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> void safeAddToMap(Map<? super K, Object> map, K key, Object value) throws NullPointerException {
/* 1098 */     map.put(key, (value == null) ? "" : value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> putAll(Map<K, V> map, Object[] array) {
/* 1152 */     if (map == null) {
/* 1153 */       throw new NullPointerException("The map must not be null");
/*      */     }
/* 1155 */     if (array == null || array.length == 0) {
/* 1156 */       return map;
/*      */     }
/* 1158 */     Object obj = array[0];
/* 1159 */     if (obj instanceof Map.Entry) {
/* 1160 */       for (Object element : array) {
/*      */         
/* 1162 */         Map.Entry<K, V> entry = (Map.Entry<K, V>)element;
/* 1163 */         map.put(entry.getKey(), entry.getValue());
/*      */       } 
/* 1165 */     } else if (obj instanceof KeyValue) {
/* 1166 */       for (Object element : array) {
/*      */         
/* 1168 */         KeyValue<K, V> keyval = (KeyValue<K, V>)element;
/* 1169 */         map.put(keyval.getKey(), keyval.getValue());
/*      */       } 
/* 1171 */     } else if (obj instanceof Object[]) {
/* 1172 */       for (int i = 0; i < array.length; i++) {
/* 1173 */         Object[] sub = (Object[])array[i];
/* 1174 */         if (sub == null || sub.length < 2) {
/* 1175 */           throw new IllegalArgumentException("Invalid array element: " + i);
/*      */         }
/*      */         
/* 1178 */         map.put((K)sub[0], (V)sub[1]);
/*      */       } 
/*      */     } else {
/* 1181 */       for (int i = 0; i < array.length - 1;)
/*      */       {
/* 1183 */         map.put((K)array[i++], (V)array[i++]);
/*      */       }
/*      */     } 
/* 1186 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
/* 1201 */     return (map == null) ? Collections.<K, V>emptyMap() : map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Map<?, ?> map) {
/* 1214 */     return (map == null || map.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isNotEmpty(Map<?, ?> map) {
/* 1227 */     return !isEmpty(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
/* 1257 */     return Collections.synchronizedMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> map) {
/* 1272 */     return UnmodifiableMap.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableMap<K, V> predicatedMap(Map<K, V> map, Predicate<? super K> keyPred, Predicate<? super V> valuePred) {
/* 1294 */     return (IterableMap<K, V>)PredicatedMap.predicatedMap(map, keyPred, valuePred);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableMap<K, V> transformedMap(Map<K, V> map, Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
/* 1323 */     return (IterableMap<K, V>)TransformedMap.transformingMap(map, keyTransformer, valueTransformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableMap<K, V> fixedSizeMap(Map<K, V> map) {
/* 1339 */     return (IterableMap<K, V>)FixedSizeMap.fixedSizeMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableMap<K, V> lazyMap(Map<K, V> map, Factory<? extends V> factory) {
/* 1373 */     return (IterableMap<K, V>)LazyMap.lazyMap(map, factory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableMap<K, V> lazyMap(Map<K, V> map, Transformer<? super K, ? extends V> transformerFactory) {
/* 1415 */     return (IterableMap<K, V>)LazyMap.lazyMap(map, transformerFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> OrderedMap<K, V> orderedMap(Map<K, V> map) {
/* 1432 */     return (OrderedMap<K, V>)ListOrderedMap.listOrderedMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> MultiValueMap<K, V> multiValueMap(Map<K, ? super Collection<V>> map) {
/* 1449 */     return MultiValueMap.multiValueMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, C> map, Class<C> collectionClass) {
/* 1470 */     return MultiValueMap.multiValueMap(map, collectionClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, C> map, Factory<C> collectionFactory) {
/* 1491 */     return MultiValueMap.multiValueMap(map, collectionFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> map) {
/* 1522 */     return Collections.synchronizedSortedMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> map) {
/* 1537 */     return UnmodifiableSortedMap.unmodifiableSortedMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> predicatedSortedMap(SortedMap<K, V> map, Predicate<? super K> keyPred, Predicate<? super V> valuePred) {
/* 1559 */     return (SortedMap<K, V>)PredicatedSortedMap.predicatedSortedMap(map, keyPred, valuePred);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> transformedSortedMap(SortedMap<K, V> map, Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
/* 1588 */     return (SortedMap<K, V>)TransformedSortedMap.transformingSortedMap(map, keyTransformer, valueTransformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> fixedSizeSortedMap(SortedMap<K, V> map) {
/* 1604 */     return (SortedMap<K, V>)FixedSizeSortedMap.fixedSizeSortedMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> lazySortedMap(SortedMap<K, V> map, Factory<? extends V> factory) {
/* 1639 */     return (SortedMap<K, V>)LazySortedMap.lazySortedMap(map, factory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> lazySortedMap(SortedMap<K, V> map, Transformer<? super K, ? extends V> transformerFactory) {
/* 1681 */     return (SortedMap<K, V>)LazySortedMap.lazySortedMap(map, transformerFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> void populateMap(Map<K, V> map, Iterable<? extends V> elements, Transformer<V, K> keyTransformer) {
/* 1697 */     populateMap(map, elements, keyTransformer, TransformerUtils.nopTransformer());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V, E> void populateMap(Map<K, V> map, Iterable<? extends E> elements, Transformer<E, K> keyTransformer, Transformer<E, V> valueTransformer) {
/* 1716 */     Iterator<? extends E> iter = elements.iterator();
/* 1717 */     while (iter.hasNext()) {
/* 1718 */       E temp = iter.next();
/* 1719 */       map.put(keyTransformer.transform(temp), valueTransformer.transform(temp));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> void populateMap(MultiMap<K, V> map, Iterable<? extends V> elements, Transformer<V, K> keyTransformer) {
/* 1736 */     populateMap(map, elements, keyTransformer, TransformerUtils.nopTransformer());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V, E> void populateMap(MultiMap<K, V> map, Iterable<? extends E> elements, Transformer<E, K> keyTransformer, Transformer<E, V> valueTransformer) {
/* 1755 */     Iterator<? extends E> iter = elements.iterator();
/* 1756 */     while (iter.hasNext()) {
/* 1757 */       E temp = iter.next();
/* 1758 */       map.put(keyTransformer.transform(temp), valueTransformer.transform(temp));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableMap<K, V> iterableMap(Map<K, V> map) {
/* 1773 */     if (map == null) {
/* 1774 */       throw new NullPointerException("Map must not be null");
/*      */     }
/* 1776 */     return (map instanceof IterableMap) ? (IterableMap<K, V>)map : (IterableMap<K, V>)new AbstractMapDecorator<K, V>(map)
/*      */       {
/*      */       
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IterableSortedMap<K, V> iterableSortedMap(SortedMap<K, V> sortedMap) {
/* 1790 */     if (sortedMap == null) {
/* 1791 */       throw new NullPointerException("Map must not be null");
/*      */     }
/* 1793 */     return (sortedMap instanceof IterableSortedMap) ? (IterableSortedMap<K, V>)sortedMap : (IterableSortedMap<K, V>)new AbstractSortedMapDecorator<K, V>(sortedMap) {
/*      */       
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\MapUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */