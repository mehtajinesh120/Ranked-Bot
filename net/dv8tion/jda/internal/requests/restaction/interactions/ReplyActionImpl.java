/*     */ package net.dv8tion.jda.internal.requests.restaction.interactions;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.IMentionable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.InteractionCallbackAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.interactions.InteractionHookImpl;
/*     */ import net.dv8tion.jda.internal.utils.AllowedMentionsImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ public class ReplyActionImpl
/*     */   extends InteractionCallbackActionImpl
/*     */   implements ReplyAction
/*     */ {
/*  44 */   private final List<MessageEmbed> embeds = new ArrayList<>();
/*  45 */   private final AllowedMentionsImpl allowedMentions = new AllowedMentionsImpl();
/*  46 */   private final List<ActionRow> components = new ArrayList<>();
/*  47 */   private String content = "";
/*     */   
/*     */   private int flags;
/*     */   
/*     */   private boolean tts;
/*     */   
/*     */   public ReplyActionImpl(InteractionHookImpl hook) {
/*  54 */     super(hook);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReplyActionImpl applyMessage(Message message) {
/*  59 */     this.content = message.getContentRaw();
/*  60 */     this.tts = message.isTTS();
/*  61 */     this.embeds.addAll(message.getEmbeds());
/*  62 */     this.components.addAll(message.getActionRows());
/*  63 */     this.allowedMentions.applyMessage(message);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataObject toData() {
/*  69 */     DataObject json = DataObject.empty();
/*  70 */     if (isEmpty()) {
/*     */       
/*  72 */       json.put("type", Integer.valueOf(InteractionCallbackAction.ResponseType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE.getRaw()));
/*  73 */       if (this.flags != 0) {
/*  74 */         json.put("data", DataObject.empty().put("flags", Integer.valueOf(this.flags)));
/*     */       }
/*     */     } else {
/*     */       
/*  78 */       DataObject payload = DataObject.empty();
/*  79 */       payload.put("allowed_mentions", this.allowedMentions);
/*  80 */       payload.put("content", this.content);
/*  81 */       payload.put("tts", Boolean.valueOf(this.tts));
/*  82 */       payload.put("flags", Integer.valueOf(this.flags));
/*  83 */       if (!this.embeds.isEmpty())
/*  84 */         payload.put("embeds", DataArray.fromCollection(this.embeds)); 
/*  85 */       if (!this.components.isEmpty())
/*  86 */         payload.put("components", DataArray.fromCollection(this.components)); 
/*  87 */       json.put("data", payload);
/*     */       
/*  89 */       json.put("type", Integer.valueOf(InteractionCallbackAction.ResponseType.CHANNEL_MESSAGE_WITH_SOURCE.getRaw()));
/*     */     } 
/*  91 */     return json;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isEmpty() {
/*  96 */     return (Helpers.isEmpty(this.content) && this.embeds.isEmpty() && this.files.isEmpty() && this.components.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyActionImpl setEphemeral(boolean ephemeral) {
/* 103 */     if (ephemeral) {
/* 104 */       this.flags |= 0x40;
/*     */     } else {
/* 106 */       this.flags &= 0xFFFFFFBF;
/* 107 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 114 */     Checks.notNull(data, "Data");
/* 115 */     Checks.notEmpty(name, "Name");
/* 116 */     Checks.noneNull((Object[])options, "Options");
/* 117 */     if (options.length > 0) {
/* 118 */       name = "SPOILER_" + name;
/*     */     }
/* 120 */     this.files.put(name, data);
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction addEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/* 128 */     Checks.noneNull(embeds, "MessageEmbed");
/* 129 */     for (MessageEmbed embed : embeds)
/*     */     {
/* 131 */       Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", 
/*     */           
/* 133 */           Integer.valueOf(6000));
/*     */     }
/*     */     
/* 136 */     if (embeds.size() + this.embeds.size() > 10)
/* 137 */       throw new IllegalStateException("Cannot have more than 10 embeds per message!"); 
/* 138 */     this.embeds.addAll(embeds);
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction addActionRows(@Nonnull ActionRow... rows) {
/* 146 */     Checks.noneNull((Object[])rows, "ActionRows");
/* 147 */     Checks.check((this.components.size() + rows.length <= 5), "Can only have 5 action rows per message!");
/* 148 */     Collections.addAll(this.components, rows);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction setCheck(BooleanSupplier checks) {
/* 156 */     return (ReplyAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction timeout(long timeout, @Nonnull TimeUnit unit) {
/* 163 */     return (ReplyAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction deadline(long timestamp) {
/* 170 */     return (ReplyAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyActionImpl setTTS(boolean isTTS) {
/* 177 */     this.tts = isTTS;
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyActionImpl setContent(String content) {
/* 185 */     if (content != null)
/* 186 */       Checks.notLonger(content, 2000, "Content"); 
/* 187 */     this.content = (content == null) ? "" : content;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction mentionRepliedUser(boolean mention) {
/* 196 */     this.allowedMentions.mentionRepliedUser(mention);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction allowedMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
/* 205 */     this.allowedMentions.allowedMentions(allowedMentions);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction mention(@Nonnull IMentionable... mentions) {
/* 214 */     this.allowedMentions.mention(mentions);
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction mentionUsers(@Nonnull String... userIds) {
/* 223 */     this.allowedMentions.mentionUsers(userIds);
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction mentionRoles(@Nonnull String... roleIds) {
/* 232 */     this.allowedMentions.mentionRoles(roleIds);
/* 233 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\interactions\ReplyActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */