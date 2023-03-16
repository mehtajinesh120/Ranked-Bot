/*     */ package org.jsoup.select;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.parser.TokenQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryParser
/*     */ {
/*  18 */   private static final char[] combinators = new char[] { ',', '>', '+', '~', ' ' };
/*  19 */   private static final String[] AttributeEvals = new String[] { "=", "!=", "^=", "$=", "*=", "~=" };
/*     */   
/*     */   private final TokenQueue tq;
/*     */   private final String query;
/*  23 */   private final List<Evaluator> evals = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private QueryParser(String query) {
/*  30 */     Validate.notEmpty(query);
/*  31 */     query = query.trim();
/*  32 */     this.query = query;
/*  33 */     this.tq = new TokenQueue(query);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Evaluator parse(String query) {
/*     */     try {
/*  44 */       QueryParser p = new QueryParser(query);
/*  45 */       return p.parse();
/*  46 */     } catch (IllegalArgumentException e) {
/*  47 */       throw new Selector.SelectorParseException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Evaluator parse() {
/*  56 */     this.tq.consumeWhitespace();
/*     */     
/*  58 */     if (this.tq.matchesAny(combinators)) {
/*  59 */       this.evals.add(new StructuralEvaluator.Root());
/*  60 */       combinator(this.tq.consume());
/*     */     } else {
/*  62 */       findElements();
/*     */     } 
/*     */     
/*  65 */     while (!this.tq.isEmpty()) {
/*     */       
/*  67 */       boolean seenWhite = this.tq.consumeWhitespace();
/*     */       
/*  69 */       if (this.tq.matchesAny(combinators)) {
/*  70 */         combinator(this.tq.consume()); continue;
/*  71 */       }  if (seenWhite) {
/*  72 */         combinator(' '); continue;
/*     */       } 
/*  74 */       findElements();
/*     */     } 
/*     */ 
/*     */     
/*  78 */     if (this.evals.size() == 1) {
/*  79 */       return this.evals.get(0);
/*     */     }
/*  81 */     return new CombiningEvaluator.And(this.evals);
/*     */   } private void combinator(char combinator) {
/*     */     Evaluator rootEval, currentEval;
/*     */     CombiningEvaluator.Or or;
/*  85 */     this.tq.consumeWhitespace();
/*  86 */     String subQuery = consumeSubQuery();
/*     */ 
/*     */ 
/*     */     
/*  90 */     Evaluator newEval = parse(subQuery);
/*  91 */     boolean replaceRightMost = false;
/*     */     
/*  93 */     if (this.evals.size() == 1) {
/*  94 */       rootEval = currentEval = this.evals.get(0);
/*     */       
/*  96 */       if (rootEval instanceof CombiningEvaluator.Or && combinator != ',') {
/*  97 */         currentEval = ((CombiningEvaluator.Or)currentEval).rightMostEvaluator();
/*  98 */         assert currentEval != null;
/*  99 */         replaceRightMost = true;
/*     */       } 
/*     */     } else {
/*     */       
/* 103 */       rootEval = currentEval = new CombiningEvaluator.And(this.evals);
/*     */     } 
/* 105 */     this.evals.clear();
/*     */ 
/*     */     
/* 108 */     switch (combinator) {
/*     */       case '>':
/* 110 */         currentEval = new CombiningEvaluator.And(new Evaluator[] { new StructuralEvaluator.ImmediateParent(currentEval), newEval });
/*     */         break;
/*     */       case ' ':
/* 113 */         currentEval = new CombiningEvaluator.And(new Evaluator[] { new StructuralEvaluator.Parent(currentEval), newEval });
/*     */         break;
/*     */       case '+':
/* 116 */         currentEval = new CombiningEvaluator.And(new Evaluator[] { new StructuralEvaluator.ImmediatePreviousSibling(currentEval), newEval });
/*     */         break;
/*     */       case '~':
/* 119 */         currentEval = new CombiningEvaluator.And(new Evaluator[] { new StructuralEvaluator.PreviousSibling(currentEval), newEval });
/*     */         break;
/*     */       
/*     */       case ',':
/* 123 */         if (currentEval instanceof CombiningEvaluator.Or) {
/* 124 */           or = (CombiningEvaluator.Or)currentEval;
/*     */         } else {
/* 126 */           or = new CombiningEvaluator.Or();
/* 127 */           or.add(currentEval);
/*     */         } 
/* 129 */         or.add(newEval);
/* 130 */         currentEval = or;
/*     */         break;
/*     */       default:
/* 133 */         throw new Selector.SelectorParseException("Unknown combinator '%s'", new Object[] { Character.valueOf(combinator) });
/*     */     } 
/*     */     
/* 136 */     if (replaceRightMost)
/* 137 */     { ((CombiningEvaluator.Or)rootEval).replaceRightMostEvaluator(currentEval); }
/* 138 */     else { rootEval = currentEval; }
/* 139 */      this.evals.add(rootEval);
/*     */   }
/*     */   
/*     */   private String consumeSubQuery() {
/* 143 */     StringBuilder sq = StringUtil.borrowBuilder();
/* 144 */     while (!this.tq.isEmpty()) {
/* 145 */       if (this.tq.matches("(")) {
/* 146 */         sq.append("(").append(this.tq.chompBalanced('(', ')')).append(")"); continue;
/* 147 */       }  if (this.tq.matches("[")) {
/* 148 */         sq.append("[").append(this.tq.chompBalanced('[', ']')).append("]"); continue;
/* 149 */       }  if (this.tq.matchesAny(combinators)) {
/* 150 */         if (sq.length() > 0) {
/*     */           break;
/*     */         }
/* 153 */         this.tq.consume(); continue;
/*     */       } 
/* 155 */       sq.append(this.tq.consume());
/*     */     } 
/* 157 */     return StringUtil.releaseBuilder(sq);
/*     */   }
/*     */   
/*     */   private void findElements() {
/* 161 */     if (this.tq.matchChomp("#")) {
/* 162 */       byId();
/* 163 */     } else if (this.tq.matchChomp(".")) {
/* 164 */       byClass();
/* 165 */     } else if (this.tq.matchesWord() || this.tq.matches("*|")) {
/* 166 */       byTag();
/* 167 */     } else if (this.tq.matches("[")) {
/* 168 */       byAttribute();
/* 169 */     } else if (this.tq.matchChomp("*")) {
/* 170 */       allElements();
/* 171 */     } else if (this.tq.matchChomp(":lt(")) {
/* 172 */       indexLessThan();
/* 173 */     } else if (this.tq.matchChomp(":gt(")) {
/* 174 */       indexGreaterThan();
/* 175 */     } else if (this.tq.matchChomp(":eq(")) {
/* 176 */       indexEquals();
/* 177 */     } else if (this.tq.matches(":has(")) {
/* 178 */       has();
/* 179 */     } else if (this.tq.matches(":contains(")) {
/* 180 */       contains(false);
/* 181 */     } else if (this.tq.matches(":containsOwn(")) {
/* 182 */       contains(true);
/* 183 */     } else if (this.tq.matches(":containsWholeText(")) {
/* 184 */       containsWholeText(false);
/* 185 */     } else if (this.tq.matches(":containsWholeOwnText(")) {
/* 186 */       containsWholeText(true);
/* 187 */     } else if (this.tq.matches(":containsData(")) {
/* 188 */       containsData();
/* 189 */     } else if (this.tq.matches(":matches(")) {
/* 190 */       matches(false);
/* 191 */     } else if (this.tq.matches(":matchesOwn(")) {
/* 192 */       matches(true);
/* 193 */     } else if (this.tq.matches(":matchesWholeText(")) {
/* 194 */       matchesWholeText(false);
/* 195 */     } else if (this.tq.matches(":matchesWholeOwnText(")) {
/* 196 */       matchesWholeText(true);
/* 197 */     } else if (this.tq.matches(":not(")) {
/* 198 */       not();
/* 199 */     } else if (this.tq.matchChomp(":nth-child(")) {
/* 200 */       cssNthChild(false, false);
/* 201 */     } else if (this.tq.matchChomp(":nth-last-child(")) {
/* 202 */       cssNthChild(true, false);
/* 203 */     } else if (this.tq.matchChomp(":nth-of-type(")) {
/* 204 */       cssNthChild(false, true);
/* 205 */     } else if (this.tq.matchChomp(":nth-last-of-type(")) {
/* 206 */       cssNthChild(true, true);
/* 207 */     } else if (this.tq.matchChomp(":first-child")) {
/* 208 */       this.evals.add(new Evaluator.IsFirstChild());
/* 209 */     } else if (this.tq.matchChomp(":last-child")) {
/* 210 */       this.evals.add(new Evaluator.IsLastChild());
/* 211 */     } else if (this.tq.matchChomp(":first-of-type")) {
/* 212 */       this.evals.add(new Evaluator.IsFirstOfType());
/* 213 */     } else if (this.tq.matchChomp(":last-of-type")) {
/* 214 */       this.evals.add(new Evaluator.IsLastOfType());
/* 215 */     } else if (this.tq.matchChomp(":only-child")) {
/* 216 */       this.evals.add(new Evaluator.IsOnlyChild());
/* 217 */     } else if (this.tq.matchChomp(":only-of-type")) {
/* 218 */       this.evals.add(new Evaluator.IsOnlyOfType());
/* 219 */     } else if (this.tq.matchChomp(":empty")) {
/* 220 */       this.evals.add(new Evaluator.IsEmpty());
/* 221 */     } else if (this.tq.matchChomp(":root")) {
/* 222 */       this.evals.add(new Evaluator.IsRoot());
/* 223 */     } else if (this.tq.matchChomp(":matchText")) {
/* 224 */       this.evals.add(new Evaluator.MatchText());
/*     */     } else {
/* 226 */       throw new Selector.SelectorParseException("Could not parse query '%s': unexpected token at '%s'", new Object[] { this.query, this.tq.remainder() });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void byId() {
/* 231 */     String id = this.tq.consumeCssIdentifier();
/* 232 */     Validate.notEmpty(id);
/* 233 */     this.evals.add(new Evaluator.Id(id));
/*     */   }
/*     */   
/*     */   private void byClass() {
/* 237 */     String className = this.tq.consumeCssIdentifier();
/* 238 */     Validate.notEmpty(className);
/* 239 */     this.evals.add(new Evaluator.Class(className.trim()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void byTag() {
/* 246 */     String tagName = Normalizer.normalize(this.tq.consumeElementSelector());
/* 247 */     Validate.notEmpty(tagName);
/*     */ 
/*     */     
/* 250 */     if (tagName.startsWith("*|")) {
/* 251 */       String plainTag = tagName.substring(2);
/* 252 */       this.evals.add(new CombiningEvaluator.Or(new Evaluator[] { new Evaluator.Tag(plainTag), new Evaluator.TagEndsWith(tagName
/*     */                 
/* 254 */                 .replace("*|", ":")) }));
/*     */     }
/*     */     else {
/*     */       
/* 258 */       if (tagName.contains("|")) {
/* 259 */         tagName = tagName.replace("|", ":");
/*     */       }
/* 261 */       this.evals.add(new Evaluator.Tag(tagName));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void byAttribute() {
/* 266 */     TokenQueue cq = new TokenQueue(this.tq.chompBalanced('[', ']'));
/* 267 */     String key = cq.consumeToAny(AttributeEvals);
/* 268 */     Validate.notEmpty(key);
/* 269 */     cq.consumeWhitespace();
/*     */     
/* 271 */     if (cq.isEmpty()) {
/* 272 */       if (key.startsWith("^")) {
/* 273 */         this.evals.add(new Evaluator.AttributeStarting(key.substring(1)));
/*     */       } else {
/* 275 */         this.evals.add(new Evaluator.Attribute(key));
/*     */       } 
/* 277 */     } else if (cq.matchChomp("=")) {
/* 278 */       this.evals.add(new Evaluator.AttributeWithValue(key, cq.remainder()));
/*     */     }
/* 280 */     else if (cq.matchChomp("!=")) {
/* 281 */       this.evals.add(new Evaluator.AttributeWithValueNot(key, cq.remainder()));
/*     */     }
/* 283 */     else if (cq.matchChomp("^=")) {
/* 284 */       this.evals.add(new Evaluator.AttributeWithValueStarting(key, cq.remainder()));
/*     */     }
/* 286 */     else if (cq.matchChomp("$=")) {
/* 287 */       this.evals.add(new Evaluator.AttributeWithValueEnding(key, cq.remainder()));
/*     */     }
/* 289 */     else if (cq.matchChomp("*=")) {
/* 290 */       this.evals.add(new Evaluator.AttributeWithValueContaining(key, cq.remainder()));
/*     */     }
/* 292 */     else if (cq.matchChomp("~=")) {
/* 293 */       this.evals.add(new Evaluator.AttributeWithValueMatching(key, Pattern.compile(cq.remainder())));
/*     */     } else {
/* 295 */       throw new Selector.SelectorParseException("Could not parse attribute query '%s': unexpected token at '%s'", new Object[] { this.query, cq.remainder() });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void allElements() {
/* 300 */     this.evals.add(new Evaluator.AllElements());
/*     */   }
/*     */ 
/*     */   
/*     */   private void indexLessThan() {
/* 305 */     this.evals.add(new Evaluator.IndexLessThan(consumeIndex()));
/*     */   }
/*     */   
/*     */   private void indexGreaterThan() {
/* 309 */     this.evals.add(new Evaluator.IndexGreaterThan(consumeIndex()));
/*     */   }
/*     */   
/*     */   private void indexEquals() {
/* 313 */     this.evals.add(new Evaluator.IndexEquals(consumeIndex()));
/*     */   }
/*     */ 
/*     */   
/* 317 */   private static final Pattern NTH_AB = Pattern.compile("(([+-])?(\\d+)?)n(\\s*([+-])?\\s*\\d+)?", 2);
/* 318 */   private static final Pattern NTH_B = Pattern.compile("([+-])?(\\d+)");
/*     */   private void cssNthChild(boolean backwards, boolean ofType) {
/*     */     int a, b;
/* 321 */     String argS = Normalizer.normalize(this.tq.chompTo(")"));
/* 322 */     Matcher mAB = NTH_AB.matcher(argS);
/* 323 */     Matcher mB = NTH_B.matcher(argS);
/*     */     
/* 325 */     if ("odd".equals(argS)) {
/* 326 */       a = 2;
/* 327 */       b = 1;
/* 328 */     } else if ("even".equals(argS)) {
/* 329 */       a = 2;
/* 330 */       b = 0;
/* 331 */     } else if (mAB.matches()) {
/* 332 */       a = (mAB.group(3) != null) ? Integer.parseInt(mAB.group(1).replaceFirst("^\\+", "")) : 1;
/* 333 */       b = (mAB.group(4) != null) ? Integer.parseInt(mAB.group(4).replaceFirst("^\\+", "")) : 0;
/* 334 */     } else if (mB.matches()) {
/* 335 */       a = 0;
/* 336 */       b = Integer.parseInt(mB.group().replaceFirst("^\\+", ""));
/*     */     } else {
/* 338 */       throw new Selector.SelectorParseException("Could not parse nth-index '%s': unexpected format", new Object[] { argS });
/*     */     } 
/* 340 */     if (ofType) {
/* 341 */       if (backwards) {
/* 342 */         this.evals.add(new Evaluator.IsNthLastOfType(a, b));
/*     */       } else {
/* 344 */         this.evals.add(new Evaluator.IsNthOfType(a, b));
/*     */       } 
/* 346 */     } else if (backwards) {
/* 347 */       this.evals.add(new Evaluator.IsNthLastChild(a, b));
/*     */     } else {
/* 349 */       this.evals.add(new Evaluator.IsNthChild(a, b));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int consumeIndex() {
/* 354 */     String indexS = this.tq.chompTo(")").trim();
/* 355 */     Validate.isTrue(StringUtil.isNumeric(indexS), "Index must be numeric");
/* 356 */     return Integer.parseInt(indexS);
/*     */   }
/*     */ 
/*     */   
/*     */   private void has() {
/* 361 */     this.tq.consume(":has");
/* 362 */     String subQuery = this.tq.chompBalanced('(', ')');
/* 363 */     Validate.notEmpty(subQuery, ":has(selector) sub-select must not be empty");
/* 364 */     this.evals.add(new StructuralEvaluator.Has(parse(subQuery)));
/*     */   }
/*     */ 
/*     */   
/*     */   private void contains(boolean own) {
/* 369 */     String query = own ? ":containsOwn" : ":contains";
/* 370 */     this.tq.consume(query);
/* 371 */     String searchText = TokenQueue.unescape(this.tq.chompBalanced('(', ')'));
/* 372 */     Validate.notEmpty(searchText, query + "(text) query must not be empty");
/* 373 */     this.evals.add(own ? 
/* 374 */         new Evaluator.ContainsOwnText(searchText) : 
/* 375 */         new Evaluator.ContainsText(searchText));
/*     */   }
/*     */   
/*     */   private void containsWholeText(boolean own) {
/* 379 */     String query = own ? ":containsWholeOwnText" : ":containsWholeText";
/* 380 */     this.tq.consume(query);
/* 381 */     String searchText = TokenQueue.unescape(this.tq.chompBalanced('(', ')'));
/* 382 */     Validate.notEmpty(searchText, query + "(text) query must not be empty");
/* 383 */     this.evals.add(own ? 
/* 384 */         new Evaluator.ContainsWholeOwnText(searchText) : 
/* 385 */         new Evaluator.ContainsWholeText(searchText));
/*     */   }
/*     */ 
/*     */   
/*     */   private void containsData() {
/* 390 */     this.tq.consume(":containsData");
/* 391 */     String searchText = TokenQueue.unescape(this.tq.chompBalanced('(', ')'));
/* 392 */     Validate.notEmpty(searchText, ":containsData(text) query must not be empty");
/* 393 */     this.evals.add(new Evaluator.ContainsData(searchText));
/*     */   }
/*     */ 
/*     */   
/*     */   private void matches(boolean own) {
/* 398 */     String query = own ? ":matchesOwn" : ":matches";
/* 399 */     this.tq.consume(query);
/* 400 */     String regex = this.tq.chompBalanced('(', ')');
/* 401 */     Validate.notEmpty(regex, query + "(regex) query must not be empty");
/*     */     
/* 403 */     this.evals.add(own ? 
/* 404 */         new Evaluator.MatchesOwn(Pattern.compile(regex)) : 
/* 405 */         new Evaluator.Matches(Pattern.compile(regex)));
/*     */   }
/*     */ 
/*     */   
/*     */   private void matchesWholeText(boolean own) {
/* 410 */     String query = own ? ":matchesWholeOwnText" : ":matchesWholeText";
/* 411 */     this.tq.consume(query);
/* 412 */     String regex = this.tq.chompBalanced('(', ')');
/* 413 */     Validate.notEmpty(regex, query + "(regex) query must not be empty");
/*     */     
/* 415 */     this.evals.add(own ? 
/* 416 */         new Evaluator.MatchesWholeOwnText(Pattern.compile(regex)) : 
/* 417 */         new Evaluator.MatchesWholeText(Pattern.compile(regex)));
/*     */   }
/*     */ 
/*     */   
/*     */   private void not() {
/* 422 */     this.tq.consume(":not");
/* 423 */     String subQuery = this.tq.chompBalanced('(', ')');
/* 424 */     Validate.notEmpty(subQuery, ":not(selector) subselect must not be empty");
/*     */     
/* 426 */     this.evals.add(new StructuralEvaluator.Not(parse(subQuery)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 431 */     return this.query;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\QueryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */