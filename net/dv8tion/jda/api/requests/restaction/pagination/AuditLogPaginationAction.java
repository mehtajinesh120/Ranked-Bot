package net.dv8tion.jda.api.requests.restaction.pagination;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public interface AuditLogPaginationAction extends PaginationAction<AuditLogEntry, AuditLogPaginationAction> {
  @Nonnull
  Guild getGuild();
  
  @Nonnull
  AuditLogPaginationAction type(@Nullable ActionType paramActionType);
  
  @Nonnull
  AuditLogPaginationAction user(@Nullable User paramUser);
  
  @Nonnull
  AuditLogPaginationAction user(@Nullable String paramString);
  
  @Nonnull
  AuditLogPaginationAction user(long paramLong);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\pagination\AuditLogPaginationAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */