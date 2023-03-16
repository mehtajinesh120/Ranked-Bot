/*     */ package net.dv8tion.jda.api.requests.restaction.interactions;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
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
/*     */ public interface UpdateInteractionAction
/*     */   extends InteractionCallbackAction
/*     */ {
/*     */   @Nonnull
/*     */   UpdateInteractionAction setContent(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction setEmbeds(@Nonnull MessageEmbed... embeds) {
/*  66 */     Checks.noneNull((Object[])embeds, "MessageEmbed");
/*  67 */     return setEmbeds(Arrays.asList(embeds));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction setEmbeds(@Nonnull Collection<? extends MessageEmbed> paramCollection);
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
/*     */   default UpdateInteractionAction setActionRows(@Nonnull Collection<? extends ActionRow> rows) {
/* 100 */     Checks.noneNull(rows, "ActionRows");
/* 101 */     return setActionRows(rows.<ActionRow>toArray(new ActionRow[0]));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction setActionRows(@Nonnull ActionRow... paramVarArgs);
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
/*     */   UpdateInteractionAction setActionRow(@Nonnull Component... components) {
/* 136 */     return setActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default UpdateInteractionAction setActionRow(@Nonnull Collection<? extends Component> components) {
/* 156 */     return setActionRows(new ActionRow[] { ActionRow.of(components) });
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction addFile(@Nonnull File file, @Nonnull AttachmentOption... options) {
/* 178 */     Checks.notNull(file, "File");
/* 179 */     return addFile(file, file.getName(), options);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction addFile(@Nonnull File file, @Nonnull String name, @Nonnull AttachmentOption... options) {
/*     */     try {
/* 217 */       Checks.notNull(file, "File");
/* 218 */       Checks.check((file.exists() && file.canRead()), "Provided file either does not exist or cannot be read from!");
/* 219 */       return addFile(new FileInputStream(file), name, options);
/*     */     }
/* 221 */     catch (FileNotFoundException e) {
/*     */       
/* 223 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction addFile(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
/* 249 */     Checks.notNull(data, "Data");
/* 250 */     return addFile(new ByteArrayInputStream(data), name, options);
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   UpdateInteractionAction addFile(@Nonnull InputStream paramInputStream, @Nonnull String paramString, @Nonnull AttachmentOption... paramVarArgs);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\interactions\UpdateInteractionAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */