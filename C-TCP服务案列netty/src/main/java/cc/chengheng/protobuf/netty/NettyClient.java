package cc.chengheng.protobuf.netty;

import cc.chengheng.protobuf.netty.Handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {
        // 客户端需要一个事件循环组
        NioEventLoopGroup eventExecutorsGroup = new NioEventLoopGroup();

        try {
            /*
             * 创建客户端启动对象
             * 注意：客户端使用的不是 ServerBootstrap 而是 BootStrap
             */
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap.group(eventExecutorsGroup) // 设置线程组
                     .channel(NioSocketChannel.class) // 设置客户端通道的实现类（Netty会用反射来处理的）
                     .handler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    // 加入自己的处理器
                                    ch.pipeline().addLast(new NettyClientHandler());
                                }
                            }
                     );
            System.out.println("客户端 ok..");

            /*
             * 启动客户端去连接服务器端
             * 关于 channelFuture 要分析，涉及到 netty 的异步模型
             */
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668);

            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventExecutorsGroup.shutdownGracefully();
        }
    }
}
