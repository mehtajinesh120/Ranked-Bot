/*    */ package com.kasp.rankedbot.listener;
/*    */ 
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Party;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PartyCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
/*    */ import net.dv8tion.jda.api.hooks.ListenerAdapter;
/*    */ 
/*    */ public class PartyInviteButton extends ListenerAdapter {
/*    */   public void onButtonClick(ButtonClickEvent event) {
/* 17 */     if (event.getButton().getId().startsWith("rankedbot-pinvitation-")) {
/* 18 */       String[] players = event.getButton().getId().replace("rankedbot-pinvitation-", "").split("=");
/* 19 */       Player leader = PlayerCache.getPlayer(players[0]);
/* 20 */       Player invited = PlayerCache.getPlayer(players[1]);
/*    */       
/* 22 */       if (!event.getMember().getId().equals(invited.getID())) {
/* 23 */         event.reply(Msg.getMsg("not-invited")).setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 27 */       if (PartyCache.getParty(invited) != null) {
/* 28 */         event.reply(Msg.getMsg("already-in-party")).setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 32 */       if (PartyCache.getParty(leader) == null) {
/* 33 */         event.reply(Msg.getMsg("player-not-in-party")).setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 37 */       Party party = PartyCache.getParty(leader);
/*    */       
/* 39 */       if (!party.getInvitedPlayers().contains(invited)) {
/* 40 */         event.reply(Msg.getMsg("not-invited")).setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 44 */       if (party.getMembers().size() >= Integer.parseInt(Config.getValue("max-party-members"))) {
/* 45 */         event.reply(Msg.getMsg("this-party-full")).setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 49 */       int partyElo = 0;
/* 50 */       for (Player p : party.getMembers()) {
/* 51 */         partyElo += p.getElo();
/*    */       }
/*    */       
/* 54 */       if (partyElo + invited.getElo() > Integer.parseInt(Config.getValue("max-party-elo"))) {
/* 55 */         event.reply("You have too much elo to join this party\nParty elo: `" + partyElo + "`\nYour elo: `" + invited.getElo() + "`\nParty elo limit: `" + Config.getValue("max-party-elo") + "`").setEphemeral(true).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 59 */       invited.joinParty(party);
/*    */       
/* 61 */       Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("joined-party"), 1);
/* 62 */       event.replyEmbeds(reply.build(), new MessageEmbed[0]).setEphemeral(true).queue();
/*    */       
/* 64 */       Embed embed = new Embed(EmbedType.DEFAULT, "", "<@" + invited.getID() + "> has joined your party", 1);
/* 65 */       event.getChannel().sendMessage("<@" + leader.getID() + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\listener\PartyInviteButton.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */