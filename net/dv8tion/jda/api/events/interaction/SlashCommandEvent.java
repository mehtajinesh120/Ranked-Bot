/*    */ package net.dv8tion.jda.api.events.interaction;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.interactions.Interaction;
/*    */ import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
/*    */ import net.dv8tion.jda.api.interactions.commands.OptionMapping;
/*    */ import net.dv8tion.jda.internal.interactions.CommandInteractionImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SlashCommandEvent
/*    */   extends GenericInteractionCreateEvent
/*    */   implements CommandInteraction
/*    */ {
/*    */   private final CommandInteractionImpl commandInteraction;
/*    */   
/*    */   public SlashCommandEvent(@Nonnull JDA api, long responseNumber, @Nonnull CommandInteractionImpl interaction) {
/* 42 */     super(api, responseNumber, (Interaction)interaction);
/* 43 */     this.commandInteraction = interaction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public MessageChannel getChannel() {
/* 50 */     return this.commandInteraction.getChannel();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getName() {
/* 57 */     return this.commandInteraction.getName();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getSubcommandName() {
/* 64 */     return this.commandInteraction.getSubcommandName();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getSubcommandGroup() {
/* 71 */     return this.commandInteraction.getSubcommandGroup();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getCommandIdLong() {
/* 77 */     return this.commandInteraction.getCommandIdLong();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<OptionMapping> getOptions() {
/* 84 */     return this.commandInteraction.getOptions();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getCommandString() {
/* 91 */     return this.commandInteraction.getCommandString();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\interaction\SlashCommandEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */