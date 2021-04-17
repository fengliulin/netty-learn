package cc.chengheng.nio.example.零拷贝;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class 零拷贝Server {
    public static void main(String[] args) throws IOException {

        // 创建服务器绑定端口
        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        // 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            while (readCount != -1) {
                readCount = socketChannel.read(byteBuffer);
            }

            byteBuffer.rewind(); // 倒带 position = 0; mark 作废
        }

    }
}
