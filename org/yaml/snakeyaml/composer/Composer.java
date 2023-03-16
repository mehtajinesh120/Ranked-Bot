/*     */ package org.yaml.snakeyaml.composer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.comments.CommentEventsCollector;
/*     */ import org.yaml.snakeyaml.comments.CommentLine;
/*     */ import org.yaml.snakeyaml.comments.CommentType;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.NodeEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*     */ public class Composer
/*     */ {
/*     */   protected final Parser parser;
/*     */   private final Resolver resolver;
/*     */   private final Map<String, Node> anchors;
/*     */   private final Set<Node> recursiveNodes;
/*  59 */   private int nonScalarAliasesCount = 0;
/*     */   
/*     */   private final LoaderOptions loadingConfig;
/*     */   private final CommentEventsCollector blockCommentsCollector;
/*     */   private final CommentEventsCollector inlineCommentsCollector;
/*  64 */   private int nestingDepth = 0;
/*     */   private final int nestingDepthLimit;
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver) {
/*  68 */     this(parser, resolver, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver, LoaderOptions loadingConfig) {
/*  72 */     this.parser = parser;
/*  73 */     this.resolver = resolver;
/*  74 */     this.anchors = new HashMap<>();
/*  75 */     this.recursiveNodes = new HashSet<>();
/*  76 */     this.loadingConfig = loadingConfig;
/*  77 */     this.blockCommentsCollector = new CommentEventsCollector(parser, new CommentType[] { CommentType.BLANK_LINE, CommentType.BLOCK });
/*     */     
/*  79 */     this.inlineCommentsCollector = new CommentEventsCollector(parser, new CommentType[] { CommentType.IN_LINE });
/*  80 */     this.nestingDepthLimit = loadingConfig.getNestingDepthLimit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNode() {
/*  90 */     if (this.parser.checkEvent(Event.ID.StreamStart)) {
/*  91 */       this.parser.getEvent();
/*     */     }
/*     */     
/*  94 */     return !this.parser.checkEvent(Event.ID.StreamEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/* 104 */     this.blockCommentsCollector.collectEvents();
/* 105 */     if (this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 106 */       List<CommentLine> commentLines = this.blockCommentsCollector.consume();
/* 107 */       Mark startMark = ((CommentLine)commentLines.get(0)).getStartMark();
/* 108 */       List<NodeTuple> children = Collections.emptyList();
/* 109 */       MappingNode mappingNode = new MappingNode(Tag.COMMENT, false, children, startMark, null, DumperOptions.FlowStyle.BLOCK);
/* 110 */       mappingNode.setBlockComments(commentLines);
/* 111 */       return (Node)mappingNode;
/*     */     } 
/*     */     
/* 114 */     this.parser.getEvent();
/*     */     
/* 116 */     Node node = composeNode(null);
/*     */     
/* 118 */     this.blockCommentsCollector.collectEvents();
/* 119 */     if (!this.blockCommentsCollector.isEmpty()) {
/* 120 */       node.setEndComments(this.blockCommentsCollector.consume());
/*     */     }
/* 122 */     this.parser.getEvent();
/* 123 */     this.anchors.clear();
/* 124 */     this.recursiveNodes.clear();
/* 125 */     return node;
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
/*     */   public Node getSingleNode() {
/* 138 */     this.parser.getEvent();
/*     */     
/* 140 */     Node document = null;
/* 141 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 142 */       document = getNode();
/*     */     }
/*     */     
/* 145 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 146 */       Event event = this.parser.getEvent();
/* 147 */       Mark contextMark = (document != null) ? document.getStartMark() : null;
/* 148 */       throw new ComposerException("expected a single document in the stream", contextMark, "but found another document", event
/* 149 */           .getStartMark());
/*     */     } 
/*     */     
/* 152 */     this.parser.getEvent();
/* 153 */     return document;
/*     */   }
/*     */   private Node composeNode(Node parent) {
/*     */     Node node;
/* 157 */     this.blockCommentsCollector.collectEvents();
/* 158 */     if (parent != null) {
/* 159 */       this.recursiveNodes.add(parent);
/*     */     }
/*     */     
/* 162 */     if (this.parser.checkEvent(Event.ID.Alias)) {
/* 163 */       AliasEvent event = (AliasEvent)this.parser.getEvent();
/* 164 */       String anchor = event.getAnchor();
/* 165 */       if (!this.anchors.containsKey(anchor)) {
/* 166 */         throw new ComposerException(null, null, "found undefined alias " + anchor, event
/* 167 */             .getStartMark());
/*     */       }
/* 169 */       node = this.anchors.get(anchor);
/* 170 */       if (!(node instanceof ScalarNode)) {
/* 171 */         this.nonScalarAliasesCount++;
/* 172 */         if (this.nonScalarAliasesCount > this.loadingConfig.getMaxAliasesForCollections()) {
/* 173 */           throw new YAMLException("Number of aliases for non-scalar nodes exceeds the specified max=" + this.loadingConfig
/*     */               
/* 175 */               .getMaxAliasesForCollections());
/*     */         }
/*     */       } 
/* 178 */       if (this.recursiveNodes.remove(node)) {
/* 179 */         node.setTwoStepsConstruction(true);
/*     */       }
/*     */       
/* 182 */       this.blockCommentsCollector.consume();
/* 183 */       this.inlineCommentsCollector.collectEvents().consume();
/*     */     } else {
/* 185 */       NodeEvent event = (NodeEvent)this.parser.peekEvent();
/* 186 */       String anchor = event.getAnchor();
/* 187 */       increaseNestingDepth();
/*     */       
/* 189 */       if (this.parser.checkEvent(Event.ID.Scalar)) {
/* 190 */         node = composeScalarNode(anchor, this.blockCommentsCollector.consume());
/* 191 */       } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
/* 192 */         node = composeSequenceNode(anchor);
/*     */       } else {
/* 194 */         node = composeMappingNode(anchor);
/*     */       } 
/* 196 */       decreaseNestingDepth();
/*     */     } 
/* 198 */     this.recursiveNodes.remove(parent);
/* 199 */     return node;
/*     */   }
/*     */   protected Node composeScalarNode(String anchor, List<CommentLine> blockComments) {
/*     */     Tag nodeTag;
/* 203 */     ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
/* 204 */     String tag = ev.getTag();
/* 205 */     boolean resolved = false;
/*     */     
/* 207 */     if (tag == null || tag.equals("!")) {
/* 208 */       nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev
/* 209 */           .getImplicit().canOmitTagInPlainScalar());
/* 210 */       resolved = true;
/*     */     } else {
/* 212 */       nodeTag = new Tag(tag);
/*     */     } 
/*     */     
/* 215 */     ScalarNode scalarNode = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getScalarStyle());
/* 216 */     if (anchor != null) {
/* 217 */       scalarNode.setAnchor(anchor);
/* 218 */       this.anchors.put(anchor, scalarNode);
/*     */     } 
/* 220 */     scalarNode.setBlockComments(blockComments);
/* 221 */     scalarNode.setInLineComments(this.inlineCommentsCollector.collectEvents().consume());
/* 222 */     return (Node)scalarNode;
/*     */   }
/*     */   protected Node composeSequenceNode(String anchor) {
/*     */     Tag nodeTag;
/* 226 */     SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
/* 227 */     String tag = startEvent.getTag();
/*     */ 
/*     */     
/* 230 */     boolean resolved = false;
/* 231 */     if (tag == null || tag.equals("!")) {
/* 232 */       nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
/* 233 */       resolved = true;
/*     */     } else {
/* 235 */       nodeTag = new Tag(tag);
/*     */     } 
/* 237 */     ArrayList<Node> children = new ArrayList<>();
/*     */     
/* 239 */     SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/* 240 */     if (startEvent.isFlow()) {
/* 241 */       node.setBlockComments(this.blockCommentsCollector.consume());
/*     */     }
/* 243 */     if (anchor != null) {
/* 244 */       node.setAnchor(anchor);
/* 245 */       this.anchors.put(anchor, node);
/*     */     } 
/* 247 */     while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
/* 248 */       this.blockCommentsCollector.collectEvents();
/* 249 */       if (this.parser.checkEvent(Event.ID.SequenceEnd)) {
/*     */         break;
/*     */       }
/* 252 */       children.add(composeNode((Node)node));
/*     */     } 
/* 254 */     if (startEvent.isFlow()) {
/* 255 */       node.setInLineComments(this.inlineCommentsCollector.collectEvents().consume());
/*     */     }
/* 257 */     Event endEvent = this.parser.getEvent();
/* 258 */     node.setEndMark(endEvent.getEndMark());
/* 259 */     this.inlineCommentsCollector.collectEvents();
/* 260 */     if (!this.inlineCommentsCollector.isEmpty()) {
/* 261 */       node.setInLineComments(this.inlineCommentsCollector.consume());
/*     */     }
/* 263 */     return (Node)node;
/*     */   }
/*     */   protected Node composeMappingNode(String anchor) {
/*     */     Tag nodeTag;
/* 267 */     MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
/* 268 */     String tag = startEvent.getTag();
/*     */     
/* 270 */     boolean resolved = false;
/* 271 */     if (tag == null || tag.equals("!")) {
/* 272 */       nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
/* 273 */       resolved = true;
/*     */     } else {
/* 275 */       nodeTag = new Tag(tag);
/*     */     } 
/*     */     
/* 278 */     List<NodeTuple> children = new ArrayList<>();
/*     */     
/* 280 */     MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/* 281 */     if (startEvent.isFlow()) {
/* 282 */       node.setBlockComments(this.blockCommentsCollector.consume());
/*     */     }
/* 284 */     if (anchor != null) {
/* 285 */       node.setAnchor(anchor);
/* 286 */       this.anchors.put(anchor, node);
/*     */     } 
/* 288 */     while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
/* 289 */       this.blockCommentsCollector.collectEvents();
/* 290 */       if (this.parser.checkEvent(Event.ID.MappingEnd)) {
/*     */         break;
/*     */       }
/* 293 */       composeMappingChildren(children, node);
/*     */     } 
/* 295 */     if (startEvent.isFlow()) {
/* 296 */       node.setInLineComments(this.inlineCommentsCollector.collectEvents().consume());
/*     */     }
/* 298 */     Event endEvent = this.parser.getEvent();
/* 299 */     node.setEndMark(endEvent.getEndMark());
/* 300 */     this.inlineCommentsCollector.collectEvents();
/* 301 */     if (!this.inlineCommentsCollector.isEmpty()) {
/* 302 */       node.setInLineComments(this.inlineCommentsCollector.consume());
/*     */     }
/* 304 */     return (Node)node;
/*     */   }
/*     */   
/*     */   protected void composeMappingChildren(List<NodeTuple> children, MappingNode node) {
/* 308 */     Node itemKey = composeKeyNode(node);
/* 309 */     if (itemKey.getTag().equals(Tag.MERGE)) {
/* 310 */       node.setMerged(true);
/*     */     }
/* 312 */     Node itemValue = composeValueNode(node);
/* 313 */     children.add(new NodeTuple(itemKey, itemValue));
/*     */   }
/*     */   
/*     */   protected Node composeKeyNode(MappingNode node) {
/* 317 */     return composeNode((Node)node);
/*     */   }
/*     */   
/*     */   protected Node composeValueNode(MappingNode node) {
/* 321 */     return composeNode((Node)node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void increaseNestingDepth() {
/* 328 */     if (this.nestingDepth > this.nestingDepthLimit) {
/* 329 */       throw new YAMLException("Nesting Depth exceeded max " + this.nestingDepthLimit);
/*     */     }
/* 331 */     this.nestingDepth++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decreaseNestingDepth() {
/* 338 */     if (this.nestingDepth > 0) {
/* 339 */       this.nestingDepth--;
/*     */     } else {
/* 341 */       throw new YAMLException("Nesting Depth cannot be negative");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\composer\Composer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */