/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public class VoiceChannelImpl
/*     */   extends AbstractChannelImpl<VoiceChannel, VoiceChannelImpl>
/*     */   implements VoiceChannel
/*     */ {
/*  34 */   private final TLongObjectMap<Member> connectedMembers = MiscUtil.newLongMap();
/*     */   
/*     */   private int userLimit;
/*     */   private int bitrate;
/*     */   private String region;
/*     */   
/*     */   public VoiceChannelImpl(long id, GuildImpl guild) {
/*  41 */     super(id, guild);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoiceChannelImpl setPosition(int rawPosition) {
/*  47 */     getGuild().getVoiceChannelsView().clearCachedLists();
/*  48 */     return super.setPosition(rawPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUserLimit() {
/*  54 */     return this.userLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBitrate() {
/*  60 */     return this.bitrate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/*  67 */     return ChannelType.VOICE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Region getRegion() {
/*  74 */     return (this.region == null) ? Region.AUTOMATIC : Region.fromKey(this.region);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getRegionRaw() {
/*  81 */     return this.region;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getMembers() {
/*  88 */     return Collections.unmodifiableList(new ArrayList<>(getConnectedMembersMap().valueCollection()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  94 */     List<VoiceChannel> channels = getGuild().getVoiceChannels();
/*  95 */     for (int i = 0; i < channels.size(); i++) {
/*     */       
/*  97 */       if (equals(channels.get(i)))
/*  98 */         return i; 
/*     */     } 
/* 100 */     throw new IllegalStateException("Somehow when determining position we never found the VoiceChannel in the Guild's channels? wtf?");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<VoiceChannel> createCopy(@Nonnull Guild guild) {
/* 107 */     Checks.notNull(guild, "Guild");
/* 108 */     ChannelAction<VoiceChannel> action = guild.createVoiceChannel(this.name).setBitrate(Integer.valueOf(this.bitrate)).setUserlimit(Integer.valueOf(this.userLimit));
/* 109 */     if (guild.equals(getGuild())) {
/*     */       
/* 111 */       Category parent = getParent();
/* 112 */       if (parent != null)
/* 113 */         action.setParent(parent); 
/* 114 */       for (PermissionOverride o : this.overrides.valueCollection()) {
/*     */         
/* 116 */         if (o.isMemberOverride()) {
/* 117 */           action.addMemberPermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw()); continue;
/*     */         } 
/* 119 */         action.addRolePermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
/*     */       } 
/*     */     } 
/* 122 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 128 */     if (!(o instanceof VoiceChannel))
/* 129 */       return false; 
/* 130 */     VoiceChannel oVChannel = (VoiceChannel)o;
/* 131 */     return (this == oVChannel || getIdLong() == oVChannel.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return "VC:" + getName() + '(' + this.id + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VoiceChannelImpl setUserLimit(int userLimit) {
/* 144 */     this.userLimit = userLimit;
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoiceChannelImpl setBitrate(int bitrate) {
/* 150 */     this.bitrate = bitrate;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoiceChannelImpl setRegion(String region) {
/* 156 */     this.region = region;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TLongObjectMap<Member> getConnectedMembersMap() {
/* 164 */     this.connectedMembers.transformValues(member -> {
/*     */           Member real = getGuild().getMemberById(member.getIdLong());
/*     */           
/*     */           return (real != null) ? real : member;
/*     */         });
/* 169 */     return this.connectedMembers;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\VoiceChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */