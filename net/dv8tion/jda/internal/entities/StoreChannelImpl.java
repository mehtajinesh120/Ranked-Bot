/*    */ package net.dv8tion.jda.internal.entities;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.ChannelType;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.GuildChannel;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.StoreChannel;
/*    */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StoreChannelImpl
/*    */   extends AbstractChannelImpl<StoreChannel, StoreChannelImpl>
/*    */   implements StoreChannel
/*    */ {
/*    */   public StoreChannelImpl(long id, GuildImpl guild) {
/* 31 */     super(id, guild);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StoreChannelImpl setPosition(int rawPosition) {
/* 37 */     getGuild().getStoreChannelView().clearCachedLists();
/* 38 */     return super.setPosition(rawPosition);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public ChannelType getType() {
/* 45 */     return ChannelType.STORE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Member> getMembers() {
/* 52 */     return Collections.emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPosition() {
/* 58 */     List<GuildChannel> channels = new ArrayList<>(getGuild().getTextChannels());
/* 59 */     channels.addAll(getGuild().getStoreChannels());
/* 60 */     Collections.sort(channels);
/* 61 */     for (int i = 0; i < channels.size(); i++) {
/*    */       
/* 63 */       if (equals(channels.get(i)))
/* 64 */         return i; 
/*    */     } 
/* 66 */     throw new IllegalStateException("Somehow when determining position we never found the StoreChannel in the Guild's channels? wtf?");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public ChannelAction<StoreChannel> createCopy(@Nonnull Guild guild) {
/* 73 */     throw new UnsupportedOperationException("Bots cannot create store channels");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return "SC:" + getName() + '(' + getId() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\StoreChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */