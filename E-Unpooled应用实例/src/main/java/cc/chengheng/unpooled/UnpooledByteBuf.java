package cc.chengheng.unpooled;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class UnpooledByteBuf {

    public static void main(String[] args) {

        // 创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world", StandardCharsets.UTF_8);

        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            // 将 content 转成字符串
            System.out.println(new String(content, 0, byteBuf.writerIndex(), StandardCharsets.UTF_8));

            System.out.println("byteBuf=" + byteBuf);

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex()); // 写到那个位置了
            System.out.println(byteBuf.capacity()); // 容量

            System.out.println(byteBuf.getByte(0));

            int len = byteBuf.readableBytes(); //  可读取字节数

            System.out.println("len=" + len);

            // 使用for取出各个字节
            for (int i = 0; i < len; i++) {
                System.out.print((char)byteBuf.getByte(i));
                if (i == len-1) {
                    System.out.println();
                }
            }

            System.out.println(byteBuf.getCharSequence(0, 4, StandardCharsets.UTF_8));
        }
    }
}
