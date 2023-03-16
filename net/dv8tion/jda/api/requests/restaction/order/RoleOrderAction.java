package net.dv8tion.jda.api.requests.restaction.order;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public interface RoleOrderAction extends OrderAction<Role, RoleOrderAction> {
  @Nonnull
  Guild getGuild();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\order\RoleOrderAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */