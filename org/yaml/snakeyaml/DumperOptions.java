/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.serializer.AnchorGenerator;
/*     */ import org.yaml.snakeyaml.serializer.NumberAnchorGenerator;
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
/*     */ public class DumperOptions
/*     */ {
/*     */   public enum ScalarStyle
/*     */   {
/*  35 */     DOUBLE_QUOTED((String)Character.valueOf('"')), SINGLE_QUOTED((String)Character.valueOf('\'')), LITERAL((String)Character.valueOf('|')), FOLDED((String)Character.valueOf('>')), PLAIN(null);
/*     */     
/*     */     private final Character styleChar;
/*     */     
/*     */     ScalarStyle(Character style) {
/*  40 */       this.styleChar = style;
/*     */     }
/*     */     
/*     */     public Character getChar() {
/*  44 */       return this.styleChar;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  49 */       return "Scalar style: '" + this.styleChar + "'";
/*     */     }
/*     */     
/*     */     public static ScalarStyle createStyle(Character style) {
/*  53 */       if (style == null) {
/*  54 */         return PLAIN;
/*     */       }
/*  56 */       switch (style.charValue()) {
/*     */         case '"':
/*  58 */           return DOUBLE_QUOTED;
/*     */         case '\'':
/*  60 */           return SINGLE_QUOTED;
/*     */         case '|':
/*  62 */           return LITERAL;
/*     */         case '>':
/*  64 */           return FOLDED;
/*     */       } 
/*  66 */       throw new YAMLException("Unknown scalar style character: " + style);
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
/*     */   public enum FlowStyle
/*     */   {
/*  80 */     FLOW((String)Boolean.TRUE), BLOCK((String)Boolean.FALSE), AUTO(null);
/*     */     
/*     */     private final Boolean styleBoolean;
/*     */     
/*     */     FlowStyle(Boolean flowStyle) {
/*  85 */       this.styleBoolean = flowStyle;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public static FlowStyle fromBoolean(Boolean flowStyle) {
/*  97 */       return (flowStyle == null) ? AUTO : (flowStyle.booleanValue() ? FLOW : BLOCK);
/*     */     }
/*     */     
/*     */     public Boolean getStyleBoolean() {
/* 101 */       return this.styleBoolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 106 */       return "Flow style: '" + this.styleBoolean + "'";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum LineBreak
/*     */   {
/* 114 */     WIN("\r\n"), MAC("\r"), UNIX("\n");
/*     */     
/*     */     private final String lineBreak;
/*     */     
/*     */     LineBreak(String lineBreak) {
/* 119 */       this.lineBreak = lineBreak;
/*     */     }
/*     */     
/*     */     public String getString() {
/* 123 */       return this.lineBreak;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 128 */       return "Line break: " + name();
/*     */     }
/*     */     
/*     */     public static LineBreak getPlatformLineBreak() {
/* 132 */       String platformLineBreak = System.getProperty("line.separator");
/* 133 */       for (LineBreak lb : values()) {
/* 134 */         if (lb.lineBreak.equals(platformLineBreak)) {
/* 135 */           return lb;
/*     */         }
/*     */       } 
/* 138 */       return UNIX;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Version
/*     */   {
/* 146 */     V1_0((String)new Integer[] { Integer.valueOf(1), Integer.valueOf(0) }), V1_1((String)new Integer[] { Integer.valueOf(1), Integer.valueOf(1) });
/*     */     
/*     */     private final Integer[] version;
/*     */     
/*     */     Version(Integer[] version) {
/* 151 */       this.version = version;
/*     */     }
/*     */     
/*     */     public int major() {
/* 155 */       return this.version[0].intValue();
/*     */     }
/*     */     
/*     */     public int minor() {
/* 159 */       return this.version[1].intValue();
/*     */     }
/*     */     
/*     */     public String getRepresentation() {
/* 163 */       return this.version[0] + "." + this.version[1];
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 168 */       return "Version: " + getRepresentation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum NonPrintableStyle
/*     */   {
/* 176 */     BINARY,
/*     */ 
/*     */ 
/*     */     
/* 180 */     ESCAPE;
/*     */   }
/*     */   
/* 183 */   private ScalarStyle defaultStyle = ScalarStyle.PLAIN;
/* 184 */   private FlowStyle defaultFlowStyle = FlowStyle.AUTO;
/*     */   private boolean canonical = false;
/*     */   private boolean allowUnicode = true;
/*     */   private boolean allowReadOnlyProperties = false;
/* 188 */   private int indent = 2;
/* 189 */   private int indicatorIndent = 0;
/*     */   private boolean indentWithIndicator = false;
/* 191 */   private int bestWidth = 80;
/*     */   private boolean splitLines = true;
/* 193 */   private LineBreak lineBreak = LineBreak.UNIX;
/*     */   private boolean explicitStart = false;
/*     */   private boolean explicitEnd = false;
/* 196 */   private TimeZone timeZone = null;
/* 197 */   private int maxSimpleKeyLength = 128;
/*     */   private boolean processComments = false;
/* 199 */   private NonPrintableStyle nonPrintableStyle = NonPrintableStyle.BINARY;
/*     */   
/* 201 */   private Version version = null;
/* 202 */   private Map<String, String> tags = null;
/* 203 */   private Boolean prettyFlow = Boolean.valueOf(false);
/* 204 */   private AnchorGenerator anchorGenerator = (AnchorGenerator)new NumberAnchorGenerator(0);
/*     */   
/*     */   public boolean isAllowUnicode() {
/* 207 */     return this.allowUnicode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowUnicode(boolean allowUnicode) {
/* 218 */     this.allowUnicode = allowUnicode;
/*     */   }
/*     */   
/*     */   public ScalarStyle getDefaultScalarStyle() {
/* 222 */     return this.defaultStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultScalarStyle(ScalarStyle defaultStyle) {
/* 232 */     if (defaultStyle == null) {
/* 233 */       throw new NullPointerException("Use ScalarStyle enum.");
/*     */     }
/* 235 */     this.defaultStyle = defaultStyle;
/*     */   }
/*     */   
/*     */   public void setIndent(int indent) {
/* 239 */     if (indent < 1) {
/* 240 */       throw new YAMLException("Indent must be at least 1");
/*     */     }
/* 242 */     if (indent > 10) {
/* 243 */       throw new YAMLException("Indent must be at most 10");
/*     */     }
/* 245 */     this.indent = indent;
/*     */   }
/*     */   
/*     */   public int getIndent() {
/* 249 */     return this.indent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndicatorIndent(int indicatorIndent) {
/* 258 */     if (indicatorIndent < 0) {
/* 259 */       throw new YAMLException("Indicator indent must be non-negative.");
/*     */     }
/* 261 */     if (indicatorIndent > 9) {
/* 262 */       throw new YAMLException("Indicator indent must be at most Emitter.MAX_INDENT-1: 9");
/*     */     }
/*     */     
/* 265 */     this.indicatorIndent = indicatorIndent;
/*     */   }
/*     */   
/*     */   public int getIndicatorIndent() {
/* 269 */     return this.indicatorIndent;
/*     */   }
/*     */   
/*     */   public boolean getIndentWithIndicator() {
/* 273 */     return this.indentWithIndicator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentWithIndicator(boolean indentWithIndicator) {
/* 282 */     this.indentWithIndicator = indentWithIndicator;
/*     */   }
/*     */   
/*     */   public void setVersion(Version version) {
/* 286 */     this.version = version;
/*     */   }
/*     */   
/*     */   public Version getVersion() {
/* 290 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanonical(boolean canonical) {
/* 299 */     this.canonical = canonical;
/*     */   }
/*     */   
/*     */   public boolean isCanonical() {
/* 303 */     return this.canonical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrettyFlow(boolean prettyFlow) {
/* 312 */     this.prettyFlow = Boolean.valueOf(prettyFlow);
/*     */   }
/*     */   
/*     */   public boolean isPrettyFlow() {
/* 316 */     return this.prettyFlow.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWidth(int bestWidth) {
/* 326 */     this.bestWidth = bestWidth;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 330 */     return this.bestWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSplitLines(boolean splitLines) {
/* 339 */     this.splitLines = splitLines;
/*     */   }
/*     */   
/*     */   public boolean getSplitLines() {
/* 343 */     return this.splitLines;
/*     */   }
/*     */   
/*     */   public LineBreak getLineBreak() {
/* 347 */     return this.lineBreak;
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(FlowStyle defaultFlowStyle) {
/* 351 */     if (defaultFlowStyle == null) {
/* 352 */       throw new NullPointerException("Use FlowStyle enum.");
/*     */     }
/* 354 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public FlowStyle getDefaultFlowStyle() {
/* 358 */     return this.defaultFlowStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineBreak(LineBreak lineBreak) {
/* 368 */     if (lineBreak == null) {
/* 369 */       throw new NullPointerException("Specify line break.");
/*     */     }
/* 371 */     this.lineBreak = lineBreak;
/*     */   }
/*     */   
/*     */   public boolean isExplicitStart() {
/* 375 */     return this.explicitStart;
/*     */   }
/*     */   
/*     */   public void setExplicitStart(boolean explicitStart) {
/* 379 */     this.explicitStart = explicitStart;
/*     */   }
/*     */   
/*     */   public boolean isExplicitEnd() {
/* 383 */     return this.explicitEnd;
/*     */   }
/*     */   
/*     */   public void setExplicitEnd(boolean explicitEnd) {
/* 387 */     this.explicitEnd = explicitEnd;
/*     */   }
/*     */   
/*     */   public Map<String, String> getTags() {
/* 391 */     return this.tags;
/*     */   }
/*     */   
/*     */   public void setTags(Map<String, String> tags) {
/* 395 */     this.tags = tags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllowReadOnlyProperties() {
/* 405 */     return this.allowReadOnlyProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
/* 416 */     this.allowReadOnlyProperties = allowReadOnlyProperties;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 420 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 429 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnchorGenerator getAnchorGenerator() {
/* 434 */     return this.anchorGenerator;
/*     */   }
/*     */   
/*     */   public void setAnchorGenerator(AnchorGenerator anchorGenerator) {
/* 438 */     this.anchorGenerator = anchorGenerator;
/*     */   }
/*     */   
/*     */   public int getMaxSimpleKeyLength() {
/* 442 */     return this.maxSimpleKeyLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxSimpleKeyLength(int maxSimpleKeyLength) {
/* 452 */     if (maxSimpleKeyLength > 1024) {
/* 453 */       throw new YAMLException("The simple key must not span more than 1024 stream characters. See https://yaml.org/spec/1.1/#id934537");
/*     */     }
/*     */     
/* 456 */     this.maxSimpleKeyLength = maxSimpleKeyLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessComments(boolean processComments) {
/* 466 */     this.processComments = processComments;
/*     */   }
/*     */   
/*     */   public boolean isProcessComments() {
/* 470 */     return this.processComments;
/*     */   }
/*     */   
/*     */   public NonPrintableStyle getNonPrintableStyle() {
/* 474 */     return this.nonPrintableStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNonPrintableStyle(NonPrintableStyle style) {
/* 485 */     this.nonPrintableStyle = style;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\DumperOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */