package cc.chengheng.nio.buffer;

import java.nio.ByteBuffer;

public class Buffer只读类型 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            buffer.put(Byte.parseByte(Integer.toString(i)) );
        }

        // 读取
        buffer.flip();

        // 得到一个只读的buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass().getName());

        // 读取
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        /*
         * public ByteBuffer put(byte x) {
         *    throw new ReadOnlyBufferException();
         * }
         */
        readOnlyBuffer.put((byte)100); // 只读的不能放数据，否则会出异常
    }
}
