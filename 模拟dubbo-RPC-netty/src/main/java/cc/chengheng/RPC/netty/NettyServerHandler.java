package cc.chengheng.RPC.netty;

import cc.chengheng.RPC.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务器的Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用服务
        System.out.println("msg" + msg);

        /*
         * 客户端在调用服务器api时，我们需要定义一个协议
         * 比如我们要求，每次发消息都必须以某个字符串开头 "HelloService#hello#xxxx"
         * HelloService#hello 以这个开头的协议
         */

        if (msg.toString().startsWith("HelloService#hello")) {
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
