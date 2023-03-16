/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
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
/*     */ public interface WebhookMessageUpdateAction<T>
/*     */   extends RestAction<T>
/*     */ {
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> setContent(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> setEmbeds(@Nonnull Collection<? extends MessageEmbed> paramCollection);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> setEmbeds(@Nonnull MessageEmbed... embeds) {
/*  92 */     Checks.noneNull((Object[])embeds, "MessageEmbeds");
/*  93 */     return setEmbeds(Arrays.asList(embeds));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> setActionRow(@Nonnull Component... components) {
/* 111 */     return setActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default WebhookMessageUpdateAction<T> setActionRow(@Nonnull Collection<? extends Component> components) {
/* 129 */     return setActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default WebhookMessageUpdateAction<T> setActionRows(@Nonnull Collection<? extends ActionRow> rows) {
/* 147 */     Checks.noneNull(rows, "ActionRows");
/* 148 */     return setActionRows(rows.<ActionRow>toArray(new ActionRow[0]));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> setActionRows(@Nonnull ActionRow... paramVarArgs);
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
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> applyMessage(@Nonnull Message paramMessage);
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
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> addFile(@Nonnull InputStream paramInputStream, @Nonnull String paramString, @Nonnull AttachmentOption... paramVarArgs);
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
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 225 */     Checks.notNull(name, "Name");
/* 226 */     Checks.notNull(data, "Data");
/* 227 */     return addFile(new ByteArrayInputStream(data), name, options);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 263 */     Checks.notEmpty(name, "Name");
/* 264 */     Checks.notNull(file, "File");
/*     */     
/*     */     try {
/* 267 */       return addFile(new FileInputStream(file), name, options);
/*     */     }
/* 269 */     catch (FileNotFoundException e) {
/*     */       
/* 271 */       throw new IllegalArgumentException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> addFile(@Nonnull File file, @Nonnull AttachmentOption... options) {
/* 294 */     Checks.notNull(file, "File");
/* 295 */     return addFile(file, file.getName(), options);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> retainFilesById(@Nonnull Collection<String> paramCollection);
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
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> retainFilesById(@Nonnull String... ids) {
/* 334 */     Checks.notNull(ids, "IDs");
/* 335 */     return retainFilesById(Arrays.asList(ids));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageUpdateAction<T> retainFilesById(long... ids) {
/* 356 */     Checks.notNull(ids, "IDs");
/* 357 */     return retainFilesById(
/* 358 */         (Collection<String>)Arrays.stream(ids)
/* 359 */         .mapToObj(Long::toUnsignedString)
/* 360 */         .collect(Collectors.toList()));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default WebhookMessageUpdateAction<T> retainFiles(@Nonnull Collection<? extends Message.Attachment> attachments) {
/* 382 */     Checks.noneNull(attachments, "Attachments");
/* 383 */     return retainFilesById((Collection<String>)attachments
/* 384 */         .stream()
/* 385 */         .map(ISnowflake::getId)
/* 386 */         .collect(Collectors.toList()));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\WebhookMessageUpdateAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */