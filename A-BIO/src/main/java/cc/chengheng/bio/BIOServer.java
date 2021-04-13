package cc.chengheng.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        // 创建一个线程池;如果有客户端连接，就创建一个线程，与之通讯
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true) {
            System.out.println("线程信息id=" + Thread.currentThread().getId() + "\t线程的名字=" + Thread.currentThread().getName());

            // 监听,等待客户连接, 这个监听的对象就是和客户端通讯的
            Socket socket = serverSocket.accept(); // 没有客户端链接会阻塞
            System.out.println("连接一个客户端");

            // 就创建一个线程，与之通讯（单独写一个方法）
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    /**
     * 和客户端通讯
     */
    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            System.out.println("线程信息id=" + Thread.currentThread().getId() + "\t线程的名字=" + Thread.currentThread().getName());

            // 获取客户端输入流数据
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true) {

                System.out.println("线程信息id=" + Thread.currentThread().getId() + "\t线程的名字=" + Thread.currentThread().getName());

                // 客户端没有数据来，这里会阻塞
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.print(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
