/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.GameMap;
/*    */ import com.kasp.rankedbot.instance.cache.MapCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class MapsCmd extends Command {
/*    */   public MapsCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     Embed embed;
/* 22 */     if (args.length != 1) {
/* 23 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String maps = "";
/* 29 */     for (GameMap m : MapCache.getMaps().values()) {
/* 30 */       maps = maps + "**" + maps + "** — `Height: " + m.getName() + "` (" + m.getHeight() + " vs " + m.getTeam1() + ")\n";
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 35 */     if (maps.equals("")) {
/* 36 */       embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("no-maps"), 1);
/*    */     } else {
/*    */       
/* 39 */       embed = new Embed(EmbedType.DEFAULT, "All maps", maps, 1);
/*    */     } 
/*    */     
/* 42 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\MapsCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */