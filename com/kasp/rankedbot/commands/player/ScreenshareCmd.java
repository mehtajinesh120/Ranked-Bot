/*    */ package com.kasp.rankedbot.commands.player;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import java.util.List;
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ 
/*    */ public class ScreenshareCmd extends Command {
/*    */   public ScreenshareCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 15 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, final Guild guild, Member sender, TextChannel channel, Message msg) {
/* 20 */     if (args.length < 3) {
/* 21 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 22 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 26 */     if (msg.getAttachments().size() != Integer.parseInt(Config.getValue("ss-attachments"))) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Error", "You need to attach " + Config.getValue("ss-attachments") + " images for proof", 1);
/* 28 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     final String ID = args[1].replaceAll("[^0-9]", "");
/* 33 */     String reason = msg.getContentRaw().replaceAll(args[0], "").replaceAll(args[1], "").trim();
/*    */     
/* 35 */     if (sender == guild.retrieveMemberById(ID).complete()) {
/* 36 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("ss-self"), 1);
/* 37 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 41 */     final List<Role> freeze = new ArrayList<>();
/* 42 */     freeze.add(guild.getRoleById(Config.getValue("frozen-role")));
/* 43 */     guild.modifyMemberRoles((Member)guild.retrieveMemberById(ID).complete(), freeze, null).queue();
/*    */     
/* 45 */     TimerTask task = new TimerTask()
/*    */       {
/*    */         public void run() {
/* 48 */           guild.modifyMemberRoles((Member)guild.retrieveMemberById(ID).complete(), null, freeze).queue();
/*    */         }
/*    */       };
/*    */     
/* 52 */     Timer timer = new Timer();
/* 53 */     timer.schedule(task, Integer.parseInt(Config.getValue("time-till-unfrozen")) * 60000L);
/*    */     
/* 55 */     Embed embed = new Embed(EmbedType.DEFAULT, "DON'T LOG OFF", "", 1);
/* 56 */     embed.setDescription(((Member)guild.retrieveMemberById(ID).complete()).getAsMention() + " you have been SS requested\nDO NOT log off or modify/delete any files on your pc\nif no screensharer turns up, you're free to go after " + ((Member)guild.retrieveMemberById(ID).complete()).getAsMention() + "mins\n\n**SS reason**: " + 
/* 57 */         Config.getValue("time-till-unfrozen") + "\n\n**Requested by**: " + reason);
/*    */ 
/*    */     
/* 60 */     guild.getTextChannelById(Config.getValue("ssreq-channel")).sendMessage(((Member)guild.retrieveMemberById(ID).complete()).getAsMention()).setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */     
/* 62 */     if (!Objects.equals(Config.getValue("ss-roles"), null)) {
/* 63 */       String roles = "";
/* 64 */       for (String s : Config.getValue("ss-roles").split(",")) {
/* 65 */         roles = roles + roles;
/*    */       }
/* 67 */       guild.getTextChannelById(Config.getValue("ssreq-channel")).sendMessage(roles + " Please screenshare this player").queue();
/*    */     } 
/*    */     
/* 70 */     msg.reply("screenshare request sent in " + guild.getTextChannelById(Config.getValue("ssreq-channel")).getAsMention()).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\ScreenshareCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */