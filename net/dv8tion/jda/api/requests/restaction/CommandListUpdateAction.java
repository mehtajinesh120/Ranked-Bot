/*    */ package net.dv8tion.jda.api.requests.restaction;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import javax.annotation.CheckReturnValue;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.interactions.commands.Command;
/*    */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
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
/*    */ public interface CommandListUpdateAction
/*    */   extends RestAction<List<Command>>
/*    */ {
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   CommandListUpdateAction addCommands(@Nonnull CommandData... commands) {
/* 87 */     Checks.noneNull((Object[])commands, "Command");
/* 88 */     return addCommands(Arrays.asList(commands));
/*    */   }
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   CommandListUpdateAction addCommands(@Nonnull Collection<? extends CommandData> paramCollection);
/*    */   
/*    */   @Nonnull
/*    */   CommandListUpdateAction addCheck(@Nonnull BooleanSupplier paramBooleanSupplier);
/*    */   
/*    */   @Nonnull
/*    */   CommandListUpdateAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*    */   
/*    */   @Nonnull
/*    */   CommandListUpdateAction deadline(long paramLong);
/*    */   
/*    */   @Nonnull
/*    */   CommandListUpdateAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\CommandListUpdateAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */