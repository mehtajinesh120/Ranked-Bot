/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.AllowedMentions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface WebhookMessageAction<T>
/*     */   extends RestAction<T>, AllowedMentions<WebhookMessageAction<T>>
/*     */ {
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> setEphemeral(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> setContent(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> setTTS(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> addEmbeds(@Nonnull Collection<? extends MessageEmbed> paramCollection);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> addEmbeds(@Nonnull MessageEmbed embed, @Nonnull MessageEmbed... other) {
/* 165 */     ArrayList<MessageEmbed> embeds = new ArrayList<>();
/* 166 */     embeds.add(embed);
/* 167 */     Collections.addAll(embeds, other);
/* 168 */     return addEmbeds(embeds);
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
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> addFile(@Nonnull InputStream paramInputStream, @Nonnull String paramString, @Nonnull AttachmentOption... paramVarArgs);
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
/*     */   WebhookMessageAction<T> addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 227 */     Checks.notNull(name, "Name");
/* 228 */     Checks.notNull(data, "Data");
/* 229 */     return addFile(new ByteArrayInputStream(data), name, options);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 264 */     Checks.notEmpty(name, "Name");
/* 265 */     Checks.notNull(file, "File");
/*     */     
/*     */     try {
/* 268 */       return addFile(new FileInputStream(file), name, options);
/*     */     }
/* 270 */     catch (FileNotFoundException e) {
/*     */       
/* 272 */       throw new IllegalArgumentException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> addFile(@Nonnull File file, @Nonnull AttachmentOption... options) {
/* 301 */     Checks.notNull(file, "File");
/* 302 */     return addFile(file, file.getName(), options);
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
/*     */   WebhookMessageAction<T> addActionRow(@Nonnull Component... components) {
/* 320 */     return addActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   default WebhookMessageAction<T> addActionRow(@Nonnull Collection<? extends Component> components) {
/* 338 */     return addActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   default WebhookMessageAction<T> addActionRows(@Nonnull Collection<? extends ActionRow> rows) {
/* 356 */     Checks.noneNull(rows, "ActionRows");
/* 357 */     return addActionRows(rows.<ActionRow>toArray(new ActionRow[0]));
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> addActionRows(@Nonnull ActionRow... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookMessageAction<T> applyMessage(@Nonnull Message paramMessage);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\WebhookMessageAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */