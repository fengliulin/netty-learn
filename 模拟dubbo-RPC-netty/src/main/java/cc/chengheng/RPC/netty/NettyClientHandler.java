package cc.chengheng.RPC.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    /** 上下文 */
    private ChannelHandlerContext context;

    /** 返回结果 */
    private String result;

    /** 客户端调用方法时，传入的参数 */
    private String param;

    /**
     *
     * 被代理对象调用，发送数据给服务器 -> wait -> 等待被唤醒（channelRead） -> 返回结果
     *
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        // 进行等待
        wait(); // 等待channelRead方法获取到服务器结果后，唤醒
        return result; // 服务方返回结果
    }

    /**
     * 与服务器连接创建后，就会被调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx; // 因为我们在其它方法会使用到上下文
    }

    /**
     * 收到服务器数据后，调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify(); // 唤醒等待的线程

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    void setParam(String param) {
        this.param = param;
    }
}
