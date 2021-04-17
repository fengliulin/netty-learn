package cc.chengheng.nio.example.零拷贝;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class 零拷贝Client {
    public static void main(String[] args) throws IOException {

        // 和服务器进行连接
        InetSocketAddress address = new InetSocketAddress(7001);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(address);

        FileChannel fileChannel = new FileInputStream("").getChannel();

        long startTime = System.currentTimeMillis();

        System.out.println(fileChannel.size());

        /*
         * 在linux下，一次transferTo方法就可以完成传输
         * 在windows下，一次调用transferTo 只能发送8m，就需要分段传输文件，而且要注意传输时的位置
         * transferTo 底层使用到了零拷贝
         */
        long transferCount = fileChannel.transferTo(0, 6987073165L, socketChannel);

        System.out.println("发送总字节数：" + transferCount +", 耗时:" + (System.currentTimeMillis() - startTime));
    }
}
