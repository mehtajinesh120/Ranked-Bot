/*     */ package org.sqlite.date;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class FastDateParser
/*     */   implements DateParser, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  76 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
/*     */ 
/*     */   
/*     */   private final String pattern;
/*     */ 
/*     */   
/*     */   private final TimeZone timeZone;
/*     */ 
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   private final int century;
/*     */ 
/*     */   
/*     */   private final int startYear;
/*     */   
/*     */   private transient Pattern parsePattern;
/*     */   
/*     */   private transient Strategy[] strategies;
/*     */   
/*     */   private transient String currentFormatField;
/*     */   
/*     */   private transient Strategy nextStrategy;
/*     */ 
/*     */   
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
/* 103 */     this(pattern, timeZone, locale, null);
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
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
/*     */     int centuryStartYear;
/* 120 */     this.pattern = pattern;
/* 121 */     this.timeZone = timeZone;
/* 122 */     this.locale = locale;
/*     */     
/* 124 */     Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
/*     */     
/* 126 */     if (centuryStart != null) {
/* 127 */       definingCalendar.setTime(centuryStart);
/* 128 */       centuryStartYear = definingCalendar.get(1);
/* 129 */     } else if (locale.equals(JAPANESE_IMPERIAL)) {
/* 130 */       centuryStartYear = 0;
/*     */     } else {
/*     */       
/* 133 */       definingCalendar.setTime(new Date());
/* 134 */       centuryStartYear = definingCalendar.get(1) - 80;
/*     */     } 
/* 136 */     this.century = centuryStartYear / 100 * 100;
/* 137 */     this.startYear = centuryStartYear - this.century;
/*     */     
/* 139 */     init(definingCalendar);
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
/*     */   private void init(Calendar definingCalendar) {
/* 151 */     StringBuilder regex = new StringBuilder();
/* 152 */     List<Strategy> collector = new ArrayList<>();
/*     */     
/* 154 */     Matcher patternMatcher = formatPattern.matcher(this.pattern);
/* 155 */     if (!patternMatcher.lookingAt()) {
/* 156 */       throw new IllegalArgumentException("Illegal pattern character '" + this.pattern
/*     */           
/* 158 */           .charAt(patternMatcher.regionStart()) + "'");
/*     */     }
/*     */ 
/*     */     
/* 162 */     this.currentFormatField = patternMatcher.group();
/* 163 */     Strategy currentStrategy = getStrategy(this.currentFormatField, definingCalendar);
/*     */     while (true) {
/* 165 */       patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
/* 166 */       if (!patternMatcher.lookingAt()) {
/* 167 */         this.nextStrategy = null;
/*     */         break;
/*     */       } 
/* 170 */       String nextFormatField = patternMatcher.group();
/* 171 */       this.nextStrategy = getStrategy(nextFormatField, definingCalendar);
/* 172 */       if (currentStrategy.addRegex(this, regex)) {
/* 173 */         collector.add(currentStrategy);
/*     */       }
/* 175 */       this.currentFormatField = nextFormatField;
/* 176 */       currentStrategy = this.nextStrategy;
/*     */     } 
/* 178 */     if (patternMatcher.regionStart() != patternMatcher.regionEnd()) {
/* 179 */       throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + patternMatcher
/*     */ 
/*     */ 
/*     */           
/* 183 */           .regionStart());
/*     */     }
/* 185 */     if (currentStrategy.addRegex(this, regex)) {
/* 186 */       collector.add(currentStrategy);
/*     */     }
/* 188 */     this.currentFormatField = null;
/* 189 */     this.strategies = collector.<Strategy>toArray(new Strategy[collector.size()]);
/* 190 */     this.parsePattern = Pattern.compile(regex.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 199 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 206 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 213 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Pattern getParsePattern() {
/* 222 */     return this.parsePattern;
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
/*     */   public boolean equals(Object obj) {
/* 235 */     if (!(obj instanceof FastDateParser)) {
/* 236 */       return false;
/*     */     }
/* 238 */     FastDateParser other = (FastDateParser)obj;
/* 239 */     return (this.pattern.equals(other.pattern) && this.timeZone
/* 240 */       .equals(other.timeZone) && this.locale
/* 241 */       .equals(other.locale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 251 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 261 */     return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 275 */     in.defaultReadObject();
/*     */     
/* 277 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/* 278 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source) throws ParseException {
/* 285 */     return parse(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source) throws ParseException {
/* 292 */     String normalizedSource = (source.length() == 19) ? (source + ".000") : source;
/* 293 */     Date date = parse(normalizedSource, new ParsePosition(0));
/* 294 */     if (date == null) {
/*     */       
/* 296 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/* 297 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + normalizedSource + "\" does not match " + this.parsePattern
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 304 */             .pattern(), 0);
/*     */       }
/*     */       
/* 307 */       throw new ParseException("Unparseable date: \"" + normalizedSource + "\" does not match " + this.parsePattern
/*     */ 
/*     */ 
/*     */           
/* 311 */           .pattern(), 0);
/*     */     } 
/*     */     
/* 314 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source, ParsePosition pos) {
/* 321 */     return parse(source, pos);
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
/*     */   public Date parse(String source, ParsePosition pos) {
/* 337 */     int offset = pos.getIndex();
/* 338 */     Matcher matcher = this.parsePattern.matcher(source.substring(offset));
/* 339 */     if (!matcher.lookingAt()) {
/* 340 */       return null;
/*     */     }
/*     */     
/* 343 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/* 344 */     cal.clear();
/*     */     
/* 346 */     for (int i = 0; i < this.strategies.length; ) {
/* 347 */       Strategy strategy = this.strategies[i++];
/* 348 */       strategy.setCalendar(this, cal, matcher.group(i));
/*     */     } 
/* 350 */     pos.setIndex(offset + matcher.end());
/* 351 */     return cal.getTime();
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
/*     */   private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
/* 367 */     regex.append("\\Q");
/* 368 */     for (int i = 0; i < value.length(); i++) {
/* 369 */       char c = value.charAt(i);
/* 370 */       switch (c) {
/*     */         case '\'':
/* 372 */           if (unquote) {
/* 373 */             if (++i == value.length()) {
/* 374 */               return regex;
/*     */             }
/* 376 */             c = value.charAt(i);
/*     */           } 
/*     */           break;
/*     */         case '\\':
/* 380 */           if (++i == value.length()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 390 */           regex.append(c);
/* 391 */           c = value.charAt(i);
/* 392 */           if (c == 'E') {
/* 393 */             regex.append("E\\\\E\\");
/* 394 */             c = 'Q';
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 400 */       regex.append(c);
/*     */     } 
/* 402 */     regex.append("\\E");
/* 403 */     return regex;
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
/*     */   private static Map<String, Integer> getDisplayNames(int field, Calendar definingCalendar, Locale locale) {
/* 416 */     return definingCalendar.getDisplayNames(field, 0, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int adjustYear(int twoDigitYear) {
/* 426 */     int trial = this.century + twoDigitYear;
/* 427 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isNextNumber() {
/* 436 */     return (this.nextStrategy != null && this.nextStrategy.isNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getFieldWidth() {
/* 445 */     return this.currentFormatField.length();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Strategy
/*     */   {
/*     */     private Strategy() {}
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 457 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean addRegex(FastDateParser param1FastDateParser, StringBuilder param1StringBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 485 */   private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|X+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getStrategy(String formatField, Calendar definingCalendar) {
/* 496 */     switch (formatField.charAt(0)) {
/*     */       case '\'':
/* 498 */         if (formatField.length() > 2) {
/* 499 */           return new CopyQuotedStrategy(formatField
/* 500 */               .substring(1, formatField.length() - 1));
/*     */         }
/*     */       
/*     */       default:
/* 504 */         return new CopyQuotedStrategy(formatField);
/*     */       case 'D':
/* 506 */         return DAY_OF_YEAR_STRATEGY;
/*     */       case 'E':
/* 508 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*     */       case 'F':
/* 510 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*     */       case 'G':
/* 512 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*     */       case 'H':
/* 514 */         return HOUR_OF_DAY_STRATEGY;
/*     */       case 'K':
/* 516 */         return HOUR_STRATEGY;
/*     */       case 'M':
/* 518 */         return (formatField.length() >= 3) ? 
/* 519 */           getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*     */       
/*     */       case 'S':
/* 522 */         return MILLISECOND_STRATEGY;
/*     */       case 'W':
/* 524 */         return WEEK_OF_MONTH_STRATEGY;
/*     */       case 'a':
/* 526 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*     */       case 'd':
/* 528 */         return DAY_OF_MONTH_STRATEGY;
/*     */       case 'h':
/* 530 */         return HOUR12_STRATEGY;
/*     */       case 'k':
/* 532 */         return HOUR24_OF_DAY_STRATEGY;
/*     */       case 'm':
/* 534 */         return MINUTE_STRATEGY;
/*     */       case 's':
/* 536 */         return SECOND_STRATEGY;
/*     */       case 'w':
/* 538 */         return WEEK_OF_YEAR_STRATEGY;
/*     */       case 'y':
/* 540 */         return (formatField.length() > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*     */       case 'X':
/* 542 */         return ISO8601TimeZoneStrategy.getStrategy(formatField.length());
/*     */       case 'Z':
/* 544 */         if (formatField.equals("ZZ"))
/* 545 */           return ISO_8601_STRATEGY;  break;
/*     */       case 'z':
/*     */         break;
/*     */     } 
/* 549 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 554 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/* 564 */     synchronized (caches) {
/* 565 */       if (caches[field] == null) {
/* 566 */         caches[field] = new ConcurrentHashMap<>(3);
/*     */       }
/* 568 */       return caches[field];
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
/*     */   private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
/* 580 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/* 581 */     Strategy strategy = cache.get(this.locale);
/* 582 */     if (strategy == null) {
/* 583 */       strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
/*     */ 
/*     */ 
/*     */       
/* 587 */       Strategy inCache = cache.putIfAbsent(this.locale, strategy);
/* 588 */       if (inCache != null) {
/* 589 */         return inCache;
/*     */       }
/*     */     } 
/* 592 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CopyQuotedStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String formatField;
/*     */ 
/*     */ 
/*     */     
/*     */     CopyQuotedStrategy(String formatField) {
/* 605 */       this.formatField = formatField;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 611 */       char c = this.formatField.charAt(0);
/* 612 */       if (c == '\'') {
/* 613 */         c = this.formatField.charAt(1);
/*     */       }
/* 615 */       return Character.isDigit(c);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 621 */       FastDateParser.escapeRegex(regex, this.formatField, true);
/* 622 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CaseInsensitiveTextStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */     
/*     */     private final Locale locale;
/*     */ 
/*     */     
/*     */     private final Map<String, Integer> lKeyValues;
/*     */ 
/*     */     
/*     */     CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
/* 641 */       this.field = field;
/* 642 */       this.locale = locale;
/* 643 */       Map<String, Integer> keyValues = FastDateParser.getDisplayNames(field, definingCalendar, locale);
/* 644 */       this.lKeyValues = new HashMap<>();
/*     */       
/* 646 */       for (Map.Entry<String, Integer> entry : keyValues.entrySet()) {
/* 647 */         this.lKeyValues.put(((String)entry.getKey()).toLowerCase(locale), entry.getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 654 */       regex.append("((?iu)");
/* 655 */       for (String textKeyValue : this.lKeyValues.keySet()) {
/* 656 */         FastDateParser.escapeRegex(regex, textKeyValue, false).append('|');
/*     */       }
/* 658 */       regex.setCharAt(regex.length() - 1, ')');
/* 659 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 665 */       Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
/* 666 */       if (iVal == null) {
/* 667 */         StringBuilder sb = new StringBuilder(value);
/* 668 */         sb.append(" not in (");
/* 669 */         for (String textKeyValue : this.lKeyValues.keySet()) {
/* 670 */           sb.append(textKeyValue).append(' ');
/*     */         }
/* 672 */         sb.setCharAt(sb.length() - 1, ')');
/* 673 */         throw new IllegalArgumentException(sb.toString());
/*     */       } 
/* 675 */       cal.set(this.field, iVal.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NumberStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */ 
/*     */     
/*     */     NumberStrategy(int field) {
/* 689 */       this.field = field;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 695 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 703 */       if (parser.isNextNumber()) {
/* 704 */         regex.append("(\\p{Nd}{").append(parser.getFieldWidth()).append("}+)");
/*     */       } else {
/* 706 */         regex.append("(\\p{Nd}++)");
/*     */       } 
/* 708 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 714 */       cal.set(this.field, modify(Integer.parseInt(value)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int modify(int iValue) {
/* 724 */       return iValue;
/*     */     }
/*     */   }
/*     */   
/* 728 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*     */     {
/*     */ 
/*     */       
/*     */       void setCalendar(FastDateParser parser, Calendar cal, String value)
/*     */       {
/* 734 */         int iValue = Integer.parseInt(value);
/* 735 */         if (iValue < 100) {
/* 736 */           iValue = parser.adjustYear(iValue);
/*     */         }
/* 738 */         cal.set(1, iValue);
/*     */       }
/*     */     };
/*     */   
/*     */   private static class TimeZoneStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String validTimeZoneChars;
/* 746 */     private final SortedMap<String, TimeZone> tzNames = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/*     */     
/*     */     private static final int ID = 0;
/*     */ 
/*     */     
/*     */     private static final int LONG_STD = 1;
/*     */ 
/*     */     
/*     */     private static final int SHORT_STD = 2;
/*     */ 
/*     */     
/*     */     private static final int LONG_DST = 3;
/*     */ 
/*     */     
/*     */     private static final int SHORT_DST = 4;
/*     */ 
/*     */ 
/*     */     
/*     */     TimeZoneStrategy(Locale locale) {
/* 766 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/* 767 */       for (String[] zone : zones) {
/* 768 */         if (!zone[0].startsWith("GMT")) {
/*     */ 
/*     */           
/* 771 */           TimeZone tz = TimeZone.getTimeZone(zone[0]);
/* 772 */           if (!this.tzNames.containsKey(zone[1])) {
/* 773 */             this.tzNames.put(zone[1], tz);
/*     */           }
/* 775 */           if (!this.tzNames.containsKey(zone[2])) {
/* 776 */             this.tzNames.put(zone[2], tz);
/*     */           }
/* 778 */           if (tz.useDaylightTime()) {
/* 779 */             if (!this.tzNames.containsKey(zone[3])) {
/* 780 */               this.tzNames.put(zone[3], tz);
/*     */             }
/* 782 */             if (!this.tzNames.containsKey(zone[4])) {
/* 783 */               this.tzNames.put(zone[4], tz);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 788 */       StringBuilder sb = new StringBuilder();
/* 789 */       sb.append("(GMT[+-]\\d{1,2}:\\d{2}").append('|');
/* 790 */       sb.append("[+-]\\d{4}").append('|');
/* 791 */       for (String id : this.tzNames.keySet()) {
/* 792 */         FastDateParser.escapeRegex(sb, id, false).append('|');
/*     */       }
/* 794 */       sb.setCharAt(sb.length() - 1, ')');
/* 795 */       this.validTimeZoneChars = sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 801 */       regex.append(this.validTimeZoneChars);
/* 802 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/*     */       TimeZone tz;
/* 809 */       if (value.charAt(0) == '+' || value.charAt(0) == '-') {
/* 810 */         tz = TimeZone.getTimeZone("GMT" + value);
/* 811 */       } else if (value.startsWith("GMT")) {
/* 812 */         tz = TimeZone.getTimeZone(value);
/*     */       } else {
/* 814 */         tz = this.tzNames.get(value);
/* 815 */         if (tz == null) {
/* 816 */           throw new IllegalArgumentException(value + " is not a supported timezone name");
/*     */         }
/*     */       } 
/* 819 */       cal.setTimeZone(tz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ISO8601TimeZoneStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String pattern;
/*     */ 
/*     */ 
/*     */     
/*     */     ISO8601TimeZoneStrategy(String pattern) {
/* 833 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 839 */       regex.append(this.pattern);
/* 840 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 846 */       if (value.equals("Z")) {
/* 847 */         cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       } else {
/* 849 */         cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
/*     */       } 
/*     */     }
/*     */     
/* 853 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/*     */     
/* 855 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/*     */     
/* 857 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 868 */       switch (tokenLen) {
/*     */         case 1:
/* 870 */           return ISO_8601_1_STRATEGY;
/*     */         case 2:
/* 872 */           return ISO_8601_2_STRATEGY;
/*     */         case 3:
/* 874 */           return ISO_8601_3_STRATEGY;
/*     */       } 
/* 876 */       throw new IllegalArgumentException("invalid number of X");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 881 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*     */     {
/*     */       int modify(int iValue)
/*     */       {
/* 885 */         return iValue - 1;
/*     */       }
/*     */     };
/* 888 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 889 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 890 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/*     */   
/* 892 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 893 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 894 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/*     */   
/* 896 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 897 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*     */     {
/*     */       int modify(int iValue)
/*     */       {
/* 901 */         return (iValue == 24) ? 0 : iValue;
/*     */       }
/*     */     };
/* 904 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*     */     {
/*     */       int modify(int iValue)
/*     */       {
/* 908 */         return (iValue == 12) ? 0 : iValue;
/*     */       }
/*     */     };
/* 911 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 912 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 913 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 914 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/* 915 */   private static final Strategy ISO_8601_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::?\\d{2})?))");
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\date\FastDateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */