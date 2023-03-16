/*      */ package org.apache.commons.collections4.list;
/*      */ 
/*      */ import java.util.AbstractList;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import org.apache.commons.collections4.OrderedIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TreeList<E>
/*      */   extends AbstractList<E>
/*      */ {
/*      */   private AVLNode<E> root;
/*      */   private int size;
/*      */   
/*      */   public TreeList() {}
/*      */   
/*      */   public TreeList(Collection<? extends E> coll) {
/*   86 */     if (!coll.isEmpty()) {
/*   87 */       this.root = new AVLNode<E>(coll);
/*   88 */       this.size = coll.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E get(int index) {
/*  101 */     checkInterval(index, 0, size() - 1);
/*  102 */     return this.root.get(index).getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  112 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<E> iterator() {
/*  123 */     return listIterator(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ListIterator<E> listIterator() {
/*  134 */     return listIterator(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ListIterator<E> listIterator(int fromIndex) {
/*  147 */     checkInterval(fromIndex, 0, size());
/*  148 */     return new TreeListIterator<E>(this, fromIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(Object object) {
/*  160 */     if (this.root == null) {
/*  161 */       return -1;
/*      */     }
/*  163 */     return this.root.indexOf(object, this.root.relativePosition);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contains(Object object) {
/*  174 */     return (indexOf(object) >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] toArray() {
/*  185 */     Object[] array = new Object[size()];
/*  186 */     if (this.root != null) {
/*  187 */       this.root.toArray(array, this.root.relativePosition);
/*      */     }
/*  189 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(int index, E obj) {
/*  201 */     this.modCount++;
/*  202 */     checkInterval(index, 0, size());
/*  203 */     if (this.root == null) {
/*  204 */       this.root = new AVLNode<E>(index, obj, null, null);
/*      */     } else {
/*  206 */       this.root = this.root.insert(index, obj);
/*      */     } 
/*  208 */     this.size++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addAll(Collection<? extends E> c) {
/*  224 */     if (c.isEmpty()) {
/*  225 */       return false;
/*      */     }
/*  227 */     this.modCount += c.size();
/*  228 */     AVLNode<E> cTree = new AVLNode<E>(c);
/*  229 */     this.root = (this.root == null) ? cTree : this.root.addAll(cTree, this.size);
/*  230 */     this.size += c.size();
/*  231 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E set(int index, E obj) {
/*  244 */     checkInterval(index, 0, size() - 1);
/*  245 */     AVLNode<E> node = this.root.get(index);
/*  246 */     E result = node.value;
/*  247 */     node.setValue(obj);
/*  248 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public E remove(int index) {
/*  259 */     this.modCount++;
/*  260 */     checkInterval(index, 0, size() - 1);
/*  261 */     E result = get(index);
/*  262 */     this.root = this.root.remove(index);
/*  263 */     this.size--;
/*  264 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  272 */     this.modCount++;
/*  273 */     this.root = null;
/*  274 */     this.size = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkInterval(int index, int startIndex, int endIndex) {
/*  287 */     if (index < startIndex || index > endIndex) {
/*  288 */       throw new IndexOutOfBoundsException("Invalid index:" + index + ", size=" + size());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class AVLNode<E>
/*      */   {
/*      */     private AVLNode<E> left;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean leftIsPrevious;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> right;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean rightIsNext;
/*      */ 
/*      */ 
/*      */     
/*      */     private int height;
/*      */ 
/*      */ 
/*      */     
/*      */     private int relativePosition;
/*      */ 
/*      */ 
/*      */     
/*      */     private E value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode(int relativePosition, E obj, AVLNode<E> rightFollower, AVLNode<E> leftFollower) {
/*  331 */       this.relativePosition = relativePosition;
/*  332 */       this.value = obj;
/*  333 */       this.rightIsNext = true;
/*  334 */       this.leftIsPrevious = true;
/*  335 */       this.right = rightFollower;
/*  336 */       this.left = leftFollower;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode(Collection<? extends E> coll) {
/*  347 */       this(coll.iterator(), 0, coll.size() - 1, 0, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode(Iterator<? extends E> iterator, int start, int end, int absolutePositionOfParent, AVLNode<E> prev, AVLNode<E> next) {
/*  373 */       int mid = start + (end - start) / 2;
/*  374 */       if (start < mid) {
/*  375 */         this.left = new AVLNode(iterator, start, mid - 1, mid, prev, this);
/*      */       } else {
/*  377 */         this.leftIsPrevious = true;
/*  378 */         this.left = prev;
/*      */       } 
/*  380 */       this.value = iterator.next();
/*  381 */       this.relativePosition = mid - absolutePositionOfParent;
/*  382 */       if (mid < end) {
/*  383 */         this.right = new AVLNode(iterator, mid + 1, end, mid, this, next);
/*      */       } else {
/*  385 */         this.rightIsNext = true;
/*  386 */         this.right = next;
/*      */       } 
/*  388 */       recalcHeight();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     E getValue() {
/*  397 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue(E obj) {
/*  406 */       this.value = obj;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AVLNode<E> get(int index) {
/*  414 */       int indexRelativeToMe = index - this.relativePosition;
/*      */       
/*  416 */       if (indexRelativeToMe == 0) {
/*  417 */         return this;
/*      */       }
/*      */       
/*  420 */       AVLNode<E> nextNode = (indexRelativeToMe < 0) ? getLeftSubTree() : getRightSubTree();
/*  421 */       if (nextNode == null) {
/*  422 */         return null;
/*      */       }
/*  424 */       return nextNode.get(indexRelativeToMe);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int indexOf(Object object, int index) {
/*  431 */       if (getLeftSubTree() != null) {
/*  432 */         int result = this.left.indexOf(object, index + this.left.relativePosition);
/*  433 */         if (result != -1) {
/*  434 */           return result;
/*      */         }
/*      */       } 
/*  437 */       if ((this.value == null) ? (this.value == object) : this.value.equals(object)) {
/*  438 */         return index;
/*      */       }
/*  440 */       if (getRightSubTree() != null) {
/*  441 */         return this.right.indexOf(object, index + this.right.relativePosition);
/*      */       }
/*  443 */       return -1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void toArray(Object[] array, int index) {
/*  453 */       array[index] = this.value;
/*  454 */       if (getLeftSubTree() != null) {
/*  455 */         this.left.toArray(array, index + this.left.relativePosition);
/*      */       }
/*  457 */       if (getRightSubTree() != null) {
/*  458 */         this.right.toArray(array, index + this.right.relativePosition);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AVLNode<E> next() {
/*  468 */       if (this.rightIsNext || this.right == null) {
/*  469 */         return this.right;
/*      */       }
/*  471 */       return this.right.min();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AVLNode<E> previous() {
/*  480 */       if (this.leftIsPrevious || this.left == null) {
/*  481 */         return this.left;
/*      */       }
/*  483 */       return this.left.max();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AVLNode<E> insert(int index, E obj) {
/*  494 */       int indexRelativeToMe = index - this.relativePosition;
/*      */       
/*  496 */       if (indexRelativeToMe <= 0) {
/*  497 */         return insertOnLeft(indexRelativeToMe, obj);
/*      */       }
/*  499 */       return insertOnRight(indexRelativeToMe, obj);
/*      */     }
/*      */     
/*      */     private AVLNode<E> insertOnLeft(int indexRelativeToMe, E obj) {
/*  503 */       if (getLeftSubTree() == null) {
/*  504 */         setLeft(new AVLNode(-1, obj, this, this.left), null);
/*      */       } else {
/*  506 */         setLeft(this.left.insert(indexRelativeToMe, obj), null);
/*      */       } 
/*      */       
/*  509 */       if (this.relativePosition >= 0) {
/*  510 */         this.relativePosition++;
/*      */       }
/*  512 */       AVLNode<E> ret = balance();
/*  513 */       recalcHeight();
/*  514 */       return ret;
/*      */     }
/*      */     
/*      */     private AVLNode<E> insertOnRight(int indexRelativeToMe, E obj) {
/*  518 */       if (getRightSubTree() == null) {
/*  519 */         setRight(new AVLNode(1, obj, this.right, this), null);
/*      */       } else {
/*  521 */         setRight(this.right.insert(indexRelativeToMe, obj), null);
/*      */       } 
/*  523 */       if (this.relativePosition < 0) {
/*  524 */         this.relativePosition--;
/*      */       }
/*  526 */       AVLNode<E> ret = balance();
/*  527 */       recalcHeight();
/*  528 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> getLeftSubTree() {
/*  536 */       return this.leftIsPrevious ? null : this.left;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> getRightSubTree() {
/*  543 */       return this.rightIsNext ? null : this.right;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> max() {
/*  552 */       return (getRightSubTree() == null) ? this : this.right.max();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> min() {
/*  561 */       return (getLeftSubTree() == null) ? this : this.left.min();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AVLNode<E> remove(int index) {
/*  571 */       int indexRelativeToMe = index - this.relativePosition;
/*      */       
/*  573 */       if (indexRelativeToMe == 0) {
/*  574 */         return removeSelf();
/*      */       }
/*  576 */       if (indexRelativeToMe > 0) {
/*  577 */         setRight(this.right.remove(indexRelativeToMe), this.right.right);
/*  578 */         if (this.relativePosition < 0) {
/*  579 */           this.relativePosition++;
/*      */         }
/*      */       } else {
/*  582 */         setLeft(this.left.remove(indexRelativeToMe), this.left.left);
/*  583 */         if (this.relativePosition > 0) {
/*  584 */           this.relativePosition--;
/*      */         }
/*      */       } 
/*  587 */       recalcHeight();
/*  588 */       return balance();
/*      */     }
/*      */     
/*      */     private AVLNode<E> removeMax() {
/*  592 */       if (getRightSubTree() == null) {
/*  593 */         return removeSelf();
/*      */       }
/*  595 */       setRight(this.right.removeMax(), this.right.right);
/*  596 */       if (this.relativePosition < 0) {
/*  597 */         this.relativePosition++;
/*      */       }
/*  599 */       recalcHeight();
/*  600 */       return balance();
/*      */     }
/*      */     
/*      */     private AVLNode<E> removeMin() {
/*  604 */       if (getLeftSubTree() == null) {
/*  605 */         return removeSelf();
/*      */       }
/*  607 */       setLeft(this.left.removeMin(), this.left.left);
/*  608 */       if (this.relativePosition > 0) {
/*  609 */         this.relativePosition--;
/*      */       }
/*  611 */       recalcHeight();
/*  612 */       return balance();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> removeSelf() {
/*  621 */       if (getRightSubTree() == null && getLeftSubTree() == null) {
/*  622 */         return null;
/*      */       }
/*  624 */       if (getRightSubTree() == null) {
/*  625 */         if (this.relativePosition > 0) {
/*  626 */           this.left.relativePosition += this.relativePosition + ((this.relativePosition > 0) ? 0 : 1);
/*      */         }
/*  628 */         this.left.max().setRight(null, this.right);
/*  629 */         return this.left;
/*      */       } 
/*  631 */       if (getLeftSubTree() == null) {
/*  632 */         this.right.relativePosition += this.relativePosition - ((this.relativePosition < 0) ? 0 : 1);
/*  633 */         this.right.min().setLeft(null, this.left);
/*  634 */         return this.right;
/*      */       } 
/*      */       
/*  637 */       if (heightRightMinusLeft() > 0) {
/*      */         
/*  639 */         AVLNode<E> rightMin = this.right.min();
/*  640 */         this.value = rightMin.value;
/*  641 */         if (this.leftIsPrevious) {
/*  642 */           this.left = rightMin.left;
/*      */         }
/*  644 */         this.right = this.right.removeMin();
/*  645 */         if (this.relativePosition < 0) {
/*  646 */           this.relativePosition++;
/*      */         }
/*      */       } else {
/*      */         
/*  650 */         AVLNode<E> leftMax = this.left.max();
/*  651 */         this.value = leftMax.value;
/*  652 */         if (this.rightIsNext) {
/*  653 */           this.right = leftMax.right;
/*      */         }
/*  655 */         AVLNode<E> leftPrevious = this.left.left;
/*  656 */         this.left = this.left.removeMax();
/*  657 */         if (this.left == null) {
/*      */ 
/*      */           
/*  660 */           this.left = leftPrevious;
/*  661 */           this.leftIsPrevious = true;
/*      */         } 
/*  663 */         if (this.relativePosition > 0) {
/*  664 */           this.relativePosition--;
/*      */         }
/*      */       } 
/*  667 */       recalcHeight();
/*  668 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> balance() {
/*  676 */       switch (heightRightMinusLeft()) {
/*      */         case -1:
/*      */         case 0:
/*      */         case 1:
/*  680 */           return this;
/*      */         case -2:
/*  682 */           if (this.left.heightRightMinusLeft() > 0) {
/*  683 */             setLeft(this.left.rotateLeft(), null);
/*      */           }
/*  685 */           return rotateRight();
/*      */         case 2:
/*  687 */           if (this.right.heightRightMinusLeft() < 0) {
/*  688 */             setRight(this.right.rotateRight(), null);
/*      */           }
/*  690 */           return rotateLeft();
/*      */       } 
/*  692 */       throw new RuntimeException("tree inconsistent!");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int getOffset(AVLNode<E> node) {
/*  700 */       if (node == null) {
/*  701 */         return 0;
/*      */       }
/*  703 */       return node.relativePosition;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int setOffset(AVLNode<E> node, int newOffest) {
/*  710 */       if (node == null) {
/*  711 */         return 0;
/*      */       }
/*  713 */       int oldOffset = getOffset(node);
/*  714 */       node.relativePosition = newOffest;
/*  715 */       return oldOffset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void recalcHeight() {
/*  722 */       this.height = Math.max((getLeftSubTree() == null) ? -1 : (getLeftSubTree()).height, (getRightSubTree() == null) ? -1 : (getRightSubTree()).height) + 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int getHeight(AVLNode<E> node) {
/*  731 */       return (node == null) ? -1 : node.height;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int heightRightMinusLeft() {
/*  738 */       return getHeight(getRightSubTree()) - getHeight(getLeftSubTree());
/*      */     }
/*      */     
/*      */     private AVLNode<E> rotateLeft() {
/*  742 */       AVLNode<E> newTop = this.right;
/*  743 */       AVLNode<E> movedNode = getRightSubTree().getLeftSubTree();
/*      */       
/*  745 */       int newTopPosition = this.relativePosition + getOffset(newTop);
/*  746 */       int myNewPosition = -newTop.relativePosition;
/*  747 */       int movedPosition = getOffset(newTop) + getOffset(movedNode);
/*      */       
/*  749 */       setRight(movedNode, newTop);
/*  750 */       newTop.setLeft(this, null);
/*      */       
/*  752 */       setOffset(newTop, newTopPosition);
/*  753 */       setOffset(this, myNewPosition);
/*  754 */       setOffset(movedNode, movedPosition);
/*  755 */       return newTop;
/*      */     }
/*      */     
/*      */     private AVLNode<E> rotateRight() {
/*  759 */       AVLNode<E> newTop = this.left;
/*  760 */       AVLNode<E> movedNode = getLeftSubTree().getRightSubTree();
/*      */       
/*  762 */       int newTopPosition = this.relativePosition + getOffset(newTop);
/*  763 */       int myNewPosition = -newTop.relativePosition;
/*  764 */       int movedPosition = getOffset(newTop) + getOffset(movedNode);
/*      */       
/*  766 */       setLeft(movedNode, newTop);
/*  767 */       newTop.setRight(this, null);
/*      */       
/*  769 */       setOffset(newTop, newTopPosition);
/*  770 */       setOffset(this, myNewPosition);
/*  771 */       setOffset(movedNode, movedPosition);
/*  772 */       return newTop;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setLeft(AVLNode<E> node, AVLNode<E> previous) {
/*  782 */       this.leftIsPrevious = (node == null);
/*  783 */       this.left = this.leftIsPrevious ? previous : node;
/*  784 */       recalcHeight();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setRight(AVLNode<E> node, AVLNode<E> next) {
/*  794 */       this.rightIsNext = (node == null);
/*  795 */       this.right = this.rightIsNext ? next : node;
/*  796 */       recalcHeight();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private AVLNode<E> addAll(AVLNode<E> otherTree, int currentSize) {
/*  811 */       AVLNode<E> maxNode = max();
/*  812 */       AVLNode<E> otherTreeMin = otherTree.min();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  820 */       if (otherTree.height > this.height) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  825 */         AVLNode<E> leftSubTree = removeMax();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  831 */         Deque<AVLNode<E>> deque = new ArrayDeque<AVLNode<E>>();
/*  832 */         AVLNode<E> aVLNode1 = otherTree;
/*  833 */         int i = aVLNode1.relativePosition + currentSize;
/*  834 */         int j = 0;
/*  835 */         while (aVLNode1 != null && aVLNode1.height > getHeight(leftSubTree)) {
/*  836 */           j = i;
/*  837 */           deque.push(aVLNode1);
/*  838 */           aVLNode1 = aVLNode1.left;
/*  839 */           if (aVLNode1 != null) {
/*  840 */             i += aVLNode1.relativePosition;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  847 */         maxNode.setLeft(leftSubTree, null);
/*  848 */         maxNode.setRight(aVLNode1, otherTreeMin);
/*  849 */         if (leftSubTree != null) {
/*  850 */           leftSubTree.max().setRight(null, maxNode);
/*  851 */           leftSubTree.relativePosition -= currentSize - 1;
/*      */         } 
/*  853 */         if (aVLNode1 != null) {
/*  854 */           aVLNode1.min().setLeft(null, maxNode);
/*  855 */           aVLNode1.relativePosition = i - currentSize + 1;
/*      */         } 
/*  857 */         maxNode.relativePosition = currentSize - 1 - j;
/*  858 */         otherTree.relativePosition += currentSize;
/*      */ 
/*      */         
/*  861 */         aVLNode1 = maxNode;
/*  862 */         while (!deque.isEmpty()) {
/*  863 */           AVLNode<E> sAncestor = deque.pop();
/*  864 */           sAncestor.setLeft(aVLNode1, null);
/*  865 */           aVLNode1 = sAncestor.balance();
/*      */         } 
/*  867 */         return aVLNode1;
/*      */       } 
/*  869 */       otherTree = otherTree.removeMin();
/*      */       
/*  871 */       Deque<AVLNode<E>> sAncestors = new ArrayDeque<AVLNode<E>>();
/*  872 */       AVLNode<E> s = this;
/*  873 */       int sAbsolutePosition = s.relativePosition;
/*  874 */       int sParentAbsolutePosition = 0;
/*  875 */       while (s != null && s.height > getHeight(otherTree)) {
/*  876 */         sParentAbsolutePosition = sAbsolutePosition;
/*  877 */         sAncestors.push(s);
/*  878 */         s = s.right;
/*  879 */         if (s != null) {
/*  880 */           sAbsolutePosition += s.relativePosition;
/*      */         }
/*      */       } 
/*      */       
/*  884 */       otherTreeMin.setRight(otherTree, null);
/*  885 */       otherTreeMin.setLeft(s, maxNode);
/*  886 */       if (otherTree != null) {
/*  887 */         otherTree.min().setLeft(null, otherTreeMin);
/*  888 */         otherTree.relativePosition++;
/*      */       } 
/*  890 */       if (s != null) {
/*  891 */         s.max().setRight(null, otherTreeMin);
/*  892 */         s.relativePosition = sAbsolutePosition - currentSize;
/*      */       } 
/*  894 */       otherTreeMin.relativePosition = currentSize - sParentAbsolutePosition;
/*      */       
/*  896 */       s = otherTreeMin;
/*  897 */       while (!sAncestors.isEmpty()) {
/*  898 */         AVLNode<E> sAncestor = sAncestors.pop();
/*  899 */         sAncestor.setRight(s, null);
/*  900 */         s = sAncestor.balance();
/*      */       } 
/*  902 */       return s;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  962 */       return "AVLNode(" + this.relativePosition + ',' + ((this.left != null) ? 1 : 0) + ',' + this.value + ',' + ((getRightSubTree() != null) ? 1 : 0) + ", faedelung " + this.rightIsNext + " )";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TreeListIterator<E>
/*      */     implements ListIterator<E>, OrderedIterator<E>
/*      */   {
/*      */     private final TreeList<E> parent;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private TreeList.AVLNode<E> next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int nextIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private TreeList.AVLNode<E> current;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int currentIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int expectedModCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TreeListIterator(TreeList<E> parent, int fromIndex) throws IndexOutOfBoundsException {
/* 1017 */       this.parent = parent;
/* 1018 */       this.expectedModCount = parent.modCount;
/* 1019 */       this.next = (parent.root == null) ? null : parent.root.get(fromIndex);
/* 1020 */       this.nextIndex = fromIndex;
/* 1021 */       this.currentIndex = -1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void checkModCount() {
/* 1032 */       if (this.parent.modCount != this.expectedModCount) {
/* 1033 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1038 */       return (this.nextIndex < this.parent.size());
/*      */     }
/*      */     
/*      */     public E next() {
/* 1042 */       checkModCount();
/* 1043 */       if (!hasNext()) {
/* 1044 */         throw new NoSuchElementException("No element at index " + this.nextIndex + ".");
/*      */       }
/* 1046 */       if (this.next == null) {
/* 1047 */         this.next = this.parent.root.get(this.nextIndex);
/*      */       }
/* 1049 */       E value = this.next.getValue();
/* 1050 */       this.current = this.next;
/* 1051 */       this.currentIndex = this.nextIndex++;
/* 1052 */       this.next = this.next.next();
/* 1053 */       return value;
/*      */     }
/*      */     
/*      */     public boolean hasPrevious() {
/* 1057 */       return (this.nextIndex > 0);
/*      */     }
/*      */     
/*      */     public E previous() {
/* 1061 */       checkModCount();
/* 1062 */       if (!hasPrevious()) {
/* 1063 */         throw new NoSuchElementException("Already at start of list.");
/*      */       }
/* 1065 */       if (this.next == null) {
/* 1066 */         this.next = this.parent.root.get(this.nextIndex - 1);
/*      */       } else {
/* 1068 */         this.next = this.next.previous();
/*      */       } 
/* 1070 */       E value = this.next.getValue();
/* 1071 */       this.current = this.next;
/* 1072 */       this.currentIndex = --this.nextIndex;
/* 1073 */       return value;
/*      */     }
/*      */     
/*      */     public int nextIndex() {
/* 1077 */       return this.nextIndex;
/*      */     }
/*      */     
/*      */     public int previousIndex() {
/* 1081 */       return nextIndex() - 1;
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1085 */       checkModCount();
/* 1086 */       if (this.currentIndex == -1) {
/* 1087 */         throw new IllegalStateException();
/*      */       }
/* 1089 */       this.parent.remove(this.currentIndex);
/* 1090 */       if (this.nextIndex != this.currentIndex)
/*      */       {
/* 1092 */         this.nextIndex--;
/*      */       }
/*      */ 
/*      */       
/* 1096 */       this.next = null;
/* 1097 */       this.current = null;
/* 1098 */       this.currentIndex = -1;
/* 1099 */       this.expectedModCount++;
/*      */     }
/*      */     
/*      */     public void set(E obj) {
/* 1103 */       checkModCount();
/* 1104 */       if (this.current == null) {
/* 1105 */         throw new IllegalStateException();
/*      */       }
/* 1107 */       this.current.setValue(obj);
/*      */     }
/*      */     
/*      */     public void add(E obj) {
/* 1111 */       checkModCount();
/* 1112 */       this.parent.add(this.nextIndex, obj);
/* 1113 */       this.current = null;
/* 1114 */       this.currentIndex = -1;
/* 1115 */       this.nextIndex++;
/* 1116 */       this.expectedModCount++;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\TreeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */