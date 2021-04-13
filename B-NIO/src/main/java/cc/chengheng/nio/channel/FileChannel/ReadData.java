package cc.chengheng.nio.channel.FileChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class ReadData {

    private static final String resourceFolder;
    static {
        resourceFolder = Objects.requireNonNull(WriteData.class.getResource("/")).getPath();
    }

    public static void main(String[] args) throws IOException {
        File file = new File(resourceFolder + "/file01.txt");
        // 创建文件输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        // 获取FileChannel， 真实类: FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将通道的数据读入到缓冲区
        fileChannel.read(byteBuffer);

        // 将ByteBuffer字节转成字符串
        System.out.println(new String(byteBuffer.array()));

        // 关闭
        fileChannel.close();
        fileInputStream.close();
    }

}
