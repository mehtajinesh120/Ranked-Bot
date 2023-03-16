/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.AllowedMentions;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;
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
/*     */ public interface MessageAction
/*     */   extends RestAction<Message>, Appendable, AllowedMentions<MessageAction>
/*     */ {
/*     */   static void setDefaultMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
/* 119 */     AllowedMentions.setDefaultMentions(allowedMentions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   static EnumSet<Message.MentionType> getDefaultMentions() {
/* 131 */     return AllowedMentions.getDefaultMentions();
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
/*     */   static void setDefaultMentionRepliedUser(boolean mention) {
/* 144 */     AllowedMentions.setDefaultMentionRepliedUser(mention);
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
/*     */   static boolean isDefaultMentionRepliedUser() {
/* 158 */     return AllowedMentions.isDefaultMentionRepliedUser();
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
/*     */   static void setDefaultFailOnInvalidReply(boolean fail) {
/* 171 */     MessageActionImpl.setDefaultFailOnInvalidReply(fail);
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
/*     */   static boolean isDefaultFailOnInvalidReply() {
/* 185 */     return MessageActionImpl.isDefaultFailOnInvalidReply();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   default MessageAction referenceById(@Nonnull String messageId) {
/* 298 */     return referenceById(MiscUtil.parseSnowflake(messageId));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default MessageAction reference(@Nonnull Message message) {
/* 327 */     Checks.notNull(message, "Message");
/* 328 */     return referenceById(message.getIdLong());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   MessageAction setEmbeds(@Nonnull MessageEmbed... embeds) {
/* 493 */     Checks.noneNull((Object[])embeds, "MessageEmbeds");
/* 494 */     return setEmbeds(Arrays.asList(embeds));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default MessageAction append(@Nonnull CharSequence csq) {
/* 510 */     return append(csq, 0, csq.length());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   MessageAction appendFormat(@Nonnull String format, Object... args) {
/* 567 */     return append(String.format(format, args));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   MessageAction addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 630 */     Checks.notNull(data, "Data");
/* 631 */     long maxSize = getJDA().getSelfUser().getAllowedFileSize();
/* 632 */     Checks.check((data.length <= maxSize), "File may not exceed the maximum file length of %d bytes!", Long.valueOf(maxSize));
/* 633 */     return addFile(new ByteArrayInputStream(data), name, options);
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
/*     */   MessageAction addFile(@Nonnull File file, @Nonnull AttachmentOption... options) {
/* 661 */     Checks.notNull(file, "File");
/* 662 */     return addFile(file, file.getName(), options);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   MessageAction retainFilesById(@Nonnull String... ids) {
/* 778 */     Checks.notNull(ids, "IDs");
/* 779 */     return retainFilesById(Arrays.asList(ids));
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
/*     */   MessageAction retainFilesById(long... ids) {
/* 800 */     Checks.notNull(ids, "IDs");
/* 801 */     return retainFilesById(
/* 802 */         (Collection<String>)Arrays.stream(ids)
/* 803 */         .mapToObj(Long::toUnsignedString)
/* 804 */         .collect(Collectors.toList()));
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
/*     */   default MessageAction retainFiles(@Nonnull Collection<? extends Message.Attachment> attachments) {
/* 826 */     Checks.noneNull(attachments, "Attachments");
/* 827 */     return retainFilesById((Collection<String>)attachments
/* 828 */         .stream()
/* 829 */         .map(ISnowflake::getId)
/* 830 */         .collect(Collectors.toList()));
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
/*     */   @CheckReturnValue
/*     */   default MessageAction setActionRows(@Nonnull Collection<? extends ActionRow> rows) {
/* 849 */     Checks.noneNull(rows, "ActionRows");
/* 850 */     return setActionRows(rows.<ActionRow>toArray(new ActionRow[0]));
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
/*     */   default MessageAction setActionRow(@Nonnull Collection<? extends Component> components) {
/* 886 */     return setActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   MessageAction setActionRow(@Nonnull Component... components) {
/* 907 */     return setActionRows(new ActionRow[] { ActionRow.of(components) });
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   MessageAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*     */   
/*     */   @Nonnull
/*     */   MessageAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*     */   
/*     */   @Nonnull
/*     */   MessageAction deadline(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   MessageChannel getChannel();
/*     */   
/*     */   boolean isEmpty();
/*     */   
/*     */   boolean isEdit();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction apply(@Nullable Message paramMessage);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction referenceById(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction mentionRepliedUser(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction failOnInvalidReply(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction tts(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction reset();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction nonce(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction content(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @ReplaceWith("setEmbeds(embed)")
/*     */   @DeprecatedSince("4.4.0")
/*     */   MessageAction embed(@Nullable MessageEmbed paramMessageEmbed);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction setEmbeds(@Nonnull Collection<? extends MessageEmbed> paramCollection);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction append(@Nullable CharSequence paramCharSequence, int paramInt1, int paramInt2);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction append(char paramChar);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction addFile(@Nonnull InputStream paramInputStream, @Nonnull String paramString, @Nonnull AttachmentOption... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction addFile(@Nonnull File paramFile, @Nonnull String paramString, @Nonnull AttachmentOption... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction clearFiles();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction clearFiles(@Nonnull BiConsumer<String, InputStream> paramBiConsumer);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction clearFiles(@Nonnull Consumer<InputStream> paramConsumer);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction retainFilesById(@Nonnull Collection<String> paramCollection);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction setActionRows(@Nonnull ActionRow... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   MessageAction override(boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\MessageAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */