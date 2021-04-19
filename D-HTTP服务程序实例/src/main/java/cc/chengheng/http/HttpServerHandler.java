package cc.chengheng.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * 说明
 * 1、SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 得子类
 * 2、HttpObject: 客户端和服务端相互通讯得数据封装成 HttpObject
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws URISyntaxException {
        // 判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest) {

            System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + ", HttpServerHandler" + this.hashCode());

            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            // 获取到
            HttpRequest httpRequest = (HttpRequest) msg;

            // 获取uri,过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("你请求了favicon.ico");
            }


            // 回复信息给浏览器[http 协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", StandardCharsets.UTF_8);

            // 构造一个http响应，即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好 response 返回
            ctx.writeAndFlush(response);
        }
    }
}
