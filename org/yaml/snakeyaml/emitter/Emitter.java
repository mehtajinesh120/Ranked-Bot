/*      */ package org.yaml.snakeyaml.emitter;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.comments.CommentEventsCollector;
/*      */ import org.yaml.snakeyaml.comments.CommentLine;
/*      */ import org.yaml.snakeyaml.comments.CommentType;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.events.CollectionStartEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*      */ import org.yaml.snakeyaml.events.Event;
/*      */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*      */ import org.yaml.snakeyaml.events.NodeEvent;
/*      */ import org.yaml.snakeyaml.events.ScalarEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.scanner.Constant;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Emitter
/*      */   implements Emitable
/*      */ {
/*      */   public static final int MIN_INDENT = 1;
/*      */   public static final int MAX_INDENT = 10;
/*   72 */   private static final char[] SPACE = new char[] { ' ' };
/*      */   
/*   74 */   private static final Pattern SPACES_PATTERN = Pattern.compile("\\s");
/*   75 */   private static final Set<Character> INVALID_ANCHOR = new HashSet<>();
/*      */   
/*      */   static {
/*   78 */     INVALID_ANCHOR.add(Character.valueOf('['));
/*   79 */     INVALID_ANCHOR.add(Character.valueOf(']'));
/*   80 */     INVALID_ANCHOR.add(Character.valueOf('{'));
/*   81 */     INVALID_ANCHOR.add(Character.valueOf('}'));
/*   82 */     INVALID_ANCHOR.add(Character.valueOf(','));
/*   83 */     INVALID_ANCHOR.add(Character.valueOf('*'));
/*   84 */     INVALID_ANCHOR.add(Character.valueOf('&'));
/*      */   }
/*      */   
/*   87 */   private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
/*      */ 
/*      */   
/*      */   static {
/*   91 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(false), "0");
/*   92 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
/*   93 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
/*   94 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
/*   95 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
/*   96 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
/*   97 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
/*   98 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
/*   99 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
/*  100 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*  101 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*  102 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
/*  103 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
/*  104 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
/*  105 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
/*      */   }
/*      */   
/*  108 */   private static final Map<String, String> DEFAULT_TAG_PREFIXES = new LinkedHashMap<>();
/*      */   private final Writer stream;
/*      */   
/*      */   static {
/*  112 */     DEFAULT_TAG_PREFIXES.put("!", "!");
/*  113 */     DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final ArrayStack<EmitterState> states;
/*      */ 
/*      */   
/*      */   private EmitterState state;
/*      */ 
/*      */   
/*      */   private final Queue<Event> events;
/*      */ 
/*      */   
/*      */   private Event event;
/*      */ 
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */   
/*      */   private Integer indent;
/*      */   
/*      */   private int flowLevel;
/*      */   
/*      */   private boolean rootContext;
/*      */   
/*      */   private boolean mappingContext;
/*      */   
/*      */   private boolean simpleKeyContext;
/*      */   
/*      */   private int column;
/*      */   
/*      */   private boolean whitespace;
/*      */   
/*      */   private boolean indention;
/*      */   
/*      */   private boolean openEnded;
/*      */   
/*      */   private final Boolean canonical;
/*      */   
/*      */   private final Boolean prettyFlow;
/*      */   
/*      */   private final boolean allowUnicode;
/*      */   
/*      */   private int bestIndent;
/*      */   
/*      */   private final int indicatorIndent;
/*      */   
/*      */   private final boolean indentWithIndicator;
/*      */   
/*      */   private int bestWidth;
/*      */   
/*      */   private final char[] bestLineBreak;
/*      */   
/*      */   private final boolean splitLines;
/*      */   
/*      */   private final int maxSimpleKeyLength;
/*      */   
/*      */   private final boolean emitComments;
/*      */   
/*      */   private Map<String, String> tagPrefixes;
/*      */   
/*      */   private String preparedAnchor;
/*      */   
/*      */   private String preparedTag;
/*      */   
/*      */   private ScalarAnalysis analysis;
/*      */   
/*      */   private DumperOptions.ScalarStyle style;
/*      */   
/*      */   private final CommentEventsCollector blockCommentsCollector;
/*      */   
/*      */   private final CommentEventsCollector inlineCommentsCollector;
/*      */ 
/*      */   
/*      */   public Emitter(Writer stream, DumperOptions opts) {
/*  188 */     this.stream = stream;
/*      */ 
/*      */     
/*  191 */     this.states = new ArrayStack(100);
/*  192 */     this.state = new ExpectStreamStart();
/*      */     
/*  194 */     this.events = new ArrayDeque<>(100);
/*  195 */     this.event = null;
/*      */     
/*  197 */     this.indents = new ArrayStack(10);
/*  198 */     this.indent = null;
/*      */     
/*  200 */     this.flowLevel = 0;
/*      */     
/*  202 */     this.mappingContext = false;
/*  203 */     this.simpleKeyContext = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  211 */     this.column = 0;
/*  212 */     this.whitespace = true;
/*  213 */     this.indention = true;
/*      */ 
/*      */     
/*  216 */     this.openEnded = false;
/*      */ 
/*      */     
/*  219 */     this.canonical = Boolean.valueOf(opts.isCanonical());
/*  220 */     this.prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
/*  221 */     this.allowUnicode = opts.isAllowUnicode();
/*  222 */     this.bestIndent = 2;
/*  223 */     if (opts.getIndent() > 1 && opts.getIndent() < 10) {
/*  224 */       this.bestIndent = opts.getIndent();
/*      */     }
/*  226 */     this.indicatorIndent = opts.getIndicatorIndent();
/*  227 */     this.indentWithIndicator = opts.getIndentWithIndicator();
/*  228 */     this.bestWidth = 80;
/*  229 */     if (opts.getWidth() > this.bestIndent * 2) {
/*  230 */       this.bestWidth = opts.getWidth();
/*      */     }
/*  232 */     this.bestLineBreak = opts.getLineBreak().getString().toCharArray();
/*  233 */     this.splitLines = opts.getSplitLines();
/*  234 */     this.maxSimpleKeyLength = opts.getMaxSimpleKeyLength();
/*  235 */     this.emitComments = opts.isProcessComments();
/*      */ 
/*      */     
/*  238 */     this.tagPrefixes = new LinkedHashMap<>();
/*      */ 
/*      */     
/*  241 */     this.preparedAnchor = null;
/*  242 */     this.preparedTag = null;
/*      */ 
/*      */     
/*  245 */     this.analysis = null;
/*  246 */     this.style = null;
/*      */ 
/*      */     
/*  249 */     this.blockCommentsCollector = new CommentEventsCollector(this.events, new CommentType[] { CommentType.BLANK_LINE, CommentType.BLOCK });
/*      */     
/*  251 */     this.inlineCommentsCollector = new CommentEventsCollector(this.events, new CommentType[] { CommentType.IN_LINE });
/*      */   }
/*      */   
/*      */   public void emit(Event event) throws IOException {
/*  255 */     this.events.add(event);
/*  256 */     while (!needMoreEvents()) {
/*  257 */       this.event = this.events.poll();
/*  258 */       this.state.expect();
/*  259 */       this.event = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreEvents() {
/*  266 */     if (this.events.isEmpty()) {
/*  267 */       return true;
/*      */     }
/*      */     
/*  270 */     Iterator<Event> iter = this.events.iterator();
/*  271 */     Event event = iter.next();
/*  272 */     while (event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  273 */       if (!iter.hasNext()) {
/*  274 */         return true;
/*      */       }
/*  276 */       event = iter.next();
/*      */     } 
/*      */     
/*  279 */     if (event instanceof DocumentStartEvent)
/*  280 */       return needEvents(iter, 1); 
/*  281 */     if (event instanceof SequenceStartEvent)
/*  282 */       return needEvents(iter, 2); 
/*  283 */     if (event instanceof MappingStartEvent)
/*  284 */       return needEvents(iter, 3); 
/*  285 */     if (event instanceof org.yaml.snakeyaml.events.StreamStartEvent)
/*  286 */       return needEvents(iter, 2); 
/*  287 */     if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent)
/*  288 */       return false; 
/*  289 */     if (this.emitComments) {
/*  290 */       return needEvents(iter, 1);
/*      */     }
/*  292 */     return false;
/*      */   }
/*      */   
/*      */   private boolean needEvents(Iterator<Event> iter, int count) {
/*  296 */     int level = 0;
/*  297 */     int actualCount = 0;
/*  298 */     while (iter.hasNext()) {
/*  299 */       Event event = iter.next();
/*  300 */       if (event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*      */         continue;
/*      */       }
/*  303 */       actualCount++;
/*  304 */       if (event instanceof DocumentStartEvent || event instanceof CollectionStartEvent) {
/*  305 */         level++;
/*  306 */       } else if (event instanceof DocumentEndEvent || event instanceof org.yaml.snakeyaml.events.CollectionEndEvent) {
/*  307 */         level--;
/*  308 */       } else if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*  309 */         level = -1;
/*      */       } 
/*  311 */       if (level < 0) {
/*  312 */         return false;
/*      */       }
/*      */     } 
/*  315 */     return (actualCount < count);
/*      */   }
/*      */   
/*      */   private void increaseIndent(boolean flow, boolean indentless) {
/*  319 */     this.indents.push(this.indent);
/*  320 */     if (this.indent == null) {
/*  321 */       if (flow) {
/*  322 */         this.indent = Integer.valueOf(this.bestIndent);
/*      */       } else {
/*  324 */         this.indent = Integer.valueOf(0);
/*      */       } 
/*  326 */     } else if (!indentless) {
/*  327 */       Emitter emitter = this; emitter.indent = Integer.valueOf(emitter.indent.intValue() + this.bestIndent);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private class ExpectStreamStart
/*      */     implements EmitterState
/*      */   {
/*      */     private ExpectStreamStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  338 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamStartEvent) {
/*  339 */         Emitter.this.writeStreamStart();
/*  340 */         Emitter.this.state = new Emitter.ExpectFirstDocumentStart();
/*      */       } else {
/*  342 */         throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectNothing implements EmitterState {
/*      */     private ExpectNothing() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  350 */       throw new EmitterException("expecting nothing, but got " + Emitter.this.event);
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectFirstDocumentStart
/*      */     implements EmitterState {
/*      */     private ExpectFirstDocumentStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  359 */       (new Emitter.ExpectDocumentStart(true)).expect();
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectDocumentStart
/*      */     implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectDocumentStart(boolean first) {
/*  368 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  372 */       if (Emitter.this.event instanceof DocumentStartEvent) {
/*  373 */         DocumentStartEvent ev = (DocumentStartEvent)Emitter.this.event;
/*  374 */         if ((ev.getVersion() != null || ev.getTags() != null) && Emitter.this.openEnded) {
/*  375 */           Emitter.this.writeIndicator("...", true, false, false);
/*  376 */           Emitter.this.writeIndent();
/*      */         } 
/*  378 */         if (ev.getVersion() != null) {
/*  379 */           String versionText = Emitter.this.prepareVersion(ev.getVersion());
/*  380 */           Emitter.this.writeVersionDirective(versionText);
/*      */         } 
/*  382 */         Emitter.this.tagPrefixes = (Map)new LinkedHashMap<>(Emitter.DEFAULT_TAG_PREFIXES);
/*  383 */         if (ev.getTags() != null) {
/*  384 */           Set<String> handles = new TreeSet<>(ev.getTags().keySet());
/*  385 */           for (String handle : handles) {
/*  386 */             String prefix = (String)ev.getTags().get(handle);
/*  387 */             Emitter.this.tagPrefixes.put(prefix, handle);
/*  388 */             String handleText = Emitter.this.prepareTagHandle(handle);
/*  389 */             String prefixText = Emitter.this.prepareTagPrefix(prefix);
/*  390 */             Emitter.this.writeTagDirective(handleText, prefixText);
/*      */           } 
/*      */         } 
/*      */         
/*  394 */         boolean implicit = (this.first && !ev.getExplicit() && !Emitter.this.canonical.booleanValue() && ev.getVersion() == null && (ev.getTags() == null || ev.getTags().isEmpty()) && !Emitter.this.checkEmptyDocument());
/*  395 */         if (!implicit) {
/*  396 */           Emitter.this.writeIndent();
/*  397 */           Emitter.this.writeIndicator("---", true, false, false);
/*  398 */           if (Emitter.this.canonical.booleanValue()) {
/*  399 */             Emitter.this.writeIndent();
/*      */           }
/*      */         } 
/*  402 */         Emitter.this.state = new Emitter.ExpectDocumentRoot();
/*  403 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*  404 */         Emitter.this.writeStreamEnd();
/*  405 */         Emitter.this.state = new Emitter.ExpectNothing();
/*  406 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  407 */         Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*  408 */         Emitter.this.writeBlockComment();
/*      */       } else {
/*      */         
/*  411 */         throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  419 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  420 */       Emitter.this.writeBlockComment();
/*  421 */       if (Emitter.this.event instanceof DocumentEndEvent) {
/*  422 */         Emitter.this.writeIndent();
/*  423 */         if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
/*  424 */           Emitter.this.writeIndicator("...", true, false, false);
/*  425 */           Emitter.this.writeIndent();
/*      */         } 
/*  427 */         Emitter.this.flushStream();
/*  428 */         Emitter.this.state = new Emitter.ExpectDocumentStart(false);
/*      */       } else {
/*  430 */         throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentRoot implements EmitterState {
/*      */     private ExpectDocumentRoot() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  438 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  439 */       if (!Emitter.this.blockCommentsCollector.isEmpty()) {
/*  440 */         Emitter.this.writeBlockComment();
/*  441 */         if (Emitter.this.event instanceof DocumentEndEvent) {
/*  442 */           (new Emitter.ExpectDocumentEnd()).expect();
/*      */           return;
/*      */         } 
/*      */       } 
/*  446 */       Emitter.this.states.push(new Emitter.ExpectDocumentEnd());
/*  447 */       Emitter.this.expectNode(true, false, false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectNode(boolean root, boolean mapping, boolean simpleKey) throws IOException {
/*  454 */     this.rootContext = root;
/*  455 */     this.mappingContext = mapping;
/*  456 */     this.simpleKeyContext = simpleKey;
/*  457 */     if (this.event instanceof org.yaml.snakeyaml.events.AliasEvent) {
/*  458 */       expectAlias();
/*  459 */     } else if (this.event instanceof ScalarEvent || this.event instanceof CollectionStartEvent) {
/*  460 */       processAnchor("&");
/*  461 */       processTag();
/*  462 */       if (this.event instanceof ScalarEvent) {
/*  463 */         expectScalar();
/*  464 */       } else if (this.event instanceof SequenceStartEvent) {
/*  465 */         if (this.flowLevel != 0 || this.canonical.booleanValue() || ((SequenceStartEvent)this.event).isFlow() || 
/*  466 */           checkEmptySequence()) {
/*  467 */           expectFlowSequence();
/*      */         } else {
/*  469 */           expectBlockSequence();
/*      */         }
/*      */       
/*  472 */       } else if (this.flowLevel != 0 || this.canonical.booleanValue() || ((MappingStartEvent)this.event).isFlow() || 
/*  473 */         checkEmptyMapping()) {
/*  474 */         expectFlowMapping();
/*      */       } else {
/*  476 */         expectBlockMapping();
/*      */       } 
/*      */     } else {
/*      */       
/*  480 */       throw new EmitterException("expected NodeEvent, but got " + this.event);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void expectAlias() throws IOException {
/*  485 */     if (!(this.event instanceof org.yaml.snakeyaml.events.AliasEvent)) {
/*  486 */       throw new EmitterException("Alias must be provided");
/*      */     }
/*  488 */     processAnchor("*");
/*  489 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */   
/*      */   private void expectScalar() throws IOException {
/*  493 */     increaseIndent(true, false);
/*  494 */     processScalar();
/*  495 */     this.indent = (Integer)this.indents.pop();
/*  496 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowSequence() throws IOException {
/*  502 */     writeIndicator("[", true, true, false);
/*  503 */     this.flowLevel++;
/*  504 */     increaseIndent(true, false);
/*  505 */     if (this.prettyFlow.booleanValue()) {
/*  506 */       writeIndent();
/*      */     }
/*  508 */     this.state = new ExpectFirstFlowSequenceItem();
/*      */   }
/*      */   
/*      */   private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  514 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  515 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  516 */         Emitter.this.flowLevel--;
/*  517 */         Emitter.this.writeIndicator("]", false, false, false);
/*  518 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  519 */         Emitter.this.writeInlineComments();
/*  520 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*  521 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  522 */         Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*  523 */         Emitter.this.writeBlockComment();
/*      */       } else {
/*  525 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  526 */           Emitter.this.writeIndent();
/*      */         }
/*  528 */         Emitter.this.states.push(new Emitter.ExpectFlowSequenceItem());
/*  529 */         Emitter.this.expectNode(false, false, false);
/*  530 */         Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  531 */         Emitter.this.writeInlineComments();
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowSequenceItem implements EmitterState {
/*      */     private ExpectFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  539 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  540 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  541 */         Emitter.this.flowLevel--;
/*  542 */         if (Emitter.this.canonical.booleanValue()) {
/*  543 */           Emitter.this.writeIndicator(",", false, false, false);
/*  544 */           Emitter.this.writeIndent();
/*  545 */         } else if (Emitter.this.prettyFlow.booleanValue()) {
/*  546 */           Emitter.this.writeIndent();
/*      */         } 
/*  548 */         Emitter.this.writeIndicator("]", false, false, false);
/*  549 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  550 */         Emitter.this.writeInlineComments();
/*  551 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  552 */           Emitter.this.writeIndent();
/*      */         }
/*  554 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*  555 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  556 */         Emitter.this.event = Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*      */       } else {
/*  558 */         Emitter.this.writeIndicator(",", false, false, false);
/*  559 */         Emitter.this.writeBlockComment();
/*  560 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  561 */           Emitter.this.writeIndent();
/*      */         }
/*  563 */         Emitter.this.states.push(new ExpectFlowSequenceItem());
/*  564 */         Emitter.this.expectNode(false, false, false);
/*  565 */         Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  566 */         Emitter.this.writeInlineComments();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowMapping() throws IOException {
/*  574 */     writeIndicator("{", true, true, false);
/*  575 */     this.flowLevel++;
/*  576 */     increaseIndent(true, false);
/*  577 */     if (this.prettyFlow.booleanValue()) {
/*  578 */       writeIndent();
/*      */     }
/*  580 */     this.state = new ExpectFirstFlowMappingKey();
/*      */   }
/*      */   
/*      */   private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  586 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  587 */       Emitter.this.writeBlockComment();
/*  588 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  589 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  590 */         Emitter.this.flowLevel--;
/*  591 */         Emitter.this.writeIndicator("}", false, false, false);
/*  592 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  593 */         Emitter.this.writeInlineComments();
/*  594 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  596 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  597 */           Emitter.this.writeIndent();
/*      */         }
/*  599 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  600 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  601 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  603 */           Emitter.this.writeIndicator("?", true, false, false);
/*  604 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  605 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingKey implements EmitterState {
/*      */     private ExpectFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  614 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  615 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  616 */         Emitter.this.flowLevel--;
/*  617 */         if (Emitter.this.canonical.booleanValue()) {
/*  618 */           Emitter.this.writeIndicator(",", false, false, false);
/*  619 */           Emitter.this.writeIndent();
/*      */         } 
/*  621 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  622 */           Emitter.this.writeIndent();
/*      */         }
/*  624 */         Emitter.this.writeIndicator("}", false, false, false);
/*  625 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  626 */         Emitter.this.writeInlineComments();
/*  627 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  629 */         Emitter.this.writeIndicator(",", false, false, false);
/*  630 */         Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  631 */         Emitter.this.writeBlockComment();
/*  632 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  633 */           Emitter.this.writeIndent();
/*      */         }
/*  635 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  636 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  637 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  639 */           Emitter.this.writeIndicator("?", true, false, false);
/*  640 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  641 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  650 */       Emitter.this.writeIndicator(":", false, false, false);
/*  651 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  652 */       Emitter.this.writeInlineComments();
/*  653 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  654 */       Emitter.this.expectNode(false, true, false);
/*  655 */       Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  656 */       Emitter.this.writeInlineComments();
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingValue implements EmitterState {
/*      */     private ExpectFlowMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  663 */       if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  664 */         Emitter.this.writeIndent();
/*      */       }
/*  666 */       Emitter.this.writeIndicator(":", true, false, false);
/*  667 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  668 */       Emitter.this.writeInlineComments();
/*  669 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  670 */       Emitter.this.expectNode(false, true, false);
/*  671 */       Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  672 */       Emitter.this.writeInlineComments();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectBlockSequence() throws IOException {
/*  679 */     boolean indentless = (this.mappingContext && !this.indention);
/*  680 */     increaseIndent(false, indentless);
/*  681 */     this.state = new ExpectFirstBlockSequenceItem();
/*      */   }
/*      */   
/*      */   private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  687 */       (new Emitter.ExpectBlockSequenceItem(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockSequenceItem
/*      */     implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectBlockSequenceItem(boolean first) {
/*  696 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  700 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  701 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  702 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*  703 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  704 */         Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*      */       } else {
/*  706 */         Emitter.this.writeIndent();
/*  707 */         if (!Emitter.this.indentWithIndicator || this.first) {
/*  708 */           Emitter.this.writeWhitespace(Emitter.this.indicatorIndent);
/*      */         }
/*  710 */         Emitter.this.writeIndicator("-", true, false, true);
/*  711 */         if (Emitter.this.indentWithIndicator && this.first) {
/*  712 */           Emitter.this.indent = Integer.valueOf(Emitter.this.indent.intValue() + Emitter.this.indicatorIndent);
/*      */         }
/*  714 */         if (!Emitter.this.blockCommentsCollector.isEmpty()) {
/*  715 */           Emitter.this.increaseIndent(false, false);
/*  716 */           Emitter.this.writeBlockComment();
/*  717 */           if (Emitter.this.event instanceof ScalarEvent) {
/*  718 */             Emitter.this.analysis = Emitter.this.analyzeScalar(((ScalarEvent)Emitter.this.event).getValue());
/*  719 */             if (!Emitter.this.analysis.isEmpty()) {
/*  720 */               Emitter.this.writeIndent();
/*      */             }
/*      */           } 
/*  723 */           Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*      */         } 
/*  725 */         Emitter.this.states.push(new ExpectBlockSequenceItem(false));
/*  726 */         Emitter.this.expectNode(false, false, false);
/*  727 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  728 */         Emitter.this.writeInlineComments();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void expectBlockMapping() throws IOException {
/*  735 */     increaseIndent(false, false);
/*  736 */     this.state = new ExpectFirstBlockMappingKey();
/*      */   }
/*      */   
/*      */   private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  742 */       (new Emitter.ExpectBlockMappingKey(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockMappingKey
/*      */     implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectBlockMappingKey(boolean first) {
/*  751 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  755 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  756 */       Emitter.this.writeBlockComment();
/*  757 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  758 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  759 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  761 */         Emitter.this.writeIndent();
/*  762 */         if (Emitter.this.checkSimpleKey()) {
/*  763 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingSimpleValue());
/*  764 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  766 */           Emitter.this.writeIndicator("?", true, false, true);
/*  767 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingValue());
/*  768 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isFoldedOrLiteral(Event event) {
/*  775 */     if (!event.is(Event.ID.Scalar)) {
/*  776 */       return false;
/*      */     }
/*  778 */     ScalarEvent scalarEvent = (ScalarEvent)event;
/*  779 */     DumperOptions.ScalarStyle style = scalarEvent.getScalarStyle();
/*  780 */     return (style == DumperOptions.ScalarStyle.FOLDED || style == DumperOptions.ScalarStyle.LITERAL);
/*      */   }
/*      */   
/*      */   private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  786 */       Emitter.this.writeIndicator(":", false, false, false);
/*  787 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  788 */       if (!Emitter.this.isFoldedOrLiteral(Emitter.this.event) && 
/*  789 */         Emitter.this.writeInlineComments()) {
/*  790 */         Emitter.this.increaseIndent(true, false);
/*  791 */         Emitter.this.writeIndent();
/*  792 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*      */       } 
/*      */       
/*  795 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  796 */       if (!Emitter.this.blockCommentsCollector.isEmpty()) {
/*  797 */         Emitter.this.increaseIndent(true, false);
/*  798 */         Emitter.this.writeBlockComment();
/*  799 */         Emitter.this.writeIndent();
/*  800 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*      */       } 
/*  802 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  803 */       Emitter.this.expectNode(false, true, false);
/*  804 */       Emitter.this.inlineCommentsCollector.collectEvents();
/*  805 */       Emitter.this.writeInlineComments();
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingValue implements EmitterState {
/*      */     private ExpectBlockMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  812 */       Emitter.this.writeIndent();
/*  813 */       Emitter.this.writeIndicator(":", true, false, true);
/*  814 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  815 */       Emitter.this.writeInlineComments();
/*  816 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  817 */       Emitter.this.writeBlockComment();
/*  818 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  819 */       Emitter.this.expectNode(false, true, false);
/*  820 */       Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  821 */       Emitter.this.writeInlineComments();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkEmptySequence() {
/*  828 */     return (this.event instanceof SequenceStartEvent && !this.events.isEmpty() && this.events
/*  829 */       .peek() instanceof org.yaml.snakeyaml.events.SequenceEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyMapping() {
/*  833 */     return (this.event instanceof MappingStartEvent && !this.events.isEmpty() && this.events
/*  834 */       .peek() instanceof org.yaml.snakeyaml.events.MappingEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyDocument() {
/*  838 */     if (!(this.event instanceof DocumentStartEvent) || this.events.isEmpty()) {
/*  839 */       return false;
/*      */     }
/*  841 */     Event event = this.events.peek();
/*  842 */     if (event instanceof ScalarEvent) {
/*  843 */       ScalarEvent e = (ScalarEvent)event;
/*  844 */       return (e.getAnchor() == null && e.getTag() == null && e.getImplicit() != null && e
/*  845 */         .getValue().length() == 0);
/*      */     } 
/*  847 */     return false;
/*      */   }
/*      */   
/*      */   private boolean checkSimpleKey() {
/*  851 */     int length = 0;
/*  852 */     if (this.event instanceof NodeEvent && ((NodeEvent)this.event).getAnchor() != null) {
/*  853 */       if (this.preparedAnchor == null) {
/*  854 */         this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
/*      */       }
/*  856 */       length += this.preparedAnchor.length();
/*      */     } 
/*  858 */     String tag = null;
/*  859 */     if (this.event instanceof ScalarEvent) {
/*  860 */       tag = ((ScalarEvent)this.event).getTag();
/*  861 */     } else if (this.event instanceof CollectionStartEvent) {
/*  862 */       tag = ((CollectionStartEvent)this.event).getTag();
/*      */     } 
/*  864 */     if (tag != null) {
/*  865 */       if (this.preparedTag == null) {
/*  866 */         this.preparedTag = prepareTag(tag);
/*      */       }
/*  868 */       length += this.preparedTag.length();
/*      */     } 
/*  870 */     if (this.event instanceof ScalarEvent) {
/*  871 */       if (this.analysis == null) {
/*  872 */         this.analysis = analyzeScalar(((ScalarEvent)this.event).getValue());
/*      */       }
/*  874 */       length += this.analysis.getScalar().length();
/*      */     } 
/*  876 */     return (length < this.maxSimpleKeyLength && (this.event instanceof org.yaml.snakeyaml.events.AliasEvent || (this.event instanceof ScalarEvent && 
/*  877 */       !this.analysis.isEmpty() && !this.analysis.isMultiline()) || 
/*  878 */       checkEmptySequence() || checkEmptyMapping()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processAnchor(String indicator) throws IOException {
/*  884 */     NodeEvent ev = (NodeEvent)this.event;
/*  885 */     if (ev.getAnchor() == null) {
/*  886 */       this.preparedAnchor = null;
/*      */       return;
/*      */     } 
/*  889 */     if (this.preparedAnchor == null) {
/*  890 */       this.preparedAnchor = prepareAnchor(ev.getAnchor());
/*      */     }
/*  892 */     writeIndicator(indicator + this.preparedAnchor, true, false, false);
/*  893 */     this.preparedAnchor = null;
/*      */   }
/*      */   
/*      */   private void processTag() throws IOException {
/*  897 */     String tag = null;
/*  898 */     if (this.event instanceof ScalarEvent) {
/*  899 */       ScalarEvent ev = (ScalarEvent)this.event;
/*  900 */       tag = ev.getTag();
/*  901 */       if (this.style == null) {
/*  902 */         this.style = chooseScalarStyle();
/*      */       }
/*  904 */       if ((!this.canonical.booleanValue() || tag == null) && ((this.style == null && ev
/*  905 */         .getImplicit().canOmitTagInPlainScalar()) || (this.style != null && ev
/*  906 */         .getImplicit().canOmitTagInNonPlainScalar()))) {
/*  907 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*  910 */       if (ev.getImplicit().canOmitTagInPlainScalar() && tag == null) {
/*  911 */         tag = "!";
/*  912 */         this.preparedTag = null;
/*      */       } 
/*      */     } else {
/*  915 */       CollectionStartEvent ev = (CollectionStartEvent)this.event;
/*  916 */       tag = ev.getTag();
/*  917 */       if ((!this.canonical.booleanValue() || tag == null) && ev.getImplicit()) {
/*  918 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*      */     } 
/*  922 */     if (tag == null) {
/*  923 */       throw new EmitterException("tag is not specified");
/*      */     }
/*  925 */     if (this.preparedTag == null) {
/*  926 */       this.preparedTag = prepareTag(tag);
/*      */     }
/*  928 */     writeIndicator(this.preparedTag, true, false, false);
/*  929 */     this.preparedTag = null;
/*      */   }
/*      */   
/*      */   private DumperOptions.ScalarStyle chooseScalarStyle() {
/*  933 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  934 */     if (this.analysis == null) {
/*  935 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  937 */     if ((!ev.isPlain() && ev.getScalarStyle() == DumperOptions.ScalarStyle.DOUBLE_QUOTED) || this.canonical
/*  938 */       .booleanValue()) {
/*  939 */       return DumperOptions.ScalarStyle.DOUBLE_QUOTED;
/*      */     }
/*  941 */     if (ev.isPlain() && ev.getImplicit().canOmitTagInPlainScalar() && (
/*  942 */       !this.simpleKeyContext || (!this.analysis.isEmpty() && !this.analysis.isMultiline())) && ((this.flowLevel != 0 && this.analysis
/*  943 */       .isAllowFlowPlain()) || (this.flowLevel == 0 && this.analysis
/*  944 */       .isAllowBlockPlain()))) {
/*  945 */       return null;
/*      */     }
/*      */     
/*  948 */     if (!ev.isPlain() && (ev.getScalarStyle() == DumperOptions.ScalarStyle.LITERAL || ev
/*  949 */       .getScalarStyle() == DumperOptions.ScalarStyle.FOLDED) && 
/*  950 */       this.flowLevel == 0 && !this.simpleKeyContext && this.analysis.isAllowBlock()) {
/*  951 */       return ev.getScalarStyle();
/*      */     }
/*      */     
/*  954 */     if ((ev.isPlain() || ev.getScalarStyle() == DumperOptions.ScalarStyle.SINGLE_QUOTED) && 
/*  955 */       this.analysis.isAllowSingleQuoted() && (!this.simpleKeyContext || !this.analysis.isMultiline())) {
/*  956 */       return DumperOptions.ScalarStyle.SINGLE_QUOTED;
/*      */     }
/*      */     
/*  959 */     return DumperOptions.ScalarStyle.DOUBLE_QUOTED;
/*      */   }
/*      */   
/*      */   private void processScalar() throws IOException {
/*  963 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  964 */     if (this.analysis == null) {
/*  965 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  967 */     if (this.style == null) {
/*  968 */       this.style = chooseScalarStyle();
/*      */     }
/*  970 */     boolean split = (!this.simpleKeyContext && this.splitLines);
/*  971 */     if (this.style == null) {
/*  972 */       writePlain(this.analysis.getScalar(), split);
/*      */     } else {
/*  974 */       switch (this.style) {
/*      */         case DOUBLE_QUOTED:
/*  976 */           writeDoubleQuoted(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case SINGLE_QUOTED:
/*  979 */           writeSingleQuoted(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case FOLDED:
/*  982 */           writeFolded(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case LITERAL:
/*  985 */           writeLiteral(this.analysis.getScalar());
/*      */           break;
/*      */         default:
/*  988 */           throw new YAMLException("Unexpected style: " + this.style);
/*      */       } 
/*      */     } 
/*  991 */     this.analysis = null;
/*  992 */     this.style = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String prepareVersion(DumperOptions.Version version) {
/*  998 */     if (version.major() != 1) {
/*  999 */       throw new EmitterException("unsupported YAML version: " + version);
/*      */     }
/* 1001 */     return version.getRepresentation();
/*      */   }
/*      */   
/* 1004 */   private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
/*      */   
/*      */   private String prepareTagHandle(String handle) {
/* 1007 */     if (handle.length() == 0)
/* 1008 */       throw new EmitterException("tag handle must not be empty"); 
/* 1009 */     if (handle.charAt(0) != '!' || handle.charAt(handle.length() - 1) != '!')
/* 1010 */       throw new EmitterException("tag handle must start and end with '!': " + handle); 
/* 1011 */     if (!"!".equals(handle) && !HANDLE_FORMAT.matcher(handle).matches()) {
/* 1012 */       throw new EmitterException("invalid character in the tag handle: " + handle);
/*      */     }
/* 1014 */     return handle;
/*      */   }
/*      */   
/*      */   private String prepareTagPrefix(String prefix) {
/* 1018 */     if (prefix.length() == 0) {
/* 1019 */       throw new EmitterException("tag prefix must not be empty");
/*      */     }
/* 1021 */     StringBuilder chunks = new StringBuilder();
/* 1022 */     int start = 0;
/* 1023 */     int end = 0;
/* 1024 */     if (prefix.charAt(0) == '!') {
/* 1025 */       end = 1;
/*      */     }
/* 1027 */     while (end < prefix.length()) {
/* 1028 */       end++;
/*      */     }
/* 1030 */     if (start < end) {
/* 1031 */       chunks.append(prefix, start, end);
/*      */     }
/* 1033 */     return chunks.toString();
/*      */   }
/*      */   
/*      */   private String prepareTag(String tag) {
/* 1037 */     if (tag.length() == 0) {
/* 1038 */       throw new EmitterException("tag must not be empty");
/*      */     }
/* 1040 */     if ("!".equals(tag)) {
/* 1041 */       return tag;
/*      */     }
/* 1043 */     String handle = null;
/* 1044 */     String suffix = tag;
/*      */     
/* 1046 */     for (String prefix : this.tagPrefixes.keySet()) {
/* 1047 */       if (tag.startsWith(prefix) && ("!".equals(prefix) || prefix.length() < tag.length())) {
/* 1048 */         handle = prefix;
/*      */       }
/*      */     } 
/* 1051 */     if (handle != null) {
/* 1052 */       suffix = tag.substring(handle.length());
/* 1053 */       handle = this.tagPrefixes.get(handle);
/*      */     } 
/*      */     
/* 1056 */     int end = suffix.length();
/* 1057 */     String suffixText = (end > 0) ? suffix.substring(0, end) : "";
/*      */     
/* 1059 */     if (handle != null) {
/* 1060 */       return handle + suffixText;
/*      */     }
/* 1062 */     return "!<" + suffixText + ">";
/*      */   }
/*      */   
/*      */   static String prepareAnchor(String anchor) {
/* 1066 */     if (anchor.length() == 0) {
/* 1067 */       throw new EmitterException("anchor must not be empty");
/*      */     }
/* 1069 */     for (Character invalid : INVALID_ANCHOR) {
/* 1070 */       if (anchor.indexOf(invalid.charValue()) > -1) {
/* 1071 */         throw new EmitterException("Invalid character '" + invalid + "' in the anchor: " + anchor);
/*      */       }
/*      */     } 
/* 1074 */     Matcher matcher = SPACES_PATTERN.matcher(anchor);
/* 1075 */     if (matcher.find()) {
/* 1076 */       throw new EmitterException("Anchor may not contain spaces: " + anchor);
/*      */     }
/* 1078 */     return anchor;
/*      */   }
/*      */ 
/*      */   
/*      */   private ScalarAnalysis analyzeScalar(String scalar) {
/* 1083 */     if (scalar.length() == 0) {
/* 1084 */       return new ScalarAnalysis(scalar, true, false, false, true, true, false);
/*      */     }
/*      */     
/* 1087 */     boolean blockIndicators = false;
/* 1088 */     boolean flowIndicators = false;
/* 1089 */     boolean lineBreaks = false;
/* 1090 */     boolean specialCharacters = false;
/*      */ 
/*      */     
/* 1093 */     boolean leadingSpace = false;
/* 1094 */     boolean leadingBreak = false;
/* 1095 */     boolean trailingSpace = false;
/* 1096 */     boolean trailingBreak = false;
/* 1097 */     boolean breakSpace = false;
/* 1098 */     boolean spaceBreak = false;
/*      */ 
/*      */     
/* 1101 */     if (scalar.startsWith("---") || scalar.startsWith("...")) {
/* 1102 */       blockIndicators = true;
/* 1103 */       flowIndicators = true;
/*      */     } 
/*      */     
/* 1106 */     boolean preceededByWhitespace = true;
/*      */     
/* 1108 */     boolean followedByWhitespace = (scalar.length() == 1 || Constant.NULL_BL_T_LINEBR.has(scalar.codePointAt(1)));
/*      */     
/* 1110 */     boolean previousSpace = false;
/*      */ 
/*      */     
/* 1113 */     boolean previousBreak = false;
/*      */     
/* 1115 */     int index = 0;
/*      */     
/* 1117 */     while (index < scalar.length()) {
/* 1118 */       int c = scalar.codePointAt(index);
/*      */       
/* 1120 */       if (index == 0) {
/*      */         
/* 1122 */         if ("#,[]{}&*!|>'\"%@`".indexOf(c) != -1) {
/* 1123 */           flowIndicators = true;
/* 1124 */           blockIndicators = true;
/*      */         } 
/* 1126 */         if (c == 63 || c == 58) {
/* 1127 */           flowIndicators = true;
/* 1128 */           if (followedByWhitespace) {
/* 1129 */             blockIndicators = true;
/*      */           }
/*      */         } 
/* 1132 */         if (c == 45 && followedByWhitespace) {
/* 1133 */           flowIndicators = true;
/* 1134 */           blockIndicators = true;
/*      */         } 
/*      */       } else {
/*      */         
/* 1138 */         if (",?[]{}".indexOf(c) != -1) {
/* 1139 */           flowIndicators = true;
/*      */         }
/* 1141 */         if (c == 58) {
/* 1142 */           flowIndicators = true;
/* 1143 */           if (followedByWhitespace) {
/* 1144 */             blockIndicators = true;
/*      */           }
/*      */         } 
/* 1147 */         if (c == 35 && preceededByWhitespace) {
/* 1148 */           flowIndicators = true;
/* 1149 */           blockIndicators = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1153 */       boolean isLineBreak = Constant.LINEBR.has(c);
/* 1154 */       if (isLineBreak) {
/* 1155 */         lineBreaks = true;
/*      */       }
/* 1157 */       if (c != 10 && (32 > c || c > 126)) {
/* 1158 */         if (c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111)) {
/*      */ 
/*      */           
/* 1161 */           if (!this.allowUnicode) {
/* 1162 */             specialCharacters = true;
/*      */           }
/*      */         } else {
/* 1165 */           specialCharacters = true;
/*      */         } 
/*      */       }
/*      */       
/* 1169 */       if (c == 32) {
/* 1170 */         if (index == 0) {
/* 1171 */           leadingSpace = true;
/*      */         }
/* 1173 */         if (index == scalar.length() - 1) {
/* 1174 */           trailingSpace = true;
/*      */         }
/* 1176 */         if (previousBreak) {
/* 1177 */           breakSpace = true;
/*      */         }
/* 1179 */         previousSpace = true;
/* 1180 */         previousBreak = false;
/* 1181 */       } else if (isLineBreak) {
/* 1182 */         if (index == 0) {
/* 1183 */           leadingBreak = true;
/*      */         }
/* 1185 */         if (index == scalar.length() - 1) {
/* 1186 */           trailingBreak = true;
/*      */         }
/* 1188 */         if (previousSpace) {
/* 1189 */           spaceBreak = true;
/*      */         }
/* 1191 */         previousSpace = false;
/* 1192 */         previousBreak = true;
/*      */       } else {
/* 1194 */         previousSpace = false;
/* 1195 */         previousBreak = false;
/*      */       } 
/*      */ 
/*      */       
/* 1199 */       index += Character.charCount(c);
/* 1200 */       preceededByWhitespace = (Constant.NULL_BL_T.has(c) || isLineBreak);
/* 1201 */       followedByWhitespace = true;
/* 1202 */       if (index + 1 < scalar.length()) {
/* 1203 */         int nextIndex = index + Character.charCount(scalar.codePointAt(index));
/* 1204 */         if (nextIndex < scalar.length())
/*      */         {
/* 1206 */           followedByWhitespace = (Constant.NULL_BL_T.has(scalar.codePointAt(nextIndex)) || isLineBreak);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1211 */     boolean allowFlowPlain = true;
/* 1212 */     boolean allowBlockPlain = true;
/* 1213 */     boolean allowSingleQuoted = true;
/* 1214 */     boolean allowBlock = true;
/*      */     
/* 1216 */     if (leadingSpace || leadingBreak || trailingSpace || trailingBreak) {
/* 1217 */       allowFlowPlain = allowBlockPlain = false;
/*      */     }
/*      */     
/* 1220 */     if (trailingSpace) {
/* 1221 */       allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1225 */     if (breakSpace) {
/* 1226 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
/*      */     }
/*      */ 
/*      */     
/* 1230 */     if (spaceBreak || specialCharacters) {
/* 1231 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1235 */     if (lineBreaks) {
/* 1236 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1239 */     if (flowIndicators) {
/* 1240 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1243 */     if (blockIndicators) {
/* 1244 */       allowBlockPlain = false;
/*      */     }
/*      */     
/* 1247 */     return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void flushStream() throws IOException {
/* 1254 */     this.stream.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeStreamStart() {}
/*      */ 
/*      */   
/*      */   void writeStreamEnd() throws IOException {
/* 1262 */     flushStream();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException {
/* 1267 */     if (!this.whitespace && needWhitespace) {
/* 1268 */       this.column++;
/* 1269 */       this.stream.write(SPACE);
/*      */     } 
/* 1271 */     this.whitespace = whitespace;
/* 1272 */     this.indention = (this.indention && indentation);
/* 1273 */     this.column += indicator.length();
/* 1274 */     this.openEnded = false;
/* 1275 */     this.stream.write(indicator);
/*      */   }
/*      */   
/*      */   void writeIndent() throws IOException {
/*      */     int indent;
/* 1280 */     if (this.indent != null) {
/* 1281 */       indent = this.indent.intValue();
/*      */     } else {
/* 1283 */       indent = 0;
/*      */     } 
/*      */     
/* 1286 */     if (!this.indention || this.column > indent || (this.column == indent && !this.whitespace)) {
/* 1287 */       writeLineBreak(null);
/*      */     }
/*      */     
/* 1290 */     writeWhitespace(indent - this.column);
/*      */   }
/*      */   
/*      */   private void writeWhitespace(int length) throws IOException {
/* 1294 */     if (length <= 0) {
/*      */       return;
/*      */     }
/* 1297 */     this.whitespace = true;
/* 1298 */     char[] data = new char[length];
/* 1299 */     for (int i = 0; i < data.length; i++) {
/* 1300 */       data[i] = ' ';
/*      */     }
/* 1302 */     this.column += length;
/* 1303 */     this.stream.write(data);
/*      */   }
/*      */   
/*      */   private void writeLineBreak(String data) throws IOException {
/* 1307 */     this.whitespace = true;
/* 1308 */     this.indention = true;
/* 1309 */     this.column = 0;
/* 1310 */     if (data == null) {
/* 1311 */       this.stream.write(this.bestLineBreak);
/*      */     } else {
/* 1313 */       this.stream.write(data);
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeVersionDirective(String versionText) throws IOException {
/* 1318 */     this.stream.write("%YAML ");
/* 1319 */     this.stream.write(versionText);
/* 1320 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeTagDirective(String handleText, String prefixText) throws IOException {
/* 1326 */     this.stream.write("%TAG ");
/* 1327 */     this.stream.write(handleText);
/* 1328 */     this.stream.write(SPACE);
/* 1329 */     this.stream.write(prefixText);
/* 1330 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeSingleQuoted(String text, boolean split) throws IOException {
/* 1335 */     writeIndicator("'", true, false, false);
/* 1336 */     boolean spaces = false;
/* 1337 */     boolean breaks = false;
/* 1338 */     int start = 0, end = 0;
/*      */     
/* 1340 */     while (end <= text.length()) {
/* 1341 */       char ch = Character.MIN_VALUE;
/* 1342 */       if (end < text.length()) {
/* 1343 */         ch = text.charAt(end);
/*      */       }
/* 1345 */       if (spaces) {
/* 1346 */         if (ch == '\000' || ch != ' ') {
/* 1347 */           if (start + 1 == end && this.column > this.bestWidth && split && start != 0 && end != text
/* 1348 */             .length()) {
/* 1349 */             writeIndent();
/*      */           } else {
/* 1351 */             int len = end - start;
/* 1352 */             this.column += len;
/* 1353 */             this.stream.write(text, start, len);
/*      */           } 
/* 1355 */           start = end;
/*      */         } 
/* 1357 */       } else if (breaks) {
/* 1358 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1359 */           if (text.charAt(start) == '\n') {
/* 1360 */             writeLineBreak(null);
/*      */           }
/* 1362 */           String data = text.substring(start, end);
/* 1363 */           for (char br : data.toCharArray()) {
/* 1364 */             if (br == '\n') {
/* 1365 */               writeLineBreak(null);
/*      */             } else {
/* 1367 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1370 */           writeIndent();
/* 1371 */           start = end;
/*      */         }
/*      */       
/* 1374 */       } else if (Constant.LINEBR.has(ch, "\000 '") && 
/* 1375 */         start < end) {
/* 1376 */         int len = end - start;
/* 1377 */         this.column += len;
/* 1378 */         this.stream.write(text, start, len);
/* 1379 */         start = end;
/*      */       } 
/*      */ 
/*      */       
/* 1383 */       if (ch == '\'') {
/* 1384 */         this.column += 2;
/* 1385 */         this.stream.write("''");
/* 1386 */         start = end + 1;
/*      */       } 
/* 1388 */       if (ch != '\000') {
/* 1389 */         spaces = (ch == ' ');
/* 1390 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1392 */       end++;
/*      */     } 
/* 1394 */     writeIndicator("'", false, false, false);
/*      */   }
/*      */   
/*      */   private void writeDoubleQuoted(String text, boolean split) throws IOException {
/* 1398 */     writeIndicator("\"", true, false, false);
/* 1399 */     int start = 0;
/* 1400 */     int end = 0;
/* 1401 */     while (end <= text.length()) {
/* 1402 */       Character ch = null;
/* 1403 */       if (end < text.length()) {
/* 1404 */         ch = Character.valueOf(text.charAt(end));
/*      */       }
/* 1406 */       if (ch == null || "\"\\  ﻿".indexOf(ch.charValue()) != -1 || ' ' > ch
/* 1407 */         .charValue() || ch.charValue() > '~') {
/* 1408 */         if (start < end) {
/* 1409 */           int len = end - start;
/* 1410 */           this.column += len;
/* 1411 */           this.stream.write(text, start, len);
/* 1412 */           start = end;
/*      */         } 
/* 1414 */         if (ch != null) {
/*      */           String data;
/*      */           
/* 1417 */           if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
/* 1418 */             data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch);
/*      */           } else {
/*      */             int codePoint;
/*      */             
/* 1422 */             if (Character.isHighSurrogate(ch.charValue()) && end + 1 < text.length()) {
/* 1423 */               char ch2 = text.charAt(end + 1);
/* 1424 */               codePoint = Character.toCodePoint(ch.charValue(), ch2);
/*      */             } else {
/* 1426 */               codePoint = ch.charValue();
/*      */             } 
/*      */             
/* 1429 */             if (this.allowUnicode && StreamReader.isPrintable(codePoint)) {
/* 1430 */               data = String.valueOf(Character.toChars(codePoint));
/*      */               
/* 1432 */               if (Character.charCount(codePoint) == 2) {
/* 1433 */                 end++;
/*      */               
/*      */               }
/*      */             
/*      */             }
/* 1438 */             else if (ch.charValue() <= 'ÿ') {
/* 1439 */               String s = "0" + Integer.toString(ch.charValue(), 16);
/* 1440 */               data = "\\x" + s.substring(s.length() - 2);
/* 1441 */             } else if (Character.charCount(codePoint) == 2) {
/* 1442 */               end++;
/* 1443 */               String s = "000" + Long.toHexString(codePoint);
/* 1444 */               data = "\\U" + s.substring(s.length() - 8);
/*      */             } else {
/* 1446 */               String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1447 */               data = "\\u" + s.substring(s.length() - 4);
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 1452 */           this.column += data.length();
/* 1453 */           this.stream.write(data);
/* 1454 */           start = end + 1;
/*      */         } 
/*      */       } 
/* 1457 */       if (0 < end && end < text.length() - 1 && (ch.charValue() == ' ' || start >= end) && this.column + end - start > this.bestWidth && split) {
/*      */         String data;
/*      */         
/* 1460 */         if (start >= end) {
/* 1461 */           data = "\\";
/*      */         } else {
/* 1463 */           data = text.substring(start, end) + "\\";
/*      */         } 
/* 1465 */         if (start < end) {
/* 1466 */           start = end;
/*      */         }
/* 1468 */         this.column += data.length();
/* 1469 */         this.stream.write(data);
/* 1470 */         writeIndent();
/* 1471 */         this.whitespace = false;
/* 1472 */         this.indention = false;
/* 1473 */         if (text.charAt(start) == ' ') {
/* 1474 */           data = "\\";
/* 1475 */           this.column += data.length();
/* 1476 */           this.stream.write(data);
/*      */         } 
/*      */       } 
/* 1479 */       end++;
/*      */     } 
/* 1481 */     writeIndicator("\"", false, false, false);
/*      */   }
/*      */   
/*      */   private boolean writeCommentLines(List<CommentLine> commentLines) throws IOException {
/* 1485 */     boolean wroteComment = false;
/* 1486 */     if (this.emitComments) {
/* 1487 */       int indentColumns = 0;
/* 1488 */       boolean firstComment = true;
/* 1489 */       for (CommentLine commentLine : commentLines) {
/* 1490 */         if (commentLine.getCommentType() != CommentType.BLANK_LINE) {
/* 1491 */           if (firstComment) {
/* 1492 */             firstComment = false;
/* 1493 */             writeIndicator("#", (commentLine.getCommentType() == CommentType.IN_LINE), false, false);
/* 1494 */             indentColumns = (this.column > 0) ? (this.column - 1) : 0;
/*      */           } else {
/* 1496 */             writeWhitespace(indentColumns);
/* 1497 */             writeIndicator("#", false, false, false);
/*      */           } 
/* 1499 */           this.stream.write(commentLine.getValue());
/* 1500 */           writeLineBreak(null);
/*      */         } else {
/* 1502 */           writeLineBreak(null);
/* 1503 */           writeIndent();
/*      */         } 
/* 1505 */         wroteComment = true;
/*      */       } 
/*      */     } 
/* 1508 */     return wroteComment;
/*      */   }
/*      */   
/*      */   private void writeBlockComment() throws IOException {
/* 1512 */     if (!this.blockCommentsCollector.isEmpty()) {
/* 1513 */       writeIndent();
/* 1514 */       writeCommentLines(this.blockCommentsCollector.consume());
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean writeInlineComments() throws IOException {
/* 1519 */     return writeCommentLines(this.inlineCommentsCollector.consume());
/*      */   }
/*      */   
/*      */   private String determineBlockHints(String text) {
/* 1523 */     StringBuilder hints = new StringBuilder();
/* 1524 */     if (Constant.LINEBR.has(text.charAt(0), " ")) {
/* 1525 */       hints.append(this.bestIndent);
/*      */     }
/* 1527 */     char ch1 = text.charAt(text.length() - 1);
/* 1528 */     if (Constant.LINEBR.hasNo(ch1)) {
/* 1529 */       hints.append("-");
/* 1530 */     } else if (text.length() == 1 || Constant.LINEBR.has(text.charAt(text.length() - 2))) {
/* 1531 */       hints.append("+");
/*      */     } 
/* 1533 */     return hints.toString();
/*      */   }
/*      */   
/*      */   void writeFolded(String text, boolean split) throws IOException {
/* 1537 */     String hints = determineBlockHints(text);
/* 1538 */     writeIndicator(">" + hints, true, false, false);
/* 1539 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1540 */       this.openEnded = true;
/*      */     }
/* 1542 */     if (!writeInlineComments()) {
/* 1543 */       writeLineBreak(null);
/*      */     }
/* 1545 */     boolean leadingSpace = true;
/* 1546 */     boolean spaces = false;
/* 1547 */     boolean breaks = true;
/* 1548 */     int start = 0, end = 0;
/* 1549 */     while (end <= text.length()) {
/* 1550 */       char ch = Character.MIN_VALUE;
/* 1551 */       if (end < text.length()) {
/* 1552 */         ch = text.charAt(end);
/*      */       }
/* 1554 */       if (breaks) {
/* 1555 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1556 */           if (!leadingSpace && ch != '\000' && ch != ' ' && text.charAt(start) == '\n') {
/* 1557 */             writeLineBreak(null);
/*      */           }
/* 1559 */           leadingSpace = (ch == ' ');
/* 1560 */           String data = text.substring(start, end);
/* 1561 */           for (char br : data.toCharArray()) {
/* 1562 */             if (br == '\n') {
/* 1563 */               writeLineBreak(null);
/*      */             } else {
/* 1565 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1568 */           if (ch != '\000') {
/* 1569 */             writeIndent();
/*      */           }
/* 1571 */           start = end;
/*      */         } 
/* 1573 */       } else if (spaces) {
/* 1574 */         if (ch != ' ') {
/* 1575 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1576 */             writeIndent();
/*      */           } else {
/* 1578 */             int len = end - start;
/* 1579 */             this.column += len;
/* 1580 */             this.stream.write(text, start, len);
/*      */           } 
/* 1582 */           start = end;
/*      */         }
/*      */       
/* 1585 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1586 */         int len = end - start;
/* 1587 */         this.column += len;
/* 1588 */         this.stream.write(text, start, len);
/* 1589 */         if (ch == '\000') {
/* 1590 */           writeLineBreak(null);
/*      */         }
/* 1592 */         start = end;
/*      */       } 
/*      */       
/* 1595 */       if (ch != '\000') {
/* 1596 */         breaks = Constant.LINEBR.has(ch);
/* 1597 */         spaces = (ch == ' ');
/*      */       } 
/* 1599 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeLiteral(String text) throws IOException {
/* 1604 */     String hints = determineBlockHints(text);
/* 1605 */     writeIndicator("|" + hints, true, false, false);
/* 1606 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1607 */       this.openEnded = true;
/*      */     }
/* 1609 */     if (!writeInlineComments()) {
/* 1610 */       writeLineBreak(null);
/*      */     }
/* 1612 */     boolean breaks = true;
/* 1613 */     int start = 0, end = 0;
/* 1614 */     while (end <= text.length()) {
/* 1615 */       char ch = Character.MIN_VALUE;
/* 1616 */       if (end < text.length()) {
/* 1617 */         ch = text.charAt(end);
/*      */       }
/* 1619 */       if (breaks) {
/* 1620 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1621 */           String data = text.substring(start, end);
/* 1622 */           for (char br : data.toCharArray()) {
/* 1623 */             if (br == '\n') {
/* 1624 */               writeLineBreak(null);
/*      */             } else {
/* 1626 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1629 */           if (ch != '\000') {
/* 1630 */             writeIndent();
/*      */           }
/* 1632 */           start = end;
/*      */         }
/*      */       
/* 1635 */       } else if (ch == '\000' || Constant.LINEBR.has(ch)) {
/* 1636 */         this.stream.write(text, start, end - start);
/* 1637 */         if (ch == '\000') {
/* 1638 */           writeLineBreak(null);
/*      */         }
/* 1640 */         start = end;
/*      */       } 
/*      */       
/* 1643 */       if (ch != '\000') {
/* 1644 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1646 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writePlain(String text, boolean split) throws IOException {
/* 1651 */     if (this.rootContext) {
/* 1652 */       this.openEnded = true;
/*      */     }
/* 1654 */     if (text.length() == 0) {
/*      */       return;
/*      */     }
/* 1657 */     if (!this.whitespace) {
/* 1658 */       this.column++;
/* 1659 */       this.stream.write(SPACE);
/*      */     } 
/* 1661 */     this.whitespace = false;
/* 1662 */     this.indention = false;
/* 1663 */     boolean spaces = false;
/* 1664 */     boolean breaks = false;
/* 1665 */     int start = 0, end = 0;
/* 1666 */     while (end <= text.length()) {
/* 1667 */       char ch = Character.MIN_VALUE;
/* 1668 */       if (end < text.length()) {
/* 1669 */         ch = text.charAt(end);
/*      */       }
/* 1671 */       if (spaces) {
/* 1672 */         if (ch != ' ') {
/* 1673 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1674 */             writeIndent();
/* 1675 */             this.whitespace = false;
/* 1676 */             this.indention = false;
/*      */           } else {
/* 1678 */             int len = end - start;
/* 1679 */             this.column += len;
/* 1680 */             this.stream.write(text, start, len);
/*      */           } 
/* 1682 */           start = end;
/*      */         } 
/* 1684 */       } else if (breaks) {
/* 1685 */         if (Constant.LINEBR.hasNo(ch)) {
/* 1686 */           if (text.charAt(start) == '\n') {
/* 1687 */             writeLineBreak(null);
/*      */           }
/* 1689 */           String data = text.substring(start, end);
/* 1690 */           for (char br : data.toCharArray()) {
/* 1691 */             if (br == '\n') {
/* 1692 */               writeLineBreak(null);
/*      */             } else {
/* 1694 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1697 */           writeIndent();
/* 1698 */           this.whitespace = false;
/* 1699 */           this.indention = false;
/* 1700 */           start = end;
/*      */         }
/*      */       
/* 1703 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1704 */         int len = end - start;
/* 1705 */         this.column += len;
/* 1706 */         this.stream.write(text, start, len);
/* 1707 */         start = end;
/*      */       } 
/*      */       
/* 1710 */       if (ch != '\000') {
/* 1711 */         spaces = (ch == ' ');
/* 1712 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1714 */       end++;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\emitter\Emitter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */