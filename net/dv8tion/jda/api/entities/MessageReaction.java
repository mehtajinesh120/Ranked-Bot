/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.PermissionException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.pagination.ReactionPaginationActionImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.EncodingUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageReaction
/*     */ {
/*     */   private final MessageChannel channel;
/*     */   private final ReactionEmote emote;
/*     */   private final long messageId;
/*     */   private final boolean self;
/*     */   private final int count;
/*     */   
/*     */   public MessageReaction(@Nonnull MessageChannel channel, @Nonnull ReactionEmote emote, long messageId, boolean self, int count) {
/*  71 */     this.channel = channel;
/*  72 */     this.emote = emote;
/*  73 */     this.messageId = messageId;
/*  74 */     this.self = self;
/*  75 */     this.count = count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  86 */     return this.channel.getJDA();
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
/*     */   public boolean isSelf() {
/*  99 */     return this.self;
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
/*     */   public boolean hasCount() {
/* 113 */     return (this.count >= 0);
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
/*     */   public int getCount() {
/* 131 */     if (!hasCount())
/* 132 */       throw new IllegalStateException("Cannot retrieve count for this MessageReaction!"); 
/* 133 */     return this.count;
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
/*     */   public ChannelType getChannelType() {
/* 145 */     return this.channel.getType();
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
/*     */   public boolean isFromType(@Nonnull ChannelType type) {
/* 159 */     return (getChannelType() == type);
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
/*     */   @Nullable
/*     */   public Guild getGuild() {
/* 172 */     TextChannel channel = getTextChannel();
/* 173 */     return (channel != null) ? channel.getGuild() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TextChannel getTextChannel() {
/* 185 */     return (getChannel() instanceof TextChannel) ? (TextChannel)getChannel() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PrivateChannel getPrivateChannel() {
/* 197 */     return (getChannel() instanceof PrivateChannel) ? (PrivateChannel)getChannel() : null;
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
/*     */   public MessageChannel getChannel() {
/* 209 */     return this.channel;
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
/*     */   public ReactionEmote getReactionEmote() {
/* 221 */     return this.emote;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getMessageId() {
/* 232 */     return Long.toUnsignedString(this.messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMessageIdLong() {
/* 242 */     return this.messageId;
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public ReactionPaginationAction retrieveUsers() {
/* 267 */     return (ReactionPaginationAction)new ReactionPaginationActionImpl(this);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Void> removeReaction() {
/* 294 */     return removeReaction(getJDA().getSelfUser());
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Void> removeReaction(@Nonnull User user) {
/* 335 */     Checks.notNull(user, "User");
/* 336 */     boolean self = user.equals(getJDA().getSelfUser());
/* 337 */     if (!self)
/*     */     {
/* 339 */       if (this.channel.getType() == ChannelType.TEXT) {
/*     */         
/* 341 */         GuildChannel channel = (GuildChannel)this.channel;
/* 342 */         if (!channel.getGuild().getSelfMember().hasPermission(channel, new Permission[] { Permission.MESSAGE_MANAGE })) {
/* 343 */           throw new InsufficientPermissionException(channel, Permission.MESSAGE_MANAGE);
/*     */         }
/*     */       } else {
/*     */         
/* 347 */         throw new PermissionException("Unable to remove Reaction of other user in non-text channel!");
/*     */       } 
/*     */     }
/*     */     
/* 351 */     String code = getReactionCode();
/* 352 */     String target = self ? "@me" : user.getId();
/* 353 */     Route.CompiledRoute route = Route.Messages.REMOVE_REACTION.compile(new String[] { this.channel.getId(), getMessageId(), code, target });
/* 354 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route);
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
/*     */   public RestAction<Void> clearReactions() {
/* 389 */     if (!getChannelType().isGuild())
/* 390 */       throw new UnsupportedOperationException("Cannot clear reactions on a message sent from a private channel"); 
/* 391 */     TextChannel guildChannel = Objects.<TextChannel>requireNonNull(getTextChannel());
/* 392 */     if (!guildChannel.getGuild().getSelfMember().hasPermission(guildChannel, new Permission[] { Permission.MESSAGE_MANAGE })) {
/* 393 */       throw new InsufficientPermissionException(guildChannel, Permission.MESSAGE_MANAGE);
/*     */     }
/* 395 */     String code = getReactionCode();
/* 396 */     Route.CompiledRoute route = Route.Messages.CLEAR_EMOTE_REACTIONS.compile(new String[] { this.channel.getId(), getMessageId(), code });
/* 397 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getReactionCode() {
/* 402 */     return this.emote.isEmote() ? (
/* 403 */       this.emote.getName() + ":" + this.emote.getId()) : 
/* 404 */       EncodingUtil.encodeUTF8(this.emote.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 410 */     if (obj == this)
/* 411 */       return true; 
/* 412 */     if (!(obj instanceof MessageReaction))
/* 413 */       return false; 
/* 414 */     MessageReaction r = (MessageReaction)obj;
/* 415 */     return (r.emote.equals(this.emote) && r.self == this.self && r.messageId == this.messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 423 */     return "MR:(M:(" + this.messageId + ") / " + this.emote + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ReactionEmote
/*     */     implements ISnowflake
/*     */   {
/*     */     private final JDA api;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final long id;
/*     */     
/*     */     private final Emote emote;
/*     */     
/*     */     private ReactionEmote(@Nonnull String name, @Nonnull JDA api) {
/* 439 */       this.name = name;
/* 440 */       this.api = api;
/* 441 */       this.id = 0L;
/* 442 */       this.emote = null;
/*     */     }
/*     */ 
/*     */     
/*     */     private ReactionEmote(@Nonnull Emote emote) {
/* 447 */       this.api = emote.getJDA();
/* 448 */       this.name = emote.getName();
/* 449 */       this.id = emote.getIdLong();
/* 450 */       this.emote = emote;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static ReactionEmote fromUnicode(@Nonnull String name, @Nonnull JDA api) {
/* 456 */       return new ReactionEmote(name, api);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static ReactionEmote fromCustom(@Nonnull Emote emote) {
/* 462 */       return new ReactionEmote(emote);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmote() {
/* 475 */       return (this.emote != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmoji() {
/* 488 */       return (this.emote == null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 505 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getAsCodepoints() {
/* 519 */       if (!isEmoji())
/* 520 */         throw new IllegalStateException("Cannot get codepoint for custom emote reaction"); 
/* 521 */       return EncodingUtil.encodeCodepoints(this.name);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getIdLong() {
/* 527 */       if (!isEmote())
/* 528 */         throw new IllegalStateException("Cannot get id for emoji reaction"); 
/* 529 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getAsReactionCode() {
/* 542 */       return (this.emote != null) ? (
/* 543 */         this.name + ":" + this.id) : 
/* 544 */         this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getEmoji() {
/* 558 */       if (!isEmoji())
/* 559 */         throw new IllegalStateException("Cannot get emoji code for custom emote reaction"); 
/* 560 */       return getName();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Emote getEmote() {
/* 575 */       if (!isEmote())
/* 576 */         throw new IllegalStateException("Cannot get custom emote for emoji reaction"); 
/* 577 */       return this.emote;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public JDA getJDA() {
/* 588 */       return this.api;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 594 */       return (obj instanceof ReactionEmote && 
/* 595 */         Objects.equals(Long.valueOf(((ReactionEmote)obj).id), Long.valueOf(this.id)) && ((ReactionEmote)obj)
/* 596 */         .getName().equals(this.name));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 602 */       if (isEmoji())
/* 603 */         return "RE:" + getAsCodepoints(); 
/* 604 */       return "RE:" + getName() + "(" + getId() + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageReaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */