package cc.chengheng.CustomCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ServerByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * 会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list，或者是ByteBuf没有更多的可读字节为止
     * 如果list out不为空，就会将list的内容传递给下一个channelInboundHandler处理，该处理器的方法也会被调用多次
     *
     * @param ctx 上下文对象
     * @param in 入栈的ByteBuf
     * @param out List集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("CustomCodecServerByteToLongDecoder Decoder 被调用");
        // 因为long8个字节，需要判断有8个字节，才能读取一个long
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
