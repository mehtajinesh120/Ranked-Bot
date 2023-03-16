/*      */ package net.dv8tion.jda.api;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*      */ import net.dv8tion.jda.annotations.ForRemoval;
/*      */ import net.dv8tion.jda.annotations.ReplaceWith;
/*      */ import net.dv8tion.jda.api.entities.EmbedType;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.IMentionable;
/*      */ import net.dv8tion.jda.api.entities.Member;
/*      */ import net.dv8tion.jda.api.entities.Message;
/*      */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*      */ import net.dv8tion.jda.api.entities.Role;
/*      */ import net.dv8tion.jda.api.entities.TextChannel;
/*      */ import net.dv8tion.jda.api.entities.User;
/*      */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*      */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*      */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*      */ import net.dv8tion.jda.internal.entities.DataMessage;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.Helpers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MessageBuilder
/*      */   implements Appendable
/*      */ {
/*   46 */   protected final StringBuilder builder = new StringBuilder();
/*      */   
/*   48 */   protected final List<MessageEmbed> embeds = new ArrayList<>();
/*   49 */   protected final List<ComponentLayout> components = new ArrayList<>();
/*      */   protected boolean isTTS = false;
/*      */   protected String nonce;
/*   52 */   protected EnumSet<Message.MentionType> allowedMentions = null;
/*   53 */   protected Set<String> mentionedUsers = new HashSet<>();
/*   54 */   protected Set<String> mentionedRoles = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageBuilder(@Nullable CharSequence content) {
/*   60 */     if (content != null) {
/*   61 */       this.builder.append(content);
/*      */     }
/*      */   }
/*      */   
/*      */   public MessageBuilder(@Nullable Message message) {
/*   66 */     if (message != null) {
/*      */       
/*   68 */       this.isTTS = message.isTTS();
/*   69 */       this.builder.append(message.getContentRaw());
/*   70 */       List<MessageEmbed> embeds = message.getEmbeds();
/*   71 */       if (embeds != null) {
/*   72 */         Objects.requireNonNull(this.embeds); embeds.stream().filter(it -> (it.getType() == EmbedType.RICH)).forEach(this.embeds::add);
/*   73 */       }  this.components.addAll(message.getActionRows());
/*   74 */       if (message instanceof DataMessage) {
/*      */         
/*   76 */         DataMessage data = (DataMessage)message;
/*   77 */         if (data.getAllowedMentions() != null)
/*   78 */           this.allowedMentions = Helpers.copyEnumSet(Message.MentionType.class, data.getAllowedMentions()); 
/*   79 */         Collections.addAll(this.mentionedUsers, data.getMentionedUsersWhitelist());
/*   80 */         Collections.addAll(this.mentionedRoles, data.getMentionedRolesWhitelist());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public MessageBuilder(@Nullable MessageBuilder builder) {
/*   87 */     if (builder != null) {
/*      */       
/*   89 */       this.isTTS = builder.isTTS;
/*   90 */       this.builder.append(builder.builder);
/*   91 */       this.nonce = builder.nonce;
/*   92 */       this.embeds.addAll(builder.embeds);
/*   93 */       this.components.addAll(builder.components);
/*   94 */       if (builder.allowedMentions != null)
/*   95 */         this.allowedMentions = Helpers.copyEnumSet(Message.MentionType.class, builder.allowedMentions); 
/*   96 */       this.mentionedRoles.addAll(builder.mentionedRoles);
/*   97 */       this.mentionedUsers.addAll(builder.mentionedUsers);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public MessageBuilder(@Nullable EmbedBuilder builder) {
/*  103 */     if (builder != null) {
/*  104 */       this.embeds.add(builder.build());
/*      */     }
/*      */   }
/*      */   
/*      */   public MessageBuilder(@Nullable MessageEmbed embed) {
/*  109 */     if (embed != null) {
/*  110 */       this.embeds.add(embed);
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
/*      */   @Nonnull
/*      */   public MessageBuilder setTTS(boolean tts) {
/*  126 */     this.isTTS = tts;
/*  127 */     return this;
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
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "5.0.0")
/*      */   @ReplaceWith("setEmbeds(embed)")
/*      */   @DeprecatedSince("4.4.0")
/*      */   public MessageBuilder setEmbed(@Nullable MessageEmbed embed) {
/*  148 */     return (embed == null) ? setEmbeds(new MessageEmbed[0]) : setEmbeds(new MessageEmbed[] { embed });
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
/*      */   @Nonnull
/*      */   public MessageBuilder setEmbeds(@Nonnull MessageEmbed... embeds) {
/*  167 */     Checks.noneNull((Object[])embeds, "MessageEmbeds");
/*  168 */     return setEmbeds(Arrays.asList(embeds));
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
/*      */   @Nonnull
/*      */   public MessageBuilder setEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/*  188 */     Checks.noneNull(embeds, "MessageEmbeds");
/*  189 */     embeds.forEach(embed -> Checks.check(embed.isSendable(), "Provided Message contains an empty embed or an embed with a length greater than %d characters, which is the max for bot accounts!", Integer.valueOf(6000)));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  194 */     Checks.check((embeds.size() <= 10), "Cannot have more than 10 embeds in a message!");
/*  195 */     Checks.check((embeds.stream().mapToInt(MessageEmbed::getLength).sum() <= 6000), "The sum of all MessageEmbeds may not exceed %d!", Integer.valueOf(6000));
/*  196 */     this.embeds.clear();
/*  197 */     this.embeds.addAll(embeds);
/*  198 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder setActionRows(@Nullable Collection<? extends ActionRow> rows) {
/*  215 */     if (rows == null) {
/*      */       
/*  217 */       this.components.clear();
/*  218 */       return this;
/*      */     } 
/*  220 */     Checks.noneNull(rows, "ActionRows");
/*  221 */     Checks.check((rows.size() <= 5), "Can only have 5 action rows per message!");
/*  222 */     this.components.clear();
/*  223 */     this.components.addAll(rows);
/*  224 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder setActionRows(@Nullable ActionRow... rows) {
/*  241 */     if (rows == null) {
/*      */       
/*  243 */       this.components.clear();
/*  244 */       return this;
/*      */     } 
/*  246 */     return setActionRows(Arrays.asList(rows));
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
/*      */   @Nonnull
/*      */   public MessageBuilder setNonce(@Nullable String nonce) {
/*  267 */     this.nonce = nonce;
/*  268 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder setContent(@Nullable String content) {
/*  285 */     if (content == null) {
/*      */       
/*  287 */       this.builder.setLength(0);
/*      */     }
/*      */     else {
/*      */       
/*  291 */       int newLength = Math.max(this.builder.length(), content.length());
/*  292 */       this.builder.replace(0, newLength, content);
/*      */     } 
/*  294 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageBuilder append(@Nullable CharSequence text) {
/*  301 */     this.builder.append(text);
/*  302 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageBuilder append(@Nullable CharSequence text, int start, int end) {
/*  309 */     this.builder.append(text, start, end);
/*  310 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageBuilder append(char c) {
/*  317 */     this.builder.append(c);
/*  318 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder append(@Nullable Object object) {
/*  333 */     return append(String.valueOf(object));
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
/*      */   @Nonnull
/*      */   public MessageBuilder append(@Nonnull IMentionable mention) {
/*  355 */     Checks.notNull(mention, "Mentionable");
/*  356 */     this.builder.append(mention.getAsMention());
/*  357 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder append(@Nullable CharSequence text, @Nonnull Formatting... format) {
/*  373 */     boolean blockPresent = false;
/*  374 */     for (Formatting formatting : format) {
/*      */       
/*  376 */       if (formatting == Formatting.BLOCK) {
/*      */         
/*  378 */         blockPresent = true;
/*      */       } else {
/*      */         
/*  381 */         this.builder.append(formatting.getTag());
/*      */       } 
/*  383 */     }  if (blockPresent) {
/*  384 */       this.builder.append(Formatting.BLOCK.getTag());
/*      */     }
/*  386 */     this.builder.append(text);
/*      */     
/*  388 */     if (blockPresent)
/*  389 */       this.builder.append(Formatting.BLOCK.getTag()); 
/*  390 */     for (int i = format.length - 1; i >= 0; i--) {
/*      */       
/*  392 */       if (format[i] != Formatting.BLOCK)
/*  393 */         this.builder.append(format[i].getTag()); 
/*      */     } 
/*  395 */     return this;
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
/*      */   
/*      */   @Nonnull
/*      */   public MessageBuilder appendFormat(@Nonnull String format, @Nonnull Object... args) {
/*  452 */     Checks.notEmpty(format, "Format String");
/*  453 */     append(String.format(format, args));
/*  454 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder appendCodeLine(@Nullable CharSequence text) {
/*  469 */     append(text, new Formatting[] { Formatting.BLOCK });
/*  470 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder appendCodeBlock(@Nullable CharSequence text, @Nullable CharSequence language) {
/*  488 */     this.builder.append("```").append(language).append('\n').append(text).append("\n```");
/*  489 */     return this;
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
/*      */   public int length() {
/*  506 */     return this.builder.length();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  515 */     return (this.builder.length() == 0 && this.embeds.isEmpty());
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
/*      */   @Nonnull
/*      */   public MessageBuilder replace(@Nonnull String target, @Nonnull String replacement) {
/*  533 */     int index = this.builder.indexOf(target);
/*  534 */     while (index != -1) {
/*      */       
/*  536 */       this.builder.replace(index, index + target.length(), replacement);
/*  537 */       index = this.builder.indexOf(target, index + replacement.length());
/*      */     } 
/*  539 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder replaceFirst(@Nonnull String target, @Nonnull String replacement) {
/*  555 */     int index = this.builder.indexOf(target);
/*  556 */     if (index != -1)
/*      */     {
/*  558 */       this.builder.replace(index, index + target.length(), replacement);
/*      */     }
/*  560 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder replaceLast(@Nonnull String target, @Nonnull String replacement) {
/*  576 */     int index = this.builder.lastIndexOf(target);
/*  577 */     if (index != -1)
/*      */     {
/*  579 */       this.builder.replace(index, index + target.length(), replacement);
/*      */     }
/*  581 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder clearMentionedUsers() {
/*  594 */     this.mentionedUsers.clear();
/*  595 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder clearMentionedRoles() {
/*  608 */     this.mentionedRoles.clear();
/*  609 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder clearMentions() {
/*  623 */     return clearMentionedUsers().clearMentionedRoles();
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
/*      */   @Nonnull
/*      */   public MessageBuilder setAllowedMentions(@Nullable Collection<Message.MentionType> mentionTypes) {
/*  638 */     this
/*      */       
/*  640 */       .allowedMentions = (mentionTypes == null) ? MessageAction.getDefaultMentions() : Helpers.copyEnumSet(Message.MentionType.class, mentionTypes);
/*  641 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder allowMentions(@Nonnull Message.MentionType... types) {
/*  658 */     Checks.noneNull((Object[])types, "MentionTypes");
/*  659 */     if (types.length > 0) {
/*      */       
/*  661 */       if (this.allowedMentions == null)
/*  662 */         this.allowedMentions = MessageAction.getDefaultMentions(); 
/*  663 */       Collections.addAll(this.allowedMentions, types);
/*      */     } 
/*  665 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder denyMentions(@Nonnull Message.MentionType... types) {
/*  682 */     Checks.noneNull((Object[])types, "MentionTypes");
/*  683 */     if (types.length > 0) {
/*      */       
/*  685 */       if (this.allowedMentions == null)
/*  686 */         this.allowedMentions = MessageAction.getDefaultMentions(); 
/*  687 */       for (Message.MentionType type : types)
/*  688 */         this.allowedMentions.remove(type); 
/*      */     } 
/*  690 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder mention(@Nonnull IMentionable... mentions) {
/*  714 */     Checks.noneNull((Object[])mentions, "Mentions");
/*      */     
/*  716 */     for (IMentionable mention : mentions) {
/*      */       
/*  718 */       if (mention instanceof User || mention instanceof Member) {
/*  719 */         this.mentionedUsers.add(mention.getId());
/*  720 */       } else if (mention instanceof Role) {
/*  721 */         this.mentionedRoles.add(mention.getId());
/*      */       } 
/*  723 */     }  return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder mention(@Nonnull Collection<? extends IMentionable> mentions) {
/*  747 */     Checks.noneNull(mentions, "Mentions");
/*  748 */     return mention(mentions.<IMentionable>toArray(new IMentionable[0]));
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
/*      */   @Nonnull
/*      */   public MessageBuilder mentionUsers(@Nonnull String... users) {
/*  773 */     Checks.noneNull((Object[])users, "Users");
/*  774 */     Collections.addAll(this.mentionedUsers, users);
/*  775 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder mentionRoles(@Nonnull String... roles) {
/*  800 */     Checks.noneNull((Object[])roles, "Roles");
/*  801 */     Collections.addAll(this.mentionedRoles, roles);
/*  802 */     return this;
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
/*      */   @Nonnull
/*      */   public MessageBuilder mentionUsers(@Nonnull long... users) {
/*  827 */     Checks.notNull(users, "Users");
/*  828 */     return mentionUsers(toStringArray(users));
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
/*      */   @Nonnull
/*      */   public MessageBuilder mentionRoles(@Nonnull long... roles) {
/*  853 */     Checks.notNull(roles, "Roles");
/*  854 */     return mentionRoles(toStringArray(roles));
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
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @ReplaceWith("setAllowedMentions(Collections.emptyList())")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public MessageBuilder stripMentions(@Nonnull JDA jda) {
/*  880 */     return stripMentions(jda, null, Message.MentionType.values());
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
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @ReplaceWith("setAllowedMentions(Collections.emptyList())")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public MessageBuilder stripMentions(@Nonnull Guild guild) {
/*  907 */     return stripMentions(guild.getJDA(), guild, Message.MentionType.values());
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
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @ReplaceWith("denyMentions(types)")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public MessageBuilder stripMentions(@Nonnull Guild guild, @Nonnull Message.MentionType... types) {
/*  934 */     return stripMentions(guild.getJDA(), guild, types);
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
/*      */   @Nonnull
/*      */   @Deprecated
/*      */   @ForRemoval(deadline = "4.4.0")
/*      */   @ReplaceWith("denyMentions(types)")
/*      */   @DeprecatedSince("4.2.0")
/*      */   public MessageBuilder stripMentions(@Nonnull JDA jda, @Nonnull Message.MentionType... types) {
/*  960 */     return stripMentions(jda, null, types);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   private MessageBuilder stripMentions(JDA jda, Guild guild, Message.MentionType... types) {
/*  966 */     if (types == null) {
/*  967 */       return this;
/*      */     }
/*  969 */     String string = null;
/*      */     
/*  971 */     for (Message.MentionType mention : types) {
/*      */       
/*  973 */       if (mention != null) {
/*      */         Matcher matcher;
/*  975 */         switch (mention) {
/*      */           
/*      */           case EVERYONE:
/*  978 */             replace("@everyone", "@еveryone");
/*      */             break;
/*      */           case HERE:
/*  981 */             replace("@here", "@hеre");
/*      */             break;
/*      */           
/*      */           case CHANNEL:
/*  985 */             if (string == null)
/*      */             {
/*  987 */               string = this.builder.toString();
/*      */             }
/*      */             
/*  990 */             matcher = Message.MentionType.CHANNEL.getPattern().matcher(string);
/*  991 */             while (matcher.find()) {
/*      */               
/*  993 */               TextChannel channel = jda.getTextChannelById(matcher.group(1));
/*  994 */               if (channel != null)
/*      */               {
/*  996 */                 replace(matcher.group(), "#" + channel.getName());
/*      */               }
/*      */             } 
/*      */             break;
/*      */ 
/*      */           
/*      */           case ROLE:
/* 1003 */             if (string == null)
/*      */             {
/* 1005 */               string = this.builder.toString();
/*      */             }
/*      */             
/* 1008 */             matcher = Message.MentionType.ROLE.getPattern().matcher(string);
/* 1009 */             while (matcher.find()) {
/*      */               
/* 1011 */               for (Guild g : jda.getGuilds()) {
/*      */                 
/* 1013 */                 Role role = g.getRoleById(matcher.group(1));
/* 1014 */                 if (role != null)
/*      */                 {
/* 1016 */                   replace(matcher.group(), "@" + role.getName());
/*      */                 }
/*      */               } 
/*      */             } 
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case USER:
/* 1025 */             if (string == null)
/*      */             {
/* 1027 */               string = this.builder.toString();
/*      */             }
/*      */             
/* 1030 */             matcher = Message.MentionType.USER.getPattern().matcher(string);
/* 1031 */             while (matcher.find()) {
/*      */               String replacement;
/* 1033 */               User user = jda.getUserById(matcher.group(1));
/*      */ 
/*      */               
/* 1036 */               if (user == null) {
/*      */                 continue;
/*      */               }
/*      */               
/*      */               Member member;
/* 1041 */               if (guild != null && (member = guild.getMember(user)) != null) {
/* 1042 */                 replacement = member.getEffectiveName();
/*      */               } else {
/* 1044 */                 replacement = user.getName();
/*      */               } 
/* 1046 */               replace(matcher.group(), "@" + replacement);
/*      */             } 
/*      */             break;
/*      */         } 
/*      */ 
/*      */       
/*      */       } 
/*      */     } 
/* 1054 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public StringBuilder getStringBuilder() {
/* 1065 */     return this.builder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public MessageBuilder clear() {
/* 1077 */     this.builder.setLength(0);
/* 1078 */     this.embeds.clear();
/* 1079 */     this.isTTS = false;
/* 1080 */     return this;
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
/*      */   public int indexOf(@Nonnull CharSequence target, int fromIndex, int endIndex) {
/* 1108 */     if (fromIndex < 0)
/* 1109 */       throw new IndexOutOfBoundsException("index out of range: " + fromIndex); 
/* 1110 */     if (endIndex < 0)
/* 1111 */       throw new IndexOutOfBoundsException("index out of range: " + endIndex); 
/* 1112 */     if (fromIndex > length())
/* 1113 */       throw new IndexOutOfBoundsException("fromIndex > length()"); 
/* 1114 */     if (fromIndex > endIndex) {
/* 1115 */       throw new IndexOutOfBoundsException("fromIndex > endIndex");
/*      */     }
/* 1117 */     if (endIndex >= this.builder.length())
/*      */     {
/* 1119 */       endIndex = this.builder.length() - 1;
/*      */     }
/*      */     
/* 1122 */     int targetCount = target.length();
/* 1123 */     if (targetCount == 0)
/*      */     {
/* 1125 */       return fromIndex;
/*      */     }
/*      */     
/* 1128 */     char strFirstChar = target.charAt(0);
/* 1129 */     int max = endIndex + targetCount - 1;
/*      */ 
/*      */     
/* 1132 */     for (int i = fromIndex; i <= max; i++) {
/*      */       
/* 1134 */       if (this.builder.charAt(i) == strFirstChar) {
/*      */         
/* 1136 */         int j = 1; while (true) { if (j < targetCount) {
/*      */             
/* 1138 */             if (this.builder.charAt(i + j) != target.charAt(j))
/*      */               break; 
/*      */             j++;
/*      */             continue;
/*      */           } 
/* 1143 */           return i; }
/*      */       
/*      */       } 
/* 1146 */     }  return -1;
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
/*      */   public int lastIndexOf(@Nonnull CharSequence target, int fromIndex, int endIndex) {
/* 1174 */     if (fromIndex < 0)
/* 1175 */       throw new IndexOutOfBoundsException("index out of range: " + fromIndex); 
/* 1176 */     if (endIndex < 0)
/* 1177 */       throw new IndexOutOfBoundsException("index out of range: " + endIndex); 
/* 1178 */     if (fromIndex > length())
/* 1179 */       throw new IndexOutOfBoundsException("fromIndex > length()"); 
/* 1180 */     if (fromIndex > endIndex) {
/* 1181 */       throw new IndexOutOfBoundsException("fromIndex > endIndex");
/*      */     }
/* 1183 */     if (endIndex >= this.builder.length())
/*      */     {
/* 1185 */       endIndex = this.builder.length() - 1;
/*      */     }
/*      */     
/* 1188 */     int targetCount = target.length();
/* 1189 */     if (targetCount == 0)
/*      */     {
/* 1191 */       return endIndex;
/*      */     }
/*      */     
/* 1194 */     int rightIndex = endIndex - targetCount;
/*      */     
/* 1196 */     if (fromIndex > rightIndex)
/*      */     {
/* 1198 */       fromIndex = rightIndex;
/*      */     }
/*      */     
/* 1201 */     int strLastIndex = targetCount - 1;
/* 1202 */     char strLastChar = target.charAt(strLastIndex);
/*      */     
/* 1204 */     int min = fromIndex + targetCount - 1;
/*      */ 
/*      */     
/* 1207 */     for (int i = endIndex; i >= min; i--) {
/*      */       
/* 1209 */       if (this.builder.charAt(i) == strLastChar) {
/*      */         
/* 1211 */         int j = strLastIndex - 1, k = 1; while (true) { if (j >= 0) {
/*      */             
/* 1213 */             if (this.builder.charAt(i - k) != target.charAt(j))
/*      */               break;  j--;
/*      */             k++;
/*      */             continue;
/*      */           } 
/* 1218 */           return i - target.length() + 1; }
/*      */       
/*      */       } 
/* 1221 */     }  return -1;
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
/*      */   @Nonnull
/*      */   public Message build() {
/* 1243 */     String message = this.builder.toString();
/* 1244 */     if (isEmpty())
/* 1245 */       throw new IllegalStateException("Cannot build a Message with no content. (You never added any content to the message)"); 
/* 1246 */     if (message.length() > 2000) {
/* 1247 */       throw new IllegalStateException("Cannot build a Message with more than 2000 characters. Please limit your input.");
/*      */     }
/* 1249 */     String[] ids = new String[0];
/* 1250 */     return (Message)new DataMessage(this.isTTS, message, this.nonce, this.embeds, this.allowedMentions, this.mentionedUsers
/* 1251 */         .<String>toArray(ids), this.mentionedRoles.<String>toArray(ids), this.components.<ComponentLayout>toArray(new ComponentLayout[0]));
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
/*      */   @Nonnull
/*      */   public Queue<Message> buildAll(@Nullable SplitPolicy... policy) {
/* 1273 */     if (isEmpty()) {
/* 1274 */       throw new UnsupportedOperationException("Cannot build a Message with no content. (You never added any content to the message)");
/*      */     }
/* 1276 */     LinkedList<Message> messages = new LinkedList<>();
/*      */     
/* 1278 */     if (this.builder.length() <= 2000) {
/*      */       
/* 1280 */       messages.add(build());
/* 1281 */       return messages;
/*      */     } 
/*      */     
/* 1284 */     if (policy == null || policy.length == 0) {
/* 1285 */       policy = new SplitPolicy[] { SplitPolicy.ANYWHERE };
/*      */     }
/* 1287 */     int currentBeginIndex = 0;
/*      */ 
/*      */     
/* 1290 */     label27: while (currentBeginIndex < this.builder.length() - 2000) {
/*      */       
/* 1292 */       for (SplitPolicy splitPolicy : policy) {
/*      */         
/* 1294 */         int currentEndIndex = splitPolicy.nextMessage(currentBeginIndex, this);
/* 1295 */         if (currentEndIndex != -1) {
/*      */           
/* 1297 */           messages.add(build(currentBeginIndex, currentEndIndex));
/* 1298 */           currentBeginIndex = currentEndIndex;
/*      */           continue label27;
/*      */         } 
/*      */       } 
/* 1302 */       throw new IllegalStateException("Failed to split the messages");
/*      */     } 
/*      */     
/* 1305 */     if (currentBeginIndex < this.builder.length()) {
/* 1306 */       messages.add(build(currentBeginIndex, this.builder.length()));
/*      */     }
/* 1308 */     if (!this.embeds.isEmpty()) {
/* 1309 */       ((DataMessage)messages.get(messages.size() - 1)).setEmbeds(this.embeds);
/*      */     }
/* 1311 */     return messages;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   protected DataMessage build(int beginIndex, int endIndex) {
/* 1317 */     String[] ids = new String[0];
/* 1318 */     return new DataMessage(this.isTTS, this.builder.substring(beginIndex, endIndex), null, null, this.allowedMentions, this.mentionedUsers
/* 1319 */         .<String>toArray(ids), this.mentionedRoles.<String>toArray(ids), this.components.<ComponentLayout>toArray(new ComponentLayout[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   private String[] toStringArray(long[] users) {
/* 1324 */     String[] ids = new String[users.length];
/* 1325 */     for (int i = 0; i < ids.length; i++)
/* 1326 */       ids[i] = Long.toUnsignedString(users[i]); 
/* 1327 */     return ids;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MessageBuilder() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface SplitPolicy
/*      */   {
/* 1339 */     public static final SplitPolicy NEWLINE = new CharSequenceSplitPolicy("\n", true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1344 */     public static final SplitPolicy SPACE = new CharSequenceSplitPolicy(" ", true);
/*      */     
/*      */     public static final SplitPolicy ANYWHERE;
/*      */     
/*      */     static {
/* 1349 */       ANYWHERE = ((i, b) -> Math.min(i + 2000, b.length()));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int nextMessage(int param1Int, MessageBuilder param1MessageBuilder);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     static SplitPolicy onChars(@Nonnull CharSequence chars, boolean remove) {
/* 1364 */       return new CharSequenceSplitPolicy(chars, remove);
/*      */     }
/*      */ 
/*      */     
/*      */     public static class CharSequenceSplitPolicy
/*      */       implements SplitPolicy
/*      */     {
/*      */       private final boolean remove;
/*      */       
/*      */       private final CharSequence chars;
/*      */ 
/*      */       
/*      */       private CharSequenceSplitPolicy(@Nonnull CharSequence chars, boolean remove) {
/* 1377 */         this.chars = chars;
/* 1378 */         this.remove = remove;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public int nextMessage(int currentBeginIndex, MessageBuilder builder) {
/* 1384 */         int currentEndIndex = builder.lastIndexOf(this.chars, currentBeginIndex, currentBeginIndex + 2000 - (this.remove ? this.chars.length() : 0));
/* 1385 */         if (currentEndIndex < 0)
/*      */         {
/* 1387 */           return -1;
/*      */         }
/*      */ 
/*      */         
/* 1391 */         return currentEndIndex + this.chars.length();
/*      */       }
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
/*      */   public enum Formatting
/*      */   {
/* 1417 */     ITALICS("*"),
/* 1418 */     BOLD("**"),
/* 1419 */     STRIKETHROUGH("~~"),
/* 1420 */     UNDERLINE("__"),
/* 1421 */     BLOCK("`");
/*      */     
/*      */     private final String tag;
/*      */ 
/*      */     
/*      */     Formatting(String tag) {
/* 1427 */       this.tag = tag;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     private String getTag() {
/* 1433 */       return this.tag;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\MessageBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */