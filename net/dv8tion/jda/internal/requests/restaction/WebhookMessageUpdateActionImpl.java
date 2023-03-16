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
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Requester;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ 
/*     */ public class WebhookMessageUpdateActionImpl<T>
/*     */   extends TriggerRestAction<T>
/*     */   implements WebhookMessageUpdateAction<T>
/*     */ {
/*     */   private static final int CONTENT = 1;
/*     */   private static final int EMBEDS = 2;
/*     */   private static final int FILES = 4;
/*     */   private static final int COMPONENTS = 8;
/*     */   private static final int RETAINED_FILES = 16;
/*  53 */   private int set = 0;
/*  54 */   private final List<ActionRow> components = new ArrayList<>();
/*  55 */   private final List<MessageEmbed> embeds = new ArrayList<>();
/*  56 */   private final List<String> retainedFiles = new ArrayList<>();
/*  57 */   private final Map<String, InputStream> files = new HashMap<>();
/*     */   
/*     */   private final Function<DataObject, T> transformer;
/*     */   private String content;
/*     */   
/*     */   public WebhookMessageUpdateActionImpl(JDA api, Route.CompiledRoute route, Function<DataObject, T> transformer) {
/*  63 */     super(api, route);
/*  64 */     this.transformer = transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> setContent(@Nullable String content) {
/*  71 */     if (content != null)
/*  72 */       Checks.notLonger(content, 2000, "Content"); 
/*  73 */     this.content = content;
/*  74 */     this.set |= 0x1;
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/*  82 */     Checks.noneNull(embeds, "MessageEmbeds");
/*  83 */     embeds.forEach(embed -> Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", Integer.valueOf(6000)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     Checks.check((embeds.size() <= 10), "Cannot have more than 10 embeds in a message!");
/*  89 */     Checks.check((embeds.stream().mapToInt(MessageEmbed::getLength).sum() <= 6000), "The sum of all MessageEmbeds may not exceed %d!", Integer.valueOf(6000));
/*  90 */     this.embeds.clear();
/*  91 */     this.embeds.addAll(embeds);
/*  92 */     this.set |= 0x2;
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 100 */     Checks.notNull(name, "File name");
/* 101 */     Checks.notNull(data, "File data");
/* 102 */     Checks.noneNull((Object[])options, "AttachmentOptions");
/* 103 */     if (options.length > 0)
/* 104 */       name = "SPOILER_" + name; 
/* 105 */     this.files.put(name, data);
/* 106 */     this.set |= 0x4;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> retainFilesById(@Nonnull Collection<String> ids) {
/* 114 */     Checks.noneNull(ids, "IDs");
/* 115 */     ids.forEach(Checks::isSnowflake);
/* 116 */     this.retainedFiles.clear();
/* 117 */     this.retainedFiles.addAll(ids);
/* 118 */     this.set |= 0x10;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> setActionRows(@Nonnull ActionRow... rows) {
/* 126 */     Checks.noneNull((Object[])rows, "ActionRows");
/* 127 */     this.components.clear();
/* 128 */     Collections.addAll(this.components, rows);
/* 129 */     this.set |= 0x8;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookMessageUpdateAction<T> applyMessage(@Nonnull Message message) {
/* 138 */     Checks.notNull(message, "Message");
/* 139 */     setContent(message.getContentRaw());
/* 140 */     setActionRows(message.getActionRows());
/* 141 */     setEmbeds(message.getEmbeds());
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isUpdate(int flag) {
/* 147 */     return ((this.set & flag) == flag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 153 */     DataObject json = DataObject.empty();
/* 154 */     if (isUpdate(1))
/* 155 */       json.put("content", this.content); 
/* 156 */     if (isUpdate(2))
/* 157 */       json.put("embeds", DataArray.fromCollection(this.embeds)); 
/* 158 */     if (isUpdate(8))
/* 159 */       json.put("components", DataArray.fromCollection(this.components)); 
/* 160 */     if (isUpdate(16))
/*     */     {
/* 162 */       json.put("attachments", DataArray.fromCollection((Collection)this.retainedFiles
/* 163 */             .stream()
/* 164 */             .map(id -> DataObject.empty().put("id", id))
/* 165 */             .collect(Collectors.toList())));
/*     */     }
/*     */ 
/*     */     
/* 169 */     if (!isUpdate(4))
/* 170 */       return getRequestBody(json); 
/* 171 */     MultipartBody.Builder body = (new MultipartBody.Builder()).setType(MultipartBody.FORM);
/* 172 */     int i = 0;
/* 173 */     for (Map.Entry<String, InputStream> file : this.files.entrySet()) {
/*     */       
/* 175 */       RequestBody stream = IOUtil.createRequestBody(Requester.MEDIA_TYPE_OCTET, file.getValue());
/* 176 */       body.addFormDataPart("file" + i++, file.getKey(), stream);
/*     */     } 
/* 178 */     body.addFormDataPart("payload_json", json.toString());
/* 179 */     this.files.clear();
/* 180 */     return (RequestBody)body.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<T> request) {
/* 186 */     T message = this.transformer.apply(response.getObject());
/* 187 */     request.onSuccess(message);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\WebhookMessageUpdateActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */