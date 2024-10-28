package cc.chengheng.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到selector对象
        Selector selector = Selector.open();

        // 绑定一个端口6666,在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // ServerSocketChannel 注册到 Selector,关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动注册后的selectionKey 数量=" +  selector.keys().size());

        // 循环等待客户端连接
        while (true) {
            selector.select(1000);
            // 等待1秒，如果没有事件发生，返回
//            if (selector.select(1000) == 0) { // 没有事件发生
//                System.out.println("服务器等待了1秒，无连接");
//                continue;
//            }

            /* 如果返回的 > 0，就获取到相关的 selectedKey 集合
             *  1、如果返回的 > 0，表示已经获取到关注的事件
             *  2、selector.selectedKeys(); 返回关注事件的集合
             *      通过 selectedKeys 反向获取通道
             *
             * selector.selectedKeys() 返回只有事件发生的，不是注册的
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("selectionKeys 数量 = " + selectionKeys.size());

            // 遍历集合，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到SelectionKey
                SelectionKey selectionKey = keyIterator.next();

                // 根据 selectionKey 对应的通道发生的事件做相应的处理
                if (selectionKey.isAcceptable()) { // 如果是  SelectionKey.OP_ACCEPT 有新的客户端连接
                    /* 给该客户端生成一个 SocketChannel
                     * 本身这个方法 .accept() 是阻塞的，因为是事件驱动的，有一个客户端链接了，就不会阻塞了，直接连接上了
                     */
                    SocketChannel socketChannel = serverSocketChannel.accept();

//                    System.out.println("客户端连接成功，生成一个socketChannel " + socketChannel.hashCode());

                    // 设置非阻塞模式
                    socketChannel.configureBlocking(false);

                    // 将当前的 SocketChannel 注册到 Selector, 关注事件为 SelectionKey.OP_READ, 同时给SocketChannel 关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端连接后注册后的selectionKey 数量=" + selector.keys().size());
                }

                if (selectionKey.isReadable()) { // 发生 OP_READ
                    // 通过selectionKey 反向获取到对应的channel
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

                    // 改变关联的事件，要根据自己的业务逻辑修改
//                    selectionKey.interestOps(SelectionKey.OP_WRITE);

                    // 获取到该socketChannel 关联的buffer
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    buffer.clear();
                    int read = socketChannel.read(buffer);
                    buffer.flip();
                    System.out.println("从客户端发送的是：" + new String(buffer.array(), 0, read));
                }

                // 手动从集合中删除，防止重复操作
                keyIterator.remove();
            }

        }
    }
}
