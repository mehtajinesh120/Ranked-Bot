/*     */ package net.dv8tion.jda.internal.interactions;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Emoji;
/*     */ import net.dv8tion.jda.api.interactions.components.Button;
/*     */ import net.dv8tion.jda.api.interactions.components.ButtonStyle;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
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
/*     */ public class ButtonImpl
/*     */   implements Button
/*     */ {
/*     */   private final String id;
/*     */   private final String label;
/*     */   private final ButtonStyle style;
/*     */   private final String url;
/*     */   private final boolean disabled;
/*     */   private final Emoji emoji;
/*     */   
/*     */   public ButtonImpl(DataObject data) {
/*  39 */     this(data
/*  40 */         .getString("custom_id", null), data
/*  41 */         .getString("label", ""), 
/*  42 */         ButtonStyle.fromKey(data.getInt("style")), data
/*  43 */         .getString("url", null), data
/*  44 */         .getBoolean("disabled"), data
/*  45 */         .optObject("emoji").map(Emoji::fromData).orElse(null));
/*     */   }
/*     */ 
/*     */   
/*     */   public ButtonImpl(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji) {
/*  50 */     this(id, label, style, null, disabled, emoji);
/*     */   }
/*     */ 
/*     */   
/*     */   public ButtonImpl(String id, String label, ButtonStyle style, String url, boolean disabled, Emoji emoji) {
/*  55 */     this.id = id;
/*  56 */     this.label = label;
/*  57 */     this.style = style;
/*  58 */     this.url = url;
/*  59 */     this.disabled = disabled;
/*  60 */     this.emoji = emoji;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Component.Type getType() {
/*  67 */     return Component.Type.BUTTON;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getId() {
/*  74 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getLabel() {
/*  81 */     return this.label;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ButtonStyle getStyle() {
/*  88 */     return this.style;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getUrl() {
/*  95 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Emoji getEmoji() {
/* 102 */     return this.emoji;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 108 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 115 */     DataObject json = DataObject.empty();
/* 116 */     json.put("type", Integer.valueOf(2));
/* 117 */     json.put("label", this.label);
/* 118 */     json.put("style", Integer.valueOf(this.style.getKey()));
/* 119 */     json.put("disabled", Boolean.valueOf(this.disabled));
/* 120 */     if (this.emoji != null)
/* 121 */       json.put("emoji", this.emoji); 
/* 122 */     if (this.url != null) {
/* 123 */       json.put("url", this.url);
/*     */     } else {
/* 125 */       json.put("custom_id", this.id);
/* 126 */     }  return json;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 132 */     return Objects.hash(new Object[] { this.id, this.label, this.style, this.url, Boolean.valueOf(this.disabled), this.emoji });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 138 */     if (obj == this) return true; 
/* 139 */     if (!(obj instanceof ButtonImpl)) return false; 
/* 140 */     ButtonImpl other = (ButtonImpl)obj;
/* 141 */     return (Objects.equals(other.id, this.id) && 
/* 142 */       Objects.equals(other.label, this.label) && 
/* 143 */       Objects.equals(other.url, this.url) && 
/* 144 */       Objects.equals(other.emoji, this.emoji) && other.disabled == this.disabled && other.style == this.style);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     return "B:" + this.label + "(" + this.id + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\ButtonImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */