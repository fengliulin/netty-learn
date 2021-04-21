package cc.chengheng.自定义解决粘包和拆包解决方案;

/**
 * 协议包
 */
public class MessageProtocol {

    /** 长度，关键 */
    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
