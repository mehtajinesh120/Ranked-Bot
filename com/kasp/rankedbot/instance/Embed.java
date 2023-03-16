/*     */ package com.kasp.rankedbot.instance;
/*     */ 
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.RankedBot;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import java.awt.Color;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import net.dv8tion.jda.api.EmbedBuilder;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.Button;
/*     */ 
/*     */ 
/*     */ public class Embed
/*     */ {
/*  18 */   public static HashMap<String, List<Embed>> embedPages = new HashMap<>();
/*     */   
/*     */   private int pages;
/*  21 */   private int currentPage = 0;
/*     */   private EmbedType type;
/*     */   private String title;
/*     */   private String description;
/*     */   private String thumbnailURL;
/*     */   private String imageURL;
/*     */   private String footer;
/*  28 */   private List<MessageEmbed.Field> fields = new ArrayList<>();
/*     */   
/*  30 */   EmbedBuilder eb = new EmbedBuilder();
/*     */   
/*     */   public Embed(EmbedType type, String title, String description, int pages) {
/*  33 */     this.type = type;
/*  34 */     this.title = title;
/*  35 */     this.description = description;
/*  36 */     this.pages = pages;
/*     */     
/*  38 */     String[] d = Config.getValue("default").split(",");
/*  39 */     String[] s = Config.getValue("success").split(",");
/*  40 */     String[] e = Config.getValue("error").split(",");
/*     */     
/*  42 */     if (type == EmbedType.SUCCESS) { this.eb.setColor(new Color(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]))); }
/*  43 */     else if (type == EmbedType.ERROR) { this.eb.setColor(new Color(Integer.parseInt(e[0]), Integer.parseInt(e[1]), Integer.parseInt(e[2]))); }
/*  44 */     else { this.eb.setColor(new Color(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]))); }
/*     */     
/*  46 */     this.footer = Config.getValue("footer").replaceAll("%name%", Config.getValue("server-name")).replaceAll("%version%", RankedBot.version);
/*     */   }
/*     */   
/*     */   public MessageEmbed build() {
/*  50 */     if (this.title != null && !this.title.equals(""))
/*  51 */       if (this.pages != 1) {
/*  52 */         this.eb.setTitle(this.title + " `[Page: " + this.title + "/" + this.currentPage + 1 + "]`");
/*     */       } else {
/*     */         
/*  55 */         this.eb.setTitle(this.title);
/*     */       }  
/*  57 */     if (this.description != null && !this.description.equals(""))
/*  58 */       this.eb.setDescription(this.description); 
/*  59 */     if (this.thumbnailURL != null && !this.thumbnailURL.equals(""))
/*  60 */       this.eb.setThumbnail(this.thumbnailURL); 
/*  61 */     if (this.imageURL != null && !this.imageURL.equals(""))
/*  62 */       this.eb.setImage(this.imageURL); 
/*  63 */     if (this.footer != null && !this.footer.equals("")) {
/*  64 */       this.eb.setFooter(this.footer).setTimestamp(OffsetDateTime.now());
/*     */     }
/*  66 */     for (MessageEmbed.Field f : this.fields) {
/*  67 */       this.eb.addField(f.getName(), f.getValue(), f.isInline());
/*     */     }
/*     */     
/*  70 */     this.fields.clear();
/*     */     
/*  72 */     return this.eb.build();
/*     */   }
/*     */   
/*     */   public void addField(String title, String content, boolean inline) {
/*  76 */     this.fields.add(new MessageEmbed.Field(title, content, inline));
/*     */   }
/*     */   
/*     */   public List<MessageEmbed.Field> getFields() {
/*  80 */     return this.fields;
/*     */   }
/*     */   
/*     */   public static List<Button> createButtons(int currentPage) {
/*  84 */     List<Button> buttons = new ArrayList<>();
/*  85 */     buttons.add(Button.secondary("rankedbot-page-" + currentPage - 1, "←"));
/*  86 */     buttons.add(Button.secondary("rankedbot-page-" + currentPage + 1, "→"));
/*  87 */     return buttons;
/*     */   }
/*     */   
/*     */   public static Embed getPage(String msgID, int page) {
/*  91 */     return ((List<Embed>)embedPages.get(msgID)).get(page);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addPage(String msgID, Embed embed) {
/*     */     List<Embed> pages;
/*  97 */     if (embedPages.get(msgID) != null) {
/*  98 */       pages = new ArrayList<>(embedPages.get(msgID));
/*     */     } else {
/*     */       
/* 101 */       pages = new ArrayList<>();
/*     */     } 
/*     */     
/* 104 */     pages.add(embed);
/* 105 */     embedPages.put(msgID, pages);
/*     */   }
/*     */   
/*     */   public int getPages() {
/* 109 */     return this.pages;
/*     */   }
/*     */   
/*     */   public void setPages(int pages) {
/* 113 */     this.pages = pages;
/*     */   }
/*     */   
/*     */   public int getCurrentPage() {
/* 117 */     return this.currentPage;
/*     */   }
/*     */   
/*     */   public void setCurrentPage(int currentPage) {
/* 121 */     this.currentPage = currentPage;
/*     */   }
/*     */   
/*     */   public EmbedType getType() {
/* 125 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(EmbedType type) {
/* 129 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 133 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 137 */     this.title = title;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 141 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 145 */     this.description = description;
/*     */   }
/*     */   
/*     */   public String getThumbnailURL() {
/* 149 */     return this.thumbnailURL;
/*     */   }
/*     */   
/*     */   public void setThumbnailURL(String thumbnailURL) {
/* 153 */     this.thumbnailURL = thumbnailURL;
/*     */   }
/*     */   
/*     */   public String getImageURL() {
/* 157 */     return this.imageURL;
/*     */   }
/*     */   
/*     */   public void setImageURL(String imageURL) {
/* 161 */     this.imageURL = imageURL;
/*     */   }
/*     */   
/*     */   public String getFooter() {
/* 165 */     return this.footer;
/*     */   }
/*     */   
/*     */   public void setFooter(String footer) {
/* 169 */     this.footer = footer;
/*     */   }
/*     */   
/*     */   public EmbedBuilder getEb() {
/* 173 */     return this.eb;
/*     */   }
/*     */   
/*     */   public void setEb(EmbedBuilder eb) {
/* 177 */     this.eb = eb;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Embed.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */