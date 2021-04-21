package cc.chengheng.CustomCodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class CustomCodecClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个出栈的handler 对数据进行一个编码
        pipeline.addLast(new CustomCodecLongToByteEncoder());

        // 加入一个自定义的handler，处理业务
        pipeline.addLast(new CustomCodecClientHandler());

    }
}
