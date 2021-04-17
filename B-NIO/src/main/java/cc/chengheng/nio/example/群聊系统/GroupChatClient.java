package cc.chengheng.nio.example.群聊系统;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 群聊服务端
 */
public class GroupChatClient {

    // 定义相关属性
    private final String HOST = "127.0.0.1";
    private static final int PORT = 6667;

    private Selector selector;
    private SocketChannel socketChannel;

    private String username;

    /**
     * 完成初始化的工作
     * @throws IOException
     */
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

        // 设置非阻塞
        socketChannel.configureBlocking(false);

        // 将socketChannel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);

        // 得到username
        username = this.socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(username + " is ok");
    }

    // 向服务器发送消息
    public void sendInfo(String info) {
        info = username + "说:" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取从服务端回复的消息
    public void readInfo() {
        try {
            selector.select();

            // 得到选择的key
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey =  iterator.next();

                if (selectionKey.isReadable()) {
                    // 得到相关的通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    // 读取
                    int readLength = socketChannel.read(buffer);

                    // 把读到的缓冲去的数据转成字符串
                    buffer.flip();
                    String message = new String(buffer.array(), 0, readLength);
                    System.out.println(message.trim());
                }

                // 不移除，服务器拿不到消息
                iterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动我们的客户端
        GroupChatClient chatClient = new GroupChatClient();

        // 启动一个线程
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();

                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
