/*     */ package net.dv8tion.jda.api.events.interaction;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.Incubating;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.events.Event;
/*     */ import net.dv8tion.jda.api.interactions.Interaction;
/*     */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
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
/*     */ @Incubating
/*     */ public class GenericInteractionCreateEvent
/*     */   extends Event
/*     */   implements Interaction
/*     */ {
/*     */   private final Interaction interaction;
/*     */   
/*     */   public GenericInteractionCreateEvent(@Nonnull JDA api, long responseNumber, @Nonnull Interaction interaction) {
/*  48 */     super(api, responseNumber);
/*  49 */     this.interaction = interaction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Interaction getInteraction() {
/*  61 */     return this.interaction;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getToken() {
/*  68 */     return this.interaction.getToken();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTypeRaw() {
/*  74 */     return this.interaction.getTypeRaw();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Guild getGuild() {
/*  81 */     return this.interaction.getGuild();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AbstractChannel getChannel() {
/*  88 */     return this.interaction.getChannel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InteractionHook getHook() {
/*  95 */     return this.interaction.getHook();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Member getMember() {
/* 102 */     return this.interaction.getMember();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getUser() {
/* 109 */     return this.interaction.getUser();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 115 */     return this.interaction.getIdLong();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAcknowledged() {
/* 121 */     return this.interaction.isAcknowledged();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReplyAction deferReply() {
/* 128 */     return this.interaction.deferReply();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\interaction\GenericInteractionCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */