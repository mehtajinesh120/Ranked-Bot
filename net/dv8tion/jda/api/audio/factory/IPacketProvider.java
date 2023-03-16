package net.dv8tion.jda.api.audio.factory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.VoiceChannel;

@NotThreadSafe
public interface IPacketProvider {
  @Nonnull
  String getIdentifier();
  
  @Nonnull
  VoiceChannel getConnectedChannel();
  
  @Nonnull
  DatagramSocket getUdpSocket();
  
  @Nonnull
  InetSocketAddress getSocketAddress();
  
  @Nullable
  ByteBuffer getNextPacketRaw(boolean paramBoolean);
  
  @Nullable
  DatagramPacket getNextPacket(boolean paramBoolean);
  
  void onConnectionError(@Nonnull ConnectionStatus paramConnectionStatus);
  
  void onConnectionLost();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\factory\IPacketProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */