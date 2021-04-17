package cc.chengheng.nio.example.零拷贝;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class 传统Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] bytes = new byte[4096];

            while (true) {
                int readCount = dataInputStream.read(bytes, 0, bytes.length);
                if (readCount == -1) {
                    break;
                }
            }

//            dataInputStream.close();
//            serverSocket.close();
        }
    }
}
