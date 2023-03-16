/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CursorableLinkedList<E>
/*     */   extends AbstractLinkedList<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8836393098519411393L;
/*     */   private transient List<WeakReference<Cursor<E>>> cursors;
/*     */   
/*     */   public CursorableLinkedList() {
/*  72 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CursorableLinkedList(Collection<? extends E> coll) {
/*  81 */     super(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/*  90 */     super.init();
/*  91 */     this.cursors = new ArrayList<WeakReference<Cursor<E>>>();
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
/*     */   public Iterator<E> iterator() {
/* 106 */     return super.listIterator(0);
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
/*     */   public ListIterator<E> listIterator() {
/* 126 */     return cursor(0);
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
/*     */   public ListIterator<E> listIterator(int fromIndex) {
/* 147 */     return cursor(fromIndex);
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
/*     */   public Cursor<E> cursor() {
/* 174 */     return cursor(0);
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
/*     */   public Cursor<E> cursor(int fromIndex) {
/* 205 */     Cursor<E> cursor = new Cursor<E>(this, fromIndex);
/* 206 */     registerCursor(cursor);
/* 207 */     return cursor;
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
/*     */   protected void updateNode(AbstractLinkedList.Node<E> node, E value) {
/* 221 */     super.updateNode(node, value);
/* 222 */     broadcastNodeChanged(node);
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
/*     */   protected void addNode(AbstractLinkedList.Node<E> nodeToInsert, AbstractLinkedList.Node<E> insertBeforeNode) {
/* 234 */     super.addNode(nodeToInsert, insertBeforeNode);
/* 235 */     broadcastNodeInserted(nodeToInsert);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeNode(AbstractLinkedList.Node<E> node) {
/* 246 */     super.removeNode(node);
/* 247 */     broadcastNodeRemoved(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeAllNodes() {
/* 255 */     if (size() > 0) {
/*     */       
/* 257 */       Iterator<E> it = iterator();
/* 258 */       while (it.hasNext()) {
/* 259 */         it.next();
/* 260 */         it.remove();
/*     */       } 
/*     */     } 
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
/*     */   protected void registerCursor(Cursor<E> cursor) {
/* 274 */     for (Iterator<WeakReference<Cursor<E>>> it = this.cursors.iterator(); it.hasNext(); ) {
/* 275 */       WeakReference<Cursor<E>> ref = it.next();
/* 276 */       if (ref.get() == null) {
/* 277 */         it.remove();
/*     */       }
/*     */     } 
/* 280 */     this.cursors.add(new WeakReference<Cursor<E>>(cursor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void unregisterCursor(Cursor<E> cursor) {
/* 289 */     for (Iterator<WeakReference<Cursor<E>>> it = this.cursors.iterator(); it.hasNext(); ) {
/* 290 */       WeakReference<Cursor<E>> ref = it.next();
/* 291 */       Cursor<E> cur = ref.get();
/* 292 */       if (cur == null) {
/*     */ 
/*     */ 
/*     */         
/* 296 */         it.remove(); continue;
/* 297 */       }  if (cur == cursor) {
/* 298 */         ref.clear();
/* 299 */         it.remove();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void broadcastNodeChanged(AbstractLinkedList.Node<E> node) {
/* 313 */     Iterator<WeakReference<Cursor<E>>> it = this.cursors.iterator();
/* 314 */     while (it.hasNext()) {
/* 315 */       WeakReference<Cursor<E>> ref = it.next();
/* 316 */       Cursor<E> cursor = ref.get();
/* 317 */       if (cursor == null) {
/* 318 */         it.remove(); continue;
/*     */       } 
/* 320 */       cursor.nodeChanged(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void broadcastNodeRemoved(AbstractLinkedList.Node<E> node) {
/* 332 */     Iterator<WeakReference<Cursor<E>>> it = this.cursors.iterator();
/* 333 */     while (it.hasNext()) {
/* 334 */       WeakReference<Cursor<E>> ref = it.next();
/* 335 */       Cursor<E> cursor = ref.get();
/* 336 */       if (cursor == null) {
/* 337 */         it.remove(); continue;
/*     */       } 
/* 339 */       cursor.nodeRemoved(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void broadcastNodeInserted(AbstractLinkedList.Node<E> node) {
/* 351 */     Iterator<WeakReference<Cursor<E>>> it = this.cursors.iterator();
/* 352 */     while (it.hasNext()) {
/* 353 */       WeakReference<Cursor<E>> ref = it.next();
/* 354 */       Cursor<E> cursor = ref.get();
/* 355 */       if (cursor == null) {
/* 356 */         it.remove(); continue;
/*     */       } 
/* 358 */       cursor.nodeInserted(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 368 */     out.defaultWriteObject();
/* 369 */     doWriteObject(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 376 */     in.defaultReadObject();
/* 377 */     doReadObject(in);
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
/*     */   protected ListIterator<E> createSubListListIterator(AbstractLinkedList.LinkedSubList<E> subList, int fromIndex) {
/* 390 */     SubCursor<E> cursor = new SubCursor<E>(subList, fromIndex);
/* 391 */     registerCursor(cursor);
/* 392 */     return cursor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Cursor<E>
/*     */     extends AbstractLinkedList.LinkedListIterator<E>
/*     */   {
/*     */     boolean valid = true;
/*     */ 
/*     */ 
/*     */     
/*     */     boolean nextIndexValid = true;
/*     */ 
/*     */ 
/*     */     
/*     */     boolean currentRemovedByAnother = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Cursor(CursorableLinkedList<E> parent, int index) {
/* 415 */       super(parent, index);
/* 416 */       this.valid = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 434 */       if (this.current != null || !this.currentRemovedByAnother) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 440 */         checkModCount();
/* 441 */         this.parent.removeNode(getLastNodeReturned());
/*     */       } 
/* 443 */       this.currentRemovedByAnother = false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(E obj) {
/* 455 */       super.add(obj);
/*     */ 
/*     */       
/* 458 */       this.next = this.next.next;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 473 */       if (!this.nextIndexValid) {
/* 474 */         if (this.next == this.parent.header) {
/* 475 */           this.nextIndex = this.parent.size();
/*     */         } else {
/* 477 */           int pos = 0;
/* 478 */           AbstractLinkedList.Node<E> temp = this.parent.header.next;
/* 479 */           while (temp != this.next) {
/* 480 */             pos++;
/* 481 */             temp = temp.next;
/*     */           } 
/* 483 */           this.nextIndex = pos;
/*     */         } 
/* 485 */         this.nextIndexValid = true;
/*     */       } 
/* 487 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void nodeChanged(AbstractLinkedList.Node<E> node) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void nodeRemoved(AbstractLinkedList.Node<E> node) {
/* 505 */       if (node == this.next && node == this.current) {
/*     */         
/* 507 */         this.next = node.next;
/* 508 */         this.current = null;
/* 509 */         this.currentRemovedByAnother = true;
/* 510 */       } else if (node == this.next) {
/*     */ 
/*     */         
/* 513 */         this.next = node.next;
/* 514 */         this.currentRemovedByAnother = false;
/* 515 */       } else if (node == this.current) {
/*     */ 
/*     */         
/* 518 */         this.current = null;
/* 519 */         this.currentRemovedByAnother = true;
/* 520 */         this.nextIndex--;
/*     */       } else {
/* 522 */         this.nextIndexValid = false;
/* 523 */         this.currentRemovedByAnother = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void nodeInserted(AbstractLinkedList.Node<E> node) {
/* 533 */       if (node.previous == this.current) {
/* 534 */         this.next = node;
/* 535 */       } else if (this.next.previous == node) {
/* 536 */         this.next = node;
/*     */       } else {
/* 538 */         this.nextIndexValid = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void checkModCount() {
/* 547 */       if (!this.valid) {
/* 548 */         throw new ConcurrentModificationException("Cursor closed");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 561 */       if (this.valid) {
/* 562 */         ((CursorableLinkedList<E>)this.parent).unregisterCursor(this);
/* 563 */         this.valid = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class SubCursor<E>
/*     */     extends Cursor<E>
/*     */   {
/*     */     protected final AbstractLinkedList.LinkedSubList<E> sub;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected SubCursor(AbstractLinkedList.LinkedSubList<E> sub, int index) {
/* 586 */       super((CursorableLinkedList<E>)sub.parent, index + sub.offset);
/* 587 */       this.sub = sub;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 592 */       return (nextIndex() < this.sub.size);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 597 */       return (previousIndex() >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 602 */       return super.nextIndex() - this.sub.offset;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(E obj) {
/* 607 */       super.add(obj);
/* 608 */       this.sub.expectedModCount = this.parent.modCount;
/* 609 */       this.sub.size++;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 614 */       super.remove();
/* 615 */       this.sub.expectedModCount = this.parent.modCount;
/* 616 */       this.sub.size--;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\CursorableLinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */