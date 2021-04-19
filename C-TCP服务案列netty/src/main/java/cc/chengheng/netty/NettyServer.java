package cc.chengheng.netty;

import cc.chengheng.netty.Handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*
         * 创建BossGroup 和 WorkerGroup
         * 说明：
         *  1、创建两个线程组 bossGroup 和 workerGroup
         *  2、bossGroup 只处理连接请求。 真正的和客户端业务处理会给workerGroup来处理
         *  3、两个都是无限循环
         *  4、bossGroup 和 workerGroup 含有的子线程（NioEventLoop）的个数，默认cpu核数 * 2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 老板
        EventLoopGroup workerGroup = new NioEventLoopGroup(8); // 工人

        //region 主要方法
        try {
            // 创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程进行设置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                     .channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道实现
                     .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                     .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                     .childHandler(
                            // 创建一个通道测试对象
                            new ChannelInitializer<SocketChannel>() {
                                // 给 pipeline 设置处理器
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    /*
                                     * 可以使用一个集合管理 SocketChannel，在推送消息时，
                                     * 可以将业务加入到各个channel对应的NIOEventLoop 的 taskQueue,
                                     * 或者scheduleTaskQueue
                                     */
                                    System.out.println("客户SocketChannel hashcode=" + ch.hashCode());

                                    ch.pipeline().addLast(new NettyServerHandler());
                                }
                            }
                    ); // 给我们的 workerGroup 的 EventLoop 对应的管道设置处理器

            System.out.println("服务器准备好了.......");

            // 绑定一个端口并且同步，生成了一个ChannelFuture 对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            // 给 cf 注册监听器，监控我们的关心事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        //endregion
    }
}
