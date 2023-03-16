/*     */ package net.dv8tion.jda.api.requests;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.exceptions.ErrorResponseException;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
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
/*     */ public enum ErrorResponse
/*     */ {
/*  40 */   UNKNOWN_ACCOUNT(10001, "Unknown Account"),
/*  41 */   UNKNOWN_APPLICATION(10002, "Unknown Application"),
/*  42 */   UNKNOWN_CHANNEL(10003, "Unknown Channel"),
/*  43 */   UNKNOWN_GUILD(10004, "Unknown Guild"),
/*  44 */   UNKNOWN_INTEGRATION(10005, "Unknown Integration"),
/*  45 */   UNKNOWN_INVITE(10006, "Unknown Invite"),
/*  46 */   UNKNOWN_MEMBER(10007, "Unknown Member"),
/*  47 */   UNKNOWN_MESSAGE(10008, "Unknown Message"),
/*  48 */   UNKNOWN_OVERRIDE(10009, "Unknown Override"),
/*  49 */   UNKNOWN_PROVIDER(10010, "Unknown Provider"),
/*  50 */   UNKNOWN_ROLE(10011, "Unknown Role"),
/*  51 */   UNKNOWN_TOKEN(10012, "Unknown Token"),
/*  52 */   UNKNOWN_USER(10013, "Unknown User"),
/*  53 */   UNKNOWN_EMOJI(10014, "Unknown Emoji"),
/*  54 */   UNKNOWN_WEBHOOK(10015, "Unknown Webhook"),
/*  55 */   UNKNOWN_BAN(10026, "Unknown Ban"),
/*  56 */   UNKNOWN_SKU(10027, "Unknown SKU"),
/*  57 */   UNKNOWN_STORE_LISTING(10028, "Unknown Store Listing"),
/*  58 */   UNKNOWN_ENTITLEMENT(10029, "Unknown Entitlement"),
/*  59 */   UNKNOWN_BUILD(10030, "Unknown Build"),
/*  60 */   UNKNOWN_LOBBY(10031, "Unknown Lobby"),
/*  61 */   UNKNOWN_BRANCH(10032, "Unknown Branch"),
/*  62 */   UNKNOWN_REDISTRIBUTABLE(10036, "Unknown Redistributable"),
/*  63 */   UNKNOWN_GUILD_TEMPLATE(10057, "Unknown Guild Template"),
/*  64 */   UNKNOWN_INTERACTION(10062, "Unknown Interaction"),
/*  65 */   UNKNOWN_COMMAND(10063, "Unknown application command"),
/*  66 */   UNKNOWN_COMMAND_PERMISSIONS(10066, "Unknown application command permissions"),
/*  67 */   UNKNOWN_STAGE_INSTANCE(10067, "Unknown Stage Instance"),
/*  68 */   BOTS_NOT_ALLOWED(20001, "Bots cannot use this endpoint"),
/*  69 */   ONLY_BOTS_ALLOWED(20002, "Only bots can use this endpoint"),
/*  70 */   MAX_GUILDS(30001, "Maximum number of Guilds reached (100)"),
/*  71 */   MAX_FRIENDS(30002, "Maximum number of Friends reached (1000)"),
/*  72 */   MAX_MESSAGE_PINS(30003, "Maximum number of pinned messages reached (50)"),
/*  73 */   MAX_USERS_PER_DM(30004, "Maximum number of recipients reached. (10)"),
/*  74 */   MAX_ROLES_PER_GUILD(30005, "Maximum number of guild roles reached (250)"),
/*  75 */   MAX_WEBHOOKS(30007, "Maximum number of webhooks reached (10)"),
/*  76 */   TOO_MANY_REACTIONS(30010, "Maximum number of reactions reached (20)"),
/*  77 */   MAX_CHANNELS(30013, "Maximum number of guild channels reached (500)"),
/*  78 */   MAX_INVITES(30016, "Maximum number of invites reached (1000)"),
/*  79 */   ALREADY_HAS_TEMPLATE(30031, "Guild already has a template"),
/*  80 */   UNAUTHORIZED(40001, "Unauthorized"),
/*  81 */   REQUEST_ENTITY_TOO_LARGE(40005, "Request entity too large"),
/*  82 */   USER_NOT_CONNECTED(40032, "Target user is not connected to voice."),
/*  83 */   ALREADY_CROSSPOSTED(40033, "This message has already been crossposted."),
/*  84 */   MISSING_ACCESS(50001, "Missing Access"),
/*  85 */   INVALID_ACCOUNT_TYPE(50002, "Invalid Account Type"),
/*  86 */   INVALID_DM_ACTION(50003, "Cannot execute action on a DM channel"),
/*  87 */   EMBED_DISABLED(50004, "Widget Disabled"),
/*  88 */   INVALID_AUTHOR_EDIT(50005, "Cannot edit a message authored by another user"),
/*  89 */   EMPTY_MESSAGE(50006, "Cannot send an empty message"),
/*  90 */   CANNOT_SEND_TO_USER(50007, "Cannot send messages to this user"),
/*  91 */   CANNOT_MESSAGE_VC(50008, "Cannot send messages in a voice channel"),
/*  92 */   VERIFICATION_ERROR(50009, "Channel verification level is too high"),
/*  93 */   OAUTH_NOT_BOT(50010, "OAuth2 application does not have a bot"),
/*  94 */   MAX_OAUTH_APPS(50011, "OAuth2 application limit reached"),
/*  95 */   INVALID_OAUTH_STATE(50012, "Invalid OAuth state"),
/*  96 */   MISSING_PERMISSIONS(50013, "Missing Permissions"),
/*  97 */   INVALID_TOKEN(50014, "Invalid Authentication Token"),
/*  98 */   NOTE_TOO_LONG(50015, "Note is too long"),
/*  99 */   INVALID_BULK_DELETE(50016, "Provided too few or too many messages to delete. Must provided at least 2 and fewer than 100 messages to delete"),
/* 100 */   INVALID_MFA_LEVEL(50017, "Provided MFA level was invalid."),
/* 101 */   INVALID_PASSWORD(50018, "Provided password was invalid"),
/* 102 */   INVALID_PIN(50019, "A message can only be pinned to the channel it was sent in"),
/* 103 */   INVITE_CODE_INVALID(50020, "Invite code is either invalid or taken"),
/* 104 */   INVALID_MESSAGE_TARGET(50021, "Cannot execute action on a system message"),
/* 105 */   INVALID_OAUTH_ACCESS_TOKEN(50025, "Invalid OAuth2 access token"),
/* 106 */   INVALID_WEBHOOK_TOKEN(50027, "Invalid Webhook Token"),
/* 107 */   INVALID_BULK_DELETE_MESSAGE_AGE(50034, "A Message provided to bulk_delete was older than 2 weeks"),
/* 108 */   INVALID_FORM_BODY(50035, "Invalid Form Body"),
/* 109 */   INVITE_FOR_UNKNOWN_GUILD(50036, "An invite was accepted to a guild the application's bot is not in"),
/* 110 */   INVALID_API_VERSION(50041, "Invalid API version"),
/* 111 */   MFA_NOT_ENABLED(60003, "MFA auth required but not enabled"),
/* 112 */   REACTION_BLOCKED(90001, "Reaction Blocked"),
/* 113 */   RESOURCES_OVERLOADED(130000, "Resource overloaded"),
/* 114 */   STAGE_ALREADY_OPEN(150006, "The Stage is already open"),
/*     */   
/* 116 */   SERVER_ERROR(0, "Discord encountered an internal server error! Not good!");
/*     */   
/*     */   private final int code;
/*     */   
/*     */   private final String meaning;
/*     */ 
/*     */   
/*     */   ErrorResponse(int code, String meaning) {
/* 124 */     this.code = code;
/* 125 */     this.meaning = meaning;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCode() {
/* 130 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getMeaning() {
/* 136 */     return this.meaning;
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
/*     */   public boolean test(Throwable throwable) {
/* 150 */     return (throwable instanceof ErrorResponseException && ((ErrorResponseException)throwable).getErrorResponse() == this);
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
/*     */   public static Predicate<Throwable> test(@Nonnull ErrorResponse... responses) {
/* 165 */     Checks.noneNull((Object[])responses, "ErrorResponse");
/* 166 */     EnumSet<ErrorResponse> set = EnumSet.noneOf(ErrorResponse.class);
/* 167 */     Collections.addAll(set, responses);
/* 168 */     return test(set);
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
/*     */   public static Predicate<Throwable> test(@Nonnull Collection<ErrorResponse> responses) {
/* 183 */     Checks.noneNull(responses, "ErrorResponse");
/* 184 */     EnumSet<ErrorResponse> set = EnumSet.copyOf(responses);
/* 185 */     return error -> (error instanceof ErrorResponseException && set.contains(((ErrorResponseException)error).getErrorResponse()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static ErrorResponse fromCode(int code) {
/* 192 */     for (ErrorResponse error : values()) {
/*     */       
/* 194 */       if (code == error.getCode())
/* 195 */         return error; 
/*     */     } 
/* 197 */     return SERVER_ERROR;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static ErrorResponse fromJSON(@Nullable DataObject obj) {
/* 203 */     if (obj == null || obj.isNull("code"))
/* 204 */       return SERVER_ERROR; 
/* 205 */     return fromCode(obj.getInt("code"));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\ErrorResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */