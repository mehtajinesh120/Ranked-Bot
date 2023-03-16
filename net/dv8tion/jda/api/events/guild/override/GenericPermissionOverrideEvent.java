/*     */ package net.dv8tion.jda.api.events.guild.override;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.StoreChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericPermissionOverrideEvent
/*     */   extends GenericGuildEvent
/*     */ {
/*     */   protected final GuildChannel channel;
/*     */   protected final PermissionOverride override;
/*     */   
/*     */   public GenericPermissionOverrideEvent(@Nonnull JDA api, long responseNumber, @Nonnull GuildChannel channel, @Nonnull PermissionOverride override) {
/*  39 */     super(api, responseNumber, channel.getGuild());
/*  40 */     this.channel = channel;
/*  41 */     this.override = override;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getChannelType() {
/*  52 */     return this.channel.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildChannel getChannel() {
/*  63 */     return this.channel;
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
/*     */   public TextChannel getTextChannel() {
/*  80 */     if (this.channel instanceof TextChannel)
/*  81 */       return (TextChannel)this.channel; 
/*  82 */     throw new IllegalStateException("This override is for a channel of type " + getChannelType());
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
/*     */   public VoiceChannel getVoiceChannel() {
/*  99 */     if (this.channel instanceof VoiceChannel)
/* 100 */       return (VoiceChannel)this.channel; 
/* 101 */     throw new IllegalStateException("This override is for a channel of type " + getChannelType());
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
/*     */   public StoreChannel getStoreChannel() {
/* 118 */     if (this.channel instanceof StoreChannel)
/* 119 */       return (StoreChannel)this.channel; 
/* 120 */     throw new IllegalStateException("This override is for a channel of type " + getChannelType());
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
/*     */   public Category getCategory() {
/* 138 */     if (this.channel instanceof Category)
/* 139 */       return (Category)this.channel; 
/* 140 */     throw new IllegalStateException("This override is for a channel of type " + getChannelType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverride getPermissionOverride() {
/* 151 */     return this.override;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoleOverride() {
/* 162 */     return this.override.isRoleOverride();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMemberOverride() {
/* 173 */     return this.override.isMemberOverride();
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
/*     */   public IPermissionHolder getPermissionHolder() {
/* 185 */     return isMemberOverride() ? (IPermissionHolder)this.override.getMember() : (IPermissionHolder)this.override.getRole();
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
/*     */   public Member getMember() {
/* 197 */     return this.override.getMember();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Role getRole() {
/* 208 */     return this.override.getRole();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\override\GenericPermissionOverrideEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */