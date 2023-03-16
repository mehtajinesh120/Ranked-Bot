package net.dv8tion.jda.internal.requests.ratelimit;

import java.util.Queue;
import net.dv8tion.jda.api.requests.Request;

public interface IBucket {
  Queue<Request> getRequests();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\ratelimit\IBucket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */