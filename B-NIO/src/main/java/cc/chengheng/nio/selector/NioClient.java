package cc.chengheng.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NioClient {
    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置非阻塞模式
        socketChannel.configureBlocking(false);

        // 提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if (!(socketChannel.connect(inetSocketAddress))) {
            while (!(socketChannel.finishConnect())) {
                System.out.println("因为连接需要事件，客户端不会阻塞，可以做其它工作...");
            }
        }

        // 如果连接成功，就发送数据
        String str = "hello, 程衡";
        // 包裹一个字节数组到buffer里
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        // 发送数据, 将buffer数据写入socketChannel
        socketChannel.write(buffer);

        System.in.read();
    }
}
