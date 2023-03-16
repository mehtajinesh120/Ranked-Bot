/*    */ package com.kasp.rankedbot.commands.clanwar;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class CWUnregisterCmd extends Command {
/*    */   public CWUnregisterCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 12 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {}
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clanwar\CWUnregisterCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */