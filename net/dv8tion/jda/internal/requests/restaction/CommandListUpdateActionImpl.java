/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.interactions.commands.Command;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
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
/*     */ public class CommandListUpdateActionImpl
/*     */   extends RestActionImpl<List<Command>>
/*     */   implements CommandListUpdateAction
/*     */ {
/*  42 */   private final List<CommandData> commands = new ArrayList<>();
/*     */   
/*     */   private final GuildImpl guild;
/*     */   
/*     */   public CommandListUpdateActionImpl(JDA api, GuildImpl guild, Route.CompiledRoute route) {
/*  47 */     super(api, route);
/*  48 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandListUpdateAction timeout(long timeout, @Nonnull TimeUnit unit) {
/*  55 */     return (CommandListUpdateAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandListUpdateAction addCheck(@Nonnull BooleanSupplier checks) {
/*  62 */     return (CommandListUpdateAction)super.addCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandListUpdateAction setCheck(BooleanSupplier checks) {
/*  69 */     return (CommandListUpdateAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandListUpdateAction deadline(long timestamp) {
/*  76 */     return (CommandListUpdateAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandListUpdateAction addCommands(@Nonnull Collection<? extends CommandData> commands) {
/*  83 */     Checks.noneNull(commands, "Command");
/*  84 */     Checks.check((this.commands.size() + commands.size() <= 100), "Cannot have more than 100 commands! Try using subcommands instead.");
/*  85 */     this.commands.addAll(commands);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/*  92 */     DataArray json = DataArray.empty();
/*  93 */     json.addAll(this.commands);
/*  94 */     return getRequestBody(json);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<List<Command>> request) {
/* 102 */     List<Command> commands = (List<Command>)response.getArray().stream(DataArray::getObject).map(obj -> new Command(this.api, (Guild)this.guild, obj)).collect(Collectors.toList());
/* 103 */     request.onSuccess(commands);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\CommandListUpdateActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */