/*    */ package com.kasp.rankedbot.commands.player;
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
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class TransferGoldCmd extends Command {
/*    */   public TransferGoldCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 22 */     if (args.length != 3) {
/* 23 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 30 */     if (ID.equals(sender.getId())) {
/* 31 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-player"), 1);
/* 32 */       msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     if (PlayerCache.getPlayer(ID) == null) {
/* 37 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-player"), 1);
/* 38 */       msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 44 */     int gold = Integer.parseInt(args[2]);
/*    */     
/* 46 */     if (gold < 1) {
/* 47 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("too-little-gold"), 1);
/* 48 */       msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     if (player.getGold() < gold) {
/* 53 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-enough-gold"), 1);
/* 54 */       msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     Player getter = PlayerCache.getPlayer(ID);
/*    */     
/* 60 */     getter.setGold(getter.getGold() + gold);
/* 61 */     player.setGold(player.getGold() - gold);
/*    */     
/* 63 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "<@" + player.getID() + "> has sent you `" + gold + "` GOLD\nYou now have `" + getter.getGold() + "` GOLD in total", 1);
/* 64 */     msg.reply("<@" + ID + ">").setEmbeds(new MessageEmbed[] { reply.build() }).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\TransferGoldCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */