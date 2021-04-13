package cc.chengheng.nio.buffer;

import java.nio.IntBuffer;

/**
 * 说明nio的Buffer的使用
 */
public class Buffer基本使用 {
    public static void main(String[] args) {
        // 创建一个buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        
        // 向buffer存放数据
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        /*
         *  limit = position;  读取数据不能超过position
         *  position = 0;
         *  mark = -1;
         */
        // 从buffer读取数据, 读写切换
        intBuffer.flip();

        /*
         * public final boolean hasRemaining() {
         *         return position < limit;
         *     }
         */
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
