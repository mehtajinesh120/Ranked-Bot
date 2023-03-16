/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
/*    */ import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
/*    */ import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
/*    */ import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
/*    */ import net.dv8tion.jda.api.interactions.Interaction;
/*    */ import net.dv8tion.jda.api.interactions.InteractionType;
/*    */ import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
/*    */ import net.dv8tion.jda.api.interactions.components.Component;
/*    */ import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.interactions.ButtonInteractionImpl;
/*    */ import net.dv8tion.jda.internal.interactions.CommandInteractionImpl;
/*    */ import net.dv8tion.jda.internal.interactions.InteractionImpl;
/*    */ import net.dv8tion.jda.internal.interactions.SelectionMenuInteractionImpl;
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
/*    */ public class InteractionCreateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public InteractionCreateHandler(JDAImpl api) {
/* 37 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 43 */     int type = content.getInt("type");
/* 44 */     if (content.getInt("version", 1) != 1) {
/*    */       
/* 46 */       WebSocketClient.LOG.debug("Received interaction with version {}. This version is currently unsupported by this version of JDA. Consider updating!", Integer.valueOf(content.getInt("version", 1)));
/* 47 */       return null;
/*    */     } 
/*    */     
/* 50 */     long guildId = content.getUnsignedLong("guild_id", 0L);
/* 51 */     if (this.api.getGuildSetupController().isLocked(guildId))
/* 52 */       return Long.valueOf(guildId); 
/* 53 */     if (guildId != 0L && this.api.getGuildById(guildId) == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     switch (InteractionType.fromKey(type))
/*    */     
/*    */     { case BUTTON:
/* 59 */         handleCommand(content);
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
/* 70 */         return null;case SELECTION_MENU: handleAction(content); return null; }  this.api.handleEvent((GenericEvent)new GenericInteractionCreateEvent((JDA)this.api, this.responseNumber, (Interaction)new InteractionImpl(this.api, content))); return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private void handleCommand(DataObject content) {
/* 75 */     this.api.handleEvent((GenericEvent)new SlashCommandEvent((JDA)this.api, this.responseNumber, new CommandInteractionImpl(this.api, content)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void handleAction(DataObject content) {
/* 82 */     switch (Component.Type.fromKey(content.getObject("data").getInt("component_type"))) {
/*    */       
/*    */       case BUTTON:
/* 85 */         this.api.handleEvent((GenericEvent)new ButtonClickEvent((JDA)this.api, this.responseNumber, (ButtonInteraction)new ButtonInteractionImpl(this.api, content)));
/*    */         break;
/*    */ 
/*    */       
/*    */       case SELECTION_MENU:
/* 90 */         this.api.handleEvent((GenericEvent)new SelectionMenuEvent((JDA)this.api, this.responseNumber, (SelectionMenuInteraction)new SelectionMenuInteractionImpl(this.api, content)));
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\InteractionCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */