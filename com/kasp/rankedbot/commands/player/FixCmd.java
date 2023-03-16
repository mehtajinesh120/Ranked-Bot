/*    */ package com.kasp.rankedbot.commands.player;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class FixCmd extends Command {
/*    */   public FixCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     String ID;
/* 22 */     if (args.length > 2) {
/* 23 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 29 */     if (args.length == 2) {
/* 30 */       ID = args[1].replaceAll("[^0-9]", "");
/*    */     } else {
/*    */       
/* 33 */       ID = sender.getId();
/*    */     } 
/*    */     
/* 36 */     Player player = PlayerCache.getPlayer(ID);
/* 37 */     player.fix();
/*    */     
/* 39 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "Fixed the roles and nickname for player `" + player.getIgn() + "`", 1);
/* 40 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\FixCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */