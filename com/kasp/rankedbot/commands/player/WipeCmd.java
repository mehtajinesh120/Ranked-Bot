/*    */ package com.kasp.rankedbot.commands.player;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.database.SQLPlayerManager;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class WipeCmd extends Command {
/*    */   public WipeCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 18 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 23 */     if (args.length < 2) {
/* 24 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 25 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (args[1].equals("everyone")) {
/* 30 */       double time = SQLPlayerManager.getPlayerSize() / 20.0D;
/*    */       
/* 32 */       Embed reply = new Embed(EmbedType.DEFAULT, "Resetting everyone's stats...", "`Check console for more details`", 1);
/* 33 */       reply.addField("WARNING", "please do not use any other cmd during the reset\nit might result in errors / slower resetting", false);
/* 34 */       reply.addField("Estimated time", "" + time + " second(s) `(" + time + " players)`", false);
/* 35 */       reply.addField("Reset by:", sender.getAsMention(), true);
/* 36 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/* 37 */       long start = System.currentTimeMillis();
/*    */       
/* 39 */       for (Player p : PlayerCache.getPlayers().values()) {
/* 40 */         p.wipe();
/* 41 */         if (guild.getMemberById(p.getID()) != null) {
/* 42 */           p.fix();
/*    */         }
/* 44 */         System.out.println("[=wipe] successfully reset " + p.getIgn() + " (" + p.getID() + ")");
/*    */       } 
/*    */       
/* 47 */       long end = System.currentTimeMillis();
/* 48 */       float elapsedTime = (float)(end - start) / 1000.0F;
/*    */       
/* 50 */       Embed success = new Embed(EmbedType.SUCCESS, "All stats were successfully reset", "", 1);
/* 51 */       success.addField("Resetting took", "`" + elapsedTime + "` seconds `(" + SQLPlayerManager.getPlayerSize() + " players)`", true);
/* 52 */       success.addField("Reset by:", sender.getAsMention(), true);
/* 53 */       msg.replyEmbeds(success.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } else {
/*    */       
/* 56 */       Player player = PlayerCache.getPlayer(args[1].replaceAll("[^0-9]", ""));
/* 57 */       player.wipe();
/* 58 */       player.fix();
/*    */       
/* 60 */       Embed reply = new Embed(EmbedType.SUCCESS, "Stats wiped", Msg.getMsg("successfully-wiped"), 1);
/* 61 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\WipeCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */