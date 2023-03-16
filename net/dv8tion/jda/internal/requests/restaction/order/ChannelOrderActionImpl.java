/*     */ package net.dv8tion.jda.internal.requests.restaction.order;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
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
/*     */ public class ChannelOrderActionImpl
/*     */   extends OrderActionImpl<GuildChannel, ChannelOrderAction>
/*     */   implements ChannelOrderAction
/*     */ {
/*     */   protected final Guild guild;
/*     */   protected final int bucket;
/*     */   
/*     */   public ChannelOrderActionImpl(Guild guild, int bucket) {
/*  53 */     this(guild, bucket, getChannelsOfType(guild, bucket));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelOrderActionImpl(Guild guild, int bucket, Collection<? extends GuildChannel> channels) {
/*  78 */     super(guild.getJDA(), Route.Guilds.MODIFY_CHANNELS.compile(new String[] { guild.getId() }));
/*     */     
/*  80 */     Checks.notNull(channels, "Channels to order");
/*  81 */     Checks.notEmpty(channels, "Channels to order");
/*  82 */     Checks.check(channels.stream().allMatch(c -> guild.equals(c.getGuild())), "One or more channels are not from the correct guild");
/*     */     
/*  84 */     Checks.check(channels.stream().allMatch(c -> (c.getType().getSortBucket() == bucket)), "One or more channels did not match the expected bucket " + bucket);
/*     */ 
/*     */     
/*  87 */     this.guild = guild;
/*  88 */     this.bucket = bucket;
/*  89 */     this.orderList.addAll(channels);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  96 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSortBucket() {
/* 102 */     return this.bucket;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 108 */     Member self = this.guild.getSelfMember();
/* 109 */     if (!self.hasPermission(new Permission[] { Permission.MANAGE_CHANNEL }))
/* 110 */       throw new InsufficientPermissionException(this.guild, Permission.MANAGE_CHANNEL); 
/* 111 */     DataArray array = DataArray.empty();
/* 112 */     for (int i = 0; i < this.orderList.size(); i++) {
/*     */       
/* 114 */       GuildChannel chan = this.orderList.get(i);
/* 115 */       array.add(DataObject.empty()
/* 116 */           .put("id", chan.getId())
/* 117 */           .put("position", Integer.valueOf(i)));
/*     */     } 
/*     */     
/* 120 */     return getRequestBody(array);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateInput(GuildChannel entity) {
/* 126 */     Checks.check(entity.getGuild().equals(this.guild), "Provided channel is not from this Guild!");
/* 127 */     Checks.check(this.orderList.contains(entity), "Provided channel is not in the list of orderable channels!");
/*     */   }
/*     */ 
/*     */   
/*     */   protected static Collection<GuildChannel> getChannelsOfType(Guild guild, int bucket) {
/* 132 */     return (Collection<GuildChannel>)guild.getChannels().stream()
/* 133 */       .filter(it -> (it.getType().getSortBucket() == bucket))
/* 134 */       .sorted()
/* 135 */       .collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\order\ChannelOrderActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */