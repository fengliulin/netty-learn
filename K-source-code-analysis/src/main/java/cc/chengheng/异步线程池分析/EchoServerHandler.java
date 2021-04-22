/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cc.chengheng.异步线程池分析;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.StandardCharsets;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // group就是充当业务线程池，可以将任务提交到该线程池。 创建了16个
    private final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("EchoServerHandler 的线程是=" + Thread.currentThread().getName());

        // 用户程序自定义普通任务
        /*ctx.channel().eventLoop().execute(() -> {
             try {
                Thread.sleep(5 * 1000);
                 System.out.println("EchoServerHandler execute 线程是=" + Thread.currentThread().getName());
                // 输出线程名
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端: 喵2🐱", StandardCharsets.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/

        // 将任务提交到group线程池
        group.submit(() -> {
            // 接收客户端信息
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            new String(bytes, StandardCharsets.UTF_8);

            // 休眠10秒
            try {
                Thread.sleep(10 * 1000);
                System.out.println("group.submit 的 call 线程是=" + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端: 喵2🐱", StandardCharsets.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        });

        System.out.println("go on");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
