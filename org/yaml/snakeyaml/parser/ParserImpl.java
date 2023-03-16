/*     */ package org.yaml.snakeyaml.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.comments.CommentType;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.CommentEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.scanner.Scanner;
/*     */ import org.yaml.snakeyaml.scanner.ScannerImpl;
/*     */ import org.yaml.snakeyaml.tokens.AliasToken;
/*     */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*     */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*     */ import org.yaml.snakeyaml.tokens.CommentToken;
/*     */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*     */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*     */ import org.yaml.snakeyaml.tokens.TagToken;
/*     */ import org.yaml.snakeyaml.tokens.TagTuple;
/*     */ import org.yaml.snakeyaml.tokens.Token;
/*     */ import org.yaml.snakeyaml.util.ArrayStack;
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
/*     */ public class ParserImpl
/*     */   implements Parser
/*     */ {
/* 121 */   private static final Map<String, String> DEFAULT_TAGS = new HashMap<>();
/*     */   
/*     */   static {
/* 124 */     DEFAULT_TAGS.put("!", "!");
/* 125 */     DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */   protected final Scanner scanner;
/*     */   private Event currentEvent;
/*     */   private final ArrayStack<Production> states;
/*     */   private final ArrayStack<Mark> marks;
/*     */   private Production state;
/*     */   private VersionTagsTuple directives;
/*     */   
/*     */   public ParserImpl(StreamReader reader) {
/* 136 */     this((Scanner)new ScannerImpl(reader));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public ParserImpl(StreamReader reader, boolean parseComments) {
/* 141 */     this((Scanner)new ScannerImpl(reader, (new LoaderOptions()).setProcessComments(parseComments)));
/*     */   }
/*     */   
/*     */   public ParserImpl(StreamReader reader, LoaderOptions options) {
/* 145 */     this((Scanner)new ScannerImpl(reader, options));
/*     */   }
/*     */   
/*     */   public ParserImpl(Scanner scanner) {
/* 149 */     this.scanner = scanner;
/* 150 */     this.currentEvent = null;
/* 151 */     this.directives = new VersionTagsTuple(null, new HashMap<>(DEFAULT_TAGS));
/* 152 */     this.states = new ArrayStack(100);
/* 153 */     this.marks = new ArrayStack(10);
/* 154 */     this.state = new ParseStreamStart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkEvent(Event.ID choice) {
/* 161 */     peekEvent();
/* 162 */     return (this.currentEvent != null && this.currentEvent.is(choice));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event peekEvent() {
/* 169 */     if (this.currentEvent == null && 
/* 170 */       this.state != null) {
/* 171 */       this.currentEvent = this.state.produce();
/*     */     }
/*     */     
/* 174 */     return this.currentEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event getEvent() {
/* 181 */     peekEvent();
/* 182 */     Event value = this.currentEvent;
/* 183 */     this.currentEvent = null;
/* 184 */     return value;
/*     */   }
/*     */   
/*     */   private CommentEvent produceCommentEvent(CommentToken token) {
/* 188 */     Mark startMark = token.getStartMark();
/* 189 */     Mark endMark = token.getEndMark();
/* 190 */     String value = token.getValue();
/* 191 */     CommentType type = token.getCommentType();
/*     */ 
/*     */ 
/*     */     
/* 195 */     return new CommentEvent(type, value, startMark, endMark);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseStreamStart
/*     */     implements Production
/*     */   {
/*     */     private ParseStreamStart() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 209 */       StreamStartToken token = (StreamStartToken)ParserImpl.this.scanner.getToken();
/* 210 */       StreamStartEvent streamStartEvent = new StreamStartEvent(token.getStartMark(), token.getEndMark());
/*     */       
/* 212 */       ParserImpl.this.state = new ParserImpl.ParseImplicitDocumentStart();
/* 213 */       return (Event)streamStartEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseImplicitDocumentStart implements Production {
/*     */     private ParseImplicitDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 221 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 222 */         ParserImpl.this.state = new ParseImplicitDocumentStart();
/* 223 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 225 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd })) {
/* 226 */         Token token = ParserImpl.this.scanner.peekToken();
/* 227 */         Mark startMark = token.getStartMark();
/* 228 */         Mark endMark = startMark;
/* 229 */         DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, false, null, null);
/*     */         
/* 231 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 232 */         ParserImpl.this.state = new ParserImpl.ParseBlockNode();
/* 233 */         return (Event)documentStartEvent;
/*     */       } 
/* 235 */       return (new ParserImpl.ParseDocumentStart()).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentStart implements Production {
/*     */     private ParseDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 243 */       while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 244 */         ParserImpl.this.scanner.getToken();
/*     */       }
/*     */ 
/*     */       
/* 248 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 249 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 250 */         Mark startMark = token1.getStartMark();
/* 251 */         VersionTagsTuple tuple = ParserImpl.this.processDirectives();
/* 252 */         while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment }))
/*     */         {
/* 254 */           ParserImpl.this.scanner.getToken();
/*     */         }
/* 256 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 257 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentStart })) {
/* 258 */             throw new ParserException(null, null, "expected '<document start>', but found '" + ParserImpl.this.scanner
/* 259 */                 .peekToken().getTokenId() + "'", ParserImpl.this.scanner
/* 260 */                 .peekToken().getStartMark());
/*     */           }
/* 262 */           token1 = ParserImpl.this.scanner.getToken();
/* 263 */           Mark endMark = token1.getEndMark();
/*     */           
/* 265 */           DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
/* 266 */           ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 267 */           ParserImpl.this.state = new ParserImpl.ParseDocumentContent();
/* 268 */           return (Event)documentStartEvent;
/*     */         } 
/*     */       } 
/*     */       
/* 272 */       StreamEndToken token = (StreamEndToken)ParserImpl.this.scanner.getToken();
/* 273 */       StreamEndEvent streamEndEvent = new StreamEndEvent(token.getStartMark(), token.getEndMark());
/* 274 */       if (!ParserImpl.this.states.isEmpty()) {
/* 275 */         throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
/*     */       }
/* 277 */       if (!ParserImpl.this.marks.isEmpty()) {
/* 278 */         throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
/*     */       }
/* 280 */       ParserImpl.this.state = null;
/* 281 */       return (Event)streamEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentEnd implements Production {
/*     */     private ParseDocumentEnd() {}
/*     */     
/*     */     public Event produce() {
/* 289 */       Token token = ParserImpl.this.scanner.peekToken();
/* 290 */       Mark startMark = token.getStartMark();
/* 291 */       Mark endMark = startMark;
/* 292 */       boolean explicit = false;
/* 293 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 294 */         token = ParserImpl.this.scanner.getToken();
/* 295 */         endMark = token.getEndMark();
/* 296 */         explicit = true;
/*     */       } 
/* 298 */       DocumentEndEvent documentEndEvent = new DocumentEndEvent(startMark, endMark, explicit);
/*     */       
/* 300 */       ParserImpl.this.state = new ParserImpl.ParseDocumentStart();
/* 301 */       return (Event)documentEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseDocumentContent implements Production {
/*     */     private ParseDocumentContent() {}
/*     */     
/*     */     public Event produce() {
/* 308 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 309 */         ParserImpl.this.state = new ParseDocumentContent();
/* 310 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 312 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd })) {
/*     */         
/* 314 */         Event event = ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/* 315 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 316 */         return event;
/*     */       } 
/* 318 */       return (new ParserImpl.ParseBlockNode()).produce();
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
/*     */   private VersionTagsTuple processDirectives() {
/* 332 */     HashMap<String, String> tagHandles = new HashMap<>(this.directives.getTags());
/* 333 */     for (String key : DEFAULT_TAGS.keySet()) {
/* 334 */       tagHandles.remove(key);
/*     */     }
/*     */     
/* 337 */     this.directives = new VersionTagsTuple(null, tagHandles);
/* 338 */     while (this.scanner.checkToken(new Token.ID[] { Token.ID.Directive })) {
/*     */       
/* 340 */       DirectiveToken token = (DirectiveToken)this.scanner.getToken();
/* 341 */       if (token.getName().equals("YAML")) {
/* 342 */         if (this.directives.getVersion() != null) {
/* 343 */           throw new ParserException(null, null, "found duplicate YAML directive", token
/* 344 */               .getStartMark());
/*     */         }
/* 346 */         List<Integer> value = token.getValue();
/* 347 */         Integer major = value.get(0);
/* 348 */         if (major.intValue() != 1) {
/* 349 */           throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token
/* 350 */               .getStartMark());
/*     */         }
/* 352 */         Integer minor = value.get(1);
/*     */         
/* 354 */         if (minor.intValue() == 0) {
/* 355 */           this.directives = new VersionTagsTuple(DumperOptions.Version.V1_0, tagHandles); continue;
/*     */         } 
/* 357 */         this.directives = new VersionTagsTuple(DumperOptions.Version.V1_1, tagHandles); continue;
/*     */       } 
/* 359 */       if (token.getName().equals("TAG")) {
/* 360 */         List<String> value = token.getValue();
/* 361 */         String handle = value.get(0);
/* 362 */         String prefix = value.get(1);
/* 363 */         if (tagHandles.containsKey(handle)) {
/* 364 */           throw new ParserException(null, null, "duplicate tag handle " + handle, token
/* 365 */               .getStartMark());
/*     */         }
/* 367 */         tagHandles.put(handle, prefix);
/*     */       } 
/*     */     } 
/* 370 */     HashMap<String, String> detectedTagHandles = new HashMap<>();
/* 371 */     if (!tagHandles.isEmpty())
/*     */     {
/* 373 */       detectedTagHandles = new HashMap<>(tagHandles);
/*     */     }
/*     */     
/* 376 */     for (String key : DEFAULT_TAGS.keySet()) {
/*     */       
/* 378 */       if (!tagHandles.containsKey(key)) {
/* 379 */         tagHandles.put(key, DEFAULT_TAGS.get(key));
/*     */       }
/*     */     } 
/*     */     
/* 383 */     return new VersionTagsTuple(this.directives.getVersion(), detectedTagHandles);
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
/*     */   private class ParseBlockNode
/*     */     implements Production
/*     */   {
/*     */     private ParseBlockNode() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 409 */       return ParserImpl.this.parseNode(true, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private Event parseFlowNode() {
/* 414 */     return parseNode(false, false);
/*     */   }
/*     */   
/*     */   private Event parseBlockNodeOrIndentlessSequence() {
/* 418 */     return parseNode(true, true);
/*     */   }
/*     */   
/*     */   private Event parseNode(boolean block, boolean indentlessSequence) {
/*     */     ScalarEvent scalarEvent;
/* 423 */     Mark startMark = null;
/* 424 */     Mark endMark = null;
/* 425 */     Mark tagMark = null;
/* 426 */     if (this.scanner.checkToken(new Token.ID[] { Token.ID.Alias })) {
/* 427 */       AliasToken token = (AliasToken)this.scanner.getToken();
/* 428 */       AliasEvent aliasEvent = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
/* 429 */       this.state = (Production)this.states.pop();
/*     */     } else {
/* 431 */       String anchor = null;
/* 432 */       TagTuple tagTokenTag = null;
/* 433 */       if (this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 434 */         AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 435 */         startMark = token.getStartMark();
/* 436 */         endMark = token.getEndMark();
/* 437 */         anchor = token.getValue();
/* 438 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
/* 439 */           TagToken tagToken = (TagToken)this.scanner.getToken();
/* 440 */           tagMark = tagToken.getStartMark();
/* 441 */           endMark = tagToken.getEndMark();
/* 442 */           tagTokenTag = tagToken.getValue();
/*     */         } 
/*     */       } else {
/* 445 */         TagToken tagToken = (TagToken)this.scanner.getToken();
/* 446 */         startMark = tagToken.getStartMark();
/* 447 */         tagMark = startMark;
/* 448 */         endMark = tagToken.getEndMark();
/* 449 */         tagTokenTag = tagToken.getValue();
/* 450 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag }) && this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 451 */           AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 452 */           endMark = token.getEndMark();
/* 453 */           anchor = token.getValue();
/*     */         } 
/*     */       } 
/* 456 */       String tag = null;
/* 457 */       if (tagTokenTag != null) {
/* 458 */         String handle = tagTokenTag.getHandle();
/* 459 */         String suffix = tagTokenTag.getSuffix();
/* 460 */         if (handle != null) {
/* 461 */           if (!this.directives.getTags().containsKey(handle)) {
/* 462 */             throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
/*     */           }
/*     */           
/* 465 */           tag = (String)this.directives.getTags().get(handle) + suffix;
/*     */         } else {
/* 467 */           tag = suffix;
/*     */         } 
/*     */       } 
/* 470 */       if (startMark == null) {
/* 471 */         startMark = this.scanner.peekToken().getStartMark();
/* 472 */         endMark = startMark;
/*     */       } 
/* 474 */       Event event = null;
/* 475 */       boolean implicit = (tag == null || tag.equals("!"));
/* 476 */       if (indentlessSequence && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 477 */         endMark = this.scanner.peekToken().getEndMark();
/* 478 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 480 */         this.state = new ParseIndentlessSequenceEntryKey();
/*     */       }
/* 482 */       else if (this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 483 */         ImplicitTuple implicitValues; ScalarToken token = (ScalarToken)this.scanner.getToken();
/* 484 */         endMark = token.getEndMark();
/*     */         
/* 486 */         if ((token.getPlain() && tag == null) || "!".equals(tag)) {
/* 487 */           implicitValues = new ImplicitTuple(true, false);
/* 488 */         } else if (tag == null) {
/* 489 */           implicitValues = new ImplicitTuple(false, true);
/*     */         } else {
/* 491 */           implicitValues = new ImplicitTuple(false, false);
/*     */         } 
/*     */         
/* 494 */         scalarEvent = new ScalarEvent(anchor, tag, implicitValues, token.getValue(), startMark, endMark, token.getStyle());
/* 495 */         this.state = (Production)this.states.pop();
/* 496 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceStart })) {
/* 497 */         endMark = this.scanner.peekToken().getEndMark();
/* 498 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
/*     */         
/* 500 */         this.state = new ParseFlowSequenceFirstEntry();
/* 501 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingStart })) {
/* 502 */         endMark = this.scanner.peekToken().getEndMark();
/* 503 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
/*     */         
/* 505 */         this.state = new ParseFlowMappingFirstKey();
/* 506 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockSequenceStart })) {
/* 507 */         endMark = this.scanner.peekToken().getStartMark();
/* 508 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 510 */         this.state = new ParseBlockSequenceFirstEntry();
/* 511 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockMappingStart })) {
/* 512 */         endMark = this.scanner.peekToken().getStartMark();
/* 513 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 515 */         this.state = new ParseBlockMappingFirstKey();
/* 516 */       } else if (anchor != null || tag != null) {
/*     */ 
/*     */         
/* 519 */         scalarEvent = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, DumperOptions.ScalarStyle.PLAIN);
/*     */         
/* 521 */         this.state = (Production)this.states.pop();
/*     */       } else {
/* 523 */         Token token = this.scanner.peekToken();
/* 524 */         throw new ParserException("while parsing a " + (block ? "block" : "flow") + " node", startMark, "expected the node content, but found '" + token
/* 525 */             .getTokenId() + "'", token
/* 526 */             .getStartMark());
/*     */       } 
/*     */     } 
/*     */     
/* 530 */     return (Event)scalarEvent;
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceFirstEntry
/*     */     implements Production
/*     */   {
/*     */     private ParseBlockSequenceFirstEntry() {}
/*     */     
/*     */     public Event produce() {
/* 539 */       Token token = ParserImpl.this.scanner.getToken();
/* 540 */       ParserImpl.this.marks.push(token.getStartMark());
/* 541 */       return (new ParserImpl.ParseBlockSequenceEntryKey()).produce();
/*     */     } }
/*     */   
/*     */   private class ParseBlockSequenceEntryKey implements Production {
/*     */     private ParseBlockSequenceEntryKey() {}
/*     */     
/*     */     public Event produce() {
/* 548 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 549 */         ParserImpl.this.state = new ParseBlockSequenceEntryKey();
/* 550 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 552 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 553 */         BlockEntryToken blockEntryToken = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 554 */         return (new ParserImpl.ParseBlockSequenceEntryValue(blockEntryToken)).produce();
/*     */       } 
/* 556 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 557 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 558 */         throw new ParserException("while parsing a block collection", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token1
/* 559 */             .getTokenId() + "'", token1.getStartMark());
/*     */       } 
/* 561 */       Token token = ParserImpl.this.scanner.getToken();
/* 562 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 563 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 564 */       ParserImpl.this.marks.pop();
/* 565 */       return (Event)sequenceEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceEntryValue
/*     */     implements Production {
/*     */     BlockEntryToken token;
/*     */     
/*     */     public ParseBlockSequenceEntryValue(BlockEntryToken token) {
/* 574 */       this.token = token;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 578 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 579 */         ParserImpl.this.state = new ParseBlockSequenceEntryValue(this.token);
/* 580 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 582 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.BlockEnd })) {
/* 583 */         ParserImpl.this.states.push(new ParserImpl.ParseBlockSequenceEntryKey());
/* 584 */         return (new ParserImpl.ParseBlockNode()).produce();
/*     */       } 
/* 586 */       ParserImpl.this.state = new ParserImpl.ParseBlockSequenceEntryKey();
/* 587 */       return ParserImpl.this.processEmptyScalar(this.token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseIndentlessSequenceEntryKey
/*     */     implements Production
/*     */   {
/*     */     private ParseIndentlessSequenceEntryKey() {}
/*     */     
/*     */     public Event produce() {
/* 597 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 598 */         ParserImpl.this.state = new ParseIndentlessSequenceEntryKey();
/* 599 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 601 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 602 */         BlockEntryToken blockEntryToken = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 603 */         return (new ParserImpl.ParseIndentlessSequenceEntryValue(blockEntryToken)).produce();
/*     */       } 
/* 605 */       Token token = ParserImpl.this.scanner.peekToken();
/* 606 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 607 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 608 */       return (Event)sequenceEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseIndentlessSequenceEntryValue
/*     */     implements Production {
/*     */     BlockEntryToken token;
/*     */     
/*     */     public ParseIndentlessSequenceEntryValue(BlockEntryToken token) {
/* 617 */       this.token = token;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 621 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 622 */         ParserImpl.this.state = new ParseIndentlessSequenceEntryValue(this.token);
/* 623 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 625 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/*     */         
/* 627 */         ParserImpl.this.states.push(new ParserImpl.ParseIndentlessSequenceEntryKey());
/* 628 */         return (new ParserImpl.ParseBlockNode()).produce();
/*     */       } 
/* 630 */       ParserImpl.this.state = new ParserImpl.ParseIndentlessSequenceEntryKey();
/* 631 */       return ParserImpl.this.processEmptyScalar(this.token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingFirstKey implements Production {
/*     */     private ParseBlockMappingFirstKey() {}
/*     */     
/*     */     public Event produce() {
/* 639 */       Token token = ParserImpl.this.scanner.getToken();
/* 640 */       ParserImpl.this.marks.push(token.getStartMark());
/* 641 */       return (new ParserImpl.ParseBlockMappingKey()).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingKey implements Production { private ParseBlockMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 648 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 649 */         ParserImpl.this.state = new ParseBlockMappingKey();
/* 650 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 652 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 653 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 654 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 655 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingValue());
/* 656 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 658 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingValue();
/* 659 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 662 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 663 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 664 */         throw new ParserException("while parsing a block mapping", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token1
/* 665 */             .getTokenId() + "'", token1.getStartMark());
/*     */       } 
/* 667 */       Token token = ParserImpl.this.scanner.getToken();
/* 668 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 669 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 670 */       ParserImpl.this.marks.pop();
/* 671 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingValue implements Production {
/*     */     private ParseBlockMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 678 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 679 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 680 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 681 */           ParserImpl.this.state = new ParserImpl.ParseBlockMappingValueComment();
/* 682 */           return ParserImpl.this.state.produce();
/* 683 */         }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 684 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 685 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 687 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 688 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/* 690 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 691 */         ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 692 */         return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */       } 
/* 694 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 695 */       Token token = ParserImpl.this.scanner.peekToken();
/* 696 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingValueComment
/*     */     implements Production {
/* 702 */     List<CommentToken> tokens = new LinkedList<>();
/*     */     
/*     */     public Event produce() {
/* 705 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 706 */         this.tokens.add((CommentToken)ParserImpl.this.scanner.getToken());
/* 707 */         return produce();
/* 708 */       }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 709 */         if (!this.tokens.isEmpty()) {
/* 710 */           return (Event)ParserImpl.this.produceCommentEvent(this.tokens.remove(0));
/*     */         }
/* 712 */         ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 713 */         return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */       } 
/* 715 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingValueCommentList(this.tokens);
/* 716 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/*     */     }
/*     */     
/*     */     private ParseBlockMappingValueComment() {}
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingValueCommentList implements Production {
/*     */     List<CommentToken> tokens;
/*     */     
/*     */     public ParseBlockMappingValueCommentList(List<CommentToken> tokens) {
/* 726 */       this.tokens = tokens;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 730 */       if (!this.tokens.isEmpty()) {
/* 731 */         return (Event)ParserImpl.this.produceCommentEvent(this.tokens.remove(0));
/*     */       }
/* 733 */       return (new ParserImpl.ParseBlockMappingKey()).produce();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceFirstEntry
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowSequenceFirstEntry() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 753 */       Token token = ParserImpl.this.scanner.getToken();
/* 754 */       ParserImpl.this.marks.push(token.getStartMark());
/* 755 */       return (new ParserImpl.ParseFlowSequenceEntry(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntry
/*     */     implements Production {
/*     */     private final boolean first;
/*     */     
/*     */     public ParseFlowSequenceEntry(boolean first) {
/* 764 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 768 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 769 */         ParserImpl.this.state = new ParseFlowSequenceEntry(this.first);
/* 770 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 772 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 773 */         if (!this.first) {
/* 774 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 775 */             ParserImpl.this.scanner.getToken();
/* 776 */             if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 777 */               ParserImpl.this.state = new ParseFlowSequenceEntry(true);
/* 778 */               return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */             } 
/*     */           } else {
/* 781 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 782 */             throw new ParserException("while parsing a flow sequence", (Mark)ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + token1
/* 783 */                 .getTokenId(), token1.getStartMark());
/*     */           } 
/*     */         }
/* 786 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 787 */           Token token1 = ParserImpl.this.scanner.peekToken();
/*     */           
/* 789 */           MappingStartEvent mappingStartEvent = new MappingStartEvent(null, null, true, token1.getStartMark(), token1.getEndMark(), DumperOptions.FlowStyle.FLOW);
/* 790 */           ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingKey();
/* 791 */           return (Event)mappingStartEvent;
/* 792 */         }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 793 */           ParserImpl.this.states.push(new ParseFlowSequenceEntry(false));
/* 794 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 797 */       Token token = ParserImpl.this.scanner.getToken();
/* 798 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 799 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 800 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/*     */       } else {
/* 802 */         ParserImpl.this.state = new ParserImpl.ParseFlowEndComment();
/*     */       } 
/* 804 */       ParserImpl.this.marks.pop();
/* 805 */       return (Event)sequenceEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowEndComment implements Production { private ParseFlowEndComment() {}
/*     */     
/*     */     public Event produce() {
/* 812 */       CommentEvent commentEvent = ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/* 813 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 814 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/*     */       }
/* 816 */       return (Event)commentEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingKey implements Production {
/*     */     private ParseFlowSequenceEntryMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 823 */       Token token = ParserImpl.this.scanner.getToken();
/* 824 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 825 */         ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingValue());
/* 826 */         return ParserImpl.this.parseFlowNode();
/*     */       } 
/* 828 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingValue();
/* 829 */       return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingValue implements Production {
/*     */     private ParseFlowSequenceEntryMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 837 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 838 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 839 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 840 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingEnd());
/* 841 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 843 */         ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 844 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 847 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 848 */       Token token = ParserImpl.this.scanner.peekToken();
/* 849 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingEnd implements Production {
/*     */     private ParseFlowSequenceEntryMappingEnd() {}
/*     */     
/*     */     public Event produce() {
/* 857 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntry(false);
/* 858 */       Token token = ParserImpl.this.scanner.peekToken();
/* 859 */       return (Event)new MappingEndEvent(token.getStartMark(), token.getEndMark());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowMappingFirstKey
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowMappingFirstKey() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 875 */       Token token = ParserImpl.this.scanner.getToken();
/* 876 */       ParserImpl.this.marks.push(token.getStartMark());
/* 877 */       return (new ParserImpl.ParseFlowMappingKey(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingKey
/*     */     implements Production {
/*     */     private final boolean first;
/*     */     
/*     */     public ParseFlowMappingKey(boolean first) {
/* 886 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 890 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 891 */         ParserImpl.this.state = new ParseFlowMappingKey(this.first);
/* 892 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 894 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 895 */         if (!this.first) {
/* 896 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 897 */             ParserImpl.this.scanner.getToken();
/* 898 */             if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 899 */               ParserImpl.this.state = new ParseFlowMappingKey(true);
/* 900 */               return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */             } 
/*     */           } else {
/* 903 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 904 */             throw new ParserException("while parsing a flow mapping", (Mark)ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + token1
/* 905 */                 .getTokenId(), token1.getStartMark());
/*     */           } 
/*     */         }
/* 908 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 909 */           Token token1 = ParserImpl.this.scanner.getToken();
/* 910 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/* 911 */             ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingValue());
/* 912 */             return ParserImpl.this.parseFlowNode();
/*     */           } 
/* 914 */           ParserImpl.this.state = new ParserImpl.ParseFlowMappingValue();
/* 915 */           return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */         } 
/* 917 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 918 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingEmptyValue());
/* 919 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 922 */       Token token = ParserImpl.this.scanner.getToken();
/* 923 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 924 */       ParserImpl.this.marks.pop();
/* 925 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 926 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/*     */       } else {
/* 928 */         ParserImpl.this.state = new ParserImpl.ParseFlowEndComment();
/*     */       } 
/* 930 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingValue implements Production {
/*     */     private ParseFlowMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 937 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 938 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 939 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/* 940 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingKey(false));
/* 941 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 943 */         ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 944 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 947 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 948 */       Token token = ParserImpl.this.scanner.peekToken();
/* 949 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingEmptyValue implements Production {
/*     */     private ParseFlowMappingEmptyValue() {}
/*     */     
/*     */     public Event produce() {
/* 957 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 958 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
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
/*     */   private Event processEmptyScalar(Mark mark) {
/* 971 */     return (Event)new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\parser\ParserImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */