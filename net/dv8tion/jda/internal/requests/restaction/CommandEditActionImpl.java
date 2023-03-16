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
/*     */ import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import okhttp3.RequestBody;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ 
/*     */ public class CommandEditActionImpl
/*     */   extends RestActionImpl<Command>
/*     */   implements CommandEditAction
/*     */ {
/*     */   private static final String UNDEFINED = "undefined";
/*     */   private static final int NAME_SET = 1;
/*     */   private static final int DESCRIPTION_SET = 2;
/*     */   private static final int OPTIONS_SET = 4;
/*     */   private final Guild guild;
/*  47 */   private int mask = 0;
/*  48 */   private CommandData data = new CommandData("undefined", "undefined");
/*     */ 
/*     */   
/*     */   public CommandEditActionImpl(JDA api, String id) {
/*  52 */     super(api, Route.Interactions.EDIT_COMMAND.compile(new String[] { api.getSelfUser().getApplicationId(), id }));
/*  53 */     this.guild = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandEditActionImpl(Guild guild, String id) {
/*  58 */     super(guild.getJDA(), Route.Interactions.EDIT_GUILD_COMMAND.compile(new String[] { guild.getJDA().getSelfUser().getApplicationId(), guild.getId(), id }));
/*  59 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction setCheck(BooleanSupplier checks) {
/*  66 */     return (CommandEditAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction deadline(long timestamp) {
/*  73 */     return (CommandEditAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction apply(@Nonnull CommandData commandData) {
/*  80 */     Checks.notNull(commandData, "Command Data");
/*  81 */     this.mask = 7;
/*  82 */     this.data = commandData;
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction setDefaultEnabled(boolean enabled) {
/*  90 */     this.data.setDefaultEnabled(enabled);
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction addCheck(@Nonnull BooleanSupplier checks) {
/*  98 */     return (CommandEditAction)super.addCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction timeout(long timeout, @Nonnull TimeUnit unit) {
/* 105 */     return (CommandEditAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction setName(@Nullable String name) {
/* 112 */     if (name == null) {
/*     */       
/* 114 */       this.mask &= 0xFFFFFFFE;
/* 115 */       return this;
/*     */     } 
/* 117 */     this.data.setName(name);
/* 118 */     this.mask |= 0x1;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction setDescription(@Nullable String description) {
/* 126 */     if (description == null) {
/*     */       
/* 128 */       this.mask &= 0xFFFFFFFD;
/* 129 */       return this;
/*     */     } 
/* 131 */     this.data.setDescription(description);
/* 132 */     this.mask |= 0x2;
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction clearOptions() {
/* 140 */     this.data = new CommandData(this.data.getName(), this.data.getDescription());
/* 141 */     this.mask &= 0xFFFFFFFB;
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction addOptions(@Nonnull OptionData... options) {
/* 149 */     this.data.addOptions(options);
/* 150 */     this.mask |= 0x4;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction addSubcommands(@Nonnull SubcommandData... subcommands) {
/* 158 */     this.data.addSubcommands(subcommands);
/* 159 */     this.mask |= 0x4;
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CommandEditAction addSubcommandGroups(@Nonnull SubcommandGroupData... groups) {
/* 167 */     this.data.addSubcommandGroups(groups);
/* 168 */     this.mask |= 0x4;
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isUnchanged(int flag) {
/* 174 */     return ((this.mask & flag) != flag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 180 */     DataObject json = this.data.toData();
/* 181 */     if (isUnchanged(1))
/* 182 */       json.remove("name"); 
/* 183 */     if (isUnchanged(2))
/* 184 */       json.remove("description"); 
/* 185 */     if (isUnchanged(4))
/* 186 */       json.remove("options"); 
/* 187 */     this.mask = 0;
/* 188 */     this.data = new CommandData("undefined", "undefined");
/* 189 */     return getRequestBody(json);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Command> request) {
/* 195 */     DataObject json = response.getObject();
/* 196 */     request.onSuccess(new Command(this.api, this.guild, json));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\CommandEditActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */