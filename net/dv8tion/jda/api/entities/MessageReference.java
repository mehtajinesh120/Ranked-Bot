/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.ReceivedMessage;
/*     */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ public class MessageReference
/*     */ {
/*     */   private final long messageId;
/*     */   private final long channelId;
/*     */   private final long guildId;
/*     */   private final JDA api;
/*     */   private final MessageChannel channel;
/*     */   private final Guild guild;
/*     */   private Message referencedMessage;
/*     */   
/*     */   public MessageReference(long messageId, long channelId, long guildId, @Nullable Message referencedMessage, JDA api) {
/*  48 */     this.messageId = messageId;
/*  49 */     this.channelId = channelId;
/*  50 */     this.guildId = guildId;
/*  51 */     this.referencedMessage = referencedMessage;
/*     */     
/*  53 */     if (guildId == 0L) {
/*  54 */       this.channel = api.getPrivateChannelById(channelId);
/*     */     } else {
/*  56 */       this.channel = (MessageChannel)api.getGuildChannelById(channelId);
/*     */     } 
/*  58 */     this.guild = api.getGuildById(guildId);
/*     */     
/*  60 */     this.api = api;
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
/*     */   public RestAction<Message> resolve() {
/* 100 */     return resolve(true);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Message> resolve(boolean update) {
/* 143 */     checkPermission(Permission.MESSAGE_READ);
/* 144 */     checkPermission(Permission.MESSAGE_HISTORY);
/*     */     
/* 146 */     if (this.channel == null) {
/* 147 */       throw new IllegalStateException("Cannot resolve a message without a channel present.");
/*     */     }
/* 149 */     JDAImpl jda = (JDAImpl)getJDA();
/* 150 */     Message referenced = getMessage();
/*     */     
/* 152 */     if (referenced != null && !update) {
/* 153 */       return (RestAction<Message>)new CompletedRestAction((JDA)jda, referenced);
/*     */     }
/* 155 */     Route.CompiledRoute route = Route.Messages.GET_MESSAGE.compile(new String[] { getChannelId(), getMessageId() });
/* 156 */     return (RestAction<Message>)new RestActionImpl((JDA)jda, route, (response, request) -> {
/*     */           ReceivedMessage receivedMessage = jda.getEntityBuilder().createMessage(response.getObject(), getChannel(), false);
/*     */           this.referencedMessage = (Message)receivedMessage;
/*     */           return (Message)receivedMessage;
/*     */         });
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
/*     */   @Nullable
/*     */   public Message getMessage() {
/* 177 */     return this.referencedMessage;
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
/*     */   @Nullable
/*     */   public MessageChannel getChannel() {
/* 191 */     return this.channel;
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
/*     */   @Nullable
/*     */   public Guild getGuild() {
/* 206 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMessageIdLong() {
/* 216 */     return this.messageId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getChannelIdLong() {
/* 226 */     return this.channelId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getGuildIdLong() {
/* 236 */     return this.guildId;
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
/* 247 */     return Long.toUnsignedString(getMessageIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getChannelId() {
/* 258 */     return Long.toUnsignedString(getChannelIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getGuildId() {
/* 269 */     return Long.toUnsignedString(getGuildIdLong());
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
/* 280 */     return this.api;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkPermission(Permission permission) {
/* 285 */     if (this.guild == null || !(this.channel instanceof GuildChannel))
/*     */       return; 
/* 287 */     Member selfMember = this.guild.getSelfMember();
/* 288 */     GuildChannel guildChannel = (GuildChannel)this.channel;
/*     */     
/* 290 */     if (!selfMember.hasAccess(guildChannel))
/* 291 */       throw new MissingAccessException(guildChannel, Permission.VIEW_CHANNEL); 
/* 292 */     if (!selfMember.hasPermission(guildChannel, new Permission[] { permission }))
/* 293 */       throw new InsufficientPermissionException(guildChannel, permission); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */