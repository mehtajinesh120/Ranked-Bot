/*    */ package com.kasp.rankedbot.commands.game;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.Permission;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class CallCmd extends Command {
/*    */   public CallCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 22 */     if (args.length != 2) {
/* 23 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     if (GameCache.getGame(channel.getId()) == null) {
/* 29 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-game-channel"), 1);
/* 30 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 34 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 36 */     if (sender.getVoiceState() == null) {
/* 37 */       Embed reply = new Embed(EmbedType.ERROR, "Error", "You aren't in a vc", 1);
/* 38 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     Embed embed = new Embed(EmbedType.SUCCESS, "", "Given <@" + ID + "> access to your vc", 1);
/*    */     
/* 44 */     sender.getVoiceState().getChannel().getManager().setUserLimit(sender.getVoiceState().getChannel().getUserLimit() + 1).queue();
/* 45 */     sender.getVoiceState().getChannel().createPermissionOverride((IPermissionHolder)guild.getMemberById(ID)).setAllow(new Permission[] { Permission.VIEW_CHANNEL }).setAllow(new Permission[] { Permission.VOICE_CONNECT }).queue();
/*    */     
/* 47 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\CallCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */