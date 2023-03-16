/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.IMentionable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.utils.AllowedMentions;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
/*     */ import net.dv8tion.jda.internal.entities.DataMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AllowedMentionsImpl
/*     */   implements SerializableData, AllowedMentions<AllowedMentionsImpl>
/*     */ {
/*  32 */   private static EnumSet<Message.MentionType> defaultParse = EnumSet.allOf(Message.MentionType.class);
/*     */   private static boolean defaultMentionRepliedUser = true;
/*  34 */   private EnumSet<Message.MentionType> parse = getDefaultMentions();
/*  35 */   private final Set<String> users = new HashSet<>();
/*  36 */   private final Set<String> roles = new HashSet<>();
/*  37 */   private boolean mentionRepliedUser = defaultMentionRepliedUser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
/*  43 */     defaultParse = (allowedMentions == null) ? EnumSet.<Message.MentionType>allOf(Message.MentionType.class) : Helpers.<Message.MentionType>copyEnumSet(Message.MentionType.class, allowedMentions);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<Message.MentionType> getDefaultMentions() {
/*  49 */     return defaultParse.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDefaultMentionRepliedUser(boolean mention) {
/*  54 */     defaultMentionRepliedUser = mention;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isDefaultMentionRepliedUser() {
/*  59 */     return defaultMentionRepliedUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/*  66 */     DataObject allowedMentionsObj = DataObject.empty();
/*  67 */     DataArray parsable = DataArray.empty();
/*  68 */     if (this.parse != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       Objects.requireNonNull(parsable); this.parse.stream().map(Message.MentionType::getParseKey).filter(Objects::nonNull).distinct().forEach(parsable::add);
/*     */     } 
/*  77 */     if (!this.users.isEmpty()) {
/*     */ 
/*     */       
/*  80 */       parsable.remove(Message.MentionType.USER.getParseKey());
/*  81 */       allowedMentionsObj.put("users", DataArray.fromCollection(this.users));
/*     */     } 
/*  83 */     if (!this.roles.isEmpty()) {
/*     */ 
/*     */       
/*  86 */       parsable.remove(Message.MentionType.ROLE.getParseKey());
/*  87 */       allowedMentionsObj.put("roles", DataArray.fromCollection(this.roles));
/*     */     } 
/*  89 */     allowedMentionsObj.put("replied_user", Boolean.valueOf(this.mentionRepliedUser));
/*  90 */     return allowedMentionsObj.put("parse", parsable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AllowedMentionsImpl applyMessage(Message message) {
/*  97 */     if (message instanceof DataMessage) {
/*     */       
/*  99 */       DataMessage data = (DataMessage)message;
/* 100 */       String[] mentionedRoles = data.getMentionedRolesWhitelist();
/* 101 */       String[] mentionedUsers = data.getMentionedUsersWhitelist();
/* 102 */       EnumSet<Message.MentionType> allowedMentions = data.getAllowedMentions();
/* 103 */       if (allowedMentions != null)
/* 104 */         allowedMentions(allowedMentions); 
/* 105 */       mentionRoles(mentionedRoles);
/* 106 */       mentionUsers(mentionedUsers);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 111 */       if (message.mentionsEveryone()) {
/*     */         
/* 113 */         String content = message.getContentRaw();
/* 114 */         EnumSet<Message.MentionType> parse = EnumSet.noneOf(Message.MentionType.class);
/* 115 */         if (content.contains("@everyone"))
/* 116 */           parse.add(Message.MentionType.EVERYONE); 
/* 117 */         if (content.contains("@here"))
/* 118 */           parse.add(Message.MentionType.HERE); 
/* 119 */         this.parse = parse;
/*     */       }
/*     */       else {
/*     */         
/* 123 */         this.parse = EnumSet.noneOf(Message.MentionType.class);
/*     */       } 
/*     */       
/* 126 */       ((AllowedMentionsImpl)mention(message.getMentionedUsers()))
/* 127 */         .mention(message.getMentionedRoles());
/*     */     } 
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AllowedMentionsImpl mentionRepliedUser(boolean mention) {
/* 136 */     this.mentionRepliedUser = mention;
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AllowedMentionsImpl allowedMentions(@Nullable Collection<Message.MentionType> allowedMentions) {
/* 144 */     this
/*     */       
/* 146 */       .parse = (allowedMentions == null) ? EnumSet.<Message.MentionType>allOf(Message.MentionType.class) : Helpers.<Message.MentionType>copyEnumSet(Message.MentionType.class, allowedMentions);
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AllowedMentionsImpl mention(@Nonnull IMentionable... mentions) {
/* 154 */     Checks.noneNull((Object[])mentions, "Mentionables");
/* 155 */     for (IMentionable mentionable : mentions) {
/*     */       
/* 157 */       if (mentionable instanceof net.dv8tion.jda.api.entities.User || mentionable instanceof net.dv8tion.jda.api.entities.Member) {
/* 158 */         this.users.add(mentionable.getId());
/* 159 */       } else if (mentionable instanceof net.dv8tion.jda.api.entities.Role) {
/* 160 */         this.roles.add(mentionable.getId());
/*     */       } 
/* 162 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AllowedMentionsImpl mentionUsers(@Nonnull String... userIds) {
/* 169 */     Checks.noneNull((Object[])userIds, "User Id");
/* 170 */     Collections.addAll(this.users, userIds);
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AllowedMentionsImpl mentionRoles(@Nonnull String... roleIds) {
/* 178 */     Checks.noneNull((Object[])roleIds, "Role Id");
/* 179 */     Collections.addAll(this.roles, roleIds);
/* 180 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\AllowedMentionsImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */