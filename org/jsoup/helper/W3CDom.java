/*     */ package org.jsoup.helper;
/*     */ 
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Stack;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathException;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Attribute;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ import org.jsoup.nodes.Comment;
/*     */ import org.jsoup.nodes.DataNode;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.DocumentType;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.TextNode;
/*     */ import org.jsoup.select.NodeTraversor;
/*     */ import org.jsoup.select.NodeVisitor;
/*     */ import org.jsoup.select.Selector;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class W3CDom
/*     */ {
/*     */   public static final String SourceProperty = "jsoupSource";
/*     */   private static final String ContextProperty = "jsoupContextSource";
/*     */   private static final String ContextNodeProperty = "jsoupContextNode";
/*     */   public static final String XPathFactoryProperty = "javax.xml.xpath.XPathFactory:jsoup";
/*     */   protected DocumentBuilderFactory factory;
/*     */   private boolean namespaceAware = true;
/*     */   
/*     */   public W3CDom() {
/*  65 */     this.factory = DocumentBuilderFactory.newInstance();
/*  66 */     this.factory.setNamespaceAware(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean namespaceAware() {
/*  75 */     return this.namespaceAware;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public W3CDom namespaceAware(boolean namespaceAware) {
/*  84 */     this.namespaceAware = namespaceAware;
/*  85 */     this.factory.setNamespaceAware(namespaceAware);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Document convert(Document in) {
/*  96 */     return (new W3CDom()).fromJsoup(in);
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
/*     */   public static String asString(Document doc, @Nullable Map<String, String> properties) {
/*     */     try {
/* 121 */       DOMSource domSource = new DOMSource(doc);
/* 122 */       StringWriter writer = new StringWriter();
/* 123 */       StreamResult result = new StreamResult(writer);
/* 124 */       TransformerFactory tf = TransformerFactory.newInstance();
/* 125 */       Transformer transformer = tf.newTransformer();
/* 126 */       if (properties != null) {
/* 127 */         transformer.setOutputProperties(propertiesFromMap(properties));
/*     */       }
/* 129 */       if (doc.getDoctype() != null) {
/* 130 */         DocumentType doctype = doc.getDoctype();
/* 131 */         if (!StringUtil.isBlank(doctype.getPublicId()))
/* 132 */           transformer.setOutputProperty("doctype-public", doctype.getPublicId()); 
/* 133 */         if (!StringUtil.isBlank(doctype.getSystemId())) {
/* 134 */           transformer.setOutputProperty("doctype-system", doctype.getSystemId());
/*     */         }
/* 136 */         else if (doctype.getName().equalsIgnoreCase("html") && 
/* 137 */           StringUtil.isBlank(doctype.getPublicId()) && 
/* 138 */           StringUtil.isBlank(doctype.getSystemId())) {
/* 139 */           transformer.setOutputProperty("doctype-system", "about:legacy-compat");
/*     */         } 
/*     */       } 
/* 142 */       transformer.transform(domSource, result);
/* 143 */       return writer.toString();
/*     */     }
/* 145 */     catch (TransformerException e) {
/* 146 */       throw new IllegalStateException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static Properties propertiesFromMap(Map<String, String> map) {
/* 151 */     Properties props = new Properties();
/* 152 */     props.putAll(map);
/* 153 */     return props;
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashMap<String, String> OutputHtml() {
/* 158 */     return methodMap("html");
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashMap<String, String> OutputXml() {
/* 163 */     return methodMap("xml");
/*     */   }
/*     */   
/*     */   private static HashMap<String, String> methodMap(String method) {
/* 167 */     HashMap<String, String> map = new HashMap<>();
/* 168 */     map.put("method", method);
/* 169 */     return map;
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
/*     */   public Document fromJsoup(Document in) {
/* 182 */     return fromJsoup((Element)in);
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
/*     */ 
/*     */   
/*     */   public Document fromJsoup(Element in) {
/* 197 */     Validate.notNull(in);
/*     */     
/*     */     try {
/* 200 */       DocumentBuilder builder = this.factory.newDocumentBuilder();
/* 201 */       DOMImplementation impl = builder.getDOMImplementation();
/* 202 */       Document out = builder.newDocument();
/* 203 */       Document inDoc = in.ownerDocument();
/* 204 */       DocumentType doctype = (inDoc != null) ? inDoc.documentType() : null;
/* 205 */       if (doctype != null) {
/* 206 */         DocumentType documentType = impl.createDocumentType(doctype.name(), doctype.publicId(), doctype.systemId());
/* 207 */         out.appendChild(documentType);
/*     */       } 
/* 209 */       out.setXmlStandalone(true);
/*     */       
/* 211 */       Element context = (in instanceof Document) ? in.child(0) : in;
/* 212 */       out.setUserData("jsoupContextSource", context, null);
/* 213 */       convert((inDoc != null) ? (Element)inDoc : in, out);
/* 214 */       return out;
/* 215 */     } catch (ParserConfigurationException e) {
/* 216 */       throw new IllegalStateException(e);
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
/*     */   public void convert(Document in, Document out) {
/* 230 */     convert((Element)in, out);
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
/*     */   public void convert(Element in, Document out) {
/* 242 */     W3CBuilder builder = new W3CBuilder(out);
/* 243 */     builder.namespaceAware = this.namespaceAware;
/* 244 */     Document inDoc = in.ownerDocument();
/* 245 */     if (inDoc != null) {
/* 246 */       if (!StringUtil.isBlank(inDoc.location())) {
/* 247 */         out.setDocumentURI(inDoc.location());
/*     */       }
/* 249 */       builder.syntax = inDoc.outputSettings().syntax();
/*     */     } 
/* 251 */     Element rootEl = (in instanceof Document) ? in.child(0) : in;
/* 252 */     NodeTraversor.traverse(builder, (Node)rootEl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NodeList selectXpath(String xpath, Document doc) {
/* 262 */     return selectXpath(xpath, doc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NodeList selectXpath(String xpath, Node contextNode) {
/*     */     NodeList nodeList;
/* 272 */     Validate.notEmptyParam(xpath, "xpath");
/* 273 */     Validate.notNullParam(contextNode, "contextNode");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 278 */       String property = System.getProperty("javax.xml.xpath.XPathFactory:jsoup");
/*     */ 
/*     */       
/* 281 */       XPathFactory xPathFactory = (property != null) ? XPathFactory.newInstance("jsoup") : XPathFactory.newInstance();
/*     */       
/* 283 */       XPathExpression expression = xPathFactory.newXPath().compile(xpath);
/* 284 */       nodeList = (NodeList)expression.evaluate(contextNode, XPathConstants.NODESET);
/* 285 */       Validate.notNull(nodeList);
/* 286 */     } catch (XPathExpressionException|javax.xml.xpath.XPathFactoryConfigurationException e) {
/* 287 */       throw new Selector.SelectorParseException("Could not evaluate XPath query [%s]: %s", new Object[] { xpath, e.getMessage() });
/*     */     } 
/* 289 */     return nodeList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Node> List<T> sourceNodes(NodeList nodeList, Class<T> nodeType) {
/* 300 */     Validate.notNull(nodeList);
/* 301 */     Validate.notNull(nodeType);
/* 302 */     List<T> nodes = new ArrayList<>(nodeList.getLength());
/*     */     
/* 304 */     for (int i = 0; i < nodeList.getLength(); i++) {
/* 305 */       Node node = nodeList.item(i);
/* 306 */       Object source = node.getUserData("jsoupSource");
/* 307 */       if (nodeType.isInstance(source)) {
/* 308 */         nodes.add(nodeType.cast(source));
/*     */       }
/*     */     } 
/* 311 */     return nodes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node contextNode(Document wDoc) {
/* 320 */     return (Node)wDoc.getUserData("jsoupContextNode");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asString(Document doc) {
/* 331 */     return asString(doc, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class W3CBuilder
/*     */     implements NodeVisitor
/*     */   {
/*     */     private static final String xmlnsKey = "xmlns";
/*     */     
/*     */     private static final String xmlnsPrefix = "xmlns:";
/*     */     private final Document doc;
/*     */     private boolean namespaceAware = true;
/* 343 */     private final Stack<HashMap<String, String>> namespacesStack = new Stack<>();
/*     */     private Node dest;
/* 345 */     private Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
/*     */ 
/*     */     
/*     */     public W3CBuilder(Document doc) {
/* 349 */       this.doc = doc;
/* 350 */       this.namespacesStack.push(new HashMap<>());
/* 351 */       this.dest = doc;
/* 352 */       this.contextElement = (Element)doc.getUserData("jsoupContextSource");
/*     */     } @Nullable
/*     */     private final Element contextElement;
/*     */     public void head(Node source, int depth) {
/* 356 */       this.namespacesStack.push(new HashMap<>(this.namespacesStack.peek()));
/* 357 */       if (source instanceof Element) {
/* 358 */         Element sourceEl = (Element)source;
/*     */         
/* 360 */         String prefix = updateNamespaces(sourceEl);
/* 361 */         String namespace = this.namespaceAware ? (String)((HashMap)this.namespacesStack.peek()).get(prefix) : null;
/* 362 */         String tagName = sourceEl.tagName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 371 */           Element el = (namespace == null && tagName.contains(":")) ? this.doc.createElementNS("", tagName) : this.doc.createElementNS(namespace, tagName);
/* 372 */           copyAttributes((Node)sourceEl, el);
/* 373 */           append(el, (Node)sourceEl);
/* 374 */           if (sourceEl == this.contextElement)
/* 375 */             this.doc.setUserData("jsoupContextNode", el, null); 
/* 376 */           this.dest = el;
/* 377 */         } catch (DOMException e) {
/* 378 */           append(this.doc.createTextNode("<" + tagName + ">"), (Node)sourceEl);
/*     */         } 
/* 380 */       } else if (source instanceof TextNode) {
/* 381 */         TextNode sourceText = (TextNode)source;
/* 382 */         Text text = this.doc.createTextNode(sourceText.getWholeText());
/* 383 */         append(text, (Node)sourceText);
/* 384 */       } else if (source instanceof Comment) {
/* 385 */         Comment sourceComment = (Comment)source;
/* 386 */         Comment comment = this.doc.createComment(sourceComment.getData());
/* 387 */         append(comment, (Node)sourceComment);
/* 388 */       } else if (source instanceof DataNode) {
/* 389 */         DataNode sourceData = (DataNode)source;
/* 390 */         Text node = this.doc.createTextNode(sourceData.getWholeData());
/* 391 */         append(node, (Node)sourceData);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void append(Node append, Node source) {
/* 398 */       append.setUserData("jsoupSource", source, null);
/* 399 */       this.dest.appendChild(append);
/*     */     }
/*     */     
/*     */     public void tail(Node source, int depth) {
/* 403 */       if (source instanceof Element && this.dest.getParentNode() instanceof Element) {
/* 404 */         this.dest = this.dest.getParentNode();
/*     */       }
/* 406 */       this.namespacesStack.pop();
/*     */     }
/*     */     
/*     */     private void copyAttributes(Node source, Element el) {
/* 410 */       for (Attribute attribute : source.attributes()) {
/* 411 */         String key = Attribute.getValidKey(attribute.getKey(), this.syntax);
/* 412 */         if (key != null) {
/* 413 */           el.setAttribute(key, attribute.getValue());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String updateNamespaces(Element el) {
/* 424 */       Attributes attributes = el.attributes();
/* 425 */       for (Attribute attr : attributes) {
/* 426 */         String prefix, key = attr.getKey();
/*     */         
/* 428 */         if (key.equals("xmlns")) {
/* 429 */           prefix = "";
/* 430 */         } else if (key.startsWith("xmlns:")) {
/* 431 */           prefix = key.substring("xmlns:".length());
/*     */         } else {
/*     */           continue;
/*     */         } 
/* 435 */         ((HashMap<String, String>)this.namespacesStack.peek()).put(prefix, attr.getValue());
/*     */       } 
/*     */ 
/*     */       
/* 439 */       int pos = el.tagName().indexOf(':');
/* 440 */       return (pos > 0) ? el.tagName().substring(0, pos) : "";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\W3CDom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */