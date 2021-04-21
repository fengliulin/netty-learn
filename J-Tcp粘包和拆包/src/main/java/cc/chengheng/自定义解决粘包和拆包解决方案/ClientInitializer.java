package cc.chengheng.自定义解决粘包和拆包解决方案;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageDecoder()); //加入解码器
        pipeline.addLast(new MyMessageEncoder()); // 加入编码器, 顺序错了，就没反应
        pipeline.addLast(new ClientHandler());
    }
}
