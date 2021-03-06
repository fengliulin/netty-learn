package cc.chengheng;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workerGroup)
                           .channel(NioServerSocketChannel.class)
                           .option(ChannelOption.SO_BACKLOG, 128)
                           .childOption(ChannelOption.SO_KEEPALIVE, true)
                           .childHandler(
                                   new ChannelInitializer<SocketChannel>() {
                                       @Override
                                       protected void initChannel(SocketChannel ch) throws Exception {
                                           // 获取pipeline
                                           ChannelPipeline pipeline = ch.pipeline();

                                           // 向pipeline加入解码器
                                           pipeline.addLast("decoder", new StringDecoder());

                                           // 向pipeline加入编码器
                                           pipeline.addLast("encoder", new StringEncoder());

                                           // 加入自己的业务处理handler
                                           pipeline.addLast(new GroupChatServerHandler());
                                       }
                                   }
                           );

            System.out.println("netty 服务器启动");

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        GroupChatServer groupChatServer = new GroupChatServer(7000);
        groupChatServer.run();
    }
}
