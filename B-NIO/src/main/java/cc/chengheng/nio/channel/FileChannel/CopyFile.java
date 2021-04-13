package cc.chengheng.nio.channel.FileChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class CopyFile {

    private static final String resourceFolder;
    static {
        resourceFolder = Objects.requireNonNull(WriteData.class.getResource("/")).getPath();
    }

    public static void main(String[] args) throws IOException {

        // 创建文件输入流，获取通道
        FileInputStream fileInputStream = new FileInputStream(resourceFolder + "/1.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        // 创建一个输出流,获取通道
        FileOutputStream fileOutputStream = new FileOutputStream(resourceFolder + "/2.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        // 创建缓冲区，传输数据，要遵守这个规则
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        //region 重点，就是读取数据和写入
        while (true) { // 循环读取
            // 重置
            byteBuffer.clear();

            int read = fileInputStreamChannel.read(byteBuffer);
            System.out.println("read:" + read);
            if (read == -1) { // 表示读取完毕
                break;
            }

            byteBuffer.flip(); // 翻转
            // 将buffer中的数据写入到2.txt
            fileOutputStreamChannel.write(byteBuffer);
        }
        //endregion

        // 关闭
        fileOutputStreamChannel.close();
        fileOutputStream.close();

        fileInputStreamChannel.close();
        fileInputStream.close();
    }

}
