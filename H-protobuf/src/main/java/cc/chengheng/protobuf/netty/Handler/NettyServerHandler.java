package cc.chengheng.protobuf.netty.Handler;

import cc.chengheng.protobuf.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * 说明：
 * 1、自定义一个 Handler 需要继承 netty 规定好的某个 HandlerAdapter
 * 2、自定义一个 Handler，才能称为一个 Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据实际（这里我们可以读取客户端发来的消息）
     *
     * @param ctx 上下文对象，含有 管道pipeline、通道channel、地址
     * @param msg 就是客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取客户端发送的StudentPojo.Student
        StudentPOJO.Student student = (StudentPOJO.Student) msg;

        System.out.println("客户端发送的数据id=" + student.getId() + " 名字=" + student.getName());
    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
         * writeAndFlush 是 write + flush
         * 将数据写入到缓存，并刷新
         * 一般讲，我们对这个发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端: 喵1🐱", StandardCharsets.UTF_8));
    }

    /**
     * 处理异常，一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
