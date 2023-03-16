/*      */ package net.dv8tion.jda.internal.entities;
/*      */ import gnu.trove.set.TLongSet;
/*      */ import java.time.OffsetDateTime;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Formatter;
/*      */ import java.util.List;
/*      */ import java.util.function.Function;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.JDA;
/*      */ import net.dv8tion.jda.api.Permission;
/*      */ import net.dv8tion.jda.api.entities.ChannelType;
/*      */ import net.dv8tion.jda.api.entities.Emote;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.GuildChannel;
/*      */ import net.dv8tion.jda.api.entities.IMentionable;
/*      */ import net.dv8tion.jda.api.entities.Member;
/*      */ import net.dv8tion.jda.api.entities.Message;
/*      */ import net.dv8tion.jda.api.entities.MessageActivity;
/*      */ import net.dv8tion.jda.api.entities.MessageChannel;
/*      */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*      */ import net.dv8tion.jda.api.entities.MessageReaction;
/*      */ import net.dv8tion.jda.api.entities.MessageReference;
/*      */ import net.dv8tion.jda.api.entities.MessageSticker;
/*      */ import net.dv8tion.jda.api.entities.MessageType;
/*      */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*      */ import net.dv8tion.jda.api.entities.Role;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*      */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*      */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*      */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*      */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
/*      */ import net.dv8tion.jda.api.utils.MiscUtil;
/*      */ import net.dv8tion.jda.internal.JDAImpl;
/*      */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import org.apache.commons.collections4.Bag;
/*      */ import org.apache.commons.collections4.CollectionUtils;
/*      */ import org.apache.commons.collections4.bag.HashBag;
/*      */ 
/*      */ public class ReceivedMessage extends AbstractMessage {
/*   56 */   private final Object mutex = new Object();
/*      */   
/*      */   protected final JDAImpl api;
/*      */   
/*      */   protected final long id;
/*      */   protected final MessageType type;
/*      */   protected final MessageChannel channel;
/*      */   protected final MessageReference messageReference;
/*      */   protected final boolean fromWebhook;
/*      */   protected final boolean mentionsEveryone;
/*      */   protected final boolean pinned;
/*      */   protected final User author;
/*      */   protected final Member member;
/*      */   protected final MessageActivity activity;
/*      */   protected final OffsetDateTime editedTime;
/*      */   protected final List<MessageReaction> reactions;
/*      */   protected final List<Message.Attachment> attachments;
/*      */   protected final List<MessageEmbed> embeds;
/*      */   protected final List<MessageSticker> stickers;
/*      */   protected final List<ActionRow> components;
/*      */   protected final TLongSet mentionedUsers;
/*      */   protected final TLongSet mentionedRoles;
/*      */   protected final int flags;
/*      */   protected final Message.Interaction interaction;
/*   80 */   protected InteractionHook interactionHook = null;
/*      */ 
/*      */   
/*   83 */   protected String altContent = null;
/*   84 */   protected String strippedContent = null;
/*      */   
/*   86 */   protected List<User> userMentions = null;
/*   87 */   protected List<Member> memberMentions = null;
/*   88 */   protected List<Emote> emoteMentions = null;
/*   89 */   protected List<Role> roleMentions = null;
/*   90 */   protected List<TextChannel> channelMentions = null;
/*   91 */   protected List<String> invites = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReceivedMessage(long id, MessageChannel channel, MessageType type, MessageReference messageReference, boolean fromWebhook, boolean mentionsEveryone, TLongSet mentionedUsers, TLongSet mentionedRoles, boolean tts, boolean pinned, String content, String nonce, User author, Member member, MessageActivity activity, OffsetDateTime editTime, List<MessageReaction> reactions, List<Message.Attachment> attachments, List<MessageEmbed> embeds, List<MessageSticker> stickers, List<ActionRow> components, int flags, Message.Interaction interaction) {
/*   99 */     super(content, nonce, tts);
/*  100 */     this.id = id;
/*  101 */     this.channel = channel;
/*  102 */     this.messageReference = messageReference;
/*  103 */     this.type = type;
/*  104 */     this.api = (channel != null) ? (JDAImpl)channel.getJDA() : null;
/*  105 */     this.fromWebhook = fromWebhook;
/*  106 */     this.mentionsEveryone = mentionsEveryone;
/*  107 */     this.pinned = pinned;
/*  108 */     this.author = author;
/*  109 */     this.member = member;
/*  110 */     this.activity = activity;
/*  111 */     this.editedTime = editTime;
/*  112 */     this.reactions = Collections.unmodifiableList(reactions);
/*  113 */     this.attachments = Collections.unmodifiableList(attachments);
/*  114 */     this.embeds = Collections.unmodifiableList(embeds);
/*  115 */     this.stickers = Collections.unmodifiableList(stickers);
/*  116 */     this.components = Collections.unmodifiableList(components);
/*  117 */     this.mentionedUsers = mentionedUsers;
/*  118 */     this.mentionedRoles = mentionedRoles;
/*  119 */     this.flags = flags;
/*  120 */     this.interaction = interaction;
/*      */   }
/*      */ 
/*      */   
/*      */   public ReceivedMessage withHook(InteractionHook hook) {
/*  125 */     this.interactionHook = hook;
/*  126 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public JDA getJDA() {
/*  133 */     return (JDA)this.api;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public MessageReference getMessageReference() {
/*  140 */     return this.messageReference;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPinned() {
/*  146 */     return this.pinned;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> pin() {
/*  153 */     if (isEphemeral()) {
/*  154 */       throw new IllegalStateException("Cannot pin ephemeral messages.");
/*      */     }
/*  156 */     return this.channel.pinMessageById(getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> unpin() {
/*  163 */     if (isEphemeral()) {
/*  164 */       throw new IllegalStateException("Cannot unpin ephemeral messages.");
/*      */     }
/*  166 */     return this.channel.unpinMessageById(getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> addReaction(@Nonnull Emote emote) {
/*  173 */     if (isEphemeral()) {
/*  174 */       throw new IllegalStateException("Cannot add reactions to ephemeral messages.");
/*      */     }
/*  176 */     Checks.notNull(emote, "Emote");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  181 */     boolean missingReaction = this.reactions.stream().map(MessageReaction::getReactionEmote).filter(MessageReaction.ReactionEmote::isEmote).noneMatch(r -> (r.getIdLong() == emote.getIdLong()));
/*      */     
/*  183 */     if (missingReaction)
/*      */     {
/*  185 */       Checks.check(emote.canInteract((User)getJDA().getSelfUser(), this.channel), "Cannot react with the provided emote because it is not available in the current channel.");
/*      */     }
/*      */     
/*  188 */     return this.channel.addReactionById(getId(), emote);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> addReaction(@Nonnull String unicode) {
/*  195 */     if (isEphemeral()) {
/*  196 */       throw new IllegalStateException("Cannot add reactions to ephemeral messages.");
/*      */     }
/*  198 */     return this.channel.addReactionById(getId(), unicode);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> clearReactions() {
/*  205 */     if (isEphemeral())
/*  206 */       throw new IllegalStateException("Cannot clear reactions from ephemeral messages."); 
/*  207 */     if (!isFromGuild())
/*  208 */       throw new IllegalStateException("Cannot clear reactions from a message in a Group or PrivateChannel."); 
/*  209 */     return getTextChannel().clearReactionsById(getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> clearReactions(@Nonnull String unicode) {
/*  216 */     if (isEphemeral())
/*  217 */       throw new IllegalStateException("Cannot clear reactions from ephemeral messages."); 
/*  218 */     if (!isFromGuild())
/*  219 */       throw new IllegalStateException("Cannot clear reactions from a message in a Group or PrivateChannel."); 
/*  220 */     return getTextChannel().clearReactionsById(getId(), unicode);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> clearReactions(@Nonnull Emote emote) {
/*  227 */     if (isEphemeral())
/*  228 */       throw new IllegalStateException("Cannot clear reactions from ephemeral messages."); 
/*  229 */     if (!isFromGuild())
/*  230 */       throw new IllegalStateException("Cannot clear reactions from a message in a Group or PrivateChannel."); 
/*  231 */     return getTextChannel().clearReactionsById(getId(), emote);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> removeReaction(@Nonnull Emote emote) {
/*  238 */     if (isEphemeral()) {
/*  239 */       throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
/*      */     }
/*  241 */     return this.channel.removeReactionById(getId(), emote);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> removeReaction(@Nonnull Emote emote, @Nonnull User user) {
/*  248 */     Checks.notNull(user, "User");
/*  249 */     if (isEphemeral()) {
/*  250 */       throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
/*      */     }
/*      */     
/*  253 */     if (user.equals(getJDA().getSelfUser())) {
/*  254 */       return this.channel.removeReactionById(getIdLong(), emote);
/*      */     }
/*  256 */     if (!isFromGuild())
/*  257 */       throw new IllegalStateException("Cannot remove reactions of others from a message in a Group or PrivateChannel."); 
/*  258 */     return getTextChannel().removeReactionById(getIdLong(), emote, user);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> removeReaction(@Nonnull String unicode) {
/*  265 */     if (isEphemeral()) {
/*  266 */       throw new IllegalStateException("Cannot remove reactions from ephemeral messages.");
/*      */     }
/*  268 */     return this.channel.removeReactionById(getId(), unicode);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Void> removeReaction(@Nonnull String unicode, @Nonnull User user) {
/*  275 */     Checks.notNull(user, "User");
/*  276 */     if (user.equals(getJDA().getSelfUser())) {
/*  277 */       return this.channel.removeReactionById(getIdLong(), unicode);
/*      */     }
/*  279 */     if (isEphemeral())
/*  280 */       throw new IllegalStateException("Cannot remove reactions from ephemeral messages."); 
/*  281 */     if (!isFromGuild())
/*  282 */       throw new IllegalStateException("Cannot remove reactions of others from a message in a Group or PrivateChannel."); 
/*  283 */     return getTextChannel().removeReactionById(getId(), unicode, user);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ReactionPaginationAction retrieveReactionUsers(@Nonnull Emote emote) {
/*  290 */     if (isEphemeral()) {
/*  291 */       throw new IllegalStateException("Cannot retrieve reactions on ephemeral messages.");
/*      */     }
/*  293 */     return this.channel.retrieveReactionUsersById(this.id, emote);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ReactionPaginationAction retrieveReactionUsers(@Nonnull String unicode) {
/*  300 */     if (isEphemeral()) {
/*  301 */       throw new IllegalStateException("Cannot retrieve reactions on ephemeral messages.");
/*      */     }
/*  303 */     return this.channel.retrieveReactionUsersById(this.id, unicode);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageReaction.ReactionEmote getReactionByUnicode(@Nonnull String unicode) {
/*  309 */     Checks.notEmpty(unicode, "Emoji");
/*  310 */     Checks.noWhitespace(unicode, "Emoji");
/*      */     
/*  312 */     return this.reactions.stream()
/*  313 */       .map(MessageReaction::getReactionEmote)
/*  314 */       .filter(r -> (r.isEmoji() && r.getEmoji().equals(unicode)))
/*  315 */       .findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageReaction.ReactionEmote getReactionById(@Nonnull String id) {
/*  321 */     return getReactionById(MiscUtil.parseSnowflake(id));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageReaction.ReactionEmote getReactionById(long id) {
/*  327 */     return this.reactions.stream()
/*  328 */       .map(MessageReaction::getReactionEmote)
/*  329 */       .filter(r -> (r.isEmote() && r.getIdLong() == id))
/*  330 */       .findFirst().orElse(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageType getType() {
/*  337 */     return this.type;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Message.Interaction getInteraction() {
/*  344 */     return this.interaction;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdLong() {
/*  350 */     return this.id;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getJumpUrl() {
/*  357 */     return String.format("https://discord.com/channels/%s/%s/%s", new Object[] { isFromGuild() ? getGuild().getId() : "@me", getChannel().getId(), getId() });
/*      */   }
/*      */ 
/*      */   
/*      */   private User matchUser(Matcher matcher) {
/*  362 */     long userId = MiscUtil.parseSnowflake(matcher.group(1));
/*  363 */     if (!this.mentionedUsers.contains(userId))
/*  364 */       return null; 
/*  365 */     User user = getJDA().getUserById(userId);
/*  366 */     if (user == null && this.userMentions != null)
/*  367 */       user = this.userMentions.stream().filter(it -> (it.getIdLong() == userId)).findFirst().orElse(null); 
/*  368 */     return user;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public synchronized List<User> getMentionedUsers() {
/*  375 */     if (this.userMentions == null)
/*  376 */       this.userMentions = Collections.unmodifiableList(processMentions(Message.MentionType.USER, new ArrayList<>(), true, this::matchUser)); 
/*  377 */     return this.userMentions;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Bag<User> getMentionedUsersBag() {
/*  384 */     return (Bag<User>)processMentions(Message.MentionType.USER, new HashBag(), false, this::matchUser);
/*      */   }
/*      */ 
/*      */   
/*      */   private TextChannel matchTextChannel(Matcher matcher) {
/*  389 */     long channelId = MiscUtil.parseSnowflake(matcher.group(1));
/*  390 */     return getJDA().getTextChannelById(channelId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public synchronized List<TextChannel> getMentionedChannels() {
/*  397 */     if (this.channelMentions == null)
/*  398 */       this.channelMentions = Collections.unmodifiableList(processMentions(Message.MentionType.CHANNEL, new ArrayList<>(), true, this::matchTextChannel)); 
/*  399 */     return this.channelMentions;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Bag<TextChannel> getMentionedChannelsBag() {
/*  406 */     return (Bag<TextChannel>)processMentions(Message.MentionType.CHANNEL, new HashBag(), false, this::matchTextChannel);
/*      */   }
/*      */ 
/*      */   
/*      */   private Role matchRole(Matcher matcher) {
/*  411 */     long roleId = MiscUtil.parseSnowflake(matcher.group(1));
/*  412 */     if (!this.mentionedRoles.contains(roleId))
/*  413 */       return null; 
/*  414 */     if (getChannelType().isGuild()) {
/*  415 */       return getGuild().getRoleById(roleId);
/*      */     }
/*  417 */     return getJDA().getRoleById(roleId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public synchronized List<Role> getMentionedRoles() {
/*  424 */     if (this.roleMentions == null)
/*  425 */       this.roleMentions = Collections.unmodifiableList(processMentions(Message.MentionType.ROLE, new ArrayList<>(), true, this::matchRole)); 
/*  426 */     return this.roleMentions;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Bag<Role> getMentionedRolesBag() {
/*  433 */     return (Bag<Role>)processMentions(Message.MentionType.ROLE, new HashBag(), false, this::matchRole);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Member> getMentionedMembers(@Nonnull Guild guild) {
/*  440 */     Checks.notNull(guild, "Guild");
/*  441 */     if (isFromGuild() && guild.equals(getGuild()) && this.memberMentions != null)
/*  442 */       return this.memberMentions; 
/*  443 */     List<User> mentionedUsers = getMentionedUsers();
/*  444 */     List<Member> members = new ArrayList<>();
/*  445 */     for (User user : mentionedUsers) {
/*      */       
/*  447 */       Member member = guild.getMember(user);
/*  448 */       if (member != null) {
/*  449 */         members.add(member);
/*      */       }
/*      */     } 
/*  452 */     return Collections.unmodifiableList(members);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Member> getMentionedMembers() {
/*  459 */     if (isFromGuild()) {
/*  460 */       return getMentionedMembers(getGuild());
/*      */     }
/*  462 */     throw new IllegalStateException("You must specify a Guild for Messages which are not sent from a TextChannel!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<IMentionable> getMentions(@Nonnull Message.MentionType... types) {
/*  469 */     if (types == null || types.length == 0)
/*  470 */       return getMentions(Message.MentionType.values()); 
/*  471 */     List<IMentionable> mentions = new ArrayList<>();
/*      */ 
/*      */     
/*  474 */     boolean channel = false;
/*  475 */     boolean role = false;
/*  476 */     boolean user = false;
/*  477 */     boolean emote = false;
/*  478 */     for (Message.MentionType type : types) {
/*      */       
/*  480 */       switch (type) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case CHANNEL:
/*  486 */           if (!channel)
/*  487 */             mentions.addAll(getMentionedChannels()); 
/*  488 */           channel = true;
/*      */           break;
/*      */         case USER:
/*  491 */           if (!user)
/*  492 */             mentions.addAll(getMentionedUsers()); 
/*  493 */           user = true;
/*      */           break;
/*      */         case ROLE:
/*  496 */           if (!role)
/*  497 */             mentions.addAll(getMentionedRoles()); 
/*  498 */           role = true;
/*      */           break;
/*      */         case EMOTE:
/*  501 */           if (!emote)
/*  502 */             mentions.addAll(getEmotes()); 
/*  503 */           emote = true; break;
/*      */       } 
/*      */     } 
/*  506 */     return Collections.unmodifiableList(mentions);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMentioned(@Nonnull IMentionable mentionable, @Nonnull Message.MentionType... types) {
/*  512 */     Checks.notNull(types, "Mention Types");
/*  513 */     if (types.length == 0)
/*  514 */       return isMentioned(mentionable, Message.MentionType.values()); 
/*  515 */     boolean isUserEntity = (mentionable instanceof User || mentionable instanceof Member);
/*  516 */     for (Message.MentionType type : types) {
/*      */       
/*  518 */       switch (type) {
/*      */ 
/*      */         
/*      */         case HERE:
/*  522 */           if (isMass("@here") && isUserEntity) {
/*  523 */             return true;
/*      */           }
/*      */           break;
/*      */         
/*      */         case EVERYONE:
/*  528 */           if (isMass("@everyone") && isUserEntity) {
/*  529 */             return true;
/*      */           }
/*      */           break;
/*      */         
/*      */         case USER:
/*  534 */           if (isUserMentioned(mentionable)) {
/*  535 */             return true;
/*      */           }
/*      */           break;
/*      */         
/*      */         case ROLE:
/*  540 */           if (isRoleMentioned(mentionable)) {
/*  541 */             return true;
/*      */           }
/*      */           break;
/*      */         
/*      */         case CHANNEL:
/*  546 */           if (mentionable instanceof TextChannel)
/*      */           {
/*  548 */             if (getMentionedChannels().contains(mentionable)) {
/*  549 */               return true;
/*      */             }
/*      */           }
/*      */           break;
/*      */         
/*      */         case EMOTE:
/*  555 */           if (mentionable instanceof Emote)
/*      */           {
/*  557 */             if (getEmotes().contains(mentionable)) {
/*  558 */               return true;
/*      */             }
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  565 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isUserMentioned(IMentionable mentionable) {
/*  570 */     if (mentionable instanceof User)
/*      */     {
/*  572 */       return getMentionedUsers().contains(mentionable);
/*      */     }
/*  574 */     if (mentionable instanceof Member) {
/*      */       
/*  576 */       Member member = (Member)mentionable;
/*  577 */       return getMentionedUsers().contains(member.getUser());
/*      */     } 
/*  579 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isRoleMentioned(IMentionable mentionable) {
/*  584 */     if (mentionable instanceof Role)
/*      */     {
/*  586 */       return getMentionedRoles().contains(mentionable);
/*      */     }
/*  588 */     if (mentionable instanceof Member) {
/*      */       
/*  590 */       Member member = (Member)mentionable;
/*  591 */       return CollectionUtils.containsAny(getMentionedRoles(), member.getRoles());
/*      */     } 
/*  593 */     if (isFromGuild() && mentionable instanceof User) {
/*      */       
/*  595 */       Member member = getGuild().getMember((User)mentionable);
/*  596 */       return (member != null && CollectionUtils.containsAny(getMentionedRoles(), member.getRoles()));
/*      */     } 
/*  598 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isMass(String s) {
/*  603 */     return (this.mentionsEveryone && this.content.contains(s));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mentionsEveryone() {
/*  609 */     return this.mentionsEveryone;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEdited() {
/*  615 */     return (this.editedTime != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public OffsetDateTime getTimeEdited() {
/*  621 */     return this.editedTime;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public User getAuthor() {
/*  628 */     return this.author;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Member getMember() {
/*  634 */     return this.member;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getContentStripped() {
/*  641 */     if (this.strippedContent != null)
/*  642 */       return this.strippedContent; 
/*  643 */     synchronized (this.mutex) {
/*      */       
/*  645 */       if (this.strippedContent != null)
/*  646 */         return this.strippedContent; 
/*  647 */       return this.strippedContent = MarkdownSanitizer.sanitize(getContentDisplay());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getContentDisplay() {
/*  655 */     if (this.altContent != null)
/*  656 */       return this.altContent; 
/*  657 */     synchronized (this.mutex) {
/*      */       
/*  659 */       if (this.altContent != null)
/*  660 */         return this.altContent; 
/*  661 */       String tmp = this.content;
/*  662 */       for (User user : getMentionedUsers()) {
/*      */         String name;
/*      */         
/*  665 */         if (isFromGuild() && getGuild().isMember(user)) {
/*  666 */           name = getGuild().getMember(user).getEffectiveName();
/*      */         } else {
/*  668 */           name = user.getName();
/*  669 */         }  tmp = tmp.replaceAll("<@!?" + Pattern.quote(user.getId()) + '>', '@' + Matcher.quoteReplacement(name));
/*      */       } 
/*  671 */       for (Emote emote : getEmotes())
/*      */       {
/*  673 */         tmp = tmp.replace(emote.getAsMention(), ":" + emote.getName() + ":");
/*      */       }
/*  675 */       for (TextChannel mentionedChannel : getMentionedChannels())
/*      */       {
/*  677 */         tmp = tmp.replace(mentionedChannel.getAsMention(), '#' + mentionedChannel.getName());
/*      */       }
/*  679 */       for (Role mentionedRole : getMentionedRoles())
/*      */       {
/*  681 */         tmp = tmp.replace(mentionedRole.getAsMention(), '@' + mentionedRole.getName());
/*      */       }
/*  683 */       return this.altContent = tmp;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public String getContentRaw() {
/*  691 */     return this.content;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<String> getInvites() {
/*  698 */     if (this.invites != null)
/*  699 */       return this.invites; 
/*  700 */     synchronized (this.mutex) {
/*      */       
/*  702 */       if (this.invites != null)
/*  703 */         return this.invites; 
/*  704 */       this.invites = new ArrayList<>();
/*  705 */       Matcher m = INVITE_PATTERN.matcher(getContentRaw());
/*  706 */       while (m.find())
/*  707 */         this.invites.add(m.group(1)); 
/*  708 */       return this.invites = Collections.unmodifiableList(this.invites);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNonce() {
/*  715 */     return this.nonce;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFromType(@Nonnull ChannelType type) {
/*  721 */     return (getChannelType() == type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public ChannelType getChannelType() {
/*  728 */     return this.channel.getType();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageChannel getChannel() {
/*  735 */     return this.channel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public PrivateChannel getPrivateChannel() {
/*  742 */     if (!isFromType(ChannelType.PRIVATE))
/*  743 */       throw new IllegalStateException("This message was not sent in a private channel"); 
/*  744 */     return (PrivateChannel)this.channel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public TextChannel getTextChannel() {
/*  753 */     if (!isFromType(ChannelType.TEXT))
/*  754 */       throw new IllegalStateException("This message was not sent in a text channel"); 
/*  755 */     return (TextChannel)this.channel;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Category getCategory() {
/*  761 */     return isFromGuild() ? getTextChannel().getParent() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Guild getGuild() {
/*  768 */     return getTextChannel().getGuild();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<Message.Attachment> getAttachments() {
/*  775 */     return this.attachments;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<MessageEmbed> getEmbeds() {
/*  782 */     return this.embeds;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<ActionRow> getActionRows() {
/*  789 */     return this.components;
/*      */   }
/*      */   
/*      */   private Emote matchEmote(Matcher m) {
/*      */     EmoteImpl emoteImpl;
/*  794 */     long emoteId = MiscUtil.parseSnowflake(m.group(2));
/*  795 */     String name = m.group(1);
/*  796 */     boolean animated = m.group(0).startsWith("<a:");
/*  797 */     Emote emote = getJDA().getEmoteById(emoteId);
/*  798 */     if (emote == null)
/*  799 */       emoteImpl = (new EmoteImpl(emoteId, this.api)).setName(name).setAnimated(animated); 
/*  800 */     return (Emote)emoteImpl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public synchronized List<Emote> getEmotes() {
/*  807 */     if (this.emoteMentions == null)
/*  808 */       this.emoteMentions = Collections.unmodifiableList(processMentions(Message.MentionType.EMOTE, new ArrayList<>(), true, this::matchEmote)); 
/*  809 */     return this.emoteMentions;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public Bag<Emote> getEmotesBag() {
/*  816 */     return (Bag<Emote>)processMentions(Message.MentionType.EMOTE, new HashBag(), false, this::matchEmote);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<MessageReaction> getReactions() {
/*  823 */     return this.reactions;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public List<MessageSticker> getStickers() {
/*  830 */     return this.stickers;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWebhookMessage() {
/*  836 */     return this.fromWebhook;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTTS() {
/*  842 */     return this.isTTS;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public MessageActivity getActivity() {
/*  849 */     return this.activity;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageAction editMessage(@Nonnull CharSequence newContent) {
/*  856 */     checkUser();
/*  857 */     return (MessageAction)((MessageActionImpl)this.channel.editMessageById(getId(), newContent)).withHook(this.interactionHook);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageAction editMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/*  864 */     checkUser();
/*  865 */     return (MessageAction)((MessageActionImpl)this.channel.editMessageEmbedsById(getId(), embeds)).withHook(this.interactionHook);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageAction editMessageComponents(@Nonnull Collection<? extends ComponentLayout> components) {
/*  872 */     checkUser();
/*  873 */     return (MessageAction)((MessageActionImpl)this.channel.editMessageComponentsById(getId(), components)).withHook(this.interactionHook);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageAction editMessageFormat(@Nonnull String format, @Nonnull Object... args) {
/*  880 */     checkUser();
/*  881 */     return (MessageAction)((MessageActionImpl)this.channel.editMessageFormatById(getId(), format, args)).withHook(this.interactionHook);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageAction editMessage(@Nonnull Message newContent) {
/*  888 */     checkUser();
/*  889 */     return (MessageAction)((MessageActionImpl)this.channel.editMessageById(getId(), newContent)).withHook(this.interactionHook);
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkUser() {
/*  894 */     if (!getJDA().getSelfUser().equals(getAuthor())) {
/*  895 */       throw new IllegalStateException("Attempted to update message that was not sent by this account. You cannot modify other User's messages!");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> delete() {
/*  902 */     if (isEphemeral()) {
/*  903 */       throw new IllegalStateException("Cannot delete ephemeral messages.");
/*      */     }
/*  905 */     if (!getJDA().getSelfUser().equals(getAuthor())) {
/*      */       
/*  907 */       if (isFromType(ChannelType.PRIVATE))
/*  908 */         throw new IllegalStateException("Cannot delete another User's messages in a PrivateChannel."); 
/*  909 */       if (!getGuild().getSelfMember().hasAccess((GuildChannel)getTextChannel())) {
/*  910 */         throw new MissingAccessException(getTextChannel(), Permission.VIEW_CHANNEL);
/*      */       }
/*  912 */       if (!getGuild().getSelfMember().hasPermission((GuildChannel)getChannel(), new Permission[] { Permission.MESSAGE_MANAGE }))
/*  913 */         throw new InsufficientPermissionException(getTextChannel(), Permission.MESSAGE_MANAGE); 
/*      */     } 
/*  915 */     return this.channel.deleteMessageById(getIdLong());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public AuditableRestAction<Void> suppressEmbeds(boolean suppressed) {
/*  922 */     if (isEphemeral()) {
/*  923 */       throw new IllegalStateException("Cannot suppress embeds on ephemeral messages.");
/*      */     }
/*  925 */     if (!getJDA().getSelfUser().equals(getAuthor())) {
/*      */       
/*  927 */       if (isFromType(ChannelType.PRIVATE))
/*  928 */         throw new PermissionException("Cannot suppress embeds of others in a PrivateChannel."); 
/*  929 */       if (!getGuild().getSelfMember().hasPermission((GuildChannel)getTextChannel(), new Permission[] { Permission.MESSAGE_MANAGE }))
/*  930 */         throw new InsufficientPermissionException(getTextChannel(), Permission.MESSAGE_MANAGE); 
/*      */     } 
/*  932 */     JDAImpl jda = (JDAImpl)getJDA();
/*  933 */     Route.CompiledRoute route = Route.Messages.EDIT_MESSAGE.compile(new String[] { getChannel().getId(), getId() });
/*  934 */     int newFlags = this.flags;
/*  935 */     int suppressionValue = Message.MessageFlag.EMBEDS_SUPPRESSED.getValue();
/*  936 */     if (suppressed) {
/*  937 */       newFlags |= suppressionValue;
/*      */     } else {
/*  939 */       newFlags &= suppressionValue ^ 0xFFFFFFFF;
/*  940 */     }  return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)jda, route, DataObject.empty().put("flags", Integer.valueOf(newFlags)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public RestAction<Message> crosspost() {
/*  947 */     if (isEphemeral()) {
/*  948 */       throw new IllegalStateException("Cannot crosspost ephemeral messages.");
/*      */     }
/*  950 */     if (getFlags().contains(Message.MessageFlag.CROSSPOSTED))
/*  951 */       return (RestAction<Message>)new CompletedRestAction(getJDA(), this); 
/*  952 */     TextChannel textChannel = getTextChannel();
/*  953 */     if (!getGuild().getSelfMember().hasAccess((GuildChannel)textChannel))
/*  954 */       throw new MissingAccessException(textChannel, Permission.VIEW_CHANNEL); 
/*  955 */     if (!getAuthor().equals(getJDA().getSelfUser()) && !getGuild().getSelfMember().hasPermission((GuildChannel)textChannel, new Permission[] { Permission.MESSAGE_MANAGE }))
/*  956 */       throw new InsufficientPermissionException(textChannel, Permission.MESSAGE_MANAGE); 
/*  957 */     return textChannel.crosspostMessageById(getId());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSuppressedEmbeds() {
/*  963 */     return ((this.flags & Message.MessageFlag.EMBEDS_SUPPRESSED.getValue()) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public EnumSet<Message.MessageFlag> getFlags() {
/*  970 */     return Message.MessageFlag.fromBitField(this.flags);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getFlagsRaw() {
/*  976 */     return this.flags;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEphemeral() {
/*  982 */     return ((this.flags & Message.MessageFlag.EPHEMERAL.getValue()) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  988 */     if (o == this)
/*  989 */       return true; 
/*  990 */     if (!(o instanceof ReceivedMessage))
/*  991 */       return false; 
/*  992 */     ReceivedMessage oMsg = (ReceivedMessage)o;
/*  993 */     return (this.id == oMsg.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  999 */     return Long.hashCode(this.id);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1005 */     return (this.author != null) ? 
/* 1006 */       String.format("M:%#s:%.20s(%s)", new Object[] { this.author, this, getId()
/* 1007 */         }) : String.format("M:%.20s", new Object[] { this });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void unsupported() {
/* 1013 */     throw new UnsupportedOperationException("This operation is not supported on received messages!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void formatTo(Formatter formatter, int flags, int width, int precision) {
/* 1019 */     boolean upper = ((flags & 0x2) == 2);
/* 1020 */     boolean leftJustified = ((flags & 0x1) == 1);
/* 1021 */     boolean alt = ((flags & 0x4) == 4);
/*      */     
/* 1023 */     String out = alt ? getContentRaw() : getContentDisplay();
/*      */     
/* 1025 */     if (upper) {
/* 1026 */       out = out.toUpperCase(formatter.locale());
/*      */     }
/* 1028 */     appendFormat(formatter, width, precision, leftJustified, out);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMentions(List<User> users, List<Member> members) {
/* 1033 */     users.sort(Comparator.comparing(user -> Integer.valueOf(Math.max(this.content.indexOf("<@" + user.getId() + ">"), this.content.indexOf("<@!" + user.getId() + ">")))));
/*      */ 
/*      */ 
/*      */     
/* 1037 */     members.sort(Comparator.comparing(user -> Integer.valueOf(Math.max(this.content.indexOf("<@" + user.getId() + ">"), this.content.indexOf("<@!" + user.getId() + ">")))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1042 */     this.userMentions = Collections.unmodifiableList(users);
/* 1043 */     this.memberMentions = Collections.unmodifiableList(members);
/*      */   }
/*      */ 
/*      */   
/*      */   private <T, C extends Collection<T>> C processMentions(Message.MentionType type, C collection, boolean distinct, Function<Matcher, T> map) {
/* 1048 */     Matcher matcher = type.getPattern().matcher(getContentRaw());
/* 1049 */     while (matcher.find()) {
/*      */ 
/*      */       
/*      */       try {
/* 1053 */         T elem = map.apply(matcher);
/* 1054 */         if (elem == null || (distinct && collection.contains(elem)))
/*      */           continue; 
/* 1056 */         collection.add(elem);
/*      */       }
/* 1058 */       catch (NumberFormatException numberFormatException) {}
/*      */     } 
/* 1060 */     return collection;
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FormatToken
/*      */   {
/*      */     public final String format;
/*      */     public final int start;
/*      */     
/*      */     public FormatToken(String format, int start) {
/* 1070 */       this.format = format;
/* 1071 */       this.start = start;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\ReceivedMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */