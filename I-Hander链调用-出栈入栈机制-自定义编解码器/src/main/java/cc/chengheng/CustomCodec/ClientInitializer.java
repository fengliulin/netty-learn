package cc.chengheng.CustomCodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个出栈的handler 对数据进行一个编码
        pipeline.addLast(new LongToByteEncoder());

        // 这是一个入栈的解码器
//        pipeline.addLast(new ByteToLongDecoder());

        pipeline.addLast(new NettyReplayingDecoder());

        // 加入一个自定义的handler，处理业务
        pipeline.addLast(new ClientHandler());

    }
}
