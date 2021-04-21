package cc.chengheng.CustomCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * netty自带的解码器ReplayingDecoder
 */
public class NettyReplayingDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("NettyReplayingDecoder 被调用");
        // 在ReplayingDecoder不需要判断数据是否足够读取，内部会进行处理判断
        /*if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }*/
        out.add(in.readLong());
    }
}
