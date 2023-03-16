/*    */ package com.kasp.rankedbot.commands.utilities;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.PickingMode;
/*    */ import com.kasp.rankedbot.database.SQLUtilsManager;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*    */ 
/*    */ public class AddQueueCmd extends Command {
/*    */   public AddQueueCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 16 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 21 */     if (args.length != 5) {
/* 22 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 23 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 27 */     VoiceChannel vc = null;
/* 28 */     String ID = args[1].replaceAll("[^0-9]", ""); 
/* 29 */     try { vc = guild.getVoiceChannelById(ID); } catch (Exception exception) {}
/*    */     
/* 31 */     if (vc == null) {
/* 32 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-vc"), 1);
/* 33 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 37 */     int playersEachTeam = Integer.parseInt(args[2]);
/*    */     
/* 39 */     if (playersEachTeam <= 0) {
/* 40 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("q-more-players"), 1);
/* 41 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     if (QueueCache.containsQueue(ID)) {
/* 46 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("q-already-exists"), 1);
/* 47 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     PickingMode pickingMode = PickingMode.valueOf(args[3].toUpperCase());
/*    */     
/* 53 */     boolean casual = Boolean.parseBoolean(args[4]);
/*    */     
/* 55 */     SQLUtilsManager.createQueue(ID, playersEachTeam, pickingMode, casual);
/* 56 */     new Queue(ID);
/*    */     
/* 58 */     Embed embed = new Embed(EmbedType.SUCCESS, "✅ successfully added `" + vc.getName() + "` queue", "", 1);
/* 59 */     embed.addField("VC", vc.getAsMention(), true);
/* 60 */     embed.addField("Players in each team:", args[2], true);
/* 61 */     embed.addField("Sorting mode:", args[3], true);
/* 62 */     embed.addField("Casual queue:", "" + casual, true);
/* 63 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\command\\utilities\AddQueueCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */