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
/*    */ public class ThemeCmd extends Command {
/*    */   public ThemeCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 2) {
/* 25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     String name = args[1];
/*    */     
/* 32 */     if (name.equals("list")) {
/* 33 */       String themes = "";
/* 34 */       for (Theme t : ThemeCache.getThemes().values()) {
/* 35 */         themes = themes + "`" + themes + "` ";
/*    */       }
/*    */       
/* 38 */       Embed embed = new Embed(EmbedType.DEFAULT, "All themes `(" + ThemeCache.getThemes().size() + ")`", themes, 1);
/* 39 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } else {
/*    */       
/* 42 */       if (!ThemeCache.containsTheme(name)) {
/* 43 */         Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("theme-doesnt-exist"), 1);
/* 44 */         msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 48 */       Theme theme = ThemeCache.getTheme(name);
/*    */       
/* 50 */       Player player = PlayerCache.getPlayer(sender.getId());
/*    */       
/* 52 */       if (!player.getOwnedThemes().contains(theme)) {
/* 53 */         Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("theme-access-denied"), 1);
/* 54 */         msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 58 */       player.setTheme(theme);
/*    */       
/* 60 */       Embed embed = new Embed(EmbedType.SUCCESS, "", "You have successfully selected theme `" + theme.getName() + "`", 1);
/* 61 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\ThemeCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */