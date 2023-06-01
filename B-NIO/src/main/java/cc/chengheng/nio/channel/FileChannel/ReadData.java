package cc.chengheng.nio.channel.FileChannel;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ReadData {

    private static final String resourceFolder;

    static {
        String resourceFolder1;
        resourceFolder1 = Objects.requireNonNull(WriteData.class.getResource("/")).getPath();
        resourceFolder1 = URLDecoder.decode(resourceFolder1, StandardCharsets.UTF_8);
        resourceFolder = resourceFolder1;
    }

    public static void main(String[] args) {
        File file = new File(resourceFolder + "/file01.txt");
        Charset charset = StandardCharsets.UTF_8;
        CharsetDecoder charsetDecoder = charset.newDecoder();


        try (
                FileInputStream fileInputStream = new FileInputStream(file); // 创建文件输入流
                FileChannel fileChannel = fileInputStream.getChannel(); // 获取FileChannel， 真实类: FileChannelImpl

        ) {

            // 创建缓冲区, 就是分配一快内存
            ByteBuffer byteBuffer = ByteBuffer.allocate(30);
            CharBuffer charBuffer = CharBuffer.allocate(3);

            byte[] remainByte = null; // 剩余字节
            int leftNum = 0;
            char[] tmp = null;


            while (true) {
                int len = fileChannel.read(byteBuffer); // 从 通道 读取输入, 向 byteBuffer 写入
                if (len == -1) break;

                byteBuffer.flip(); // 切换读模式

                charsetDecoder.decode(byteBuffer, charBuffer, true);

                charBuffer.flip();

                leftNum = byteBuffer.limit() - byteBuffer.position();
                if (leftNum>0) {
                    remainByte = new byte[leftNum];
                    byteBuffer.get(remainByte, 0, leftNum);
                }
                tmp = new char[charBuffer.length()];
                while (charBuffer.hasRemaining()) {
                    charBuffer.get(tmp);
                    System.out.print(new String(tmp));
                }



//                System.out.println(new String(byteBuffer.array()));
                byteBuffer.clear(); // 切换写模式
                charBuffer.clear();

                if (remainByte != null) {
                    byteBuffer.put(remainByte);
                    remainByte = null;
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
