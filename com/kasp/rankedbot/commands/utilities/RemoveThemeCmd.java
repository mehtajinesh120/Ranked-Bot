/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.Theme;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.instance.cache.ThemeCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class RemoveThemeCmd extends Command {
/*    */   public RemoveThemeCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 3) {
/* 25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     String name = args[2];
/*    */     
/* 32 */     if (!ThemeCache.containsTheme(name)) {
/* 33 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("theme-doesnt-exist"), 1);
/* 34 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     Theme theme = ThemeCache.getTheme(name);
/*    */     
/* 40 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 42 */     Player player = PlayerCache.getPlayer(ID);
/*    */     
/* 44 */     if (!player.getOwnedThemes().contains(theme)) {
/* 45 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("doesnt-have-theme"), 1);
/* 46 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     player.removeTheme(theme);
/*    */     
/* 52 */     Embed embed = new Embed(EmbedType.SUCCESS, "", "You have removed `" + theme.getName() + "` theme from " + guild.getMemberById(ID).getAsMention(), 1);
/* 53 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\RemoveThemeCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */