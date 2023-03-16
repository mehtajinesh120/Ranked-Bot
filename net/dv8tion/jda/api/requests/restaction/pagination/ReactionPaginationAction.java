package net.dv8tion.jda.api.requests.restaction.pagination;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public interface ReactionPaginationAction extends PaginationAction<User, ReactionPaginationAction> {
  @Nonnull
  MessageReaction getReaction();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\pagination\ReactionPaginationAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */