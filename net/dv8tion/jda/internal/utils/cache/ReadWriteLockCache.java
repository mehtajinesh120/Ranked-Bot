/*    */ package net.dv8tion.jda.internal.utils.cache;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.NavigableSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*    */ import net.dv8tion.jda.api.utils.MiscUtil;
/*    */ import net.dv8tion.jda.internal.utils.UnlockHook;
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
/*    */ public abstract class ReadWriteLockCache<T>
/*    */ {
/* 31 */   protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*    */   
/*    */   protected WeakReference<List<T>> cachedList;
/*    */   protected WeakReference<Set<T>> cachedSet;
/*    */   
/*    */   public UnlockHook writeLock() {
/* 37 */     if (this.lock.getReadHoldCount() > 0)
/* 38 */       throw new IllegalStateException("Unable to acquire write-lock while holding read-lock!"); 
/* 39 */     ReentrantReadWriteLock.WriteLock writeLock = this.lock.writeLock();
/* 40 */     MiscUtil.tryLock(writeLock);
/* 41 */     onAcquireWriteLock();
/* 42 */     clearCachedLists();
/* 43 */     return new UnlockHook(writeLock);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnlockHook readLock() {
/* 48 */     ReentrantReadWriteLock.ReadLock readLock = this.lock.readLock();
/* 49 */     MiscUtil.tryLock(readLock);
/* 50 */     onAcquireReadLock();
/* 51 */     return new UnlockHook(readLock);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clearCachedLists() {
/* 56 */     this.cachedList = null;
/* 57 */     this.cachedSet = null;
/*    */   }
/*    */   
/*    */   protected void onAcquireWriteLock() {}
/*    */   
/*    */   protected void onAcquireReadLock() {}
/*    */   
/*    */   protected List<T> getCachedList() {
/* 65 */     return (this.cachedList == null) ? null : this.cachedList.get();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<T> getCachedSet() {
/* 70 */     return (this.cachedSet == null) ? null : this.cachedSet.get();
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<T> cache(List<T> list) {
/* 75 */     list = Collections.unmodifiableList(list);
/* 76 */     this.cachedList = new WeakReference<>(list);
/* 77 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Set<T> cache(Set<T> set) {
/* 82 */     set = Collections.unmodifiableSet(set);
/* 83 */     this.cachedSet = new WeakReference<>(set);
/* 84 */     return set;
/*    */   }
/*    */ 
/*    */   
/*    */   protected NavigableSet<T> cache(NavigableSet<T> set) {
/* 89 */     set = Collections.unmodifiableNavigableSet(set);
/* 90 */     this.cachedSet = new WeakReference<>(set);
/* 91 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\ReadWriteLockCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */