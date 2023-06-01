package cc.chengheng.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Start Date: 2021/11/9
 * Author: 冯镠霖(fengliulin)
 */
public class E_ByteBuffer字符串转 {
    public static void main(String[] args) {

        //  1、字符串转为 ByteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        buffer1.put("hello".getBytes(StandardCharsets.UTF_8));

        // 2、Charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");

        // 3、wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello张".getBytes(StandardCharsets.UTF_8));

        // 这个要用   .flip 读模式，因为 position没有在开始读取的位置
        String s = StandardCharsets.UTF_8.decode(buffer3).toString();
        System.out.println(s);
    }
}
