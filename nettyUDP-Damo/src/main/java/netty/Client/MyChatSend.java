package netty.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @Auther: Lina Wen
 * @Date: 2021/8/6 - 08 - 06 - 15:27
 * @Description: server
 * @version: 1.0
 */
public class MyChatSend {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Channel ch= null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .handler(new MyChannelInitializer());
            ch = b.bind(7398).sync().channel();
            //向目标端口发送信息
            while (true) {
                Scanner sc = new Scanner(System.in);
                String msg = sc.next();
                ch.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer(msg, Charset.forName("GBK")),
                        new InetSocketAddress("127.0.0.1", 7397))).sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ch.closeFuture().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            group.shutdownGracefully();
        }
    }

}
