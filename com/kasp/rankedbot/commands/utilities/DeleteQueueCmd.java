/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*    */ 
/*    */ public class DeleteQueueCmd extends Command {
/*    */   public DeleteQueueCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 14 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 19 */     if (args.length != 2) {
/* 20 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 21 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 25 */     VoiceChannel vc = null;
/* 26 */     String ID = args[1].replaceAll("[^0-9]", ""); 
/* 27 */     try { vc = guild.getVoiceChannelById(ID); } catch (Exception exception) {}
/*    */     
/* 29 */     if (vc == null) {
/* 30 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-vc"), 1);
/* 31 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     if (!QueueCache.containsQueue(ID)) {
/* 36 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("q-doesnt-exist"), 1);
/* 37 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 41 */     Queue.delete(ID);
/*    */     
/* 43 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("q-deleted"), 1);
/* 44 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\DeleteQueueCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */