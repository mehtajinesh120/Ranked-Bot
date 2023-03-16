/*     */ package net.dv8tion.jda.internal.requests.restaction.order;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import okhttp3.RequestBody;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoleOrderActionImpl
/*     */   extends OrderActionImpl<Role, RoleOrderAction>
/*     */   implements RoleOrderAction
/*     */ {
/*     */   protected final Guild guild;
/*     */   
/*     */   public RoleOrderActionImpl(Guild guild, boolean useAscendingOrder) {
/*  58 */     super(guild.getJDA(), !useAscendingOrder, Route.Guilds.MODIFY_ROLES.compile(new String[] { guild.getId() }));
/*  59 */     this.guild = guild;
/*     */     
/*  61 */     List<Role> roles = guild.getRoles();
/*  62 */     roles = roles.subList(0, roles.size() - 1);
/*     */     
/*  64 */     if (useAscendingOrder) {
/*     */ 
/*     */ 
/*     */       
/*  68 */       for (int i = roles.size() - 1; i >= 0; i--) {
/*  69 */         this.orderList.add(roles.get(i));
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  76 */       this.orderList.addAll(roles);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  85 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/*  91 */     Member self = this.guild.getSelfMember();
/*  92 */     boolean isOwner = self.isOwner();
/*     */     
/*  94 */     if (!isOwner) {
/*     */       
/*  96 */       if (self.getRoles().isEmpty())
/*  97 */         throw new IllegalStateException("Cannot move roles above your highest role unless you are the guild owner"); 
/*  98 */       if (!self.hasPermission(new Permission[] { Permission.MANAGE_ROLES })) {
/*  99 */         throw new InsufficientPermissionException(this.guild, Permission.MANAGE_ROLES);
/*     */       }
/*     */     } 
/* 102 */     DataArray array = DataArray.empty();
/* 103 */     List<Role> ordering = new ArrayList<>(this.orderList);
/*     */ 
/*     */ 
/*     */     
/* 107 */     if (this.ascendingOrder) {
/* 108 */       Collections.reverse(ordering);
/*     */     }
/* 110 */     for (int i = 0; i < ordering.size(); i++) {
/*     */       
/* 112 */       Role role = ordering.get(i);
/* 113 */       int initialPos = role.getPosition();
/* 114 */       if (initialPos != i && !isOwner && !self.canInteract(role))
/*     */       {
/* 116 */         throw new IllegalStateException("Cannot change order: One of the roles could not be moved due to hierarchical power!");
/*     */       }
/* 118 */       array.add(DataObject.empty()
/* 119 */           .put("id", role.getId())
/* 120 */           .put("position", Integer.valueOf(i + 1)));
/*     */     } 
/*     */     
/* 123 */     return getRequestBody(array);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateInput(Role entity) {
/* 129 */     Checks.check(entity.getGuild().equals(this.guild), "Provided selected role is not from this Guild!");
/* 130 */     Checks.check(this.orderList.contains(entity), "Provided role is not in the list of orderable roles!");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\order\RoleOrderActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */