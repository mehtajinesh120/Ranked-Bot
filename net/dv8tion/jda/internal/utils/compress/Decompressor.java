/*    */ package net.dv8tion.jda.internal.utils.compress;
/*    */ 
/*    */ import java.util.zip.DataFormatException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.utils.Compression;
/*    */ import net.dv8tion.jda.internal.utils.JDALogger;
/*    */ import org.slf4j.Logger;
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
/*    */ public interface Decompressor
/*    */ {
/* 28 */   public static final Logger LOG = JDALogger.getLog(Decompressor.class);
/*    */   
/*    */   Compression getType();
/*    */   
/*    */   void reset();
/*    */   
/*    */   void shutdown();
/*    */   
/*    */   @Nullable
/*    */   byte[] decompress(byte[] paramArrayOfbyte) throws DataFormatException;
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\compress\Decompressor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */