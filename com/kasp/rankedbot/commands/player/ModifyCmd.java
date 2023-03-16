/*     */ package com.kasp.rankedbot.commands.player;
/*     */ 
/*     */ import com.kasp.rankedbot.CommandSubsystem;
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.Statistic;
/*     */ import com.kasp.rankedbot.commands.Command;
/*     */ import com.kasp.rankedbot.instance.Embed;
/*     */ import com.kasp.rankedbot.instance.Player;
/*     */ import com.kasp.rankedbot.instance.cache.LevelCache;
/*     */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*     */ import com.kasp.rankedbot.messages.Msg;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ 
/*     */ public class ModifyCmd extends Command {
/*     */   public ModifyCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/*  19 */     super(command, usage, aliases, description, subsystem);
/*     */   }
/*     */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*     */     Statistic stat;
/*     */     Embed error;
/*  24 */     if (args.length != 4) {
/*  25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/*  26 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  30 */     String ID = args[1].replaceAll("[^0-9]", "");
/*     */ 
/*     */     
/*     */     try {
/*  34 */       stat = Statistic.valueOf(args[2].toUpperCase());
/*  35 */     } catch (Exception e) {
/*  36 */       String stats = "";
/*  37 */       for (Statistic s : Statistic.values()) {
/*  38 */         stats = stats + "`" + stats + "` ";
/*     */       }
/*  40 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", "**This statistic does not exist**\nAvailable stats: " + stats, 1);
/*  41 */       msg.replyEmbeds(embed1.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     int amount = Integer.parseInt(args[3]);
/*     */     
/*  47 */     Player player = PlayerCache.getPlayer(ID);
/*     */     
/*  49 */     Embed embed = new Embed(EmbedType.SUCCESS, "Modified `" + player.getIgn() + "`'s stats", "", 1);
/*     */     
/*  51 */     switch (stat) {
/*     */       case ELO:
/*  53 */         player.setElo(player.getElo() + amount);
/*  54 */         embed.setDescription("Player's elo: " + player.getElo() - amount + " > " + player.getElo());
/*     */         break;
/*     */       case WINS:
/*  57 */         player.setWins(player.getWins() + amount);
/*  58 */         embed.setDescription("Player's wins: " + player.getWins() - amount + " > " + player.getWins());
/*     */         break;
/*     */       case LOSSES:
/*  61 */         player.setLosses(player.getLosses() + amount);
/*  62 */         embed.setDescription("Player's losses: " + player.getLosses() - amount + " > " + player.getLosses());
/*     */         break;
/*     */       case MVP:
/*  65 */         player.setMvp(player.getMvp() + amount);
/*  66 */         embed.setDescription("Player's mvp: " + player.getMvp() - amount + " > " + player.getMvp());
/*     */         break;
/*     */       case KILLS:
/*  69 */         player.setKills(player.getKills() + amount);
/*  70 */         embed.setDescription("Player's kills: " + player.getKills() - amount + " > " + player.getKills());
/*     */         break;
/*     */       case DEATHS:
/*  73 */         player.setDeaths(player.getDeaths() + amount);
/*  74 */         embed.setDescription("Player's deaths: " + player.getDeaths() - amount + " > " + player.getDeaths());
/*     */         break;
/*     */       case STRIKES:
/*  77 */         player.setStrikes(player.getStrikes() + amount);
/*  78 */         embed.setDescription("Player's strikes: " + player.getStrikes() - amount + " > " + player.getStrikes());
/*     */         break;
/*     */       case SCORED:
/*  81 */         player.setScored(player.getScored() + amount);
/*  82 */         embed.setDescription("Player's scored: " + player.getScored() - amount + " > " + player.getScored());
/*     */         break;
/*     */       case GOLD:
/*  85 */         player.setGold(player.getGold() + amount);
/*  86 */         embed.setDescription("Player's gold: " + player.getGold() - amount + " > " + player.getGold());
/*     */         break;
/*     */       case LEVEL:
/*  89 */         player.setLevel(LevelCache.getLevel(player.getLevel().getLevel() + amount));
/*  90 */         embed.setDescription("Player's level: " + player.getLevel().getLevel() - amount + " > " + player.getLevel().getLevel());
/*     */         break;
/*     */       case XP:
/*  93 */         player.setXp(player.getXp() + amount);
/*  94 */         embed.setDescription("Player's xp: " + player.getXp() - amount + " > " + player.getXp());
/*     */         break;
/*     */       default:
/*  97 */         error = new Embed(EmbedType.ERROR, "Error", "You cannot modify this statistic", 1);
/*  98 */         msg.replyEmbeds(error.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */         return;
/*     */     } 
/*     */     
/* 102 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\ModifyCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */