/*    */ package com.kasp.rankedbot.commands;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.perms.Perms;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class Command
/*    */ {
/*    */   private String command;
/*    */   private String usage;
/*    */   private String[] aliases;
/*    */   private String description;
/*    */   private CommandSubsystem subsystem;
/*    */   
/*    */   public Command(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     System.out.println(command + " command successfully loaded");
/* 20 */     this.command = command;
/* 21 */     this.usage = usage;
/* 22 */     this.aliases = aliases;
/* 23 */     this.description = description;
/* 24 */     this.subsystem = subsystem;
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 28 */     System.out.println("Something went wrong...");
/*    */   }
/*    */   
/*    */   public String getCommand() {
/* 32 */     return this.command;
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 36 */     return this.usage;
/*    */   }
/*    */   
/*    */   public String[] getAliases() {
/* 40 */     return this.aliases;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 44 */     return this.description;
/*    */   }
/*    */   
/*    */   public String[] getPermissions() {
/* 48 */     return Perms.getPerm(this.command).split(",");
/*    */   }
/*    */   
/*    */   public CommandSubsystem getSubsystem() {
/* 52 */     return this.subsystem;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\Command.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */