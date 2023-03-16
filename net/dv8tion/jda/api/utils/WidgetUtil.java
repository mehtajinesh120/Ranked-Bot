/*      */ package net.dv8tion.jda.api.utils;
/*      */ 
/*      */ import gnu.trove.map.TLongObjectMap;
/*      */ import gnu.trove.map.hash.TLongObjectHashMap;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UncheckedIOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.OnlineStatus;
/*      */ import net.dv8tion.jda.api.entities.Activity;
/*      */ import net.dv8tion.jda.api.entities.Guild;
/*      */ import net.dv8tion.jda.api.entities.IMentionable;
/*      */ import net.dv8tion.jda.api.entities.ISnowflake;
/*      */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*      */ import net.dv8tion.jda.api.utils.data.DataArray;
/*      */ import net.dv8tion.jda.api.utils.data.DataObject;
/*      */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*      */ import net.dv8tion.jda.internal.requests.Requester;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ import net.dv8tion.jda.internal.utils.Helpers;
/*      */ import net.dv8tion.jda.internal.utils.IOUtil;
/*      */ import okhttp3.OkHttpClient;
/*      */ import okhttp3.Request;
/*      */ import okhttp3.Response;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class WidgetUtil
/*      */ {
/*   53 */   public static final String WIDGET_PNG = Requester.DISCORD_API_PREFIX + "guilds/%s/widget.png?style=%s";
/*   54 */   public static final String WIDGET_URL = Requester.DISCORD_API_PREFIX + "guilds/%s/widget.json";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String WIDGET_HTML = "<iframe src=\"https://discord.com/widget?id=%s&theme=%s\" width=\"%d\" height=\"%d\" allowtransparency=\"true\" frameborder=\"0\"></iframe>";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public static String getWidgetBanner(@Nonnull Guild guild, @Nonnull BannerType type) {
/*   72 */     Checks.notNull(guild, "Guild");
/*   73 */     return getWidgetBanner(guild.getId(), type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public static String getWidgetBanner(@Nonnull String guildId, @Nonnull BannerType type) {
/*   92 */     Checks.notNull(guildId, "GuildId");
/*   93 */     Checks.notNull(type, "BannerType");
/*   94 */     return String.format(WIDGET_PNG, new Object[] { guildId, type.name().toLowerCase() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public static String getPremadeWidgetHtml(@Nonnull Guild guild, @Nonnull WidgetTheme theme, int width, int height) {
/*  116 */     Checks.notNull(guild, "Guild");
/*  117 */     return getPremadeWidgetHtml(guild.getId(), theme, width, height);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   public static String getPremadeWidgetHtml(@Nonnull String guildId, @Nonnull WidgetTheme theme, int width, int height) {
/*  140 */     Checks.notNull(guildId, "GuildId");
/*  141 */     Checks.notNull(theme, "WidgetTheme");
/*  142 */     Checks.notNegative(width, "Width");
/*  143 */     Checks.notNegative(height, "Height");
/*  144 */     return Helpers.format("<iframe src=\"https://discord.com/widget?id=%s&theme=%s\" width=\"%d\" height=\"%d\" allowtransparency=\"true\" frameborder=\"0\"></iframe>", new Object[] { guildId, theme.name().toLowerCase(), Integer.valueOf(width), Integer.valueOf(height) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Widget getWidget(@Nonnull String guildId) throws RateLimitedException {
/*  175 */     return getWidget(MiscUtil.parseSnowflake(guildId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Widget getWidget(long guildId) throws RateLimitedException {
/*  206 */     Checks.notNull(Long.valueOf(guildId), "GuildId");
/*      */ 
/*      */     
/*  209 */     OkHttpClient client = (new OkHttpClient.Builder()).build();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  215 */     Request request = (new Request.Builder()).url(String.format(WIDGET_URL, new Object[] { Long.valueOf(guildId) })).method("GET", null).header("user-agent", Requester.USER_AGENT).header("accept-encoding", "gzip").build();
/*      */     
/*  217 */     try { Response response = client.newCall(request).execute(); 
/*      */       try { Widget widget; long retryAfter;
/*  219 */         int code = response.code();
/*  220 */         InputStream data = IOUtil.getBody(response);
/*      */         
/*  222 */         switch (code)
/*      */         
/*      */         { case 200:
/*      */             
/*  226 */             try { InputStream stream = data;
/*      */               
/*  228 */               try { Widget widget1 = new Widget(DataObject.fromJson(stream));
/*  229 */                 if (stream != null) stream.close();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*  256 */                 if (response != null) response.close();  return widget1; } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e) { throw new UncheckedIOException(e); } case 400: case 404: widget = null; if (response != null) response.close();  return widget;case 403: widget = new Widget(guildId); if (response != null) response.close();  return widget;case 429: try { InputStream stream = data; try { retryAfter = DataObject.fromJson(stream).getLong("retry_after"); if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e) { retryAfter = 0L; }  throw new RateLimitedException(WIDGET_URL, retryAfter); }  throw new IllegalStateException("An unknown status was returned: " + code + " " + response.message()); } catch (Throwable throwable) { if (response != null)
/*  257 */           try { response.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*      */     
/*  259 */     { throw new UncheckedIOException(e); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum BannerType
/*      */   {
/*  276 */     SHIELD, BANNER1, BANNER2, BANNER3, BANNER4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum WidgetTheme
/*      */   {
/*  285 */     LIGHT, DARK;
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Widget
/*      */     implements ISnowflake
/*      */   {
/*      */     private final boolean isAvailable;
/*      */     
/*      */     private final long id;
/*      */     
/*      */     private final String name;
/*      */     private final String invite;
/*      */     private final TLongObjectMap<VoiceChannel> channels;
/*      */     private final TLongObjectMap<Member> members;
/*      */     
/*      */     private Widget(long guildId) {
/*  302 */       this.isAvailable = false;
/*  303 */       this.id = guildId;
/*  304 */       this.name = null;
/*  305 */       this.invite = null;
/*  306 */       this.channels = (TLongObjectMap<VoiceChannel>)new TLongObjectHashMap();
/*  307 */       this.members = (TLongObjectMap<Member>)new TLongObjectHashMap();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Widget(@Nonnull DataObject json) {
/*  318 */       String inviteCode = json.getString("instant_invite", null);
/*  319 */       if (inviteCode != null) {
/*  320 */         inviteCode = inviteCode.substring(inviteCode.lastIndexOf("/") + 1);
/*      */       }
/*  322 */       this.isAvailable = true;
/*  323 */       this.id = json.getLong("id");
/*  324 */       this.name = json.getString("name");
/*  325 */       this.invite = inviteCode;
/*  326 */       this.channels = MiscUtil.newLongMap();
/*  327 */       this.members = MiscUtil.newLongMap();
/*      */       
/*  329 */       DataArray channelsJson = json.getArray("channels");
/*  330 */       for (int i = 0; i < channelsJson.length(); i++) {
/*      */         
/*  332 */         DataObject channel = channelsJson.getObject(i);
/*  333 */         this.channels.put(channel.getLong("id"), new VoiceChannel(channel, this));
/*      */       } 
/*      */       
/*  336 */       DataArray membersJson = json.getArray("members");
/*  337 */       for (int j = 0; j < membersJson.length(); j++) {
/*      */         
/*  339 */         DataObject memberJson = membersJson.getObject(j);
/*  340 */         Member member = new Member(memberJson, this);
/*  341 */         if (!memberJson.isNull("channel_id")) {
/*      */           
/*  343 */           VoiceChannel channel = (VoiceChannel)this.channels.get(memberJson.getLong("channel_id"));
/*  344 */           member.setVoiceState(new VoiceState(channel, memberJson
/*  345 */                 .getBoolean("mute"), memberJson
/*  346 */                 .getBoolean("deaf"), memberJson
/*  347 */                 .getBoolean("suppress"), memberJson
/*  348 */                 .getBoolean("self_mute"), memberJson
/*  349 */                 .getBoolean("self_deaf"), member, this));
/*      */ 
/*      */           
/*  352 */           channel.addMember(member);
/*      */         } 
/*  354 */         this.members.put(member.getIdLong(), member);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAvailable() {
/*  366 */       return this.isAvailable;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long getIdLong() {
/*  372 */       return this.id;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public String getName() {
/*  386 */       checkAvailable();
/*      */       
/*  388 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public String getInviteCode() {
/*  403 */       checkAvailable();
/*      */       
/*  405 */       return this.invite;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public List<VoiceChannel> getVoiceChannels() {
/*  419 */       checkAvailable();
/*      */       
/*  421 */       return Collections.unmodifiableList(new ArrayList<>(this.channels.valueCollection()));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public VoiceChannel getVoiceChannelById(String id) {
/*  440 */       checkAvailable();
/*      */       
/*  442 */       return (VoiceChannel)this.channels.get(MiscUtil.parseSnowflake(id));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public VoiceChannel getVoiceChannelById(long id) {
/*  459 */       checkAvailable();
/*      */       
/*  461 */       return (VoiceChannel)this.channels.get(id);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nonnull
/*      */     public List<Member> getMembers() {
/*  475 */       checkAvailable();
/*      */       
/*  477 */       return Collections.unmodifiableList(new ArrayList<>(this.members.valueCollection()));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Member getMemberById(String id) {
/*  496 */       checkAvailable();
/*      */       
/*  498 */       return (Member)this.members.get(MiscUtil.parseSnowflake(id));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Member getMemberById(long id) {
/*  515 */       checkAvailable();
/*      */       
/*  517 */       return (Member)this.members.get(id);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  522 */       return Long.hashCode(this.id);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  527 */       if (!(obj instanceof Widget))
/*  528 */         return false; 
/*  529 */       Widget oWidget = (Widget)obj;
/*  530 */       return (this == oWidget || this.id == oWidget.getIdLong());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  536 */       return "W:" + (isAvailable() ? getName() : "") + '(' + this.id + ')';
/*      */     }
/*      */ 
/*      */     
/*      */     private void checkAvailable() {
/*  541 */       if (!this.isAvailable)
/*  542 */         throw new IllegalStateException("The widget for this Guild is unavailable!"); 
/*      */     }
/*      */     
/*      */     public static class Member
/*      */       implements IMentionable
/*      */     {
/*      */       private final boolean bot;
/*      */       private final long id;
/*      */       private final String username;
/*      */       private final String discriminator;
/*      */       private final String avatar;
/*      */       private final String nickname;
/*      */       private final OnlineStatus status;
/*      */       private final Activity game;
/*      */       private final WidgetUtil.Widget widget;
/*      */       private WidgetUtil.Widget.VoiceState state;
/*      */       
/*      */       private Member(@Nonnull DataObject json, @Nonnull WidgetUtil.Widget widget) {
/*  560 */         this.widget = widget;
/*  561 */         this.bot = json.getBoolean("bot");
/*  562 */         this.id = json.getLong("id");
/*  563 */         this.username = json.getString("username");
/*  564 */         this.discriminator = json.getString("discriminator");
/*  565 */         this.avatar = json.getString("avatar", null);
/*  566 */         this.nickname = json.getString("nick", null);
/*  567 */         this.status = OnlineStatus.fromKey(json.getString("status"));
/*  568 */         this.game = json.isNull("game") ? null : EntityBuilder.createActivity(json.getObject("game"));
/*      */       }
/*      */ 
/*      */       
/*      */       private void setVoiceState(WidgetUtil.Widget.VoiceState voiceState) {
/*  573 */         this.state = voiceState;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isBot() {
/*  583 */         return this.bot;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getName() {
/*  594 */         return this.username;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public long getIdLong() {
/*  600 */         return this.id;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getAsMention() {
/*  607 */         return "<@" + getId() + ">";
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getDiscriminator() {
/*  618 */         return this.discriminator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nullable
/*      */       public String getAvatarId() {
/*  631 */         return this.avatar;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nullable
/*      */       public String getAvatarUrl() {
/*  644 */         String avatarId = getAvatarId();
/*  645 */         return (avatarId == null) ? null : String.format("https://cdn.discordapp.com/avatars/%s/%s.%s", new Object[] { getId(), avatarId, avatarId.startsWith("a_") ? ".gif" : ".png" });
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getDefaultAvatarId() {
/*  657 */         return String.valueOf(Integer.parseInt(getDiscriminator()) % 5);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getDefaultAvatarUrl() {
/*  669 */         return String.format("https://cdn.discordapp.com/embed/avatars/%s.png", new Object[] { getDefaultAvatarId() });
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getEffectiveAvatarUrl() {
/*  682 */         String avatarUrl = getAvatarUrl();
/*  683 */         return (avatarUrl == null) ? getDefaultAvatarUrl() : avatarUrl;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nullable
/*      */       public String getNickname() {
/*  695 */         return this.nickname;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getEffectiveName() {
/*  707 */         return (this.nickname == null) ? this.username : this.nickname;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public OnlineStatus getOnlineStatus() {
/*  719 */         return this.status;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nullable
/*      */       public Activity getActivity() {
/*  733 */         return this.game;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public WidgetUtil.Widget.VoiceState getVoiceState() {
/*  745 */         return (this.state == null) ? new WidgetUtil.Widget.VoiceState(this, this.widget) : this.state;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public WidgetUtil.Widget getWidget() {
/*  756 */         return this.widget;
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  761 */         return (this.widget.getId() + ' ' + this.id).hashCode();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  766 */         if (!(obj instanceof Member))
/*  767 */           return false; 
/*  768 */         Member oMember = (Member)obj;
/*  769 */         return (this == oMember || (this.id == oMember.getIdLong() && this.widget.getIdLong() == oMember.getWidget().getIdLong()));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/*  775 */         return "W.M:" + getName() + '(' + this.id + ')';
/*      */       }
/*      */     }
/*      */     
/*      */     public static class VoiceChannel
/*      */       implements ISnowflake
/*      */     {
/*      */       private final int position;
/*      */       private final long id;
/*      */       private final String name;
/*      */       private final List<WidgetUtil.Widget.Member> members;
/*      */       private final WidgetUtil.Widget widget;
/*      */       
/*      */       private VoiceChannel(@Nonnull DataObject json, @Nonnull WidgetUtil.Widget widget) {
/*  789 */         this.widget = widget;
/*  790 */         this.position = json.getInt("position");
/*  791 */         this.id = json.getLong("id");
/*  792 */         this.name = json.getString("name");
/*  793 */         this.members = new ArrayList<>();
/*      */       }
/*      */ 
/*      */       
/*      */       private void addMember(@Nonnull WidgetUtil.Widget.Member member) {
/*  798 */         this.members.add(member);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public int getPosition() {
/*  808 */         return this.position;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public long getIdLong() {
/*  814 */         return this.id;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public String getName() {
/*  825 */         return this.name;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public List<WidgetUtil.Widget.Member> getMembers() {
/*  836 */         return this.members;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public WidgetUtil.Widget getWidget() {
/*  847 */         return this.widget;
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  852 */         return Long.hashCode(this.id);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  857 */         if (!(obj instanceof VoiceChannel))
/*  858 */           return false; 
/*  859 */         VoiceChannel oVChannel = (VoiceChannel)obj;
/*  860 */         return (this == oVChannel || this.id == oVChannel.getIdLong());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/*  866 */         return "W.VC:" + getName() + '(' + this.id + ')';
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public static class VoiceState
/*      */     {
/*      */       private final WidgetUtil.Widget.VoiceChannel channel;
/*      */       private final boolean muted;
/*      */       private final boolean deafened;
/*      */       private final boolean suppress;
/*      */       private final boolean selfMute;
/*      */       private final boolean selfDeaf;
/*      */       private final WidgetUtil.Widget.Member member;
/*      */       private final WidgetUtil.Widget widget;
/*      */       
/*      */       private VoiceState(@Nonnull WidgetUtil.Widget.Member member, @Nonnull WidgetUtil.Widget widget) {
/*  883 */         this(null, false, false, false, false, false, member, widget);
/*      */       }
/*      */ 
/*      */       
/*      */       private VoiceState(@Nullable WidgetUtil.Widget.VoiceChannel channel, boolean muted, boolean deafened, boolean suppress, boolean selfMute, boolean selfDeaf, @Nonnull WidgetUtil.Widget.Member member, @Nonnull WidgetUtil.Widget widget) {
/*  888 */         this.channel = channel;
/*  889 */         this.muted = muted;
/*  890 */         this.deafened = deafened;
/*  891 */         this.suppress = suppress;
/*  892 */         this.selfMute = selfMute;
/*  893 */         this.selfDeaf = selfDeaf;
/*  894 */         this.member = member;
/*  895 */         this.widget = widget;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Nullable
/*      */       public WidgetUtil.Widget.VoiceChannel getChannel() {
/*  906 */         return this.channel;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean inVoiceChannel() {
/*  917 */         return (this.channel != null);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isGuildMuted() {
/*  927 */         return this.muted;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isGuildDeafened() {
/*  937 */         return this.deafened;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isSuppressed() {
/*  947 */         return this.suppress;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isSelfMuted() {
/*  957 */         return this.selfMute;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isSelfDeafened() {
/*  967 */         return this.selfDeaf;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isMuted() {
/*  977 */         return (this.selfMute || this.muted);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean isDeafened() {
/*  987 */         return (this.selfDeaf || this.deafened);
/*      */       }
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public WidgetUtil.Widget.Member getMember() {
/*  993 */         return this.member;
/*      */       }
/*      */ 
/*      */       
/*      */       @Nonnull
/*      */       public WidgetUtil.Widget getWidget() {
/*  999 */         return this.widget;
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/* 1004 */         return this.member.hashCode();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/* 1009 */         if (!(obj instanceof VoiceState))
/* 1010 */           return false; 
/* 1011 */         VoiceState oState = (VoiceState)obj;
/* 1012 */         return (this == oState || (this.member.equals(oState.getMember()) && this.widget.equals(oState.getWidget())));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1017 */         return "VS:" + this.widget.getName() + ':' + this.member.getEffectiveName();
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\WidgetUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */