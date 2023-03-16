/*     */ package net.dv8tion.jda.api.interactions.components;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Emoji;
/*     */ import net.dv8tion.jda.internal.interactions.ButtonImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Button
/*     */   extends Component
/*     */ {
/*     */   public static final int LABEL_MAX_LENGTH = 80;
/*     */   public static final int ID_MAX_LENGTH = 100;
/*     */   public static final int URL_MAX_LENGTH = 512;
/*     */   
/*     */   @Nonnull
/*     */   String getLabel();
/*     */   
/*     */   @Nonnull
/*     */   ButtonStyle getStyle();
/*     */   
/*     */   @Nullable
/*     */   String getUrl();
/*     */   
/*     */   @Nullable
/*     */   Emoji getEmoji();
/*     */   
/*     */   boolean isDisabled();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default Button asDisabled() {
/* 139 */     return (Button)new ButtonImpl(getId(), getLabel(), getStyle(), getUrl(), true, getEmoji());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default Button asEnabled() {
/* 151 */     return (Button)new ButtonImpl(getId(), getLabel(), getStyle(), getUrl(), false, getEmoji());
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default Button withDisabled(boolean disabled) {
/* 166 */     return (Button)new ButtonImpl(getId(), getLabel(), getStyle(), getUrl(), disabled, getEmoji());
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default Button withEmoji(@Nullable Emoji emoji) {
/* 181 */     return (Button)new ButtonImpl(getId(), getLabel(), getStyle(), getUrl(), isDisabled(), emoji);
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
/*     */   default Button withLabel(@Nonnull String label) {
/* 203 */     Checks.notEmpty(label, "Label");
/* 204 */     Checks.notLonger(label, 80, "Label");
/* 205 */     return (Button)new ButtonImpl(getId(), label, getStyle(), getUrl(), isDisabled(), getEmoji());
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
/*     */   default Button withId(@Nonnull String id) {
/* 227 */     Checks.notEmpty(id, "ID");
/* 228 */     Checks.notLonger(id, 100, "ID");
/* 229 */     return (Button)new ButtonImpl(id, getLabel(), getStyle(), null, isDisabled(), getEmoji());
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
/*     */   default Button withUrl(@Nonnull String url) {
/* 251 */     Checks.notEmpty(url, "URL");
/* 252 */     Checks.notLonger(url, 512, "URL");
/* 253 */     return (Button)new ButtonImpl(null, getLabel(), ButtonStyle.LINK, url, isDisabled(), getEmoji());
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
/*     */   @CheckReturnValue
/*     */   default Button withStyle(@Nonnull ButtonStyle style) {
/* 276 */     Checks.notNull(style, "Style");
/* 277 */     Checks.check((style != ButtonStyle.UNKNOWN), "Cannot make button with unknown style!");
/* 278 */     if (getStyle() == ButtonStyle.LINK && style != ButtonStyle.LINK)
/* 279 */       throw new IllegalArgumentException("You cannot change a link button to another style!"); 
/* 280 */     if (getStyle() != ButtonStyle.LINK && style == ButtonStyle.LINK)
/* 281 */       throw new IllegalArgumentException("You cannot change a styled button to a link button!"); 
/* 282 */     return (Button)new ButtonImpl(getId(), getLabel(), style, getUrl(), isDisabled(), getEmoji());
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
/*     */   @Nonnull
/*     */   static Button primary(@Nonnull String id, @Nonnull String label) {
/* 309 */     Checks.notEmpty(id, "Id");
/* 310 */     Checks.notEmpty(label, "Label");
/* 311 */     Checks.notLonger(id, 100, "Id");
/* 312 */     Checks.notLonger(label, 80, "Label");
/* 313 */     return (Button)new ButtonImpl(id, label, ButtonStyle.PRIMARY, false, null);
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
/*     */   @Nonnull
/*     */   static Button primary(@Nonnull String id, @Nonnull Emoji emoji) {
/* 340 */     Checks.notEmpty(id, "Id");
/* 341 */     Checks.notNull(emoji, "Emoji");
/* 342 */     Checks.notLonger(id, 100, "Id");
/* 343 */     return (Button)new ButtonImpl(id, "", ButtonStyle.PRIMARY, false, emoji);
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
/*     */   @Nonnull
/*     */   static Button secondary(@Nonnull String id, @Nonnull String label) {
/* 370 */     Checks.notEmpty(id, "Id");
/* 371 */     Checks.notEmpty(label, "Label");
/* 372 */     Checks.notLonger(id, 100, "Id");
/* 373 */     Checks.notLonger(label, 80, "Label");
/* 374 */     return (Button)new ButtonImpl(id, label, ButtonStyle.SECONDARY, false, null);
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
/*     */   @Nonnull
/*     */   static Button secondary(@Nonnull String id, @Nonnull Emoji emoji) {
/* 401 */     Checks.notEmpty(id, "Id");
/* 402 */     Checks.notNull(emoji, "Emoji");
/* 403 */     Checks.notLonger(id, 100, "Id");
/* 404 */     return (Button)new ButtonImpl(id, "", ButtonStyle.SECONDARY, false, emoji);
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
/*     */   @Nonnull
/*     */   static Button success(@Nonnull String id, @Nonnull String label) {
/* 431 */     Checks.notEmpty(id, "Id");
/* 432 */     Checks.notEmpty(label, "Label");
/* 433 */     Checks.notLonger(id, 100, "Id");
/* 434 */     Checks.notLonger(label, 80, "Label");
/* 435 */     return (Button)new ButtonImpl(id, label, ButtonStyle.SUCCESS, false, null);
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
/*     */   @Nonnull
/*     */   static Button success(@Nonnull String id, @Nonnull Emoji emoji) {
/* 462 */     Checks.notEmpty(id, "Id");
/* 463 */     Checks.notNull(emoji, "Emoji");
/* 464 */     Checks.notLonger(id, 100, "Id");
/* 465 */     return (Button)new ButtonImpl(id, "", ButtonStyle.SUCCESS, false, emoji);
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
/*     */   @Nonnull
/*     */   static Button danger(@Nonnull String id, @Nonnull String label) {
/* 492 */     Checks.notEmpty(id, "Id");
/* 493 */     Checks.notEmpty(label, "Label");
/* 494 */     Checks.notLonger(id, 100, "Id");
/* 495 */     Checks.notLonger(label, 80, "Label");
/* 496 */     return (Button)new ButtonImpl(id, label, ButtonStyle.DANGER, false, null);
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
/*     */   @Nonnull
/*     */   static Button danger(@Nonnull String id, @Nonnull Emoji emoji) {
/* 523 */     Checks.notEmpty(id, "Id");
/* 524 */     Checks.notNull(emoji, "Emoji");
/* 525 */     Checks.notLonger(id, 100, "Id");
/* 526 */     return (Button)new ButtonImpl(id, "", ButtonStyle.DANGER, false, emoji);
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
/*     */   @Nonnull
/*     */   static Button link(@Nonnull String url, @Nonnull String label) {
/* 556 */     Checks.notEmpty(url, "URL");
/* 557 */     Checks.notEmpty(label, "Label");
/* 558 */     Checks.notLonger(url, 512, "URL");
/* 559 */     Checks.notLonger(label, 80, "Label");
/* 560 */     return (Button)new ButtonImpl(null, label, ButtonStyle.LINK, url, false, null);
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
/*     */   @Nonnull
/*     */   static Button link(@Nonnull String url, @Nonnull Emoji emoji) {
/* 590 */     Checks.notEmpty(url, "URL");
/* 591 */     Checks.notNull(emoji, "Emoji");
/* 592 */     Checks.notLonger(url, 512, "URL");
/* 593 */     return (Button)new ButtonImpl(null, "", ButtonStyle.LINK, url, false, emoji);
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
/*     */   @Nonnull
/*     */   static Button of(@Nonnull ButtonStyle style, @Nonnull String idOrUrl, @Nonnull String label) {
/* 624 */     Checks.check((style != ButtonStyle.UNKNOWN), "Cannot make button with unknown style!");
/* 625 */     Checks.notNull(style, "Style");
/* 626 */     Checks.notNull(label, "Label");
/* 627 */     Checks.notLonger(label, 80, "Label");
/* 628 */     if (style == ButtonStyle.LINK)
/* 629 */       return link(idOrUrl, label); 
/* 630 */     Checks.notEmpty(idOrUrl, "Id");
/* 631 */     Checks.notLonger(idOrUrl, 100, "Id");
/* 632 */     return (Button)new ButtonImpl(idOrUrl, label, style, false, null);
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
/*     */   @Nonnull
/*     */   static Button of(@Nonnull ButtonStyle style, @Nonnull String idOrUrl, @Nonnull Emoji emoji) {
/* 661 */     Checks.check((style != ButtonStyle.UNKNOWN), "Cannot make button with unknown style!");
/* 662 */     Checks.notNull(style, "Style");
/* 663 */     Checks.notNull(emoji, "Emoji");
/* 664 */     if (style == ButtonStyle.LINK)
/* 665 */       return link(idOrUrl, emoji); 
/* 666 */     Checks.notEmpty(idOrUrl, "Id");
/* 667 */     Checks.notLonger(idOrUrl, 100, "Id");
/* 668 */     return (Button)new ButtonImpl(idOrUrl, "", style, false, emoji);
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
/*     */   @Nonnull
/*     */   static Button of(@Nonnull ButtonStyle style, @Nonnull String idOrUrl, @Nullable String label, @Nullable Emoji emoji) {
/* 702 */     if (label != null)
/* 703 */       return of(style, idOrUrl, label).withEmoji(emoji); 
/* 704 */     if (emoji != null)
/* 705 */       return of(style, idOrUrl, emoji); 
/* 706 */     throw new IllegalArgumentException("Cannot build a button without a label and emoji. At least one has to be provided as non-null.");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\Button.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */