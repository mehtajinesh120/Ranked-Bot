/*     */ package org.yaml.snakeyaml.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.comments.CommentLine;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
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
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public final class Serializer
/*     */ {
/*     */   private final Emitable emitter;
/*     */   private final Resolver resolver;
/*     */   private final boolean explicitStart;
/*     */   private final boolean explicitEnd;
/*     */   private DumperOptions.Version useVersion;
/*     */   private final Map<String, String> useTags;
/*     */   private final Set<Node> serializedNodes;
/*     */   private final Map<Node, String> anchors;
/*     */   private final AnchorGenerator anchorGenerator;
/*     */   private Boolean closed;
/*     */   private final Tag explicitRoot;
/*     */   
/*     */   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
/*  63 */     this.emitter = emitter;
/*  64 */     this.resolver = resolver;
/*  65 */     this.explicitStart = opts.isExplicitStart();
/*  66 */     this.explicitEnd = opts.isExplicitEnd();
/*  67 */     if (opts.getVersion() != null) {
/*  68 */       this.useVersion = opts.getVersion();
/*     */     }
/*  70 */     this.useTags = opts.getTags();
/*  71 */     this.serializedNodes = new HashSet<>();
/*  72 */     this.anchors = new HashMap<>();
/*  73 */     this.anchorGenerator = opts.getAnchorGenerator();
/*  74 */     this.closed = null;
/*  75 */     this.explicitRoot = rootTag;
/*     */   }
/*     */   
/*     */   public void open() throws IOException {
/*  79 */     if (this.closed == null)
/*  80 */     { this.emitter.emit((Event)new StreamStartEvent(null, null));
/*  81 */       this.closed = Boolean.FALSE; }
/*  82 */     else { if (Boolean.TRUE.equals(this.closed)) {
/*  83 */         throw new SerializerException("serializer is closed");
/*     */       }
/*  85 */       throw new SerializerException("serializer is already opened"); }
/*     */   
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  90 */     if (this.closed == null)
/*  91 */       throw new SerializerException("serializer is not opened"); 
/*  92 */     if (!Boolean.TRUE.equals(this.closed)) {
/*  93 */       this.emitter.emit((Event)new StreamEndEvent(null, null));
/*  94 */       this.closed = Boolean.TRUE;
/*     */       
/*  96 */       this.serializedNodes.clear();
/*  97 */       this.anchors.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serialize(Node node) throws IOException {
/* 102 */     if (this.closed == null)
/* 103 */       throw new SerializerException("serializer is not opened"); 
/* 104 */     if (this.closed.booleanValue()) {
/* 105 */       throw new SerializerException("serializer is closed");
/*     */     }
/* 107 */     this.emitter
/* 108 */       .emit((Event)new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
/* 109 */     anchorNode(node);
/* 110 */     if (this.explicitRoot != null) {
/* 111 */       node.setTag(this.explicitRoot);
/*     */     }
/* 113 */     serializeNode(node, null);
/* 114 */     this.emitter.emit((Event)new DocumentEndEvent(null, null, this.explicitEnd));
/* 115 */     this.serializedNodes.clear();
/* 116 */     this.anchors.clear();
/*     */   }
/*     */   
/*     */   private void anchorNode(Node node) {
/* 120 */     if (node.getNodeId() == NodeId.anchor) {
/* 121 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 123 */     if (this.anchors.containsKey(node)) {
/* 124 */       String anchor = this.anchors.get(node);
/* 125 */       if (null == anchor) {
/* 126 */         anchor = this.anchorGenerator.nextAnchor(node);
/* 127 */         this.anchors.put(node, anchor);
/*     */       } 
/*     */     } else {
/* 130 */       SequenceNode seqNode; List<Node> list; MappingNode mnode; List<NodeTuple> map; this.anchors.put(node, 
/* 131 */           (node.getAnchor() != null) ? this.anchorGenerator.nextAnchor(node) : null);
/* 132 */       switch (node.getNodeId()) {
/*     */         case sequence:
/* 134 */           seqNode = (SequenceNode)node;
/* 135 */           list = seqNode.getValue();
/* 136 */           for (Node item : list) {
/* 137 */             anchorNode(item);
/*     */           }
/*     */           break;
/*     */         case mapping:
/* 141 */           mnode = (MappingNode)node;
/* 142 */           map = mnode.getValue();
/* 143 */           for (NodeTuple object : map) {
/* 144 */             Node key = object.getKeyNode();
/* 145 */             Node value = object.getValueNode();
/* 146 */             anchorNode(key);
/* 147 */             anchorNode(value);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serializeNode(Node node, Node parent) throws IOException {
/* 156 */     if (node.getNodeId() == NodeId.anchor) {
/* 157 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 159 */     String tAlias = this.anchors.get(node);
/* 160 */     if (this.serializedNodes.contains(node)) {
/* 161 */       this.emitter.emit((Event)new AliasEvent(tAlias, null, null));
/*     */     } else {
/* 163 */       ScalarNode scalarNode; Tag detectedTag, defaultTag; ImplicitTuple tuple; ScalarEvent event; SequenceNode seqNode; boolean implicitS; List<Node> list; this.serializedNodes.add(node);
/* 164 */       switch (node.getNodeId()) {
/*     */         case scalar:
/* 166 */           scalarNode = (ScalarNode)node;
/* 167 */           serializeComments(node.getBlockComments());
/* 168 */           detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
/* 169 */           defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
/*     */           
/* 171 */           tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
/*     */           
/* 173 */           event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getScalarStyle());
/* 174 */           this.emitter.emit((Event)event);
/* 175 */           serializeComments(node.getInLineComments());
/* 176 */           serializeComments(node.getEndComments());
/*     */           return;
/*     */         case sequence:
/* 179 */           seqNode = (SequenceNode)node;
/* 180 */           serializeComments(node.getBlockComments());
/*     */           
/* 182 */           implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
/* 183 */           this.emitter.emit((Event)new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode
/* 184 */                 .getFlowStyle()));
/* 185 */           list = seqNode.getValue();
/* 186 */           for (Node item : list) {
/* 187 */             serializeNode(item, node);
/*     */           }
/* 189 */           this.emitter.emit((Event)new SequenceEndEvent(null, null));
/* 190 */           serializeComments(node.getInLineComments());
/* 191 */           serializeComments(node.getEndComments());
/*     */           return;
/*     */       } 
/* 194 */       serializeComments(node.getBlockComments());
/* 195 */       Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
/* 196 */       boolean implicitM = node.getTag().equals(implicitTag);
/* 197 */       MappingNode mnode = (MappingNode)node;
/* 198 */       List<NodeTuple> map = mnode.getValue();
/* 199 */       if (mnode.getTag() != Tag.COMMENT) {
/* 200 */         this.emitter.emit((Event)new MappingStartEvent(tAlias, mnode.getTag().getValue(), implicitM, null, null, mnode
/* 201 */               .getFlowStyle()));
/* 202 */         for (NodeTuple row : map) {
/* 203 */           Node key = row.getKeyNode();
/* 204 */           Node value = row.getValueNode();
/* 205 */           serializeNode(key, (Node)mnode);
/* 206 */           serializeNode(value, (Node)mnode);
/*     */         } 
/* 208 */         this.emitter.emit((Event)new MappingEndEvent(null, null));
/* 209 */         serializeComments(node.getInLineComments());
/* 210 */         serializeComments(node.getEndComments());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void serializeComments(List<CommentLine> comments) throws IOException {
/* 217 */     if (comments == null) {
/*     */       return;
/*     */     }
/* 220 */     for (CommentLine line : comments) {
/*     */       
/* 222 */       CommentEvent commentEvent = new CommentEvent(line.getCommentType(), line.getValue(), line.getStartMark(), line.getEndMark());
/* 223 */       this.emitter.emit((Event)commentEvent);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\serializer\Serializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */