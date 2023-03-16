/*     */ package net.dv8tion.jda.api.interactions.commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.interactions.Interaction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CommandInteraction
/*     */   extends Interaction
/*     */ {
/*     */   @Nonnull
/*     */   default String getCommandPath() {
/* 108 */     StringBuilder builder = new StringBuilder(getName());
/* 109 */     if (getSubcommandGroup() != null)
/* 110 */       builder.append('/').append(getSubcommandGroup()); 
/* 111 */     if (getSubcommandName() != null)
/* 112 */       builder.append('/').append(getSubcommandName()); 
/* 113 */     return builder.toString();
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
/*     */   @Nonnull
/*     */   default String getCommandId() {
/* 135 */     return Long.toUnsignedString(getCommandIdLong());
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
/*     */   @Nonnull
/*     */   default List<OptionMapping> getOptionsByName(@Nonnull String name) {
/* 163 */     Checks.notNull(name, "Name");
/* 164 */     return (List<OptionMapping>)getOptions().stream()
/* 165 */       .filter(opt -> opt.getName().equals(name))
/* 166 */       .collect(Collectors.toList());
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
/*     */   default List<OptionMapping> getOptionsByType(@Nonnull OptionType type) {
/* 183 */     Checks.notNull(type, "Type");
/* 184 */     return (List<OptionMapping>)getOptions().stream()
/* 185 */       .filter(it -> (it.getType() == type))
/* 186 */       .collect(Collectors.toList());
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
/*     */   @Nullable
/*     */   default OptionMapping getOption(@Nonnull String name) {
/* 203 */     List<OptionMapping> options = getOptionsByName(name);
/* 204 */     return options.isEmpty() ? null : options.get(0);
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
/*     */   @Nonnull
/*     */   default String getCommandString() {
/* 219 */     StringBuilder builder = new StringBuilder();
/* 220 */     builder.append("/").append(getName());
/* 221 */     if (getSubcommandGroup() != null)
/* 222 */       builder.append(" ").append(getSubcommandGroup()); 
/* 223 */     if (getSubcommandName() != null)
/* 224 */       builder.append(" ").append(getSubcommandName()); 
/* 225 */     for (OptionMapping o : getOptions()) {
/*     */       
/* 227 */       builder.append(" ").append(o.getName()).append(": ");
/* 228 */       switch (o.getType()) {
/*     */         
/*     */         case CHANNEL:
/* 231 */           builder.append("#").append(o.getAsGuildChannel().getName());
/*     */           continue;
/*     */         case USER:
/* 234 */           builder.append("@").append(o.getAsUser().getName());
/*     */           continue;
/*     */         case ROLE:
/* 237 */           builder.append("@").append(o.getAsRole().getName());
/*     */           continue;
/*     */         case MENTIONABLE:
/* 240 */           if (o.getAsMentionable() instanceof net.dv8tion.jda.api.entities.Role) {
/* 241 */             builder.append("@").append(o.getAsRole().getName()); continue;
/* 242 */           }  if (o.getAsMentionable() instanceof net.dv8tion.jda.api.entities.Member) {
/* 243 */             builder.append("@").append(o.getAsUser().getName()); continue;
/* 244 */           }  if (o.getAsMentionable() instanceof net.dv8tion.jda.api.entities.User) {
/* 245 */             builder.append("@").append(o.getAsUser().getName()); continue;
/*     */           } 
/* 247 */           builder.append("@").append(o.getAsMentionable().getIdLong());
/*     */           continue;
/*     */       } 
/* 250 */       builder.append(o.getAsString());
/*     */     } 
/*     */ 
/*     */     
/* 254 */     return builder.toString();
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   String getName();
/*     */   
/*     */   @Nullable
/*     */   String getSubcommandName();
/*     */   
/*     */   @Nullable
/*     */   String getSubcommandGroup();
/*     */   
/*     */   @Nonnull
/*     */   MessageChannel getChannel();
/*     */   
/*     */   long getCommandIdLong();
/*     */   
/*     */   @Nonnull
/*     */   List<OptionMapping> getOptions();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\CommandInteraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */