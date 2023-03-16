/*      */ package org.yaml.snakeyaml.scanner;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.LoaderOptions;
/*      */ import org.yaml.snakeyaml.comments.CommentType;
/*      */ import org.yaml.snakeyaml.error.Mark;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.tokens.AliasToken;
/*      */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEndToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.CommentToken;
/*      */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentEndToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.KeyToken;
/*      */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*      */ import org.yaml.snakeyaml.tokens.TagToken;
/*      */ import org.yaml.snakeyaml.tokens.TagTuple;
/*      */ import org.yaml.snakeyaml.tokens.Token;
/*      */ import org.yaml.snakeyaml.tokens.ValueToken;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ import org.yaml.snakeyaml.util.UriEncoder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ScannerImpl
/*      */   implements Scanner
/*      */ {
/*   89 */   private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   99 */   public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  114 */   public static final Map<Character, Integer> ESCAPE_CODES = new HashMap<>();
/*      */   private final StreamReader reader;
/*      */   
/*      */   static {
/*  118 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
/*      */     
/*  120 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
/*      */     
/*  122 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
/*      */     
/*  124 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
/*      */     
/*  126 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
/*      */     
/*  128 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
/*      */     
/*  130 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
/*      */     
/*  132 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
/*      */     
/*  134 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
/*      */     
/*  136 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
/*      */     
/*  138 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*      */     
/*  140 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*      */     
/*  142 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
/*      */     
/*  144 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
/*      */     
/*  146 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
/*      */     
/*  148 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
/*      */ 
/*      */     
/*  151 */     ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
/*      */     
/*  153 */     ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
/*      */     
/*  155 */     ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean done = false;
/*      */ 
/*      */   
/*  163 */   private int flowLevel = 0;
/*      */ 
/*      */   
/*      */   private final List<Token> tokens;
/*      */ 
/*      */   
/*      */   private Token lastToken;
/*      */ 
/*      */   
/*  172 */   private int tokensTaken = 0;
/*      */ 
/*      */   
/*  175 */   private int indent = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean parseComments;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final LoaderOptions loaderOptions;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowSimpleKey = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Map<Integer, SimpleKey> possibleSimpleKeys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScannerImpl(StreamReader reader) {
/*  220 */     this(reader, new LoaderOptions());
/*      */   }
/*      */   
/*      */   public ScannerImpl(StreamReader reader, LoaderOptions options) {
/*  224 */     this.parseComments = options.isProcessComments();
/*  225 */     this.reader = reader;
/*  226 */     this.tokens = new ArrayList<>(100);
/*  227 */     this.indents = new ArrayStack(10);
/*      */     
/*  229 */     this.possibleSimpleKeys = new LinkedHashMap<>();
/*  230 */     this.loaderOptions = options;
/*  231 */     fetchStreamStart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ScannerImpl setParseComments(boolean parseComments) {
/*  242 */     this.parseComments = parseComments;
/*  243 */     return this;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public boolean isParseComments() {
/*  248 */     return this.parseComments;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkToken(Token.ID... choices) {
/*  255 */     while (needMoreTokens()) {
/*  256 */       fetchMoreTokens();
/*      */     }
/*  258 */     if (!this.tokens.isEmpty()) {
/*  259 */       if (choices.length == 0) {
/*  260 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  264 */       Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
/*  265 */       for (int i = 0; i < choices.length; i++) {
/*  266 */         if (first == choices[i]) {
/*  267 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  271 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token peekToken() {
/*  278 */     while (needMoreTokens()) {
/*  279 */       fetchMoreTokens();
/*      */     }
/*  281 */     return this.tokens.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getToken() {
/*  288 */     this.tokensTaken++;
/*  289 */     return this.tokens.remove(0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addToken(Token token) {
/*  295 */     this.lastToken = token;
/*  296 */     this.tokens.add(token);
/*      */   }
/*      */   
/*      */   private void addToken(int index, Token token) {
/*  300 */     if (index == this.tokens.size()) {
/*  301 */       this.lastToken = token;
/*      */     }
/*  303 */     this.tokens.add(index, token);
/*      */   }
/*      */   
/*      */   private void addAllTokens(List<Token> tokens) {
/*  307 */     this.lastToken = tokens.get(tokens.size() - 1);
/*  308 */     this.tokens.addAll(tokens);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreTokens() {
/*  316 */     if (this.done) {
/*  317 */       return false;
/*      */     }
/*      */     
/*  320 */     if (this.tokens.isEmpty()) {
/*  321 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  325 */     stalePossibleSimpleKeys();
/*  326 */     return (nextPossibleSimpleKey() == this.tokensTaken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchMoreTokens() {
/*  333 */     if (this.reader.getIndex() > this.loaderOptions.getCodePointLimit()) {
/*  334 */       throw new YAMLException("The incoming YAML document exceeds the limit: " + this.loaderOptions
/*  335 */           .getCodePointLimit() + " code points.");
/*      */     }
/*      */     
/*  338 */     scanToNextToken();
/*      */     
/*  340 */     stalePossibleSimpleKeys();
/*      */ 
/*      */     
/*  343 */     unwindIndent(this.reader.getColumn());
/*      */ 
/*      */     
/*  346 */     int c = this.reader.peek();
/*  347 */     switch (c) {
/*      */       
/*      */       case 0:
/*  350 */         fetchStreamEnd();
/*      */         return;
/*      */       
/*      */       case 37:
/*  354 */         if (checkDirective()) {
/*  355 */           fetchDirective();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 45:
/*  361 */         if (checkDocumentStart()) {
/*  362 */           fetchDocumentStart();
/*      */           return;
/*      */         } 
/*  365 */         if (checkBlockEntry()) {
/*  366 */           fetchBlockEntry();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 46:
/*  372 */         if (checkDocumentEnd()) {
/*  373 */           fetchDocumentEnd();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 91:
/*  380 */         fetchFlowSequenceStart();
/*      */         return;
/*      */       
/*      */       case 123:
/*  384 */         fetchFlowMappingStart();
/*      */         return;
/*      */       
/*      */       case 93:
/*  388 */         fetchFlowSequenceEnd();
/*      */         return;
/*      */       
/*      */       case 125:
/*  392 */         fetchFlowMappingEnd();
/*      */         return;
/*      */       
/*      */       case 44:
/*  396 */         fetchFlowEntry();
/*      */         return;
/*      */ 
/*      */       
/*      */       case 63:
/*  401 */         if (checkKey()) {
/*  402 */           fetchKey();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 58:
/*  408 */         if (checkValue()) {
/*  409 */           fetchValue();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 42:
/*  415 */         fetchAlias();
/*      */         return;
/*      */       
/*      */       case 38:
/*  419 */         fetchAnchor();
/*      */         return;
/*      */       
/*      */       case 33:
/*  423 */         fetchTag();
/*      */         return;
/*      */       
/*      */       case 124:
/*  427 */         if (this.flowLevel == 0) {
/*  428 */           fetchLiteral();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 62:
/*  434 */         if (this.flowLevel == 0) {
/*  435 */           fetchFolded();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 39:
/*  441 */         fetchSingle();
/*      */         return;
/*      */       
/*      */       case 34:
/*  445 */         fetchDouble();
/*      */         return;
/*      */     } 
/*      */     
/*  449 */     if (checkPlain()) {
/*  450 */       fetchPlain();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  456 */     String chRepresentation = escapeChar(String.valueOf(Character.toChars(c)));
/*  457 */     if (c == 9) {
/*  458 */       chRepresentation = chRepresentation + "(TAB)";
/*      */     }
/*  460 */     String text = String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { chRepresentation, chRepresentation });
/*      */ 
/*      */     
/*  463 */     throw new ScannerException("while scanning for the next token", null, text, this.reader.getMark());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String escapeChar(String chRepresentation) {
/*  470 */     for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
/*  471 */       String v = ESCAPE_REPLACEMENTS.get(s);
/*  472 */       if (v.equals(chRepresentation)) {
/*  473 */         return "\\" + s;
/*      */       }
/*      */     } 
/*  476 */     return chRepresentation;
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
/*      */   private int nextPossibleSimpleKey() {
/*  490 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  491 */       return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber();
/*      */     }
/*  493 */     return -1;
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
/*      */   private void stalePossibleSimpleKeys() {
/*  507 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  508 */       Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
/*  509 */       while (iterator.hasNext()) {
/*  510 */         SimpleKey key = iterator.next();
/*  511 */         if (key.getLine() != this.reader.getLine() || this.reader.getIndex() - key.getIndex() > 1024) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  516 */           if (key.isRequired())
/*      */           {
/*      */             
/*  519 */             throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader
/*  520 */                 .getMark());
/*      */           }
/*  522 */           iterator.remove();
/*      */         } 
/*      */       } 
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
/*      */   private void savePossibleSimpleKey() {
/*  540 */     boolean required = (this.flowLevel == 0 && this.indent == this.reader.getColumn());
/*      */     
/*  542 */     if (this.allowSimpleKey || !required) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  552 */       if (this.allowSimpleKey) {
/*  553 */         removePossibleSimpleKey();
/*  554 */         int tokenNumber = this.tokensTaken + this.tokens.size();
/*      */         
/*  556 */         SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
/*  557 */         this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
/*      */       } 
/*      */       return;
/*      */     } 
/*      */     throw new YAMLException("A simple key is required only if it is the first token in the current line");
/*      */   }
/*      */   
/*      */   private void removePossibleSimpleKey() {
/*  565 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  566 */     if (key != null && key.isRequired()) {
/*  567 */       throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader
/*  568 */           .getMark());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void unwindIndent(int col) {
/*  596 */     if (this.flowLevel != 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  601 */     while (this.indent > col) {
/*  602 */       Mark mark = this.reader.getMark();
/*  603 */       this.indent = ((Integer)this.indents.pop()).intValue();
/*  604 */       addToken((Token)new BlockEndToken(mark, mark));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addIndent(int column) {
/*  612 */     if (this.indent < column) {
/*  613 */       this.indents.push(Integer.valueOf(this.indent));
/*  614 */       this.indent = column;
/*  615 */       return true;
/*      */     } 
/*  617 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchStreamStart() {
/*  627 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  630 */     StreamStartToken streamStartToken = new StreamStartToken(mark, mark);
/*  631 */     addToken((Token)streamStartToken);
/*      */   }
/*      */ 
/*      */   
/*      */   private void fetchStreamEnd() {
/*  636 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  639 */     removePossibleSimpleKey();
/*  640 */     this.allowSimpleKey = false;
/*  641 */     this.possibleSimpleKeys.clear();
/*      */ 
/*      */     
/*  644 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  647 */     StreamEndToken streamEndToken = new StreamEndToken(mark, mark);
/*  648 */     addToken((Token)streamEndToken);
/*      */ 
/*      */     
/*  651 */     this.done = true;
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
/*      */   private void fetchDirective() {
/*  663 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  666 */     removePossibleSimpleKey();
/*  667 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  670 */     List<Token> tok = scanDirective();
/*  671 */     addAllTokens(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentStart() {
/*  678 */     fetchDocumentIndicator(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentEnd() {
/*  685 */     fetchDocumentIndicator(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentIndicator(boolean isDocumentStart) {
/*      */     DocumentEndToken documentEndToken;
/*  694 */     unwindIndent(-1);
/*      */ 
/*      */ 
/*      */     
/*  698 */     removePossibleSimpleKey();
/*  699 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  702 */     Mark startMark = this.reader.getMark();
/*  703 */     this.reader.forward(3);
/*  704 */     Mark endMark = this.reader.getMark();
/*      */     
/*  706 */     if (isDocumentStart) {
/*  707 */       DocumentStartToken documentStartToken = new DocumentStartToken(startMark, endMark);
/*      */     } else {
/*  709 */       documentEndToken = new DocumentEndToken(startMark, endMark);
/*      */     } 
/*  711 */     addToken((Token)documentEndToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceStart() {
/*  715 */     fetchFlowCollectionStart(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingStart() {
/*  719 */     fetchFlowCollectionStart(true);
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
/*      */   private void fetchFlowCollectionStart(boolean isMappingStart) {
/*      */     FlowSequenceStartToken flowSequenceStartToken;
/*  735 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  738 */     this.flowLevel++;
/*      */ 
/*      */     
/*  741 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  744 */     Mark startMark = this.reader.getMark();
/*  745 */     this.reader.forward(1);
/*  746 */     Mark endMark = this.reader.getMark();
/*      */     
/*  748 */     if (isMappingStart) {
/*  749 */       FlowMappingStartToken flowMappingStartToken = new FlowMappingStartToken(startMark, endMark);
/*      */     } else {
/*  751 */       flowSequenceStartToken = new FlowSequenceStartToken(startMark, endMark);
/*      */     } 
/*  753 */     addToken((Token)flowSequenceStartToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceEnd() {
/*  757 */     fetchFlowCollectionEnd(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingEnd() {
/*  761 */     fetchFlowCollectionEnd(true);
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
/*      */   private void fetchFlowCollectionEnd(boolean isMappingEnd) {
/*      */     FlowSequenceEndToken flowSequenceEndToken;
/*  775 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  778 */     this.flowLevel--;
/*      */ 
/*      */     
/*  781 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  784 */     Mark startMark = this.reader.getMark();
/*  785 */     this.reader.forward();
/*  786 */     Mark endMark = this.reader.getMark();
/*      */     
/*  788 */     if (isMappingEnd) {
/*  789 */       FlowMappingEndToken flowMappingEndToken = new FlowMappingEndToken(startMark, endMark);
/*      */     } else {
/*  791 */       flowSequenceEndToken = new FlowSequenceEndToken(startMark, endMark);
/*      */     } 
/*  793 */     addToken((Token)flowSequenceEndToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowEntry() {
/*  804 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  807 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  810 */     Mark startMark = this.reader.getMark();
/*  811 */     this.reader.forward();
/*  812 */     Mark endMark = this.reader.getMark();
/*  813 */     FlowEntryToken flowEntryToken = new FlowEntryToken(startMark, endMark);
/*  814 */     addToken((Token)flowEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockEntry() {
/*  824 */     if (this.flowLevel == 0) {
/*      */       
/*  826 */       if (!this.allowSimpleKey) {
/*  827 */         throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader
/*  828 */             .getMark());
/*      */       }
/*      */ 
/*      */       
/*  832 */       if (addIndent(this.reader.getColumn())) {
/*  833 */         Mark mark = this.reader.getMark();
/*  834 */         addToken((Token)new BlockSequenceStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  841 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  844 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  847 */     Mark startMark = this.reader.getMark();
/*  848 */     this.reader.forward();
/*  849 */     Mark endMark = this.reader.getMark();
/*  850 */     BlockEntryToken blockEntryToken = new BlockEntryToken(startMark, endMark);
/*  851 */     addToken((Token)blockEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchKey() {
/*  861 */     if (this.flowLevel == 0) {
/*      */       
/*  863 */       if (!this.allowSimpleKey) {
/*  864 */         throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader
/*  865 */             .getMark());
/*      */       }
/*      */       
/*  868 */       if (addIndent(this.reader.getColumn())) {
/*  869 */         Mark mark = this.reader.getMark();
/*  870 */         addToken((Token)new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */     
/*  874 */     this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */     
/*  877 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  880 */     Mark startMark = this.reader.getMark();
/*  881 */     this.reader.forward();
/*  882 */     Mark endMark = this.reader.getMark();
/*  883 */     KeyToken keyToken = new KeyToken(startMark, endMark);
/*  884 */     addToken((Token)keyToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchValue() {
/*  894 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  895 */     if (key != null) {
/*      */       
/*  897 */       addToken(key.getTokenNumber() - this.tokensTaken, (Token)new KeyToken(key.getMark(), key.getMark()));
/*      */ 
/*      */ 
/*      */       
/*  901 */       if (this.flowLevel == 0 && 
/*  902 */         addIndent(key.getColumn())) {
/*  903 */         addToken(key.getTokenNumber() - this.tokensTaken, (Token)new BlockMappingStartToken(key
/*  904 */               .getMark(), key.getMark()));
/*      */       }
/*      */ 
/*      */       
/*  908 */       this.allowSimpleKey = false;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  914 */       if (this.flowLevel == 0)
/*      */       {
/*      */ 
/*      */         
/*  918 */         if (!this.allowSimpleKey) {
/*  919 */           throw new ScannerException(null, null, "mapping values are not allowed here", this.reader
/*  920 */               .getMark());
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  927 */       if (this.flowLevel == 0 && 
/*  928 */         addIndent(this.reader.getColumn())) {
/*  929 */         Mark mark = this.reader.getMark();
/*  930 */         addToken((Token)new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  935 */       this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */       
/*  938 */       removePossibleSimpleKey();
/*      */     } 
/*      */     
/*  941 */     Mark startMark = this.reader.getMark();
/*  942 */     this.reader.forward();
/*  943 */     Mark endMark = this.reader.getMark();
/*  944 */     ValueToken valueToken = new ValueToken(startMark, endMark);
/*  945 */     addToken((Token)valueToken);
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
/*      */   private void fetchAlias() {
/*  959 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  962 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  965 */     Token tok = scanAnchor(false);
/*  966 */     addToken(tok);
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
/*      */   private void fetchAnchor() {
/*  980 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  983 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  986 */     Token tok = scanAnchor(true);
/*  987 */     addToken(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchTag() {
/*  997 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/* 1000 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1003 */     Token tok = scanTag();
/* 1004 */     addToken(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchLiteral() {
/* 1014 */     fetchBlockScalar('|');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFolded() {
/* 1024 */     fetchBlockScalar('>');
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
/*      */   private void fetchBlockScalar(char style) {
/* 1036 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/* 1039 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/* 1042 */     List<Token> tok = scanBlockScalar(style);
/* 1043 */     addAllTokens(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchSingle() {
/* 1050 */     fetchFlowScalar('\'');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDouble() {
/* 1057 */     fetchFlowScalar('"');
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
/*      */   private void fetchFlowScalar(char style) {
/* 1069 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/* 1072 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1075 */     Token tok = scanFlowScalar(style);
/* 1076 */     addToken(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchPlain() {
/* 1084 */     savePossibleSimpleKey();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1089 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1092 */     Token tok = scanPlain();
/* 1093 */     addToken(tok);
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
/*      */   private boolean checkDirective() {
/* 1107 */     return (this.reader.getColumn() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentStart() {
/* 1116 */     if (this.reader.getColumn() == 0) {
/* 1117 */       return ("---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)));
/*      */     }
/* 1119 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentEnd() {
/* 1128 */     if (this.reader.getColumn() == 0) {
/* 1129 */       return ("...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)));
/*      */     }
/* 1131 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkBlockEntry() {
/* 1139 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkKey() {
/* 1147 */     if (this.flowLevel != 0) {
/* 1148 */       return true;
/*      */     }
/*      */     
/* 1151 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkValue() {
/* 1160 */     if (this.flowLevel != 0) {
/* 1161 */       return true;
/*      */     }
/*      */     
/* 1164 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
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
/*      */   private boolean checkPlain() {
/* 1188 */     int c = this.reader.peek();
/*      */ 
/*      */     
/* 1191 */     return (Constant.NULL_BL_T_LINEBR.hasNo(c, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR
/* 1192 */       .hasNo(this.reader.peek(1)) && (c == 45 || (this.flowLevel == 0 && "?:"
/* 1193 */       .indexOf(c) != -1))));
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
/*      */   private void scanToNextToken() {
/* 1222 */     if (this.reader.getIndex() == 0 && this.reader.peek() == 65279) {
/* 1223 */       this.reader.forward();
/*      */     }
/* 1225 */     boolean found = false;
/* 1226 */     int inlineStartColumn = -1;
/* 1227 */     while (!found) {
/* 1228 */       Mark startMark = this.reader.getMark();
/* 1229 */       int columnBeforeComment = this.reader.getColumn();
/* 1230 */       boolean commentSeen = false;
/* 1231 */       int ff = 0;
/*      */ 
/*      */       
/* 1234 */       while (this.reader.peek(ff) == 32) {
/* 1235 */         ff++;
/*      */       }
/* 1237 */       if (ff > 0) {
/* 1238 */         this.reader.forward(ff);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1244 */       if (this.reader.peek() == 35) {
/* 1245 */         CommentType type; commentSeen = true;
/*      */         
/* 1247 */         if (columnBeforeComment != 0 && (this.lastToken == null || this.lastToken
/* 1248 */           .getTokenId() != Token.ID.BlockEntry)) {
/* 1249 */           type = CommentType.IN_LINE;
/* 1250 */           inlineStartColumn = this.reader.getColumn();
/* 1251 */         } else if (inlineStartColumn == this.reader.getColumn()) {
/* 1252 */           type = CommentType.IN_LINE;
/*      */         } else {
/* 1254 */           inlineStartColumn = -1;
/* 1255 */           type = CommentType.BLOCK;
/*      */         } 
/* 1257 */         CommentToken token = scanComment(type);
/* 1258 */         if (this.parseComments) {
/* 1259 */           addToken((Token)token);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1264 */       String breaks = scanLineBreak();
/* 1265 */       if (breaks.length() != 0) {
/* 1266 */         if (this.parseComments && !commentSeen && 
/* 1267 */           columnBeforeComment == 0) {
/* 1268 */           Mark endMark = this.reader.getMark();
/* 1269 */           addToken((Token)new CommentToken(CommentType.BLANK_LINE, breaks, startMark, endMark));
/*      */         } 
/*      */         
/* 1272 */         if (this.flowLevel == 0)
/*      */         {
/*      */           
/* 1275 */           this.allowSimpleKey = true; } 
/*      */         continue;
/*      */       } 
/* 1278 */       found = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CommentToken scanComment(CommentType type) {
/* 1285 */     Mark startMark = this.reader.getMark();
/* 1286 */     this.reader.forward();
/* 1287 */     int length = 0;
/* 1288 */     while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1289 */       length++;
/*      */     }
/* 1291 */     String value = this.reader.prefixForward(length);
/* 1292 */     Mark endMark = this.reader.getMark();
/* 1293 */     return new CommentToken(type, value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Token> scanDirective() {
/* 1299 */     Mark endMark, startMark = this.reader.getMark();
/*      */     
/* 1301 */     this.reader.forward();
/* 1302 */     String name = scanDirectiveName(startMark);
/* 1303 */     List<?> value = null;
/* 1304 */     if ("YAML".equals(name)) {
/* 1305 */       value = scanYamlDirectiveValue(startMark);
/* 1306 */       endMark = this.reader.getMark();
/* 1307 */     } else if ("TAG".equals(name)) {
/* 1308 */       value = scanTagDirectiveValue(startMark);
/* 1309 */       endMark = this.reader.getMark();
/*      */     } else {
/* 1311 */       endMark = this.reader.getMark();
/* 1312 */       int ff = 0;
/* 1313 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1314 */         ff++;
/*      */       }
/* 1316 */       if (ff > 0) {
/* 1317 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/* 1320 */     CommentToken commentToken = scanDirectiveIgnoredLine(startMark);
/* 1321 */     DirectiveToken token = new DirectiveToken(name, value, startMark, endMark);
/* 1322 */     return makeTokenList(new Token[] { (Token)token, (Token)commentToken });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanDirectiveName(Mark startMark) {
/* 1332 */     int length = 0;
/*      */ 
/*      */ 
/*      */     
/* 1336 */     int c = this.reader.peek(length);
/* 1337 */     while (Constant.ALPHA.has(c)) {
/* 1338 */       length++;
/* 1339 */       c = this.reader.peek(length);
/*      */     } 
/*      */     
/* 1342 */     if (length == 0) {
/* 1343 */       String s = String.valueOf(Character.toChars(c));
/* 1344 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1346 */           .getMark());
/*      */     } 
/* 1348 */     String value = this.reader.prefixForward(length);
/* 1349 */     c = this.reader.peek();
/* 1350 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1351 */       String s = String.valueOf(Character.toChars(c));
/* 1352 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1354 */           .getMark());
/*      */     } 
/* 1356 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<Integer> scanYamlDirectiveValue(Mark startMark) {
/* 1361 */     while (this.reader.peek() == 32) {
/* 1362 */       this.reader.forward();
/*      */     }
/* 1364 */     Integer major = scanYamlDirectiveNumber(startMark);
/* 1365 */     int c = this.reader.peek();
/* 1366 */     if (c != 46) {
/* 1367 */       String s = String.valueOf(Character.toChars(c));
/* 1368 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + s + "(" + c + ")", this.reader
/* 1369 */           .getMark());
/*      */     } 
/* 1371 */     this.reader.forward();
/* 1372 */     Integer minor = scanYamlDirectiveNumber(startMark);
/* 1373 */     c = this.reader.peek();
/* 1374 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1375 */       String s = String.valueOf(Character.toChars(c));
/* 1376 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + s + "(" + c + ")", this.reader
/* 1377 */           .getMark());
/*      */     } 
/* 1379 */     List<Integer> result = new ArrayList<>(2);
/* 1380 */     result.add(major);
/* 1381 */     result.add(minor);
/* 1382 */     return result;
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
/*      */   private Integer scanYamlDirectiveNumber(Mark startMark) {
/* 1394 */     int c = this.reader.peek();
/* 1395 */     if (!Character.isDigit(c)) {
/* 1396 */       String s = String.valueOf(Character.toChars(c));
/* 1397 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + s + "(" + c + ")", this.reader
/* 1398 */           .getMark());
/*      */     } 
/* 1400 */     int length = 0;
/* 1401 */     while (Character.isDigit(this.reader.peek(length))) {
/* 1402 */       length++;
/*      */     }
/* 1404 */     Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
/* 1405 */     return value;
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
/*      */   private List<String> scanTagDirectiveValue(Mark startMark) {
/* 1422 */     while (this.reader.peek() == 32) {
/* 1423 */       this.reader.forward();
/*      */     }
/* 1425 */     String handle = scanTagDirectiveHandle(startMark);
/* 1426 */     while (this.reader.peek() == 32) {
/* 1427 */       this.reader.forward();
/*      */     }
/* 1429 */     String prefix = scanTagDirectivePrefix(startMark);
/* 1430 */     List<String> result = new ArrayList<>(2);
/* 1431 */     result.add(handle);
/* 1432 */     result.add(prefix);
/* 1433 */     return result;
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
/*      */   private String scanTagDirectiveHandle(Mark startMark) {
/* 1445 */     String value = scanTagHandle("directive", startMark);
/* 1446 */     int c = this.reader.peek();
/* 1447 */     if (c != 32) {
/* 1448 */       String s = String.valueOf(Character.toChars(c));
/* 1449 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader
/* 1450 */           .getMark());
/*      */     } 
/* 1452 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectivePrefix(Mark startMark) {
/* 1462 */     String value = scanTagUri("directive", startMark);
/* 1463 */     int c = this.reader.peek();
/* 1464 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1465 */       String s = String.valueOf(Character.toChars(c));
/* 1466 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader
/* 1467 */           .getMark());
/*      */     } 
/* 1469 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private CommentToken scanDirectiveIgnoredLine(Mark startMark) {
/* 1474 */     while (this.reader.peek() == 32) {
/* 1475 */       this.reader.forward();
/*      */     }
/* 1477 */     CommentToken commentToken = null;
/* 1478 */     if (this.reader.peek() == 35) {
/* 1479 */       CommentToken comment = scanComment(CommentType.IN_LINE);
/* 1480 */       if (this.parseComments) {
/* 1481 */         commentToken = comment;
/*      */       }
/*      */     } 
/* 1484 */     int c = this.reader.peek();
/* 1485 */     String lineBreak = scanLineBreak();
/* 1486 */     if (lineBreak.length() == 0 && c != 0) {
/* 1487 */       String s = String.valueOf(Character.toChars(c));
/* 1488 */       throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader
/* 1489 */           .getMark());
/*      */     } 
/* 1491 */     return commentToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanAnchor(boolean isAnchor) {
/*      */     AliasToken aliasToken;
/* 1503 */     Mark startMark = this.reader.getMark();
/* 1504 */     int indicator = this.reader.peek();
/* 1505 */     String name = (indicator == 42) ? "alias" : "anchor";
/* 1506 */     this.reader.forward();
/* 1507 */     int length = 0;
/* 1508 */     int c = this.reader.peek(length);
/* 1509 */     while (Constant.NULL_BL_T_LINEBR.hasNo(c, ":,[]{}/.*&")) {
/* 1510 */       length++;
/* 1511 */       c = this.reader.peek(length);
/*      */     } 
/* 1513 */     if (length == 0) {
/* 1514 */       String s = String.valueOf(Character.toChars(c));
/* 1515 */       throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader
/* 1516 */           .getMark());
/*      */     } 
/* 1518 */     String value = this.reader.prefixForward(length);
/* 1519 */     c = this.reader.peek();
/* 1520 */     if (Constant.NULL_BL_T_LINEBR.hasNo(c, "?:,]}%@`")) {
/* 1521 */       String s = String.valueOf(Character.toChars(c));
/* 1522 */       throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader
/* 1523 */           .getMark());
/*      */     } 
/* 1525 */     Mark endMark = this.reader.getMark();
/*      */     
/* 1527 */     if (isAnchor) {
/* 1528 */       AnchorToken anchorToken = new AnchorToken(value, startMark, endMark);
/*      */     } else {
/* 1530 */       aliasToken = new AliasToken(value, startMark, endMark);
/*      */     } 
/* 1532 */     return (Token)aliasToken;
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
/*      */   private Token scanTag() {
/* 1566 */     Mark startMark = this.reader.getMark();
/*      */ 
/*      */     
/* 1569 */     int c = this.reader.peek(1);
/* 1570 */     String handle = null;
/* 1571 */     String suffix = null;
/*      */     
/* 1573 */     if (c == 60) {
/*      */ 
/*      */       
/* 1576 */       this.reader.forward(2);
/* 1577 */       suffix = scanTagUri("tag", startMark);
/* 1578 */       c = this.reader.peek();
/* 1579 */       if (c != 62) {
/*      */ 
/*      */         
/* 1582 */         String s = String.valueOf(Character.toChars(c));
/* 1583 */         throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + s + "' (" + c + ")", this.reader
/* 1584 */             .getMark());
/*      */       } 
/* 1586 */       this.reader.forward();
/* 1587 */     } else if (Constant.NULL_BL_T_LINEBR.has(c)) {
/*      */ 
/*      */       
/* 1590 */       suffix = "!";
/* 1591 */       this.reader.forward();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1597 */       int length = 1;
/* 1598 */       boolean useHandle = false;
/* 1599 */       while (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1600 */         if (c == 33) {
/* 1601 */           useHandle = true;
/*      */           break;
/*      */         } 
/* 1604 */         length++;
/* 1605 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */       
/* 1609 */       if (useHandle) {
/* 1610 */         handle = scanTagHandle("tag", startMark);
/*      */       } else {
/* 1612 */         handle = "!";
/* 1613 */         this.reader.forward();
/*      */       } 
/* 1615 */       suffix = scanTagUri("tag", startMark);
/*      */     } 
/* 1617 */     c = this.reader.peek();
/*      */ 
/*      */     
/* 1620 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1621 */       String s = String.valueOf(Character.toChars(c));
/* 1622 */       throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + s + "' (" + c + ")", this.reader
/* 1623 */           .getMark());
/*      */     } 
/* 1625 */     TagTuple value = new TagTuple(handle, suffix);
/* 1626 */     Mark endMark = this.reader.getMark();
/* 1627 */     return (Token)new TagToken(value, startMark, endMark);
/*      */   }
/*      */ 
/*      */   
/*      */   private List<Token> scanBlockScalar(char style) {
/*      */     String breaks;
/*      */     int indent;
/*      */     Mark mark1;
/* 1635 */     boolean folded = (style == '>');
/* 1636 */     StringBuilder chunks = new StringBuilder();
/* 1637 */     Mark startMark = this.reader.getMark();
/*      */     
/* 1639 */     this.reader.forward();
/* 1640 */     Chomping chompi = scanBlockScalarIndicators(startMark);
/* 1641 */     int increment = chompi.getIncrement();
/* 1642 */     CommentToken commentToken = scanBlockScalarIgnoredLine(startMark);
/*      */ 
/*      */     
/* 1645 */     int minIndent = this.indent + 1;
/* 1646 */     if (minIndent < 1) {
/* 1647 */       minIndent = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1653 */     if (increment == -1) {
/* 1654 */       Object[] brme = scanBlockScalarIndentation();
/* 1655 */       breaks = (String)brme[0];
/* 1656 */       int maxIndent = ((Integer)brme[1]).intValue();
/* 1657 */       mark1 = (Mark)brme[2];
/* 1658 */       indent = Math.max(minIndent, maxIndent);
/*      */     } else {
/* 1660 */       indent = minIndent + increment - 1;
/* 1661 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1662 */       breaks = (String)brme[0];
/* 1663 */       mark1 = (Mark)brme[1];
/*      */     } 
/*      */     
/* 1666 */     String lineBreak = "";
/*      */ 
/*      */     
/* 1669 */     while (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/* 1670 */       chunks.append(breaks);
/* 1671 */       boolean leadingNonSpace = (" \t".indexOf(this.reader.peek()) == -1);
/* 1672 */       int length = 0;
/* 1673 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1674 */         length++;
/*      */       }
/* 1676 */       chunks.append(this.reader.prefixForward(length));
/* 1677 */       lineBreak = scanLineBreak();
/* 1678 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1679 */       breaks = (String)brme[0];
/* 1680 */       mark1 = (Mark)brme[1];
/* 1681 */       if (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1686 */         if (folded && "\n".equals(lineBreak) && leadingNonSpace && " \t"
/* 1687 */           .indexOf(this.reader.peek()) == -1) {
/* 1688 */           if (breaks.length() == 0)
/* 1689 */             chunks.append(" "); 
/*      */           continue;
/*      */         } 
/* 1692 */         chunks.append(lineBreak);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1701 */     if (chompi.chompTailIsNotFalse()) {
/* 1702 */       chunks.append(lineBreak);
/*      */     }
/* 1704 */     if (chompi.chompTailIsTrue()) {
/* 1705 */       chunks.append(breaks);
/*      */     }
/*      */ 
/*      */     
/* 1709 */     ScalarToken scalarToken = new ScalarToken(chunks.toString(), false, startMark, mark1, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
/* 1710 */     return makeTokenList(new Token[] { (Token)commentToken, (Token)scalarToken });
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
/*      */   private Chomping scanBlockScalarIndicators(Mark startMark) {
/* 1730 */     Boolean chomping = null;
/* 1731 */     int increment = -1;
/* 1732 */     int c = this.reader.peek();
/* 1733 */     if (c == 45 || c == 43) {
/* 1734 */       if (c == 43) {
/* 1735 */         chomping = Boolean.TRUE;
/*      */       } else {
/* 1737 */         chomping = Boolean.FALSE;
/*      */       } 
/* 1739 */       this.reader.forward();
/* 1740 */       c = this.reader.peek();
/* 1741 */       if (Character.isDigit(c)) {
/* 1742 */         String s = String.valueOf(Character.toChars(c));
/* 1743 */         increment = Integer.parseInt(s);
/* 1744 */         if (increment == 0) {
/* 1745 */           throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader
/* 1746 */               .getMark());
/*      */         }
/* 1748 */         this.reader.forward();
/*      */       } 
/* 1750 */     } else if (Character.isDigit(c)) {
/* 1751 */       String s = String.valueOf(Character.toChars(c));
/* 1752 */       increment = Integer.parseInt(s);
/* 1753 */       if (increment == 0) {
/* 1754 */         throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader
/* 1755 */             .getMark());
/*      */       }
/* 1757 */       this.reader.forward();
/* 1758 */       c = this.reader.peek();
/* 1759 */       if (c == 45 || c == 43) {
/* 1760 */         if (c == 43) {
/* 1761 */           chomping = Boolean.TRUE;
/*      */         } else {
/* 1763 */           chomping = Boolean.FALSE;
/*      */         } 
/* 1765 */         this.reader.forward();
/*      */       } 
/*      */     } 
/* 1768 */     c = this.reader.peek();
/* 1769 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1770 */       String s = String.valueOf(Character.toChars(c));
/* 1771 */       throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1773 */           .getMark());
/*      */     } 
/* 1775 */     return new Chomping(chomping, increment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CommentToken scanBlockScalarIgnoredLine(Mark startMark) {
/* 1786 */     while (this.reader.peek() == 32) {
/* 1787 */       this.reader.forward();
/*      */     }
/*      */ 
/*      */     
/* 1791 */     CommentToken commentToken = null;
/* 1792 */     if (this.reader.peek() == 35) {
/* 1793 */       commentToken = scanComment(CommentType.IN_LINE);
/*      */     }
/*      */ 
/*      */     
/* 1797 */     int c = this.reader.peek();
/* 1798 */     String lineBreak = scanLineBreak();
/* 1799 */     if (lineBreak.length() == 0 && c != 0) {
/* 1800 */       String s = String.valueOf(Character.toChars(c));
/* 1801 */       throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader
/* 1802 */           .getMark());
/*      */     } 
/* 1804 */     return commentToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarIndentation() {
/* 1815 */     StringBuilder chunks = new StringBuilder();
/* 1816 */     int maxIndent = 0;
/* 1817 */     Mark endMark = this.reader.getMark();
/*      */ 
/*      */ 
/*      */     
/* 1821 */     while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
/* 1822 */       if (this.reader.peek() != 32) {
/*      */ 
/*      */         
/* 1825 */         chunks.append(scanLineBreak());
/* 1826 */         endMark = this.reader.getMark();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1831 */       this.reader.forward();
/* 1832 */       if (this.reader.getColumn() > maxIndent) {
/* 1833 */         maxIndent = this.reader.getColumn();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1838 */     return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
/*      */   }
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarBreaks(int indent) {
/* 1843 */     StringBuilder chunks = new StringBuilder();
/* 1844 */     Mark endMark = this.reader.getMark();
/* 1845 */     int col = this.reader.getColumn();
/*      */ 
/*      */     
/* 1848 */     while (col < indent && this.reader.peek() == 32) {
/* 1849 */       this.reader.forward();
/* 1850 */       col++;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1855 */     String lineBreak = null;
/* 1856 */     while ((lineBreak = scanLineBreak()).length() != 0) {
/* 1857 */       chunks.append(lineBreak);
/* 1858 */       endMark = this.reader.getMark();
/*      */ 
/*      */       
/* 1861 */       col = this.reader.getColumn();
/* 1862 */       while (col < indent && this.reader.peek() == 32) {
/* 1863 */         this.reader.forward();
/* 1864 */         col++;
/*      */       } 
/*      */     } 
/*      */     
/* 1868 */     return new Object[] { chunks.toString(), endMark };
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
/*      */   private Token scanFlowScalar(char style) {
/* 1890 */     boolean _double = (style == '"');
/* 1891 */     StringBuilder chunks = new StringBuilder();
/* 1892 */     Mark startMark = this.reader.getMark();
/* 1893 */     int quote = this.reader.peek();
/* 1894 */     this.reader.forward();
/* 1895 */     chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/* 1896 */     while (this.reader.peek() != quote) {
/* 1897 */       chunks.append(scanFlowScalarSpaces(startMark));
/* 1898 */       chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/*      */     } 
/* 1900 */     this.reader.forward();
/* 1901 */     Mark endMark = this.reader.getMark();
/* 1902 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, endMark, 
/* 1903 */         DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark) {
/* 1911 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1915 */       int length = 0;
/* 1916 */       while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\")) {
/* 1917 */         length++;
/*      */       }
/* 1919 */       if (length != 0) {
/* 1920 */         chunks.append(this.reader.prefixForward(length));
/*      */       }
/*      */ 
/*      */       
/* 1924 */       int c = this.reader.peek();
/* 1925 */       if (!doubleQuoted && c == 39 && this.reader.peek(1) == 39) {
/* 1926 */         chunks.append("'");
/* 1927 */         this.reader.forward(2); continue;
/* 1928 */       }  if ((doubleQuoted && c == 39) || (!doubleQuoted && "\"\\".indexOf(c) != -1)) {
/* 1929 */         chunks.appendCodePoint(c);
/* 1930 */         this.reader.forward(); continue;
/* 1931 */       }  if (doubleQuoted && c == 92) {
/* 1932 */         this.reader.forward();
/* 1933 */         c = this.reader.peek();
/* 1934 */         if (!Character.isSupplementaryCodePoint(c) && ESCAPE_REPLACEMENTS
/* 1935 */           .containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */ 
/*      */           
/* 1939 */           chunks.append(ESCAPE_REPLACEMENTS.get(Character.valueOf((char)c)));
/* 1940 */           this.reader.forward(); continue;
/* 1941 */         }  if (!Character.isSupplementaryCodePoint(c) && ESCAPE_CODES
/* 1942 */           .containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */           
/* 1945 */           length = ((Integer)ESCAPE_CODES.get(Character.valueOf((char)c))).intValue();
/* 1946 */           this.reader.forward();
/* 1947 */           String hex = this.reader.prefix(length);
/* 1948 */           if (NOT_HEXA.matcher(hex).find()) {
/* 1949 */             throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader
/*      */                 
/* 1951 */                 .getMark());
/*      */           }
/* 1953 */           int decimal = Integer.parseInt(hex, 16);
/* 1954 */           String unicode = new String(Character.toChars(decimal));
/* 1955 */           chunks.append(unicode);
/* 1956 */           this.reader.forward(length); continue;
/* 1957 */         }  if (scanLineBreak().length() != 0) {
/* 1958 */           chunks.append(scanFlowScalarBreaks(startMark)); continue;
/*      */         } 
/* 1960 */         String s = String.valueOf(Character.toChars(c));
/* 1961 */         throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + s + "(" + c + ")", this.reader
/* 1962 */             .getMark());
/*      */       }  break;
/*      */     } 
/* 1965 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarSpaces(Mark startMark) {
/* 1972 */     StringBuilder chunks = new StringBuilder();
/* 1973 */     int length = 0;
/*      */ 
/*      */     
/* 1976 */     while (" \t".indexOf(this.reader.peek(length)) != -1) {
/* 1977 */       length++;
/*      */     }
/* 1979 */     String whitespaces = this.reader.prefixForward(length);
/* 1980 */     int c = this.reader.peek();
/* 1981 */     if (c == 0)
/*      */     {
/* 1983 */       throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader
/* 1984 */           .getMark());
/*      */     }
/*      */     
/* 1987 */     String lineBreak = scanLineBreak();
/* 1988 */     if (lineBreak.length() != 0) {
/* 1989 */       String breaks = scanFlowScalarBreaks(startMark);
/* 1990 */       if (!"\n".equals(lineBreak)) {
/* 1991 */         chunks.append(lineBreak);
/* 1992 */       } else if (breaks.length() == 0) {
/* 1993 */         chunks.append(" ");
/*      */       } 
/* 1995 */       chunks.append(breaks);
/*      */     } else {
/* 1997 */       chunks.append(whitespaces);
/*      */     } 
/* 1999 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanFlowScalarBreaks(Mark startMark) {
/* 2004 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 2008 */       String prefix = this.reader.prefix(3);
/* 2009 */       if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR
/* 2010 */         .has(this.reader.peek(3))) {
/* 2011 */         throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader
/* 2012 */             .getMark());
/*      */       }
/*      */       
/* 2015 */       while (" \t".indexOf(this.reader.peek()) != -1) {
/* 2016 */         this.reader.forward();
/*      */       }
/*      */ 
/*      */       
/* 2020 */       String lineBreak = scanLineBreak();
/* 2021 */       if (lineBreak.length() != 0) {
/* 2022 */         chunks.append(lineBreak); continue;
/*      */       }  break;
/* 2024 */     }  return chunks.toString();
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
/*      */   private Token scanPlain() {
/* 2041 */     StringBuilder chunks = new StringBuilder();
/* 2042 */     Mark startMark = this.reader.getMark();
/* 2043 */     Mark endMark = startMark;
/* 2044 */     int indent = this.indent + 1;
/* 2045 */     String spaces = "";
/*      */     
/*      */     do {
/* 2048 */       int length = 0;
/*      */       
/* 2050 */       if (this.reader.peek() == 35) {
/*      */         break;
/*      */       }
/*      */       while (true) {
/* 2054 */         int c = this.reader.peek(length);
/* 2055 */         if (Constant.NULL_BL_T_LINEBR.has(c) || (c == 58 && Constant.NULL_BL_T_LINEBR
/* 2056 */           .has(this.reader.peek(length + 1), (this.flowLevel != 0) ? ",[]{}" : "")) || (this.flowLevel != 0 && ",?[]{}"
/*      */           
/* 2058 */           .indexOf(c) != -1)) {
/*      */           break;
/*      */         }
/* 2061 */         length++;
/*      */       } 
/* 2063 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 2066 */       this.allowSimpleKey = false;
/* 2067 */       chunks.append(spaces);
/* 2068 */       chunks.append(this.reader.prefixForward(length));
/* 2069 */       endMark = this.reader.getMark();
/* 2070 */       spaces = scanPlainSpaces();
/*      */     }
/* 2072 */     while (spaces.length() != 0 && this.reader.peek() != 35 && (this.flowLevel != 0 || this.reader
/* 2073 */       .getColumn() >= indent));
/*      */ 
/*      */ 
/*      */     
/* 2077 */     return (Token)new ScalarToken(chunks.toString(), startMark, endMark, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean atEndOfPlain() {
/* 2085 */     int wsLength = 0;
/* 2086 */     int wsColumn = this.reader.getColumn();
/*      */     
/*      */     int c;
/* 2089 */     while ((c = this.reader.peek(wsLength)) != 0 && Constant.NULL_BL_T_LINEBR.has(c)) {
/* 2090 */       wsLength++;
/* 2091 */       if (!Constant.LINEBR.has(c) && (c != 13 || this.reader.peek(wsLength + 1) != 10) && c != 65279) {
/*      */         
/* 2093 */         wsColumn++; continue;
/*      */       } 
/* 2095 */       wsColumn = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2102 */     if (this.reader.peek(wsLength) == 35 || this.reader.peek(wsLength + 1) == 0 || (this.flowLevel == 0 && wsColumn < this.indent))
/*      */     {
/* 2104 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2109 */     if (this.flowLevel == 0) {
/*      */       
/* 2111 */       int extra = 1; while (true) { if ((c = 
/* 2112 */           this.reader.peek(wsLength + extra)) != 0 && !Constant.NULL_BL_T_LINEBR.has(c)) {
/* 2113 */           if (c == 58 && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(wsLength + extra + 1)))
/* 2114 */             return true;  extra++;
/*      */           continue;
/*      */         } 
/*      */         break; }
/*      */     
/*      */     } 
/* 2120 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanPlainSpaces() {
/* 2127 */     int length = 0;
/* 2128 */     while (this.reader.peek(length) == 32 || this.reader.peek(length) == 9) {
/* 2129 */       length++;
/*      */     }
/* 2131 */     String whitespaces = this.reader.prefixForward(length);
/* 2132 */     String lineBreak = scanLineBreak();
/* 2133 */     if (lineBreak.length() != 0) {
/* 2134 */       this.allowSimpleKey = true;
/* 2135 */       String prefix = this.reader.prefix(3);
/* 2136 */       if ("---".equals(prefix) || ("..."
/* 2137 */         .equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
/* 2138 */         return "";
/*      */       }
/* 2140 */       if (this.parseComments && atEndOfPlain()) {
/* 2141 */         return "";
/*      */       }
/* 2143 */       StringBuilder breaks = new StringBuilder();
/*      */       while (true) {
/* 2145 */         while (this.reader.peek() == 32) {
/* 2146 */           this.reader.forward();
/*      */         }
/* 2148 */         String lb = scanLineBreak();
/* 2149 */         if (lb.length() != 0) {
/* 2150 */           breaks.append(lb);
/* 2151 */           prefix = this.reader.prefix(3);
/* 2152 */           if ("---".equals(prefix) || ("..."
/* 2153 */             .equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
/* 2154 */             return "";
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 2161 */       if (!"\n".equals(lineBreak))
/* 2162 */         return lineBreak + breaks; 
/* 2163 */       if (breaks.length() == 0) {
/* 2164 */         return " ";
/*      */       }
/* 2166 */       return breaks.toString();
/*      */     } 
/* 2168 */     return whitespaces;
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
/*      */   private String scanTagHandle(String name, Mark startMark) {
/* 2194 */     int c = this.reader.peek();
/* 2195 */     if (c != 33) {
/* 2196 */       String s = String.valueOf(Character.toChars(c));
/* 2197 */       throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader
/* 2198 */           .getMark());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2203 */     int length = 1;
/* 2204 */     c = this.reader.peek(length);
/* 2205 */     if (c != 32) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2210 */       while (Constant.ALPHA.has(c)) {
/* 2211 */         length++;
/* 2212 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2217 */       if (c != 33) {
/* 2218 */         this.reader.forward(length);
/* 2219 */         String s = String.valueOf(Character.toChars(c));
/* 2220 */         throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader
/* 2221 */             .getMark());
/*      */       } 
/* 2223 */       length++;
/*      */     } 
/* 2225 */     String value = this.reader.prefixForward(length);
/* 2226 */     return value;
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
/*      */   private String scanTagUri(String name, Mark startMark) {
/* 2246 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/* 2250 */     int length = 0;
/* 2251 */     int c = this.reader.peek(length);
/* 2252 */     while (Constant.URI_CHARS.has(c)) {
/* 2253 */       if (c == 37) {
/* 2254 */         chunks.append(this.reader.prefixForward(length));
/* 2255 */         length = 0;
/* 2256 */         chunks.append(scanUriEscapes(name, startMark));
/*      */       } else {
/* 2258 */         length++;
/*      */       } 
/* 2260 */       c = this.reader.peek(length);
/*      */     } 
/*      */ 
/*      */     
/* 2264 */     if (length != 0) {
/* 2265 */       chunks.append(this.reader.prefixForward(length));
/*      */     }
/* 2267 */     if (chunks.length() == 0) {
/*      */       
/* 2269 */       String s = String.valueOf(Character.toChars(c));
/* 2270 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + s + "(" + c + ")", this.reader
/* 2271 */           .getMark());
/*      */     } 
/* 2273 */     return chunks.toString();
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
/*      */   private String scanUriEscapes(String name, Mark startMark) {
/* 2290 */     int length = 1;
/* 2291 */     while (this.reader.peek(length * 3) == 37) {
/* 2292 */       length++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2298 */     Mark beginningMark = this.reader.getMark();
/* 2299 */     ByteBuffer buff = ByteBuffer.allocate(length);
/* 2300 */     while (this.reader.peek() == 37) {
/* 2301 */       this.reader.forward();
/*      */       try {
/* 2303 */         byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
/* 2304 */         buff.put(code);
/* 2305 */       } catch (NumberFormatException nfe) {
/* 2306 */         int c1 = this.reader.peek();
/* 2307 */         String s1 = String.valueOf(Character.toChars(c1));
/* 2308 */         int c2 = this.reader.peek(1);
/* 2309 */         String s2 = String.valueOf(Character.toChars(c2));
/* 2310 */         throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + s1 + "(" + c1 + ") and " + s2 + "(" + c2 + ")", this.reader
/*      */ 
/*      */             
/* 2313 */             .getMark());
/*      */       } 
/* 2315 */       this.reader.forward(2);
/*      */     } 
/* 2317 */     buff.flip();
/*      */     try {
/* 2319 */       return UriEncoder.decode(buff);
/* 2320 */     } catch (CharacterCodingException e) {
/* 2321 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e
/* 2322 */           .getMessage(), beginningMark);
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
/*      */   private String scanLineBreak() {
/* 2338 */     int c = this.reader.peek();
/* 2339 */     if (c == 13 || c == 10 || c == 133) {
/* 2340 */       if (c == 13 && 10 == this.reader.peek(1)) {
/* 2341 */         this.reader.forward(2);
/*      */       } else {
/* 2343 */         this.reader.forward();
/*      */       } 
/* 2345 */       return "\n";
/* 2346 */     }  if (c == 8232 || c == 8233) {
/* 2347 */       this.reader.forward();
/* 2348 */       return String.valueOf(Character.toChars(c));
/*      */     } 
/* 2350 */     return "";
/*      */   }
/*      */   
/*      */   private List<Token> makeTokenList(Token... tokens) {
/* 2354 */     List<Token> tokenList = new ArrayList<>();
/* 2355 */     for (int ix = 0; ix < tokens.length; ix++) {
/* 2356 */       if (tokens[ix] != null)
/*      */       {
/*      */         
/* 2359 */         if (this.parseComments || !(tokens[ix] instanceof CommentToken))
/*      */         {
/*      */           
/* 2362 */           tokenList.add(tokens[ix]); }  } 
/*      */     } 
/* 2364 */     return tokenList;
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Chomping
/*      */   {
/*      */     private final Boolean value;
/*      */     
/*      */     private final int increment;
/*      */ 
/*      */     
/*      */     public Chomping(Boolean value, int increment) {
/* 2376 */       this.value = value;
/* 2377 */       this.increment = increment;
/*      */     }
/*      */     
/*      */     public boolean chompTailIsNotFalse() {
/* 2381 */       return (this.value == null || this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public boolean chompTailIsTrue() {
/* 2385 */       return (this.value != null && this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public int getIncrement() {
/* 2389 */       return this.increment;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\scanner\ScannerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */