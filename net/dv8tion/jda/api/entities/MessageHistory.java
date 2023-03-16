/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.apache.commons.collections4.map.ListOrderedMap;
/*     */ import org.slf4j.Logger;
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
/*     */ public class MessageHistory
/*     */ {
/*     */   protected final MessageChannel channel;
/*  60 */   protected static final Logger LOG = JDALogger.getLog(MessageHistory.class);
/*     */   
/*  62 */   protected final ListOrderedMap<Long, Message> history = new ListOrderedMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageHistory(@Nonnull MessageChannel channel) {
/*  72 */     Checks.notNull(channel, "Channel");
/*  73 */     this.channel = channel;
/*  74 */     if (channel instanceof TextChannel) {
/*     */       
/*  76 */       TextChannel tc = (TextChannel)channel;
/*  77 */       Member selfMember = tc.getGuild().getSelfMember();
/*  78 */       if (!selfMember.hasAccess(tc))
/*  79 */         throw new MissingAccessException(tc, Permission.VIEW_CHANNEL); 
/*  80 */       if (!selfMember.hasPermission(tc, new Permission[] { Permission.MESSAGE_HISTORY })) {
/*  81 */         throw new InsufficientPermissionException(tc, Permission.MESSAGE_HISTORY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  93 */     return this.channel.getJDA();
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
/*     */   public int size() {
/* 106 */     return this.history.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 116 */     return (size() == 0);
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
/*     */   public MessageChannel getChannel() {
/* 128 */     return this.channel;
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
/*     */   @CheckReturnValue
/*     */   public RestAction<List<Message>> retrievePast(int amount) {
/* 186 */     if (amount > 100 || amount < 1) {
/* 187 */       throw new IllegalArgumentException("Message retrieval limit is between 1 and 100 messages. No more, no less. Limit provided: " + amount);
/*     */     }
/* 189 */     Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(new String[] { this.channel.getId() }).withQueryParams(new String[] { "limit", Integer.toString(amount) });
/*     */     
/* 191 */     if (!this.history.isEmpty()) {
/* 192 */       route = route.withQueryParams(new String[] { "before", String.valueOf(this.history.lastKey()) });
/*     */     }
/* 194 */     JDAImpl jda = (JDAImpl)getJDA();
/* 195 */     return (RestAction<List<Message>>)new RestActionImpl((JDA)jda, route, (response, request) -> {
/*     */           EntityBuilder builder = jda.getEntityBuilder();
/*     */ 
/*     */           
/*     */           LinkedList<Message> messages = new LinkedList<>();
/*     */ 
/*     */           
/*     */           DataArray historyJson = response.getArray();
/*     */           
/*     */           for (int i = 0; i < historyJson.length(); i++) {
/*     */             try {
/*     */               messages.add(builder.createMessage(historyJson.getObject(i), this.channel, false));
/* 207 */             } catch (Exception e) {
/*     */               LOG.warn("Encountered exception when retrieving messages ", e);
/*     */             } 
/*     */           } 
/*     */           messages.forEach(());
/*     */           return messages;
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
/*     */   @CheckReturnValue
/*     */   public RestAction<List<Message>> retrieveFuture(int amount) {
/* 265 */     if (amount > 100 || amount < 1) {
/* 266 */       throw new IllegalArgumentException("Message retrieval limit is between 1 and 100 messages. No more, no less. Limit provided: " + amount);
/*     */     }
/* 268 */     if (this.history.isEmpty()) {
/* 269 */       throw new IllegalStateException("No messages have been retrieved yet, so there is no message to act as a marker to retrieve more recent messages based on.");
/*     */     }
/* 271 */     Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(new String[] { this.channel.getId() }).withQueryParams(new String[] { "limit", Integer.toString(amount), "after", String.valueOf(this.history.firstKey()) });
/* 272 */     JDAImpl jda = (JDAImpl)getJDA();
/* 273 */     return (RestAction<List<Message>>)new RestActionImpl((JDA)jda, route, (response, request) -> {
/*     */           EntityBuilder builder = jda.getEntityBuilder();
/*     */ 
/*     */           
/*     */           LinkedList<Message> messages = new LinkedList<>();
/*     */ 
/*     */           
/*     */           DataArray historyJson = response.getArray();
/*     */           
/*     */           for (int i = 0; i < historyJson.length(); i++) {
/*     */             try {
/*     */               messages.add(builder.createMessage(historyJson.getObject(i), this.channel, false));
/* 285 */             } catch (Exception e) {
/*     */               LOG.warn("Encountered exception when retrieving messages ", e);
/*     */             } 
/*     */           } 
/*     */           Iterator<Message> it = messages.descendingIterator();
/*     */           while (it.hasNext()) {
/*     */             Message m = it.next();
/*     */             this.history.put(0, Long.valueOf(m.getIdLong()), m);
/*     */           } 
/*     */           return messages;
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Message> getRetrievedHistory() {
/* 314 */     int size = size();
/* 315 */     if (size == 0)
/* 316 */       return Collections.emptyList(); 
/* 317 */     if (size == 1)
/* 318 */       return Collections.singletonList((Message)this.history.getValue(0)); 
/* 319 */     return Collections.unmodifiableList(new ArrayList<>(this.history.values()));
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
/*     */   @Nullable
/*     */   public Message getMessageById(@Nonnull String id) {
/* 343 */     return getMessageById(MiscUtil.parseSnowflake(id));
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
/*     */   @Nullable
/*     */   public Message getMessageById(long id) {
/* 362 */     return (Message)this.history.get(Long.valueOf(id));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static MessageRetrieveAction getHistoryAfter(@Nonnull MessageChannel channel, @Nonnull String messageId) {
/* 405 */     checkArguments(channel, messageId);
/* 406 */     Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(new String[] { channel.getId() }).withQueryParams(new String[] { "after", messageId });
/* 407 */     return new MessageRetrieveAction(route, channel);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static MessageRetrieveAction getHistoryBefore(@Nonnull MessageChannel channel, @Nonnull String messageId) {
/* 450 */     checkArguments(channel, messageId);
/* 451 */     Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(new String[] { channel.getId() }).withQueryParams(new String[] { "before", messageId });
/* 452 */     return new MessageRetrieveAction(route, channel);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static MessageRetrieveAction getHistoryAround(@Nonnull MessageChannel channel, @Nonnull String messageId) {
/* 495 */     checkArguments(channel, messageId);
/* 496 */     Route.CompiledRoute route = Route.Messages.GET_MESSAGE_HISTORY.compile(new String[] { channel.getId() }).withQueryParams(new String[] { "around", messageId });
/* 497 */     return new MessageRetrieveAction(route, channel);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static MessageRetrieveAction getHistoryFromBeginning(@Nonnull MessageChannel channel) {
/* 528 */     return getHistoryAfter(channel, "0");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkArguments(MessageChannel channel, String messageId) {
/* 533 */     Checks.isSnowflake(messageId, "Message ID");
/* 534 */     Checks.notNull(channel, "Channel");
/* 535 */     if (channel.getType() == ChannelType.TEXT) {
/*     */       
/* 537 */       TextChannel t = (TextChannel)channel;
/* 538 */       Member selfMember = t.getGuild().getSelfMember();
/* 539 */       if (!selfMember.hasAccess(t))
/* 540 */         throw new MissingAccessException(t, Permission.VIEW_CHANNEL); 
/* 541 */       if (!selfMember.hasPermission(t, new Permission[] { Permission.MESSAGE_HISTORY })) {
/* 542 */         throw new InsufficientPermissionException(t, Permission.MESSAGE_HISTORY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static class MessageRetrieveAction
/*     */     extends RestActionImpl<MessageHistory>
/*     */   {
/*     */     private final MessageChannel channel;
/*     */     
/*     */     private Integer limit;
/*     */ 
/*     */     
/*     */     protected MessageRetrieveAction(Route.CompiledRoute route, MessageChannel channel) {
/* 557 */       super(channel.getJDA(), route);
/* 558 */       this.channel = channel;
/*     */     }
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
/*     */     @Nonnull
/*     */     @CheckReturnValue
/*     */     public MessageRetrieveAction limit(@Nullable Integer limit) {
/* 576 */       if (limit != null) {
/*     */         
/* 578 */         Checks.positive(limit.intValue(), "Limit");
/* 579 */         Checks.check((limit.intValue() <= 100), "Limit may not exceed 100!");
/*     */       } 
/* 581 */       this.limit = limit;
/* 582 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Route.CompiledRoute finalizeRoute() {
/* 588 */       Route.CompiledRoute route = super.finalizeRoute();
/* 589 */       return (this.limit == null) ? route : route.withQueryParams(new String[] { "limit", String.valueOf(this.limit) });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void handleSuccess(Response response, Request<MessageHistory> request) {
/* 595 */       MessageHistory result = new MessageHistory(this.channel);
/* 596 */       DataArray array = response.getArray();
/* 597 */       EntityBuilder builder = this.api.getEntityBuilder();
/* 598 */       for (int i = 0; i < array.length(); i++) {
/*     */ 
/*     */         
/*     */         try {
/* 602 */           DataObject obj = array.getObject(i);
/* 603 */           result.history.put(Long.valueOf(obj.getLong("id")), builder.createMessage(obj, this.channel, false));
/*     */         }
/* 605 */         catch (Exception e) {
/*     */           
/* 607 */           LOG.warn("Encountered exception in MessagePagination", e);
/*     */         } 
/*     */       } 
/* 610 */       request.onSuccess(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */