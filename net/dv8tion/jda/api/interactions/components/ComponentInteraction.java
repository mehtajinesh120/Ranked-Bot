/*     */ package net.dv8tion.jda.api.interactions.components;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.Interaction;
/*     */ import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;
/*     */ import net.dv8tion.jda.internal.requests.restaction.interactions.UpdateInteractionActionImpl;
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
/*     */ public interface ComponentInteraction
/*     */   extends Interaction
/*     */ {
/*     */   @Nonnull
/*     */   default String getMessageId() {
/*  86 */     return Long.toUnsignedString(getMessageIdLong());
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default UpdateInteractionAction editMessage(@Nonnull Message message) {
/* 144 */     Checks.notNull(message, "Message");
/* 145 */     UpdateInteractionActionImpl action = (UpdateInteractionActionImpl)deferEdit();
/* 146 */     return action.applyMessage(message);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default UpdateInteractionAction editMessage(@Nonnull String content) {
/* 170 */     Checks.notNull(content, "Content");
/* 171 */     return deferEdit().setContent(content);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default UpdateInteractionAction editComponents(@Nonnull Collection<? extends ComponentLayout> components) {
/* 195 */     Checks.noneNull(components, "Components");
/* 196 */     if (components.stream().anyMatch(it -> !(it instanceof ActionRow)))
/* 197 */       throw new UnsupportedOperationException("The provided component layout is not supported"); 
/* 198 */     Objects.requireNonNull(ActionRow.class); List<ActionRow> actionRows = (List<ActionRow>)components.stream().map(ActionRow.class::cast).collect(Collectors.toList());
/* 199 */     return deferEdit().setActionRows(actionRows);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction editComponents(@Nonnull ComponentLayout... components) {
/* 223 */     Checks.noneNull((Object[])components, "ComponentLayouts");
/* 224 */     return editComponents(Arrays.asList(components));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default UpdateInteractionAction editMessageEmbeds(@Nonnull Collection<? extends MessageEmbed> embeds) {
/* 248 */     Checks.noneNull(embeds, "MessageEmbed");
/* 249 */     return deferEdit().setEmbeds(embeds);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction editMessageEmbeds(@Nonnull MessageEmbed... embeds) {
/* 273 */     Checks.noneNull((Object[])embeds, "MessageEmbed");
/* 274 */     return deferEdit().setEmbeds(embeds);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction editMessageFormat(@Nonnull String format, @Nonnull Object... args) {
/* 300 */     Checks.notNull(format, "Format String");
/* 301 */     return editMessage(String.format(format, args));
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   String getComponentId();
/*     */   
/*     */   @Nullable
/*     */   Component getComponent();
/*     */   
/*     */   @Nonnull
/*     */   Message getMessage();
/*     */   
/*     */   long getMessageIdLong();
/*     */   
/*     */   @Nonnull
/*     */   Component.Type getComponentType();
/*     */   
/*     */   @Nonnull
/*     */   MessageChannel getChannel();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction deferEdit();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\ComponentInteraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */