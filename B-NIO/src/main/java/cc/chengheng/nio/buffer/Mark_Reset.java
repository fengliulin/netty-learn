/******************************************
 * ProjectName : 程衡服装进销存            
 * Web         : www.chengheng.cc
 * Start Date  : 2023/6/1
 * Author      : 冯镠霖(fengliulin)
 * Email       : menin@163.com
 ******************************************/
package cc.chengheng.nio.buffer;

import java.nio.ByteBuffer;

public class Mark_Reset {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{
                'a', 'b', 'c', 'd'
        });

        buffer.flip();

        // mark & reset
        // mark 做一个标记，记录 position 位置，reset 是将position 重置到mark 的位置
        System.out.println(buffer.get());
        System.out.println(buffer.get());

        buffer.mark(); // 标记
        System.out.println(buffer.get());
        buffer.reset(); // 把位置重置到标记位置

        System.out.println();


    }
}

