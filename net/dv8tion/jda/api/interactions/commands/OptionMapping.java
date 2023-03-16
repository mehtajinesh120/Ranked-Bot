/*     */ package net.dv8tion.jda.api.interactions.commands;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IMentionable;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionMapping
/*     */ {
/*     */   private final DataObject data;
/*     */   private final OptionType type;
/*     */   private final String name;
/*     */   private final TLongObjectMap<Object> resolved;
/*     */   
/*     */   public OptionMapping(DataObject data, TLongObjectMap<Object> resolved) {
/*  45 */     this.data = data;
/*  46 */     this.type = OptionType.fromKey(data.getInt("type", -1));
/*  47 */     this.name = data.getString("name");
/*  48 */     this.resolved = resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OptionType getType() {
/*  59 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  70 */     return this.name;
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
/*     */   @Nonnull
/*     */   public String getAsString() {
/*  83 */     return this.data.getString("value");
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
/*     */   public boolean getAsBoolean() {
/*  96 */     if (this.type != OptionType.BOOLEAN)
/*  97 */       throw new IllegalStateException("Cannot convert option of type " + this.type + " to boolean"); 
/*  98 */     return this.data.getBoolean("value");
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
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 114 */     switch (this.type)
/*     */     
/*     */     { default:
/* 117 */         throw new IllegalStateException("Cannot convert option of type " + this.type + " to long");
/*     */       case STRING:
/*     */       case MENTIONABLE:
/*     */       case CHANNEL:
/*     */       case ROLE:
/*     */       case USER:
/*     */       case INTEGER:
/* 124 */         break; }  return this.data.getLong("value");
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
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAsDouble() {
/* 140 */     switch (this.type)
/*     */     
/*     */     { default:
/* 143 */         throw new IllegalStateException("Cannot convert option of type " + this.type + " to double");
/*     */       case STRING:
/*     */       case INTEGER:
/*     */       case NUMBER:
/* 147 */         break; }  return this.data.getDouble("value");
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
/*     */   
/*     */   @Nonnull
/*     */   public IMentionable getAsMentionable() {
/* 162 */     Object entity = this.resolved.get(getAsLong());
/* 163 */     if (entity instanceof IMentionable)
/* 164 */       return (IMentionable)entity; 
/* 165 */     throw new IllegalStateException("Cannot resolve option of type " + this.type + " to IMentionable");
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
/*     */   
/*     */   @Nullable
/*     */   public Member getAsMember() {
/* 180 */     if (this.type != OptionType.USER)
/* 181 */       throw new IllegalStateException("Cannot resolve Member for option " + getName() + " of type " + this.type); 
/* 182 */     Object object = this.resolved.get(getAsLong());
/* 183 */     if (object instanceof Member)
/* 184 */       return (Member)object; 
/* 185 */     return null;
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
/*     */   public User getAsUser() {
/* 199 */     if (this.type != OptionType.USER)
/* 200 */       throw new IllegalStateException("Cannot resolve User for option " + getName() + " of type " + this.type); 
/* 201 */     Object object = this.resolved.get(getAsLong());
/* 202 */     if (object instanceof Member)
/* 203 */       return ((Member)object).getUser(); 
/* 204 */     if (object instanceof User)
/* 205 */       return (User)object; 
/* 206 */     throw new IllegalStateException("Could not resolve user!");
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
/*     */   public Role getAsRole() {
/* 220 */     if (this.type != OptionType.ROLE)
/* 221 */       throw new IllegalStateException("Cannot resolve Role for option " + getName() + " of type " + this.type); 
/* 222 */     Object role = this.resolved.get(getAsLong());
/* 223 */     if (role instanceof Role)
/* 224 */       return (Role)role; 
/* 225 */     throw new IllegalStateException("Could not resolve role!");
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildChannel getAsGuildChannel() {
/* 241 */     AbstractChannel value = getAsChannel();
/* 242 */     if (value instanceof GuildChannel)
/* 243 */       return (GuildChannel)value; 
/* 244 */     throw new IllegalStateException("Could not resolve GuildChannel!");
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
/*     */   
/*     */   @Nullable
/*     */   public MessageChannel getAsMessageChannel() {
/* 259 */     AbstractChannel value = getAsChannel();
/* 260 */     return (value instanceof MessageChannel) ? (MessageChannel)value : null;
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
/*     */   public ChannelType getChannelType() {
/* 274 */     AbstractChannel channel = getAsChannel();
/* 275 */     return (channel == null) ? ChannelType.UNKNOWN : channel.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 281 */     return "Option[" + getType() + "](" + getName() + "=" + getAsString() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 287 */     return Objects.hash(new Object[] { getType(), getName() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 293 */     if (obj == this)
/* 294 */       return true; 
/* 295 */     if (!(obj instanceof OptionMapping))
/* 296 */       return false; 
/* 297 */     OptionMapping data = (OptionMapping)obj;
/* 298 */     return (getType() == data.getType() && getName().equals(data.getName()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private AbstractChannel getAsChannel() {
/* 304 */     if (this.type != OptionType.CHANNEL)
/* 305 */       throw new IllegalStateException("Cannot resolve AbstractChannel for option " + getName() + " of type " + this.type); 
/* 306 */     return (AbstractChannel)this.resolved.get(getAsLong());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\OptionMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */