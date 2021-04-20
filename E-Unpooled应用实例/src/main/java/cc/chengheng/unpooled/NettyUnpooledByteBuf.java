package cc.chengheng.unpooled;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyUnpooledByteBuf {

    public static void main(String[] args) {
        /*
         * 创建一个Bytebuffer
         * 说明：
         *  1、创建对象，该对象包含一个数组arr，是一个byte[10]
         *  2、在netty的buffer中，不需要使用flip进行翻转
         *  3、底层维护了readerIndex和writerIndex
         *  4、通过readerIndex 和 writerIndex 和 capacity， 将 buffer 分成 三个区域
         *     0      <=      readerIndex  已经读取的区域
         *      readerIndex   <=   writerIndex 可读区域
         *      writerIndex    <=    capacity 可写的区域
         *      +-------------------+------------------+------------------+
         *      | discardable bytes |  readable bytes  |  writable bytes  |
         *      +-------------------+------------------+------------------+
         *      |                   |                  |                  |
         *      0      <=      readerIndex   <=   writerIndex    <=    capacity
         *
         */
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity =" + buffer.capacity());
        // 输出
        /*for (int i = 0; i < buffer.writerIndex(); i++) {
            System.out.println(buffer.getByte(i));
        }*/

        for (int i = 0; i < buffer.writerIndex(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
