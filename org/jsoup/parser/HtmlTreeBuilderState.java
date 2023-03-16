/*      */ package org.jsoup.parser;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import org.jsoup.internal.StringUtil;
/*      */ import org.jsoup.nodes.Attribute;
/*      */ import org.jsoup.nodes.Attributes;
/*      */ import org.jsoup.nodes.Document;
/*      */ import org.jsoup.nodes.DocumentType;
/*      */ import org.jsoup.nodes.Element;
/*      */ import org.jsoup.nodes.FormElement;
/*      */ import org.jsoup.nodes.Node;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ enum HtmlTreeBuilderState
/*      */ {
/*   20 */   Initial {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*   22 */       if (isWhitespace(t))
/*   23 */         return true; 
/*   24 */       if (t.isComment()) {
/*   25 */         tb.insert(t.asComment());
/*   26 */       } else if (t.isDoctype()) {
/*      */ 
/*      */         
/*   29 */         Token.Doctype d = t.asDoctype();
/*      */         
/*   31 */         DocumentType doctype = new DocumentType(tb.settings.normalizeTag(d.getName()), d.getPublicIdentifier(), d.getSystemIdentifier());
/*   32 */         doctype.setPubSysKey(d.getPubSysKey());
/*   33 */         tb.getDocument().appendChild((Node)doctype);
/*   34 */         tb.onNodeInserted((Node)doctype, t);
/*   35 */         if (d.isForceQuirks())
/*   36 */           tb.getDocument().quirksMode(Document.QuirksMode.quirks); 
/*   37 */         tb.transition(BeforeHtml);
/*      */       } else {
/*      */         
/*   40 */         tb.transition(BeforeHtml);
/*   41 */         return tb.process(t);
/*      */       } 
/*   43 */       return true;
/*      */     }
/*      */   },
/*   46 */   BeforeHtml {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*   48 */       if (t.isDoctype()) {
/*   49 */         tb.error(this);
/*   50 */         return false;
/*   51 */       }  if (t.isComment())
/*   52 */       { tb.insert(t.asComment()); }
/*   53 */       else if (isWhitespace(t))
/*   54 */       { tb.insert(t.asCharacter()); }
/*   55 */       else if (t.isStartTag() && t.asStartTag().normalName().equals("html"))
/*   56 */       { tb.insert(t.asStartTag());
/*   57 */         tb.transition(BeforeHead); }
/*   58 */       else { if (t.isEndTag() && StringUtil.inSorted(t.asEndTag().normalName(), Constants.BeforeHtmlToHead))
/*   59 */           return anythingElse(t, tb); 
/*   60 */         if (t.isEndTag()) {
/*   61 */           tb.error(this);
/*   62 */           return false;
/*      */         } 
/*   64 */         return anythingElse(t, tb); }
/*      */       
/*   66 */       return true;
/*      */     }
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/*   70 */       tb.insertStartTag("html");
/*   71 */       tb.transition(BeforeHead);
/*   72 */       return tb.process(t);
/*      */     }
/*      */   },
/*   75 */   BeforeHead {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*   77 */       if (isWhitespace(t))
/*   78 */       { tb.insert(t.asCharacter()); }
/*   79 */       else if (t.isComment())
/*   80 */       { tb.insert(t.asComment()); }
/*   81 */       else { if (t.isDoctype()) {
/*   82 */           tb.error(this);
/*   83 */           return false;
/*   84 */         }  if (t.isStartTag() && t.asStartTag().normalName().equals("html"))
/*   85 */           return InBody.process(t, tb); 
/*   86 */         if (t.isStartTag() && t.asStartTag().normalName().equals("head"))
/*   87 */         { Element head = tb.insert(t.asStartTag());
/*   88 */           tb.setHeadElement(head);
/*   89 */           tb.transition(InHead); }
/*   90 */         else { if (t.isEndTag() && StringUtil.inSorted(t.asEndTag().normalName(), Constants.BeforeHtmlToHead)) {
/*   91 */             tb.processStartTag("head");
/*   92 */             return tb.process(t);
/*   93 */           }  if (t.isEndTag()) {
/*   94 */             tb.error(this);
/*   95 */             return false;
/*      */           } 
/*   97 */           tb.processStartTag("head");
/*   98 */           return tb.process(t); }
/*      */          }
/*  100 */        return true;
/*      */     }
/*      */   },
/*  103 */   InHead
/*      */   {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: invokestatic access$100 : (Lorg/jsoup/parser/Token;)Z
/*      */       //   4: ifeq -> 17
/*      */       //   7: aload_2
/*      */       //   8: aload_1
/*      */       //   9: invokevirtual asCharacter : ()Lorg/jsoup/parser/Token$Character;
/*      */       //   12: invokevirtual insert : (Lorg/jsoup/parser/Token$Character;)V
/*      */       //   15: iconst_1
/*      */       //   16: ireturn
/*      */       //   17: getstatic org/jsoup/parser/HtmlTreeBuilderState$25.$SwitchMap$org$jsoup$parser$Token$TokenType : [I
/*      */       //   20: aload_1
/*      */       //   21: getfield type : Lorg/jsoup/parser/Token$TokenType;
/*      */       //   24: invokevirtual ordinal : ()I
/*      */       //   27: iaload
/*      */       //   28: tableswitch default -> 482, 1 -> 60, 2 -> 71, 3 -> 78, 4 -> 343
/*      */       //   60: aload_2
/*      */       //   61: aload_1
/*      */       //   62: invokevirtual asComment : ()Lorg/jsoup/parser/Token$Comment;
/*      */       //   65: invokevirtual insert : (Lorg/jsoup/parser/Token$Comment;)V
/*      */       //   68: goto -> 489
/*      */       //   71: aload_2
/*      */       //   72: aload_0
/*      */       //   73: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   76: iconst_0
/*      */       //   77: ireturn
/*      */       //   78: aload_1
/*      */       //   79: invokevirtual asStartTag : ()Lorg/jsoup/parser/Token$StartTag;
/*      */       //   82: astore_3
/*      */       //   83: aload_3
/*      */       //   84: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   87: astore #4
/*      */       //   89: aload #4
/*      */       //   91: ldc 'html'
/*      */       //   93: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   96: ifeq -> 108
/*      */       //   99: getstatic org/jsoup/parser/HtmlTreeBuilderState$4.InBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   102: aload_1
/*      */       //   103: aload_2
/*      */       //   104: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   107: ireturn
/*      */       //   108: aload #4
/*      */       //   110: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InHeadEmpty : [Ljava/lang/String;
/*      */       //   113: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   116: ifeq -> 155
/*      */       //   119: aload_2
/*      */       //   120: aload_3
/*      */       //   121: invokevirtual insertEmpty : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   124: astore #5
/*      */       //   126: aload #4
/*      */       //   128: ldc 'base'
/*      */       //   130: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   133: ifeq -> 152
/*      */       //   136: aload #5
/*      */       //   138: ldc 'href'
/*      */       //   140: invokevirtual hasAttr : (Ljava/lang/String;)Z
/*      */       //   143: ifeq -> 152
/*      */       //   146: aload_2
/*      */       //   147: aload #5
/*      */       //   149: invokevirtual maybeSetBaseUri : (Lorg/jsoup/nodes/Element;)V
/*      */       //   152: goto -> 489
/*      */       //   155: aload #4
/*      */       //   157: ldc 'meta'
/*      */       //   159: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   162: ifeq -> 174
/*      */       //   165: aload_2
/*      */       //   166: aload_3
/*      */       //   167: invokevirtual insertEmpty : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   170: pop
/*      */       //   171: goto -> 489
/*      */       //   174: aload #4
/*      */       //   176: ldc 'title'
/*      */       //   178: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   181: ifeq -> 192
/*      */       //   184: aload_3
/*      */       //   185: aload_2
/*      */       //   186: invokestatic access$200 : (Lorg/jsoup/parser/Token$StartTag;Lorg/jsoup/parser/HtmlTreeBuilder;)V
/*      */       //   189: goto -> 489
/*      */       //   192: aload #4
/*      */       //   194: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InHeadRaw : [Ljava/lang/String;
/*      */       //   197: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   200: ifeq -> 211
/*      */       //   203: aload_3
/*      */       //   204: aload_2
/*      */       //   205: invokestatic access$300 : (Lorg/jsoup/parser/Token$StartTag;Lorg/jsoup/parser/HtmlTreeBuilder;)V
/*      */       //   208: goto -> 489
/*      */       //   211: aload #4
/*      */       //   213: ldc 'noscript'
/*      */       //   215: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   218: ifeq -> 237
/*      */       //   221: aload_2
/*      */       //   222: aload_3
/*      */       //   223: invokevirtual insert : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   226: pop
/*      */       //   227: aload_2
/*      */       //   228: getstatic org/jsoup/parser/HtmlTreeBuilderState$4.InHeadNoscript : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   231: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   234: goto -> 489
/*      */       //   237: aload #4
/*      */       //   239: ldc 'script'
/*      */       //   241: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   244: ifeq -> 277
/*      */       //   247: aload_2
/*      */       //   248: getfield tokeniser : Lorg/jsoup/parser/Tokeniser;
/*      */       //   251: getstatic org/jsoup/parser/TokeniserState.ScriptData : Lorg/jsoup/parser/TokeniserState;
/*      */       //   254: invokevirtual transition : (Lorg/jsoup/parser/TokeniserState;)V
/*      */       //   257: aload_2
/*      */       //   258: invokevirtual markInsertionMode : ()V
/*      */       //   261: aload_2
/*      */       //   262: getstatic org/jsoup/parser/HtmlTreeBuilderState$4.Text : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   265: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   268: aload_2
/*      */       //   269: aload_3
/*      */       //   270: invokevirtual insert : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   273: pop
/*      */       //   274: goto -> 489
/*      */       //   277: aload #4
/*      */       //   279: ldc 'head'
/*      */       //   281: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   284: ifeq -> 294
/*      */       //   287: aload_2
/*      */       //   288: aload_0
/*      */       //   289: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   292: iconst_0
/*      */       //   293: ireturn
/*      */       //   294: aload #4
/*      */       //   296: ldc 'template'
/*      */       //   298: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   301: ifeq -> 336
/*      */       //   304: aload_2
/*      */       //   305: aload_3
/*      */       //   306: invokevirtual insert : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   309: pop
/*      */       //   310: aload_2
/*      */       //   311: invokevirtual insertMarkerToFormattingElements : ()V
/*      */       //   314: aload_2
/*      */       //   315: iconst_0
/*      */       //   316: invokevirtual framesetOk : (Z)V
/*      */       //   319: aload_2
/*      */       //   320: getstatic org/jsoup/parser/HtmlTreeBuilderState$4.InTemplate : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   323: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   326: aload_2
/*      */       //   327: getstatic org/jsoup/parser/HtmlTreeBuilderState$4.InTemplate : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   330: invokevirtual pushTemplateMode : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   333: goto -> 489
/*      */       //   336: aload_0
/*      */       //   337: aload_1
/*      */       //   338: aload_2
/*      */       //   339: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/TreeBuilder;)Z
/*      */       //   342: ireturn
/*      */       //   343: aload_1
/*      */       //   344: invokevirtual asEndTag : ()Lorg/jsoup/parser/Token$EndTag;
/*      */       //   347: astore #5
/*      */       //   349: aload #5
/*      */       //   351: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   354: astore #4
/*      */       //   356: aload #4
/*      */       //   358: ldc 'head'
/*      */       //   360: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   363: ifeq -> 381
/*      */       //   366: aload_2
/*      */       //   367: invokevirtual pop : ()Lorg/jsoup/nodes/Element;
/*      */       //   370: pop
/*      */       //   371: aload_2
/*      */       //   372: getstatic org/jsoup/parser/HtmlTreeBuilderState$4.AfterHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   375: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   378: goto -> 489
/*      */       //   381: aload #4
/*      */       //   383: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InHeadEnd : [Ljava/lang/String;
/*      */       //   386: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   389: ifeq -> 399
/*      */       //   392: aload_0
/*      */       //   393: aload_1
/*      */       //   394: aload_2
/*      */       //   395: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/TreeBuilder;)Z
/*      */       //   398: ireturn
/*      */       //   399: aload #4
/*      */       //   401: ldc 'template'
/*      */       //   403: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   406: ifeq -> 475
/*      */       //   409: aload_2
/*      */       //   410: aload #4
/*      */       //   412: invokevirtual onStack : (Ljava/lang/String;)Z
/*      */       //   415: ifne -> 426
/*      */       //   418: aload_2
/*      */       //   419: aload_0
/*      */       //   420: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   423: goto -> 489
/*      */       //   426: aload_2
/*      */       //   427: iconst_1
/*      */       //   428: invokevirtual generateImpliedEndTags : (Z)V
/*      */       //   431: aload #4
/*      */       //   433: aload_2
/*      */       //   434: invokevirtual currentElement : ()Lorg/jsoup/nodes/Element;
/*      */       //   437: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   440: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   443: ifne -> 451
/*      */       //   446: aload_2
/*      */       //   447: aload_0
/*      */       //   448: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   451: aload_2
/*      */       //   452: aload #4
/*      */       //   454: invokevirtual popStackToClose : (Ljava/lang/String;)Lorg/jsoup/nodes/Element;
/*      */       //   457: pop
/*      */       //   458: aload_2
/*      */       //   459: invokevirtual clearFormattingElementsToLastMarker : ()V
/*      */       //   462: aload_2
/*      */       //   463: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   466: pop
/*      */       //   467: aload_2
/*      */       //   468: invokevirtual resetInsertionMode : ()Z
/*      */       //   471: pop
/*      */       //   472: goto -> 489
/*      */       //   475: aload_2
/*      */       //   476: aload_0
/*      */       //   477: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   480: iconst_0
/*      */       //   481: ireturn
/*      */       //   482: aload_0
/*      */       //   483: aload_1
/*      */       //   484: aload_2
/*      */       //   485: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/TreeBuilder;)Z
/*      */       //   488: ireturn
/*      */       //   489: iconst_1
/*      */       //   490: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #105	-> 0
/*      */       //   #106	-> 7
/*      */       //   #107	-> 15
/*      */       //   #109	-> 17
/*      */       //   #111	-> 60
/*      */       //   #112	-> 68
/*      */       //   #114	-> 71
/*      */       //   #115	-> 76
/*      */       //   #117	-> 78
/*      */       //   #118	-> 83
/*      */       //   #119	-> 89
/*      */       //   #120	-> 99
/*      */       //   #121	-> 108
/*      */       //   #122	-> 119
/*      */       //   #124	-> 126
/*      */       //   #125	-> 146
/*      */       //   #126	-> 152
/*      */       //   #127	-> 165
/*      */       //   #129	-> 174
/*      */       //   #130	-> 184
/*      */       //   #131	-> 192
/*      */       //   #132	-> 203
/*      */       //   #133	-> 211
/*      */       //   #135	-> 221
/*      */       //   #136	-> 227
/*      */       //   #137	-> 237
/*      */       //   #139	-> 247
/*      */       //   #140	-> 257
/*      */       //   #141	-> 261
/*      */       //   #142	-> 268
/*      */       //   #143	-> 277
/*      */       //   #144	-> 287
/*      */       //   #145	-> 292
/*      */       //   #146	-> 294
/*      */       //   #147	-> 304
/*      */       //   #148	-> 310
/*      */       //   #149	-> 314
/*      */       //   #150	-> 319
/*      */       //   #151	-> 326
/*      */       //   #153	-> 336
/*      */       //   #157	-> 343
/*      */       //   #158	-> 349
/*      */       //   #159	-> 356
/*      */       //   #160	-> 366
/*      */       //   #161	-> 371
/*      */       //   #162	-> 381
/*      */       //   #163	-> 392
/*      */       //   #164	-> 399
/*      */       //   #165	-> 409
/*      */       //   #166	-> 418
/*      */       //   #168	-> 426
/*      */       //   #169	-> 431
/*      */       //   #170	-> 451
/*      */       //   #171	-> 458
/*      */       //   #172	-> 462
/*      */       //   #173	-> 467
/*      */       //   #177	-> 475
/*      */       //   #178	-> 480
/*      */       //   #182	-> 482
/*      */       //   #184	-> 489
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   126	26	5	el	Lorg/jsoup/nodes/Element;
/*      */       //   83	260	3	start	Lorg/jsoup/parser/Token$StartTag;
/*      */       //   89	254	4	name	Ljava/lang/String;
/*      */       //   356	126	4	name	Ljava/lang/String;
/*      */       //   349	133	5	end	Lorg/jsoup/parser/Token$EndTag;
/*      */       //   0	491	0	this	Lorg/jsoup/parser/HtmlTreeBuilderState$4;
/*      */       //   0	491	1	t	Lorg/jsoup/parser/Token;
/*      */       //   0	491	2	tb	Lorg/jsoup/parser/HtmlTreeBuilder;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean anythingElse(Token t, TreeBuilder tb) {
/*  188 */       tb.processEndTag("head");
/*  189 */       return tb.process(t);
/*      */     }
/*      */   },
/*  192 */   InHeadNoscript {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*  194 */       if (t.isDoctype())
/*  195 */       { tb.error(this); }
/*  196 */       else { if (t.isStartTag() && t.asStartTag().normalName().equals("html"))
/*  197 */           return tb.process(t, InBody); 
/*  198 */         if (t.isEndTag() && t.asEndTag().normalName().equals("noscript"))
/*  199 */         { tb.pop();
/*  200 */           tb.transition(InHead); }
/*  201 */         else { if (isWhitespace(t) || t.isComment() || (t.isStartTag() && StringUtil.inSorted(t.asStartTag().normalName(), Constants.InHeadNoScriptHead)))
/*      */           {
/*  203 */             return tb.process(t, InHead); } 
/*  204 */           if (t.isEndTag() && t.asEndTag().normalName().equals("br"))
/*  205 */             return anythingElse(t, tb); 
/*  206 */           if ((t.isStartTag() && StringUtil.inSorted(t.asStartTag().normalName(), Constants.InHeadNoscriptIgnore)) || t.isEndTag()) {
/*  207 */             tb.error(this);
/*  208 */             return false;
/*      */           } 
/*  210 */           return anythingElse(t, tb); }
/*      */          }
/*  212 */        return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/*  219 */       tb.error(this);
/*  220 */       tb.insert((new Token.Character()).data(t.toString()));
/*  221 */       return true;
/*      */     }
/*      */   },
/*  224 */   AfterHead {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*  226 */       if (isWhitespace(t)) {
/*  227 */         tb.insert(t.asCharacter());
/*  228 */       } else if (t.isComment()) {
/*  229 */         tb.insert(t.asComment());
/*  230 */       } else if (t.isDoctype()) {
/*  231 */         tb.error(this);
/*  232 */       } else if (t.isStartTag()) {
/*  233 */         Token.StartTag startTag = t.asStartTag();
/*  234 */         String name = startTag.normalName();
/*  235 */         if (name.equals("html"))
/*  236 */           return tb.process(t, InBody); 
/*  237 */         if (name.equals("body"))
/*  238 */         { tb.insert(startTag);
/*  239 */           tb.framesetOk(false);
/*  240 */           tb.transition(InBody); }
/*  241 */         else if (name.equals("frameset"))
/*  242 */         { tb.insert(startTag);
/*  243 */           tb.transition(InFrameset); }
/*  244 */         else if (StringUtil.inSorted(name, Constants.InBodyStartToHead))
/*  245 */         { tb.error(this);
/*  246 */           Element head = tb.getHeadElement();
/*  247 */           tb.push(head);
/*  248 */           tb.process(t, InHead);
/*  249 */           tb.removeFromStack(head); }
/*  250 */         else { if (name.equals("head")) {
/*  251 */             tb.error(this);
/*  252 */             return false;
/*      */           } 
/*  254 */           anythingElse(t, tb); }
/*      */       
/*  256 */       } else if (t.isEndTag()) {
/*  257 */         String name = t.asEndTag().normalName();
/*  258 */         if (StringUtil.inSorted(name, Constants.AfterHeadBody)) {
/*  259 */           anythingElse(t, tb);
/*  260 */         } else if (name.equals("template")) {
/*  261 */           tb.process(t, InHead);
/*      */         } else {
/*      */           
/*  264 */           tb.error(this);
/*  265 */           return false;
/*      */         } 
/*      */       } else {
/*  268 */         anythingElse(t, tb);
/*      */       } 
/*  270 */       return true;
/*      */     }
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/*  274 */       tb.processStartTag("body");
/*  275 */       tb.framesetOk(true);
/*  276 */       return tb.process(t);
/*      */     }
/*      */   },
/*  279 */   InBody
/*      */   {
/*      */     private static final int MaxStackScan = 24;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*      */       // Byte code:
/*      */       //   0: getstatic org/jsoup/parser/HtmlTreeBuilderState$25.$SwitchMap$org$jsoup$parser$Token$TokenType : [I
/*      */       //   3: aload_1
/*      */       //   4: getfield type : Lorg/jsoup/parser/Token$TokenType;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 164, 1 -> 116, 2 -> 127, 3 -> 134, 4 -> 141, 5 -> 48, 6 -> 148
/*      */       //   48: aload_1
/*      */       //   49: invokevirtual asCharacter : ()Lorg/jsoup/parser/Token$Character;
/*      */       //   52: astore_3
/*      */       //   53: aload_3
/*      */       //   54: invokevirtual getData : ()Ljava/lang/String;
/*      */       //   57: invokestatic access$400 : ()Ljava/lang/String;
/*      */       //   60: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   63: ifeq -> 73
/*      */       //   66: aload_2
/*      */       //   67: aload_0
/*      */       //   68: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   71: iconst_0
/*      */       //   72: ireturn
/*      */       //   73: aload_2
/*      */       //   74: invokevirtual framesetOk : ()Z
/*      */       //   77: ifeq -> 99
/*      */       //   80: aload_3
/*      */       //   81: invokestatic access$100 : (Lorg/jsoup/parser/Token;)Z
/*      */       //   84: ifeq -> 99
/*      */       //   87: aload_2
/*      */       //   88: invokevirtual reconstructFormattingElements : ()V
/*      */       //   91: aload_2
/*      */       //   92: aload_3
/*      */       //   93: invokevirtual insert : (Lorg/jsoup/parser/Token$Character;)V
/*      */       //   96: goto -> 164
/*      */       //   99: aload_2
/*      */       //   100: invokevirtual reconstructFormattingElements : ()V
/*      */       //   103: aload_2
/*      */       //   104: aload_3
/*      */       //   105: invokevirtual insert : (Lorg/jsoup/parser/Token$Character;)V
/*      */       //   108: aload_2
/*      */       //   109: iconst_0
/*      */       //   110: invokevirtual framesetOk : (Z)V
/*      */       //   113: goto -> 164
/*      */       //   116: aload_2
/*      */       //   117: aload_1
/*      */       //   118: invokevirtual asComment : ()Lorg/jsoup/parser/Token$Comment;
/*      */       //   121: invokevirtual insert : (Lorg/jsoup/parser/Token$Comment;)V
/*      */       //   124: goto -> 164
/*      */       //   127: aload_2
/*      */       //   128: aload_0
/*      */       //   129: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   132: iconst_0
/*      */       //   133: ireturn
/*      */       //   134: aload_0
/*      */       //   135: aload_1
/*      */       //   136: aload_2
/*      */       //   137: invokespecial inBodyStartTag : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   140: ireturn
/*      */       //   141: aload_0
/*      */       //   142: aload_1
/*      */       //   143: aload_2
/*      */       //   144: invokespecial inBodyEndTag : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   147: ireturn
/*      */       //   148: aload_2
/*      */       //   149: invokevirtual templateModeSize : ()I
/*      */       //   152: ifle -> 164
/*      */       //   155: aload_2
/*      */       //   156: aload_1
/*      */       //   157: getstatic org/jsoup/parser/HtmlTreeBuilderState$7.InTemplate : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   160: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   163: ireturn
/*      */       //   164: iconst_1
/*      */       //   165: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #281	-> 0
/*      */       //   #283	-> 48
/*      */       //   #284	-> 53
/*      */       //   #286	-> 66
/*      */       //   #287	-> 71
/*      */       //   #288	-> 73
/*      */       //   #289	-> 87
/*      */       //   #290	-> 91
/*      */       //   #292	-> 99
/*      */       //   #293	-> 103
/*      */       //   #294	-> 108
/*      */       //   #296	-> 113
/*      */       //   #299	-> 116
/*      */       //   #300	-> 124
/*      */       //   #303	-> 127
/*      */       //   #304	-> 132
/*      */       //   #307	-> 134
/*      */       //   #309	-> 141
/*      */       //   #311	-> 148
/*      */       //   #312	-> 155
/*      */       //   #317	-> 164
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   53	63	3	c	Lorg/jsoup/parser/Token$Character;
/*      */       //   0	166	0	this	Lorg/jsoup/parser/HtmlTreeBuilderState$7;
/*      */       //   0	166	1	t	Lorg/jsoup/parser/Token;
/*      */       //   0	166	2	tb	Lorg/jsoup/parser/HtmlTreeBuilder;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inBodyStartTag(Token t, HtmlTreeBuilder tb) {
/*      */       ArrayList<Element> stack;
/*      */       Element el;
/*      */       int i;
/*      */       Element body, second;
/*      */       String prompt;
/*      */       Attributes inputAttribs;
/*      */       int bottom, upper, j;
/*  321 */       Token.StartTag startTag = t.asStartTag();
/*  322 */       String name = startTag.normalName();
/*      */ 
/*      */ 
/*      */       
/*  326 */       switch (name)
/*      */       { case "a":
/*  328 */           if (tb.getActiveFormattingElement("a") != null) {
/*  329 */             tb.error(this);
/*  330 */             tb.processEndTag("a");
/*      */ 
/*      */             
/*  333 */             Element remainingA = tb.getFromStack("a");
/*  334 */             if (remainingA != null) {
/*  335 */               tb.removeFromActiveFormattingElements(remainingA);
/*  336 */               tb.removeFromStack(remainingA);
/*      */             } 
/*      */           } 
/*  339 */           tb.reconstructFormattingElements();
/*  340 */           el = tb.insert(startTag);
/*  341 */           tb.pushActiveFormattingElements(el);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  689 */           return true;case "span": tb.reconstructFormattingElements(); tb.insert(startTag); return true;case "li": tb.framesetOk(false); stack = tb.getStack(); for (i = stack.size() - 1; i > 0; i--) { el = stack.get(i); if (el.normalName().equals("li")) { tb.processEndTag("li"); break; }  if (tb.isSpecial(el) && !StringUtil.inSorted(el.normalName(), Constants.InBodyStartLiBreakers)) break;  }  if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.insert(startTag); return true;case "html": tb.error(this); if (tb.onStack("template")) return false;  stack = tb.getStack(); if (stack.size() > 0) { Element html = tb.getStack().get(0); if (startTag.hasAttributes()) for (Attribute attribute : startTag.attributes) { if (!html.hasAttr(attribute.getKey())) html.attributes().put(attribute);  }   }  return true;case "body": tb.error(this); stack = tb.getStack(); if (stack.size() == 1 || (stack.size() > 2 && !((Element)stack.get(1)).normalName().equals("body")) || tb.onStack("template")) return false;  tb.framesetOk(false); if (startTag.hasAttributes() && (body = tb.getFromStack("body")) != null) for (Attribute attribute : startTag.attributes) { if (!body.hasAttr(attribute.getKey())) body.attributes().put(attribute);  }   return true;case "frameset": tb.error(this); stack = tb.getStack(); if (stack.size() == 1 || (stack.size() > 2 && !((Element)stack.get(1)).normalName().equals("body"))) return false;  if (!tb.framesetOk()) return false;  second = stack.get(1); if (second.parent() != null) second.remove();  while (stack.size() > 1) stack.remove(stack.size() - 1);  tb.insert(startTag); tb.transition(InFrameset); return true;case "form": if (tb.getFormElement() != null && !tb.onStack("template")) { tb.error(this); return false; }  if (tb.inButtonScope("p")) tb.closeElement("p");  tb.insertForm(startTag, true, true); return true;case "plaintext": if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.insert(startTag); tb.tokeniser.transition(TokeniserState.PLAINTEXT); return true;case "button": if (tb.inButtonScope("button")) { tb.error(this); tb.processEndTag("button"); tb.process(startTag); } else { tb.reconstructFormattingElements(); tb.insert(startTag); tb.framesetOk(false); }  return true;case "nobr": tb.reconstructFormattingElements(); if (tb.inScope("nobr")) { tb.error(this); tb.processEndTag("nobr"); tb.reconstructFormattingElements(); }  el = tb.insert(startTag); tb.pushActiveFormattingElements(el); return true;case "table": if (tb.getDocument().quirksMode() != Document.QuirksMode.quirks && tb.inButtonScope("p")) tb.processEndTag("p");  tb.insert(startTag); tb.framesetOk(false); tb.transition(InTable); return true;case "input": tb.reconstructFormattingElements(); el = tb.insertEmpty(startTag); if (!el.attr("type").equalsIgnoreCase("hidden")) tb.framesetOk(false);  return true;case "hr": if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.insertEmpty(startTag); tb.framesetOk(false); return true;case "image": if (tb.getFromStack("svg") == null) return tb.process(startTag.name("img"));  tb.insert(startTag); return true;case "isindex": tb.error(this); if (tb.getFormElement() != null) return false;  tb.processStartTag("form"); if (startTag.hasAttribute("action")) { FormElement formElement = tb.getFormElement(); if (formElement != null && startTag.hasAttribute("action")) { String action = startTag.attributes.get("action"); formElement.attributes().put("action", action); }  }  tb.processStartTag("hr"); tb.processStartTag("label"); prompt = startTag.hasAttribute("prompt") ? startTag.attributes.get("prompt") : "This is a searchable index. Enter search keywords: "; tb.process((new Token.Character()).data(prompt)); inputAttribs = new Attributes(); if (startTag.hasAttributes()) for (Attribute attr : startTag.attributes) { if (!StringUtil.inSorted(attr.getKey(), Constants.InBodyStartInputAttribs)) inputAttribs.put(attr);  }   inputAttribs.put("name", "isindex"); tb.processStartTag("input", inputAttribs); tb.processEndTag("label"); tb.processStartTag("hr"); tb.processEndTag("form"); return true;case "textarea": tb.insert(startTag); if (!startTag.isSelfClosing()) { tb.tokeniser.transition(TokeniserState.Rcdata); tb.markInsertionMode(); tb.framesetOk(false); tb.transition(Text); }  return true;case "xmp": if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.reconstructFormattingElements(); tb.framesetOk(false); handleRawtext(startTag, tb); return true;case "iframe": tb.framesetOk(false); handleRawtext(startTag, tb); return true;case "noembed": handleRawtext(startTag, tb); return true;case "select": tb.reconstructFormattingElements(); tb.insert(startTag); tb.framesetOk(false); if (!startTag.selfClosing) { HtmlTreeBuilderState state = tb.state(); if (state.equals(InTable) || state.equals(InCaption) || state.equals(InTableBody) || state.equals(InRow) || state.equals(InCell)) { tb.transition(InSelectInTable); } else { tb.transition(InSelect); }  }  return true;case "math": tb.reconstructFormattingElements(); tb.insert(startTag); return true;case "svg": tb.reconstructFormattingElements(); tb.insert(startTag); return true;case "h1": case "h2": case "h3": case "h4": case "h5": case "h6": if (tb.inButtonScope("p")) tb.processEndTag("p");  if (StringUtil.inSorted(tb.currentElement().normalName(), Constants.Headings)) { tb.error(this); tb.pop(); }  tb.insert(startTag); return true;case "pre": case "listing": if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.insert(startTag); tb.reader.matchConsume("\n"); tb.framesetOk(false); return true;case "dd": case "dt": tb.framesetOk(false); stack = tb.getStack(); bottom = stack.size() - 1; upper = (bottom >= 24) ? (bottom - 24) : 0; for (j = bottom; j >= upper; j--) { el = stack.get(j); if (StringUtil.inSorted(el.normalName(), Constants.DdDt)) { tb.processEndTag(el.normalName()); break; }  if (tb.isSpecial(el) && !StringUtil.inSorted(el.normalName(), Constants.InBodyStartLiBreakers)) break;  }  if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.insert(startTag); return true;case "optgroup": case "option": if (tb.currentElementIs("option")) tb.processEndTag("option");  tb.reconstructFormattingElements(); tb.insert(startTag); return true;case "rp": case "rt": if (tb.inScope("ruby")) { tb.generateImpliedEndTags(); if (!tb.currentElementIs("ruby")) { tb.error(this); tb.popStackToBefore("ruby"); }  tb.insert(startTag); }  return true;case "area": case "br": case "embed": case "img": case "keygen": case "wbr": tb.reconstructFormattingElements(); tb.insertEmpty(startTag); tb.framesetOk(false); return true;case "b": case "big": case "code": case "em": case "font": case "i": case "s": case "small": case "strike": case "strong": case "tt": case "u": tb.reconstructFormattingElements(); el = tb.insert(startTag); tb.pushActiveFormattingElements(el); return true; }  if (!Tag.isKnownTag(name)) { tb.insert(startTag); } else if (StringUtil.inSorted(name, Constants.InBodyStartPClosers)) { if (tb.inButtonScope("p")) tb.processEndTag("p");  tb.insert(startTag); } else { if (StringUtil.inSorted(name, Constants.InBodyStartToHead)) return tb.process(t, InHead);  if (StringUtil.inSorted(name, Constants.InBodyStartApplets)) { tb.reconstructFormattingElements(); tb.insert(startTag); tb.insertMarkerToFormattingElements(); tb.framesetOk(false); } else if (StringUtil.inSorted(name, Constants.InBodyStartMedia)) { tb.insertEmpty(startTag); } else { if (StringUtil.inSorted(name, Constants.InBodyStartDrop)) { tb.error(this); return false; }  tb.reconstructFormattingElements(); tb.insert(startTag); }  }  return true;
/*      */     }
/*      */     
/*      */     private boolean inBodyEndTag(Token t, HtmlTreeBuilder tb) {
/*      */       boolean notIgnored;
/*  694 */       Token.EndTag endTag = t.asEndTag();
/*  695 */       String name = endTag.normalName();
/*      */       
/*  697 */       switch (name)
/*      */       { case "template":
/*  699 */           tb.process(t, InHead);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  829 */           return true;case "sarcasm": case "span": return anyOtherEndTag(t, tb);case "li": if (!tb.inListItemScope(name)) { tb.error(this); return false; }  tb.generateImpliedEndTags(name); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(name); return true;case "body": if (!tb.inScope("body")) { tb.error(this); return false; }  anyOtherEndTag(t, tb); tb.transition(AfterBody); return true;case "html": notIgnored = tb.processEndTag("body"); if (notIgnored) return tb.process(endTag);  return true;case "form": if (!tb.onStack("template")) { FormElement formElement = tb.getFormElement(); tb.setFormElement((FormElement)null); if (formElement == null || !tb.inScope(name)) { tb.error(this); return false; }  tb.generateImpliedEndTags(); if (!tb.currentElementIs(name)) tb.error(this);  tb.removeFromStack((Element)formElement); } else { if (!tb.inScope(name)) { tb.error(this); return false; }  tb.generateImpliedEndTags(); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(name); }  return true;case "p": if (!tb.inButtonScope(name)) { tb.error(this); tb.processStartTag(name); return tb.process(endTag); }  tb.generateImpliedEndTags(name); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(name); return true;case "dd": case "dt": if (!tb.inScope(name)) { tb.error(this); return false; }  tb.generateImpliedEndTags(name); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(name); return true;case "h1": case "h2": case "h3": case "h4": case "h5": case "h6": if (!tb.inScope(Constants.Headings)) { tb.error(this); return false; }  tb.generateImpliedEndTags(name); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(Constants.Headings); return true;case "br": tb.error(this); tb.processStartTag("br"); return false; }  if (StringUtil.inSorted(name, Constants.InBodyEndAdoptionFormatters)) return inBodyEndTagAdoption(t, tb);  if (StringUtil.inSorted(name, Constants.InBodyEndClosers)) { if (!tb.inScope(name)) { tb.error(this); return false; }  tb.generateImpliedEndTags(); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(name); } else if (StringUtil.inSorted(name, Constants.InBodyStartApplets)) { if (!tb.inScope("name")) { if (!tb.inScope(name)) { tb.error(this); return false; }  tb.generateImpliedEndTags(); if (!tb.currentElementIs(name)) tb.error(this);  tb.popStackToClose(name); tb.clearFormattingElementsToLastMarker(); }  } else { return anyOtherEndTag(t, tb); }  return true;
/*      */     }
/*      */     
/*      */     boolean anyOtherEndTag(Token t, HtmlTreeBuilder tb) {
/*  833 */       String name = (t.asEndTag()).normalName;
/*  834 */       ArrayList<Element> stack = tb.getStack();
/*      */ 
/*      */       
/*  837 */       Element elFromStack = tb.getFromStack(name);
/*  838 */       if (elFromStack == null) {
/*  839 */         tb.error(this);
/*  840 */         return false;
/*      */       } 
/*      */       
/*  843 */       for (int pos = stack.size() - 1; pos >= 0; pos--) {
/*  844 */         Element node = stack.get(pos);
/*  845 */         if (node.normalName().equals(name)) {
/*  846 */           tb.generateImpliedEndTags(name);
/*  847 */           if (!tb.currentElementIs(name))
/*  848 */             tb.error(this); 
/*  849 */           tb.popStackToClose(name);
/*      */           break;
/*      */         } 
/*  852 */         if (tb.isSpecial(node)) {
/*  853 */           tb.error(this);
/*  854 */           return false;
/*      */         } 
/*      */       } 
/*      */       
/*  858 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean inBodyEndTagAdoption(Token t, HtmlTreeBuilder tb) {
/*  863 */       Token.EndTag endTag = t.asEndTag();
/*  864 */       String name = endTag.normalName();
/*      */       
/*  866 */       ArrayList<Element> stack = tb.getStack();
/*      */       
/*  868 */       for (int i = 0; i < 8; i++) {
/*  869 */         Element formatEl = tb.getActiveFormattingElement(name);
/*  870 */         if (formatEl == null)
/*  871 */           return anyOtherEndTag(t, tb); 
/*  872 */         if (!tb.onStack(formatEl)) {
/*  873 */           tb.error(this);
/*  874 */           tb.removeFromActiveFormattingElements(formatEl);
/*  875 */           return true;
/*  876 */         }  if (!tb.inScope(formatEl.normalName())) {
/*  877 */           tb.error(this);
/*  878 */           return false;
/*  879 */         }  if (tb.currentElement() != formatEl) {
/*  880 */           tb.error(this);
/*      */         }
/*  882 */         Element furthestBlock = null;
/*  883 */         Element commonAncestor = null;
/*  884 */         boolean seenFormattingElement = false;
/*      */         
/*  886 */         int stackSize = stack.size();
/*  887 */         int bookmark = -1;
/*  888 */         for (int si = 1; si < stackSize && si < 64; si++) {
/*      */           
/*  890 */           Element el = stack.get(si);
/*  891 */           if (el == formatEl) {
/*  892 */             commonAncestor = stack.get(si - 1);
/*  893 */             seenFormattingElement = true;
/*      */             
/*  895 */             bookmark = tb.positionOfElement(el);
/*  896 */           } else if (seenFormattingElement && tb.isSpecial(el)) {
/*  897 */             furthestBlock = el;
/*      */             break;
/*      */           } 
/*      */         } 
/*  901 */         if (furthestBlock == null) {
/*  902 */           tb.popStackToClose(formatEl.normalName());
/*  903 */           tb.removeFromActiveFormattingElements(formatEl);
/*  904 */           return true;
/*      */         } 
/*      */         
/*  907 */         Element node = furthestBlock;
/*  908 */         Element lastNode = furthestBlock;
/*  909 */         for (int j = 0; j < 3; j++) {
/*  910 */           if (tb.onStack(node))
/*  911 */             node = tb.aboveOnStack(node); 
/*  912 */           if (!tb.isInActiveFormattingElements(node)) {
/*  913 */             tb.removeFromStack(node);
/*      */           } else {
/*  915 */             if (node == formatEl) {
/*      */               break;
/*      */             }
/*  918 */             Element replacement = new Element(tb.tagFor(node.nodeName(), ParseSettings.preserveCase), tb.getBaseUri());
/*      */             
/*  920 */             tb.replaceActiveFormattingElement(node, replacement);
/*  921 */             tb.replaceOnStack(node, replacement);
/*  922 */             node = replacement;
/*      */             
/*  924 */             if (lastNode == furthestBlock)
/*      */             {
/*      */               
/*  927 */               bookmark = tb.positionOfElement(node) + 1;
/*      */             }
/*  929 */             if (lastNode.parent() != null)
/*  930 */               lastNode.remove(); 
/*  931 */             node.appendChild((Node)lastNode);
/*      */             
/*  933 */             lastNode = node;
/*      */           } 
/*      */         } 
/*  936 */         if (commonAncestor != null) {
/*  937 */           if (StringUtil.inSorted(commonAncestor.normalName(), Constants.InBodyEndTableFosters)) {
/*  938 */             if (lastNode.parent() != null)
/*  939 */               lastNode.remove(); 
/*  940 */             tb.insertInFosterParent((Node)lastNode);
/*      */           } else {
/*  942 */             if (lastNode.parent() != null)
/*  943 */               lastNode.remove(); 
/*  944 */             commonAncestor.appendChild((Node)lastNode);
/*      */           } 
/*      */         }
/*      */         
/*  948 */         Element adopter = new Element(formatEl.tag(), tb.getBaseUri());
/*  949 */         adopter.attributes().addAll(formatEl.attributes());
/*  950 */         adopter.appendChildren(furthestBlock.childNodes());
/*  951 */         furthestBlock.appendChild((Node)adopter);
/*  952 */         tb.removeFromActiveFormattingElements(formatEl);
/*      */         
/*  954 */         tb.pushWithBookmark(adopter, bookmark);
/*  955 */         tb.removeFromStack(formatEl);
/*  956 */         tb.insertOnStackAfter(furthestBlock, adopter);
/*      */       } 
/*  958 */       return true;
/*      */     }
/*      */   },
/*  961 */   Text
/*      */   {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*  964 */       if (t.isCharacter())
/*  965 */       { tb.insert(t.asCharacter()); }
/*  966 */       else { if (t.isEOF()) {
/*  967 */           tb.error(this);
/*      */           
/*  969 */           tb.pop();
/*  970 */           tb.transition(tb.originalState());
/*  971 */           return tb.process(t);
/*  972 */         }  if (t.isEndTag()) {
/*      */           
/*  974 */           tb.pop();
/*  975 */           tb.transition(tb.originalState());
/*      */         }  }
/*  977 */        return true;
/*      */     }
/*      */   },
/*  980 */   InTable {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*  982 */       if (t.isCharacter() && StringUtil.inSorted(tb.currentElement().normalName(), Constants.InTableFoster)) {
/*  983 */         tb.newPendingTableCharacters();
/*  984 */         tb.markInsertionMode();
/*  985 */         tb.transition(InTableText);
/*  986 */         return tb.process(t);
/*  987 */       }  if (t.isComment()) {
/*  988 */         tb.insert(t.asComment());
/*  989 */         return true;
/*  990 */       }  if (t.isDoctype()) {
/*  991 */         tb.error(this);
/*  992 */         return false;
/*  993 */       }  if (t.isStartTag()) {
/*  994 */         Token.StartTag startTag = t.asStartTag();
/*  995 */         String name = startTag.normalName();
/*  996 */         if (name.equals("caption"))
/*  997 */         { tb.clearStackToTableContext();
/*  998 */           tb.insertMarkerToFormattingElements();
/*  999 */           tb.insert(startTag);
/* 1000 */           tb.transition(InCaption); }
/* 1001 */         else if (name.equals("colgroup"))
/* 1002 */         { tb.clearStackToTableContext();
/* 1003 */           tb.insert(startTag);
/* 1004 */           tb.transition(InColumnGroup); }
/* 1005 */         else { if (name.equals("col")) {
/* 1006 */             tb.clearStackToTableContext();
/* 1007 */             tb.processStartTag("colgroup");
/* 1008 */             return tb.process(t);
/* 1009 */           }  if (StringUtil.inSorted(name, Constants.InTableToBody))
/* 1010 */           { tb.clearStackToTableContext();
/* 1011 */             tb.insert(startTag);
/* 1012 */             tb.transition(InTableBody); }
/* 1013 */           else { if (StringUtil.inSorted(name, Constants.InTableAddBody)) {
/* 1014 */               tb.clearStackToTableContext();
/* 1015 */               tb.processStartTag("tbody");
/* 1016 */               return tb.process(t);
/* 1017 */             }  if (name.equals("table")) {
/* 1018 */               tb.error(this);
/* 1019 */               if (!tb.inTableScope(name)) {
/* 1020 */                 return false;
/*      */               }
/* 1022 */               tb.popStackToClose(name);
/* 1023 */               if (!tb.resetInsertionMode()) {
/*      */                 
/* 1025 */                 tb.insert(startTag);
/* 1026 */                 return true;
/*      */               } 
/* 1028 */               return tb.process(t);
/*      */             } 
/* 1030 */             if (StringUtil.inSorted(name, Constants.InTableToHead))
/* 1031 */               return tb.process(t, InHead); 
/* 1032 */             if (name.equals("input"))
/* 1033 */             { if (!startTag.hasAttributes() || !startTag.attributes.get("type").equalsIgnoreCase("hidden")) {
/* 1034 */                 return anythingElse(t, tb);
/*      */               }
/* 1036 */               tb.insertEmpty(startTag); }
/*      */             
/* 1038 */             else if (name.equals("form"))
/* 1039 */             { tb.error(this);
/* 1040 */               if (tb.getFormElement() != null || tb.onStack("template")) {
/* 1041 */                 return false;
/*      */               }
/* 1043 */               tb.insertForm(startTag, false, false); }
/*      */             else
/*      */             
/* 1046 */             { return anythingElse(t, tb); }  }
/*      */            }
/* 1048 */          return true;
/* 1049 */       }  if (t.isEndTag()) {
/* 1050 */         Token.EndTag endTag = t.asEndTag();
/* 1051 */         String name = endTag.normalName();
/*      */         
/* 1053 */         if (name.equals("table")) {
/* 1054 */           if (!tb.inTableScope(name)) {
/* 1055 */             tb.error(this);
/* 1056 */             return false;
/*      */           } 
/* 1058 */           tb.popStackToClose("table");
/* 1059 */           tb.resetInsertionMode();
/*      */         } else {
/* 1061 */           if (StringUtil.inSorted(name, Constants.InTableEndErr)) {
/* 1062 */             tb.error(this);
/* 1063 */             return false;
/* 1064 */           }  if (name.equals("template")) {
/* 1065 */             tb.process(t, InHead);
/*      */           } else {
/* 1067 */             return anythingElse(t, tb);
/*      */           } 
/* 1069 */         }  return true;
/* 1070 */       }  if (t.isEOF()) {
/* 1071 */         if (tb.currentElementIs("html"))
/* 1072 */           tb.error(this); 
/* 1073 */         return true;
/*      */       } 
/* 1075 */       return anythingElse(t, tb);
/*      */     }
/*      */     
/*      */     boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/* 1079 */       tb.error(this);
/* 1080 */       tb.setFosterInserts(true);
/* 1081 */       tb.process(t, InBody);
/* 1082 */       tb.setFosterInserts(false);
/* 1083 */       return true;
/*      */     }
/*      */   },
/* 1086 */   InTableText {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1088 */       if (t.type == Token.TokenType.Character) {
/* 1089 */         Token.Character c = t.asCharacter();
/* 1090 */         if (c.getData().equals(HtmlTreeBuilderState.nullString)) {
/* 1091 */           tb.error(this);
/* 1092 */           return false;
/*      */         } 
/* 1094 */         tb.getPendingTableCharacters().add(c.getData());
/*      */       } else {
/*      */         
/* 1097 */         if (tb.getPendingTableCharacters().size() > 0) {
/* 1098 */           for (String character : tb.getPendingTableCharacters()) {
/* 1099 */             if (!isWhitespace(character)) {
/*      */               
/* 1101 */               tb.error(this);
/* 1102 */               if (StringUtil.inSorted(tb.currentElement().normalName(), Constants.InTableFoster)) {
/* 1103 */                 tb.setFosterInserts(true);
/* 1104 */                 tb.process((new Token.Character()).data(character), InBody);
/* 1105 */                 tb.setFosterInserts(false); continue;
/*      */               } 
/* 1107 */               tb.process((new Token.Character()).data(character), InBody);
/*      */               continue;
/*      */             } 
/* 1110 */             tb.insert((new Token.Character()).data(character));
/*      */           } 
/* 1112 */           tb.newPendingTableCharacters();
/*      */         } 
/* 1114 */         tb.transition(tb.originalState());
/* 1115 */         return tb.process(t);
/*      */       } 
/* 1117 */       return true;
/*      */     }
/*      */   },
/* 1120 */   InCaption {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1122 */       if (t.isEndTag() && t.asEndTag().normalName().equals("caption"))
/* 1123 */       { Token.EndTag endTag = t.asEndTag();
/* 1124 */         String name = endTag.normalName();
/* 1125 */         if (!tb.inTableScope(name)) {
/* 1126 */           tb.error(this);
/* 1127 */           return false;
/*      */         } 
/* 1129 */         tb.generateImpliedEndTags();
/* 1130 */         if (!tb.currentElementIs("caption"))
/* 1131 */           tb.error(this); 
/* 1132 */         tb.popStackToClose("caption");
/* 1133 */         tb.clearFormattingElementsToLastMarker();
/* 1134 */         tb.transition(InTable); }
/*      */       
/* 1136 */       else if ((t
/* 1137 */         .isStartTag() && StringUtil.inSorted(t.asStartTag().normalName(), Constants.InCellCol)) || (t
/* 1138 */         .isEndTag() && t.asEndTag().normalName().equals("table")))
/*      */       
/* 1140 */       { tb.error(this);
/* 1141 */         boolean processed = tb.processEndTag("caption");
/* 1142 */         if (processed)
/* 1143 */           return tb.process(t);  }
/* 1144 */       else { if (t.isEndTag() && StringUtil.inSorted(t.asEndTag().normalName(), Constants.InCaptionIgnore)) {
/* 1145 */           tb.error(this);
/* 1146 */           return false;
/*      */         } 
/* 1148 */         return tb.process(t, InBody); }
/*      */       
/* 1150 */       return true;
/*      */     }
/*      */   },
/* 1153 */   InColumnGroup
/*      */   {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*      */       // Byte code:
/*      */       //   0: aload_1
/*      */       //   1: invokestatic access$100 : (Lorg/jsoup/parser/Token;)Z
/*      */       //   4: ifeq -> 17
/*      */       //   7: aload_2
/*      */       //   8: aload_1
/*      */       //   9: invokevirtual asCharacter : ()Lorg/jsoup/parser/Token$Character;
/*      */       //   12: invokevirtual insert : (Lorg/jsoup/parser/Token$Character;)V
/*      */       //   15: iconst_1
/*      */       //   16: ireturn
/*      */       //   17: getstatic org/jsoup/parser/HtmlTreeBuilderState$25.$SwitchMap$org$jsoup$parser$Token$TokenType : [I
/*      */       //   20: aload_1
/*      */       //   21: getfield type : Lorg/jsoup/parser/Token$TokenType;
/*      */       //   24: invokevirtual ordinal : ()I
/*      */       //   27: iaload
/*      */       //   28: tableswitch default -> 431, 1 -> 68, 2 -> 79, 3 -> 87, 4 -> 252, 5 -> 431, 6 -> 413
/*      */       //   68: aload_2
/*      */       //   69: aload_1
/*      */       //   70: invokevirtual asComment : ()Lorg/jsoup/parser/Token$Comment;
/*      */       //   73: invokevirtual insert : (Lorg/jsoup/parser/Token$Comment;)V
/*      */       //   76: goto -> 438
/*      */       //   79: aload_2
/*      */       //   80: aload_0
/*      */       //   81: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   84: goto -> 438
/*      */       //   87: aload_1
/*      */       //   88: invokevirtual asStartTag : ()Lorg/jsoup/parser/Token$StartTag;
/*      */       //   91: astore_3
/*      */       //   92: aload_3
/*      */       //   93: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   96: astore #4
/*      */       //   98: iconst_m1
/*      */       //   99: istore #5
/*      */       //   101: aload #4
/*      */       //   103: invokevirtual hashCode : ()I
/*      */       //   106: lookupswitch default -> 185, -1321546630 -> 172, 98688 -> 156, 3213227 -> 140
/*      */       //   140: aload #4
/*      */       //   142: ldc 'html'
/*      */       //   144: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   147: ifeq -> 185
/*      */       //   150: iconst_0
/*      */       //   151: istore #5
/*      */       //   153: goto -> 185
/*      */       //   156: aload #4
/*      */       //   158: ldc 'col'
/*      */       //   160: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   163: ifeq -> 185
/*      */       //   166: iconst_1
/*      */       //   167: istore #5
/*      */       //   169: goto -> 185
/*      */       //   172: aload #4
/*      */       //   174: ldc 'template'
/*      */       //   176: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   179: ifeq -> 185
/*      */       //   182: iconst_2
/*      */       //   183: istore #5
/*      */       //   185: iload #5
/*      */       //   187: tableswitch default -> 242, 0 -> 212, 1 -> 221, 2 -> 230
/*      */       //   212: aload_2
/*      */       //   213: aload_1
/*      */       //   214: getstatic org/jsoup/parser/HtmlTreeBuilderState$12.InBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   217: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   220: ireturn
/*      */       //   221: aload_2
/*      */       //   222: aload_3
/*      */       //   223: invokevirtual insertEmpty : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   226: pop
/*      */       //   227: goto -> 249
/*      */       //   230: aload_2
/*      */       //   231: aload_1
/*      */       //   232: getstatic org/jsoup/parser/HtmlTreeBuilderState$12.InHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   235: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   238: pop
/*      */       //   239: goto -> 249
/*      */       //   242: aload_0
/*      */       //   243: aload_1
/*      */       //   244: aload_2
/*      */       //   245: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   248: ireturn
/*      */       //   249: goto -> 438
/*      */       //   252: aload_1
/*      */       //   253: invokevirtual asEndTag : ()Lorg/jsoup/parser/Token$EndTag;
/*      */       //   256: astore #4
/*      */       //   258: aload #4
/*      */       //   260: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   263: astore #5
/*      */       //   265: aload #5
/*      */       //   267: astore #6
/*      */       //   269: iconst_m1
/*      */       //   270: istore #7
/*      */       //   272: aload #6
/*      */       //   274: invokevirtual hashCode : ()I
/*      */       //   277: lookupswitch default -> 333, -1321546630 -> 320, -636197633 -> 304
/*      */       //   304: aload #6
/*      */       //   306: ldc 'colgroup'
/*      */       //   308: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   311: ifeq -> 333
/*      */       //   314: iconst_0
/*      */       //   315: istore #7
/*      */       //   317: goto -> 333
/*      */       //   320: aload #6
/*      */       //   322: ldc 'template'
/*      */       //   324: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   327: ifeq -> 333
/*      */       //   330: iconst_1
/*      */       //   331: istore #7
/*      */       //   333: iload #7
/*      */       //   335: lookupswitch default -> 403, 0 -> 360, 1 -> 391
/*      */       //   360: aload_2
/*      */       //   361: aload #5
/*      */       //   363: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   366: ifne -> 376
/*      */       //   369: aload_2
/*      */       //   370: aload_0
/*      */       //   371: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   374: iconst_0
/*      */       //   375: ireturn
/*      */       //   376: aload_2
/*      */       //   377: invokevirtual pop : ()Lorg/jsoup/nodes/Element;
/*      */       //   380: pop
/*      */       //   381: aload_2
/*      */       //   382: getstatic org/jsoup/parser/HtmlTreeBuilderState$12.InTable : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   385: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   388: goto -> 410
/*      */       //   391: aload_2
/*      */       //   392: aload_1
/*      */       //   393: getstatic org/jsoup/parser/HtmlTreeBuilderState$12.InHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   396: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   399: pop
/*      */       //   400: goto -> 410
/*      */       //   403: aload_0
/*      */       //   404: aload_1
/*      */       //   405: aload_2
/*      */       //   406: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   409: ireturn
/*      */       //   410: goto -> 438
/*      */       //   413: aload_2
/*      */       //   414: ldc 'html'
/*      */       //   416: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   419: ifeq -> 424
/*      */       //   422: iconst_1
/*      */       //   423: ireturn
/*      */       //   424: aload_0
/*      */       //   425: aload_1
/*      */       //   426: aload_2
/*      */       //   427: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   430: ireturn
/*      */       //   431: aload_0
/*      */       //   432: aload_1
/*      */       //   433: aload_2
/*      */       //   434: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   437: ireturn
/*      */       //   438: iconst_1
/*      */       //   439: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1155	-> 0
/*      */       //   #1156	-> 7
/*      */       //   #1157	-> 15
/*      */       //   #1159	-> 17
/*      */       //   #1161	-> 68
/*      */       //   #1162	-> 76
/*      */       //   #1164	-> 79
/*      */       //   #1165	-> 84
/*      */       //   #1167	-> 87
/*      */       //   #1168	-> 92
/*      */       //   #1170	-> 212
/*      */       //   #1172	-> 221
/*      */       //   #1173	-> 227
/*      */       //   #1175	-> 230
/*      */       //   #1176	-> 239
/*      */       //   #1178	-> 242
/*      */       //   #1180	-> 249
/*      */       //   #1182	-> 252
/*      */       //   #1183	-> 258
/*      */       //   #1184	-> 265
/*      */       //   #1186	-> 360
/*      */       //   #1187	-> 369
/*      */       //   #1188	-> 374
/*      */       //   #1190	-> 376
/*      */       //   #1191	-> 381
/*      */       //   #1193	-> 388
/*      */       //   #1195	-> 391
/*      */       //   #1196	-> 400
/*      */       //   #1198	-> 403
/*      */       //   #1200	-> 410
/*      */       //   #1202	-> 413
/*      */       //   #1203	-> 422
/*      */       //   #1205	-> 424
/*      */       //   #1207	-> 431
/*      */       //   #1209	-> 438
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   92	160	3	startTag	Lorg/jsoup/parser/Token$StartTag;
/*      */       //   258	155	4	endTag	Lorg/jsoup/parser/Token$EndTag;
/*      */       //   265	148	5	name	Ljava/lang/String;
/*      */       //   0	440	0	this	Lorg/jsoup/parser/HtmlTreeBuilderState$12;
/*      */       //   0	440	1	t	Lorg/jsoup/parser/Token;
/*      */       //   0	440	2	tb	Lorg/jsoup/parser/HtmlTreeBuilder;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/* 1213 */       if (!tb.currentElementIs("colgroup")) {
/* 1214 */         tb.error(this);
/* 1215 */         return false;
/*      */       } 
/* 1217 */       tb.pop();
/* 1218 */       tb.transition(InTable);
/* 1219 */       tb.process(t);
/* 1220 */       return true;
/*      */     }
/*      */   },
/* 1223 */   InTableBody
/*      */   {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*      */       // Byte code:
/*      */       //   0: getstatic org/jsoup/parser/HtmlTreeBuilderState$25.$SwitchMap$org$jsoup$parser$Token$TokenType : [I
/*      */       //   3: aload_1
/*      */       //   4: getfield type : Lorg/jsoup/parser/Token$TokenType;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: lookupswitch default -> 232, 3 -> 36, 4 -> 131
/*      */       //   36: aload_1
/*      */       //   37: invokevirtual asStartTag : ()Lorg/jsoup/parser/Token$StartTag;
/*      */       //   40: astore_3
/*      */       //   41: aload_3
/*      */       //   42: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   45: astore #4
/*      */       //   47: aload #4
/*      */       //   49: ldc 'tr'
/*      */       //   51: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   54: ifeq -> 77
/*      */       //   57: aload_2
/*      */       //   58: invokevirtual clearStackToTableBodyContext : ()V
/*      */       //   61: aload_2
/*      */       //   62: aload_3
/*      */       //   63: invokevirtual insert : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   66: pop
/*      */       //   67: aload_2
/*      */       //   68: getstatic org/jsoup/parser/HtmlTreeBuilderState$13.InRow : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   71: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   74: goto -> 239
/*      */       //   77: aload #4
/*      */       //   79: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InCellNames : [Ljava/lang/String;
/*      */       //   82: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   85: ifeq -> 106
/*      */       //   88: aload_2
/*      */       //   89: aload_0
/*      */       //   90: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   93: aload_2
/*      */       //   94: ldc 'tr'
/*      */       //   96: invokevirtual processStartTag : (Ljava/lang/String;)Z
/*      */       //   99: pop
/*      */       //   100: aload_2
/*      */       //   101: aload_3
/*      */       //   102: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   105: ireturn
/*      */       //   106: aload #4
/*      */       //   108: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InTableBodyExit : [Ljava/lang/String;
/*      */       //   111: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   114: ifeq -> 124
/*      */       //   117: aload_0
/*      */       //   118: aload_1
/*      */       //   119: aload_2
/*      */       //   120: invokespecial exitTableBody : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   123: ireturn
/*      */       //   124: aload_0
/*      */       //   125: aload_1
/*      */       //   126: aload_2
/*      */       //   127: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   130: ireturn
/*      */       //   131: aload_1
/*      */       //   132: invokevirtual asEndTag : ()Lorg/jsoup/parser/Token$EndTag;
/*      */       //   135: astore #5
/*      */       //   137: aload #5
/*      */       //   139: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   142: astore #4
/*      */       //   144: aload #4
/*      */       //   146: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InTableEndIgnore : [Ljava/lang/String;
/*      */       //   149: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   152: ifeq -> 190
/*      */       //   155: aload_2
/*      */       //   156: aload #4
/*      */       //   158: invokevirtual inTableScope : (Ljava/lang/String;)Z
/*      */       //   161: ifne -> 171
/*      */       //   164: aload_2
/*      */       //   165: aload_0
/*      */       //   166: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   169: iconst_0
/*      */       //   170: ireturn
/*      */       //   171: aload_2
/*      */       //   172: invokevirtual clearStackToTableBodyContext : ()V
/*      */       //   175: aload_2
/*      */       //   176: invokevirtual pop : ()Lorg/jsoup/nodes/Element;
/*      */       //   179: pop
/*      */       //   180: aload_2
/*      */       //   181: getstatic org/jsoup/parser/HtmlTreeBuilderState$13.InTable : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   184: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   187: goto -> 239
/*      */       //   190: aload #4
/*      */       //   192: ldc 'table'
/*      */       //   194: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   197: ifeq -> 207
/*      */       //   200: aload_0
/*      */       //   201: aload_1
/*      */       //   202: aload_2
/*      */       //   203: invokespecial exitTableBody : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   206: ireturn
/*      */       //   207: aload #4
/*      */       //   209: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InTableBodyEndIgnore : [Ljava/lang/String;
/*      */       //   212: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   215: ifeq -> 225
/*      */       //   218: aload_2
/*      */       //   219: aload_0
/*      */       //   220: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   223: iconst_0
/*      */       //   224: ireturn
/*      */       //   225: aload_0
/*      */       //   226: aload_1
/*      */       //   227: aload_2
/*      */       //   228: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   231: ireturn
/*      */       //   232: aload_0
/*      */       //   233: aload_1
/*      */       //   234: aload_2
/*      */       //   235: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   238: ireturn
/*      */       //   239: iconst_1
/*      */       //   240: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1225	-> 0
/*      */       //   #1227	-> 36
/*      */       //   #1228	-> 41
/*      */       //   #1229	-> 47
/*      */       //   #1230	-> 57
/*      */       //   #1231	-> 61
/*      */       //   #1232	-> 67
/*      */       //   #1233	-> 77
/*      */       //   #1234	-> 88
/*      */       //   #1235	-> 93
/*      */       //   #1236	-> 100
/*      */       //   #1237	-> 106
/*      */       //   #1238	-> 117
/*      */       //   #1240	-> 124
/*      */       //   #1243	-> 131
/*      */       //   #1244	-> 137
/*      */       //   #1245	-> 144
/*      */       //   #1246	-> 155
/*      */       //   #1247	-> 164
/*      */       //   #1248	-> 169
/*      */       //   #1250	-> 171
/*      */       //   #1251	-> 175
/*      */       //   #1252	-> 180
/*      */       //   #1254	-> 190
/*      */       //   #1255	-> 200
/*      */       //   #1256	-> 207
/*      */       //   #1257	-> 218
/*      */       //   #1258	-> 223
/*      */       //   #1260	-> 225
/*      */       //   #1263	-> 232
/*      */       //   #1265	-> 239
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   41	90	3	startTag	Lorg/jsoup/parser/Token$StartTag;
/*      */       //   47	84	4	name	Ljava/lang/String;
/*      */       //   144	88	4	name	Ljava/lang/String;
/*      */       //   137	95	5	endTag	Lorg/jsoup/parser/Token$EndTag;
/*      */       //   0	241	0	this	Lorg/jsoup/parser/HtmlTreeBuilderState$13;
/*      */       //   0	241	1	t	Lorg/jsoup/parser/Token;
/*      */       //   0	241	2	tb	Lorg/jsoup/parser/HtmlTreeBuilder;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean exitTableBody(Token t, HtmlTreeBuilder tb) {
/* 1269 */       if (!tb.inTableScope("tbody") && !tb.inTableScope("thead") && !tb.inScope("tfoot")) {
/*      */         
/* 1271 */         tb.error(this);
/* 1272 */         return false;
/*      */       } 
/* 1274 */       tb.clearStackToTableBodyContext();
/* 1275 */       tb.processEndTag(tb.currentElement().normalName());
/* 1276 */       return tb.process(t);
/*      */     }
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/* 1280 */       return tb.process(t, InTable);
/*      */     }
/*      */   },
/* 1283 */   InRow {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1285 */       if (t.isStartTag())
/* 1286 */       { Token.StartTag startTag = t.asStartTag();
/* 1287 */         String name = startTag.normalName();
/*      */         
/* 1289 */         if (StringUtil.inSorted(name, Constants.InCellNames))
/* 1290 */         { tb.clearStackToTableRowContext();
/* 1291 */           tb.insert(startTag);
/* 1292 */           tb.transition(InCell);
/* 1293 */           tb.insertMarkerToFormattingElements(); }
/* 1294 */         else { if (StringUtil.inSorted(name, Constants.InRowMissing)) {
/* 1295 */             return handleMissingTr(t, tb);
/*      */           }
/* 1297 */           return anythingElse(t, tb); }
/*      */          }
/* 1299 */       else if (t.isEndTag())
/* 1300 */       { Token.EndTag endTag = t.asEndTag();
/* 1301 */         String name = endTag.normalName();
/*      */         
/* 1303 */         if (name.equals("tr"))
/* 1304 */         { if (!tb.inTableScope(name)) {
/* 1305 */             tb.error(this);
/* 1306 */             return false;
/*      */           } 
/* 1308 */           tb.clearStackToTableRowContext();
/* 1309 */           tb.pop();
/* 1310 */           tb.transition(InTableBody); }
/* 1311 */         else { if (name.equals("table"))
/* 1312 */             return handleMissingTr(t, tb); 
/* 1313 */           if (StringUtil.inSorted(name, Constants.InTableToBody))
/* 1314 */           { if (!tb.inTableScope(name) || !tb.inTableScope("tr")) {
/* 1315 */               tb.error(this);
/* 1316 */               return false;
/*      */             } 
/* 1318 */             tb.clearStackToTableRowContext();
/* 1319 */             tb.pop();
/* 1320 */             tb.transition(InTableBody); }
/* 1321 */           else { if (StringUtil.inSorted(name, Constants.InRowIgnore)) {
/* 1322 */               tb.error(this);
/* 1323 */               return false;
/*      */             } 
/* 1325 */             return anythingElse(t, tb); }
/*      */            }
/*      */          }
/* 1328 */       else { return anythingElse(t, tb); }
/*      */       
/* 1330 */       return true;
/*      */     }
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/* 1334 */       return tb.process(t, InTable);
/*      */     }
/*      */     
/*      */     private boolean handleMissingTr(Token t, TreeBuilder tb) {
/* 1338 */       boolean processed = tb.processEndTag("tr");
/* 1339 */       if (processed) {
/* 1340 */         return tb.process(t);
/*      */       }
/* 1342 */       return false;
/*      */     }
/*      */   },
/* 1345 */   InCell {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1347 */       if (t.isEndTag())
/* 1348 */       { Token.EndTag endTag = t.asEndTag();
/* 1349 */         String name = endTag.normalName();
/*      */         
/* 1351 */         if (StringUtil.inSorted(name, Constants.InCellNames))
/* 1352 */         { if (!tb.inTableScope(name)) {
/* 1353 */             tb.error(this);
/* 1354 */             tb.transition(InRow);
/* 1355 */             return false;
/*      */           } 
/* 1357 */           tb.generateImpliedEndTags();
/* 1358 */           if (!tb.currentElementIs(name))
/* 1359 */             tb.error(this); 
/* 1360 */           tb.popStackToClose(name);
/* 1361 */           tb.clearFormattingElementsToLastMarker();
/* 1362 */           tb.transition(InRow); }
/* 1363 */         else { if (StringUtil.inSorted(name, Constants.InCellBody)) {
/* 1364 */             tb.error(this);
/* 1365 */             return false;
/* 1366 */           }  if (StringUtil.inSorted(name, Constants.InCellTable)) {
/* 1367 */             if (!tb.inTableScope(name)) {
/* 1368 */               tb.error(this);
/* 1369 */               return false;
/*      */             } 
/* 1371 */             closeCell(tb);
/* 1372 */             return tb.process(t);
/*      */           } 
/* 1374 */           return anythingElse(t, tb); }
/*      */          }
/* 1376 */       else { if (t.isStartTag() && 
/* 1377 */           StringUtil.inSorted(t.asStartTag().normalName(), Constants.InCellCol)) {
/* 1378 */           if (!tb.inTableScope("td") && !tb.inTableScope("th")) {
/* 1379 */             tb.error(this);
/* 1380 */             return false;
/*      */           } 
/* 1382 */           closeCell(tb);
/* 1383 */           return tb.process(t);
/*      */         } 
/* 1385 */         return anythingElse(t, tb); }
/*      */       
/* 1387 */       return true;
/*      */     }
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/* 1391 */       return tb.process(t, InBody);
/*      */     }
/*      */     
/*      */     private void closeCell(HtmlTreeBuilder tb) {
/* 1395 */       if (tb.inTableScope("td")) {
/* 1396 */         tb.processEndTag("td");
/*      */       } else {
/* 1398 */         tb.processEndTag("th");
/*      */       } 
/*      */     } },
/* 1401 */   InSelect
/*      */   {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/*      */       // Byte code:
/*      */       //   0: getstatic org/jsoup/parser/HtmlTreeBuilderState$25.$SwitchMap$org$jsoup$parser$Token$TokenType : [I
/*      */       //   3: aload_1
/*      */       //   4: getfield type : Lorg/jsoup/parser/Token$TokenType;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 643, 1 -> 81, 2 -> 92, 3 -> 99, 4 -> 319, 5 -> 48, 6 -> 626
/*      */       //   48: aload_1
/*      */       //   49: invokevirtual asCharacter : ()Lorg/jsoup/parser/Token$Character;
/*      */       //   52: astore_3
/*      */       //   53: aload_3
/*      */       //   54: invokevirtual getData : ()Ljava/lang/String;
/*      */       //   57: invokestatic access$400 : ()Ljava/lang/String;
/*      */       //   60: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   63: ifeq -> 73
/*      */       //   66: aload_2
/*      */       //   67: aload_0
/*      */       //   68: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   71: iconst_0
/*      */       //   72: ireturn
/*      */       //   73: aload_2
/*      */       //   74: aload_3
/*      */       //   75: invokevirtual insert : (Lorg/jsoup/parser/Token$Character;)V
/*      */       //   78: goto -> 650
/*      */       //   81: aload_2
/*      */       //   82: aload_1
/*      */       //   83: invokevirtual asComment : ()Lorg/jsoup/parser/Token$Comment;
/*      */       //   86: invokevirtual insert : (Lorg/jsoup/parser/Token$Comment;)V
/*      */       //   89: goto -> 650
/*      */       //   92: aload_2
/*      */       //   93: aload_0
/*      */       //   94: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   97: iconst_0
/*      */       //   98: ireturn
/*      */       //   99: aload_1
/*      */       //   100: invokevirtual asStartTag : ()Lorg/jsoup/parser/Token$StartTag;
/*      */       //   103: astore #4
/*      */       //   105: aload #4
/*      */       //   107: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   110: astore #5
/*      */       //   112: aload #5
/*      */       //   114: ldc 'html'
/*      */       //   116: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   119: ifeq -> 132
/*      */       //   122: aload_2
/*      */       //   123: aload #4
/*      */       //   125: getstatic org/jsoup/parser/HtmlTreeBuilderState$16.InBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   128: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   131: ireturn
/*      */       //   132: aload #5
/*      */       //   134: ldc 'option'
/*      */       //   136: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   139: ifeq -> 168
/*      */       //   142: aload_2
/*      */       //   143: ldc 'option'
/*      */       //   145: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   148: ifeq -> 158
/*      */       //   151: aload_2
/*      */       //   152: ldc 'option'
/*      */       //   154: invokevirtual processEndTag : (Ljava/lang/String;)Z
/*      */       //   157: pop
/*      */       //   158: aload_2
/*      */       //   159: aload #4
/*      */       //   161: invokevirtual insert : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   164: pop
/*      */       //   165: goto -> 650
/*      */       //   168: aload #5
/*      */       //   170: ldc 'optgroup'
/*      */       //   172: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   175: ifeq -> 220
/*      */       //   178: aload_2
/*      */       //   179: ldc 'option'
/*      */       //   181: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   184: ifeq -> 194
/*      */       //   187: aload_2
/*      */       //   188: ldc 'option'
/*      */       //   190: invokevirtual processEndTag : (Ljava/lang/String;)Z
/*      */       //   193: pop
/*      */       //   194: aload_2
/*      */       //   195: ldc 'optgroup'
/*      */       //   197: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   200: ifeq -> 210
/*      */       //   203: aload_2
/*      */       //   204: ldc 'optgroup'
/*      */       //   206: invokevirtual processEndTag : (Ljava/lang/String;)Z
/*      */       //   209: pop
/*      */       //   210: aload_2
/*      */       //   211: aload #4
/*      */       //   213: invokevirtual insert : (Lorg/jsoup/parser/Token$StartTag;)Lorg/jsoup/nodes/Element;
/*      */       //   216: pop
/*      */       //   217: goto -> 650
/*      */       //   220: aload #5
/*      */       //   222: ldc 'select'
/*      */       //   224: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   227: ifeq -> 242
/*      */       //   230: aload_2
/*      */       //   231: aload_0
/*      */       //   232: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   235: aload_2
/*      */       //   236: ldc 'select'
/*      */       //   238: invokevirtual processEndTag : (Ljava/lang/String;)Z
/*      */       //   241: ireturn
/*      */       //   242: aload #5
/*      */       //   244: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InSelectEnd : [Ljava/lang/String;
/*      */       //   247: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   250: ifeq -> 283
/*      */       //   253: aload_2
/*      */       //   254: aload_0
/*      */       //   255: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   258: aload_2
/*      */       //   259: ldc 'select'
/*      */       //   261: invokevirtual inSelectScope : (Ljava/lang/String;)Z
/*      */       //   264: ifne -> 269
/*      */       //   267: iconst_0
/*      */       //   268: ireturn
/*      */       //   269: aload_2
/*      */       //   270: ldc 'select'
/*      */       //   272: invokevirtual processEndTag : (Ljava/lang/String;)Z
/*      */       //   275: pop
/*      */       //   276: aload_2
/*      */       //   277: aload #4
/*      */       //   279: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   282: ireturn
/*      */       //   283: aload #5
/*      */       //   285: ldc 'script'
/*      */       //   287: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   290: ifne -> 303
/*      */       //   293: aload #5
/*      */       //   295: ldc 'template'
/*      */       //   297: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   300: ifeq -> 312
/*      */       //   303: aload_2
/*      */       //   304: aload_1
/*      */       //   305: getstatic org/jsoup/parser/HtmlTreeBuilderState$16.InHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   308: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   311: ireturn
/*      */       //   312: aload_0
/*      */       //   313: aload_1
/*      */       //   314: aload_2
/*      */       //   315: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   318: ireturn
/*      */       //   319: aload_1
/*      */       //   320: invokevirtual asEndTag : ()Lorg/jsoup/parser/Token$EndTag;
/*      */       //   323: astore #6
/*      */       //   325: aload #6
/*      */       //   327: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   330: astore #5
/*      */       //   332: aload #5
/*      */       //   334: astore #7
/*      */       //   336: iconst_m1
/*      */       //   337: istore #8
/*      */       //   339: aload #7
/*      */       //   341: invokevirtual hashCode : ()I
/*      */       //   344: lookupswitch default -> 449, -1321546630 -> 436, -1010136971 -> 404, -906021636 -> 420, -80773204 -> 388
/*      */       //   388: aload #7
/*      */       //   390: ldc 'optgroup'
/*      */       //   392: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   395: ifeq -> 449
/*      */       //   398: iconst_0
/*      */       //   399: istore #8
/*      */       //   401: goto -> 449
/*      */       //   404: aload #7
/*      */       //   406: ldc 'option'
/*      */       //   408: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   411: ifeq -> 449
/*      */       //   414: iconst_1
/*      */       //   415: istore #8
/*      */       //   417: goto -> 449
/*      */       //   420: aload #7
/*      */       //   422: ldc 'select'
/*      */       //   424: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   427: ifeq -> 449
/*      */       //   430: iconst_2
/*      */       //   431: istore #8
/*      */       //   433: goto -> 449
/*      */       //   436: aload #7
/*      */       //   438: ldc 'template'
/*      */       //   440: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   443: ifeq -> 449
/*      */       //   446: iconst_3
/*      */       //   447: istore #8
/*      */       //   449: iload #8
/*      */       //   451: tableswitch default -> 616, 0 -> 480, 1 -> 551, 2 -> 576, 3 -> 607
/*      */       //   480: aload_2
/*      */       //   481: ldc 'option'
/*      */       //   483: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   486: ifeq -> 526
/*      */       //   489: aload_2
/*      */       //   490: aload_2
/*      */       //   491: invokevirtual currentElement : ()Lorg/jsoup/nodes/Element;
/*      */       //   494: invokevirtual aboveOnStack : (Lorg/jsoup/nodes/Element;)Lorg/jsoup/nodes/Element;
/*      */       //   497: ifnull -> 526
/*      */       //   500: aload_2
/*      */       //   501: aload_2
/*      */       //   502: invokevirtual currentElement : ()Lorg/jsoup/nodes/Element;
/*      */       //   505: invokevirtual aboveOnStack : (Lorg/jsoup/nodes/Element;)Lorg/jsoup/nodes/Element;
/*      */       //   508: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   511: ldc 'optgroup'
/*      */       //   513: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   516: ifeq -> 526
/*      */       //   519: aload_2
/*      */       //   520: ldc 'option'
/*      */       //   522: invokevirtual processEndTag : (Ljava/lang/String;)Z
/*      */       //   525: pop
/*      */       //   526: aload_2
/*      */       //   527: ldc 'optgroup'
/*      */       //   529: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   532: ifeq -> 543
/*      */       //   535: aload_2
/*      */       //   536: invokevirtual pop : ()Lorg/jsoup/nodes/Element;
/*      */       //   539: pop
/*      */       //   540: goto -> 623
/*      */       //   543: aload_2
/*      */       //   544: aload_0
/*      */       //   545: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   548: goto -> 623
/*      */       //   551: aload_2
/*      */       //   552: ldc 'option'
/*      */       //   554: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   557: ifeq -> 568
/*      */       //   560: aload_2
/*      */       //   561: invokevirtual pop : ()Lorg/jsoup/nodes/Element;
/*      */       //   564: pop
/*      */       //   565: goto -> 623
/*      */       //   568: aload_2
/*      */       //   569: aload_0
/*      */       //   570: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   573: goto -> 623
/*      */       //   576: aload_2
/*      */       //   577: aload #5
/*      */       //   579: invokevirtual inSelectScope : (Ljava/lang/String;)Z
/*      */       //   582: ifne -> 592
/*      */       //   585: aload_2
/*      */       //   586: aload_0
/*      */       //   587: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   590: iconst_0
/*      */       //   591: ireturn
/*      */       //   592: aload_2
/*      */       //   593: aload #5
/*      */       //   595: invokevirtual popStackToClose : (Ljava/lang/String;)Lorg/jsoup/nodes/Element;
/*      */       //   598: pop
/*      */       //   599: aload_2
/*      */       //   600: invokevirtual resetInsertionMode : ()Z
/*      */       //   603: pop
/*      */       //   604: goto -> 623
/*      */       //   607: aload_2
/*      */       //   608: aload_1
/*      */       //   609: getstatic org/jsoup/parser/HtmlTreeBuilderState$16.InHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   612: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   615: ireturn
/*      */       //   616: aload_0
/*      */       //   617: aload_1
/*      */       //   618: aload_2
/*      */       //   619: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   622: ireturn
/*      */       //   623: goto -> 650
/*      */       //   626: aload_2
/*      */       //   627: ldc 'html'
/*      */       //   629: invokevirtual currentElementIs : (Ljava/lang/String;)Z
/*      */       //   632: ifne -> 650
/*      */       //   635: aload_2
/*      */       //   636: aload_0
/*      */       //   637: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   640: goto -> 650
/*      */       //   643: aload_0
/*      */       //   644: aload_1
/*      */       //   645: aload_2
/*      */       //   646: invokespecial anythingElse : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilder;)Z
/*      */       //   649: ireturn
/*      */       //   650: iconst_1
/*      */       //   651: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1403	-> 0
/*      */       //   #1405	-> 48
/*      */       //   #1406	-> 53
/*      */       //   #1407	-> 66
/*      */       //   #1408	-> 71
/*      */       //   #1410	-> 73
/*      */       //   #1412	-> 78
/*      */       //   #1414	-> 81
/*      */       //   #1415	-> 89
/*      */       //   #1417	-> 92
/*      */       //   #1418	-> 97
/*      */       //   #1420	-> 99
/*      */       //   #1421	-> 105
/*      */       //   #1422	-> 112
/*      */       //   #1423	-> 122
/*      */       //   #1424	-> 132
/*      */       //   #1425	-> 142
/*      */       //   #1426	-> 151
/*      */       //   #1427	-> 158
/*      */       //   #1428	-> 168
/*      */       //   #1429	-> 178
/*      */       //   #1430	-> 187
/*      */       //   #1431	-> 194
/*      */       //   #1432	-> 203
/*      */       //   #1433	-> 210
/*      */       //   #1434	-> 220
/*      */       //   #1435	-> 230
/*      */       //   #1436	-> 235
/*      */       //   #1437	-> 242
/*      */       //   #1438	-> 253
/*      */       //   #1439	-> 258
/*      */       //   #1440	-> 267
/*      */       //   #1441	-> 269
/*      */       //   #1442	-> 276
/*      */       //   #1443	-> 283
/*      */       //   #1444	-> 303
/*      */       //   #1446	-> 312
/*      */       //   #1450	-> 319
/*      */       //   #1451	-> 325
/*      */       //   #1452	-> 332
/*      */       //   #1454	-> 480
/*      */       //   #1455	-> 519
/*      */       //   #1456	-> 526
/*      */       //   #1457	-> 535
/*      */       //   #1459	-> 543
/*      */       //   #1460	-> 548
/*      */       //   #1462	-> 551
/*      */       //   #1463	-> 560
/*      */       //   #1465	-> 568
/*      */       //   #1466	-> 573
/*      */       //   #1468	-> 576
/*      */       //   #1469	-> 585
/*      */       //   #1470	-> 590
/*      */       //   #1472	-> 592
/*      */       //   #1473	-> 599
/*      */       //   #1475	-> 604
/*      */       //   #1477	-> 607
/*      */       //   #1479	-> 616
/*      */       //   #1481	-> 623
/*      */       //   #1483	-> 626
/*      */       //   #1484	-> 635
/*      */       //   #1487	-> 643
/*      */       //   #1489	-> 650
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   53	28	3	c	Lorg/jsoup/parser/Token$Character;
/*      */       //   105	214	4	start	Lorg/jsoup/parser/Token$StartTag;
/*      */       //   112	207	5	name	Ljava/lang/String;
/*      */       //   332	294	5	name	Ljava/lang/String;
/*      */       //   325	301	6	end	Lorg/jsoup/parser/Token$EndTag;
/*      */       //   0	652	0	this	Lorg/jsoup/parser/HtmlTreeBuilderState$16;
/*      */       //   0	652	1	t	Lorg/jsoup/parser/Token;
/*      */       //   0	652	2	tb	Lorg/jsoup/parser/HtmlTreeBuilder;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean anythingElse(Token t, HtmlTreeBuilder tb) {
/* 1493 */       tb.error(this);
/* 1494 */       return false;
/*      */     }
/*      */   },
/* 1497 */   InSelectInTable {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1499 */       if (t.isStartTag() && StringUtil.inSorted(t.asStartTag().normalName(), Constants.InSelectTableEnd)) {
/* 1500 */         tb.error(this);
/* 1501 */         tb.popStackToClose("select");
/* 1502 */         tb.resetInsertionMode();
/* 1503 */         return tb.process(t);
/* 1504 */       }  if (t.isEndTag() && StringUtil.inSorted(t.asEndTag().normalName(), Constants.InSelectTableEnd)) {
/* 1505 */         tb.error(this);
/* 1506 */         if (tb.inTableScope(t.asEndTag().normalName())) {
/* 1507 */           tb.popStackToClose("select");
/* 1508 */           tb.resetInsertionMode();
/* 1509 */           return tb.process(t);
/*      */         } 
/* 1511 */         return false;
/*      */       } 
/* 1513 */       return tb.process(t, InSelect);
/*      */     }
/*      */   },
/*      */   
/* 1517 */   InTemplate
/*      */   {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean process(Token t, HtmlTreeBuilder tb)
/*      */     {
/*      */       // Byte code:
/*      */       //   0: getstatic org/jsoup/parser/HtmlTreeBuilderState$25.$SwitchMap$org$jsoup$parser$Token$TokenType : [I
/*      */       //   3: aload_1
/*      */       //   4: getfield type : Lorg/jsoup/parser/Token$TokenType;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 361, 1 -> 48, 2 -> 48, 3 -> 60, 4 -> 261, 5 -> 48, 6 -> 297
/*      */       //   48: aload_2
/*      */       //   49: aload_1
/*      */       //   50: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   53: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   56: pop
/*      */       //   57: goto -> 361
/*      */       //   60: aload_1
/*      */       //   61: invokevirtual asStartTag : ()Lorg/jsoup/parser/Token$StartTag;
/*      */       //   64: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   67: astore_3
/*      */       //   68: aload_3
/*      */       //   69: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InTemplateToHead : [Ljava/lang/String;
/*      */       //   72: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   75: ifeq -> 90
/*      */       //   78: aload_2
/*      */       //   79: aload_1
/*      */       //   80: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   83: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   86: pop
/*      */       //   87: goto -> 361
/*      */       //   90: aload_3
/*      */       //   91: getstatic org/jsoup/parser/HtmlTreeBuilderState$Constants.InTemplateToTable : [Ljava/lang/String;
/*      */       //   94: invokestatic inSorted : (Ljava/lang/String;[Ljava/lang/String;)Z
/*      */       //   97: ifeq -> 125
/*      */       //   100: aload_2
/*      */       //   101: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   104: pop
/*      */       //   105: aload_2
/*      */       //   106: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InTable : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   109: invokevirtual pushTemplateMode : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   112: aload_2
/*      */       //   113: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InTable : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   116: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   119: aload_2
/*      */       //   120: aload_1
/*      */       //   121: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   124: ireturn
/*      */       //   125: aload_3
/*      */       //   126: ldc 'col'
/*      */       //   128: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   131: ifeq -> 159
/*      */       //   134: aload_2
/*      */       //   135: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   138: pop
/*      */       //   139: aload_2
/*      */       //   140: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InColumnGroup : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   143: invokevirtual pushTemplateMode : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   146: aload_2
/*      */       //   147: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InColumnGroup : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   150: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   153: aload_2
/*      */       //   154: aload_1
/*      */       //   155: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   158: ireturn
/*      */       //   159: aload_3
/*      */       //   160: ldc 'tr'
/*      */       //   162: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   165: ifeq -> 193
/*      */       //   168: aload_2
/*      */       //   169: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   172: pop
/*      */       //   173: aload_2
/*      */       //   174: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InTableBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   177: invokevirtual pushTemplateMode : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   180: aload_2
/*      */       //   181: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InTableBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   184: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   187: aload_2
/*      */       //   188: aload_1
/*      */       //   189: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   192: ireturn
/*      */       //   193: aload_3
/*      */       //   194: ldc 'td'
/*      */       //   196: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   199: ifne -> 211
/*      */       //   202: aload_3
/*      */       //   203: ldc 'th'
/*      */       //   205: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   208: ifeq -> 236
/*      */       //   211: aload_2
/*      */       //   212: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   215: pop
/*      */       //   216: aload_2
/*      */       //   217: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InRow : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   220: invokevirtual pushTemplateMode : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   223: aload_2
/*      */       //   224: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InRow : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   227: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   230: aload_2
/*      */       //   231: aload_1
/*      */       //   232: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   235: ireturn
/*      */       //   236: aload_2
/*      */       //   237: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   240: pop
/*      */       //   241: aload_2
/*      */       //   242: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   245: invokevirtual pushTemplateMode : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   248: aload_2
/*      */       //   249: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InBody : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   252: invokevirtual transition : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   255: aload_2
/*      */       //   256: aload_1
/*      */       //   257: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   260: ireturn
/*      */       //   261: aload_1
/*      */       //   262: invokevirtual asEndTag : ()Lorg/jsoup/parser/Token$EndTag;
/*      */       //   265: invokevirtual normalName : ()Ljava/lang/String;
/*      */       //   268: astore_3
/*      */       //   269: aload_3
/*      */       //   270: ldc 'template'
/*      */       //   272: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   275: ifeq -> 290
/*      */       //   278: aload_2
/*      */       //   279: aload_1
/*      */       //   280: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InHead : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   283: invokevirtual process : (Lorg/jsoup/parser/Token;Lorg/jsoup/parser/HtmlTreeBuilderState;)Z
/*      */       //   286: pop
/*      */       //   287: goto -> 361
/*      */       //   290: aload_2
/*      */       //   291: aload_0
/*      */       //   292: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   295: iconst_0
/*      */       //   296: ireturn
/*      */       //   297: aload_2
/*      */       //   298: ldc 'template'
/*      */       //   300: invokevirtual onStack : (Ljava/lang/String;)Z
/*      */       //   303: ifne -> 308
/*      */       //   306: iconst_1
/*      */       //   307: ireturn
/*      */       //   308: aload_2
/*      */       //   309: aload_0
/*      */       //   310: invokevirtual error : (Lorg/jsoup/parser/HtmlTreeBuilderState;)V
/*      */       //   313: aload_2
/*      */       //   314: ldc 'template'
/*      */       //   316: invokevirtual popStackToClose : (Ljava/lang/String;)Lorg/jsoup/nodes/Element;
/*      */       //   319: pop
/*      */       //   320: aload_2
/*      */       //   321: invokevirtual clearFormattingElementsToLastMarker : ()V
/*      */       //   324: aload_2
/*      */       //   325: invokevirtual popTemplateMode : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   328: pop
/*      */       //   329: aload_2
/*      */       //   330: invokevirtual resetInsertionMode : ()Z
/*      */       //   333: pop
/*      */       //   334: aload_2
/*      */       //   335: invokevirtual state : ()Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   338: getstatic org/jsoup/parser/HtmlTreeBuilderState$18.InTemplate : Lorg/jsoup/parser/HtmlTreeBuilderState;
/*      */       //   341: if_acmpeq -> 359
/*      */       //   344: aload_2
/*      */       //   345: invokevirtual templateModeSize : ()I
/*      */       //   348: bipush #12
/*      */       //   350: if_icmpge -> 359
/*      */       //   353: aload_2
/*      */       //   354: aload_1
/*      */       //   355: invokevirtual process : (Lorg/jsoup/parser/Token;)Z
/*      */       //   358: ireturn
/*      */       //   359: iconst_1
/*      */       //   360: ireturn
/*      */       //   361: iconst_1
/*      */       //   362: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1520	-> 0
/*      */       //   #1524	-> 48
/*      */       //   #1525	-> 57
/*      */       //   #1527	-> 60
/*      */       //   #1528	-> 68
/*      */       //   #1529	-> 78
/*      */       //   #1530	-> 90
/*      */       //   #1531	-> 100
/*      */       //   #1532	-> 105
/*      */       //   #1533	-> 112
/*      */       //   #1534	-> 119
/*      */       //   #1536	-> 125
/*      */       //   #1537	-> 134
/*      */       //   #1538	-> 139
/*      */       //   #1539	-> 146
/*      */       //   #1540	-> 153
/*      */       //   #1541	-> 159
/*      */       //   #1542	-> 168
/*      */       //   #1543	-> 173
/*      */       //   #1544	-> 180
/*      */       //   #1545	-> 187
/*      */       //   #1546	-> 193
/*      */       //   #1547	-> 211
/*      */       //   #1548	-> 216
/*      */       //   #1549	-> 223
/*      */       //   #1550	-> 230
/*      */       //   #1552	-> 236
/*      */       //   #1553	-> 241
/*      */       //   #1554	-> 248
/*      */       //   #1555	-> 255
/*      */       //   #1560	-> 261
/*      */       //   #1561	-> 269
/*      */       //   #1562	-> 278
/*      */       //   #1564	-> 290
/*      */       //   #1565	-> 295
/*      */       //   #1569	-> 297
/*      */       //   #1570	-> 306
/*      */       //   #1572	-> 308
/*      */       //   #1573	-> 313
/*      */       //   #1574	-> 320
/*      */       //   #1575	-> 324
/*      */       //   #1576	-> 329
/*      */       //   #1579	-> 334
/*      */       //   #1580	-> 353
/*      */       //   #1581	-> 359
/*      */       //   #1583	-> 361
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   68	193	3	name	Ljava/lang/String;
/*      */       //   269	28	3	name	Ljava/lang/String;
/*      */       //   0	363	0	this	Lorg/jsoup/parser/HtmlTreeBuilderState$18;
/*      */       //   0	363	1	t	Lorg/jsoup/parser/Token;
/*      */       //   0	363	2	tb	Lorg/jsoup/parser/HtmlTreeBuilder;
/*      */     }
/*      */   },
/* 1586 */   AfterBody {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1588 */       if (isWhitespace(t))
/* 1589 */       { tb.insert(t.asCharacter()); }
/* 1590 */       else if (t.isComment())
/* 1591 */       { tb.insert(t.asComment()); }
/* 1592 */       else { if (t.isDoctype()) {
/* 1593 */           tb.error(this);
/* 1594 */           return false;
/* 1595 */         }  if (t.isStartTag() && t.asStartTag().normalName().equals("html"))
/* 1596 */           return tb.process(t, InBody); 
/* 1597 */         if (t.isEndTag() && t.asEndTag().normalName().equals("html")) {
/* 1598 */           if (tb.isFragmentParsing()) {
/* 1599 */             tb.error(this);
/* 1600 */             return false;
/*      */           } 
/* 1602 */           if (tb.onStack("html")) tb.popStackToClose("html"); 
/* 1603 */           tb.transition(AfterAfterBody);
/*      */         }
/* 1605 */         else if (!t.isEOF()) {
/*      */ 
/*      */           
/* 1608 */           tb.error(this);
/* 1609 */           tb.resetBody();
/* 1610 */           return tb.process(t);
/*      */         }  }
/* 1612 */        return true;
/*      */     }
/*      */   },
/* 1615 */   InFrameset {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1617 */       if (isWhitespace(t))
/* 1618 */       { tb.insert(t.asCharacter()); }
/* 1619 */       else if (t.isComment())
/* 1620 */       { tb.insert(t.asComment()); }
/* 1621 */       else { if (t.isDoctype()) {
/* 1622 */           tb.error(this);
/* 1623 */           return false;
/* 1624 */         }  if (t.isStartTag())
/* 1625 */         { Token.StartTag start = t.asStartTag();
/* 1626 */           switch (start.normalName())
/*      */           { case "html":
/* 1628 */               return tb.process(start, InBody);
/*      */             case "frameset":
/* 1630 */               tb.insert(start);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1660 */               return true;case "frame": tb.insertEmpty(start); return true;case "noframes": return tb.process(start, InHead); }  tb.error(this); return false; }  if (t.isEndTag() && t.asEndTag().normalName().equals("frameset")) { if (tb.currentElementIs("html")) { tb.error(this); return false; }  tb.pop(); if (!tb.isFragmentParsing() && !tb.currentElementIs("frameset")) tb.transition(AfterFrameset);  } else if (t.isEOF()) { if (!tb.currentElementIs("html")) { tb.error(this); return true; }  } else { tb.error(this); return false; }  }  return true;
/*      */     }
/*      */   },
/* 1663 */   AfterFrameset {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1665 */       if (isWhitespace(t))
/* 1666 */       { tb.insert(t.asCharacter()); }
/* 1667 */       else if (t.isComment())
/* 1668 */       { tb.insert(t.asComment()); }
/* 1669 */       else { if (t.isDoctype()) {
/* 1670 */           tb.error(this);
/* 1671 */           return false;
/* 1672 */         }  if (t.isStartTag() && t.asStartTag().normalName().equals("html"))
/* 1673 */           return tb.process(t, InBody); 
/* 1674 */         if (t.isEndTag() && t.asEndTag().normalName().equals("html"))
/* 1675 */         { tb.transition(AfterAfterFrameset); }
/* 1676 */         else { if (t.isStartTag() && t.asStartTag().normalName().equals("noframes"))
/* 1677 */             return tb.process(t, InHead); 
/* 1678 */           if (!t.isEOF())
/*      */           
/*      */           { 
/* 1681 */             tb.error(this);
/* 1682 */             return false; }  }
/*      */          }
/* 1684 */        return true;
/*      */     }
/*      */   },
/* 1687 */   AfterAfterBody {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1689 */       if (t.isComment())
/* 1690 */       { tb.insert(t.asComment()); }
/* 1691 */       else { if (t.isDoctype() || (t.isStartTag() && t.asStartTag().normalName().equals("html")))
/* 1692 */           return tb.process(t, InBody); 
/* 1693 */         if (isWhitespace(t)) {
/* 1694 */           tb.insert(t.asCharacter());
/* 1695 */         } else if (!t.isEOF()) {
/*      */ 
/*      */           
/* 1698 */           tb.error(this);
/* 1699 */           tb.resetBody();
/* 1700 */           return tb.process(t);
/*      */         }  }
/* 1702 */        return true;
/*      */     }
/*      */   },
/* 1705 */   AfterAfterFrameset {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1707 */       if (t.isComment())
/* 1708 */       { tb.insert(t.asComment()); }
/* 1709 */       else { if (t.isDoctype() || isWhitespace(t) || (t.isStartTag() && t.asStartTag().normalName().equals("html")))
/* 1710 */           return tb.process(t, InBody); 
/* 1711 */         if (!t.isEOF()) {
/*      */           
/* 1713 */           if (t.isStartTag() && t.asStartTag().normalName().equals("noframes")) {
/* 1714 */             return tb.process(t, InHead);
/*      */           }
/* 1716 */           tb.error(this);
/* 1717 */           return false;
/*      */         }  }
/* 1719 */        return true;
/*      */     }
/*      */   },
/* 1722 */   ForeignContent {
/*      */     boolean process(Token t, HtmlTreeBuilder tb) {
/* 1724 */       return true;
/*      */     } };
/*      */   private static final String nullString;
/*      */   
/*      */   static {
/* 1729 */     nullString = String.valueOf(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isWhitespace(Token t) {
/* 1734 */     if (t.isCharacter()) {
/* 1735 */       String data = t.asCharacter().getData();
/* 1736 */       return StringUtil.isBlank(data);
/*      */     } 
/* 1738 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean isWhitespace(String data) {
/* 1742 */     return StringUtil.isBlank(data);
/*      */   }
/*      */   
/*      */   private static void handleRcData(Token.StartTag startTag, HtmlTreeBuilder tb) {
/* 1746 */     tb.tokeniser.transition(TokeniserState.Rcdata);
/* 1747 */     tb.markInsertionMode();
/* 1748 */     tb.transition(Text);
/* 1749 */     tb.insert(startTag);
/*      */   }
/*      */   
/*      */   private static void handleRawtext(Token.StartTag startTag, HtmlTreeBuilder tb) {
/* 1753 */     tb.tokeniser.transition(TokeniserState.Rawtext);
/* 1754 */     tb.markInsertionMode();
/* 1755 */     tb.transition(Text);
/* 1756 */     tb.insert(startTag);
/*      */   }
/*      */   
/*      */   abstract boolean process(Token paramToken, HtmlTreeBuilder paramHtmlTreeBuilder);
/*      */   
/* 1761 */   static final class Constants { static final String[] InHeadEmpty = new String[] { "base", "basefont", "bgsound", "command", "link" };
/* 1762 */     static final String[] InHeadRaw = new String[] { "noframes", "style" };
/* 1763 */     static final String[] InHeadEnd = new String[] { "body", "br", "html" };
/* 1764 */     static final String[] AfterHeadBody = new String[] { "body", "br", "html" };
/* 1765 */     static final String[] BeforeHtmlToHead = new String[] { "body", "br", "head", "html" };
/* 1766 */     static final String[] InHeadNoScriptHead = new String[] { "basefont", "bgsound", "link", "meta", "noframes", "style" };
/* 1767 */     static final String[] InBodyStartToHead = new String[] { "base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", "style", "template", "title" };
/* 1768 */     static final String[] InBodyStartPClosers = new String[] { "address", "article", "aside", "blockquote", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol", "p", "section", "summary", "ul" };
/*      */ 
/*      */     
/* 1771 */     static final String[] Headings = new String[] { "h1", "h2", "h3", "h4", "h5", "h6" };
/* 1772 */     static final String[] InBodyStartLiBreakers = new String[] { "address", "div", "p" };
/* 1773 */     static final String[] DdDt = new String[] { "dd", "dt" };
/* 1774 */     static final String[] InBodyStartApplets = new String[] { "applet", "marquee", "object" };
/* 1775 */     static final String[] InBodyStartMedia = new String[] { "param", "source", "track" };
/* 1776 */     static final String[] InBodyStartInputAttribs = new String[] { "action", "name", "prompt" };
/* 1777 */     static final String[] InBodyStartDrop = new String[] { "caption", "col", "colgroup", "frame", "head", "tbody", "td", "tfoot", "th", "thead", "tr" };
/* 1778 */     static final String[] InBodyEndClosers = new String[] { "address", "article", "aside", "blockquote", "button", "center", "details", "dir", "div", "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu", "nav", "ol", "pre", "section", "summary", "ul" };
/*      */ 
/*      */     
/* 1781 */     static final String[] InBodyEndAdoptionFormatters = new String[] { "a", "b", "big", "code", "em", "font", "i", "nobr", "s", "small", "strike", "strong", "tt", "u" };
/* 1782 */     static final String[] InBodyEndTableFosters = new String[] { "table", "tbody", "tfoot", "thead", "tr" };
/* 1783 */     static final String[] InTableToBody = new String[] { "tbody", "tfoot", "thead" };
/* 1784 */     static final String[] InTableAddBody = new String[] { "td", "th", "tr" };
/* 1785 */     static final String[] InTableToHead = new String[] { "script", "style", "template" };
/* 1786 */     static final String[] InCellNames = new String[] { "td", "th" };
/* 1787 */     static final String[] InCellBody = new String[] { "body", "caption", "col", "colgroup", "html" };
/* 1788 */     static final String[] InCellTable = new String[] { "table", "tbody", "tfoot", "thead", "tr" };
/* 1789 */     static final String[] InCellCol = new String[] { "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr" };
/* 1790 */     static final String[] InTableEndErr = new String[] { "body", "caption", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr" };
/* 1791 */     static final String[] InTableFoster = new String[] { "table", "tbody", "tfoot", "thead", "tr" };
/* 1792 */     static final String[] InTableBodyExit = new String[] { "caption", "col", "colgroup", "tbody", "tfoot", "thead" };
/* 1793 */     static final String[] InTableBodyEndIgnore = new String[] { "body", "caption", "col", "colgroup", "html", "td", "th", "tr" };
/* 1794 */     static final String[] InRowMissing = new String[] { "caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr" };
/* 1795 */     static final String[] InRowIgnore = new String[] { "body", "caption", "col", "colgroup", "html", "td", "th" };
/* 1796 */     static final String[] InSelectEnd = new String[] { "input", "keygen", "textarea" };
/* 1797 */     static final String[] InSelectTableEnd = new String[] { "caption", "table", "tbody", "td", "tfoot", "th", "thead", "tr" };
/* 1798 */     static final String[] InTableEndIgnore = new String[] { "tbody", "tfoot", "thead" };
/* 1799 */     static final String[] InHeadNoscriptIgnore = new String[] { "head", "noscript" };
/* 1800 */     static final String[] InCaptionIgnore = new String[] { "body", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr" };
/* 1801 */     static final String[] InTemplateToHead = new String[] { "base", "basefont", "bgsound", "link", "meta", "noframes", "script", "style", "template", "title" };
/* 1802 */     static final String[] InTemplateToTable = new String[] { "caption", "colgroup", "tbody", "tfoot", "thead" }; }
/*      */ 
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\HtmlTreeBuilderState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */