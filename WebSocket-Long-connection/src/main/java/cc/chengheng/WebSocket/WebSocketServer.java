package cc.chengheng.WebSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    public static void main(String[] args) {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在我们的bossGroup增加一个日志处理者
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();

                                    // 因为基于http协议，使用http编码和解码器
                                    pipeline.addLast(new HttpServerCodec());

                                    // 是以块方式写，添加ChunkedWriteHandler处理器
                                    pipeline.addLast(new ChunkedWriteHandler());

                                    /*
                                     * 说明
                                     *  1、http数据在传输过程中是分段的，HttpObjectAggregator就是将多个段聚合起来
                                     *  2、这就是为什么，当浏览器发送大量数据时，就会发送多次http请求
                                     */
                                    pipeline.addLast(new HttpObjectAggregator(8192));

                                    /*
                                     * 说明
                                     *  1、对应WebSocket，它的数据以帧(frame)的形式传递
                                     *  2、可以看到WebSocketFrame 下面有6个子类
                                     *  3、浏览器请求时，ws://localhost:7000/xxx 表示请求的uri
                                     *  3、WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议，保持长连接
                                     *  4、响应101状态码
                                     */
                                    pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                                    // 自定义的handler，处理业务逻辑
                                    pipeline.addLast(new WebSocketServerFrameHandler());
                                }
                            }
                    );

            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
