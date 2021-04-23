package cc.chengheng.RPC.provider;

import cc.chengheng.RPC.publicInterface.IHelloService;

public class HelloServiceImpl implements IHelloService {

    /**
     * 当有消费方调用该方法时，就返回一个结果
     *
     * @param msg
     * @return
     */
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息=" + msg);

        if (msg != null) {
            return "您好，客户端，我已经收到你的消息[" + msg + "]";
        }

        return "您好，客户端，我已经收到你的消息";
    }
}
