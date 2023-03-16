/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.EmbedType;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IMentionable;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Requester;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.AllowedMentionsImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
/*     */ import okhttp3.MultipartBody;
/*     */ import okhttp3.RequestBody;
/*     */ 
/*     */ public class MessageActionImpl extends RestActionImpl<Message> implements MessageAction {
/*  55 */   private static final String CONTENT_TOO_BIG = Helpers.format("A message may not exceed %d characters. Please limit your input!", new Object[] { Integer.valueOf(2000) });
/*     */   protected static boolean defaultFailOnInvalidReply = false;
/*  57 */   protected final Map<String, InputStream> files = new HashMap<>();
/*  58 */   protected final Set<InputStream> ownedResources = new HashSet<>();
/*     */   protected final StringBuilder content;
/*     */   protected final MessageChannel channel;
/*  61 */   protected final AllowedMentionsImpl allowedMentions = new AllowedMentionsImpl();
/*     */   protected List<ActionRow> components;
/*     */   protected List<String> retainedAttachments;
/*  64 */   protected List<MessageEmbed> embeds = null;
/*  65 */   protected String nonce = null;
/*     */   protected boolean tts = false, override = false;
/*  67 */   protected boolean failOnInvalidReply = defaultFailOnInvalidReply;
/*     */   
/*     */   protected long messageReference;
/*     */   protected final String messageId;
/*  71 */   private InteractionHook hook = null;
/*     */ 
/*     */   
/*     */   public static void setDefaultFailOnInvalidReply(boolean fail) {
/*  75 */     defaultFailOnInvalidReply = fail;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isDefaultFailOnInvalidReply() {
/*  80 */     return defaultFailOnInvalidReply;
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageActionImpl(JDA api, String messageId, MessageChannel channel) {
/*  85 */     super(api, (messageId != null) ? 
/*  86 */         Route.Messages.EDIT_MESSAGE.compile(new String[] { channel.getId(), messageId
/*  87 */           }) : Route.Messages.SEND_MESSAGE.compile(new String[] { channel.getId() }));
/*  88 */     this.content = new StringBuilder();
/*  89 */     this.channel = channel;
/*  90 */     this.messageId = messageId;
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageActionImpl(JDA api, String messageId, MessageChannel channel, StringBuilder contentBuilder) {
/*  95 */     super(api, (messageId != null) ? 
/*  96 */         Route.Messages.EDIT_MESSAGE.compile(new String[] { channel.getId(), messageId
/*  97 */           }) : Route.Messages.SEND_MESSAGE.compile(new String[] { channel.getId() }));
/*  98 */     Checks.check((contentBuilder.length() <= 2000), "Cannot build a Message with more than %d characters. Please limit your input.", 
/*  99 */         Integer.valueOf(2000));
/* 100 */     this.content = contentBuilder;
/* 101 */     this.channel = channel;
/* 102 */     this.messageId = messageId;
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageActionImpl withHook(InteractionHook hook) {
/* 107 */     this.hook = hook;
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Route.CompiledRoute finalizeRoute() {
/* 114 */     if (this.hook == null || this.hook.isExpired())
/* 115 */       return super.finalizeRoute(); 
/* 116 */     if (isEdit()) {
/* 117 */       return Route.Interactions.EDIT_FOLLOWUP.compile(new String[] { this.api.getSelfUser().getApplicationId(), this.hook.getInteraction().getToken(), this.messageId });
/*     */     }
/* 119 */     return Route.Interactions.CREATE_FOLLOWUP.compile(new String[] { this.api.getSelfUser().getApplicationId(), this.hook.getInteraction().getToken() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction setCheck(BooleanSupplier checks) {
/* 126 */     return (MessageAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction timeout(long timeout, @Nonnull TimeUnit unit) {
/* 133 */     return (MessageAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction deadline(long timestamp) {
/* 140 */     return (MessageAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageChannel getChannel() {
/* 147 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 153 */     return (!isEdit() && 
/* 154 */       Helpers.isBlank(this.content) && (this.embeds == null || this.embeds
/* 155 */       .isEmpty() || !hasPermission(Permission.MESSAGE_EMBED_LINKS)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEdit() {
/* 161 */     return (this.messageId != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl apply(Message message) {
/* 170 */     if (message == null || message.getType().isSystem())
/* 171 */       return this; 
/* 172 */     List<MessageEmbed> embeds = message.getEmbeds();
/* 173 */     if (embeds != null && !embeds.isEmpty())
/* 174 */       setEmbeds((Collection<? extends MessageEmbed>)embeds.stream().filter(e -> (e != null && e.getType() == EmbedType.RICH)).collect(Collectors.toList())); 
/* 175 */     this.files.clear();
/*     */     
/* 177 */     this.components = new ArrayList<>();
/* 178 */     this.components.addAll(message.getActionRows());
/* 179 */     this.allowedMentions.applyMessage(message);
/* 180 */     String content = message.getContentRaw();
/* 181 */     return content(content).tts(message.isTTS());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl referenceById(long messageId) {
/* 188 */     this.messageReference = messageId;
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl failOnInvalidReply(boolean fail) {
/* 196 */     this.failOnInvalidReply = fail;
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl tts(boolean isTTS) {
/* 205 */     this.tts = isTTS;
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl reset() {
/* 214 */     return content((String)null).nonce((String)null).setEmbeds(Collections.emptyList()).tts(false).override(false).clearFiles();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl nonce(String nonce) {
/* 222 */     this.nonce = nonce;
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl content(String content) {
/* 231 */     if (content == null || content.isEmpty()) {
/* 232 */       this.content.setLength(0);
/* 233 */     } else if (content.length() <= 2000) {
/* 234 */       this.content.replace(0, this.content.length(), content);
/*     */     } else {
/* 236 */       throw new IllegalArgumentException(CONTENT_TOO_BIG);
/* 237 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @Deprecated
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl embed(MessageEmbed embed) {
/* 246 */     if (embed != null) {
/*     */       
/* 248 */       Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", 
/*     */           
/* 250 */           Integer.valueOf(6000));
/* 251 */       if (this.embeds == null)
/* 252 */         this.embeds = new ArrayList<>(); 
/* 253 */       this.embeds.add(embed);
/*     */     }
/*     */     else {
/*     */       
/* 257 */       this.embeds = null;
/*     */     } 
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/* 266 */     Checks.noneNull(embeds, "MessageEmbeds");
/* 267 */     embeds.forEach(embed -> Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", Integer.valueOf(6000)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     Checks.check((embeds.size() <= 10), "Cannot have more than 10 embeds in a message!");
/* 273 */     Checks.check((embeds.stream().mapToInt(MessageEmbed::getLength).sum() <= 6000), "The sum of all MessageEmbeds may not exceed %d!", Integer.valueOf(6000));
/* 274 */     if (this.embeds == null)
/* 275 */       this.embeds = new ArrayList<>(); 
/* 276 */     this.embeds.clear();
/* 277 */     this.embeds.addAll(embeds);
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl append(CharSequence csq, int start, int end) {
/* 286 */     if (this.content.length() + end - start > 2000)
/* 287 */       throw new IllegalArgumentException("A message may not exceed 2000 characters. Please limit your input!"); 
/* 288 */     this.content.append(csq, start, end);
/* 289 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl append(char c) {
/* 297 */     if (this.content.length() == 2000)
/* 298 */       throw new IllegalArgumentException("A message may not exceed 2000 characters. Please limit your input!"); 
/* 299 */     this.content.append(c);
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl addFile(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 308 */     Checks.notNull(data, "Data");
/* 309 */     Checks.notBlank(name, "Name");
/* 310 */     Checks.noneNull((Object[])options, "Options");
/* 311 */     checkFileAmount();
/* 312 */     checkPermission(Permission.MESSAGE_ATTACH_FILES);
/* 313 */     name = applyOptions(name, options);
/* 314 */     this.files.put(name, data);
/* 315 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 323 */     Checks.notNull(file, "File");
/* 324 */     Checks.noneNull((Object[])options, "Options");
/* 325 */     Checks.check((file.exists() && file.canRead()), "Provided file either does not exist or cannot be read from!");
/* 326 */     long maxSize = getMaxFileSize();
/* 327 */     Checks.check((file.length() <= maxSize), "File may not exceed the maximum file length of %d bytes!", Long.valueOf(maxSize));
/*     */     
/*     */     try {
/* 330 */       FileInputStream data = new FileInputStream(file);
/* 331 */       this.ownedResources.add(data);
/* 332 */       name = applyOptions(name, options);
/* 333 */       return addFile(data, name, new AttachmentOption[0]);
/*     */     }
/* 335 */     catch (FileNotFoundException e) {
/*     */       
/* 337 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl clearFiles() {
/* 346 */     this.files.clear();
/* 347 */     clearResources();
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl clearFiles(@Nonnull BiConsumer<String, InputStream> finalizer) {
/* 356 */     Checks.notNull(finalizer, "Finalizer");
/* 357 */     for (Iterator<Map.Entry<String, InputStream>> it = this.files.entrySet().iterator(); it.hasNext(); ) {
/*     */       
/* 359 */       Map.Entry<String, InputStream> entry = it.next();
/* 360 */       finalizer.accept(entry.getKey(), entry.getValue());
/* 361 */       it.remove();
/*     */     } 
/* 363 */     clearResources();
/* 364 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl clearFiles(@Nonnull Consumer<InputStream> finalizer) {
/* 372 */     Checks.notNull(finalizer, "Finalizer");
/* 373 */     for (Iterator<InputStream> it = this.files.values().iterator(); it.hasNext(); ) {
/*     */       
/* 375 */       finalizer.accept(it.next());
/* 376 */       it.remove();
/*     */     } 
/* 378 */     clearResources();
/* 379 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl retainFilesById(@Nonnull Collection<String> ids) {
/* 386 */     if (!isEdit()) return this; 
/* 387 */     if (this.retainedAttachments == null)
/* 388 */       this.retainedAttachments = new ArrayList<>(); 
/* 389 */     this.retainedAttachments.addAll(ids);
/* 390 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl setActionRows(@Nonnull ActionRow... rows) {
/* 397 */     Checks.noneNull((Object[])rows, "ActionRows");
/* 398 */     if (this.components == null)
/* 399 */       this.components = new ArrayList<>(); 
/* 400 */     Checks.check((rows.length <= 5), "Can only have 5 action rows per message!");
/* 401 */     this.components.clear();
/* 402 */     Collections.addAll(this.components, rows);
/* 403 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MessageActionImpl override(boolean bool) {
/* 411 */     this.override = (isEdit() && bool);
/* 412 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl mentionRepliedUser(boolean mention) {
/* 420 */     this.allowedMentions.mentionRepliedUser(mention);
/* 421 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl allowedMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
/* 429 */     this.allowedMentions.allowedMentions(allowedMentions);
/* 430 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl mention(@Nonnull IMentionable... mentions) {
/* 438 */     this.allowedMentions.mention(mentions);
/* 439 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl mentionUsers(@Nonnull String... userIds) {
/* 447 */     this.allowedMentions.mentionUsers(userIds);
/* 448 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageActionImpl mentionRoles(@Nonnull String... roleIds) {
/* 456 */     this.allowedMentions.mentionRoles(roleIds);
/* 457 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private String applyOptions(String name, AttachmentOption[] options) {
/* 462 */     for (AttachmentOption opt : options) {
/*     */       
/* 464 */       if (opt == AttachmentOption.SPOILER) {
/*     */         
/* 466 */         name = "SPOILER_" + name;
/*     */         break;
/*     */       } 
/*     */     } 
/* 470 */     return name;
/*     */   }
/*     */ 
/*     */   
/*     */   private void clearResources() {
/* 475 */     for (InputStream ownedResource : this.ownedResources) {
/*     */ 
/*     */       
/*     */       try {
/* 479 */         ownedResource.close();
/*     */       }
/* 481 */       catch (IOException ex) {
/*     */         
/* 483 */         if (!ex.getMessage().toLowerCase().contains("closed"))
/* 484 */           LOG.error("Encountered IOException trying to close owned resource", ex); 
/*     */       } 
/*     */     } 
/* 487 */     this.ownedResources.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private long getMaxFileSize() {
/* 492 */     if (this.channel.getType().isGuild())
/* 493 */       return ((GuildChannel)this.channel).getGuild().getMaxFileSize(); 
/* 494 */     return getJDA().getSelfUser().getAllowedFileSize();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestBody asMultipart() {
/* 499 */     MultipartBody.Builder builder = (new MultipartBody.Builder()).setType(MultipartBody.FORM);
/* 500 */     int index = 0;
/* 501 */     for (Map.Entry<String, InputStream> entry : this.files.entrySet()) {
/*     */       
/* 503 */       RequestBody body = IOUtil.createRequestBody(Requester.MEDIA_TYPE_OCTET, entry.getValue());
/* 504 */       builder.addFormDataPart("file" + index++, entry.getKey(), body);
/*     */     } 
/* 506 */     if (this.messageReference != 0L || this.components != null || this.retainedAttachments != null || !isEmpty()) {
/* 507 */       builder.addFormDataPart("payload_json", getJSON().toString());
/*     */     }
/* 509 */     this.files.clear();
/* 510 */     this.ownedResources.clear();
/* 511 */     return (RequestBody)builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestBody asJSON() {
/* 516 */     return RequestBody.create(Requester.MEDIA_TYPE_JSON, getJSON().toJson());
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataObject getJSON() {
/* 521 */     DataObject obj = DataObject.empty();
/* 522 */     if (this.override) {
/*     */       
/* 524 */       if (this.embeds == null) {
/* 525 */         obj.put("embeds", DataArray.empty());
/*     */       } else {
/* 527 */         obj.put("embeds", DataArray.fromCollection(this.embeds));
/* 528 */       }  if (this.content.length() == 0) {
/* 529 */         obj.putNull("content");
/*     */       } else {
/* 531 */         obj.put("content", this.content.toString());
/* 532 */       }  if (this.nonce == null) {
/* 533 */         obj.putNull("nonce");
/*     */       } else {
/* 535 */         obj.put("nonce", this.nonce);
/* 536 */       }  if (this.components == null) {
/* 537 */         obj.put("components", DataArray.empty());
/*     */       } else {
/* 539 */         obj.put("components", DataArray.fromCollection(this.components));
/* 540 */       }  if (this.retainedAttachments != null) {
/* 541 */         obj.put("attachments", DataArray.fromCollection((Collection)this.retainedAttachments.stream()
/* 542 */               .map(id -> DataObject.empty().put("id", id))
/*     */               
/* 544 */               .collect(Collectors.toList())));
/*     */       } else {
/* 546 */         obj.put("attachments", DataArray.empty());
/*     */       } 
/*     */     } else {
/*     */       
/* 550 */       if (this.embeds != null)
/* 551 */         obj.put("embeds", DataArray.fromCollection(this.embeds)); 
/* 552 */       if (this.content.length() > 0)
/* 553 */         obj.put("content", this.content.toString()); 
/* 554 */       if (this.nonce != null)
/* 555 */         obj.put("nonce", this.nonce); 
/* 556 */       if (this.components != null)
/* 557 */         obj.put("components", DataArray.fromCollection(this.components)); 
/* 558 */       if (this.retainedAttachments != null)
/* 559 */         obj.put("attachments", DataArray.fromCollection((Collection)this.retainedAttachments.stream()
/* 560 */               .map(id -> DataObject.empty().put("id", id))
/*     */               
/* 562 */               .collect(Collectors.toList()))); 
/*     */     } 
/* 564 */     if (this.messageReference != 0L)
/*     */     {
/* 566 */       obj.put("message_reference", DataObject.empty()
/* 567 */           .put("message_id", Long.valueOf(this.messageReference))
/* 568 */           .put("channel_id", this.channel.getId())
/* 569 */           .put("fail_if_not_exists", Boolean.valueOf(this.failOnInvalidReply)));
/*     */     }
/* 571 */     obj.put("tts", Boolean.valueOf(this.tts));
/* 572 */     obj.put("allowed_mentions", this.allowedMentions);
/* 573 */     return obj;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkFileAmount() {
/* 578 */     if (this.files.size() >= 10) {
/* 579 */       throw new IllegalStateException("Cannot add more than 10 files!");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkEdit() {
/* 584 */     if (isEdit()) {
/* 585 */       throw new IllegalStateException("Cannot add files to an existing message! Edit-Message does not support this operation!");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkPermission(Permission perm) {
/* 590 */     if (!this.channel.getType().isGuild())
/*     */       return; 
/* 592 */     GuildChannel gc = (GuildChannel)this.channel;
/* 593 */     if (!gc.getGuild().getSelfMember().hasAccess(gc))
/* 594 */       throw new MissingAccessException(gc, Permission.VIEW_CHANNEL); 
/* 595 */     if (!hasPermission(perm)) {
/* 596 */       throw new InsufficientPermissionException(gc, perm);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean hasPermission(Permission perm) {
/* 601 */     if (this.channel.getType() != ChannelType.TEXT)
/* 602 */       return true; 
/* 603 */     TextChannel text = (TextChannel)this.channel;
/* 604 */     Member self = text.getGuild().getSelfMember();
/* 605 */     return self.hasPermission((GuildChannel)text, new Permission[] { perm });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 611 */     if (!this.files.isEmpty())
/* 612 */       return asMultipart(); 
/* 613 */     if (!isEmpty())
/* 614 */       return asJSON(); 
/* 615 */     if (this.embeds != null && !this.embeds.isEmpty() && this.channel instanceof GuildChannel)
/* 616 */       throw new InsufficientPermissionException((GuildChannel)this.channel, Permission.MESSAGE_EMBED_LINKS, "Cannot send message with only embeds without Permission.MESSAGE_EMBED_LINKS!"); 
/* 617 */     throw new IllegalStateException("Cannot build a message without content!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Message> request) {
/* 623 */     request.onSuccess(this.api.getEntityBuilder().createMessage(response.getObject(), this.channel, false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() {
/* 630 */     if (this.ownedResources.isEmpty())
/*     */       return; 
/* 632 */     LOG.warn("Found unclosed resources in MessageAction instance, closing on finalization step!");
/* 633 */     clearResources();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\MessageActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */