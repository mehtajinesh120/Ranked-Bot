/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import gnu.trove.set.TLongSet;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageActivity;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.MessageReference;
/*     */ import net.dv8tion.jda.api.entities.MessageSticker;
/*     */ import net.dv8tion.jda.api.entities.MessageType;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SystemMessage
/*     */   extends ReceivedMessage
/*     */ {
/*     */   public SystemMessage(long id, MessageChannel channel, MessageType type, MessageReference messageReference, boolean fromWebhook, boolean mentionsEveryone, TLongSet mentionedUsers, TLongSet mentionedRoles, boolean tts, boolean pinned, String content, String nonce, User author, Member member, MessageActivity activity, OffsetDateTime editTime, List<MessageReaction> reactions, List<Message.Attachment> attachments, List<MessageEmbed> embeds, List<MessageSticker> stickers, int flags) {
/*  40 */     super(id, channel, type, messageReference, fromWebhook, mentionsEveryone, mentionedUsers, mentionedRoles, tts, pinned, content, nonce, author, member, activity, editTime, reactions, attachments, embeds, stickers, 
/*  41 */         Collections.emptyList(), flags, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> pin() {
/*  48 */     throw new UnsupportedOperationException("Cannot pin message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> unpin() {
/*  55 */     throw new UnsupportedOperationException("Cannot unpin message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessage(@Nonnull CharSequence newContent) {
/*  62 */     throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessage(@Nonnull MessageEmbed newContent) {
/*  69 */     throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/*  76 */     throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageComponents(@Nonnull Collection<? extends ComponentLayout> components) {
/*  83 */     throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageFormat(@Nonnull String format, @Nonnull Object... args) {
/*  90 */     throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessage(@Nonnull Message newContent) {
/*  97 */     throw new UnsupportedOperationException("Cannot edit message of this Message Type. MessageType: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     return "M:[" + this.type + ']' + this.author + '(' + this.id + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\SystemMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */