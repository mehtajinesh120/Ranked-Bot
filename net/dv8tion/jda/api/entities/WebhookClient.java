/*      */ package net.dv8tion.jda.api.entities;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.WebhookMessageUpdateAction;
/*      */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public interface WebhookClient<T>
/*      */ {
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendMessage(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendMessage(@Nonnull Message paramMessage);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendMessageFormat(@Nonnull String format, @Nonnull Object... args) {
/*  113 */     Checks.notNull(format, "Format String");
/*  114 */     return sendMessage(String.format(format, args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> paramCollection);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendMessageEmbeds(@Nonnull MessageEmbed embed, @Nonnull MessageEmbed... embeds) {
/*  165 */     Checks.notNull(embed, "MessageEmbeds");
/*  166 */     Checks.noneNull((Object[])embeds, "MessageEmbeds");
/*  167 */     List<MessageEmbed> embedList = new ArrayList<>();
/*  168 */     embedList.add(embed);
/*  169 */     Collections.addAll(embedList, embeds);
/*  170 */     return sendMessageEmbeds(embedList);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendFile(@Nonnull InputStream paramInputStream, @Nonnull String paramString, @Nonnull AttachmentOption... paramVarArgs);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendFile(@Nonnull File file, @Nonnull AttachmentOption... options) {
/*  259 */     Checks.notNull(file, "File");
/*  260 */     return sendFile(file, file.getName(), options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/*  317 */     Checks.notNull(file, "File");
/*  318 */     Checks.check((file.exists() && file.canRead()), "Provided file doesn't exist or cannot be read!");
/*      */     
/*  320 */     Checks.notNull(name, "Name");
/*      */ 
/*      */     
/*      */     try {
/*  324 */       return sendFile(new FileInputStream(file), name, options);
/*      */     }
/*  326 */     catch (FileNotFoundException ex) {
/*      */       
/*  328 */       throw new IllegalArgumentException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageAction<T> sendFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/*  375 */     Checks.notNull(data, "Data");
/*  376 */     Checks.notNull(name, "Name");
/*  377 */     return sendFile(new ByteArrayInputStream(data), name, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(@Nonnull String paramString1, @Nonnull String paramString2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default WebhookMessageUpdateAction<T> editMessageById(long messageId, @Nonnull String content) {
/*  434 */     return editMessageById(Long.toUnsignedString(messageId), content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(@Nonnull String paramString, @Nonnull Message paramMessage);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default WebhookMessageUpdateAction<T> editMessageById(long messageId, Message message) {
/*  491 */     return editMessageById(Long.toUnsignedString(messageId), message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageFormatById(@Nonnull String messageId, @Nonnull String format, @Nonnull Object... args) {
/*  523 */     Checks.notNull(format, "Format String");
/*  524 */     return editMessageById(messageId, String.format(format, args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageFormatById(long messageId, @Nonnull String format, @Nonnull Object... args) {
/*  556 */     return editMessageFormatById(Long.toUnsignedString(messageId), format, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageEmbedsById(@Nonnull String paramString, @Nonnull Collection<? extends MessageEmbed> paramCollection);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default WebhookMessageUpdateAction<T> editMessageEmbedsById(long messageId, @Nonnull Collection<? extends MessageEmbed> embeds) {
/*  613 */     return editMessageEmbedsById(Long.toUnsignedString(messageId), embeds);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageEmbedsById(@Nonnull String messageId, @Nonnull MessageEmbed... embeds) {
/*  643 */     Checks.noneNull((Object[])embeds, "MessageEmbeds");
/*  644 */     return editMessageEmbedsById(messageId, Arrays.asList(embeds));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageEmbedsById(long messageId, @Nonnull MessageEmbed... embeds) {
/*  674 */     return editMessageEmbedsById(Long.toUnsignedString(messageId), embeds);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageComponentsById(@Nonnull String paramString, @Nonnull Collection<? extends ComponentLayout> paramCollection);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default WebhookMessageUpdateAction<T> editMessageComponentsById(long messageId, @Nonnull Collection<? extends ComponentLayout> components) {
/*  731 */     return editMessageComponentsById(Long.toUnsignedString(messageId), components);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageComponentsById(@Nonnull String messageId, @Nonnull ComponentLayout... components) {
/*  761 */     Checks.noneNull((Object[])components, "ComponentLayouts");
/*  762 */     return editMessageComponentsById(messageId, Arrays.asList(components));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageComponentsById(long messageId, @Nonnull ComponentLayout... components) {
/*  792 */     return editMessageComponentsById(Long.toUnsignedString(messageId), components);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(@Nonnull String paramString1, @Nonnull InputStream paramInputStream, @Nonnull String paramString2, @Nonnull AttachmentOption... paramVarArgs);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(@Nonnull String messageId, @Nonnull File file, @Nonnull AttachmentOption... options) {
/*  888 */     Checks.notNull(file, "File");
/*  889 */     return editMessageById(messageId, file, file.getName(), options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(@Nonnull String messageId, @Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/*  938 */     Checks.notNull(file, "File");
/*  939 */     Checks.check((file.exists() && file.canRead()), "Provided file doesn't exist or cannot be read!");
/*      */     
/*  941 */     Checks.notNull(name, "Name");
/*      */ 
/*      */     
/*      */     try {
/*  945 */       return editMessageById(messageId, new FileInputStream(file), name, options);
/*      */     }
/*  947 */     catch (FileNotFoundException ex) {
/*      */       
/*  949 */       throw new IllegalArgumentException(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(@Nonnull String messageId, @Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/*  999 */     Checks.notNull(data, "Data");
/* 1000 */     Checks.notNull(name, "Name");
/*      */     
/* 1002 */     return editMessageById(messageId, new ByteArrayInputStream(data), name, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(long messageId, @Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 1051 */     return editMessageById(Long.toUnsignedString(messageId), data, name, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(long messageId, @Nonnull File file, @Nonnull AttachmentOption... options) {
/* 1100 */     return editMessageById(Long.toUnsignedString(messageId), file, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(long messageId, @Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 1149 */     return editMessageById(Long.toUnsignedString(messageId), file, name, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookMessageUpdateAction<T> editMessageById(long messageId, @Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 1198 */     return editMessageById(Long.toUnsignedString(messageId), data, name, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> deleteMessageById(@Nonnull String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> deleteMessageById(long messageId) {
/* 1249 */     return deleteMessageById(Long.toUnsignedString(messageId));
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\WebhookClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */