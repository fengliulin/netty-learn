/******************************************
 * ProjectName : 程衡服装进销存            
 * Web         : www.chengheng.cc
 * Start Date  : 2023/6/1
 * Author      : 冯镠霖(fengliulin)
 * Email       : menin@163.com
 ******************************************/
package cc.chengheng.nio.buffer;

import java.nio.ByteBuffer;

public class AllocateAndAllocateDirect {
    public static void main(String[] args) {
        ByteBuffer.allocate(16); // java堆内存,读写效率低,收到GC的影响
        ByteBuffer.allocateDirect(16); // 直接内存就是系统内存, 读写效率高(少一次拷贝), 分配内存效率低, 合理释放,不释放会造成内存泄漏
    }
}

