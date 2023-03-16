/*     */ package net.dv8tion.jda.internal.interactions;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.interactions.Interaction;
/*     */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.interactions.ReplyActionImpl;
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
/*     */ public class InteractionImpl
/*     */   implements Interaction
/*     */ {
/*     */   protected final InteractionHookImpl hook;
/*     */   protected final long id;
/*     */   protected final int type;
/*     */   protected final String token;
/*     */   protected final Guild guild;
/*     */   protected final Member member;
/*     */   protected final User user;
/*     */   protected final AbstractChannel channel;
/*     */   protected final JDAImpl api;
/*     */   
/*     */   public InteractionImpl(JDAImpl jda, DataObject data) {
/*  46 */     this.api = jda;
/*  47 */     this.id = data.getUnsignedLong("id");
/*  48 */     this.token = data.getString("token");
/*  49 */     this.type = data.getInt("type");
/*  50 */     this.guild = jda.getGuildById(data.getUnsignedLong("guild_id", 0L));
/*  51 */     this.hook = new InteractionHookImpl(this, (JDA)jda);
/*  52 */     if (this.guild != null) {
/*     */       
/*  54 */       this.member = (Member)jda.getEntityBuilder().createMember((GuildImpl)this.guild, data.getObject("member"));
/*  55 */       jda.getEntityBuilder().updateMemberCache((MemberImpl)this.member);
/*  56 */       this.user = this.member.getUser();
/*  57 */       this.channel = (AbstractChannel)this.guild.getGuildChannelById(data.getUnsignedLong("channel_id"));
/*     */     }
/*     */     else {
/*     */       
/*  61 */       this.member = null;
/*  62 */       long channelId = data.getUnsignedLong("channel_id");
/*  63 */       PrivateChannel channel = jda.getPrivateChannelById(channelId);
/*  64 */       if (channel == null)
/*     */       {
/*  66 */         channel = jda.getEntityBuilder().createPrivateChannel(
/*  67 */             DataObject.empty()
/*  68 */             .put("id", Long.valueOf(channelId))
/*  69 */             .put("recipient", data.getObject("user")));
/*     */       }
/*     */       
/*  72 */       this.channel = (AbstractChannel)channel;
/*  73 */       this.user = channel.getUser();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionImpl(long id, int type, String token, Guild guild, Member member, User user, AbstractChannel channel) {
/*  79 */     this.id = id;
/*  80 */     this.type = type;
/*  81 */     this.token = token;
/*  82 */     this.guild = guild;
/*  83 */     this.member = member;
/*  84 */     this.user = user;
/*  85 */     this.channel = channel;
/*  86 */     this.api = (JDAImpl)user.getJDA();
/*  87 */     this.hook = new InteractionHookImpl(this, (JDA)this.api);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  93 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTypeRaw() {
/*  99 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getToken() {
/* 106 */     return this.token;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Guild getGuild() {
/* 113 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AbstractChannel getChannel() {
/* 120 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InteractionHook getHook() {
/* 127 */     return this.hook;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getUser() {
/* 134 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Member getMember() {
/* 141 */     return this.member;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAcknowledged() {
/* 147 */     return this.hook.isAck();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyActionImpl deferReply() {
/* 154 */     return new ReplyActionImpl(this.hook);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 161 */     return (JDA)this.api;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\InteractionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */