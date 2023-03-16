/*    */ package com.kasp.rankedbot.commands.moderation;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.Objects;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class Strike extends Command {
/*    */   public Strike(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 21 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 26 */     if (args.length < 3) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 28 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     String ID = args[1].replaceAll("[^0-9]", "");
/* 33 */     String reason = msg.getContentRaw().replaceAll(args[0], "").replaceAll(args[1], "").trim();
/*    */     
/* 35 */     Player player = PlayerCache.getPlayer(ID);
/* 36 */     player.setStrikes(player.getStrikes() + 1);
/*    */     
/* 38 */     int strikes = player.getStrikes();
/* 39 */     if (player.getStrikes() > 5) {
/* 40 */       strikes = 5;
/*    */     }
/*    */     
/* 43 */     int bantime = Integer.parseInt(Config.getValue("strike-" + strikes));
/*    */     
/* 45 */     Embed embed = new Embed(EmbedType.DEFAULT, player.getIgn() + " has been striked", "", 1);
/* 46 */     embed.addField("Strikes:", "" + player.getStrikes() - 1 + " -> " + player.getStrikes() - 1, false);
/* 47 */     embed.addField("Reason:", reason, false);
/*    */     
/* 49 */     Embed success = new Embed(EmbedType.SUCCESS, "", "Successfully striked <@" + ID + ">", 1);
/*    */     
/* 51 */     if (bantime != 0) {
/* 52 */       if (player.ban(LocalDateTime.now().plusHours(bantime), "[STRIKE] " + reason)) {
/*    */         
/* 54 */         embed.addField("You are now banned for: ", "`" + bantime + " hours`", false);
/* 55 */         embed.setDescription("<@" + ID + "> Please do `=fix` after " + bantime + "h if you're still not unbanned");
/*    */       } else {
/*    */         
/* 58 */         embed.addField("WARNING", "I couldn't ban the player since they're already banned", false);
/* 59 */         success.addField("WARNING", "I couldn't ban the player since they're already banned", false);
/*    */       } 
/*    */     }
/*    */     
/* 63 */     msg.replyEmbeds(success.build(), new MessageEmbed[0]).queue();
/* 64 */     if (!Objects.equals(Config.getValue("ban-channel"), null))
/* 65 */       guild.getTextChannelById(Config.getValue("ban-channel")).sendMessage("<@" + ID + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\moderation\Strike.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */