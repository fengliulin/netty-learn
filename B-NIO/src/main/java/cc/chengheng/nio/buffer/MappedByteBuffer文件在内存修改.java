package cc.chengheng.nio.buffer;

import cc.chengheng.nio.channel.FileChannel.WriteData;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * 可以让文件直接在内存修改
 * MappedByteBuffer：文件在堆外内存修改,操作系统不需要拷贝一次
 */
public class MappedByteBuffer文件在内存修改 {

    private static final String resourceFolder;

    static {
        resourceFolder = Objects.requireNonNull(WriteData.class.getResource("/")).getPath();
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(resourceFolder + "/1.txt", "rw");

        // 获取对应的通道
        FileChannel randomAccessFileChannel = randomAccessFile.getChannel();

        /*
         * 参数1：FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2：0 可以直接修改的起始位置
         * 参数3：5 映射到内存的大小，即将 1.txt 的多少个字节映射到内存
         *
         * 可以直接修改的范围就是 0-5
         *  实际类:DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = randomAccessFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte)'H');
        mappedByteBuffer.put(3, (byte)'9');

        randomAccessFileChannel.close();
        randomAccessFile.close();
    }
}
