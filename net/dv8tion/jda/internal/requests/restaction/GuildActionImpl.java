/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.GuildAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildActionImpl
/*     */   extends RestActionImpl<Void>
/*     */   implements GuildAction
/*     */ {
/*     */   protected String name;
/*     */   protected Region region;
/*     */   protected Icon icon;
/*     */   protected Guild.VerificationLevel verificationLevel;
/*     */   protected Guild.NotificationLevel notificationLevel;
/*     */   protected Guild.ExplicitContentLevel explicitContentLevel;
/*     */   protected final List<GuildAction.RoleData> roles;
/*     */   protected final List<GuildAction.ChannelData> channels;
/*     */   
/*     */   public GuildActionImpl(JDA api, String name) {
/*  53 */     super(api, Route.Guilds.CREATE_GUILD.compile(new String[0]));
/*  54 */     setName(name);
/*     */     
/*  56 */     this.roles = new LinkedList<>();
/*  57 */     this.channels = new LinkedList<>();
/*     */     
/*  59 */     this.roles.add(new GuildAction.RoleData(0L));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildActionImpl setCheck(BooleanSupplier checks) {
/*  66 */     return (GuildActionImpl)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildActionImpl timeout(long timeout, @Nonnull TimeUnit unit) {
/*  73 */     return (GuildActionImpl)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildActionImpl deadline(long timestamp) {
/*  80 */     return (GuildActionImpl)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl setRegion(Region region) {
/*  88 */     Checks.check((region == null || !region.isVip()), "Cannot create a Guild with a VIP voice region!");
/*  89 */     this.region = region;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl setIcon(Icon icon) {
/*  98 */     this.icon = icon;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl setName(@Nonnull String name) {
/* 107 */     Checks.notBlank(name, "Name");
/* 108 */     name = name.trim();
/* 109 */     Checks.notEmpty(name, "Name");
/* 110 */     Checks.notLonger(name, 100, "Name");
/* 111 */     this.name = name;
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl setVerificationLevel(Guild.VerificationLevel level) {
/* 120 */     this.verificationLevel = level;
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl setNotificationLevel(Guild.NotificationLevel level) {
/* 129 */     this.notificationLevel = level;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl setExplicitContentLevel(Guild.ExplicitContentLevel level) {
/* 138 */     this.explicitContentLevel = level;
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl addChannel(@Nonnull GuildAction.ChannelData channel) {
/* 149 */     Checks.notNull(channel, "Channel");
/* 150 */     this.channels.add(channel);
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildAction.ChannelData getChannel(int index) {
/* 159 */     return this.channels.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildAction.ChannelData removeChannel(int index) {
/* 167 */     return this.channels.remove(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildActionImpl removeChannel(@Nonnull GuildAction.ChannelData data) {
/* 175 */     this.channels.remove(data);
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildAction.ChannelData newChannel(@Nonnull ChannelType type, @Nonnull String name) {
/* 184 */     GuildAction.ChannelData data = new GuildAction.ChannelData(type, name);
/* 185 */     addChannel(data);
/* 186 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildAction.RoleData getPublicRole() {
/* 196 */     return this.roles.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildAction.RoleData getRole(int index) {
/* 204 */     return this.roles.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public GuildAction.RoleData newRole() {
/* 212 */     GuildAction.RoleData role = new GuildAction.RoleData(this.roles.size());
/* 213 */     this.roles.add(role);
/* 214 */     return role;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 220 */     DataObject object = DataObject.empty();
/* 221 */     object.put("name", this.name);
/* 222 */     object.put("roles", DataArray.fromCollection(this.roles));
/* 223 */     if (!this.channels.isEmpty())
/* 224 */       object.put("channels", DataArray.fromCollection(this.channels)); 
/* 225 */     if (this.icon != null)
/* 226 */       object.put("icon", this.icon.getEncoding()); 
/* 227 */     if (this.verificationLevel != null)
/* 228 */       object.put("verification_level", Integer.valueOf(this.verificationLevel.getKey())); 
/* 229 */     if (this.notificationLevel != null)
/* 230 */       object.put("default_message_notifications", Integer.valueOf(this.notificationLevel.getKey())); 
/* 231 */     if (this.explicitContentLevel != null)
/* 232 */       object.put("explicit_content_filter", Integer.valueOf(this.explicitContentLevel.getKey())); 
/* 233 */     if (this.region != null)
/* 234 */       object.put("region", this.region.getKey()); 
/* 235 */     return getRequestBody(object);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\GuildActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */