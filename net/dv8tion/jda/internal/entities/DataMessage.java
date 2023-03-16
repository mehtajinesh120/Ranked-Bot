/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageActivity;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.MessageType;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataMessage
/*     */   extends AbstractMessage
/*     */ {
/*     */   private final EnumSet<Message.MentionType> allowedMentions;
/*     */   private final String[] mentionedRoles;
/*     */   private final String[] mentionedUsers;
/*     */   private final ComponentLayout[] components;
/*     */   private Collection<? extends MessageEmbed> embeds;
/*     */   
/*     */   public DataMessage(boolean tts, String content, String nonce, Collection<? extends MessageEmbed> embeds, EnumSet<Message.MentionType> allowedMentions, String[] mentionedUsers, String[] mentionedRoles, ComponentLayout[] components) {
/*  41 */     super(content, nonce, tts);
/*  42 */     this.embeds = embeds;
/*  43 */     this.allowedMentions = allowedMentions;
/*  44 */     this.mentionedUsers = mentionedUsers;
/*  45 */     this.mentionedRoles = mentionedRoles;
/*  46 */     this.components = components;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataMessage(boolean tts, String content, String nonce, Collection<? extends MessageEmbed> embeds) {
/*  51 */     this(tts, content, nonce, embeds, (EnumSet<Message.MentionType>)null, new String[0], new String[0], new ComponentLayout[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumSet<Message.MentionType> getAllowedMentions() {
/*  56 */     return this.allowedMentions;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMentionedRolesWhitelist() {
/*  61 */     return this.mentionedRoles;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMentionedUsersWhitelist() {
/*  66 */     return this.mentionedUsers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageType getType() {
/*  73 */     return MessageType.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  79 */     if (this == o)
/*  80 */       return true; 
/*  81 */     if (!(o instanceof DataMessage))
/*  82 */       return false; 
/*  83 */     DataMessage other = (DataMessage)o;
/*  84 */     return (this.isTTS == other.isTTS && other.content
/*  85 */       .equals(this.content) && 
/*  86 */       Objects.equals(other.nonce, this.nonce) && 
/*  87 */       Objects.equals(other.embeds, this.embeds));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  93 */     return System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return String.format("DataMessage(%.30s)", new Object[] { getContentRaw() });
/*     */   }
/*     */ 
/*     */   
/*     */   public DataMessage setEmbeds(Collection<? extends MessageEmbed> embeds) {
/* 104 */     this.embeds = embeds;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<MessageEmbed> getEmbeds() {
/* 112 */     return (this.embeds == null) ? Collections.<MessageEmbed>emptyList() : new ArrayList<>(this.embeds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<ActionRow> getActionRows() {
/* 121 */     Objects.requireNonNull(ActionRow.class);
/* 122 */     Objects.requireNonNull(ActionRow.class);
/* 123 */     return (this.components == null) ? Collections.<ActionRow>emptyList() : (List<ActionRow>)Arrays.<ComponentLayout>stream(this.components).filter(ActionRow.class::isInstance).map(ActionRow.class::cast).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void unsupported() {
/* 131 */     throw new UnsupportedOperationException("This operation is not supported for Messages that were created by a MessageBuilder!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MessageActivity getActivity() {
/* 138 */     unsupported();
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 145 */     unsupported();
/* 146 */     return 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\DataMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */