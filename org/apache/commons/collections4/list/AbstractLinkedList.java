/*      */ package org.apache.commons.collections4.list;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.AbstractList;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
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
/*      */ public abstract class AbstractLinkedList<E>
/*      */   implements List<E>
/*      */ {
/*      */   transient Node<E> header;
/*      */   transient int size;
/*      */   transient int modCount;
/*      */   
/*      */   protected AbstractLinkedList() {}
/*      */   
/*      */   protected AbstractLinkedList(Collection<? extends E> coll) {
/*   87 */     init();
/*   88 */     addAll(coll);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {
/*   98 */     this.header = createHeaderNode();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  104 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  108 */     return (size() == 0);
/*      */   }
/*      */   
/*      */   public E get(int index) {
/*  112 */     Node<E> node = getNode(index, false);
/*  113 */     return node.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<E> iterator() {
/*  119 */     return listIterator();
/*      */   }
/*      */   
/*      */   public ListIterator<E> listIterator() {
/*  123 */     return new LinkedListIterator<E>(this, 0);
/*      */   }
/*      */   
/*      */   public ListIterator<E> listIterator(int fromIndex) {
/*  127 */     return new LinkedListIterator<E>(this, fromIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(Object value) {
/*  133 */     int i = 0;
/*  134 */     for (Node<E> node = this.header.next; node != this.header; node = node.next) {
/*  135 */       if (isEqualValue(node.getValue(), value)) {
/*  136 */         return i;
/*      */       }
/*  138 */       i++;
/*      */     } 
/*  140 */     return -1;
/*      */   }
/*      */   
/*      */   public int lastIndexOf(Object value) {
/*  144 */     int i = this.size - 1;
/*  145 */     for (Node<E> node = this.header.previous; node != this.header; node = node.previous) {
/*  146 */       if (isEqualValue(node.getValue(), value)) {
/*  147 */         return i;
/*      */       }
/*  149 */       i--;
/*      */     } 
/*  151 */     return -1;
/*      */   }
/*      */   
/*      */   public boolean contains(Object value) {
/*  155 */     return (indexOf(value) != -1);
/*      */   }
/*      */   
/*      */   public boolean containsAll(Collection<?> coll) {
/*  159 */     for (Object o : coll) {
/*  160 */       if (!contains(o)) {
/*  161 */         return false;
/*      */       }
/*      */     } 
/*  164 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] toArray() {
/*  170 */     return toArray(new Object[this.size]);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T[] toArray(T[] array) {
/*  176 */     if (array.length < this.size) {
/*  177 */       Class<?> componentType = array.getClass().getComponentType();
/*  178 */       array = (T[])Array.newInstance(componentType, this.size);
/*      */     } 
/*      */     
/*  181 */     int i = 0;
/*  182 */     for (Node<E> node = this.header.next; node != this.header; node = node.next, i++) {
/*  183 */       array[i] = (T)node.getValue();
/*      */     }
/*      */     
/*  186 */     if (array.length > this.size) {
/*  187 */       array[this.size] = null;
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
/*      */   public List<E> subList(int fromIndexInclusive, int toIndexExclusive) {
/*  200 */     return new LinkedSubList<E>(this, fromIndexInclusive, toIndexExclusive);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean add(E value) {
/*  206 */     addLast(value);
/*  207 */     return true;
/*      */   }
/*      */   
/*      */   public void add(int index, E value) {
/*  211 */     Node<E> node = getNode(index, true);
/*  212 */     addNodeBefore(node, value);
/*      */   }
/*      */   
/*      */   public boolean addAll(Collection<? extends E> coll) {
/*  216 */     return addAll(this.size, coll);
/*      */   }
/*      */   
/*      */   public boolean addAll(int index, Collection<? extends E> coll) {
/*  220 */     Node<E> node = getNode(index, true);
/*  221 */     for (E e : coll) {
/*  222 */       addNodeBefore(node, e);
/*      */     }
/*  224 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public E remove(int index) {
/*  230 */     Node<E> node = getNode(index, false);
/*  231 */     E oldValue = node.getValue();
/*  232 */     removeNode(node);
/*  233 */     return oldValue;
/*      */   }
/*      */   
/*      */   public boolean remove(Object value) {
/*  237 */     for (Node<E> node = this.header.next; node != this.header; node = node.next) {
/*  238 */       if (isEqualValue(node.getValue(), value)) {
/*  239 */         removeNode(node);
/*  240 */         return true;
/*      */       } 
/*      */     } 
/*  243 */     return false;
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
/*      */   public boolean removeAll(Collection<?> coll) {
/*  256 */     boolean modified = false;
/*  257 */     Iterator<E> it = iterator();
/*  258 */     while (it.hasNext()) {
/*  259 */       if (coll.contains(it.next())) {
/*  260 */         it.remove();
/*  261 */         modified = true;
/*      */       } 
/*      */     } 
/*  264 */     return modified;
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
/*      */   public boolean retainAll(Collection<?> coll) {
/*  279 */     boolean modified = false;
/*  280 */     Iterator<E> it = iterator();
/*  281 */     while (it.hasNext()) {
/*  282 */       if (!coll.contains(it.next())) {
/*  283 */         it.remove();
/*  284 */         modified = true;
/*      */       } 
/*      */     } 
/*  287 */     return modified;
/*      */   }
/*      */   
/*      */   public E set(int index, E value) {
/*  291 */     Node<E> node = getNode(index, false);
/*  292 */     E oldValue = node.getValue();
/*  293 */     updateNode(node, value);
/*  294 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void clear() {
/*  298 */     removeAllNodes();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public E getFirst() {
/*  304 */     Node<E> node = this.header.next;
/*  305 */     if (node == this.header) {
/*  306 */       throw new NoSuchElementException();
/*      */     }
/*  308 */     return node.getValue();
/*      */   }
/*      */   
/*      */   public E getLast() {
/*  312 */     Node<E> node = this.header.previous;
/*  313 */     if (node == this.header) {
/*  314 */       throw new NoSuchElementException();
/*      */     }
/*  316 */     return node.getValue();
/*      */   }
/*      */   
/*      */   public boolean addFirst(E o) {
/*  320 */     addNodeAfter(this.header, o);
/*  321 */     return true;
/*      */   }
/*      */   
/*      */   public boolean addLast(E o) {
/*  325 */     addNodeBefore(this.header, o);
/*  326 */     return true;
/*      */   }
/*      */   
/*      */   public E removeFirst() {
/*  330 */     Node<E> node = this.header.next;
/*  331 */     if (node == this.header) {
/*  332 */       throw new NoSuchElementException();
/*      */     }
/*  334 */     E oldValue = node.getValue();
/*  335 */     removeNode(node);
/*  336 */     return oldValue;
/*      */   }
/*      */   
/*      */   public E removeLast() {
/*  340 */     Node<E> node = this.header.previous;
/*  341 */     if (node == this.header) {
/*  342 */       throw new NoSuchElementException();
/*      */     }
/*  344 */     E oldValue = node.getValue();
/*  345 */     removeNode(node);
/*  346 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  352 */     if (obj == this) {
/*  353 */       return true;
/*      */     }
/*  355 */     if (!(obj instanceof List)) {
/*  356 */       return false;
/*      */     }
/*  358 */     List<?> other = (List)obj;
/*  359 */     if (other.size() != size()) {
/*  360 */       return false;
/*      */     }
/*  362 */     ListIterator<?> it1 = listIterator();
/*  363 */     ListIterator<?> it2 = other.listIterator();
/*  364 */     while (it1.hasNext() && it2.hasNext()) {
/*  365 */       Object o1 = it1.next();
/*  366 */       Object o2 = it2.next();
/*  367 */       if ((o1 == null) ? (o2 == null) : o1.equals(o2))
/*  368 */         continue;  return false;
/*      */     } 
/*      */     
/*  371 */     return (!it1.hasNext() && !it2.hasNext());
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  376 */     int hashCode = 1;
/*  377 */     for (E e : this) {
/*  378 */       hashCode = 31 * hashCode + ((e == null) ? 0 : e.hashCode());
/*      */     }
/*  380 */     return hashCode;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  385 */     if (size() == 0) {
/*  386 */       return "[]";
/*      */     }
/*  388 */     StringBuilder buf = new StringBuilder(16 * size());
/*  389 */     buf.append('[');
/*      */     
/*  391 */     Iterator<E> it = iterator();
/*  392 */     boolean hasNext = it.hasNext();
/*  393 */     while (hasNext) {
/*  394 */       Object value = it.next();
/*  395 */       buf.append((value == this) ? "(this Collection)" : value);
/*  396 */       hasNext = it.hasNext();
/*  397 */       if (hasNext) {
/*  398 */         buf.append(", ");
/*      */       }
/*      */     } 
/*  401 */     buf.append(']');
/*  402 */     return buf.toString();
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
/*      */   protected boolean isEqualValue(Object value1, Object value2) {
/*  416 */     return (value1 == value2 || (value1 != null && value1.equals(value2)));
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
/*      */   protected void updateNode(Node<E> node, E value) {
/*  428 */     node.setValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Node<E> createHeaderNode() {
/*  439 */     return new Node<E>();
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
/*      */   protected Node<E> createNode(E value) {
/*  451 */     return new Node<E>(value);
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
/*      */   protected void addNodeBefore(Node<E> node, E value) {
/*  466 */     Node<E> newNode = createNode(value);
/*  467 */     addNode(newNode, node);
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
/*      */   protected void addNodeAfter(Node<E> node, E value) {
/*  482 */     Node<E> newNode = createNode(value);
/*  483 */     addNode(newNode, node.next);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addNode(Node<E> nodeToInsert, Node<E> insertBeforeNode) {
/*  494 */     nodeToInsert.next = insertBeforeNode;
/*  495 */     nodeToInsert.previous = insertBeforeNode.previous;
/*  496 */     insertBeforeNode.previous.next = nodeToInsert;
/*  497 */     insertBeforeNode.previous = nodeToInsert;
/*  498 */     this.size++;
/*  499 */     this.modCount++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeNode(Node<E> node) {
/*  509 */     node.previous.next = node.next;
/*  510 */     node.next.previous = node.previous;
/*  511 */     this.size--;
/*  512 */     this.modCount++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeAllNodes() {
/*  519 */     this.header.next = this.header;
/*  520 */     this.header.previous = this.header;
/*  521 */     this.size = 0;
/*  522 */     this.modCount++;
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
/*      */   protected Node<E> getNode(int index, boolean endMarkerAllowed) throws IndexOutOfBoundsException {
/*      */     Node<E> node;
/*  538 */     if (index < 0) {
/*  539 */       throw new IndexOutOfBoundsException("Couldn't get the node: index (" + index + ") less than zero.");
/*      */     }
/*      */     
/*  542 */     if (!endMarkerAllowed && index == this.size) {
/*  543 */       throw new IndexOutOfBoundsException("Couldn't get the node: index (" + index + ") is the size of the list.");
/*      */     }
/*      */     
/*  546 */     if (index > this.size) {
/*  547 */       throw new IndexOutOfBoundsException("Couldn't get the node: index (" + index + ") greater than the size of the " + "list (" + this.size + ").");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  553 */     if (index < this.size / 2) {
/*      */       
/*  555 */       node = this.header.next;
/*  556 */       for (int currentIndex = 0; currentIndex < index; currentIndex++) {
/*  557 */         node = node.next;
/*      */       }
/*      */     } else {
/*      */       
/*  561 */       node = this.header;
/*  562 */       for (int currentIndex = this.size; currentIndex > index; currentIndex--) {
/*  563 */         node = node.previous;
/*      */       }
/*      */     } 
/*  566 */     return node;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Iterator<E> createSubListIterator(LinkedSubList<E> subList) {
/*  577 */     return createSubListListIterator(subList, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ListIterator<E> createSubListListIterator(LinkedSubList<E> subList, int fromIndex) {
/*  588 */     return new LinkedSubListIterator<E>(subList, fromIndex);
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
/*      */   protected void doWriteObject(ObjectOutputStream outputStream) throws IOException {
/*  603 */     outputStream.writeInt(size());
/*  604 */     for (E e : this) {
/*  605 */       outputStream.writeObject(e);
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doReadObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/*  621 */     init();
/*  622 */     int size = inputStream.readInt();
/*  623 */     for (int i = 0; i < size; i++) {
/*  624 */       add((E)inputStream.readObject());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class Node<E>
/*      */   {
/*      */     protected Node<E> previous;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Node<E> next;
/*      */ 
/*      */ 
/*      */     
/*      */     protected E value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Node() {
/*  649 */       this.previous = this;
/*  650 */       this.next = this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Node(E value) {
/*  660 */       this.value = value;
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
/*      */     protected Node(Node<E> previous, Node<E> next, E value) {
/*  672 */       this.previous = previous;
/*  673 */       this.next = next;
/*  674 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected E getValue() {
/*  684 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void setValue(E value) {
/*  694 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Node<E> getPreviousNode() {
/*  704 */       return this.previous;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void setPreviousNode(Node<E> previous) {
/*  714 */       this.previous = previous;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Node<E> getNextNode() {
/*  724 */       return this.next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void setNextNode(Node<E> next) {
/*  734 */       this.next = next;
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
/*      */   protected static class LinkedListIterator<E>
/*      */     implements ListIterator<E>, OrderedIterator<E>
/*      */   {
/*      */     protected final AbstractLinkedList<E> parent;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected AbstractLinkedList.Node<E> next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int nextIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected AbstractLinkedList.Node<E> current;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int expectedModCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected LinkedListIterator(AbstractLinkedList<E> parent, int fromIndex) throws IndexOutOfBoundsException {
/*  786 */       this.parent = parent;
/*  787 */       this.expectedModCount = parent.modCount;
/*  788 */       this.next = parent.getNode(fromIndex, true);
/*  789 */       this.nextIndex = fromIndex;
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
/*  800 */       if (this.parent.modCount != this.expectedModCount) {
/*  801 */         throw new ConcurrentModificationException();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected AbstractLinkedList.Node<E> getLastNodeReturned() throws IllegalStateException {
/*  813 */       if (this.current == null) {
/*  814 */         throw new IllegalStateException();
/*      */       }
/*  816 */       return this.current;
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  820 */       return (this.next != this.parent.header);
/*      */     }
/*      */     
/*      */     public E next() {
/*  824 */       checkModCount();
/*  825 */       if (!hasNext()) {
/*  826 */         throw new NoSuchElementException("No element at index " + this.nextIndex + ".");
/*      */       }
/*  828 */       E value = this.next.getValue();
/*  829 */       this.current = this.next;
/*  830 */       this.next = this.next.next;
/*  831 */       this.nextIndex++;
/*  832 */       return value;
/*      */     }
/*      */     
/*      */     public boolean hasPrevious() {
/*  836 */       return (this.next.previous != this.parent.header);
/*      */     }
/*      */     
/*      */     public E previous() {
/*  840 */       checkModCount();
/*  841 */       if (!hasPrevious()) {
/*  842 */         throw new NoSuchElementException("Already at start of list.");
/*      */       }
/*  844 */       this.next = this.next.previous;
/*  845 */       E value = this.next.getValue();
/*  846 */       this.current = this.next;
/*  847 */       this.nextIndex--;
/*  848 */       return value;
/*      */     }
/*      */     
/*      */     public int nextIndex() {
/*  852 */       return this.nextIndex;
/*      */     }
/*      */ 
/*      */     
/*      */     public int previousIndex() {
/*  857 */       return nextIndex() - 1;
/*      */     }
/*      */     
/*      */     public void remove() {
/*  861 */       checkModCount();
/*  862 */       if (this.current == this.next) {
/*      */         
/*  864 */         this.next = this.next.next;
/*  865 */         this.parent.removeNode(getLastNodeReturned());
/*      */       } else {
/*      */         
/*  868 */         this.parent.removeNode(getLastNodeReturned());
/*  869 */         this.nextIndex--;
/*      */       } 
/*  871 */       this.current = null;
/*  872 */       this.expectedModCount++;
/*      */     }
/*      */     
/*      */     public void set(E obj) {
/*  876 */       checkModCount();
/*  877 */       getLastNodeReturned().setValue(obj);
/*      */     }
/*      */     
/*      */     public void add(E obj) {
/*  881 */       checkModCount();
/*  882 */       this.parent.addNodeBefore(this.next, obj);
/*  883 */       this.current = null;
/*  884 */       this.nextIndex++;
/*  885 */       this.expectedModCount++;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class LinkedSubListIterator<E>
/*      */     extends LinkedListIterator<E>
/*      */   {
/*      */     protected final AbstractLinkedList.LinkedSubList<E> sub;
/*      */ 
/*      */ 
/*      */     
/*      */     protected LinkedSubListIterator(AbstractLinkedList.LinkedSubList<E> sub, int startIndex) {
/*  900 */       super(sub.parent, startIndex + sub.offset);
/*  901 */       this.sub = sub;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  906 */       return (nextIndex() < this.sub.size);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasPrevious() {
/*  911 */       return (previousIndex() >= 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public int nextIndex() {
/*  916 */       return super.nextIndex() - this.sub.offset;
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(E obj) {
/*  921 */       super.add(obj);
/*  922 */       this.sub.expectedModCount = this.parent.modCount;
/*  923 */       this.sub.size++;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/*  928 */       super.remove();
/*  929 */       this.sub.expectedModCount = this.parent.modCount;
/*  930 */       this.sub.size--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class LinkedSubList<E>
/*      */     extends AbstractList<E>
/*      */   {
/*      */     AbstractLinkedList<E> parent;
/*      */     
/*      */     int offset;
/*      */     
/*      */     int size;
/*      */     
/*      */     int expectedModCount;
/*      */ 
/*      */     
/*      */     protected LinkedSubList(AbstractLinkedList<E> parent, int fromIndex, int toIndex) {
/*  949 */       if (fromIndex < 0) {
/*  950 */         throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
/*      */       }
/*  952 */       if (toIndex > parent.size()) {
/*  953 */         throw new IndexOutOfBoundsException("toIndex = " + toIndex);
/*      */       }
/*  955 */       if (fromIndex > toIndex) {
/*  956 */         throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
/*      */       }
/*  958 */       this.parent = parent;
/*  959 */       this.offset = fromIndex;
/*  960 */       this.size = toIndex - fromIndex;
/*  961 */       this.expectedModCount = parent.modCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  966 */       checkModCount();
/*  967 */       return this.size;
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  972 */       rangeCheck(index, this.size);
/*  973 */       checkModCount();
/*  974 */       return this.parent.get(index + this.offset);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E obj) {
/*  979 */       rangeCheck(index, this.size + 1);
/*  980 */       checkModCount();
/*  981 */       this.parent.add(index + this.offset, obj);
/*  982 */       this.expectedModCount = this.parent.modCount;
/*  983 */       this.size++;
/*  984 */       this.modCount++;
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/*  989 */       rangeCheck(index, this.size);
/*  990 */       checkModCount();
/*  991 */       E result = this.parent.remove(index + this.offset);
/*  992 */       this.expectedModCount = this.parent.modCount;
/*  993 */       this.size--;
/*  994 */       this.modCount++;
/*  995 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> coll) {
/* 1000 */       return addAll(this.size, coll);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> coll) {
/* 1005 */       rangeCheck(index, this.size + 1);
/* 1006 */       int cSize = coll.size();
/* 1007 */       if (cSize == 0) {
/* 1008 */         return false;
/*      */       }
/*      */       
/* 1011 */       checkModCount();
/* 1012 */       this.parent.addAll(this.offset + index, coll);
/* 1013 */       this.expectedModCount = this.parent.modCount;
/* 1014 */       this.size += cSize;
/* 1015 */       this.modCount++;
/* 1016 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E obj) {
/* 1021 */       rangeCheck(index, this.size);
/* 1022 */       checkModCount();
/* 1023 */       return this.parent.set(index + this.offset, obj);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1028 */       checkModCount();
/* 1029 */       Iterator<E> it = iterator();
/* 1030 */       while (it.hasNext()) {
/* 1031 */         it.next();
/* 1032 */         it.remove();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1038 */       checkModCount();
/* 1039 */       return this.parent.createSubListIterator(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<E> listIterator(int index) {
/* 1044 */       rangeCheck(index, this.size + 1);
/* 1045 */       checkModCount();
/* 1046 */       return this.parent.createSubListListIterator(this, index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<E> subList(int fromIndexInclusive, int toIndexExclusive) {
/* 1051 */       return new LinkedSubList(this.parent, fromIndexInclusive + this.offset, toIndexExclusive + this.offset);
/*      */     }
/*      */     
/*      */     protected void rangeCheck(int index, int beyond) {
/* 1055 */       if (index < 0 || index >= beyond) {
/* 1056 */         throw new IndexOutOfBoundsException("Index '" + index + "' out of bounds for size '" + this.size + "'");
/*      */       }
/*      */     }
/*      */     
/*      */     protected void checkModCount() {
/* 1061 */       if (this.parent.modCount != this.expectedModCount)
/* 1062 */         throw new ConcurrentModificationException(); 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\AbstractLinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */