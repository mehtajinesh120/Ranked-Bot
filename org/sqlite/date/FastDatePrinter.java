/*      */ package org.sqlite.date;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.FieldPosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FastDatePrinter
/*      */   implements DatePrinter, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   public static final int FULL = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int MEDIUM = 2;
/*      */   public static final int SHORT = 3;
/*      */   private final String mPattern;
/*      */   private final TimeZone mTimeZone;
/*      */   private final Locale mLocale;
/*      */   private transient Rule[] mRules;
/*      */   private transient int mMaxLengthEstimate;
/*      */   
/*      */   protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
/*  127 */     this.mPattern = pattern;
/*  128 */     this.mTimeZone = timeZone;
/*  129 */     this.mLocale = locale;
/*      */     
/*  131 */     init();
/*      */   }
/*      */ 
/*      */   
/*      */   private void init() {
/*  136 */     List<Rule> rulesList = parsePattern();
/*  137 */     this.mRules = rulesList.<Rule>toArray(new Rule[rulesList.size()]);
/*      */     
/*  139 */     int len = 0;
/*  140 */     for (int i = this.mRules.length; --i >= 0;) {
/*  141 */       len += this.mRules[i].estimateLength();
/*      */     }
/*      */     
/*  144 */     this.mMaxLengthEstimate = len;
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
/*      */   protected List<Rule> parsePattern() {
/*  156 */     DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
/*  157 */     List<Rule> rules = new ArrayList<>();
/*      */     
/*  159 */     String[] ERAs = symbols.getEras();
/*  160 */     String[] months = symbols.getMonths();
/*  161 */     String[] shortMonths = symbols.getShortMonths();
/*  162 */     String[] weekdays = symbols.getWeekdays();
/*  163 */     String[] shortWeekdays = symbols.getShortWeekdays();
/*  164 */     String[] AmPmStrings = symbols.getAmPmStrings();
/*      */     
/*  166 */     int length = this.mPattern.length();
/*  167 */     int[] indexRef = new int[1];
/*      */     
/*  169 */     for (int i = 0; i < length; i++) {
/*  170 */       Rule rule; String sub; indexRef[0] = i;
/*  171 */       String token = parseToken(this.mPattern, indexRef);
/*  172 */       i = indexRef[0];
/*      */       
/*  174 */       int tokenLen = token.length();
/*  175 */       if (tokenLen == 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  180 */       char c = token.charAt(0);
/*      */       
/*  182 */       switch (c) {
/*      */         case 'G':
/*  184 */           rule = new TextField(0, ERAs);
/*      */           break;
/*      */         case 'y':
/*  187 */           if (tokenLen == 2) {
/*  188 */             rule = TwoDigitYearField.INSTANCE; break;
/*      */           } 
/*  190 */           rule = selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
/*      */           break;
/*      */         
/*      */         case 'M':
/*  194 */           if (tokenLen >= 4) {
/*  195 */             rule = new TextField(2, months); break;
/*  196 */           }  if (tokenLen == 3) {
/*  197 */             rule = new TextField(2, shortMonths); break;
/*  198 */           }  if (tokenLen == 2) {
/*  199 */             rule = TwoDigitMonthField.INSTANCE; break;
/*      */           } 
/*  201 */           rule = UnpaddedMonthField.INSTANCE;
/*      */           break;
/*      */         
/*      */         case 'd':
/*  205 */           rule = selectNumberRule(5, tokenLen);
/*      */           break;
/*      */         case 'h':
/*  208 */           rule = new TwelveHourField(selectNumberRule(10, tokenLen));
/*      */           break;
/*      */         case 'H':
/*  211 */           rule = selectNumberRule(11, tokenLen);
/*      */           break;
/*      */         case 'm':
/*  214 */           rule = selectNumberRule(12, tokenLen);
/*      */           break;
/*      */         case 's':
/*  217 */           rule = selectNumberRule(13, tokenLen);
/*      */           break;
/*      */         case 'S':
/*  220 */           rule = selectNumberRule(14, tokenLen);
/*      */           break;
/*      */         case 'E':
/*  223 */           rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 'D':
/*  228 */           rule = selectNumberRule(6, tokenLen);
/*      */           break;
/*      */         case 'F':
/*  231 */           rule = selectNumberRule(8, tokenLen);
/*      */           break;
/*      */         case 'w':
/*  234 */           rule = selectNumberRule(3, tokenLen);
/*      */           break;
/*      */         case 'W':
/*  237 */           rule = selectNumberRule(4, tokenLen);
/*      */           break;
/*      */         case 'a':
/*  240 */           rule = new TextField(9, AmPmStrings);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 'k':
/*  245 */           rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
/*      */           break;
/*      */         case 'K':
/*  248 */           rule = selectNumberRule(10, tokenLen);
/*      */           break;
/*      */         case 'X':
/*  251 */           rule = Iso8601_Rule.getRule(tokenLen);
/*      */           break;
/*      */         case 'z':
/*  254 */           if (tokenLen >= 4) {
/*  255 */             rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1); break;
/*      */           } 
/*  257 */           rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
/*      */           break;
/*      */         
/*      */         case 'Z':
/*  261 */           if (tokenLen == 1) {
/*  262 */             rule = TimeZoneNumberRule.INSTANCE_NO_COLON; break;
/*  263 */           }  if (tokenLen == 2) {
/*  264 */             rule = TimeZoneNumberRule.INSTANCE_ISO_8601; break;
/*      */           } 
/*  266 */           rule = TimeZoneNumberRule.INSTANCE_COLON;
/*      */           break;
/*      */         
/*      */         case '\'':
/*  270 */           sub = token.substring(1);
/*  271 */           if (sub.length() == 1) {
/*  272 */             rule = new CharacterLiteral(sub.charAt(0)); break;
/*      */           } 
/*  274 */           rule = new StringLiteral(sub);
/*      */           break;
/*      */         
/*      */         default:
/*  278 */           throw new IllegalArgumentException("Illegal pattern component: " + token);
/*      */       } 
/*      */       
/*  281 */       rules.add(rule);
/*      */     } 
/*      */     
/*  284 */     return rules;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String parseToken(String pattern, int[] indexRef) {
/*  295 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  297 */     int i = indexRef[0];
/*  298 */     int length = pattern.length();
/*      */     
/*  300 */     char c = pattern.charAt(i);
/*  301 */     if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
/*      */ 
/*      */       
/*  304 */       buf.append(c);
/*      */       
/*  306 */       while (i + 1 < length) {
/*  307 */         char peek = pattern.charAt(i + 1);
/*  308 */         if (peek == c) {
/*  309 */           buf.append(c);
/*  310 */           i++;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  317 */       buf.append('\'');
/*      */       
/*  319 */       boolean inLiteral = false;
/*      */       
/*  321 */       for (; i < length; i++) {
/*  322 */         c = pattern.charAt(i);
/*      */         
/*  324 */         if (c == '\'')
/*  325 */         { if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
/*      */             
/*  327 */             i++;
/*  328 */             buf.append(c);
/*      */           } else {
/*  330 */             inLiteral = !inLiteral;
/*      */           }  }
/*  332 */         else { if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
/*  333 */             i--;
/*      */             break;
/*      */           } 
/*  336 */           buf.append(c); }
/*      */       
/*      */       } 
/*      */     } 
/*      */     
/*  341 */     indexRef[0] = i;
/*  342 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NumberRule selectNumberRule(int field, int padding) {
/*  353 */     switch (padding) {
/*      */       case 1:
/*  355 */         return new UnpaddedNumberField(field);
/*      */       case 2:
/*  357 */         return new TwoDigitNumberField(field);
/*      */     } 
/*  359 */     return new PaddedNumberField(field, padding);
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
/*      */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/*  375 */     if (obj instanceof Date)
/*  376 */       return format((Date)obj, toAppendTo); 
/*  377 */     if (obj instanceof Calendar)
/*  378 */       return format((Calendar)obj, toAppendTo); 
/*  379 */     if (obj instanceof Long) {
/*  380 */       return format(((Long)obj).longValue(), toAppendTo);
/*      */     }
/*  382 */     throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj
/*  383 */         .getClass().getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(long millis) {
/*  391 */     Calendar c = newCalendar();
/*  392 */     c.setTimeInMillis(millis);
/*  393 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String applyRulesToString(Calendar c) {
/*  404 */     return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private GregorianCalendar newCalendar() {
/*  414 */     return new GregorianCalendar(this.mTimeZone, this.mLocale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Date date) {
/*  421 */     Calendar c = newCalendar();
/*  422 */     c.setTime(date);
/*  423 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Calendar calendar) {
/*  430 */     return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(long millis, StringBuffer buf) {
/*  437 */     return format(new Date(millis), buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Date date, StringBuffer buf) {
/*  444 */     Calendar c = newCalendar();
/*  445 */     c.setTime(date);
/*  446 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Calendar calendar, StringBuffer buf) {
/*  453 */     return applyRules(calendar, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
/*  464 */     for (Rule rule : this.mRules) {
/*  465 */       rule.appendTo(buf, calendar);
/*      */     }
/*  467 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPattern() {
/*  476 */     return this.mPattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  483 */     return this.mTimeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  490 */     return this.mLocale;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxLengthEstimate() {
/*  501 */     return this.mMaxLengthEstimate;
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
/*      */   public boolean equals(Object obj) {
/*  514 */     if (!(obj instanceof FastDatePrinter)) {
/*  515 */       return false;
/*      */     }
/*  517 */     FastDatePrinter other = (FastDatePrinter)obj;
/*  518 */     return (this.mPattern.equals(other.mPattern) && this.mTimeZone
/*  519 */       .equals(other.mTimeZone) && this.mLocale
/*  520 */       .equals(other.mLocale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  530 */     return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  540 */     return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
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
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  554 */     in.defaultReadObject();
/*  555 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendDigits(StringBuffer buffer, int value) {
/*  565 */     buffer.append((char)(value / 10 + 48));
/*  566 */     buffer.append((char)(value % 10 + 48));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface Rule
/*      */   {
/*      */     int estimateLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void appendTo(StringBuffer param1StringBuffer, Calendar param1Calendar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface NumberRule
/*      */     extends Rule
/*      */   {
/*      */     void appendTo(StringBuffer param1StringBuffer, int param1Int);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CharacterLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final char mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CharacterLiteral(char value) {
/*  611 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  616 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  621 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StringLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final String mValue;
/*      */ 
/*      */ 
/*      */     
/*      */     StringLiteral(String value) {
/*  635 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  640 */       return this.mValue.length();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  645 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TextField
/*      */     implements Rule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */     
/*      */     private final String[] mValues;
/*      */ 
/*      */     
/*      */     TextField(int field, String[] values) {
/*  661 */       this.mField = field;
/*  662 */       this.mValues = values;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  667 */       int max = 0;
/*  668 */       for (int i = this.mValues.length; --i >= 0; ) {
/*  669 */         int len = this.mValues[i].length();
/*  670 */         if (len > max) {
/*  671 */           max = len;
/*      */         }
/*      */       } 
/*  674 */       return max;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  679 */       buffer.append(this.mValues[calendar.get(this.mField)]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class UnpaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */     
/*      */     UnpaddedNumberField(int field) {
/*  693 */       this.mField = field;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  698 */       return 4;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  703 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  708 */       if (value < 10) {
/*  709 */         buffer.append((char)(value + 48));
/*  710 */       } else if (value < 100) {
/*  711 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  713 */         buffer.append(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnpaddedMonthField
/*      */     implements NumberRule {
/*  720 */     static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  729 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  734 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  739 */       if (value < 10) {
/*  740 */         buffer.append((char)(value + 48));
/*      */       } else {
/*  742 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class PaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */     
/*      */     private final int mSize;
/*      */ 
/*      */     
/*      */     PaddedNumberField(int field, int size) {
/*  759 */       if (size < 3)
/*      */       {
/*  761 */         throw new IllegalArgumentException();
/*      */       }
/*  763 */       this.mField = field;
/*  764 */       this.mSize = size;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  769 */       return this.mSize;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  774 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  780 */       for (int digit = 0; digit < this.mSize; digit++) {
/*  781 */         buffer.append('0');
/*      */       }
/*      */       
/*  784 */       int index = buffer.length();
/*  785 */       for (; value > 0; value /= 10) {
/*  786 */         buffer.setCharAt(--index, (char)(48 + value % 10));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwoDigitNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */     
/*      */     TwoDigitNumberField(int field) {
/*  801 */       this.mField = field;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  806 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  811 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  816 */       if (value < 100) {
/*  817 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  819 */         buffer.append(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TwoDigitYearField
/*      */     implements NumberRule {
/*  826 */     static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  835 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  840 */       appendTo(buffer, calendar.get(1) % 100);
/*      */     }
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  845 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TwoDigitMonthField
/*      */     implements NumberRule {
/*  851 */     static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  860 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  865 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  870 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwelveHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */     
/*      */     TwelveHourField(FastDatePrinter.NumberRule rule) {
/*  884 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  889 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  894 */       int value = calendar.get(10);
/*  895 */       if (value == 0) {
/*  896 */         value = calendar.getLeastMaximum(10) + 1;
/*      */       }
/*  898 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, int value) {
/*  903 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwentyFourHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */     
/*      */     TwentyFourHourField(FastDatePrinter.NumberRule rule) {
/*  918 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  923 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  928 */       int value = calendar.get(11);
/*  929 */       if (value == 0) {
/*  930 */         value = calendar.getMaximum(11) + 1;
/*      */       }
/*  932 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, int value) {
/*  937 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  943 */   private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<>(7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
/*  956 */     TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
/*  957 */     String value = cTimeZoneDisplayCache.get(key);
/*  958 */     if (value == null) {
/*      */       
/*  960 */       value = tz.getDisplayName(daylight, style, locale);
/*  961 */       String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
/*  962 */       if (prior != null) {
/*  963 */         value = prior;
/*      */       }
/*      */     } 
/*  966 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNameRule
/*      */     implements Rule
/*      */   {
/*      */     private final Locale mLocale;
/*      */     
/*      */     private final int mStyle;
/*      */     
/*      */     private final String mStandard;
/*      */     
/*      */     private final String mDaylight;
/*      */ 
/*      */     
/*      */     TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
/*  984 */       this.mLocale = locale;
/*  985 */       this.mStyle = style;
/*      */       
/*  987 */       this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
/*  988 */       this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  996 */       return Math.max(this.mStandard.length(), this.mDaylight.length());
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1001 */       TimeZone zone = calendar.getTimeZone();
/* 1002 */       if (calendar.get(16) != 0) {
/* 1003 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
/*      */       } else {
/* 1005 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TimeZoneNumberRule
/*      */     implements Rule {
/* 1012 */     static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true, false);
/* 1013 */     static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false, false);
/* 1014 */     static final TimeZoneNumberRule INSTANCE_ISO_8601 = new TimeZoneNumberRule(true, true);
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean mColon;
/*      */ 
/*      */     
/*      */     final boolean mISO8601;
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneNumberRule(boolean colon, boolean iso8601) {
/* 1026 */       this.mColon = colon;
/* 1027 */       this.mISO8601 = iso8601;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1032 */       return 5;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1037 */       if (this.mISO8601 && calendar.getTimeZone().getID().equals("UTC")) {
/* 1038 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1042 */       int offset = calendar.get(15) + calendar.get(16);
/*      */       
/* 1044 */       if (offset < 0) {
/* 1045 */         buffer.append('-');
/* 1046 */         offset = -offset;
/*      */       } else {
/* 1048 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1051 */       int hours = offset / 3600000;
/* 1052 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1054 */       if (this.mColon) {
/* 1055 */         buffer.append(':');
/*      */       }
/*      */       
/* 1058 */       int minutes = offset / 60000 - 60 * hours;
/* 1059 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Iso8601_Rule
/*      */     implements Rule
/*      */   {
/* 1067 */     static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
/*      */     
/* 1069 */     static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
/*      */     
/* 1071 */     static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
/*      */ 
/*      */ 
/*      */     
/*      */     final int length;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static Iso8601_Rule getRule(int tokenLen) {
/* 1081 */       switch (tokenLen) {
/*      */         case 1:
/* 1083 */           return ISO8601_HOURS;
/*      */         case 2:
/* 1085 */           return ISO8601_HOURS_MINUTES;
/*      */         case 3:
/* 1087 */           return ISO8601_HOURS_COLON_MINUTES;
/*      */       } 
/* 1089 */       throw new IllegalArgumentException("invalid number of X");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Iso8601_Rule(int length) {
/* 1101 */       this.length = length;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1106 */       return this.length;
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1111 */       int zoneOffset = calendar.get(15);
/* 1112 */       if (zoneOffset == 0) {
/* 1113 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1117 */       int offset = zoneOffset + calendar.get(16);
/*      */       
/* 1119 */       if (offset < 0) {
/* 1120 */         buffer.append('-');
/* 1121 */         offset = -offset;
/*      */       } else {
/* 1123 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1126 */       int hours = offset / 3600000;
/* 1127 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1129 */       if (this.length < 5) {
/*      */         return;
/*      */       }
/*      */       
/* 1133 */       if (this.length == 6) {
/* 1134 */         buffer.append(':');
/*      */       }
/*      */       
/* 1137 */       int minutes = offset / 60000 - 60 * hours;
/* 1138 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneDisplayKey
/*      */   {
/*      */     private final TimeZone mTimeZone;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Locale mLocale;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
/* 1162 */       this.mTimeZone = timeZone;
/* 1163 */       if (daylight) {
/* 1164 */         this.mStyle = style | Integer.MIN_VALUE;
/*      */       } else {
/* 1166 */         this.mStyle = style;
/*      */       } 
/* 1168 */       this.mLocale = locale;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1174 */       return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1180 */       if (this == obj) {
/* 1181 */         return true;
/*      */       }
/* 1183 */       if (obj instanceof TimeZoneDisplayKey) {
/* 1184 */         TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
/* 1185 */         return (this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale
/*      */           
/* 1187 */           .equals(other.mLocale));
/*      */       } 
/* 1189 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\date\FastDatePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */