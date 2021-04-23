package cc.chengheng.RPC.coustomer;

import cc.chengheng.RPC.netty.NettyClient;
import cc.chengheng.RPC.publicInterface.IHelloService;

public class ClientBootstrap {

    /** 这里定义协议头 */
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {
        // 创建一个消费者
        NettyClient customer = new NettyClient();

        // 创建代理对象
        IHelloService service = (IHelloService) customer.getBean(IHelloService.class, providerName);

        // 通过代理对象调用服务提供者的方法(服务)
        String res = service.hello("你好 dubbo!");
        System.out.println("调用的结果 res = " + res);
    }
}
