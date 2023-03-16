/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.AccountType;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrivateChannelImpl
/*     */   implements PrivateChannel
/*     */ {
/*     */   private final long id;
/*     */   private User user;
/*     */   private long lastMessageId;
/*     */   
/*     */   public PrivateChannelImpl(long id, User user) {
/*  44 */     this.id = id;
/*  45 */     this.user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateUser() {
/*  51 */     User realUser = getJDA().getUserById(this.user.getIdLong());
/*  52 */     if (realUser != null) {
/*  53 */       this.user = realUser;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getUser() {
/*  60 */     updateUser();
/*  61 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLatestMessageIdLong() {
/*  67 */     long messageId = this.lastMessageId;
/*  68 */     if (messageId < 0L)
/*  69 */       throw new IllegalStateException("No last message id found."); 
/*  70 */     return messageId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLatestMessage() {
/*  76 */     return (this.lastMessageId > 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  83 */     return getUser().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/*  90 */     return ChannelType.PRIVATE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  97 */     return this.user.getJDA();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> close() {
/* 104 */     Route.CompiledRoute route = Route.Channels.DELETE_CHANNEL.compile(new String[] { getId() });
/* 105 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<CompletableFuture<Void>> purgeMessages(@Nonnull List<? extends Message> messages) {
/* 112 */     if (messages == null || messages.isEmpty())
/* 113 */       return Collections.emptyList(); 
/* 114 */     for (Message m : messages) {
/*     */       
/* 116 */       if (m.getAuthor().equals(getJDA().getSelfUser()))
/*     */         continue; 
/* 118 */       throw new IllegalArgumentException("Cannot delete messages of other users in a private channel");
/*     */     } 
/* 120 */     return super.purgeMessages(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 126 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendMessage(@Nonnull CharSequence text) {
/* 133 */     checkBot();
/* 134 */     return super.sendMessage(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendMessage(@Nonnull MessageEmbed embed) {
/* 141 */     checkBot();
/* 142 */     return super.sendMessage(embed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendMessage(@Nonnull Message msg) {
/* 149 */     checkBot();
/* 150 */     return super.sendMessage(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendFile(@Nonnull InputStream data, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
/* 157 */     checkBot();
/* 158 */     return super.sendFile(data, fileName, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendFile(@Nonnull File file, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
/* 165 */     checkBot();
/* 166 */     long maxSize = getJDA().getSelfUser().getAllowedFileSize();
/* 167 */     Checks.check((file == null || file.length() <= maxSize), "File may not exceed the maximum file length of %d bytes!", 
/* 168 */         Long.valueOf(maxSize));
/* 169 */     return super.sendFile(file, fileName, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendFile(@Nonnull byte[] data, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
/* 176 */     checkBot();
/* 177 */     long maxSize = getJDA().getSelfUser().getAllowedFileSize();
/* 178 */     Checks.check((data == null || data.length <= maxSize), "File is too big! Max file-size is %d bytes", Long.valueOf(maxSize));
/* 179 */     return super.sendFile(data, fileName, options);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrivateChannelImpl setLastMessageId(long id) {
/* 184 */     this.lastMessageId = id;
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 194 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 200 */     if (obj == this)
/* 201 */       return true; 
/* 202 */     if (!(obj instanceof PrivateChannelImpl))
/* 203 */       return false; 
/* 204 */     PrivateChannelImpl impl = (PrivateChannelImpl)obj;
/* 205 */     return (impl.id == this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     return "PC:" + getUser().getName() + '(' + getId() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkBot() {
/* 216 */     if (getUser().isBot() && getJDA().getAccountType() == AccountType.BOT)
/* 217 */       throw new UnsupportedOperationException("Cannot send a private message between bots."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\PrivateChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */