/*     */ package net.dv8tion.jda.internal.interactions;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
/*     */ import net.dv8tion.jda.api.interactions.commands.OptionMapping;
/*     */ import net.dv8tion.jda.api.interactions.commands.OptionType;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ import net.dv8tion.jda.internal.entities.UserImpl;
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
/*     */ public class CommandInteractionImpl
/*     */   extends InteractionImpl
/*     */   implements CommandInteraction
/*     */ {
/*     */   private final long commandId;
/*  42 */   private final List<OptionMapping> options = new ArrayList<>();
/*  43 */   private final TLongObjectMap<Object> resolved = (TLongObjectMap<Object>)new TLongObjectHashMap();
/*     */   
/*     */   private final String name;
/*     */   private String subcommand;
/*     */   private String group;
/*     */   
/*     */   public CommandInteractionImpl(JDAImpl jda, DataObject data) {
/*  50 */     super(jda, data);
/*  51 */     DataObject commandData = data.getObject("data");
/*  52 */     this.commandId = commandData.getUnsignedLong("id");
/*  53 */     this.name = commandData.getString("name");
/*     */     
/*  55 */     DataArray options = commandData.optArray("options").orElseGet(DataArray::empty);
/*  56 */     DataObject resolveJson = commandData.optObject("resolved").orElseGet(DataObject::empty);
/*     */     
/*  58 */     if (options.length() == 1) {
/*     */       
/*  60 */       DataObject option = options.getObject(0);
/*  61 */       switch (OptionType.fromKey(option.getInt("type"))) {
/*     */         
/*     */         case SUB_COMMAND_GROUP:
/*  64 */           this.group = option.getString("name");
/*  65 */           options = option.getArray("options");
/*  66 */           option = options.getObject(0);
/*     */         case SUB_COMMAND:
/*  68 */           this.subcommand = option.getString("name");
/*  69 */           options = option.optArray("options").orElseGet(DataArray::empty);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  74 */     parseResolved(jda, resolveJson);
/*  75 */     parseOptions(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseOptions(DataArray options) {
/*  82 */     Objects.requireNonNull(this.options); options.stream(DataArray::getObject).map(json -> new OptionMapping(json, this.resolved)).forEach(this.options::add);
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseResolved(JDAImpl jda, DataObject resolveJson) {
/*  87 */     EntityBuilder entityBuilder = jda.getEntityBuilder();
/*     */     
/*  89 */     resolveJson.optObject("users").ifPresent(users -> users.keys().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     if (this.guild != null) {
/*     */       
/*  99 */       resolveJson.optObject("members").ifPresent(members -> members.keys().forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       resolveJson.optObject("roles").ifPresent(roles -> {
/*     */             Objects.requireNonNull(this.guild);
/*     */ 
/*     */             
/*     */             roles.keys().stream().map(this.guild::getRoleById).filter(Objects::nonNull).forEach(());
/*     */           });
/*     */       
/* 116 */       resolveJson.optObject("channels").ifPresent(channels -> channels.keys().forEach(()));
/*     */     } 
/*     */   }
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
/*     */   @Nonnull
/*     */   public MessageChannel getChannel() {
/* 131 */     return (MessageChannel)super.getChannel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 138 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubcommandName() {
/* 144 */     return this.subcommand;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubcommandGroup() {
/* 150 */     return this.group;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCommandIdLong() {
/* 156 */     return this.commandId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<OptionMapping> getOptions() {
/* 163 */     return this.options;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\CommandInteractionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */