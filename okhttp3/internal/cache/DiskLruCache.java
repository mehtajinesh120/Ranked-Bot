/*      */ package okhttp3.internal.cache;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.Flushable;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.annotation.Nullable;
/*      */ import okhttp3.internal.Util;
/*      */ import okhttp3.internal.io.FileSystem;
/*      */ import okhttp3.internal.platform.Platform;
/*      */ import okio.BufferedSink;
/*      */ import okio.BufferedSource;
/*      */ import okio.Okio;
/*      */ import okio.Sink;
/*      */ import okio.Source;
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
/*      */ public final class DiskLruCache
/*      */   implements Closeable, Flushable
/*      */ {
/*      */   static final String JOURNAL_FILE = "journal";
/*      */   static final String JOURNAL_FILE_TEMP = "journal.tmp";
/*      */   static final String JOURNAL_FILE_BACKUP = "journal.bkp";
/*      */   static final String MAGIC = "libcore.io.DiskLruCache";
/*      */   static final String VERSION_1 = "1";
/*      */   static final long ANY_SEQUENCE_NUMBER = -1L;
/*   94 */   static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String CLEAN = "CLEAN";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DIRTY = "DIRTY";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String REMOVE = "REMOVE";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String READ = "READ";
/*      */ 
/*      */ 
/*      */   
/*      */   final FileSystem fileSystem;
/*      */ 
/*      */ 
/*      */   
/*      */   final File directory;
/*      */ 
/*      */ 
/*      */   
/*      */   private final File journalFile;
/*      */ 
/*      */ 
/*      */   
/*      */   private final File journalFileTmp;
/*      */ 
/*      */ 
/*      */   
/*      */   private final File journalFileBackup;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int appVersion;
/*      */ 
/*      */ 
/*      */   
/*      */   private long maxSize;
/*      */ 
/*      */ 
/*      */   
/*      */   final int valueCount;
/*      */ 
/*      */ 
/*      */   
/*  148 */   private long size = 0L;
/*      */   BufferedSink journalWriter;
/*  150 */   final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75F, true);
/*      */ 
/*      */   
/*      */   int redundantOpCount;
/*      */   
/*      */   boolean hasJournalErrors;
/*      */   
/*      */   boolean initialized;
/*      */   
/*      */   boolean closed;
/*      */   
/*      */   boolean mostRecentTrimFailed;
/*      */   
/*      */   boolean mostRecentRebuildFailed;
/*      */   
/*  165 */   private long nextSequenceNumber = 0L;
/*      */   
/*      */   private final Executor executor;
/*      */   
/*  169 */   private final Runnable cleanupRunnable = new Runnable() {
/*      */       public void run() {
/*  171 */         synchronized (DiskLruCache.this) {
/*  172 */           if (((!DiskLruCache.this.initialized ? 1 : 0) | DiskLruCache.this.closed) != 0) {
/*      */             return;
/*      */           }
/*      */           
/*      */           try {
/*  177 */             DiskLruCache.this.trimToSize();
/*  178 */           } catch (IOException ignored) {
/*  179 */             DiskLruCache.this.mostRecentTrimFailed = true;
/*      */           } 
/*      */           
/*      */           try {
/*  183 */             if (DiskLruCache.this.journalRebuildRequired()) {
/*  184 */               DiskLruCache.this.rebuildJournal();
/*  185 */               DiskLruCache.this.redundantOpCount = 0;
/*      */             } 
/*  187 */           } catch (IOException e) {
/*  188 */             DiskLruCache.this.mostRecentRebuildFailed = true;
/*  189 */             DiskLruCache.this.journalWriter = Okio.buffer(Okio.blackhole());
/*      */           } 
/*      */         } 
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   DiskLruCache(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize, Executor executor) {
/*  197 */     this.fileSystem = fileSystem;
/*  198 */     this.directory = directory;
/*  199 */     this.appVersion = appVersion;
/*  200 */     this.journalFile = new File(directory, "journal");
/*  201 */     this.journalFileTmp = new File(directory, "journal.tmp");
/*  202 */     this.journalFileBackup = new File(directory, "journal.bkp");
/*  203 */     this.valueCount = valueCount;
/*  204 */     this.maxSize = maxSize;
/*  205 */     this.executor = executor;
/*      */   }
/*      */   
/*      */   public synchronized void initialize() throws IOException {
/*  209 */     assert Thread.holdsLock(this);
/*      */     
/*  211 */     if (this.initialized) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  216 */     if (this.fileSystem.exists(this.journalFileBackup))
/*      */     {
/*  218 */       if (this.fileSystem.exists(this.journalFile)) {
/*  219 */         this.fileSystem.delete(this.journalFileBackup);
/*      */       } else {
/*  221 */         this.fileSystem.rename(this.journalFileBackup, this.journalFile);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  226 */     if (this.fileSystem.exists(this.journalFile)) {
/*      */       try {
/*  228 */         readJournal();
/*  229 */         processJournal();
/*  230 */         this.initialized = true;
/*      */         return;
/*  232 */       } catch (IOException journalIsCorrupt) {
/*  233 */         Platform.get().log(5, "DiskLruCache " + this.directory + " is corrupt: " + journalIsCorrupt
/*  234 */             .getMessage() + ", removing", journalIsCorrupt);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/*  240 */           delete();
/*      */         } finally {
/*  242 */           this.closed = false;
/*      */         } 
/*      */       } 
/*      */     }
/*  246 */     rebuildJournal();
/*      */     
/*  248 */     this.initialized = true;
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
/*      */   public static DiskLruCache create(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize) {
/*  261 */     if (maxSize <= 0L) {
/*  262 */       throw new IllegalArgumentException("maxSize <= 0");
/*      */     }
/*  264 */     if (valueCount <= 0) {
/*  265 */       throw new IllegalArgumentException("valueCount <= 0");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  270 */     Executor executor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Util.threadFactory("OkHttp DiskLruCache", true));
/*      */     
/*  272 */     return new DiskLruCache(fileSystem, directory, appVersion, valueCount, maxSize, executor);
/*      */   }
/*      */   
/*      */   private void readJournal() throws IOException {
/*  276 */     BufferedSource source = Okio.buffer(this.fileSystem.source(this.journalFile)); Throwable throwable = null; try {
/*  277 */       String magic = source.readUtf8LineStrict();
/*  278 */       String version = source.readUtf8LineStrict();
/*  279 */       String appVersionString = source.readUtf8LineStrict();
/*  280 */       String valueCountString = source.readUtf8LineStrict();
/*  281 */       String blank = source.readUtf8LineStrict();
/*  282 */       if (!"libcore.io.DiskLruCache".equals(magic) || 
/*  283 */         !"1".equals(version) || 
/*  284 */         !Integer.toString(this.appVersion).equals(appVersionString) || 
/*  285 */         !Integer.toString(this.valueCount).equals(valueCountString) || 
/*  286 */         !"".equals(blank)) {
/*  287 */         throw new IOException("unexpected journal header: [" + magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
/*      */       }
/*      */ 
/*      */       
/*  291 */       int lineCount = 0;
/*      */     } catch (Throwable throwable1) {
/*      */       throwable = throwable1 = null;
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
/*      */       throw throwable1;
/*      */     } finally {
/*  308 */       if (source != null) $closeResource(throwable, (AutoCloseable)source); 
/*      */     } 
/*      */   }
/*      */   private BufferedSink newJournalWriter() throws FileNotFoundException {
/*  312 */     Sink fileSink = this.fileSystem.appendingSink(this.journalFile);
/*  313 */     FaultHidingSink faultHidingSink = new FaultHidingSink(fileSink) {
/*      */         protected void onException(IOException e) {
/*  315 */           assert Thread.holdsLock(DiskLruCache.this);
/*  316 */           DiskLruCache.this.hasJournalErrors = true;
/*      */         }
/*      */       };
/*  319 */     return Okio.buffer((Sink)faultHidingSink);
/*      */   }
/*      */   private void readJournalLine(String line) throws IOException {
/*      */     String key;
/*  323 */     int firstSpace = line.indexOf(' ');
/*  324 */     if (firstSpace == -1) {
/*  325 */       throw new IOException("unexpected journal line: " + line);
/*      */     }
/*      */     
/*  328 */     int keyBegin = firstSpace + 1;
/*  329 */     int secondSpace = line.indexOf(' ', keyBegin);
/*      */     
/*  331 */     if (secondSpace == -1) {
/*  332 */       key = line.substring(keyBegin);
/*  333 */       if (firstSpace == "REMOVE".length() && line.startsWith("REMOVE")) {
/*  334 */         this.lruEntries.remove(key);
/*      */         return;
/*      */       } 
/*      */     } else {
/*  338 */       key = line.substring(keyBegin, secondSpace);
/*      */     } 
/*      */     
/*  341 */     Entry entry = this.lruEntries.get(key);
/*  342 */     if (entry == null) {
/*  343 */       entry = new Entry(key);
/*  344 */       this.lruEntries.put(key, entry);
/*      */     } 
/*      */     
/*  347 */     if (secondSpace != -1 && firstSpace == "CLEAN".length() && line.startsWith("CLEAN")) {
/*  348 */       String[] parts = line.substring(secondSpace + 1).split(" ");
/*  349 */       entry.readable = true;
/*  350 */       entry.currentEditor = null;
/*  351 */       entry.setLengths(parts);
/*  352 */     } else if (secondSpace == -1 && firstSpace == "DIRTY".length() && line.startsWith("DIRTY")) {
/*  353 */       entry.currentEditor = new Editor(entry);
/*  354 */     } else if (secondSpace != -1 || firstSpace != "READ".length() || !line.startsWith("READ")) {
/*      */ 
/*      */       
/*  357 */       throw new IOException("unexpected journal line: " + line);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processJournal() throws IOException {
/*  366 */     this.fileSystem.delete(this.journalFileTmp);
/*  367 */     for (Iterator<Entry> i = this.lruEntries.values().iterator(); i.hasNext(); ) {
/*  368 */       Entry entry = i.next();
/*  369 */       if (entry.currentEditor == null) {
/*  370 */         for (int j = 0; j < this.valueCount; j++)
/*  371 */           this.size += entry.lengths[j]; 
/*      */         continue;
/*      */       } 
/*  374 */       entry.currentEditor = null;
/*  375 */       for (int t = 0; t < this.valueCount; t++) {
/*  376 */         this.fileSystem.delete(entry.cleanFiles[t]);
/*  377 */         this.fileSystem.delete(entry.dirtyFiles[t]);
/*      */       } 
/*  379 */       i.remove();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void rebuildJournal() throws IOException {
/*  389 */     if (this.journalWriter != null) {
/*  390 */       this.journalWriter.close();
/*      */     }
/*      */     
/*  393 */     BufferedSink writer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp)); Throwable throwable = null; 
/*  394 */     try { writer.writeUtf8("libcore.io.DiskLruCache").writeByte(10);
/*  395 */       writer.writeUtf8("1").writeByte(10);
/*  396 */       writer.writeDecimalLong(this.appVersion).writeByte(10);
/*  397 */       writer.writeDecimalLong(this.valueCount).writeByte(10);
/*  398 */       writer.writeByte(10);
/*      */       
/*  400 */       for (Entry entry : this.lruEntries.values()) {
/*  401 */         if (entry.currentEditor != null) {
/*  402 */           writer.writeUtf8("DIRTY").writeByte(32);
/*  403 */           writer.writeUtf8(entry.key);
/*  404 */           writer.writeByte(10); continue;
/*      */         } 
/*  406 */         writer.writeUtf8("CLEAN").writeByte(32);
/*  407 */         writer.writeUtf8(entry.key);
/*  408 */         entry.writeLengths(writer);
/*  409 */         writer.writeByte(10);
/*      */       }  }
/*      */     catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/*  412 */     finally { if (writer != null) $closeResource(throwable, (AutoCloseable)writer);  }
/*      */     
/*  414 */     if (this.fileSystem.exists(this.journalFile)) {
/*  415 */       this.fileSystem.rename(this.journalFile, this.journalFileBackup);
/*      */     }
/*  417 */     this.fileSystem.rename(this.journalFileTmp, this.journalFile);
/*  418 */     this.fileSystem.delete(this.journalFileBackup);
/*      */     
/*  420 */     this.journalWriter = newJournalWriter();
/*  421 */     this.hasJournalErrors = false;
/*  422 */     this.mostRecentRebuildFailed = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Snapshot get(String key) throws IOException {
/*  430 */     initialize();
/*      */     
/*  432 */     checkNotClosed();
/*  433 */     validateKey(key);
/*  434 */     Entry entry = this.lruEntries.get(key);
/*  435 */     if (entry == null || !entry.readable) return null;
/*      */     
/*  437 */     Snapshot snapshot = entry.snapshot();
/*  438 */     if (snapshot == null) return null;
/*      */     
/*  440 */     this.redundantOpCount++;
/*  441 */     this.journalWriter.writeUtf8("READ").writeByte(32).writeUtf8(key).writeByte(10);
/*  442 */     if (journalRebuildRequired()) {
/*  443 */       this.executor.execute(this.cleanupRunnable);
/*      */     }
/*      */     
/*  446 */     return snapshot;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Editor edit(String key) throws IOException {
/*  453 */     return edit(key, -1L);
/*      */   }
/*      */   
/*      */   synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
/*  457 */     initialize();
/*      */     
/*  459 */     checkNotClosed();
/*  460 */     validateKey(key);
/*  461 */     Entry entry = this.lruEntries.get(key);
/*  462 */     if (expectedSequenceNumber != -1L && (entry == null || entry.sequenceNumber != expectedSequenceNumber))
/*      */     {
/*  464 */       return null;
/*      */     }
/*  466 */     if (entry != null && entry.currentEditor != null) {
/*  467 */       return null;
/*      */     }
/*  469 */     if (this.mostRecentTrimFailed || this.mostRecentRebuildFailed) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  475 */       this.executor.execute(this.cleanupRunnable);
/*  476 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  480 */     this.journalWriter.writeUtf8("DIRTY").writeByte(32).writeUtf8(key).writeByte(10);
/*  481 */     this.journalWriter.flush();
/*      */     
/*  483 */     if (this.hasJournalErrors) {
/*  484 */       return null;
/*      */     }
/*      */     
/*  487 */     if (entry == null) {
/*  488 */       entry = new Entry(key);
/*  489 */       this.lruEntries.put(key, entry);
/*      */     } 
/*  491 */     Editor editor = new Editor(entry);
/*  492 */     entry.currentEditor = editor;
/*  493 */     return editor;
/*      */   }
/*      */ 
/*      */   
/*      */   public File getDirectory() {
/*  498 */     return this.directory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized long getMaxSize() {
/*  505 */     return this.maxSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setMaxSize(long maxSize) {
/*  513 */     this.maxSize = maxSize;
/*  514 */     if (this.initialized) {
/*  515 */       this.executor.execute(this.cleanupRunnable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized long size() throws IOException {
/*  524 */     initialize();
/*  525 */     return this.size;
/*      */   }
/*      */   
/*      */   synchronized void completeEdit(Editor editor, boolean success) throws IOException {
/*  529 */     Entry entry = editor.entry;
/*  530 */     if (entry.currentEditor != editor) {
/*  531 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     
/*  535 */     if (success && !entry.readable) {
/*  536 */       for (int j = 0; j < this.valueCount; j++) {
/*  537 */         if (!editor.written[j]) {
/*  538 */           editor.abort();
/*  539 */           throw new IllegalStateException("Newly created entry didn't create value for index " + j);
/*      */         } 
/*  541 */         if (!this.fileSystem.exists(entry.dirtyFiles[j])) {
/*  542 */           editor.abort();
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     }
/*  548 */     for (int i = 0; i < this.valueCount; i++) {
/*  549 */       File dirty = entry.dirtyFiles[i];
/*  550 */       if (success) {
/*  551 */         if (this.fileSystem.exists(dirty)) {
/*  552 */           File clean = entry.cleanFiles[i];
/*  553 */           this.fileSystem.rename(dirty, clean);
/*  554 */           long oldLength = entry.lengths[i];
/*  555 */           long newLength = this.fileSystem.size(clean);
/*  556 */           entry.lengths[i] = newLength;
/*  557 */           this.size = this.size - oldLength + newLength;
/*      */         } 
/*      */       } else {
/*  560 */         this.fileSystem.delete(dirty);
/*      */       } 
/*      */     } 
/*      */     
/*  564 */     this.redundantOpCount++;
/*  565 */     entry.currentEditor = null;
/*  566 */     if (entry.readable | success) {
/*  567 */       entry.readable = true;
/*  568 */       this.journalWriter.writeUtf8("CLEAN").writeByte(32);
/*  569 */       this.journalWriter.writeUtf8(entry.key);
/*  570 */       entry.writeLengths(this.journalWriter);
/*  571 */       this.journalWriter.writeByte(10);
/*  572 */       if (success) {
/*  573 */         entry.sequenceNumber = this.nextSequenceNumber++;
/*      */       }
/*      */     } else {
/*  576 */       this.lruEntries.remove(entry.key);
/*  577 */       this.journalWriter.writeUtf8("REMOVE").writeByte(32);
/*  578 */       this.journalWriter.writeUtf8(entry.key);
/*  579 */       this.journalWriter.writeByte(10);
/*      */     } 
/*  581 */     this.journalWriter.flush();
/*      */     
/*  583 */     if (this.size > this.maxSize || journalRebuildRequired()) {
/*  584 */       this.executor.execute(this.cleanupRunnable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean journalRebuildRequired() {
/*  593 */     int redundantOpCompactThreshold = 2000;
/*  594 */     return (this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries
/*  595 */       .size());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean remove(String key) throws IOException {
/*  605 */     initialize();
/*      */     
/*  607 */     checkNotClosed();
/*  608 */     validateKey(key);
/*  609 */     Entry entry = this.lruEntries.get(key);
/*  610 */     if (entry == null) return false; 
/*  611 */     boolean removed = removeEntry(entry);
/*  612 */     if (removed && this.size <= this.maxSize) this.mostRecentTrimFailed = false; 
/*  613 */     return removed;
/*      */   }
/*      */   
/*      */   boolean removeEntry(Entry entry) throws IOException {
/*  617 */     if (entry.currentEditor != null) {
/*  618 */       entry.currentEditor.detach();
/*      */     }
/*      */     
/*  621 */     for (int i = 0; i < this.valueCount; i++) {
/*  622 */       this.fileSystem.delete(entry.cleanFiles[i]);
/*  623 */       this.size -= entry.lengths[i];
/*  624 */       entry.lengths[i] = 0L;
/*      */     } 
/*      */     
/*  627 */     this.redundantOpCount++;
/*  628 */     this.journalWriter.writeUtf8("REMOVE").writeByte(32).writeUtf8(entry.key).writeByte(10);
/*  629 */     this.lruEntries.remove(entry.key);
/*      */     
/*  631 */     if (journalRebuildRequired()) {
/*  632 */       this.executor.execute(this.cleanupRunnable);
/*      */     }
/*      */     
/*  635 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isClosed() {
/*  640 */     return this.closed;
/*      */   }
/*      */   
/*      */   private synchronized void checkNotClosed() {
/*  644 */     if (isClosed()) {
/*  645 */       throw new IllegalStateException("cache is closed");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void flush() throws IOException {
/*  651 */     if (!this.initialized)
/*      */       return; 
/*  653 */     checkNotClosed();
/*  654 */     trimToSize();
/*  655 */     this.journalWriter.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void close() throws IOException {
/*  660 */     if (!this.initialized || this.closed) {
/*  661 */       this.closed = true;
/*      */       
/*      */       return;
/*      */     } 
/*  665 */     for (Entry entry : (Entry[])this.lruEntries.values().toArray((Object[])new Entry[this.lruEntries.size()])) {
/*  666 */       if (entry.currentEditor != null) {
/*  667 */         entry.currentEditor.abort();
/*      */       }
/*      */     } 
/*  670 */     trimToSize();
/*  671 */     this.journalWriter.close();
/*  672 */     this.journalWriter = null;
/*  673 */     this.closed = true;
/*      */   }
/*      */   
/*      */   void trimToSize() throws IOException {
/*  677 */     while (this.size > this.maxSize) {
/*  678 */       Entry toEvict = this.lruEntries.values().iterator().next();
/*  679 */       removeEntry(toEvict);
/*      */     } 
/*  681 */     this.mostRecentTrimFailed = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void delete() throws IOException {
/*  689 */     close();
/*  690 */     this.fileSystem.deleteContents(this.directory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void evictAll() throws IOException {
/*  698 */     initialize();
/*      */     
/*  700 */     for (Entry entry : (Entry[])this.lruEntries.values().toArray((Object[])new Entry[this.lruEntries.size()])) {
/*  701 */       removeEntry(entry);
/*      */     }
/*  703 */     this.mostRecentTrimFailed = false;
/*      */   }
/*      */   
/*      */   private void validateKey(String key) {
/*  707 */     Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
/*  708 */     if (!matcher.matches()) {
/*  709 */       throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + key + "\"");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Iterator<Snapshot> snapshots() throws IOException {
/*  730 */     initialize();
/*  731 */     return new Iterator<Snapshot>()
/*      */       {
/*  733 */         final Iterator<DiskLruCache.Entry> delegate = (new ArrayList<>(DiskLruCache.this.lruEntries.values())).iterator();
/*      */ 
/*      */         
/*      */         DiskLruCache.Snapshot nextSnapshot;
/*      */         
/*      */         DiskLruCache.Snapshot removeSnapshot;
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*  742 */           if (this.nextSnapshot != null) return true;
/*      */           
/*  744 */           synchronized (DiskLruCache.this) {
/*      */             
/*  746 */             if (DiskLruCache.this.closed) return false;
/*      */             
/*  748 */             while (this.delegate.hasNext()) {
/*  749 */               DiskLruCache.Entry entry = this.delegate.next();
/*  750 */               DiskLruCache.Snapshot snapshot = entry.snapshot();
/*  751 */               if (snapshot == null)
/*  752 */                 continue;  this.nextSnapshot = snapshot;
/*  753 */               return true;
/*      */             } 
/*      */           } 
/*      */           
/*  757 */           return false;
/*      */         }
/*      */         
/*      */         public DiskLruCache.Snapshot next() {
/*  761 */           if (!hasNext()) throw new NoSuchElementException(); 
/*  762 */           this.removeSnapshot = this.nextSnapshot;
/*  763 */           this.nextSnapshot = null;
/*  764 */           return this.removeSnapshot;
/*      */         }
/*      */         
/*      */         public void remove() {
/*  768 */           if (this.removeSnapshot == null) throw new IllegalStateException("remove() before next()"); 
/*      */           try {
/*  770 */             DiskLruCache.this.remove(this.removeSnapshot.key);
/*  771 */           } catch (IOException iOException) {
/*      */ 
/*      */           
/*      */           } finally {
/*  775 */             this.removeSnapshot = null;
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public final class Snapshot
/*      */     implements Closeable {
/*      */     private final String key;
/*      */     private final long sequenceNumber;
/*      */     private final Source[] sources;
/*      */     private final long[] lengths;
/*      */     
/*      */     Snapshot(String key, long sequenceNumber, Source[] sources, long[] lengths) {
/*  789 */       this.key = key;
/*  790 */       this.sequenceNumber = sequenceNumber;
/*  791 */       this.sources = sources;
/*  792 */       this.lengths = lengths;
/*      */     }
/*      */     
/*      */     public String key() {
/*  796 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public DiskLruCache.Editor edit() throws IOException {
/*  804 */       return DiskLruCache.this.edit(this.key, this.sequenceNumber);
/*      */     }
/*      */ 
/*      */     
/*      */     public Source getSource(int index) {
/*  809 */       return this.sources[index];
/*      */     }
/*      */ 
/*      */     
/*      */     public long getLength(int index) {
/*  814 */       return this.lengths[index];
/*      */     }
/*      */     
/*      */     public void close() {
/*  818 */       for (Source in : this.sources) {
/*  819 */         Util.closeQuietly((Closeable)in);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final class Editor
/*      */   {
/*      */     final DiskLruCache.Entry entry;
/*      */     final boolean[] written;
/*      */     private boolean done;
/*      */     
/*      */     Editor(DiskLruCache.Entry entry) {
/*  831 */       this.entry = entry;
/*  832 */       this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void detach() {
/*  842 */       if (this.entry.currentEditor == this) {
/*  843 */         for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
/*      */           try {
/*  845 */             DiskLruCache.this.fileSystem.delete(this.entry.dirtyFiles[i]);
/*  846 */           } catch (IOException iOException) {}
/*      */         } 
/*      */ 
/*      */         
/*  850 */         this.entry.currentEditor = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Source newSource(int index) {
/*  859 */       synchronized (DiskLruCache.this) {
/*  860 */         if (this.done) {
/*  861 */           throw new IllegalStateException();
/*      */         }
/*  863 */         if (!this.entry.readable || this.entry.currentEditor != this) {
/*  864 */           return null;
/*      */         }
/*      */         try {
/*  867 */           return DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[index]);
/*  868 */         } catch (FileNotFoundException e) {
/*  869 */           return null;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Sink newSink(int index) {
/*  880 */       synchronized (DiskLruCache.this) {
/*  881 */         Sink sink; if (this.done) {
/*  882 */           throw new IllegalStateException();
/*      */         }
/*  884 */         if (this.entry.currentEditor != this) {
/*  885 */           return Okio.blackhole();
/*      */         }
/*  887 */         if (!this.entry.readable) {
/*  888 */           this.written[index] = true;
/*      */         }
/*  890 */         File dirtyFile = this.entry.dirtyFiles[index];
/*      */         
/*      */         try {
/*  893 */           sink = DiskLruCache.this.fileSystem.sink(dirtyFile);
/*  894 */         } catch (FileNotFoundException e) {
/*  895 */           return Okio.blackhole();
/*      */         } 
/*  897 */         return (Sink)new FaultHidingSink(sink) {
/*      */             protected void onException(IOException e) {
/*  899 */               synchronized (DiskLruCache.this) {
/*  900 */                 DiskLruCache.Editor.this.detach();
/*      */               } 
/*      */             }
/*      */           };
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void commit() throws IOException {
/*  912 */       synchronized (DiskLruCache.this) {
/*  913 */         if (this.done) {
/*  914 */           throw new IllegalStateException();
/*      */         }
/*  916 */         if (this.entry.currentEditor == this) {
/*  917 */           DiskLruCache.this.completeEdit(this, true);
/*      */         }
/*  919 */         this.done = true;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void abort() throws IOException {
/*  928 */       synchronized (DiskLruCache.this) {
/*  929 */         if (this.done) {
/*  930 */           throw new IllegalStateException();
/*      */         }
/*  932 */         if (this.entry.currentEditor == this) {
/*  933 */           DiskLruCache.this.completeEdit(this, false);
/*      */         }
/*  935 */         this.done = true;
/*      */       } 
/*      */     }
/*      */     
/*      */     public void abortUnlessCommitted() {
/*  940 */       synchronized (DiskLruCache.this) {
/*  941 */         if (!this.done && this.entry.currentEditor == this) {
/*      */           try {
/*  943 */             DiskLruCache.this.completeEdit(this, false);
/*  944 */           } catch (IOException iOException) {}
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final class Entry
/*      */   {
/*      */     final String key;
/*      */     
/*      */     final long[] lengths;
/*      */     
/*      */     final File[] cleanFiles;
/*      */     
/*      */     final File[] dirtyFiles;
/*      */     
/*      */     boolean readable;
/*      */     
/*      */     DiskLruCache.Editor currentEditor;
/*      */     
/*      */     long sequenceNumber;
/*      */ 
/*      */     
/*      */     Entry(String key) {
/*  969 */       this.key = key;
/*      */       
/*  971 */       this.lengths = new long[DiskLruCache.this.valueCount];
/*  972 */       this.cleanFiles = new File[DiskLruCache.this.valueCount];
/*  973 */       this.dirtyFiles = new File[DiskLruCache.this.valueCount];
/*      */ 
/*      */       
/*  976 */       StringBuilder fileBuilder = (new StringBuilder(key)).append('.');
/*  977 */       int truncateTo = fileBuilder.length();
/*  978 */       for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
/*  979 */         fileBuilder.append(i);
/*  980 */         this.cleanFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
/*  981 */         fileBuilder.append(".tmp");
/*  982 */         this.dirtyFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
/*  983 */         fileBuilder.setLength(truncateTo);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void setLengths(String[] strings) throws IOException {
/*  989 */       if (strings.length != DiskLruCache.this.valueCount) {
/*  990 */         throw invalidLengths(strings);
/*      */       }
/*      */       
/*      */       try {
/*  994 */         for (int i = 0; i < strings.length; i++) {
/*  995 */           this.lengths[i] = Long.parseLong(strings[i]);
/*      */         }
/*  997 */       } catch (NumberFormatException e) {
/*  998 */         throw invalidLengths(strings);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void writeLengths(BufferedSink writer) throws IOException {
/* 1004 */       for (long length : this.lengths) {
/* 1005 */         writer.writeByte(32).writeDecimalLong(length);
/*      */       }
/*      */     }
/*      */     
/*      */     private IOException invalidLengths(String[] strings) throws IOException {
/* 1010 */       throw new IOException("unexpected journal line: " + Arrays.toString(strings));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     DiskLruCache.Snapshot snapshot() {
/* 1019 */       if (!Thread.holdsLock(DiskLruCache.this)) throw new AssertionError();
/*      */       
/* 1021 */       Source[] sources = new Source[DiskLruCache.this.valueCount];
/* 1022 */       long[] lengths = (long[])this.lengths.clone();
/*      */       try {
/* 1024 */         for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
/* 1025 */           sources[i] = DiskLruCache.this.fileSystem.source(this.cleanFiles[i]);
/*      */         }
/* 1027 */         return new DiskLruCache.Snapshot(this.key, this.sequenceNumber, sources, lengths);
/* 1028 */       } catch (FileNotFoundException e) {
/*      */         
/* 1030 */         for (int i = 0; i < DiskLruCache.this.valueCount && 
/* 1031 */           sources[i] != null; i++) {
/* 1032 */           Util.closeQuietly((Closeable)sources[i]);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1040 */           DiskLruCache.this.removeEntry(this);
/* 1041 */         } catch (IOException iOException) {}
/*      */         
/* 1043 */         return null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\cache\DiskLruCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */