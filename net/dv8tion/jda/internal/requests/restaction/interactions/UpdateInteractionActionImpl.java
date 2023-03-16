/*     */ package net.dv8tion.jda.internal.requests.restaction.interactions;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.InteractionCallbackAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.interactions.InteractionHookImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public class UpdateInteractionActionImpl
/*     */   extends InteractionCallbackActionImpl
/*     */   implements UpdateInteractionAction
/*     */ {
/*  40 */   private List<String> retainedFiles = null;
/*  41 */   private List<MessageEmbed> embeds = null;
/*  42 */   private List<ActionRow> components = null;
/*  43 */   private String content = null;
/*     */ 
/*     */   
/*     */   public UpdateInteractionActionImpl(InteractionHookImpl hook) {
/*  47 */     super(hook);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isEmpty() {
/*  52 */     return (this.content == null && this.embeds == null && this.components == null && this.files.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected DataObject toData() {
/*  58 */     DataObject json = DataObject.empty();
/*  59 */     if (isEmpty())
/*  60 */       return json.put("type", Integer.valueOf(InteractionCallbackAction.ResponseType.DEFERRED_MESSAGE_UPDATE.getRaw())); 
/*  61 */     json.put("type", Integer.valueOf(InteractionCallbackAction.ResponseType.MESSAGE_UPDATE.getRaw()));
/*  62 */     DataObject data = DataObject.empty();
/*  63 */     if (this.content != null)
/*  64 */       data.put("content", this.content); 
/*  65 */     if (this.embeds != null)
/*  66 */       data.put("embeds", DataArray.fromCollection(this.embeds)); 
/*  67 */     if (this.components != null)
/*  68 */       data.put("components", DataArray.fromCollection(this.components)); 
/*  69 */     if (this.retainedFiles != null)
/*     */     {
/*  71 */       json.put("attachments", DataArray.fromCollection((Collection)this.retainedFiles
/*  72 */             .stream()
/*  73 */             .map(id -> DataObject.empty().put("id", id))
/*  74 */             .collect(Collectors.toList())));
/*     */     }
/*     */     
/*  77 */     json.put("data", data);
/*  78 */     return json;
/*     */   }
/*     */ 
/*     */   
/*     */   public UpdateInteractionAction applyMessage(Message message) {
/*  83 */     this.content = message.getContentRaw();
/*  84 */     this.embeds = new ArrayList<>(message.getEmbeds());
/*  85 */     this.components = new ArrayList<>(message.getActionRows());
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public UpdateInteractionAction setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/*  93 */     Checks.noneNull(embeds, "MessageEmbed");
/*  94 */     Checks.check((embeds.size() <= 10), "Cannot have more than 10 embeds per message!");
/*  95 */     for (MessageEmbed embed : embeds)
/*     */     {
/*  97 */       Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", 
/*     */           
/*  99 */           Integer.valueOf(6000));
/*     */     }
/* 101 */     if (this.embeds == null)
/* 102 */       this.embeds = new ArrayList<>(); 
/* 103 */     this.embeds.clear();
/* 104 */     this.embeds.addAll(embeds);
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public UpdateInteractionAction setActionRows(@Nonnull ActionRow... rows) {
/* 112 */     Checks.noneNull((Object[])rows, "ActionRows");
/* 113 */     Checks.check((rows.length <= 5), "Can only have 5 action rows per message!");
/* 114 */     this.components = new ArrayList<>();
/* 115 */     Collections.addAll(this.components, rows);
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public UpdateInteractionAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 123 */     Checks.notNull(data, "Data");
/* 124 */     Checks.notEmpty(name, "Name");
/* 125 */     Checks.noneNull((Object[])options, "Options");
/* 126 */     if (options.length > 0) {
/* 127 */       name = "SPOILER_" + name;
/*     */     }
/* 129 */     this.files.put(name, data);
/* 130 */     return this;
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
/*     */   @Nonnull
/*     */   public UpdateInteractionAction setContent(@Nullable String content) {
/* 148 */     if (content != null)
/* 149 */       Checks.notLonger(content, 2000, "Content"); 
/* 150 */     this.content = (content == null) ? "" : content;
/* 151 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\interactions\UpdateInteractionActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */