package cc.chengheng.自定义解决粘包和拆包解决方案;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageEncoder()); // 编码器
        pipeline.addLast(new MyMessageDecoder()); // 解码器
        pipeline.addLast(new ServerHandler());
    }
}
