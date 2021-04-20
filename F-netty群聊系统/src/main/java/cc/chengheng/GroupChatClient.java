package cc.chengheng;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventExecutors)
                           .channel(NioSocketChannel.class)
                           .handler(
                                   new ChannelInitializer<SocketChannel>() {
                                       @Override
                                       protected void initChannel(SocketChannel ch) throws Exception {
                                           // 获取pipeline
                                           ChannelPipeline pipeline = ch.pipeline();

                                           // 加入相关的handler
                                           pipeline.addLast("decoder", new StringDecoder());
                                           pipeline.addLast("encoder", new StringEncoder());

                                           // 加入自定义的handler
                                           pipeline.addLast(new GroupChatClientHandler());
                                       }
                                   }
                           );

            System.out.println("netty 服务器启动");

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            // 得到channel
            Channel channel = channelFuture.channel();
            System.out.println("----------" + channel.localAddress() + "---------");

            // 客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();

                // 通过channel发送出去，到服务器端
                channel.writeAndFlush(msg + "\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        GroupChatClient groupChatClient = new GroupChatClient("127.0.0.1", 7000);

        groupChatClient.run();
    }
}
