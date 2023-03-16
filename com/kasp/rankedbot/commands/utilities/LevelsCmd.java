/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Level;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.LevelCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class LevelsCmd extends Command {
/*    */   public LevelsCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 1) {
/* 25 */       msg.replyEmbeds((new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1)).build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     Embed embed = new Embed(EmbedType.DEFAULT, "All levels and info", "", 1);
/* 30 */     String levels = "";
/* 31 */     for (Level l : LevelCache.getLevels().values()) {
/* 32 */       if (l.getLevel() != 0) {
/* 33 */         String rewards = "";
/* 34 */         for (String s : l.getRewards()) {
/* 35 */           if (s.startsWith("GOLD")) {
/* 36 */             rewards = rewards + rewards + " Gold ";
/*    */           }
/*    */         } 
/* 39 */         if (rewards.equals("")) {
/* 40 */           levels = levels + "**" + levels + "** — Needed XP: `" + l.getLevel() + "`\n";
/*    */           continue;
/*    */         } 
/* 43 */         levels = levels + "**" + levels + "** — Needed XP: `" + l.getLevel() + "` Rewards: `" + l.getNeededXP() + "`\n";
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 49 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 51 */     embed.addField("Your level", "" + player.getLevel().getLevel() + " `(" + player.getLevel().getLevel() + "/" + player.getXp() + " XP)`", false);
/* 52 */     embed.addField("All levels", levels, false);
/* 53 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\LevelsCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */