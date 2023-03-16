/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Route
/*     */ {
/*     */   private static final String majorParameters = "guild_id:channel_id:webhook_id:interaction_token";
/*     */   private final String route;
/*     */   private final Method method;
/*     */   private final int paramCount;
/*     */   
/*     */   public static class Misc
/*     */   {
/*  35 */     public static final Route TRACK = new Route(Method.POST, "track");
/*  36 */     public static final Route GET_VOICE_REGIONS = new Route(Method.GET, "voice/regions");
/*  37 */     public static final Route GATEWAY = new Route(Method.GET, "gateway");
/*  38 */     public static final Route GATEWAY_BOT = new Route(Method.GET, "gateway/bot");
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Applications
/*     */   {
/*  44 */     public static final Route GET_BOT_APPLICATION = new Route(Method.GET, "oauth2/applications/@me");
/*     */ 
/*     */     
/*  47 */     public static final Route GET_APPLICATIONS = new Route(Method.GET, "oauth2/applications");
/*  48 */     public static final Route CREATE_APPLICATION = new Route(Method.POST, "oauth2/applications");
/*  49 */     public static final Route GET_APPLICATION = new Route(Method.GET, "oauth2/applications/{application_id}");
/*  50 */     public static final Route MODIFY_APPLICATION = new Route(Method.PUT, "oauth2/applications/{application_id}");
/*  51 */     public static final Route DELETE_APPLICATION = new Route(Method.DELETE, "oauth2/applications/{application_id}");
/*     */     
/*  53 */     public static final Route CREATE_BOT = new Route(Method.POST, "oauth2/applications/{application_id}/bot");
/*     */     
/*  55 */     public static final Route RESET_APPLICATION_SECRET = new Route(Method.POST, "oauth2/applications/{application_id}/reset");
/*  56 */     public static final Route RESET_BOT_TOKEN = new Route(Method.POST, "oauth2/applications/{application_id}/bot/reset");
/*     */     
/*  58 */     public static final Route GET_AUTHORIZED_APPLICATIONS = new Route(Method.GET, "oauth2/tokens");
/*  59 */     public static final Route GET_AUTHORIZED_APPLICATION = new Route(Method.GET, "oauth2/tokens/{auth_id}");
/*  60 */     public static final Route DELETE_AUTHORIZED_APPLICATION = new Route(Method.DELETE, "oauth2/tokens/{auth_id}");
/*     */   }
/*     */   
/*     */   public static class Interactions
/*     */   {
/*  65 */     public static final Route GET_COMMANDS = new Route(Method.GET, "applications/{application_id}/commands");
/*  66 */     public static final Route GET_COMMAND = new Route(Method.GET, "applications/{application_id}/commands/{command_id}");
/*  67 */     public static final Route CREATE_COMMAND = new Route(Method.POST, "applications/{application_id}/commands");
/*  68 */     public static final Route UPDATE_COMMANDS = new Route(Method.PUT, "applications/{application_id}/commands");
/*  69 */     public static final Route EDIT_COMMAND = new Route(Method.PATCH, "applications/{application_id}/commands/{command_id}");
/*  70 */     public static final Route DELETE_COMMAND = new Route(Method.DELETE, "applications/{application_id}/commands/{command_id}");
/*     */     
/*  72 */     public static final Route GET_GUILD_COMMANDS = new Route(Method.GET, "applications/{application_id}/guilds/{guild_id}/commands");
/*  73 */     public static final Route GET_GUILD_COMMAND = new Route(Method.GET, "applications/{application_id}/guilds/{guild_id}/commands/{command_id}");
/*  74 */     public static final Route CREATE_GUILD_COMMAND = new Route(Method.POST, "applications/{application_id}/guilds/{guild_id}/commands");
/*  75 */     public static final Route UPDATE_GUILD_COMMANDS = new Route(Method.PUT, "applications/{application_id}/guilds/{guild_id}/commands");
/*  76 */     public static final Route EDIT_GUILD_COMMAND = new Route(Method.PATCH, "applications/{application_id}/guilds/{guild_id}/commands/{command_id}");
/*  77 */     public static final Route DELETE_GUILD_COMMAND = new Route(Method.DELETE, "applications/{application_id}/guilds/{guild_id}/commands/{command_id}");
/*     */     
/*  79 */     public static final Route GET_ALL_COMMAND_PERMISSIONS = new Route(Method.GET, "applications/{application_id}/guilds/{guild_id}/commands/permissions");
/*  80 */     public static final Route EDIT_ALL_COMMAND_PERMISSIONS = new Route(Method.PUT, "applications/{application_id}/guilds/{guild_id}/commands/permissions");
/*  81 */     public static final Route GET_COMMAND_PERMISSIONS = new Route(Method.GET, "applications/{application_id}/guilds/{guild_id}/commands/{command_id}/permissions");
/*  82 */     public static final Route EDIT_COMMAND_PERMISSIONS = new Route(Method.PUT, "applications/{application_id}/guilds/{guild_id}/commands/{command_id}/permissions");
/*     */     
/*  84 */     public static final Route CALLBACK = new Route(Method.POST, "interactions/{interaction_id}/{interaction_token}/callback");
/*  85 */     public static final Route CREATE_FOLLOWUP = new Route(Method.POST, "webhooks/{application_id}/{interaction_token}");
/*  86 */     public static final Route EDIT_FOLLOWUP = new Route(Method.PATCH, "webhooks/{application_id}/{interaction_token}/messages/{message_id}");
/*  87 */     public static final Route DELETE_FOLLOWUP = new Route(Method.DELETE, "webhooks/{application_id}/{interaction_token}/messages/{message_id}");
/*  88 */     public static final Route GET_ORIGINAL = new Route(Method.GET, "webhooks/{application_id}/{interaction_token}/messages/@original");
/*     */   }
/*     */   
/*     */   public static class Self
/*     */   {
/*  93 */     public static final Route GET_SELF = new Route(Method.GET, "users/@me");
/*  94 */     public static final Route MODIFY_SELF = new Route(Method.PATCH, "users/@me");
/*  95 */     public static final Route GET_GUILDS = new Route(Method.GET, "users/@me/guilds");
/*  96 */     public static final Route LEAVE_GUILD = new Route(Method.DELETE, "users/@me/guilds/{guild_id}");
/*  97 */     public static final Route GET_PRIVATE_CHANNELS = new Route(Method.GET, "users/@me/channels");
/*  98 */     public static final Route CREATE_PRIVATE_CHANNEL = new Route(Method.POST, "users/@me/channels");
/*     */ 
/*     */     
/* 101 */     public static final Route USER_SETTINGS = new Route(Method.GET, "users/@me/settings");
/* 102 */     public static final Route GET_CONNECTIONS = new Route(Method.GET, "users/@me/connections");
/* 103 */     public static final Route FRIEND_SUGGESTIONS = new Route(Method.GET, "friend-suggestions");
/* 104 */     public static final Route GET_RECENT_MENTIONS = new Route(Method.GET, "users/@me/mentions");
/*     */   }
/*     */   
/*     */   public static class Users
/*     */   {
/* 109 */     public static final Route GET_USER = new Route(Method.GET, "users/{user_id}");
/* 110 */     public static final Route GET_PROFILE = new Route(Method.GET, "users/{user_id}/profile");
/* 111 */     public static final Route GET_NOTE = new Route(Method.GET, "users/@me/notes/{user_id}");
/* 112 */     public static final Route SET_NOTE = new Route(Method.PUT, "users/@me/notes/{user_id}");
/*     */   }
/*     */   
/*     */   public static class Relationships
/*     */   {
/* 117 */     public static final Route GET_RELATIONSHIPS = new Route(Method.GET, "users/@me/relationships");
/* 118 */     public static final Route GET_RELATIONSHIP = new Route(Method.GET, "users/@me/relationships/{user_id}");
/* 119 */     public static final Route ADD_RELATIONSHIP = new Route(Method.PUT, "users/@me/relationships/{user_id}");
/* 120 */     public static final Route DELETE_RELATIONSHIP = new Route(Method.DELETE, "users/@me/relationships/{user_id}");
/*     */   }
/*     */   
/*     */   public static class Guilds
/*     */   {
/* 125 */     public static final Route GET_GUILD = new Route(Method.GET, "guilds/{guild_id}");
/* 126 */     public static final Route MODIFY_GUILD = new Route(Method.PATCH, "guilds/{guild_id}");
/* 127 */     public static final Route GET_VANITY_URL = new Route(Method.GET, "guilds/{guild_id}/vanity-url");
/* 128 */     public static final Route CREATE_CHANNEL = new Route(Method.POST, "guilds/{guild_id}/channels");
/* 129 */     public static final Route GET_CHANNELS = new Route(Method.GET, "guilds/{guild_id}/channels");
/* 130 */     public static final Route MODIFY_CHANNELS = new Route(Method.PATCH, "guilds/{guild_id}/channels");
/* 131 */     public static final Route MODIFY_ROLES = new Route(Method.PATCH, "guilds/{guild_id}/roles");
/* 132 */     public static final Route GET_BANS = new Route(Method.GET, "guilds/{guild_id}/bans");
/* 133 */     public static final Route GET_BAN = new Route(Method.GET, "guilds/{guild_id}/bans/{user_id}");
/* 134 */     public static final Route UNBAN = new Route(Method.DELETE, "guilds/{guild_id}/bans/{user_id}");
/* 135 */     public static final Route BAN = new Route(Method.PUT, "guilds/{guild_id}/bans/{user_id}");
/* 136 */     public static final Route KICK_MEMBER = new Route(Method.DELETE, "guilds/{guild_id}/members/{user_id}");
/* 137 */     public static final Route MODIFY_MEMBER = new Route(Method.PATCH, "guilds/{guild_id}/members/{user_id}");
/* 138 */     public static final Route ADD_MEMBER = new Route(Method.PUT, "guilds/{guild_id}/members/{user_id}");
/* 139 */     public static final Route GET_MEMBER = new Route(Method.GET, "guilds/{guild_id}/members/{user_id}");
/* 140 */     public static final Route MODIFY_SELF = new Route(Method.PATCH, "guilds/{guild_id}/members/@me");
/* 141 */     public static final Route PRUNABLE_COUNT = new Route(Method.GET, "guilds/{guild_id}/prune");
/* 142 */     public static final Route PRUNE_MEMBERS = new Route(Method.POST, "guilds/{guild_id}/prune");
/* 143 */     public static final Route GET_WEBHOOKS = new Route(Method.GET, "guilds/{guild_id}/webhooks");
/* 144 */     public static final Route GET_GUILD_EMBED = new Route(Method.GET, "guilds/{guild_id}/embed");
/* 145 */     public static final Route MODIFY_GUILD_EMBED = new Route(Method.PATCH, "guilds/{guild_id}/embed");
/* 146 */     public static final Route GET_GUILD_EMOTES = new Route(Method.GET, "guilds/{guild_id}/emojis");
/* 147 */     public static final Route GET_AUDIT_LOGS = new Route(Method.GET, "guilds/{guild_id}/audit-logs");
/* 148 */     public static final Route GET_VOICE_REGIONS = new Route(Method.GET, "guilds/{guild_id}/regions");
/* 149 */     public static final Route UPDATE_VOICE_STATE = new Route(Method.PATCH, "guilds/{guild_id}/voice-states/{user_id}");
/*     */     
/* 151 */     public static final Route GET_INTEGRATIONS = new Route(Method.GET, "guilds/{guild_id}/integrations");
/* 152 */     public static final Route CREATE_INTEGRATION = new Route(Method.POST, "guilds/{guild_id}/integrations");
/* 153 */     public static final Route DELETE_INTEGRATION = new Route(Method.DELETE, "guilds/{guild_id}/integrations/{integration_id}");
/* 154 */     public static final Route MODIFY_INTEGRATION = new Route(Method.PATCH, "guilds/{guild_id}/integrations/{integration_id}");
/* 155 */     public static final Route SYNC_INTEGRATION = new Route(Method.POST, "guilds/{guild_id}/integrations/{integration_id}/sync");
/*     */     
/* 157 */     public static final Route ADD_MEMBER_ROLE = new Route(Method.PUT, "guilds/{guild_id}/members/{user_id}/roles/{role_id}");
/* 158 */     public static final Route REMOVE_MEMBER_ROLE = new Route(Method.DELETE, "guilds/{guild_id}/members/{user_id}/roles/{role_id}");
/*     */ 
/*     */ 
/*     */     
/* 162 */     public static final Route CREATE_GUILD = new Route(Method.POST, "guilds");
/* 163 */     public static final Route DELETE_GUILD = new Route(Method.POST, "guilds/{guild_id}/delete");
/* 164 */     public static final Route ACK_GUILD = new Route(Method.POST, "guilds/{guild_id}/ack");
/*     */     
/* 166 */     public static final Route MODIFY_NOTIFICATION_SETTINGS = new Route(Method.PATCH, "users/@me/guilds/{guild_id}/settings");
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Emotes
/*     */   {
/* 172 */     public static final Route MODIFY_EMOTE = new Route(Method.PATCH, "guilds/{guild_id}/emojis/{emote_id}");
/* 173 */     public static final Route DELETE_EMOTE = new Route(Method.DELETE, "guilds/{guild_id}/emojis/{emote_id}");
/* 174 */     public static final Route CREATE_EMOTE = new Route(Method.POST, "guilds/{guild_id}/emojis");
/*     */     
/* 176 */     public static final Route GET_EMOTES = new Route(Method.GET, "guilds/{guild_id}/emojis");
/* 177 */     public static final Route GET_EMOTE = new Route(Method.GET, "guilds/{guild_id}/emojis/{emoji_id}");
/*     */   }
/*     */   
/*     */   public static class Webhooks
/*     */   {
/* 182 */     public static final Route GET_WEBHOOK = new Route(Method.GET, "webhooks/{webhook_id}");
/* 183 */     public static final Route GET_TOKEN_WEBHOOK = new Route(Method.GET, "webhooks/{webhook_id}/{token}");
/* 184 */     public static final Route DELETE_WEBHOOK = new Route(Method.DELETE, "webhooks/{webhook_id}");
/* 185 */     public static final Route DELETE_TOKEN_WEBHOOK = new Route(Method.DELETE, "webhooks/{webhook_id}/{token}");
/* 186 */     public static final Route MODIFY_WEBHOOK = new Route(Method.PATCH, "webhooks/{webhook_id}");
/* 187 */     public static final Route MODIFY_TOKEN_WEBHOOK = new Route(Method.PATCH, "webhooks/{webhook_id}/{token}");
/*     */ 
/*     */     
/* 190 */     public static final Route EXECUTE_WEBHOOK = new Route(Method.POST, "webhooks/{webhook_id}/{token}");
/* 191 */     public static final Route EXECUTE_WEBHOOK_EDIT = new Route(Method.PATCH, "webhooks/{webhook_id}/{token}/messages/{message_id}");
/* 192 */     public static final Route EXECUTE_WEBHOOK_DELETE = new Route(Method.DELETE, "webhooks/{webhook_id}/{token}/messages/{message_id}");
/* 193 */     public static final Route EXECUTE_WEBHOOK_SLACK = new Route(Method.POST, "webhooks/{webhook_id}/{token}/slack");
/* 194 */     public static final Route EXECUTE_WEBHOOK_GITHUB = new Route(Method.POST, "webhooks/{webhook_id}/{token}/github");
/*     */   }
/*     */   
/*     */   public static class Roles
/*     */   {
/* 199 */     public static final Route GET_ROLES = new Route(Method.GET, "guilds/{guild_id}/roles");
/* 200 */     public static final Route CREATE_ROLE = new Route(Method.POST, "guilds/{guild_id}/roles");
/* 201 */     public static final Route GET_ROLE = new Route(Method.GET, "guilds/{guild_id}/roles/{role_id}");
/* 202 */     public static final Route MODIFY_ROLE = new Route(Method.PATCH, "guilds/{guild_id}/roles/{role_id}");
/* 203 */     public static final Route DELETE_ROLE = new Route(Method.DELETE, "guilds/{guild_id}/roles/{role_id}");
/*     */   }
/*     */   
/*     */   public static class Channels
/*     */   {
/* 208 */     public static final Route DELETE_CHANNEL = new Route(Method.DELETE, "channels/{channel_id}");
/* 209 */     public static final Route MODIFY_CHANNEL = new Route(Method.PATCH, "channels/{channel_id}");
/* 210 */     public static final Route GET_WEBHOOKS = new Route(Method.GET, "channels/{channel_id}/webhooks");
/* 211 */     public static final Route CREATE_WEBHOOK = new Route(Method.POST, "channels/{channel_id}/webhooks");
/* 212 */     public static final Route CREATE_PERM_OVERRIDE = new Route(Method.PUT, "channels/{channel_id}/permissions/{permoverride_id}");
/* 213 */     public static final Route MODIFY_PERM_OVERRIDE = new Route(Method.PUT, "channels/{channel_id}/permissions/{permoverride_id}");
/* 214 */     public static final Route DELETE_PERM_OVERRIDE = new Route(Method.DELETE, "channels/{channel_id}/permissions/{permoverride_id}");
/*     */     
/* 216 */     public static final Route SEND_TYPING = new Route(Method.POST, "channels/{channel_id}/typing");
/* 217 */     public static final Route GET_PERMISSIONS = new Route(Method.GET, "channels/{channel_id}/permissions");
/* 218 */     public static final Route GET_PERM_OVERRIDE = new Route(Method.GET, "channels/{channel_id}/permissions/{permoverride_id}");
/* 219 */     public static final Route FOLLOW_CHANNEL = new Route(Method.POST, "channels/{channel_id}/followers");
/*     */ 
/*     */     
/* 222 */     public static final Route GET_RECIPIENTS = new Route(Method.GET, "channels/{channel_id}/recipients");
/* 223 */     public static final Route GET_RECIPIENT = new Route(Method.GET, "channels/{channel_id}/recipients/{user_id}");
/* 224 */     public static final Route ADD_RECIPIENT = new Route(Method.PUT, "channels/{channel_id}/recipients/{user_id}");
/* 225 */     public static final Route REMOVE_RECIPIENT = new Route(Method.DELETE, "channels/{channel_id}/recipients/{user_id}");
/* 226 */     public static final Route START_CALL = new Route(Method.POST, "channels/{channel_id}/call/ring");
/* 227 */     public static final Route STOP_CALL = new Route(Method.POST, "channels/{channel_id}/call/stop_ringing");
/*     */   }
/*     */   
/*     */   public static class StageInstances
/*     */   {
/* 232 */     public static final Route GET_INSTANCE = new Route(Method.GET, "stage-instances/{channel_id}");
/* 233 */     public static final Route DELETE_INSTANCE = new Route(Method.DELETE, "stage-instances/{channel_id}");
/* 234 */     public static final Route UPDATE_INSTANCE = new Route(Method.PATCH, "stage-instances/{channel_id}");
/* 235 */     public static final Route CREATE_INSTANCE = new Route(Method.POST, "stage-instances");
/*     */   }
/*     */   
/*     */   public static class Messages
/*     */   {
/* 240 */     public static final Route EDIT_MESSAGE = new Route(Method.PATCH, "channels/{channel_id}/messages/{message_id}");
/* 241 */     public static final Route SEND_MESSAGE = new Route(Method.POST, "channels/{channel_id}/messages");
/* 242 */     public static final Route GET_PINNED_MESSAGES = new Route(Method.GET, "channels/{channel_id}/pins");
/* 243 */     public static final Route ADD_PINNED_MESSAGE = new Route(Method.PUT, "channels/{channel_id}/pins/{message_id}");
/* 244 */     public static final Route REMOVE_PINNED_MESSAGE = new Route(Method.DELETE, "channels/{channel_id}/pins/{message_id}");
/*     */     
/* 246 */     public static final Route ADD_REACTION = new Route(Method.PUT, "channels/{channel_id}/messages/{message_id}/reactions/{reaction_code}/{user_id}");
/* 247 */     public static final Route REMOVE_REACTION = new Route(Method.DELETE, "channels/{channel_id}/messages/{message_id}/reactions/{reaction_code}/{user_id}");
/* 248 */     public static final Route REMOVE_ALL_REACTIONS = new Route(Method.DELETE, "channels/{channel_id}/messages/{message_id}/reactions");
/* 249 */     public static final Route GET_REACTION_USERS = new Route(Method.GET, "channels/{channel_id}/messages/{message_id}/reactions/{reaction_code}");
/* 250 */     public static final Route CLEAR_EMOTE_REACTIONS = new Route(Method.DELETE, "channels/{channel_id}/messages/{message_id}/reactions/{reaction_code}");
/*     */     
/* 252 */     public static final Route DELETE_MESSAGE = new Route(Method.DELETE, "channels/{channel_id}/messages/{message_id}");
/* 253 */     public static final Route GET_MESSAGE_HISTORY = new Route(Method.GET, "channels/{channel_id}/messages");
/* 254 */     public static final Route CROSSPOST_MESSAGE = new Route(Method.POST, "channels/{channel_id}/messages/{message_id}/crosspost");
/*     */ 
/*     */     
/* 257 */     public static final Route GET_MESSAGE = new Route(Method.GET, "channels/{channel_id}/messages/{message_id}");
/* 258 */     public static final Route DELETE_MESSAGES = new Route(Method.POST, "channels/{channel_id}/messages/bulk-delete");
/*     */ 
/*     */     
/* 261 */     public static final Route ACK_MESSAGE = new Route(Method.POST, "channels/{channel_id}/messages/{message_id}/ack");
/*     */   }
/*     */   
/*     */   public static class Invites
/*     */   {
/* 266 */     public static final Route GET_INVITE = new Route(Method.GET, "invites/{code}");
/* 267 */     public static final Route GET_GUILD_INVITES = new Route(Method.GET, "guilds/{guild_id}/invites");
/* 268 */     public static final Route GET_CHANNEL_INVITES = new Route(Method.GET, "channels/{channel_id}/invites");
/* 269 */     public static final Route CREATE_INVITE = new Route(Method.POST, "channels/{channel_id}/invites");
/* 270 */     public static final Route DELETE_INVITE = new Route(Method.DELETE, "invites/{code}");
/*     */   }
/*     */   
/*     */   public static class Templates
/*     */   {
/* 275 */     public static final Route GET_TEMPLATE = new Route(Method.GET, "guilds/templates/{code}");
/* 276 */     public static final Route SYNC_TEMPLATE = new Route(Method.PUT, "guilds/{guild_id}/templates/{code}");
/* 277 */     public static final Route CREATE_TEMPLATE = new Route(Method.POST, "guilds/{guild_id}/templates");
/* 278 */     public static final Route MODIFY_TEMPLATE = new Route(Method.PATCH, "guilds/{guild_id}/templates/{code}");
/* 279 */     public static final Route DELETE_TEMPLATE = new Route(Method.DELETE, "guilds/{guild_id}/templates/{code}");
/* 280 */     public static final Route GET_GUILD_TEMPLATES = new Route(Method.GET, "guilds/{guild_id}/templates");
/* 281 */     public static final Route CREATE_GUILD_FROM_TEMPLATE = new Route(Method.POST, "guilds/templates/{code}");
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Route custom(@Nonnull Method method, @Nonnull String route) {
/* 287 */     Checks.notNull(method, "Method");
/* 288 */     Checks.notEmpty(route, "Route");
/* 289 */     Checks.noWhitespace(route, "Route");
/* 290 */     return new Route(method, route);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Route delete(@Nonnull String route) {
/* 296 */     return custom(Method.DELETE, route);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Route post(@Nonnull String route) {
/* 302 */     return custom(Method.POST, route);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Route put(@Nonnull String route) {
/* 308 */     return custom(Method.PUT, route);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Route patch(@Nonnull String route) {
/* 314 */     return custom(Method.PATCH, route);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Route get(@Nonnull String route) {
/* 320 */     return custom(Method.GET, route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Route(Method method, String route) {
/* 330 */     this.method = method;
/* 331 */     this.route = route;
/* 332 */     this.paramCount = Helpers.countMatches(route, '{');
/*     */     
/* 334 */     if (this.paramCount != Helpers.countMatches(route, '}')) {
/* 335 */       throw new IllegalArgumentException("An argument does not have both {}'s for route: " + method + "  " + route);
/*     */     }
/*     */   }
/*     */   
/*     */   public Method getMethod() {
/* 340 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRoute() {
/* 345 */     return this.route;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParamCount() {
/* 350 */     return this.paramCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompiledRoute compile(String... params) {
/* 355 */     if (params.length != this.paramCount)
/*     */     {
/* 357 */       throw new IllegalArgumentException("Error Compiling Route: [" + this.route + "], incorrect amount of parameters provided.Expected: " + this.paramCount + ", Provided: " + params.length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 362 */     Set<String> major = new HashSet<>();
/* 363 */     StringBuilder compiledRoute = new StringBuilder(this.route);
/* 364 */     for (int i = 0; i < this.paramCount; i++) {
/*     */       
/* 366 */       int paramStart = compiledRoute.indexOf("{");
/* 367 */       int paramEnd = compiledRoute.indexOf("}");
/* 368 */       String paramName = compiledRoute.substring(paramStart + 1, paramEnd);
/* 369 */       if ("guild_id:channel_id:webhook_id:interaction_token".contains(paramName))
/*     */       {
/* 371 */         if (params[i].length() > 30) {
/* 372 */           major.add(paramName + "=" + Integer.toUnsignedString(params[i].hashCode()));
/*     */         } else {
/* 374 */           major.add(paramName + "=" + params[i]);
/*     */         } 
/*     */       }
/* 377 */       compiledRoute.replace(paramStart, paramEnd + 1, params[i]);
/*     */     } 
/*     */     
/* 380 */     return new CompiledRoute(this, compiledRoute.toString(), major.isEmpty() ? "n/a" : String.join(":", (Iterable)major));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 386 */     return (this.route + this.method.toString()).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 392 */     if (!(o instanceof Route)) {
/* 393 */       return false;
/*     */     }
/* 395 */     Route oRoute = (Route)o;
/* 396 */     return (this.method.equals(oRoute.method) && this.route.equals(oRoute.route));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 402 */     return this.method + "/" + this.route;
/*     */   }
/*     */ 
/*     */   
/*     */   public class CompiledRoute
/*     */   {
/*     */     private final Route baseRoute;
/*     */     private final String major;
/*     */     private final String compiledRoute;
/*     */     private final boolean hasQueryParams;
/*     */     
/*     */     private CompiledRoute(Route baseRoute, String compiledRoute, String major, boolean hasQueryParams) {
/* 414 */       this.baseRoute = baseRoute;
/* 415 */       this.compiledRoute = compiledRoute;
/* 416 */       this.major = major;
/* 417 */       this.hasQueryParams = hasQueryParams;
/*     */     }
/*     */ 
/*     */     
/*     */     private CompiledRoute(Route baseRoute, String compiledRoute, String major) {
/* 422 */       this(baseRoute, compiledRoute, major, false);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     @CheckReturnValue
/*     */     public CompiledRoute withQueryParams(String... params) {
/* 429 */       Checks.check((params.length >= 2), "params length must be at least 2");
/* 430 */       Checks.check((params.length % 2 == 0), "params length must be a multiple of 2");
/*     */       
/* 432 */       StringBuilder newRoute = new StringBuilder(this.compiledRoute);
/*     */       
/* 434 */       for (int i = 0; i < params.length; i++) {
/* 435 */         newRoute.append((!this.hasQueryParams && i == 0) ? 63 : 38).append(params[i]).append('=').append(params[++i]);
/*     */       }
/* 437 */       return new CompiledRoute(this.baseRoute, newRoute.toString(), this.major, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMajorParameters() {
/* 442 */       return this.major;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getCompiledRoute() {
/* 447 */       return this.compiledRoute;
/*     */     }
/*     */ 
/*     */     
/*     */     public Route getBaseRoute() {
/* 452 */       return this.baseRoute;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getMethod() {
/* 457 */       return this.baseRoute.method;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 463 */       return (this.compiledRoute + Route.this.method.toString()).hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 469 */       if (!(o instanceof CompiledRoute)) {
/* 470 */         return false;
/*     */       }
/* 472 */       CompiledRoute oCompiled = (CompiledRoute)o;
/*     */       
/* 474 */       return (this.baseRoute.equals(oCompiled.getBaseRoute()) && this.compiledRoute.equals(oCompiled.compiledRoute));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 480 */       return "CompiledRoute(" + Route.this.method + ": " + this.compiledRoute + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\Route.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */