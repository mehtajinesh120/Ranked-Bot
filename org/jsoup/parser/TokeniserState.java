/*      */ package org.jsoup.parser;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ enum TokeniserState
/*      */ {
/*    9 */   Data
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   12 */       switch (r.current()) {
/*      */         case '&':
/*   14 */           t.advanceTransition(CharacterReferenceInData);
/*      */           return;
/*      */         case '<':
/*   17 */           t.advanceTransition(TagOpen);
/*      */           return;
/*      */         case '\000':
/*   20 */           t.error(this);
/*   21 */           t.emit(r.consume());
/*      */           return;
/*      */         case '￿':
/*   24 */           t.emit(new Token.EOF());
/*      */           return;
/*      */       } 
/*   27 */       String data = r.consumeData();
/*   28 */       t.emit(data);
/*      */     }
/*      */   },
/*      */ 
/*      */   
/*   33 */   CharacterReferenceInData
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   36 */       readCharRef(t, Data);
/*      */     }
/*      */   },
/*   39 */   Rcdata
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   42 */       switch (r.current()) {
/*      */         case '&':
/*   44 */           t.advanceTransition(CharacterReferenceInRcdata);
/*      */           return;
/*      */         case '<':
/*   47 */           t.advanceTransition(RcdataLessthanSign);
/*      */           return;
/*      */         case '\000':
/*   50 */           t.error(this);
/*   51 */           r.advance();
/*   52 */           t.emit('�');
/*      */           return;
/*      */         case '￿':
/*   55 */           t.emit(new Token.EOF());
/*      */           return;
/*      */       } 
/*   58 */       String data = r.consumeData();
/*   59 */       t.emit(data);
/*      */     }
/*      */   },
/*      */ 
/*      */   
/*   64 */   CharacterReferenceInRcdata {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   66 */       readCharRef(t, Rcdata);
/*      */     }
/*      */   },
/*   69 */   Rawtext {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   71 */       readRawData(t, r, this, RawtextLessthanSign);
/*      */     }
/*      */   },
/*   74 */   ScriptData {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   76 */       readRawData(t, r, this, ScriptDataLessthanSign);
/*      */     }
/*      */   },
/*   79 */   PLAINTEXT {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*   81 */       switch (r.current()) {
/*      */         case '\000':
/*   83 */           t.error(this);
/*   84 */           r.advance();
/*   85 */           t.emit('�');
/*      */           return;
/*      */         case '￿':
/*   88 */           t.emit(new Token.EOF());
/*      */           return;
/*      */       } 
/*   91 */       String data = r.consumeTo(false);
/*   92 */       t.emit(data);
/*      */     }
/*      */   },
/*      */ 
/*      */   
/*   97 */   TagOpen
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  100 */       switch (r.current()) {
/*      */         case '!':
/*  102 */           t.advanceTransition(MarkupDeclarationOpen);
/*      */           return;
/*      */         case '/':
/*  105 */           t.advanceTransition(EndTagOpen);
/*      */           return;
/*      */         case '?':
/*  108 */           t.createBogusCommentPending();
/*  109 */           t.transition(BogusComment);
/*      */           return;
/*      */       } 
/*  112 */       if (r.matchesAsciiAlpha()) {
/*  113 */         t.createTagPending(true);
/*  114 */         t.transition(TagName);
/*      */       } else {
/*  116 */         t.error(this);
/*  117 */         t.emit('<');
/*  118 */         t.transition(Data);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */   },
/*  124 */   EndTagOpen {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  126 */       if (r.isEmpty()) {
/*  127 */         t.eofError(this);
/*  128 */         t.emit("</");
/*  129 */         t.transition(Data);
/*  130 */       } else if (r.matchesAsciiAlpha()) {
/*  131 */         t.createTagPending(false);
/*  132 */         t.transition(TagName);
/*  133 */       } else if (r.matches('>')) {
/*  134 */         t.error(this);
/*  135 */         t.advanceTransition(Data);
/*      */       } else {
/*  137 */         t.error(this);
/*  138 */         t.createBogusCommentPending();
/*  139 */         t.commentPending.append('/');
/*  140 */         t.transition(BogusComment);
/*      */       } 
/*      */     }
/*      */   },
/*  144 */   TagName
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r)
/*      */     {
/*  148 */       String tagName = r.consumeTagName();
/*  149 */       t.tagPending.appendTagName(tagName);
/*      */       
/*  151 */       char c = r.consume();
/*  152 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  158 */           t.transition(BeforeAttributeName);
/*      */           return;
/*      */         case '/':
/*  161 */           t.transition(SelfClosingStartTag);
/*      */           return;
/*      */         case '<':
/*  164 */           r.unconsume();
/*  165 */           t.error(this);
/*      */         
/*      */         case '>':
/*  168 */           t.emitTagPending();
/*  169 */           t.transition(Data);
/*      */           return;
/*      */         case '\000':
/*  172 */           t.tagPending.appendTagName(TokeniserState.replacementStr);
/*      */           return;
/*      */         case '￿':
/*  175 */           t.eofError(this);
/*  176 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  179 */       t.tagPending.appendTagName(c);
/*      */     }
/*      */   },
/*      */   
/*  183 */   RcdataLessthanSign
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  186 */       if (r.matches('/')) {
/*  187 */         t.createTempBuffer();
/*  188 */         t.advanceTransition(RCDATAEndTagOpen);
/*  189 */       } else if (r.matchesAsciiAlpha() && t.appropriateEndTagName() != null && !r.containsIgnoreCase(t.appropriateEndTagSeq())) {
/*      */ 
/*      */         
/*  192 */         t.tagPending = t.createTagPending(false).name(t.appropriateEndTagName());
/*  193 */         t.emitTagPending();
/*  194 */         t.transition(TagOpen);
/*      */       } else {
/*  196 */         t.emit("<");
/*  197 */         t.transition(Rcdata);
/*      */       } 
/*      */     }
/*      */   },
/*  201 */   RCDATAEndTagOpen {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  203 */       if (r.matchesAsciiAlpha()) {
/*  204 */         t.createTagPending(false);
/*  205 */         t.tagPending.appendTagName(r.current());
/*  206 */         t.dataBuffer.append(r.current());
/*  207 */         t.advanceTransition(RCDATAEndTagName);
/*      */       } else {
/*  209 */         t.emit("</");
/*  210 */         t.transition(Rcdata);
/*      */       } 
/*      */     }
/*      */   },
/*  214 */   RCDATAEndTagName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  216 */       if (r.matchesAsciiAlpha()) {
/*  217 */         String name = r.consumeLetterSequence();
/*  218 */         t.tagPending.appendTagName(name);
/*  219 */         t.dataBuffer.append(name);
/*      */         
/*      */         return;
/*      */       } 
/*  223 */       char c = r.consume();
/*  224 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  230 */           if (t.isAppropriateEndTagToken()) {
/*  231 */             t.transition(BeforeAttributeName);
/*      */           } else {
/*  233 */             anythingElse(t, r);
/*      */           }  return;
/*      */         case '/':
/*  236 */           if (t.isAppropriateEndTagToken()) {
/*  237 */             t.transition(SelfClosingStartTag);
/*      */           } else {
/*  239 */             anythingElse(t, r);
/*      */           }  return;
/*      */         case '>':
/*  242 */           if (t.isAppropriateEndTagToken()) {
/*  243 */             t.emitTagPending();
/*  244 */             t.transition(Data);
/*      */           } else {
/*      */             
/*  247 */             anythingElse(t, r);
/*      */           }  return;
/*      */       } 
/*  250 */       anythingElse(t, r);
/*      */     }
/*      */ 
/*      */     
/*      */     private void anythingElse(Tokeniser t, CharacterReader r) {
/*  255 */       t.emit("</");
/*  256 */       t.emit(t.dataBuffer);
/*  257 */       r.unconsume();
/*  258 */       t.transition(Rcdata);
/*      */     }
/*      */   },
/*  261 */   RawtextLessthanSign {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  263 */       if (r.matches('/')) {
/*  264 */         t.createTempBuffer();
/*  265 */         t.advanceTransition(RawtextEndTagOpen);
/*      */       } else {
/*  267 */         t.emit('<');
/*  268 */         t.transition(Rawtext);
/*      */       } 
/*      */     }
/*      */   },
/*  272 */   RawtextEndTagOpen {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  274 */       readEndTag(t, r, RawtextEndTagName, Rawtext);
/*      */     }
/*      */   },
/*  277 */   RawtextEndTagName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  279 */       handleDataEndTag(t, r, Rawtext);
/*      */     }
/*      */   },
/*  282 */   ScriptDataLessthanSign {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  284 */       switch (r.consume()) {
/*      */         case '/':
/*  286 */           t.createTempBuffer();
/*  287 */           t.transition(ScriptDataEndTagOpen);
/*      */           return;
/*      */         case '!':
/*  290 */           t.emit("<!");
/*  291 */           t.transition(ScriptDataEscapeStart);
/*      */           return;
/*      */         case '￿':
/*  294 */           t.emit("<");
/*  295 */           t.eofError(this);
/*  296 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  299 */       t.emit("<");
/*  300 */       r.unconsume();
/*  301 */       t.transition(ScriptData);
/*      */     }
/*      */   },
/*      */   
/*  305 */   ScriptDataEndTagOpen {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  307 */       readEndTag(t, r, ScriptDataEndTagName, ScriptData);
/*      */     }
/*      */   },
/*  310 */   ScriptDataEndTagName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  312 */       handleDataEndTag(t, r, ScriptData);
/*      */     }
/*      */   },
/*  315 */   ScriptDataEscapeStart {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  317 */       if (r.matches('-')) {
/*  318 */         t.emit('-');
/*  319 */         t.advanceTransition(ScriptDataEscapeStartDash);
/*      */       } else {
/*  321 */         t.transition(ScriptData);
/*      */       } 
/*      */     }
/*      */   },
/*  325 */   ScriptDataEscapeStartDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  327 */       if (r.matches('-')) {
/*  328 */         t.emit('-');
/*  329 */         t.advanceTransition(ScriptDataEscapedDashDash);
/*      */       } else {
/*  331 */         t.transition(ScriptData);
/*      */       } 
/*      */     }
/*      */   },
/*  335 */   ScriptDataEscaped {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  337 */       if (r.isEmpty()) {
/*  338 */         t.eofError(this);
/*  339 */         t.transition(Data);
/*      */         
/*      */         return;
/*      */       } 
/*  343 */       switch (r.current()) {
/*      */         case '-':
/*  345 */           t.emit('-');
/*  346 */           t.advanceTransition(ScriptDataEscapedDash);
/*      */           return;
/*      */         case '<':
/*  349 */           t.advanceTransition(ScriptDataEscapedLessthanSign);
/*      */           return;
/*      */         case '\000':
/*  352 */           t.error(this);
/*  353 */           r.advance();
/*  354 */           t.emit('�');
/*      */           return;
/*      */       } 
/*  357 */       String data = r.consumeToAny(new char[] { '-', '<', Character.MIN_VALUE });
/*  358 */       t.emit(data);
/*      */     }
/*      */   },
/*      */   
/*  362 */   ScriptDataEscapedDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  364 */       if (r.isEmpty()) {
/*  365 */         t.eofError(this);
/*  366 */         t.transition(Data);
/*      */         
/*      */         return;
/*      */       } 
/*  370 */       char c = r.consume();
/*  371 */       switch (c) {
/*      */         case '-':
/*  373 */           t.emit(c);
/*  374 */           t.transition(ScriptDataEscapedDashDash);
/*      */           return;
/*      */         case '<':
/*  377 */           t.transition(ScriptDataEscapedLessthanSign);
/*      */           return;
/*      */         case '\000':
/*  380 */           t.error(this);
/*  381 */           t.emit('�');
/*  382 */           t.transition(ScriptDataEscaped);
/*      */           return;
/*      */       } 
/*  385 */       t.emit(c);
/*  386 */       t.transition(ScriptDataEscaped);
/*      */     }
/*      */   },
/*      */   
/*  390 */   ScriptDataEscapedDashDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  392 */       if (r.isEmpty()) {
/*  393 */         t.eofError(this);
/*  394 */         t.transition(Data);
/*      */         
/*      */         return;
/*      */       } 
/*  398 */       char c = r.consume();
/*  399 */       switch (c) {
/*      */         case '-':
/*  401 */           t.emit(c);
/*      */           return;
/*      */         case '<':
/*  404 */           t.transition(ScriptDataEscapedLessthanSign);
/*      */           return;
/*      */         case '>':
/*  407 */           t.emit(c);
/*  408 */           t.transition(ScriptData);
/*      */           return;
/*      */         case '\000':
/*  411 */           t.error(this);
/*  412 */           t.emit('�');
/*  413 */           t.transition(ScriptDataEscaped);
/*      */           return;
/*      */       } 
/*  416 */       t.emit(c);
/*  417 */       t.transition(ScriptDataEscaped);
/*      */     }
/*      */   },
/*      */   
/*  421 */   ScriptDataEscapedLessthanSign {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  423 */       if (r.matchesAsciiAlpha()) {
/*  424 */         t.createTempBuffer();
/*  425 */         t.dataBuffer.append(r.current());
/*  426 */         t.emit("<");
/*  427 */         t.emit(r.current());
/*  428 */         t.advanceTransition(ScriptDataDoubleEscapeStart);
/*  429 */       } else if (r.matches('/')) {
/*  430 */         t.createTempBuffer();
/*  431 */         t.advanceTransition(ScriptDataEscapedEndTagOpen);
/*      */       } else {
/*  433 */         t.emit('<');
/*  434 */         t.transition(ScriptDataEscaped);
/*      */       } 
/*      */     }
/*      */   },
/*  438 */   ScriptDataEscapedEndTagOpen {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  440 */       if (r.matchesAsciiAlpha()) {
/*  441 */         t.createTagPending(false);
/*  442 */         t.tagPending.appendTagName(r.current());
/*  443 */         t.dataBuffer.append(r.current());
/*  444 */         t.advanceTransition(ScriptDataEscapedEndTagName);
/*      */       } else {
/*  446 */         t.emit("</");
/*  447 */         t.transition(ScriptDataEscaped);
/*      */       } 
/*      */     }
/*      */   },
/*  451 */   ScriptDataEscapedEndTagName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  453 */       handleDataEndTag(t, r, ScriptDataEscaped);
/*      */     }
/*      */   },
/*  456 */   ScriptDataDoubleEscapeStart {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  458 */       handleDataDoubleEscapeTag(t, r, ScriptDataDoubleEscaped, ScriptDataEscaped);
/*      */     }
/*      */   },
/*  461 */   ScriptDataDoubleEscaped {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  463 */       char c = r.current();
/*  464 */       switch (c) {
/*      */         case '-':
/*  466 */           t.emit(c);
/*  467 */           t.advanceTransition(ScriptDataDoubleEscapedDash);
/*      */           return;
/*      */         case '<':
/*  470 */           t.emit(c);
/*  471 */           t.advanceTransition(ScriptDataDoubleEscapedLessthanSign);
/*      */           return;
/*      */         case '\000':
/*  474 */           t.error(this);
/*  475 */           r.advance();
/*  476 */           t.emit('�');
/*      */           return;
/*      */         case '￿':
/*  479 */           t.eofError(this);
/*  480 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  483 */       String data = r.consumeToAny(new char[] { '-', '<', Character.MIN_VALUE });
/*  484 */       t.emit(data);
/*      */     }
/*      */   },
/*      */   
/*  488 */   ScriptDataDoubleEscapedDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  490 */       char c = r.consume();
/*  491 */       switch (c) {
/*      */         case '-':
/*  493 */           t.emit(c);
/*  494 */           t.transition(ScriptDataDoubleEscapedDashDash);
/*      */           return;
/*      */         case '<':
/*  497 */           t.emit(c);
/*  498 */           t.transition(ScriptDataDoubleEscapedLessthanSign);
/*      */           return;
/*      */         case '\000':
/*  501 */           t.error(this);
/*  502 */           t.emit('�');
/*  503 */           t.transition(ScriptDataDoubleEscaped);
/*      */           return;
/*      */         case '￿':
/*  506 */           t.eofError(this);
/*  507 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  510 */       t.emit(c);
/*  511 */       t.transition(ScriptDataDoubleEscaped);
/*      */     }
/*      */   },
/*      */   
/*  515 */   ScriptDataDoubleEscapedDashDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  517 */       char c = r.consume();
/*  518 */       switch (c) {
/*      */         case '-':
/*  520 */           t.emit(c);
/*      */           return;
/*      */         case '<':
/*  523 */           t.emit(c);
/*  524 */           t.transition(ScriptDataDoubleEscapedLessthanSign);
/*      */           return;
/*      */         case '>':
/*  527 */           t.emit(c);
/*  528 */           t.transition(ScriptData);
/*      */           return;
/*      */         case '\000':
/*  531 */           t.error(this);
/*  532 */           t.emit('�');
/*  533 */           t.transition(ScriptDataDoubleEscaped);
/*      */           return;
/*      */         case '￿':
/*  536 */           t.eofError(this);
/*  537 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  540 */       t.emit(c);
/*  541 */       t.transition(ScriptDataDoubleEscaped);
/*      */     }
/*      */   },
/*      */   
/*  545 */   ScriptDataDoubleEscapedLessthanSign {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  547 */       if (r.matches('/')) {
/*  548 */         t.emit('/');
/*  549 */         t.createTempBuffer();
/*  550 */         t.advanceTransition(ScriptDataDoubleEscapeEnd);
/*      */       } else {
/*  552 */         t.transition(ScriptDataDoubleEscaped);
/*      */       } 
/*      */     }
/*      */   },
/*  556 */   ScriptDataDoubleEscapeEnd {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  558 */       handleDataDoubleEscapeTag(t, r, ScriptDataEscaped, ScriptDataDoubleEscaped);
/*      */     }
/*      */   },
/*  561 */   BeforeAttributeName
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  564 */       char c = r.consume();
/*  565 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         case '/':
/*  573 */           t.transition(SelfClosingStartTag);
/*      */         
/*      */         case '<':
/*  576 */           r.unconsume();
/*  577 */           t.error(this);
/*      */         
/*      */         case '>':
/*  580 */           t.emitTagPending();
/*  581 */           t.transition(Data);
/*      */         
/*      */         case '\000':
/*  584 */           r.unconsume();
/*  585 */           t.error(this);
/*  586 */           t.tagPending.newAttribute();
/*  587 */           t.transition(AttributeName);
/*      */         
/*      */         case '￿':
/*  590 */           t.eofError(this);
/*  591 */           t.transition(Data);
/*      */         
/*      */         case '"':
/*      */         case '\'':
/*      */         case '=':
/*  596 */           t.error(this);
/*  597 */           t.tagPending.newAttribute();
/*  598 */           t.tagPending.appendAttributeName(c);
/*  599 */           t.transition(AttributeName);
/*      */       } 
/*      */       
/*  602 */       t.tagPending.newAttribute();
/*  603 */       r.unconsume();
/*  604 */       t.transition(AttributeName);
/*      */     }
/*      */   },
/*      */   
/*  608 */   AttributeName
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  611 */       String name = r.consumeToAnySorted(attributeNameCharsSorted);
/*  612 */       t.tagPending.appendAttributeName(name);
/*      */       
/*  614 */       char c = r.consume();
/*  615 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  621 */           t.transition(AfterAttributeName);
/*      */           return;
/*      */         case '/':
/*  624 */           t.transition(SelfClosingStartTag);
/*      */           return;
/*      */         case '=':
/*  627 */           t.transition(BeforeAttributeValue);
/*      */           return;
/*      */         case '>':
/*  630 */           t.emitTagPending();
/*  631 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/*  634 */           t.eofError(this);
/*  635 */           t.transition(Data);
/*      */           return;
/*      */         case '"':
/*      */         case '\'':
/*      */         case '<':
/*  640 */           t.error(this);
/*  641 */           t.tagPending.appendAttributeName(c);
/*      */           return;
/*      */       } 
/*  644 */       t.tagPending.appendAttributeName(c);
/*      */     }
/*      */   },
/*      */   
/*  648 */   AfterAttributeName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  650 */       char c = r.consume();
/*  651 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         
/*      */         case '/':
/*  660 */           t.transition(SelfClosingStartTag);
/*      */         
/*      */         case '=':
/*  663 */           t.transition(BeforeAttributeValue);
/*      */         
/*      */         case '>':
/*  666 */           t.emitTagPending();
/*  667 */           t.transition(Data);
/*      */         
/*      */         case '\000':
/*  670 */           t.error(this);
/*  671 */           t.tagPending.appendAttributeName('�');
/*  672 */           t.transition(AttributeName);
/*      */         
/*      */         case '￿':
/*  675 */           t.eofError(this);
/*  676 */           t.transition(Data);
/*      */         
/*      */         case '"':
/*      */         case '\'':
/*      */         case '<':
/*  681 */           t.error(this);
/*  682 */           t.tagPending.newAttribute();
/*  683 */           t.tagPending.appendAttributeName(c);
/*  684 */           t.transition(AttributeName);
/*      */       } 
/*      */       
/*  687 */       t.tagPending.newAttribute();
/*  688 */       r.unconsume();
/*  689 */       t.transition(AttributeName);
/*      */     }
/*      */   },
/*      */   
/*  693 */   BeforeAttributeValue {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  695 */       char c = r.consume();
/*  696 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         
/*      */         case '"':
/*  705 */           t.transition(AttributeValue_doubleQuoted);
/*      */         
/*      */         case '&':
/*  708 */           r.unconsume();
/*  709 */           t.transition(AttributeValue_unquoted);
/*      */         
/*      */         case '\'':
/*  712 */           t.transition(AttributeValue_singleQuoted);
/*      */         
/*      */         case '\000':
/*  715 */           t.error(this);
/*  716 */           t.tagPending.appendAttributeValue('�');
/*  717 */           t.transition(AttributeValue_unquoted);
/*      */         
/*      */         case '￿':
/*  720 */           t.eofError(this);
/*  721 */           t.emitTagPending();
/*  722 */           t.transition(Data);
/*      */         
/*      */         case '>':
/*  725 */           t.error(this);
/*  726 */           t.emitTagPending();
/*  727 */           t.transition(Data);
/*      */         
/*      */         case '<':
/*      */         case '=':
/*      */         case '`':
/*  732 */           t.error(this);
/*  733 */           t.tagPending.appendAttributeValue(c);
/*  734 */           t.transition(AttributeValue_unquoted);
/*      */       } 
/*      */       
/*  737 */       r.unconsume();
/*  738 */       t.transition(AttributeValue_unquoted);
/*      */     }
/*      */   },
/*      */   
/*  742 */   AttributeValue_doubleQuoted { void read(Tokeniser t, CharacterReader r) {
/*      */       int[] ref;
/*  744 */       String value = r.consumeAttributeQuoted(false);
/*  745 */       if (value.length() > 0) {
/*  746 */         t.tagPending.appendAttributeValue(value);
/*      */       } else {
/*  748 */         t.tagPending.setEmptyAttributeValue();
/*      */       } 
/*  750 */       char c = r.consume();
/*  751 */       switch (c) {
/*      */         case '"':
/*  753 */           t.transition(AfterAttributeValue_quoted);
/*      */           return;
/*      */         case '&':
/*  756 */           ref = t.consumeCharacterReference(Character.valueOf('"'), true);
/*  757 */           if (ref != null) {
/*  758 */             t.tagPending.appendAttributeValue(ref);
/*      */           } else {
/*  760 */             t.tagPending.appendAttributeValue('&');
/*      */           }  return;
/*      */         case '\000':
/*  763 */           t.error(this);
/*  764 */           t.tagPending.appendAttributeValue('�');
/*      */           return;
/*      */         case '￿':
/*  767 */           t.eofError(this);
/*  768 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  771 */       t.tagPending.appendAttributeValue(c);
/*      */     } }
/*      */   ,
/*      */   
/*  775 */   AttributeValue_singleQuoted { void read(Tokeniser t, CharacterReader r) {
/*      */       int[] ref;
/*  777 */       String value = r.consumeAttributeQuoted(true);
/*  778 */       if (value.length() > 0) {
/*  779 */         t.tagPending.appendAttributeValue(value);
/*      */       } else {
/*  781 */         t.tagPending.setEmptyAttributeValue();
/*      */       } 
/*  783 */       char c = r.consume();
/*  784 */       switch (c) {
/*      */         case '\'':
/*  786 */           t.transition(AfterAttributeValue_quoted);
/*      */           return;
/*      */         case '&':
/*  789 */           ref = t.consumeCharacterReference(Character.valueOf('\''), true);
/*  790 */           if (ref != null) {
/*  791 */             t.tagPending.appendAttributeValue(ref);
/*      */           } else {
/*  793 */             t.tagPending.appendAttributeValue('&');
/*      */           }  return;
/*      */         case '\000':
/*  796 */           t.error(this);
/*  797 */           t.tagPending.appendAttributeValue('�');
/*      */           return;
/*      */         case '￿':
/*  800 */           t.eofError(this);
/*  801 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  804 */       t.tagPending.appendAttributeValue(c);
/*      */     } }
/*      */   ,
/*      */   
/*  808 */   AttributeValue_unquoted { void read(Tokeniser t, CharacterReader r) {
/*      */       int[] ref;
/*  810 */       String value = r.consumeToAnySorted(attributeValueUnquoted);
/*  811 */       if (value.length() > 0) {
/*  812 */         t.tagPending.appendAttributeValue(value);
/*      */       }
/*  814 */       char c = r.consume();
/*  815 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  821 */           t.transition(BeforeAttributeName);
/*      */           return;
/*      */         case '&':
/*  824 */           ref = t.consumeCharacterReference(Character.valueOf('>'), true);
/*  825 */           if (ref != null) {
/*  826 */             t.tagPending.appendAttributeValue(ref);
/*      */           } else {
/*  828 */             t.tagPending.appendAttributeValue('&');
/*      */           }  return;
/*      */         case '>':
/*  831 */           t.emitTagPending();
/*  832 */           t.transition(Data);
/*      */           return;
/*      */         case '\000':
/*  835 */           t.error(this);
/*  836 */           t.tagPending.appendAttributeValue('�');
/*      */           return;
/*      */         case '￿':
/*  839 */           t.eofError(this);
/*  840 */           t.transition(Data);
/*      */           return;
/*      */         case '"':
/*      */         case '\'':
/*      */         case '<':
/*      */         case '=':
/*      */         case '`':
/*  847 */           t.error(this);
/*  848 */           t.tagPending.appendAttributeValue(c);
/*      */           return;
/*      */       } 
/*  851 */       t.tagPending.appendAttributeValue(c);
/*      */     } }
/*      */   ,
/*      */ 
/*      */ 
/*      */   
/*  857 */   AfterAttributeValue_quoted {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  859 */       char c = r.consume();
/*  860 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  866 */           t.transition(BeforeAttributeName);
/*      */           return;
/*      */         case '/':
/*  869 */           t.transition(SelfClosingStartTag);
/*      */           return;
/*      */         case '>':
/*  872 */           t.emitTagPending();
/*  873 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/*  876 */           t.eofError(this);
/*  877 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  880 */       r.unconsume();
/*  881 */       t.error(this);
/*  882 */       t.transition(BeforeAttributeName);
/*      */     }
/*      */   },
/*      */ 
/*      */   
/*  887 */   SelfClosingStartTag {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  889 */       char c = r.consume();
/*  890 */       switch (c) {
/*      */         case '>':
/*  892 */           t.tagPending.selfClosing = true;
/*  893 */           t.emitTagPending();
/*  894 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/*  897 */           t.eofError(this);
/*  898 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  901 */       r.unconsume();
/*  902 */       t.error(this);
/*  903 */       t.transition(BeforeAttributeName);
/*      */     }
/*      */   },
/*      */   
/*  907 */   BogusComment
/*      */   {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  910 */       t.commentPending.append(r.consumeTo('>'));
/*      */       
/*  912 */       char next = r.current();
/*  913 */       if (next == '>' || next == Character.MAX_VALUE) {
/*  914 */         r.consume();
/*  915 */         t.emitCommentPending();
/*  916 */         t.transition(Data);
/*      */       } 
/*      */     }
/*      */   },
/*  920 */   MarkupDeclarationOpen {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  922 */       if (r.matchConsume("--")) {
/*  923 */         t.createCommentPending();
/*  924 */         t.transition(CommentStart);
/*  925 */       } else if (r.matchConsumeIgnoreCase("DOCTYPE")) {
/*  926 */         t.transition(Doctype);
/*  927 */       } else if (r.matchConsume("[CDATA[")) {
/*      */ 
/*      */ 
/*      */         
/*  931 */         t.createTempBuffer();
/*  932 */         t.transition(CdataSection);
/*      */       } else {
/*  934 */         t.error(this);
/*  935 */         t.createBogusCommentPending();
/*  936 */         t.transition(BogusComment);
/*      */       } 
/*      */     }
/*      */   },
/*  940 */   CommentStart {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  942 */       char c = r.consume();
/*  943 */       switch (c) {
/*      */         case '-':
/*  945 */           t.transition(CommentStartDash);
/*      */           return;
/*      */         case '\000':
/*  948 */           t.error(this);
/*  949 */           t.commentPending.append('�');
/*  950 */           t.transition(Comment);
/*      */           return;
/*      */         case '>':
/*  953 */           t.error(this);
/*  954 */           t.emitCommentPending();
/*  955 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/*  958 */           t.eofError(this);
/*  959 */           t.emitCommentPending();
/*  960 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  963 */       r.unconsume();
/*  964 */       t.transition(Comment);
/*      */     }
/*      */   },
/*      */   
/*  968 */   CommentStartDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  970 */       char c = r.consume();
/*  971 */       switch (c) {
/*      */         case '-':
/*  973 */           t.transition(CommentEnd);
/*      */           return;
/*      */         case '\000':
/*  976 */           t.error(this);
/*  977 */           t.commentPending.append('�');
/*  978 */           t.transition(Comment);
/*      */           return;
/*      */         case '>':
/*  981 */           t.error(this);
/*  982 */           t.emitCommentPending();
/*  983 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/*  986 */           t.eofError(this);
/*  987 */           t.emitCommentPending();
/*  988 */           t.transition(Data);
/*      */           return;
/*      */       } 
/*  991 */       t.commentPending.append(c);
/*  992 */       t.transition(Comment);
/*      */     }
/*      */   },
/*      */   
/*  996 */   Comment {
/*      */     void read(Tokeniser t, CharacterReader r) {
/*  998 */       char c = r.current();
/*  999 */       switch (c) {
/*      */         case '-':
/* 1001 */           t.advanceTransition(CommentEndDash);
/*      */           return;
/*      */         case '\000':
/* 1004 */           t.error(this);
/* 1005 */           r.advance();
/* 1006 */           t.commentPending.append('�');
/*      */           return;
/*      */         case '￿':
/* 1009 */           t.eofError(this);
/* 1010 */           t.emitCommentPending();
/* 1011 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1014 */       t.commentPending.append(r.consumeToAny(new char[] { '-', Character.MIN_VALUE
/*      */             }));
/*      */     }
/*      */   },
/* 1018 */   CommentEndDash {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1020 */       char c = r.consume();
/* 1021 */       switch (c) {
/*      */         case '-':
/* 1023 */           t.transition(CommentEnd);
/*      */           return;
/*      */         case '\000':
/* 1026 */           t.error(this);
/* 1027 */           t.commentPending.append('-').append('�');
/* 1028 */           t.transition(Comment);
/*      */           return;
/*      */         case '￿':
/* 1031 */           t.eofError(this);
/* 1032 */           t.emitCommentPending();
/* 1033 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1036 */       t.commentPending.append('-').append(c);
/* 1037 */       t.transition(Comment);
/*      */     }
/*      */   },
/*      */   
/* 1041 */   CommentEnd {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1043 */       char c = r.consume();
/* 1044 */       switch (c) {
/*      */         case '>':
/* 1046 */           t.emitCommentPending();
/* 1047 */           t.transition(Data);
/*      */           return;
/*      */         case '\000':
/* 1050 */           t.error(this);
/* 1051 */           t.commentPending.append("--").append('�');
/* 1052 */           t.transition(Comment);
/*      */           return;
/*      */         case '!':
/* 1055 */           t.transition(CommentEndBang);
/*      */           return;
/*      */         case '-':
/* 1058 */           t.commentPending.append('-');
/*      */           return;
/*      */         case '￿':
/* 1061 */           t.eofError(this);
/* 1062 */           t.emitCommentPending();
/* 1063 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1066 */       t.commentPending.append("--").append(c);
/* 1067 */       t.transition(Comment);
/*      */     }
/*      */   },
/*      */   
/* 1071 */   CommentEndBang {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1073 */       char c = r.consume();
/* 1074 */       switch (c) {
/*      */         case '-':
/* 1076 */           t.commentPending.append("--!");
/* 1077 */           t.transition(CommentEndDash);
/*      */           return;
/*      */         case '>':
/* 1080 */           t.emitCommentPending();
/* 1081 */           t.transition(Data);
/*      */           return;
/*      */         case '\000':
/* 1084 */           t.error(this);
/* 1085 */           t.commentPending.append("--!").append('�');
/* 1086 */           t.transition(Comment);
/*      */           return;
/*      */         case '￿':
/* 1089 */           t.eofError(this);
/* 1090 */           t.emitCommentPending();
/* 1091 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1094 */       t.commentPending.append("--!").append(c);
/* 1095 */       t.transition(Comment);
/*      */     }
/*      */   },
/*      */   
/* 1099 */   Doctype {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1101 */       char c = r.consume();
/* 1102 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/* 1108 */           t.transition(BeforeDoctypeName);
/*      */           return;
/*      */         case '￿':
/* 1111 */           t.eofError(this);
/*      */         
/*      */         case '>':
/* 1114 */           t.error(this);
/* 1115 */           t.createDoctypePending();
/* 1116 */           t.doctypePending.forceQuirks = true;
/* 1117 */           t.emitDoctypePending();
/* 1118 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1121 */       t.error(this);
/* 1122 */       t.transition(BeforeDoctypeName);
/*      */     }
/*      */   },
/*      */   
/* 1126 */   BeforeDoctypeName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1128 */       if (r.matchesAsciiAlpha()) {
/* 1129 */         t.createDoctypePending();
/* 1130 */         t.transition(DoctypeName);
/*      */         return;
/*      */       } 
/* 1133 */       char c = r.consume();
/* 1134 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         case '\000':
/* 1142 */           t.error(this);
/* 1143 */           t.createDoctypePending();
/* 1144 */           t.doctypePending.name.append('�');
/* 1145 */           t.transition(DoctypeName);
/*      */         
/*      */         case '￿':
/* 1148 */           t.eofError(this);
/* 1149 */           t.createDoctypePending();
/* 1150 */           t.doctypePending.forceQuirks = true;
/* 1151 */           t.emitDoctypePending();
/* 1152 */           t.transition(Data);
/*      */       } 
/*      */       
/* 1155 */       t.createDoctypePending();
/* 1156 */       t.doctypePending.name.append(c);
/* 1157 */       t.transition(DoctypeName);
/*      */     }
/*      */   },
/*      */   
/* 1161 */   DoctypeName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1163 */       if (r.matchesLetter()) {
/* 1164 */         String name = r.consumeLetterSequence();
/* 1165 */         t.doctypePending.name.append(name);
/*      */         return;
/*      */       } 
/* 1168 */       char c = r.consume();
/* 1169 */       switch (c) {
/*      */         case '>':
/* 1171 */           t.emitDoctypePending();
/* 1172 */           t.transition(Data);
/*      */           return;
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/* 1179 */           t.transition(AfterDoctypeName);
/*      */           return;
/*      */         case '\000':
/* 1182 */           t.error(this);
/* 1183 */           t.doctypePending.name.append('�');
/*      */           return;
/*      */         case '￿':
/* 1186 */           t.eofError(this);
/* 1187 */           t.doctypePending.forceQuirks = true;
/* 1188 */           t.emitDoctypePending();
/* 1189 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1192 */       t.doctypePending.name.append(c);
/*      */     }
/*      */   },
/*      */   
/* 1196 */   AfterDoctypeName {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1198 */       if (r.isEmpty()) {
/* 1199 */         t.eofError(this);
/* 1200 */         t.doctypePending.forceQuirks = true;
/* 1201 */         t.emitDoctypePending();
/* 1202 */         t.transition(Data);
/*      */         return;
/*      */       } 
/* 1205 */       if (r.matchesAny(new char[] { '\t', '\n', '\r', '\f', ' ' })) {
/* 1206 */         r.advance();
/* 1207 */       } else if (r.matches('>')) {
/* 1208 */         t.emitDoctypePending();
/* 1209 */         t.advanceTransition(Data);
/* 1210 */       } else if (r.matchConsumeIgnoreCase("PUBLIC")) {
/* 1211 */         t.doctypePending.pubSysKey = "PUBLIC";
/* 1212 */         t.transition(AfterDoctypePublicKeyword);
/* 1213 */       } else if (r.matchConsumeIgnoreCase("SYSTEM")) {
/* 1214 */         t.doctypePending.pubSysKey = "SYSTEM";
/* 1215 */         t.transition(AfterDoctypeSystemKeyword);
/*      */       } else {
/* 1217 */         t.error(this);
/* 1218 */         t.doctypePending.forceQuirks = true;
/* 1219 */         t.advanceTransition(BogusDoctype);
/*      */       }
/*      */     
/*      */     }
/*      */   },
/* 1224 */   AfterDoctypePublicKeyword {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1226 */       char c = r.consume();
/* 1227 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/* 1233 */           t.transition(BeforeDoctypePublicIdentifier);
/*      */           return;
/*      */         case '"':
/* 1236 */           t.error(this);
/*      */           
/* 1238 */           t.transition(DoctypePublicIdentifier_doubleQuoted);
/*      */           return;
/*      */         case '\'':
/* 1241 */           t.error(this);
/*      */           
/* 1243 */           t.transition(DoctypePublicIdentifier_singleQuoted);
/*      */           return;
/*      */         case '>':
/* 1246 */           t.error(this);
/* 1247 */           t.doctypePending.forceQuirks = true;
/* 1248 */           t.emitDoctypePending();
/* 1249 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/* 1252 */           t.eofError(this);
/* 1253 */           t.doctypePending.forceQuirks = true;
/* 1254 */           t.emitDoctypePending();
/* 1255 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1258 */       t.error(this);
/* 1259 */       t.doctypePending.forceQuirks = true;
/* 1260 */       t.transition(BogusDoctype);
/*      */     }
/*      */   },
/*      */   
/* 1264 */   BeforeDoctypePublicIdentifier {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1266 */       char c = r.consume();
/* 1267 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         
/*      */         case '"':
/* 1276 */           t.transition(DoctypePublicIdentifier_doubleQuoted);
/*      */ 
/*      */         
/*      */         case '\'':
/* 1280 */           t.transition(DoctypePublicIdentifier_singleQuoted);
/*      */         
/*      */         case '>':
/* 1283 */           t.error(this);
/* 1284 */           t.doctypePending.forceQuirks = true;
/* 1285 */           t.emitDoctypePending();
/* 1286 */           t.transition(Data);
/*      */         
/*      */         case '￿':
/* 1289 */           t.eofError(this);
/* 1290 */           t.doctypePending.forceQuirks = true;
/* 1291 */           t.emitDoctypePending();
/* 1292 */           t.transition(Data);
/*      */       } 
/*      */       
/* 1295 */       t.error(this);
/* 1296 */       t.doctypePending.forceQuirks = true;
/* 1297 */       t.transition(BogusDoctype);
/*      */     }
/*      */   },
/*      */   
/* 1301 */   DoctypePublicIdentifier_doubleQuoted {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1303 */       char c = r.consume();
/* 1304 */       switch (c) {
/*      */         case '"':
/* 1306 */           t.transition(AfterDoctypePublicIdentifier);
/*      */           return;
/*      */         case '\000':
/* 1309 */           t.error(this);
/* 1310 */           t.doctypePending.publicIdentifier.append('�');
/*      */           return;
/*      */         case '>':
/* 1313 */           t.error(this);
/* 1314 */           t.doctypePending.forceQuirks = true;
/* 1315 */           t.emitDoctypePending();
/* 1316 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/* 1319 */           t.eofError(this);
/* 1320 */           t.doctypePending.forceQuirks = true;
/* 1321 */           t.emitDoctypePending();
/* 1322 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1325 */       t.doctypePending.publicIdentifier.append(c);
/*      */     }
/*      */   },
/*      */   
/* 1329 */   DoctypePublicIdentifier_singleQuoted {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1331 */       char c = r.consume();
/* 1332 */       switch (c) {
/*      */         case '\'':
/* 1334 */           t.transition(AfterDoctypePublicIdentifier);
/*      */           return;
/*      */         case '\000':
/* 1337 */           t.error(this);
/* 1338 */           t.doctypePending.publicIdentifier.append('�');
/*      */           return;
/*      */         case '>':
/* 1341 */           t.error(this);
/* 1342 */           t.doctypePending.forceQuirks = true;
/* 1343 */           t.emitDoctypePending();
/* 1344 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/* 1347 */           t.eofError(this);
/* 1348 */           t.doctypePending.forceQuirks = true;
/* 1349 */           t.emitDoctypePending();
/* 1350 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1353 */       t.doctypePending.publicIdentifier.append(c);
/*      */     }
/*      */   },
/*      */   
/* 1357 */   AfterDoctypePublicIdentifier {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1359 */       char c = r.consume();
/* 1360 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/* 1366 */           t.transition(BetweenDoctypePublicAndSystemIdentifiers);
/*      */           return;
/*      */         case '>':
/* 1369 */           t.emitDoctypePending();
/* 1370 */           t.transition(Data);
/*      */           return;
/*      */         case '"':
/* 1373 */           t.error(this);
/*      */           
/* 1375 */           t.transition(DoctypeSystemIdentifier_doubleQuoted);
/*      */           return;
/*      */         case '\'':
/* 1378 */           t.error(this);
/*      */           
/* 1380 */           t.transition(DoctypeSystemIdentifier_singleQuoted);
/*      */           return;
/*      */         case '￿':
/* 1383 */           t.eofError(this);
/* 1384 */           t.doctypePending.forceQuirks = true;
/* 1385 */           t.emitDoctypePending();
/* 1386 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1389 */       t.error(this);
/* 1390 */       t.doctypePending.forceQuirks = true;
/* 1391 */       t.transition(BogusDoctype);
/*      */     }
/*      */   },
/*      */   
/* 1395 */   BetweenDoctypePublicAndSystemIdentifiers {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1397 */       char c = r.consume();
/* 1398 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         case '>':
/* 1406 */           t.emitDoctypePending();
/* 1407 */           t.transition(Data);
/*      */         
/*      */         case '"':
/* 1410 */           t.error(this);
/*      */           
/* 1412 */           t.transition(DoctypeSystemIdentifier_doubleQuoted);
/*      */         
/*      */         case '\'':
/* 1415 */           t.error(this);
/*      */           
/* 1417 */           t.transition(DoctypeSystemIdentifier_singleQuoted);
/*      */         
/*      */         case '￿':
/* 1420 */           t.eofError(this);
/* 1421 */           t.doctypePending.forceQuirks = true;
/* 1422 */           t.emitDoctypePending();
/* 1423 */           t.transition(Data);
/*      */       } 
/*      */       
/* 1426 */       t.error(this);
/* 1427 */       t.doctypePending.forceQuirks = true;
/* 1428 */       t.transition(BogusDoctype);
/*      */     }
/*      */   },
/*      */   
/* 1432 */   AfterDoctypeSystemKeyword {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1434 */       char c = r.consume();
/* 1435 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/* 1441 */           t.transition(BeforeDoctypeSystemIdentifier);
/*      */           return;
/*      */         case '>':
/* 1444 */           t.error(this);
/* 1445 */           t.doctypePending.forceQuirks = true;
/* 1446 */           t.emitDoctypePending();
/* 1447 */           t.transition(Data);
/*      */           return;
/*      */         case '"':
/* 1450 */           t.error(this);
/*      */           
/* 1452 */           t.transition(DoctypeSystemIdentifier_doubleQuoted);
/*      */           return;
/*      */         case '\'':
/* 1455 */           t.error(this);
/*      */           
/* 1457 */           t.transition(DoctypeSystemIdentifier_singleQuoted);
/*      */           return;
/*      */         case '￿':
/* 1460 */           t.eofError(this);
/* 1461 */           t.doctypePending.forceQuirks = true;
/* 1462 */           t.emitDoctypePending();
/* 1463 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1466 */       t.error(this);
/* 1467 */       t.doctypePending.forceQuirks = true;
/* 1468 */       t.emitDoctypePending();
/*      */     }
/*      */   },
/*      */   
/* 1472 */   BeforeDoctypeSystemIdentifier {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1474 */       char c = r.consume();
/* 1475 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         
/*      */         case '"':
/* 1484 */           t.transition(DoctypeSystemIdentifier_doubleQuoted);
/*      */ 
/*      */         
/*      */         case '\'':
/* 1488 */           t.transition(DoctypeSystemIdentifier_singleQuoted);
/*      */         
/*      */         case '>':
/* 1491 */           t.error(this);
/* 1492 */           t.doctypePending.forceQuirks = true;
/* 1493 */           t.emitDoctypePending();
/* 1494 */           t.transition(Data);
/*      */         
/*      */         case '￿':
/* 1497 */           t.eofError(this);
/* 1498 */           t.doctypePending.forceQuirks = true;
/* 1499 */           t.emitDoctypePending();
/* 1500 */           t.transition(Data);
/*      */       } 
/*      */       
/* 1503 */       t.error(this);
/* 1504 */       t.doctypePending.forceQuirks = true;
/* 1505 */       t.transition(BogusDoctype);
/*      */     }
/*      */   },
/*      */   
/* 1509 */   DoctypeSystemIdentifier_doubleQuoted {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1511 */       char c = r.consume();
/* 1512 */       switch (c) {
/*      */         case '"':
/* 1514 */           t.transition(AfterDoctypeSystemIdentifier);
/*      */           return;
/*      */         case '\000':
/* 1517 */           t.error(this);
/* 1518 */           t.doctypePending.systemIdentifier.append('�');
/*      */           return;
/*      */         case '>':
/* 1521 */           t.error(this);
/* 1522 */           t.doctypePending.forceQuirks = true;
/* 1523 */           t.emitDoctypePending();
/* 1524 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/* 1527 */           t.eofError(this);
/* 1528 */           t.doctypePending.forceQuirks = true;
/* 1529 */           t.emitDoctypePending();
/* 1530 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1533 */       t.doctypePending.systemIdentifier.append(c);
/*      */     }
/*      */   },
/*      */   
/* 1537 */   DoctypeSystemIdentifier_singleQuoted {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1539 */       char c = r.consume();
/* 1540 */       switch (c) {
/*      */         case '\'':
/* 1542 */           t.transition(AfterDoctypeSystemIdentifier);
/*      */           return;
/*      */         case '\000':
/* 1545 */           t.error(this);
/* 1546 */           t.doctypePending.systemIdentifier.append('�');
/*      */           return;
/*      */         case '>':
/* 1549 */           t.error(this);
/* 1550 */           t.doctypePending.forceQuirks = true;
/* 1551 */           t.emitDoctypePending();
/* 1552 */           t.transition(Data);
/*      */           return;
/*      */         case '￿':
/* 1555 */           t.eofError(this);
/* 1556 */           t.doctypePending.forceQuirks = true;
/* 1557 */           t.emitDoctypePending();
/* 1558 */           t.transition(Data);
/*      */           return;
/*      */       } 
/* 1561 */       t.doctypePending.systemIdentifier.append(c);
/*      */     }
/*      */   },
/*      */   
/* 1565 */   AfterDoctypeSystemIdentifier {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1567 */       char c = r.consume();
/* 1568 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*      */           return;
/*      */         case '>':
/* 1576 */           t.emitDoctypePending();
/* 1577 */           t.transition(Data);
/*      */         
/*      */         case '￿':
/* 1580 */           t.eofError(this);
/* 1581 */           t.doctypePending.forceQuirks = true;
/* 1582 */           t.emitDoctypePending();
/* 1583 */           t.transition(Data);
/*      */       } 
/*      */       
/* 1586 */       t.error(this);
/* 1587 */       t.transition(BogusDoctype);
/*      */     }
/*      */   },
/*      */ 
/*      */   
/* 1592 */   BogusDoctype {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1594 */       char c = r.consume();
/* 1595 */       switch (c) {
/*      */         case '>':
/* 1597 */           t.emitDoctypePending();
/* 1598 */           t.transition(Data);
/*      */           break;
/*      */         case '￿':
/* 1601 */           t.emitDoctypePending();
/* 1602 */           t.transition(Data);
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */   },
/* 1610 */   CdataSection {
/*      */     void read(Tokeniser t, CharacterReader r) {
/* 1612 */       String data = r.consumeTo("]]>");
/* 1613 */       t.dataBuffer.append(data);
/* 1614 */       if (r.matchConsume("]]>") || r.isEmpty()) {
/* 1615 */         t.emit(new Token.CData(t.dataBuffer.toString()));
/* 1616 */         t.transition(Data);
/*      */       } 
/*      */     }
/*      */   };
/*      */   
/*      */   static final char nullChar = '\000';
/*      */   static final char[] attributeNameCharsSorted;
/*      */   static final char[] attributeValueUnquoted;
/*      */   
/*      */   static {
/* 1626 */     attributeNameCharsSorted = new char[] { '\t', '\n', '\f', '\r', ' ', '"', '\'', '/', '<', '=', '>' };
/* 1627 */     attributeValueUnquoted = new char[] { Character.MIN_VALUE, '\t', '\n', '\f', '\r', ' ', '"', '&', '\'', '<', '=', '>', '`' };
/*      */ 
/*      */     
/* 1630 */     replacementStr = String.valueOf('�');
/*      */   }
/*      */   
/*      */   private static final char replacementChar = '�';
/*      */   private static final String replacementStr;
/*      */   private static final char eof = '￿';
/*      */   
/*      */   private static void handleDataEndTag(Tokeniser t, CharacterReader r, TokeniserState elseTransition) {
/* 1638 */     if (r.matchesLetter()) {
/* 1639 */       String name = r.consumeLetterSequence();
/* 1640 */       t.tagPending.appendTagName(name);
/* 1641 */       t.dataBuffer.append(name);
/*      */       
/*      */       return;
/*      */     } 
/* 1645 */     boolean needsExitTransition = false;
/* 1646 */     if (t.isAppropriateEndTagToken() && !r.isEmpty()) {
/* 1647 */       char c = r.consume();
/* 1648 */       switch (c) {
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/* 1654 */           t.transition(BeforeAttributeName);
/*      */           break;
/*      */         case '/':
/* 1657 */           t.transition(SelfClosingStartTag);
/*      */           break;
/*      */         case '>':
/* 1660 */           t.emitTagPending();
/* 1661 */           t.transition(Data);
/*      */           break;
/*      */         default:
/* 1664 */           t.dataBuffer.append(c);
/* 1665 */           needsExitTransition = true; break;
/*      */       } 
/*      */     } else {
/* 1668 */       needsExitTransition = true;
/*      */     } 
/*      */     
/* 1671 */     if (needsExitTransition) {
/* 1672 */       t.emit("</");
/* 1673 */       t.emit(t.dataBuffer);
/* 1674 */       t.transition(elseTransition);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void readRawData(Tokeniser t, CharacterReader r, TokeniserState current, TokeniserState advance) {
/* 1679 */     switch (r.current()) {
/*      */       case '<':
/* 1681 */         t.advanceTransition(advance);
/*      */         return;
/*      */       case '\000':
/* 1684 */         t.error(current);
/* 1685 */         r.advance();
/* 1686 */         t.emit('�');
/*      */         return;
/*      */       case '￿':
/* 1689 */         t.emit(new Token.EOF());
/*      */         return;
/*      */     } 
/* 1692 */     String data = r.consumeRawData();
/* 1693 */     t.emit(data);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void readCharRef(Tokeniser t, TokeniserState advance) {
/* 1699 */     int[] c = t.consumeCharacterReference(null, false);
/* 1700 */     if (c == null) {
/* 1701 */       t.emit('&');
/*      */     } else {
/* 1703 */       t.emit(c);
/* 1704 */     }  t.transition(advance);
/*      */   }
/*      */   
/*      */   private static void readEndTag(Tokeniser t, CharacterReader r, TokeniserState a, TokeniserState b) {
/* 1708 */     if (r.matchesAsciiAlpha()) {
/* 1709 */       t.createTagPending(false);
/* 1710 */       t.transition(a);
/*      */     } else {
/* 1712 */       t.emit("</");
/* 1713 */       t.transition(b);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void handleDataDoubleEscapeTag(Tokeniser t, CharacterReader r, TokeniserState primary, TokeniserState fallback) {
/* 1718 */     if (r.matchesLetter()) {
/* 1719 */       String name = r.consumeLetterSequence();
/* 1720 */       t.dataBuffer.append(name);
/* 1721 */       t.emit(name);
/*      */       
/*      */       return;
/*      */     } 
/* 1725 */     char c = r.consume();
/* 1726 */     switch (c) {
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*      */       case ' ':
/*      */       case '/':
/*      */       case '>':
/* 1734 */         if (t.dataBuffer.toString().equals("script")) {
/* 1735 */           t.transition(primary);
/*      */         } else {
/* 1737 */           t.transition(fallback);
/* 1738 */         }  t.emit(c);
/*      */         return;
/*      */     } 
/* 1741 */     r.unconsume();
/* 1742 */     t.transition(fallback);
/*      */   }
/*      */   
/*      */   abstract void read(Tokeniser paramTokeniser, CharacterReader paramCharacterReader);
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\TokeniserState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */