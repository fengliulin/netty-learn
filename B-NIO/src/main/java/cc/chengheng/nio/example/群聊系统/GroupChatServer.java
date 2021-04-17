package cc.chengheng.nio.example.群聊系统;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 群聊服务端
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() throws IOException {
        selector = Selector.open();
        listenChannel = ServerSocketChannel.open();

        // 绑定端口
        listenChannel.socket().bind(new InetSocketAddress(PORT));

        // 设置非阻塞模式
        listenChannel.configureBlocking(false);

        // 将该listenChannel
        listenChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    // 监听
    public void listen() throws IOException {
        // 循环处理
        while (true) {
            selector.select(); // 阻塞

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                // 监听到accept
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = listenChannel.accept();

                    // 设置非阻塞
                    socketChannel.configureBlocking(false);

                    // 注册到选择器
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    // 提示某某上线了
                    System.out.println(socketChannel.getRemoteAddress() + "上线");
                }

                if (selectionKey.isReadable()) { // 通道发送read事件，即通道是可读状态
                    // 处理读
                    readData(selectionKey);
                }

                // 当前的SelectionKey删除，防止重复处理
                iterator.remove();
            }
        }
    }

    /**
     * 读取客户端消息
     *
     * @param selectionKey
     */
    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;

        try {
            // 定义一个SocketChannel
            socketChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int readLength = socketChannel.read(buffer);

            if (readLength > 0) {
                // 把缓存区数据转成字符串
                String message = new String(buffer.array(), 0, readLength);
                System.out.println("从客户端发来的消息:" + message);

                // 向其它客户端转发消息(去掉自己)
                sendInfoToOtherClients(message, socketChannel);

            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了...");

                // 取消注册
                selectionKey.cancel();

                // 关闭通道
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其它客户端（就是给通道发送）
     *
     * @param message 转发的消息
     * @param self    排除自己
     */
    private void sendInfoToOtherClients(String message, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");


        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();

            // 排除自己, 这里要这样，应为是遍历， 第一次拿到的是连接SocketServerChannel, 已连接上的拿到的才是SocketChannel
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));

                // 将buffer的数据写入通道
                ((SocketChannel) targetChannel).write(buffer);
            }
        }

        // 遍历所有注册到selector上的SocketChannel，并排除自己
        /*Iterator<SelectionKey> iterator = selector.keys().iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();

            // 取出对应的SocketChannel
            SocketChannel targetChannel = (SocketChannel)selectionKey.channel();

            // 排除自己
            if (targetChannel != null && targetChannel != self) {
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));

                // 将buffer的数据写入通道
                targetChannel.write(buffer);
            }*/
    }

    public static void main(String[] args) throws IOException {
        // 创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();

        groupChatServer.listen();
    }
}
