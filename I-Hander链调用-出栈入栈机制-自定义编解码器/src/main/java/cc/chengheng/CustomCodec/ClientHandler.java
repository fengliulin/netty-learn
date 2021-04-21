package cc.chengheng.CustomCodec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class ClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip=" + ctx.channel().remoteAddress());
        System.out.println("收到服务器消息=" + msg);
    }

    /**
     *
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
//        ctx.writeAndFlush(Unpooled.copiedBuffer(""))
        ctx.writeAndFlush(123456L);

        /*
         *  1、"aaaaaaaaaaaaaaaa"是16个字节
         *  2、该处理器的前一个handler是 ClientLongToByteEncoder
         *  3、ClientLongToByteEncoder 父类 MessageToByteEncoder
         *  if (acceptOutboundMessage(msg)) { // 判断当前msg，是不是应该处理的类型，如果是，就
                @SuppressWarnings("unchecked")
                I cast = (I) msg;
                buf = allocateBuffer(ctx, cast, preferDirect);
                try {
                    encode(ctx, cast, buf); // 回调，抽象方法，会调用实现类
                } finally {
                    ReferenceCountUtil.release(cast);
                }

                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise); // 如果不是直接写出了
            }
         * 4、因此我们在编写 Encoder 要注意传入的数据类型和处理的数据类型要一致
         */
        //ctx.writeAndFlush(Unpooled.copiedBuffer("aaaaaaaaaaaaaaaa", StandardCharsets.UTF_8));
    }
}
