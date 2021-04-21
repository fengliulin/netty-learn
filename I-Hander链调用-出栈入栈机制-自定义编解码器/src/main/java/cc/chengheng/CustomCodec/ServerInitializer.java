package cc.chengheng.CustomCodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入栈的handler进行解码 MyByteToLongDecoder
        pipeline.addLast(new ServerByteToLongDecoder());

        // 自定义的handler 处理业务逻辑
        pipeline.addLast(new ServerHandler());
    }
}
