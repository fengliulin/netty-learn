package cc.chengheng.nio.channel.FileChannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class TransferFromCopyFile {

    private static final String resourceFolder;

    static {
        resourceFolder = Objects.requireNonNull(WriteData.class.getResource("/")).getPath();
    }

    public static void main(String[] args) throws IOException {

        // 创建文件输入流，获取通道
        FileInputStream fileInputStream = new FileInputStream("/Users/fengliulin/Documents/2021春节合影副本.jpg");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        // 创建一个输出流,获取通道
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/fengliulin/Documents/abc.jpg");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        // 使用transferFrom完成拷贝, 可以拷贝视频
        fileOutputStreamChannel.transferFrom(fileInputStreamChannel, 0, fileInputStreamChannel.size());

        // 关闭
        fileOutputStreamChannel.close();
        fileOutputStream.close();

        fileInputStreamChannel.close();
        fileInputStream.close();
    }
}
