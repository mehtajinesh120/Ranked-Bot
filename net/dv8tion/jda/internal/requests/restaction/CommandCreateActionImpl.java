/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.interactions.commands.Command;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.OptionData;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import okhttp3.RequestBody;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandCreateActionImpl
/*     */   extends RestActionImpl<Command>
/*     */   implements CommandCreateAction
/*     */ {
/*     */   private final Guild guild;
/*     */   private CommandData data;
/*     */   
/*     */   public CommandCreateActionImpl(JDAImpl api, CommandData command) {
/*  45 */     super((JDA)api, Route.Interactions.CREATE_COMMAND.compile(new String[] { api.getSelfUser().getApplicationId() }));
/*  46 */     this.guild = null;
/*  47 */     this.data = command;
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandCreateActionImpl(Guild guild, CommandData command) {
/*  52 */     super(guild.getJDA(), Route.Interactions.CREATE_GUILD_COMMAND.compile(new String[] { guild.getJDA().getSelfUser().getApplicationId(), guild.getId() }));
/*  53 */     this.guild = guild;
/*  54 */     this.data = command;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction addCheck(@Nonnull BooleanSupplier checks) {
/*  61 */     return (CommandCreateAction)super.addCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction setCheck(BooleanSupplier checks) {
/*  68 */     return (CommandCreateAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction deadline(long timestamp) {
/*  75 */     return (CommandCreateAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction setDefaultEnabled(boolean enabled) {
/*  82 */     this.data.setDefaultEnabled(enabled);
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction timeout(long timeout, @Nonnull TimeUnit unit) {
/*  90 */     return (CommandCreateAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction setName(@Nonnull String name) {
/*  97 */     Checks.notEmpty(name, "Name");
/*  98 */     Checks.notLonger(name, 32, "Name");
/*  99 */     Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
/* 100 */     this.data.setName(name);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction setDescription(@Nonnull String description) {
/* 108 */     Checks.notEmpty(description, "Description");
/* 109 */     Checks.notLonger(description, 100, "Description");
/* 110 */     this.data.setDescription(description);
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction addOptions(@Nonnull OptionData... options) {
/* 118 */     this.data.addOptions(options);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction addSubcommands(@Nonnull SubcommandData subcommand) {
/* 126 */     this.data.addSubcommands(new SubcommandData[] { subcommand });
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandCreateAction addSubcommandGroups(@Nonnull SubcommandGroupData group) {
/* 134 */     this.data.addSubcommandGroups(new SubcommandGroupData[] { group });
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestBody finalizeData() {
/* 141 */     return getRequestBody(this.data.toData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Command> request) {
/* 147 */     DataObject json = response.getObject();
/* 148 */     request.onSuccess(new Command(this.api, this.guild, json));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\CommandCreateActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */