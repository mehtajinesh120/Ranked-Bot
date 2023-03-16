/*     */ package net.dv8tion.jda.api.hooks;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.utils.ClassWalker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotatedEventManager
/*     */   implements IEventManager
/*     */ {
/*  54 */   private final Set<Object> listeners = ConcurrentHashMap.newKeySet();
/*  55 */   private final Map<Class<?>, Map<Object, List<Method>>> methods = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(@Nonnull Object listener) {
/*  60 */     if (this.listeners.add(listener))
/*     */     {
/*  62 */       updateMethods();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@Nonnull Object listener) {
/*  69 */     if (this.listeners.remove(listener))
/*     */     {
/*  71 */       updateMethods();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Object> getRegisteredListeners() {
/*  79 */     return Collections.unmodifiableList(new ArrayList(this.listeners));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(@Nonnull GenericEvent event) {
/*  85 */     for (Iterator<Class<?>> iterator = ClassWalker.walk(event.getClass()).iterator(); iterator.hasNext(); ) { Class<?> eventClass = iterator.next();
/*     */       
/*  87 */       Map<Object, List<Method>> listeners = this.methods.get(eventClass);
/*  88 */       if (listeners != null)
/*     */       {
/*  90 */         listeners.forEach((key, value) -> value.forEach(()));
/*     */       } }
/*     */   
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
/*     */   private void updateMethods() {
/* 114 */     this.methods.clear();
/* 115 */     for (Object listener : this.listeners) {
/*     */       
/* 117 */       boolean isClass = listener instanceof Class;
/* 118 */       Class<?> c = isClass ? (Class)listener : listener.getClass();
/* 119 */       Method[] allMethods = c.getDeclaredMethods();
/* 120 */       for (Method m : allMethods) {
/*     */         
/* 122 */         if (m.isAnnotationPresent((Class)SubscribeEvent.class) && (!isClass || Modifier.isStatic(m.getModifiers()))) {
/*     */ 
/*     */ 
/*     */           
/* 126 */           Class<?>[] pType = m.getParameterTypes();
/* 127 */           if (pType.length == 1 && GenericEvent.class.isAssignableFrom(pType[0])) {
/*     */             
/* 129 */             Class<?> eventClass = pType[0];
/* 130 */             if (!this.methods.containsKey(eventClass))
/*     */             {
/* 132 */               this.methods.put(eventClass, new ConcurrentHashMap<>());
/*     */             }
/*     */             
/* 135 */             if (!((Map)this.methods.get(eventClass)).containsKey(listener))
/*     */             {
/* 137 */               ((Map)this.methods.get(eventClass)).put(listener, new CopyOnWriteArrayList());
/*     */             }
/*     */             
/* 140 */             ((List<Method>)((Map)this.methods.get(eventClass)).get(listener)).add(m);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\hooks\AnnotatedEventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */