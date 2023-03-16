/*    */ package com.kasp.rankedbot.commands.clan;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Clan;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Leaderboard;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.EmbedBuilder;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class ClanLBCmd extends Command {
/*    */   public ClanLBCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 21 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 26 */     if (args.length != 1) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 28 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     List<Clan> lb = new ArrayList<>(Leaderboard.getClansLeaderboard());
/*    */     
/* 34 */     Message embedmsg = (Message)msg.replyEmbeds((new EmbedBuilder()).setTitle("loading...").build(), new MessageEmbed[0]).complete();
/*    */     
/* 36 */     for (int j = 0; j < Math.ceil(lb.size() / 10.0D); j++) {
/* 37 */       Embed reply = new Embed(EmbedType.DEFAULT, "Clans Leaderboard", "this lb is for `reputation`\nwhich can be obtained\nby playing clan wars", (int)Math.ceil(lb.size() / 10.0D));
/*    */       
/* 39 */       String lbmsg = "";
/* 40 */       for (int i = j * 10; i < j * 10 + 10; i++) {
/* 41 */         if (i < lb.size()) {
/* 42 */           lbmsg = lbmsg + "**#" + lbmsg + "** `" + i + 1 + "` — " + ((Clan)lb.get(i)).getName() + "\n";
/*    */         }
/*    */       } 
/* 45 */       reply.setDescription(lbmsg);
/*    */       
/* 47 */       if (j == 0) {
/* 48 */         embedmsg.editMessageEmbeds(new MessageEmbed[] { reply.build() }).setActionRow(Embed.createButtons(reply.getCurrentPage())).queue();
/*    */       }
/*    */       
/* 51 */       Embed.addPage(embedmsg.getId(), reply);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanLBCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */