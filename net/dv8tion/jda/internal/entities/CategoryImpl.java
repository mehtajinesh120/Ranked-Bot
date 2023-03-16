/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Invite;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.StageChannel;
/*     */ import net.dv8tion.jda.api.entities.StoreChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.InviteAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
/*     */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.PermissionUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CategoryImpl
/*     */   extends AbstractChannelImpl<Category, CategoryImpl>
/*     */   implements Category
/*     */ {
/*     */   public CategoryImpl(long id, GuildImpl guild) {
/*  38 */     super(id, guild);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CategoryImpl setPosition(int rawPosition) {
/*  44 */     getGuild().getCategoriesView().clearCachedLists();
/*  45 */     return super.setPosition(rawPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Category getParent() {
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/*  58 */     return ChannelType.CATEGORY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getMembers() {
/*  65 */     return Collections.unmodifiableList((List<? extends Member>)getChannels().stream()
/*  66 */         .map(GuildChannel::getMembers)
/*  67 */         .flatMap(Collection::stream)
/*  68 */         .distinct()
/*  69 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  77 */     List<Category> channels = getGuild().getCategories();
/*  78 */     for (int i = 0; i < channels.size(); i++) {
/*     */       
/*  80 */       if (equals(channels.get(i)))
/*  81 */         return i; 
/*     */     } 
/*  83 */     throw new IllegalStateException("Somehow when determining position we never found the Category in the Guild's channels? wtf?");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<Category> createCopy(@Nonnull Guild guild) {
/*  90 */     Checks.notNull(guild, "Guild");
/*  91 */     ChannelAction<Category> action = guild.createCategory(this.name);
/*  92 */     if (guild.equals(getGuild()))
/*     */     {
/*  94 */       for (PermissionOverride o : this.overrides.valueCollection()) {
/*     */         
/*  96 */         if (o.isMemberOverride()) {
/*  97 */           action.addMemberPermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw()); continue;
/*     */         } 
/*  99 */         action.addRolePermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
/*     */       } 
/*     */     }
/* 102 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteAction createInvite() {
/* 109 */     throw new UnsupportedOperationException("Cannot create invites for category!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<List<Invite>> retrieveInvites() {
/* 116 */     return (RestAction<List<Invite>>)new CompletedRestAction(getJDA(), Collections.emptyList());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<GuildChannel> getChannels() {
/* 123 */     List<GuildChannel> channels = new ArrayList<>();
/* 124 */     channels.addAll(getStoreChannels());
/* 125 */     channels.addAll(getTextChannels());
/* 126 */     channels.addAll(getVoiceChannels());
/* 127 */     Collections.sort(channels);
/* 128 */     return Collections.unmodifiableList(channels);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<StoreChannel> getStoreChannels() {
/* 135 */     return Collections.unmodifiableList((List<? extends StoreChannel>)getGuild().getStoreChannelCache().stream()
/* 136 */         .filter(channel -> equals(channel.getParent()))
/* 137 */         .sorted().collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<TextChannel> getTextChannels() {
/* 144 */     return Collections.unmodifiableList((List<? extends TextChannel>)getGuild().getTextChannels().stream()
/* 145 */         .filter(channel -> equals(channel.getParent()))
/* 146 */         .sorted().collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<VoiceChannel> getVoiceChannels() {
/* 153 */     return Collections.unmodifiableList((List<? extends VoiceChannel>)getGuild().getVoiceChannels().stream()
/* 154 */         .filter(channel -> equals(channel.getParent()))
/* 155 */         .sorted().collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<TextChannel> createTextChannel(@Nonnull String name) {
/* 162 */     ChannelAction<TextChannel> action = getGuild().createTextChannel(name, this);
/* 163 */     return trySync(action);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<VoiceChannel> createVoiceChannel(@Nonnull String name) {
/* 170 */     ChannelAction<VoiceChannel> action = getGuild().createVoiceChannel(name, this);
/* 171 */     return trySync(action);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<StageChannel> createStageChannel(@Nonnull String name) {
/* 178 */     ChannelAction<StageChannel> action = getGuild().createStageChannel(name, this);
/* 179 */     return trySync(action);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends GuildChannel> ChannelAction<T> trySync(ChannelAction<T> action) {
/* 184 */     Member selfMember = getGuild().getSelfMember();
/* 185 */     if (!selfMember.canSync(this)) {
/*     */       
/* 187 */       long botPerms = PermissionUtil.getEffectivePermission(this, selfMember);
/* 188 */       for (PermissionOverride override : getPermissionOverrides()) {
/*     */         
/* 190 */         long perms = override.getDeniedRaw() | override.getAllowedRaw();
/* 191 */         if ((perms & (botPerms ^ 0xFFFFFFFFFFFFFFFFL)) != 0L)
/* 192 */           return action; 
/*     */       } 
/*     */     } 
/* 195 */     return action.syncPermissionOverrides();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CategoryOrderAction modifyTextChannelPositions() {
/* 202 */     return getGuild().modifyTextChannelPositions(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CategoryOrderAction modifyVoiceChannelPositions() {
/* 209 */     return getGuild().modifyVoiceChannelPositions(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     return "GC:" + getName() + '(' + this.id + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\CategoryImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */