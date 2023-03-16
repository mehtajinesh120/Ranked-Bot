/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.jsoup.Connection;
/*     */ import org.jsoup.Jsoup;
/*     */ import org.jsoup.helper.HttpConnection;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.parser.Tag;
/*     */ import org.jsoup.select.Elements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormElement
/*     */   extends Element
/*     */ {
/*  18 */   private final Elements elements = new Elements();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormElement(Tag tag, String baseUri, Attributes attributes) {
/*  28 */     super(tag, baseUri, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements elements() {
/*  36 */     return this.elements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormElement addElement(Element element) {
/*  45 */     this.elements.add(element);
/*  46 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeChild(Node out) {
/*  51 */     super.removeChild(out);
/*  52 */     this.elements.remove(out);
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
/*     */   public Connection submit() {
/*  66 */     String action = hasAttr("action") ? absUrl("action") : baseUri();
/*  67 */     Validate.notEmpty(action, "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
/*     */     
/*  69 */     Connection.Method method = attr("method").equalsIgnoreCase("POST") ? Connection.Method.POST : Connection.Method.GET;
/*     */     
/*  71 */     Document owner = ownerDocument();
/*  72 */     Connection connection = (owner != null) ? owner.connection().newRequest() : Jsoup.newSession();
/*  73 */     return connection.url(action)
/*  74 */       .data(formData())
/*  75 */       .method(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Connection.KeyVal> formData() {
/*  84 */     ArrayList<Connection.KeyVal> data = new ArrayList<>();
/*     */ 
/*     */     
/*  87 */     for (Element el : this.elements) {
/*  88 */       if (!el.tag().isFormSubmittable() || 
/*  89 */         el.hasAttr("disabled"))
/*  90 */         continue;  String name = el.attr("name");
/*  91 */       if (name.length() == 0)
/*  92 */         continue;  String type = el.attr("type");
/*     */       
/*  94 */       if (type.equalsIgnoreCase("button"))
/*     */         continue; 
/*  96 */       if ("select".equals(el.normalName())) {
/*  97 */         Elements options = el.select("option[selected]");
/*  98 */         boolean set = false;
/*  99 */         for (Element option : options) {
/* 100 */           data.add(HttpConnection.KeyVal.create(name, option.val()));
/* 101 */           set = true;
/*     */         } 
/* 103 */         if (!set) {
/* 104 */           Element option = el.selectFirst("option");
/* 105 */           if (option != null)
/* 106 */             data.add(HttpConnection.KeyVal.create(name, option.val())); 
/*     */         }  continue;
/* 108 */       }  if ("checkbox".equalsIgnoreCase(type) || "radio".equalsIgnoreCase(type)) {
/*     */         
/* 110 */         if (el.hasAttr("checked")) {
/* 111 */           String val = (el.val().length() > 0) ? el.val() : "on";
/* 112 */           data.add(HttpConnection.KeyVal.create(name, val));
/*     */         }  continue;
/*     */       } 
/* 115 */       data.add(HttpConnection.KeyVal.create(name, el.val()));
/*     */     } 
/*     */     
/* 118 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormElement clone() {
/* 123 */     return (FormElement)super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\FormElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */