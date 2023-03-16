/*     */ package net.dv8tion.jda.internal.requests.restaction.pagination;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.exceptions.ParsingException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.entities.ReceivedMessage;
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
/*     */ public class MessagePaginationActionImpl
/*     */   extends PaginationActionImpl<Message, MessagePaginationAction>
/*     */   implements MessagePaginationAction
/*     */ {
/*     */   private final MessageChannel channel;
/*     */   
/*     */   public MessagePaginationActionImpl(MessageChannel channel) {
/*  43 */     super(channel.getJDA(), Route.Messages.GET_MESSAGE_HISTORY.compile(new String[] { channel.getId() }, ), 1, 100, 100);
/*     */     
/*  45 */     if (channel.getType() == ChannelType.TEXT) {
/*     */       
/*  47 */       TextChannel textChannel = (TextChannel)channel;
/*  48 */       Member selfMember = textChannel.getGuild().getSelfMember();
/*  49 */       if (!selfMember.hasAccess((GuildChannel)textChannel))
/*  50 */         throw new MissingAccessException(textChannel, Permission.VIEW_CHANNEL); 
/*  51 */       if (!selfMember.hasPermission((GuildChannel)textChannel, new Permission[] { Permission.MESSAGE_HISTORY })) {
/*  52 */         throw new InsufficientPermissionException(textChannel, Permission.MESSAGE_HISTORY);
/*     */       }
/*     */     } 
/*  55 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageChannel getChannel() {
/*  62 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Route.CompiledRoute finalizeRoute() {
/*  68 */     Route.CompiledRoute route = super.finalizeRoute();
/*     */     
/*  70 */     String limit = String.valueOf(getLimit());
/*  71 */     long last = this.lastKey;
/*     */     
/*  73 */     route = route.withQueryParams(new String[] { "limit", limit });
/*     */     
/*  75 */     if (last != 0L) {
/*  76 */       route = route.withQueryParams(new String[] { "before", Long.toUnsignedString(last) });
/*     */     }
/*  78 */     return route;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<List<Message>> request) {
/*  84 */     DataArray array = response.getArray();
/*  85 */     List<Message> messages = new ArrayList<>(array.length());
/*  86 */     EntityBuilder builder = this.api.getEntityBuilder();
/*  87 */     for (int i = 0; i < array.length(); i++) {
/*     */ 
/*     */       
/*     */       try {
/*  91 */         ReceivedMessage receivedMessage = builder.createMessage(array.getObject(i), this.channel, false);
/*  92 */         messages.add(receivedMessage);
/*  93 */         if (this.useCache)
/*  94 */           this.cached.add(receivedMessage); 
/*  95 */         this.last = (Message)receivedMessage;
/*  96 */         this.lastKey = this.last.getIdLong();
/*     */       }
/*  98 */       catch (ParsingException|NullPointerException e) {
/*     */         
/* 100 */         LOG.warn("Encountered an exception in MessagePagination", e);
/*     */       }
/* 102 */       catch (IllegalArgumentException e) {
/*     */         
/* 104 */         if ("UNKNOWN_MESSAGE_TYPE".equals(e.getMessage())) {
/* 105 */           LOG.warn("Skipping unknown message type during pagination", e);
/*     */         } else {
/* 107 */           LOG.warn("Unexpected issue trying to parse message during pagination", e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 111 */     request.onSuccess(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getKey(Message it) {
/* 117 */     return it.getIdLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\pagination\MessagePaginationActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */