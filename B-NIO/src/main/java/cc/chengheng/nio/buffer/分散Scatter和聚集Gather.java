package cc.chengheng.nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 分散Scatter：将数据写入到buffer时，可以采用buffer数组，依次写入
 * 聚集Gather：从buffer读取数据时，可以采用buffer数组，依次读取
 */
public class 分散Scatter和聚集Gather {
    public static void main(String[] args) throws IOException {
        // 使用ServerSocketChanel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到Socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        // 创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端连接(telnet来连接测试)
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8; // 从客户端接收8个字节

        // 循环读取（不知道客户端发送多少数据就循环）
        while (true) {


            //region 读取客户端数据
            int byteRead = 0;
            while (byteRead < messageLength) {
                long len = socketChannel.read(byteBuffers);
                byteRead += 1;
                System.out.println("读取的字节byteRead = " + byteRead);

                // 使用流打印，看看当前的 ByteBuffer[] 数组里存的ByteBuffer.allocate
                Arrays.stream(byteBuffers)
                        .map(
                                byteBuffer ->
                                        "positon" + byteBuffer.position() +
                                        ",limit=" + byteBuffer.limit())
                        .forEach(System.out::println);
            }
            //endregion

            //region 服务端数据写入客户端

            // 将所有的 ByteBuffer[]数组里存的ByteBuffer.allocate 进行flip
            Arrays.stream(byteBuffers).forEach(Buffer::flip);

                // 将数据读出显示到客户端
                long byteWrite = 0;
                while (byteWrite < messageLength) {
                    try {
                        long l = socketChannel.write(byteBuffers);
                        byteWrite += l;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // 将所有的 ByteBuffer[]数组里存的ByteBuffer.allocate，进行clear
                Arrays.stream(byteBuffers).forEach(Buffer::clear);

                System.out.println("读取的字节byteRead=" + byteRead + " byteWrite=" + byteWrite);
            //endregion
        }
    }
}
