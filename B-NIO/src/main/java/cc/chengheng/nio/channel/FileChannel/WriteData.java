package cc.chengheng.nio.channel.FileChannel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WriteData {

    private static final String resourceFolder;

    static {
        resourceFolder = Objects.requireNonNull(WriteData.class.getResource("/")).getPath();
    }

    public static void main(String[] args) throws IOException {
        String str = "hello, 程衡";

        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream(resourceFolder + "/file01.txt");

        // 获取文件通道， 真实类：FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str放入到缓冲区
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));

        // 翻转
        byteBuffer.flip();

        // 写入到文件里
        fileChannel.write(byteBuffer);

        // 关闭
        fileChannel.close();
        fileOutputStream.close();
    }
}
