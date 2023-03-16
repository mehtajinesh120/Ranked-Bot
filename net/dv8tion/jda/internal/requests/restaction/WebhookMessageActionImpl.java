/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.IMentionable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Requester;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.AllowedMentionsImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
/*     */ import okhttp3.MultipartBody;
/*     */ import okhttp3.RequestBody;
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
/*     */ public class WebhookMessageActionImpl<T>
/*     */   extends TriggerRestAction<T>
/*     */   implements WebhookMessageAction<T>
/*     */ {
/*  50 */   private final StringBuilder content = new StringBuilder();
/*  51 */   private final List<MessageEmbed> embeds = new ArrayList<>();
/*  52 */   private final Map<String, InputStream> files = new HashMap<>();
/*  53 */   private final AllowedMentionsImpl allowedMentions = new AllowedMentionsImpl();
/*  54 */   private final List<ActionRow> components = new ArrayList<>();
/*     */   private final MessageChannel channel;
/*     */   private final Function<DataObject, T> transformer;
/*     */   private boolean ephemeral;
/*     */   private boolean tts;
/*     */   private String username;
/*     */   private String avatarUrl;
/*     */   
/*     */   public WebhookMessageActionImpl(JDA api, MessageChannel channel, Route.CompiledRoute route, Function<DataObject, T> transformer) {
/*  63 */     super(api, route);
/*  64 */     this.channel = channel;
/*  65 */     this.transformer = transformer;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> applyMessage(@Nonnull Message message) {
/*  71 */     Checks.notNull(message, "Message");
/*  72 */     this.tts = message.isTTS();
/*  73 */     this.embeds.addAll(message.getEmbeds());
/*  74 */     this.allowedMentions.applyMessage(message);
/*  75 */     this.components.addAll(message.getActionRows());
/*  76 */     return setContent(message.getContentRaw());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> setEphemeral(boolean ephemeral) {
/*  83 */     this.ephemeral = ephemeral;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> setContent(@Nullable String content) {
/*  91 */     this.content.setLength(0);
/*  92 */     if (content != null)
/*  93 */       this.content.append(content); 
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> setTTS(boolean tts) {
/* 101 */     this.tts = tts;
/* 102 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> addEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/* 132 */     Checks.noneNull(embeds, "MessageEmbeds");
/* 133 */     embeds.forEach(embed -> Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", Integer.valueOf(6000)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     Checks.check((this.embeds.size() + embeds.size() <= 10), "Cannot have more than 10 embeds in a message!");
/* 139 */     Checks.check((Stream.concat(embeds.stream(), this.embeds.stream()).mapToInt(MessageEmbed::getLength).sum() <= 6000), "The sum of all MessageEmbeds may not exceed %d!", Integer.valueOf(6000));
/* 140 */     this.embeds.addAll(embeds);
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 148 */     Checks.notNull(name, "Name");
/* 149 */     Checks.notNull(data, "Data");
/* 150 */     Checks.notNull(options, "AttachmentOption");
/*     */     
/* 152 */     Checks.check((this.files.size() < 10), "Cannot have more than 10 files in a message!");
/* 153 */     if (options.length > 0 && options[0] == AttachmentOption.SPOILER)
/* 154 */       name = "SPOILER_" + name; 
/* 155 */     this.files.put(name, data);
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> addActionRows(@Nonnull ActionRow... rows) {
/* 163 */     Checks.noneNull((Object[])rows, "ActionRows");
/* 164 */     Checks.check((rows.length + this.components.size() <= 5), "Can only have 5 action rows per message!");
/* 165 */     Collections.addAll(this.components, rows);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private DataObject toData() {
/* 171 */     DataObject data = DataObject.empty();
/* 172 */     data.put("content", this.content.toString());
/* 173 */     data.put("tts", Boolean.valueOf(this.tts));
/*     */     
/* 175 */     if (this.username != null)
/* 176 */       data.put("username", this.username); 
/* 177 */     if (this.avatarUrl != null)
/* 178 */       data.put("avatar_url", this.avatarUrl); 
/* 179 */     if (this.ephemeral)
/* 180 */       data.put("flags", Integer.valueOf(64)); 
/* 181 */     if (!this.embeds.isEmpty())
/* 182 */       data.put("embeds", DataArray.fromCollection(this.embeds)); 
/* 183 */     if (!this.components.isEmpty())
/* 184 */       data.put("components", DataArray.fromCollection(this.components)); 
/* 185 */     data.put("allowed_mentions", this.allowedMentions);
/* 186 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 192 */     DataObject data = toData();
/* 193 */     if (this.files.isEmpty()) {
/* 194 */       return getRequestBody(data);
/*     */     }
/* 196 */     MultipartBody.Builder body = (new MultipartBody.Builder()).setType(MultipartBody.FORM);
/* 197 */     int i = 0;
/* 198 */     for (Map.Entry<String, InputStream> file : this.files.entrySet()) {
/*     */       
/* 200 */       RequestBody stream = IOUtil.createRequestBody(Requester.MEDIA_TYPE_OCTET, file.getValue());
/* 201 */       body.addFormDataPart("file" + i++, file.getKey(), stream);
/*     */     } 
/*     */     
/* 204 */     body.addFormDataPart("payload_json", data.toString());
/* 205 */     this.files.clear();
/* 206 */     return (RequestBody)body.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<T> request) {
/* 212 */     T message = this.transformer.apply(response.getObject());
/* 213 */     request.onSuccess(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> mentionRepliedUser(boolean mention) {
/* 221 */     this.allowedMentions.mentionRepliedUser(mention);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> allowedMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
/* 230 */     this.allowedMentions.allowedMentions(allowedMentions);
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> mention(@Nonnull IMentionable... mentions) {
/* 239 */     this.allowedMentions.mention(mentions);
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> mentionUsers(@Nonnull String... userIds) {
/* 248 */     this.allowedMentions.mentionUsers(userIds);
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageActionImpl<T> mentionRoles(@Nonnull String... roleIds) {
/* 257 */     this.allowedMentions.mentionRoles(roleIds);
/* 258 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\WebhookMessageActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */