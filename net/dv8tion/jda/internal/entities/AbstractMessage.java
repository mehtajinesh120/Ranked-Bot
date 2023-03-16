/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.IMentionable;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.MessageReference;
/*     */ import net.dv8tion.jda.api.entities.MessageSticker;
/*     */ import net.dv8tion.jda.api.entities.MessageType;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ 
/*     */ public abstract class AbstractMessage
/*     */   implements Message {
/*     */   protected static final String UNSUPPORTED = "This operation is not supported for Messages of this type!";
/*     */   protected final String content;
/*     */   protected final String nonce;
/*     */   protected final boolean isTTS;
/*     */   
/*     */   public AbstractMessage(String content, String nonce, boolean isTTS) {
/*  47 */     this.content = content;
/*  48 */     this.nonce = nonce;
/*  49 */     this.isTTS = isTTS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getContentRaw() {
/*  56 */     return this.content;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNonce() {
/*  62 */     return this.nonce;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTTS() {
/*  68 */     return this.isTTS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void unsupported();
/*     */ 
/*     */   
/*     */   public void formatTo(Formatter formatter, int flags, int width, int precision) {
/*  76 */     boolean upper = ((flags & 0x2) == 2);
/*  77 */     boolean leftJustified = ((flags & 0x1) == 1);
/*     */     
/*  79 */     String out = this.content;
/*     */     
/*  81 */     if (upper) {
/*  82 */       out = out.toUpperCase(formatter.locale());
/*     */     }
/*  84 */     appendFormat(formatter, width, precision, leftJustified, out);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void appendFormat(Formatter formatter, int width, int precision, boolean leftJustified, String out) {
/*     */     try {
/*  91 */       Appendable appendable = formatter.out();
/*  92 */       if (precision > -1 && out.length() > precision) {
/*     */         
/*  94 */         appendable.append(Helpers.truncate(out, precision - 3)).append("...");
/*     */         
/*     */         return;
/*     */       } 
/*  98 */       if (leftJustified) {
/*  99 */         appendable.append(Helpers.rightPad(out, width));
/*     */       } else {
/* 101 */         appendable.append(Helpers.leftPad(out, width));
/*     */       } 
/* 103 */     } catch (IOException e) {
/*     */       
/* 105 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MessageReference getMessageReference() {
/* 113 */     unsupported();
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Bag<User> getMentionedUsersBag() {
/* 121 */     unsupported();
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Bag<TextChannel> getMentionedChannelsBag() {
/* 129 */     unsupported();
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Bag<Role> getMentionedRolesBag() {
/* 137 */     unsupported();
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<User> getMentionedUsers() {
/* 145 */     unsupported();
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<TextChannel> getMentionedChannels() {
/* 153 */     unsupported();
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Role> getMentionedRoles() {
/* 161 */     unsupported();
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getMentionedMembers(@Nonnull Guild guild) {
/* 169 */     unsupported();
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getMentionedMembers() {
/* 177 */     unsupported();
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<IMentionable> getMentions(@Nonnull Message.MentionType... types) {
/* 185 */     unsupported();
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMentioned(@Nonnull IMentionable mentionable, @Nonnull Message.MentionType... types) {
/* 192 */     unsupported();
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mentionsEveryone() {
/* 199 */     unsupported();
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEdited() {
/* 206 */     unsupported();
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public OffsetDateTime getTimeEdited() {
/* 213 */     unsupported();
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getAuthor() {
/* 221 */     unsupported();
/* 222 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 228 */     unsupported();
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getJumpUrl() {
/* 236 */     unsupported();
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getContentDisplay() {
/* 244 */     unsupported();
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getContentStripped() {
/* 252 */     unsupported();
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<String> getInvites() {
/* 260 */     unsupported();
/* 261 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFromType(@Nonnull ChannelType type) {
/* 267 */     unsupported();
/* 268 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getChannelType() {
/* 275 */     unsupported();
/* 276 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWebhookMessage() {
/* 282 */     unsupported();
/* 283 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageChannel getChannel() {
/* 290 */     unsupported();
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PrivateChannel getPrivateChannel() {
/* 298 */     unsupported();
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public TextChannel getTextChannel() {
/* 306 */     unsupported();
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Category getCategory() {
/* 313 */     unsupported();
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/* 321 */     unsupported();
/* 322 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Message.Attachment> getAttachments() {
/* 329 */     unsupported();
/* 330 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<MessageEmbed> getEmbeds() {
/* 337 */     unsupported();
/* 338 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionRow> getActionRows() {
/* 345 */     unsupported();
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Emote> getEmotes() {
/* 353 */     unsupported();
/* 354 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Bag<Emote> getEmotesBag() {
/* 361 */     unsupported();
/* 362 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<MessageReaction> getReactions() {
/* 369 */     unsupported();
/* 370 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<MessageSticker> getStickers() {
/* 377 */     unsupported();
/* 378 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessage(@Nonnull CharSequence newContent) {
/* 385 */     unsupported();
/* 386 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> newContent) {
/* 393 */     unsupported();
/* 394 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageComponents(@Nonnull Collection<? extends ComponentLayout> components) {
/* 401 */     unsupported();
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageFormat(@Nonnull String format, @Nonnull Object... args) {
/* 409 */     unsupported();
/* 410 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessage(@Nonnull Message newContent) {
/* 417 */     unsupported();
/* 418 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/* 425 */     unsupported();
/* 426 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 433 */     unsupported();
/* 434 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPinned() {
/* 440 */     unsupported();
/* 441 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> pin() {
/* 448 */     unsupported();
/* 449 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> unpin() {
/* 456 */     unsupported();
/* 457 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> addReaction(@Nonnull Emote emote) {
/* 464 */     unsupported();
/* 465 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> addReaction(@Nonnull String unicode) {
/* 472 */     unsupported();
/* 473 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> clearReactions() {
/* 480 */     unsupported();
/* 481 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> clearReactions(@Nonnull String unicode) {
/* 488 */     unsupported();
/* 489 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> clearReactions(@Nonnull Emote emote) {
/* 496 */     unsupported();
/* 497 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> removeReaction(@Nonnull Emote emote) {
/* 504 */     unsupported();
/* 505 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> removeReaction(@Nonnull Emote emote, @Nonnull User user) {
/* 512 */     unsupported();
/* 513 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> removeReaction(@Nonnull String unicode) {
/* 520 */     unsupported();
/* 521 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> removeReaction(@Nonnull String unicode, @Nonnull User user) {
/* 528 */     unsupported();
/* 529 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReactionPaginationAction retrieveReactionUsers(@Nonnull Emote emote) {
/* 536 */     unsupported();
/* 537 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReactionPaginationAction retrieveReactionUsers(@Nonnull String unicode) {
/* 544 */     unsupported();
/* 545 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageReaction.ReactionEmote getReactionByUnicode(@Nonnull String unicode) {
/* 551 */     unsupported();
/* 552 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageReaction.ReactionEmote getReactionById(@Nonnull String id) {
/* 558 */     unsupported();
/* 559 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageReaction.ReactionEmote getReactionById(long id) {
/* 565 */     unsupported();
/* 566 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> suppressEmbeds(boolean suppressed) {
/* 573 */     unsupported();
/* 574 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Message> crosspost() {
/* 581 */     unsupported();
/* 582 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuppressedEmbeds() {
/* 588 */     unsupported();
/* 589 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Message.MessageFlag> getFlags() {
/* 596 */     unsupported();
/* 597 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFlagsRaw() {
/* 603 */     unsupported();
/* 604 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEphemeral() {
/* 610 */     unsupported();
/* 611 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageType getType() {
/* 618 */     unsupported();
/* 619 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Message.Interaction getInteraction() {
/* 626 */     unsupported();
/* 627 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\AbstractMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */