package cc.chengheng.CustomCodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class CustomCodecServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入栈的handler进行解码 MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());

        // 自定义的handler 处理业务逻辑
        pipeline.addLast(new CustomCodecServerHandler());
    }
}
