/*     */ package com.kasp.rankedbot.commands.server;
/*     */ 
/*     */ import com.kasp.rankedbot.CommandSubsystem;
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.commands.Command;
/*     */ import com.kasp.rankedbot.commands.CommandManager;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.instance.Embed;
/*     */ import com.kasp.rankedbot.messages.Msg;
/*     */ import java.util.ArrayList;
/*     */ import net.dv8tion.jda.api.EmbedBuilder;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ 
/*     */ public class HelpCmd
/*     */   extends Command {
/*     */   public HelpCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/*  21 */     super(command, usage, aliases, description, subsystem);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*  26 */     if (args.length > 2) {
/*  27 */       msg.replyEmbeds((new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1)).build(), new MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  31 */     ArrayList<Command> commands = new ArrayList<>(CommandManager.getAllCommands());
/*     */     
/*  33 */     if (args.length == 1) {
/*  34 */       Embed reply = new Embed(EmbedType.DEFAULT, "Help Subsystems", "Use `=help <subsystem>` to view the commands of a sub system", 1);
/*     */       
/*  36 */       for (CommandSubsystem s : CommandSubsystem.values()) {
/*  37 */         reply.addField("• " + s.toString().toLowerCase() + " sub-system", "use `=help " + s.toString().toLowerCase() + "`", false);
/*     */       }
/*     */       
/*  40 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  44 */     if (args.length == 2) {
/*     */       CommandSubsystem subsystem;
/*     */       
/*     */       try {
/*  48 */         subsystem = CommandSubsystem.valueOf(args[1].toUpperCase());
/*  49 */       } catch (Exception e) {
/*  50 */         String subsystems = "";
/*  51 */         for (CommandSubsystem s : CommandSubsystem.values()) {
/*  52 */           subsystems = subsystems + "`" + subsystems + "` ";
/*     */         }
/*  54 */         Embed embed = new Embed(EmbedType.ERROR, "Error", "This subsystem does not exist\nAvailable subsystems: " + subsystems, 1);
/*  55 */         msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*     */         
/*     */         return;
/*     */       } 
/*  59 */       ArrayList<Command> subsystemCmds = new ArrayList<>();
/*     */       
/*  61 */       for (Command cmd : commands) {
/*  62 */         if (cmd.getSubsystem() == subsystem) {
/*  63 */           subsystemCmds.add(cmd);
/*     */         }
/*     */       } 
/*  66 */       Message embedmsg = (Message)msg.replyEmbeds((new EmbedBuilder()).setTitle("loading...").build(), new MessageEmbed[0]).complete();
/*     */       
/*  68 */       for (int j = 0; j < Math.ceil(subsystemCmds.size()); j += 3) {
/*     */         
/*  70 */         Embed reply = new Embed(EmbedType.DEFAULT, "All commands in sub-system: " + subsystem, "", (int)Math.ceil(subsystemCmds.size() / 3.0D));
/*     */         
/*  72 */         for (int i = 0; i < 3; i++) {
/*  73 */           if (i + j < subsystemCmds.size()) {
/*     */             
/*  75 */             String aliases = "";
/*  76 */             String permissions = "";
/*     */             
/*  78 */             for (String s : ((Command)subsystemCmds.get(i + j)).getAliases()) {
/*  79 */               aliases = aliases + "`" + aliases + "` ";
/*     */             }
/*  81 */             for (String s : ((Command)subsystemCmds.get(i + j)).getPermissions()) {
/*  82 */               if (s.equals("everyone")) {
/*  83 */                 permissions = "@everyone";
/*     */               } else {
/*  85 */                 permissions = permissions + permissions;
/*     */               } 
/*     */             } 
/*  88 */             reply.addField("• " + ((Command)subsystemCmds.get(i + j)).getCommand(), ((Command)subsystemCmds.get(i + j)).getDescription() + "\n> Usage: `" + ((Command)subsystemCmds.get(i + j)).getDescription() + 
/*  89 */                 Config.getValue("prefix") + "`\n> Aliases: " + ((Command)subsystemCmds.get(i + j)).getUsage() + "\n> Permissions: " + aliases + "\n", false);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*  94 */         reply.addField("Note", "`<something>` - required\n`[something]` - optional", false);
/*     */         
/*  96 */         if (j == 0) {
/*  97 */           embedmsg.editMessageEmbeds(new MessageEmbed[] { reply.build() }).setActionRow(Embed.createButtons(reply.getCurrentPage())).queue();
/*     */         }
/*     */         
/* 100 */         Embed.addPage(embedmsg.getId(), reply);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\server\HelpCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */