/*     */ package net.dv8tion.jda.internal.requests.restaction.pagination;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.exceptions.ParsingException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.entities.UserImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ public class ReactionPaginationActionImpl
/*     */   extends PaginationActionImpl<User, ReactionPaginationAction>
/*     */   implements ReactionPaginationAction
/*     */ {
/*     */   protected final MessageReaction reaction;
/*     */   
/*     */   public ReactionPaginationActionImpl(MessageReaction reaction) {
/*  50 */     super(reaction.getJDA(), Route.Messages.GET_REACTION_USERS.compile(new String[] { reaction.getChannel().getId(), reaction.getMessageId(), getCode(reaction) }, ), 1, 100, 100);
/*  51 */     this.reaction = reaction;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReactionPaginationActionImpl(Message message, String code) {
/*  56 */     super(message.getJDA(), Route.Messages.GET_REACTION_USERS.compile(new String[] { message.getChannel().getId(), message.getId(), code }, ), 1, 100, 100);
/*  57 */     this.reaction = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReactionPaginationActionImpl(MessageChannel channel, String messageId, String code) {
/*  62 */     super(channel.getJDA(), Route.Messages.GET_REACTION_USERS.compile(new String[] { channel.getId(), messageId, code }, ), 1, 100, 100);
/*  63 */     this.reaction = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static String getCode(MessageReaction reaction) {
/*  68 */     MessageReaction.ReactionEmote emote = reaction.getReactionEmote();
/*     */     
/*  70 */     return emote.isEmote() ? (
/*  71 */       emote.getName() + ":" + emote.getId()) : 
/*  72 */       EncodingUtil.encodeUTF8(emote.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageReaction getReaction() {
/*  79 */     if (this.reaction == null)
/*  80 */       throw new IllegalStateException("Cannot get reaction for this action"); 
/*  81 */     return this.reaction;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Route.CompiledRoute finalizeRoute() {
/*  87 */     Route.CompiledRoute route = super.finalizeRoute();
/*     */     
/*  89 */     String after = null;
/*  90 */     String limit = String.valueOf(getLimit());
/*  91 */     long last = this.lastKey;
/*  92 */     if (last != 0L) {
/*  93 */       after = Long.toUnsignedString(last);
/*     */     }
/*  95 */     route = route.withQueryParams(new String[] { "limit", limit });
/*     */     
/*  97 */     if (after != null) {
/*  98 */       route = route.withQueryParams(new String[] { "after", after });
/*     */     }
/* 100 */     return route;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<List<User>> request) {
/* 106 */     EntityBuilder builder = this.api.getEntityBuilder();
/* 107 */     DataArray array = response.getArray();
/* 108 */     List<User> users = new LinkedList<>();
/* 109 */     for (int i = 0; i < array.length(); i++) {
/*     */ 
/*     */       
/*     */       try {
/* 113 */         UserImpl userImpl = builder.createUser(array.getObject(i));
/* 114 */         users.add(userImpl);
/* 115 */         if (this.useCache)
/* 116 */           this.cached.add(userImpl); 
/* 117 */         this.last = (User)userImpl;
/* 118 */         this.lastKey = this.last.getIdLong();
/*     */       }
/* 120 */       catch (ParsingException|NullPointerException e) {
/*     */         
/* 122 */         LOG.warn("Encountered exception in ReactionPagination", e);
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     request.onSuccess(users);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getKey(User it) {
/* 132 */     return it.getIdLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\pagination\ReactionPaginationActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */