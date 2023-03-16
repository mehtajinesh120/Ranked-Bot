/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Formatter;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.DeferredRestAction;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ public class UserImpl
/*     */   extends UserById
/*     */   implements User
/*     */ {
/*     */   protected final JDAImpl api;
/*     */   protected short discriminator;
/*     */   protected String name;
/*     */   protected String avatarId;
/*     */   protected User.Profile profile;
/*  45 */   protected long privateChannel = 0L;
/*     */   
/*     */   protected boolean bot;
/*     */   protected boolean system;
/*     */   protected boolean fake = false;
/*     */   protected int flags;
/*     */   
/*     */   public UserImpl(long id, JDAImpl api) {
/*  53 */     super(id);
/*  54 */     this.api = api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  61 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDiscriminator() {
/*  68 */     return Helpers.format("%04d", new Object[] { Short.valueOf(this.discriminator) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAvatarId() {
/*  74 */     return this.avatarId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<User.Profile> retrieveProfile() {
/*  81 */     return (RestAction<User.Profile>)new DeferredRestAction((JDA)getJDA(), User.Profile.class, this::getProfile, () -> {
/*     */           Route.CompiledRoute route = Route.Users.GET_USER.compile(new String[] { getId() });
/*     */           return new RestActionImpl((JDA)getJDA(), route, ());
/*     */         });
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
/*     */   public User.Profile getProfile() {
/*  96 */     return this.profile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDefaultAvatarId() {
/* 103 */     return String.valueOf(this.discriminator % 5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsTag() {
/* 110 */     return getName() + '#' + getDiscriminator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrivateChannel() {
/* 116 */     return (this.privateChannel != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<PrivateChannel> openPrivateChannel() {
/* 123 */     return (RestAction<PrivateChannel>)new DeferredRestAction((JDA)getJDA(), PrivateChannel.class, this::getPrivateChannel, () -> {
/*     */           Route.CompiledRoute route = Route.Self.CREATE_PRIVATE_CHANNEL.compile(new String[0]);
/*     */           DataObject body = DataObject.empty().put("recipient_id", getId());
/*     */           return new RestActionImpl((JDA)getJDA(), route, body, ());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateChannel getPrivateChannel() {
/* 137 */     if (!hasPrivateChannel())
/* 138 */       return null; 
/* 139 */     PrivateChannel channel = getJDA().getPrivateChannelById(this.privateChannel);
/* 140 */     return (channel != null) ? channel : new PrivateChannelImpl(this.privateChannel, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Guild> getMutualGuilds() {
/* 147 */     return getJDA().getMutualGuilds(new User[] { this });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBot() {
/* 153 */     return this.bot;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSystem() {
/* 159 */     return this.system;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDAImpl getJDA() {
/* 166 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<User.UserFlag> getFlags() {
/* 173 */     return User.UserFlag.getFlags(this.flags);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFlagsRaw() {
/* 179 */     return this.flags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return "U:" + getName() + '(' + getId() + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserImpl setName(String name) {
/* 192 */     this.name = name;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setDiscriminator(String discriminator) {
/* 198 */     this.discriminator = Short.parseShort(discriminator);
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setAvatarId(String avatarId) {
/* 204 */     this.avatarId = avatarId;
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setProfile(User.Profile profile) {
/* 210 */     this.profile = profile;
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setPrivateChannel(PrivateChannel privateChannel) {
/* 216 */     if (privateChannel != null)
/* 217 */       this.privateChannel = privateChannel.getIdLong(); 
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setBot(boolean bot) {
/* 223 */     this.bot = bot;
/* 224 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setSystem(boolean system) {
/* 229 */     this.system = system;
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setFake(boolean fake) {
/* 235 */     this.fake = fake;
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public UserImpl setFlags(int flags) {
/* 241 */     this.flags = flags;
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(Formatter formatter, int flags, int width, int precision) {
/*     */     String out;
/* 248 */     boolean alt = ((flags & 0x4) == 4);
/* 249 */     boolean upper = ((flags & 0x2) == 2);
/* 250 */     boolean leftJustified = ((flags & 0x1) == 1);
/*     */ 
/*     */     
/* 253 */     if (!alt) {
/* 254 */       out = getAsMention();
/* 255 */     } else if (upper) {
/* 256 */       out = getAsTag().toUpperCase();
/*     */     } else {
/* 258 */       out = getAsTag();
/*     */     } 
/* 260 */     MiscUtil.appendTo(formatter, width, precision, leftJustified, out);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\UserImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */