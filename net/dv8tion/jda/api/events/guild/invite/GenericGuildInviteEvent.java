/*     */ package net.dv8tion.jda.api.events.guild.invite;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.StageChannel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericGuildInviteEvent
/*     */   extends GenericGuildEvent
/*     */ {
/*     */   private final String code;
/*     */   private final GuildChannel channel;
/*     */   
/*     */   public GenericGuildInviteEvent(@Nonnull JDA api, long responseNumber, @Nonnull String code, @Nonnull GuildChannel channel) {
/*  43 */     super(api, responseNumber, channel.getGuild());
/*  44 */     this.code = code;
/*  45 */     this.channel = channel;
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
/*     */   public String getCode() {
/*  57 */     return this.code;
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
/*     */   public String getUrl() {
/*  69 */     return "https://discord.gg/" + this.code;
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
/*  80 */     return this.channel;
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
/*  91 */     return this.channel.getType();
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
/* 108 */     if (getChannelType() != ChannelType.TEXT)
/* 109 */       throw new IllegalStateException("The channel is not of type TEXT"); 
/* 110 */     return (TextChannel)getChannel();
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
/* 127 */     if (!(this.channel instanceof VoiceChannel))
/* 128 */       throw new IllegalStateException("The channel is not of type VOICE or STAGE"); 
/* 129 */     return (VoiceChannel)getChannel();
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
/*     */   public StageChannel getStageChannel() {
/* 146 */     if (getChannelType() != ChannelType.STAGE)
/* 147 */       throw new IllegalStateException("The channel is not of type STAGE"); 
/* 148 */     return (StageChannel)getChannel();
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
/* 165 */     if (getChannelType() != ChannelType.STORE)
/* 166 */       throw new IllegalStateException("The channel is not of type STORE"); 
/* 167 */     return (StoreChannel)getChannel();
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
/*     */   public Category getCategory() {
/* 184 */     if (getChannelType() != ChannelType.CATEGORY)
/* 185 */       throw new IllegalStateException("The channel is not of type CATEGORY"); 
/* 186 */     return (Category)getChannel();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\invite\GenericGuildInviteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */