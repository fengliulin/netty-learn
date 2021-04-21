package cc.chengheng.CustomCodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入栈的handler进行解码 MyByteToLongDecoder

        //region 这两个独立工作，出栈时候找 Encoder， 入栈找Decoder
//        pipeline.addLast(new ByteToLongDecoder());

        pipeline.addLast(new NettyReplayingDecoder());

        // 出栈的进行编码
        pipeline.addLast(new LongToByteEncoder());
        //endregion

        // 自定义的handler 处理业务逻辑
        pipeline.addLast(new ServerHandler());
    }
}
