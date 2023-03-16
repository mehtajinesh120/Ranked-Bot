/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.ChannelType;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
/*    */ import net.dv8tion.jda.api.events.channel.store.StoreChannelCreateEvent;
/*    */ import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
/*    */ import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*    */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChannelCreateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public ChannelCreateHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     ChannelType type = ChannelType.fromId(content.getInt("type"));
/*    */     
/* 41 */     long guildId = 0L;
/* 42 */     JDAImpl jda = getJDA();
/* 43 */     if (type.isGuild()) {
/*    */       
/* 45 */       guildId = content.getLong("guild_id");
/* 46 */       if (jda.getGuildSetupController().isLocked(guildId)) {
/* 47 */         return Long.valueOf(guildId);
/*    */       }
/*    */     } 
/* 50 */     EntityBuilder builder = jda.getEntityBuilder();
/* 51 */     switch (type)
/*    */     
/*    */     { 
/*    */       case STORE:
/* 55 */         builder.createStoreChannel(content, guildId);
/* 56 */         jda.handleEvent((GenericEvent)new StoreChannelCreateEvent((JDA)jda, this.responseNumber, builder
/*    */ 
/*    */               
/* 59 */               .createStoreChannel(content, guildId)));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 93 */         return null;case TEXT: jda.handleEvent((GenericEvent)new TextChannelCreateEvent((JDA)jda, this.responseNumber, builder.createTextChannel(content, guildId))); return null;case STAGE: case VOICE: jda.handleEvent((GenericEvent)new VoiceChannelCreateEvent((JDA)jda, this.responseNumber, builder.createVoiceChannel(content, guildId))); return null;case CATEGORY: jda.handleEvent((GenericEvent)new CategoryCreateEvent((JDA)jda, this.responseNumber, builder.createCategory(content, guildId))); return null;case GROUP: WebSocketClient.LOG.warn("Received a CREATE_CHANNEL for a group which is not supported"); return null; }  WebSocketClient.LOG.debug("Discord provided an CREATE_CHANNEL event with an unknown channel type! JSON: {}", content); return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\ChannelCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */