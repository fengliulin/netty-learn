package cc.chengheng.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

import java.nio.channels.SocketChannel;

public class ServerInitializer<泛型继承 extends Channel> extends ChannelInitializer<泛型继承> {

    @Override
    protected void initChannel(泛型继承 ch) throws Exception {
        // 向管道加入处理器

        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();

        /*
         * 加入一个netty 提供得 httpServerCodec  codec -》 [coder - decoder]
         * 1、HttpServerCodec 是 netty 提供处理http得编解码器
         */
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        // 增加一个自定义得handler
        pipeline.addLast("MyHttpServerHandler", new HttpServerHandler());
    }
}
