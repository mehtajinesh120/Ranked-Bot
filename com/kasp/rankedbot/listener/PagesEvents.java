/*    */ package com.kasp.rankedbot.listener;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
/*    */ import net.dv8tion.jda.api.hooks.ListenerAdapter;
/*    */ 
/*    */ public class PagesEvents extends ListenerAdapter {
/*    */   public void onButtonClick(ButtonClickEvent event) {
/* 12 */     if (event.getButton().getId().startsWith("rankedbot-page-")) {
/* 13 */       Message msg = event.getMessage();
/* 14 */       int number = Integer.parseInt(event.getButton().getId().replace("rankedbot-page-", ""));
/*    */       
/* 16 */       if (number <= -1) {
/* 17 */         event.reply("you're already on the first page").setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/*    */       
/* 22 */       if (((List)Embed.embedPages.get(msg.getId())).size() <= number) {
/* 23 */         event.reply("you're already on the last page").setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/*    */       
/* 28 */       event.deferEdit().queue();
/*    */       
/* 30 */       updatePage(msg, number);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void updatePage(Message msg, int number) {
/* 35 */     Embed embed = ((List<Embed>)Embed.embedPages.get(msg.getId())).get(number);
/*    */     
/* 37 */     embed.setCurrentPage(number);
/* 38 */     msg.editMessageEmbeds(new MessageEmbed[] { embed.build() }).setActionRow(Embed.createButtons(embed.getCurrentPage())).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\listener\PagesEvents.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */