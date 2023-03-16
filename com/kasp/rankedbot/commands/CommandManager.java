/*     */ package com.kasp.rankedbot.commands;
/*     */ import com.kasp.rankedbot.CommandSubsystem;
/*     */ import com.kasp.rankedbot.EmbedType;
/*     */ import com.kasp.rankedbot.commands.clan.ClanListCmd;
/*     */ import com.kasp.rankedbot.commands.clanwar.CWCancelCmd;
/*     */ import com.kasp.rankedbot.commands.clanwar.CWCreateCmd;
/*     */ import com.kasp.rankedbot.commands.game.GameInfoCmd;
/*     */ import com.kasp.rankedbot.commands.game.ScoreCmd;
/*     */ import com.kasp.rankedbot.commands.game.SubmitCmd;
/*     */ import com.kasp.rankedbot.commands.game.UndoGameCmd;
/*     */ import com.kasp.rankedbot.commands.game.WinCmd;
/*     */ import com.kasp.rankedbot.commands.moderation.BanInfo;
/*     */ import com.kasp.rankedbot.commands.moderation.Unban;
/*     */ import com.kasp.rankedbot.commands.party.PartyInviteCmd;
/*     */ import com.kasp.rankedbot.commands.party.PartyLeaveCmd;
/*     */ import com.kasp.rankedbot.commands.player.RegisterCmd;
/*     */ import com.kasp.rankedbot.commands.player.RenameCmd;
/*     */ import com.kasp.rankedbot.commands.utilities.GiveThemeCmd;
/*     */ import com.kasp.rankedbot.commands.utilities.LevelsCmd;
/*     */ import com.kasp.rankedbot.commands.utilities.QueuesCmd;
/*     */ import com.kasp.rankedbot.commands.utilities.RemoveThemeCmd;
/*     */ import com.kasp.rankedbot.config.Config;
/*     */ import com.kasp.rankedbot.instance.Embed;
/*     */ import com.kasp.rankedbot.messages.Msg;
/*     */ import com.kasp.rankedbot.perms.Perms;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
/*     */ 
/*     */ public class CommandManager extends ListenerAdapter {
/*  36 */   static ArrayList<Command> commands = new ArrayList<>();
/*     */   
/*     */   public CommandManager() {
/*  39 */     commands.add(new HelpCmd("help", "help [sub-system]", new String[0], "See the available server commands", CommandSubsystem.SERVER));
/*  40 */     commands.add(new ReloadConfigCmd("reloadconfig", "reloadconfig", new String[] { "reload", "rc" }, "Reload the configs (update values)", CommandSubsystem.SERVER));
/*  41 */     commands.add(new InfoCmd("info", "info", new String[0], "View some info about the server and bot", CommandSubsystem.SERVER));
/*     */     
/*  43 */     commands.add(new RegisterCmd("register", "register <in-game name>", new String[0], "Register yourself to start playing", CommandSubsystem.PLAYER));
/*  44 */     commands.add(new RenameCmd("rename", "rename <in-game name>", new String[0], "Change your in-game name", CommandSubsystem.PLAYER));
/*  45 */     commands.add(new FixCmd("fix", "fix [ID/mention]", new String[] { "correct" }, "Fix discord roles and nickname depending on the stats", CommandSubsystem.PLAYER));
/*  46 */     commands.add(new ForceRegisterCmd("forceregister", "forceregister <ID/mention> <ign>", new String[] { "freg" }, "Forcefully register a player", CommandSubsystem.PLAYER));
/*  47 */     commands.add(new ForceRenameCmd("forcerename", "forcerename <ID/mention> <new ign>", new String[] { "fren" }, "Forcefully rename a player", CommandSubsystem.PLAYER));
/*  48 */     commands.add(new WipeCmd("wipe", "wipe <ID/mention/\"everyone\">", new String[] { "reset" }, "Reset player's / everyone's stats", CommandSubsystem.PLAYER));
/*  49 */     commands.add(new StatsCmd("stats", "stats [ID/mention/\"full\"]", new String[] { "s", "i" }, "View player's stats", CommandSubsystem.PLAYER));
/*  50 */     commands.add(new LeaderboardCmd("leaderboard", "leaderboard <statistic>", new String[] { "lb" }, "View the leaderbaord for a statistic", CommandSubsystem.PLAYER));
/*  51 */     commands.add(new ModifyCmd("modify", "modify <ID/mention> <statistic> <value>", new String[] { "edit" }, "Modify player's stats", CommandSubsystem.PLAYER));
/*  52 */     commands.add(new ScreenshareCmd("screenshare", "screenshare <ID/mention> <reason>", new String[] { "ss" }, "Screenshare a player", CommandSubsystem.PLAYER));
/*  53 */     commands.add(new TransferGoldCmd("transfergold", "transfergold <ID/mention> <amount>", new String[] { "tg" }, "Transfer specified player some of your gold", CommandSubsystem.PLAYER));
/*     */     
/*  55 */     commands.add(new PartyCreateCmd("partycreate", "partycreate", new String[] { "pcreate" }, "Create a party", CommandSubsystem.PARTY));
/*  56 */     commands.add(new PartyInviteCmd("partyinvite", "partyinvite <ID/mention>", new String[] { "pinvite" }, "Invite a player to your party", CommandSubsystem.PARTY));
/*  57 */     commands.add(new PartyJoinCmd("partyjoin", "partyjoin <ID/mention>", new String[] { "pjoin" }, "Join a player's party", CommandSubsystem.PARTY));
/*  58 */     commands.add(new PartyLeaveCmd("partyleave", "partyleave", new String[] { "pleave" }, "Leave your current party or disband it if you're the leader", CommandSubsystem.PARTY));
/*  59 */     commands.add(new PartyPromoteCmd("partypromote", "partypromote <ID/metion>", new String[] { "ppromote" }, "Promote a player in your party to party leader", CommandSubsystem.PARTY));
/*  60 */     commands.add(new PartyWarpCmd("partywarp", "partywarp", new String[] { "pwarp" }, "Warp your party to your current vc (warps a member only if that member is in any vc)", CommandSubsystem.PARTY));
/*  61 */     commands.add(new PartyListCmd("partylist", "partylist [ID/mention]", new String[] { "plist" }, "View info about your or someone else's party", CommandSubsystem.PARTY));
/*  62 */     commands.add(new PartyKickCmd("partykick", "partykick <ID/mention>", new String[] { "pkick" }, "Kick a player from your party", CommandSubsystem.PARTY));
/*     */     
/*  64 */     commands.add(new QueueCmd("queue", "queue", new String[] { "q" }, "View your game's queue", CommandSubsystem.GAME));
/*  65 */     commands.add(new QueueStatsCmd("queuestats", "queuestats", new String[] { "qs" }, "View your game's queue stats", CommandSubsystem.GAME));
/*  66 */     commands.add(new GameInfoCmd("gameinfo", "gameinfo <number>", new String[] { "gi" }, "View info about a game", CommandSubsystem.GAME));
/*  67 */     commands.add(new PickCmd("pick", "pick <ID/mention>", new String[] { "p" }, "Pick a player in your game (if you're a captain)", CommandSubsystem.GAME));
/*  68 */     commands.add(new VoidCmd("void", "void", new String[] { "cleargame", "clear", "cg" }, "Cancel a game if you can't play it anymore", CommandSubsystem.GAME));
/*  69 */     commands.add(new CallCmd("call", "call <ID/mention>", new String[0], "Give a player access to join your vc", CommandSubsystem.GAME));
/*  70 */     commands.add(new SubmitCmd("submit", "submit", new String[0], "Submit a game for scoring", CommandSubsystem.GAME));
/*  71 */     commands.add(new ScoreCmd("score", "score <number> <team> <mvp ID/mention/\"none\">", new String[0], "Score a game", CommandSubsystem.GAME));
/*  72 */     commands.add(new UndoGameCmd("undogame", "undogame <number>", new String[0], "Undo a scored game", CommandSubsystem.GAME));
/*  73 */     commands.add(new WinCmd("win", "win <ID/mention>", new String[0], "Give specified player +1 win and elo (depends on the rank). This command should be used ONLY when '=score' doesn't work or for testing purposes", CommandSubsystem.GAME));
/*  74 */     commands.add(new LoseCmd("lose", "lose <ID/mention>", new String[0], "Give specified player +1 loss and -elo (depends on the rank). This command should be used ONLY when '=score' doesn't work or for testing purposes", CommandSubsystem.GAME));
/*  75 */     commands.add(new ForceVoidCmd("forcevoid", "forcevoid", new String[] { "fv" }, "Forcefully void a game (staff cmd)", CommandSubsystem.GAME));
/*     */     
/*  77 */     commands.add(new AddQueueCmd("addqueue", "addqueue <vc ID> <playersEachTeam> <pickingMode (AUTOMATIC/CAPTAINS)> <casual (true/false)>", new String[] { "addq" }, "Add a ranked/casual queue", CommandSubsystem.UTILITIES));
/*  78 */     commands.add(new DeleteQueueCmd("deletequeue", "deletequeue <vc ID>", new String[] { "delq", "delqueue" }, "Delete a ranked/casual queue", CommandSubsystem.UTILITIES));
/*  79 */     commands.add(new QueuesCmd("queues", "queues", new String[0], "View info about all server queues and some info about them", CommandSubsystem.UTILITIES));
/*  80 */     commands.add(new AddRankCmd("addrank", "addrank <role ID/mention> <starting elo> <ending elo> <win elo> <lose elo> <mvp elo>", new String[] { "addr" }, "Add a rank", CommandSubsystem.UTILITIES));
/*  81 */     commands.add(new DeleteRankCmd("deleterank", "deleterank <role ID/mention>", new String[] { "delr", "delrank" }, "Delete a rank", CommandSubsystem.UTILITIES));
/*  82 */     commands.add(new RanksCmd("ranks", "ranks", new String[0], "View all ranks and info about them", CommandSubsystem.UTILITIES));
/*  83 */     commands.add(new AddMapCmd("addmap", "addmap <name> <height> <team1> <team2>", new String[] { "addm" }, "Add an in-game map", CommandSubsystem.UTILITIES));
/*  84 */     commands.add(new DeleteMapCmd("deletemap", "deletemap <name>", new String[] { "delm", "delmap" }, "Delete an in-game map", CommandSubsystem.UTILITIES));
/*  85 */     commands.add(new MapsCmd("maps", "maps", new String[0], "View all maps and info about them", CommandSubsystem.UTILITIES));
/*  86 */     commands.add(new GiveThemeCmd("givetheme", "givetheme <ID/mention> <theme>", new String[0], "Give specified player access to a theme", CommandSubsystem.UTILITIES));
/*  87 */     commands.add(new RemoveThemeCmd("removetheme", "removetheme <ID/mention> <theme>", new String[0], "Remove specified player's access to a theme", CommandSubsystem.UTILITIES));
/*  88 */     commands.add(new ThemeCmd("theme", "theme <theme/\"list\">", new String[0], "Select a theme or use \"list\" to view all themes", CommandSubsystem.UTILITIES));
/*  89 */     commands.add(new LevelsCmd("levels", "levels", new String[0], "View all levels and info about them", CommandSubsystem.UTILITIES));
/*     */     
/*  91 */     commands.add(new Ban("ban", "ban <ID/mention> <time> <reason>", new String[0], "Ban a player from queueing", CommandSubsystem.MODERATION));
/*  92 */     commands.add(new Unban("unban", "unban <ID/mention>", new String[0], "Unban a banned player", CommandSubsystem.MODERATION));
/*  93 */     commands.add(new BanInfo("baninfo", "baninfo <ID/mention>", new String[0], "View info about a specific ban", CommandSubsystem.MODERATION));
/*  94 */     commands.add(new Strike("strike", "strike <ID/mention> <reason>", new String[0], "Strike a player - take away elo + ban from queueing (depends on how many strikes the player already has)", CommandSubsystem.MODERATION));
/*     */     
/*  96 */     commands.add(new ClanCreateCmd("clancreate", "clancreate <name>", new String[] { "ccreate" }, "Create a clan", CommandSubsystem.CLAN));
/*  97 */     commands.add(new ClanDisbandCmd("clandisband", "clandisband", new String[] { "cdisband" }, "Disband the clan you're in (if you're the leader)", CommandSubsystem.CLAN));
/*  98 */     commands.add(new ClanInviteCmd("claninvite", "claninvite <ID/mention>", new String[] { "cinvite" }, "Invite a player to your clan (invites expire every time the bot is restarted)", CommandSubsystem.CLAN));
/*  99 */     commands.add(new ClanJoinCmd("clanjoin", "clanjoin <name>", new String[] { "cjoin" }, "Join a clan (if you're invited)", CommandSubsystem.CLAN));
/* 100 */     commands.add(new ClanLeaveCmd("clanleave", "clanleave", new String[] { "cleave" }, "Leave the clan you're currently in (if you're not the leader)", CommandSubsystem.CLAN));
/* 101 */     commands.add(new ClanStatsCmd("clanstats", "clanstats [name]", new String[] { "cstats" }, "View stats/info about a certain clan", CommandSubsystem.CLAN));
/* 102 */     commands.add(new ClanInfoCmd("claninfo", "claninfo [name]", new String[] { "cinfo" }, "View all of the info that doesn't show up in `=cstats` ab your/someone's clan", CommandSubsystem.CLAN));
/* 103 */     commands.add(new ClanKickCmd("clankick", "clankick <ID/mention>", new String[] { "ckick" }, "Kick a player from your clan", CommandSubsystem.CLAN));
/* 104 */     commands.add(new ClanSettingsCmd("clansettings", "clansettings <setting> <value>", new String[] { "csettings" }, "Modify settings for your clan", CommandSubsystem.CLAN));
/* 105 */     commands.add(new ClanListCmd("clanlist", "clanlist", new String[] { "clist" }, "View a list of all the clans in the server", CommandSubsystem.CLAN));
/* 106 */     commands.add(new ClanLBCmd("clanlb", "clanlb", new String[] { "clb", "clanleaderboard", "cleaderboard" }, "View top reputation clans leaderboard", CommandSubsystem.CLAN));
/* 107 */     commands.add(new ClanForceDisbandCmd("clanforcedisband", "clanforcedisband <name>", new String[] { "cfdisband" }, "Forcefully disband a clan", CommandSubsystem.CLAN));
/*     */     
/* 109 */     commands.add(new CWCreateCmd("cwcreate", "cwcreate <players in team> <min clans> <max clans> <win xp> <win gold>", new String[] { "" }, "Host a clan war", CommandSubsystem.CLANWAR));
/* 110 */     commands.add(new CWCancelCmd("cwcancel", "cwcancel <number>", new String[] { "" }, "Cancel a clan war", CommandSubsystem.CLANWAR));
/* 111 */     commands.add(new CWRegisterCmd("cwregister", "cwregister <IDs/mentions>", new String[] { "" }, "Register your clan team for the clan war", CommandSubsystem.CLANWAR));
/* 112 */     commands.add(new CWUnregisterCmd("cwunregister", "cwunregister", new String[] { "" }, "Unregister your clan team for the clan war", CommandSubsystem.CLANWAR));
/* 113 */     commands.add(new CWStartCmd("cwstart", "cwstart", new String[] { "" }, "Start the current clan war", CommandSubsystem.CLANWAR));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
/* 118 */     String prefix = Config.getValue("prefix");
/*     */     
/* 120 */     String[] args = event.getMessage().getContentRaw().split(" ");
/* 121 */     Guild g = event.getGuild();
/* 122 */     Member m = event.getMember();
/* 123 */     TextChannel c = event.getChannel();
/* 124 */     Message msg = event.getMessage();
/*     */     
/* 126 */     if (!msg.getContentRaw().startsWith(prefix)) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     if (RankedBot.getGuild() == null) {
/* 131 */       Embed reply = new Embed(EmbedType.ERROR, "Bot Starting", "The bot is currently starting... Please wait a few seconds and use this command again", 1);
/* 132 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/* 136 */     if (!Boolean.parseBoolean(Config.getValue("unregistered-cmd-usage")) && 
/* 137 */       !args[0].replace(prefix, "").equalsIgnoreCase("register") && 
/* 138 */       !Player.isRegistered(m.getId())) {
/* 139 */       Embed reply = new Embed(EmbedType.ERROR, "Not Registered", Msg.getMsg("not-registered"), 1);
/* 140 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 146 */     Command command = null;
/*     */     
/* 148 */     for (Command cmd : commands) {
/* 149 */       String[] aliases = cmd.getAliases();
/* 150 */       boolean isAlias = Arrays.<String>asList(aliases).contains(args[0].toLowerCase().replace(prefix, ""));
/* 151 */       if (args[0].replace(prefix, "").equalsIgnoreCase(cmd.getCommand()) || isAlias) {
/* 152 */         command = cmd;
/*     */       }
/*     */     } 
/*     */     
/* 156 */     if (command == null) {
/* 157 */       Embed embed = new Embed(EmbedType.ERROR, "Command not found", "Use `=help` to view all the available commands", 1);
/* 158 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/* 162 */     if (!checkPerms(command, m, g)) {
/* 163 */       Embed reply = new Embed(EmbedType.ERROR, "No Permission", Msg.getMsg("no-perms"), 1);
/* 164 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*     */       
/*     */       return;
/*     */     } 
/* 168 */     if (Boolean.parseBoolean(Config.getValue("log-commands"))) {
/* 169 */       System.out.println("[RankedBot] " + m.getUser().getAsTag() + " used " + msg.getContentRaw());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 174 */     command.execute(args, g, m, c, msg);
/*     */   }
/*     */   
/*     */   public static ArrayList<Command> getAllCommands() {
/* 178 */     return commands;
/*     */   }
/*     */   
/*     */   private boolean checkPerms(Command cmd, Member m, Guild g) {
/* 182 */     boolean access = false;
/*     */     
/* 184 */     if (Perms.getPerm(cmd.getCommand()) == null) {
/* 185 */       return false;
/*     */     }
/* 187 */     if (Perms.getPerm(cmd.getCommand()).equals("everyone")) {
/* 188 */       access = true;
/*     */     
/*     */     }
/* 191 */     else if (!Perms.getPerm(cmd.getCommand()).equals("")) {
/* 192 */       List<Role> roles = new ArrayList<>();
/* 193 */       for (String s : Perms.getPerm(cmd.getCommand()).split(",")) {
/* 194 */         roles.add(g.getRoleById(Long.parseLong(s)));
/*     */       }
/*     */       
/* 197 */       for (Role r : m.getRoles()) {
/* 198 */         if (roles.contains(r)) {
/* 199 */           access = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 206 */     return access;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\CommandManager.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */