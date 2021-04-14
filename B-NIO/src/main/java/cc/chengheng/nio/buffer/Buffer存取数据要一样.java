package cc.chengheng.nio.buffer;

import java.nio.ByteBuffer;

public class Buffer存取数据要一样 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        // 类型化方式存放数据
        buffer.putInt(100);
        buffer.putLong(9L);
        buffer.putChar('镠');
        buffer.putShort((short) 4);

        // 取出
        buffer.flip();

        System.out.println();

        // 取出的类型顺序要和放入的一样，才准确
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}
