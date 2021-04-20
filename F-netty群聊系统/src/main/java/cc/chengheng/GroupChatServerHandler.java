package cc.chengheng;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.*;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /** 客户端来一个添加进去一个 */
    public static List<Channel> channels = new ArrayList<>();

    /** 使用一个hashmap管理，因为不好区分客户端谁是谁，所以用hashmap */
    private static Map<String, Channel> channelsMap = new HashMap<>();
    private static Map<EntityUser, Channel> channelsUserMap = new HashMap<>();

    /**
     * 定义一个 channel 组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局事件执行器，是一个单例
     */
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示连接建立，一旦连接，第一个被执行 <br>
     * 将当前channel加入到channelGroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channels.add(channel);

        // 要么是登陆注册时候的id，反正要区分, 给你转发消息的时候，获取id对应的channel
        channelsMap.put("id100", channel);

        channelsUserMap.put(new EntityUser(10, "123"), channel);

        // 将该客户加入聊天的信息推送给其它在线的客户
        channelGroup.add(channel); // 用这个方便些

        // 该方法会将 channelGroup 中所有的channel遍历，并发送消息给每个客户端, 我们不需要自己遍历
        channelGroup.writeAndFlush(
                        "[客户端]" +
                        channel.remoteAddress() +
                        "加入聊天" +
                        simpleDateFormat.format(new Date()) + "\n"
        );
    }

    /**
     * 表示channel处于活动的状态， 提示xx上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~~");
    }

    /**
     * 表示channel处于不活动状态，提示xx离线了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~~");
    }

    /**
     * 断开连接触发，将xx客户离开信息推送给当前在线的客户
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");

        System.out.println("channelGroup size" + channelGroup.size());
    }

    /**
     * 读取数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前的channel
        Channel channel = ctx.channel();

        // 我们遍历channelGroup，根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (ch != channel) { // 不是当前的channel，转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
            } else {
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }

    /**
     * 发生异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }
}
