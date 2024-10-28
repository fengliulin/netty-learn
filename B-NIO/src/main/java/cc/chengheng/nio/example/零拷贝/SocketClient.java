package cc.chengheng.nio.example.零拷贝;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) throws IOException {
            // 接收控制台用户输入的数据
            Scanner scanner = new Scanner(System.in);
            // 要绑定的主机和端口号
            Socket socketClient = new Socket("127.0.0.1", 7001);


            String a = "CSDN首页\n" +
                    "博客\n" +
                    "下载\n" +
                    "学习\n" +
                    "社区\n" +
                    "知道\n" +
                    "GitCode\n" +
                    "InsCode\n" +
                    "会议\n" +
                    "  搜索\n" +
                    "\n" +
                    "会员1024 \n" +
                    "消息\n" +
                    "历史\n" +
                    "创作中心\n" +
                    "发布\n" +
                    "RabbitMQ交换机之死信交换机\n" +
                    "\n" +
                    "小依不秃头\n" +
                    "\n" +
                    "于 2022-02-28 19:41:58 发布\n" +
                    "\n" +
                    "阅读量2.3k\n" +
                    " 收藏 3\n" +
                    "\n" +
                    "点赞数\n" +
                    "分类专栏： RabbiMQ 文章标签： rabbitmq 分布式 java\n" +
                    "版权\n" +
                    "\n" +
                    "RabbiMQ\n" +
                    "专栏收录该内容\n" +
                    "3 篇文章0 订阅\n" +
                    "订阅专栏\n" +
                    "续博客：RabbitMQ交换机之直连交换机，扇形交换机，主题交换机_小依不秃头的博客-CSDN博客\n" +
                    "\n" +
                    "一、死信交换机（延迟队列）的概念 \n" +
                    " \n" +
                    "\n" +
                    " 死信，在官网中对应的单词为“Dead Letter”,它是 RabbitMQ 的一种消息机制。\n" +
                    "般来说，生产者将消息投递到 broker 或者直接到 queue 里了，consumer 从 queue 取出消息进行消费，如果它一直无法消费某条数据，那么可以把这条消息放入死信队列里面。等待\n" +
                    "条件满足了再从死信队列中取出来再次消费，从而避免消息丢失。\n" +
                    "\n" +
                    "死信消息来源：\n" +
                    "\n" +
                    "消息 TTL 过期\n" +
                    "队列满了，无法再次添加数据\n" +
                    "消息被拒绝（reject 或 nack），并且 requeue =false\n" +
                    "二、死信交换机（代码）\n" +
                    "①生产者，创建交换机\n" +
                    " package com.xhy.provider.mq;\n" +
                    "\n" +
                    "import org.springframework.amqp.core.Binding;\n" +
                    "import org.springframework.amqp.core.BindingBuilder;\n" +
                    "import org.springframework.amqp.core.DirectExchange;\n" +
                    "import org.springframework.amqp.core.Queue;\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "\n" +
                    "import java.util.HashMap;\n" +
                    "import java.util.Map;\n" +
                    "\n" +
                    "\n" +
                    "@Configuration\n" +
                    "@SuppressWarnings(\"all\")\n" +
                    "public class DeadConfig {\n" +
                    "    /**\n" +
                    "     * 死信交换机需要：\n" +
                    "     * 1.需要正常的交换机\n" +
                    "     * 2.正常的队列\n" +
                    "     * 3.具备死信交换机和队列\n" +
                    "     *\n" +
                    "     */\n" +
                    "    //新建队列\n" +
                    "    //正常\n" +
                    "    @Bean\n" +
                    "    public Queue normalQueue(){\n" +
                    "        Map<String,Object> config=new HashMap<>();\n" +
                    "        //过期时间\n" +
                    "        config.put(\"x-message-ttl\", 10000);//message在该队列queue的存活时间最大为10秒\n" +
                    "        //死信交换机\n" +
                    "        config.put(\"x-dead-letter-exchange\", \"deadExchange\"); //x-dead-letter-exchange参数是设置该队列的死信交换器（DLX）\n" +
                    "        //死信routingkey\n" +
                    "        config.put(\"x-dead-letter-routing-key\", \"deadQueue\");//x-dead-letter-routing-key参数是给这个DLX指定路由键\n" +
                    "\n" +
                    "        return new Queue(\"normalQueue\",true,false,false,config);\n" +
                    "    }\n" +
                    "    //死信\n" +
                    "    @Bean\n" +
                    "    public Queue daadQueue(){\n" +
                    "\n" +
                    "        return new Queue(\"daadQueue\",true);\n" +
                    "    }\n" +
                    "    //创建交换机---正常（死信交换机就是直连交换机）\n" +
                    "    @Bean\n" +
                    "    public DirectExchange normalExchange(){\n" +
                    "        return  new DirectExchange(\"directExchange\");\n" +
                    "    }\n" +
                    "    //死信\n" +
                    "    @Bean\n" +
                    "    public DirectExchange deadExchange(){\n" +
                    "        return  new DirectExchange(\"deadExchange\");\n" +
                    "    }\n" +
                    "\n" +
                    "    //进行交换机和队列的绑定\n" +
                    "    //设置绑定key\n" +
                    "//正常\n" +
                    "@Bean\n" +
                    "    public Binding normalBinding(){\n" +
                    "        return  BindingBuilder.bind(normalQueue()).to(normalExchange()).with(\"CC\");\n" +
                    "    }\n" +
                    "    //死信\n" +
                    "@Bean\n" +
                    "    public Binding deadBinding(){\n" +
                    "        return  BindingBuilder.bind(normalQueue()).to(deadExchange()).with(\"DD\");\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "}\n" +
                    "②创建controller层模拟发送消息\n" +
                    " @RequestMapping(\"/deadSend\")\n" +
                    "public String deadSend(){\n" +
                    "    //原理:假设保存一个订单\n" +
                    "    template.convertAndSend(\"normalExchange\",\"CC\",\"order-110\");\n" +
                    "    return \"yes\";\n" +
                    "}\n" +
                    "③运行\n" +
                    "文章知识点与官方知识档案匹配，可进一步学习相关知识\n" +
                    "Java技能树首页概览152780 人正在系统学习中\n" +
                    "\n" +
                    "小依不秃头\n" +
                    "关注\n" +
                    "\n" +
                    "0\n" +
                    "\n" +
                    "\n" +
                    "3\n" +
                    "\n" +
                    "2\n" +
                    "\n" +
                    "专栏目录\n" +
                    "rabbitmq + spring boot demo 消息确认、持久化、备用交换机、死信交换机等代码\n" +
                    "03-07\n" +
                    "本教程将详细介绍如何在Spring Boot应用中结合RabbitMQ实现消息确认、消息持久化、备用交换机以及死信交换机等功能。 首先，让我们理解这些概念： 1. **消息确认**：在RabbitMQ中，消息确认（Message ...\n" +
                    "2024年阿里首次分享：内部（珠峰版)Java笔记，看完直接斩获12家offer\n" +
                    "m0_72758098的博客\n" +
                    "  545\n" +
                    "自我介绍一下，小编13年上海交大毕业，曾经在小公司待过，也去过华为、OPPO等大厂，18年进入阿里一直到现在。深知大多数Java工程师，想要提升技能，往往是自己摸索成长，自己不成体系的自学效果低效漫长且无助。因此收集整理了一份《2024年Java开发全套学习资料》，初衷也很简单，就是希望能够帮助到想自学提升又不知道该从何学起的朋友，同时减轻大家的负担。\n" +
                    "2 条评论\n" +
                    "半个月敲不出一行\n" +
                    "热评\n" +
                    "哥 你是文章没有写完吗 ？\n" +
                    "写评论\n" +
                    "RabbitMQ的死信队列_java rabbitmq 死信队列\n" +
                    "10-23\n" +
                    "本文介绍了如何使用RabbitMQ在Java中实现消息发送确认机制,包括使用ConfirmCallback处理发送成功和失败情况,以及如何设置业务队列和死信队列,确保高可用性。还讨论了生产者消费者模型的应用和消息持久化的潜在优化。 摘要由CSDN通过智能技术生成 routingKey); }); return rabbitTemplate; } /** 确认发送消息是否成功(调用...\n" +
                    "中间件系列三 RabbitMQ之交换机的四种类型和属性_mq交换机和普通队列区...\n" +
                    "10-26\n" +
                    "这些是RabbitMQ默认创建的交换机。这些队列名称被预留做RabbitMQ内部使用,不能被应用使用,否则抛出403 (ACCESS_REFUSED)错误 Dead Letter Exchange(死信交换机) 在默认情况,如果消息在投递到交换机时,交换机发现此消息没有匹配的队列,则这个消息将被悄悄丢弃。为了解决这个问题,RabbitMQ中有一种交换机叫死信交换机。当...\n" +
                    "rabbitMQ死信.延迟队列\n" +
                    "m0_60402568的博客\n" +
                    "  1461\n" +
                    "死信队列(延迟队列) 死信，在官网中对应的单词为“Dead Letter”,它是 RabbitMQ 的一种消息机制。 般来说，生产者将消息投递到 broker 或者直接到 queue 里了，consumer 从 queue 取出消息进行消费，如果它一直无法消费某条数据，那么可以把这条消息放入死信队列里面。等待 条件满足了再从死信队列中取出来再次消费，从而避免消息丢失。 死信消息来源： 消息 TTL 过期 队列满了，无法再次添加数据 消息被拒绝（reject 或 nack），并且\n" +
                    "什么是死信交换机\n" +
                    "最新发布\n" +
                    "m0_57836225的博客\n" +
                    "  444\n" +
                    "死信” 这个名称表示这些消息在原始队列中无法被正常处理，处于一种 “失效” 或 “无法继续流动” 的状态，就像 “死信” 无法送达目的地一样。但通过死信交换机，可以给这些消息一个 “二次机会”，将它们路由到其他地方进行进一步处理。在这个示例中，消息发送到普通队列，如果消息在设置的 TTL 时间内没有被处理，它将变成死信并被路由到死信交换机，然后由消费死信队列的代码进行处理。当消息在一个队列中变成 “死信” 时，它可以被重新路由到一个特定的交换机，这个交换机就被称为死信交换机。如果使用 Maven，在。\n" +
                    "RabbitMQ之死信交换机_rabbitmq死信交换机\n" +
                    "10-26\n" +
                    "x-dead-letter-exchange表示该队列的死信交换器(DLX),即当消息在队列中过期或被拒绝时,将被发送到指定的交换器。 x-dead-letter-routing-key表示消息被发送到死信交换器时的路由键。 小结: 这样的配置中,queueA 是一个具有延迟特性的队列,当消息在该队列中存活时间超过设定的10秒时,消息会被发送到名为 \"Exchan...\n" +
                    "RabbitMQ--死信交换机_rabbitmq处理死信\n" +
                    "10-23\n" +
                    "RabbitMQ--死信交换机 目录 一、什么是死信交换机 二、TTL 三、代码实现 生产者(publisher) 消费者(consumer) 效果展示 四、延迟队列 五、消息发送确认 5.1.发送的消息怎么样才算失败或成功?如何确认? 消息消费者如何通知 Rabbit 消息消费成功? 六、消息堆积问题...\n" +
                    "【重磅干货】基于RabbitMQ的TTL(延迟队列+死信队列)，实现支付订单超时自动取消，一般大厂的落地方案\n" +
                    "竹林幽深\n" +
                    "  1980\n" +
                    "6. 发布延迟消息：将需要超时处理的订单消息发布到'order.delay.queue'队列，并设置消息的TTL（Time To Live），即消息在队列中的存活时间。如果订单已经超时，则将订单消息发布到'order.release.order.queue'队列中，由专门的处理程序进行后续的取消操作。5.发布订单消息：将支付订单的消息发布到'order.release.order.queue'队列，由消费者进行正常的处理。4.构建交换机对象：将'order.create.order的死信交换器设置为'\n" +
                    "Rabbitmq死信交换机\n" +
                    "leese233的博客\n" +
                    "  784\n" +
                    "Rabbitmq死信交换机\n" +
                    "RabbitMQ-中死信交换机_死信交换机没有自动转发消息\n" +
                    "10-24\n" +
                    "RabbitMQ延迟队列可以通过使用死信交换机和消息的TTL设置来实现。 设置死信交换机和延迟队列 以下是如何使用Java代码来设置死信交换机和延迟队列: importcom.rabbitmq.client.*;publicclassDLXExample{publicstaticvoidmain(String[]argv)throwsException{ConnectionFactoryfactory=newConnectionFactory();factory.setHost(\"localh...\n" +
                    "RabbitMQ-Java-05-死信队列_java获取rabbitmq unacked死信队列消息...\n" +
                    "10-19\n" +
                    "import cn.cnyasin.rabbit.utils.RabbitMqUtils; import com.rabbitmq.client.*; import java.util.HashMap; import java.util.Map; public class Consumer01 { // 交换机名 public static final String EXCHANGE_NAME_NORMAL = \"exchange.normal\";\n" +
                    "RabbitMQ --- 死信交换机(一)\n" +
                    "Stephen GS的博客\n" +
                    "  878\n" +
                    "何为死信, 死信交换机的使用, deadLetterExchange, ttl\n" +
                    "RabbitMq死信交换机\n" +
                    "qq_36740038的博客\n" +
                    "  2630\n" +
                    "如果该队列设置了 dead-letter-exchange属性，指定了一个交换机，那么队列中的私信就会投递到这个交换机中，而这个交换机称为死信交换机（Dead Letter Exchagne,简称DLX）给队列设置dead-letter-routing-key属性，设置死信交换机与死信队列的routingkey。利用TTL结合死信交换机，我们实现了消息发出后，消费者延迟收到消息的效果。因为延迟队列的需求非常多，所以Rabbitmq 的官方也推出了一个插件，原生支持延迟队列的效果。通过下图 我们可以明白了，\n" +
                    "SpringBoot整合RabbitMQ实现死信队列_springboot rabbitmq 死信队列...\n" +
                    "10-23\n" +
                    "前面一文通过Java整合RabbitMQ实现生产消费(7种通讯方式),本文基于SpringBoot实现RabbitMQ中的死信队列和延迟队列。 概念介绍 什么是死信 死信可以理解成没有被正常消费的消息,在RabbitMQ中以下几种情况会被认定为死信: 消费者使用basic.reject或basic.nack(重新排队参数设置为false)对消息进行否定确认。\n" +
                    "Java-RabbitMq-死信队列_java rabbitmq 消息投递失败 加入死信队列-CSDN...\n" +
                    "10-22\n" +
                    "RabbitMq死信队列有三种发生情况: 消息TTL 过期 队列达到最大长度(队列满了,无法再添加数据到 mq 中) 消息被拒绝(basic.reject 或 basic.nack)并且 requeue=false. 生产者: importcom.cn.mq.utils.ConnectionMq;importcom.rabbitmq.client.AMQP;importcom.rabbitmq.client.BuiltinExchangeType;importcom.rabbitmq....\n" +
                    "Spring RabbitMQ死信机制原理实例详解\n" +
                    "08-24\n" +
                    "在 Spring RabbitMQ 中，可以通过配置死信队列和死信交换机来实现死信机制。例如： ```java @Bean public Queue deadQueue() { return new Queue(DEAD_QUEUE_NAME, true); } @Bean public DirectExchange ...\n" +
                    "springboot整合rabbitmq使用死信队列\n" +
                    "11-06\n" +
                    "创建一个配置类，用于设置RabbitMQ的相关属性，包括死信交换机（dead-letter-exchange）和死信队列（dead-letter-queue）： ```java @Configuration public class RabbitConfig { @Value(\"${spring.rabbitmq.dead...\n" +
                    "rabbitmq队列和交换机的实现\n" +
                    "02-20\n" +
                    "在这个话题中，我们将深入探讨RabbitMQ中的队列和交换机的实现，以及相关的概念。 首先，让我们了解RabbitMQ中的基本组件。队列是消息的容器，存储生产者发送的消息，直到消费者将它们取出并处理。交换机则是消息...\n" +
                    "RabbitMQ --- 死信交换机\n" +
                    "乌云的博客\n" +
                    "  920\n" +
                    "什么样的消息会成为死信？消息被消费者reject或者返回nack消息超时未消费队列满了死信交换机的使用场景是什么？如果队列绑定了死信交换机，死信会投递到死信交换机；可以利用死信交换机收集所有消费者处理失败的消息（死信），交由人工处理，进一步提高消息队列的可靠性。消息超时的两种方式是？给队列设置ttl属性，进入队列后超过ttl时间的消息变为死信给消息设置ttl属性，队列接收到消息超过ttl时间后变为死信如何实现发送一个消息20秒后消费者才收到消息？给消息的目标队列指定死信交换机。\n" +
                    "RabbitMQ\n" +
                    "小旭IT-分享技术\n" +
                    "  233\n" +
                    "RabbitMQ 消息中间件的学习MQ mq RabbitMQRabbitMQ前言一、MQ的介绍1.1MQ概述1.2MQ的优势1、应用解耦2、任务异步处理3、削峰填谷1.3MQ的劣势1.4 AMQP 和 JMS1.4.1 AMQP1.4.2 JMS1.4.3 AMQP 与 JMS 区别二、 RabbitMQ介绍1、rabbitMQ概念介绍2、RabbitMQ提供的5种模式中的使用以及介绍2.1简单模式2.2work模式2.3Publish/Subscribe发布与订阅模式2.4Routing路由模式2.5\n" +
                    "【RabbitMQ笔记10】消息队列RabbitMQ之死信队列的介绍\n" +
                    "✅ Java 开发工程师，从事 Web 应用程序的研发，擅长 Spring、SpringBoot 等技术。 ✅ 热爱编程，业余时间学习新知识，通过 CSDN 记录学习心得和笔记内容。\n" +
                    "  2818\n" +
                    "在RabbitMQ中，并没有提供真正意义上的延迟队列，但是RabbitMQ可以设置队列、消息的过期时间，当队列或者消息到达过期时间之后，还没有被消费者消费，那么RabbitMQ会将这些消息放入另外一个队列，这个队列叫做：死信队列，而这个过期的消息就叫做：死信消息。哪些情况下，消息会变成死信消息？？？第一种情况：Queue队列已经满了，无法保存新进入的消息，那么这个消息就会被放入死信队列。第二种情况：队列中的消息被消费者拒绝消费了，并且没有设置重新放入Queue队列里面。\n" +
                    "【RabbitMQ】延迟队列之死信交换机\n" +
                    "万维网连五洲，二进制写尽天下事，喜欢结识新伙伴寻找志同道合的朋友，欢迎一起探讨学习\n" +
                    "  6425\n" +
                    "延迟队列是一种特殊类型的消息队列，它允许将消息在一定的延迟时间后才被消费。在传统的消息队列中，消息一旦发送到队列中就会立即被消费者获取并处理。而延迟队列则提供了一种延迟消息处理的机制。\n" +
                    "RabbitMQ入门指南(十)：延迟消息-死信交换机\n" +
                    "Rye的博客\n" +
                    "  1183\n" +
                    "RabbitMQ是一个高效、可靠的开源消息队列系统，广泛用于软件开发、数据传输、微服务等领域。本文主要介绍了死信交换机、死信交换机实现延迟消息等内容。\n" +
                    "消息队列——RabbitMq\n" +
                    "weixin_45080272的博客\n" +
                    "  5618\n" +
                    "一、消息队列的使用场景 一、消息队列的使用场景) 一、消息队列的使用场景 异步处理 应用解耦 流量削锋 消息通讯 【1】异步处理：场景说明：用户注册后，需要发注册邮件和注册短信。 引入消息队列后架构如下：用户的响应时间=注册信息写入数据库的时间，例如50毫秒。发注册邮箱、发注册短信写入消息队列后，直接返回客户端，因写入消息队列的速度很快，基本可以忽略，因此用户的响应时间可能是50毫秒。 按照传统的做法： ①、串行方式，将注册信息写入数据库成功后，发注册邮件，再发送注册短信，以上三个成功后，返回客户端。可\n" +
                    "RabbitMQ消息队列全面解析\n" +
                    "footbridge的博客\n" +
                    "  2741\n" +
                    "一.同步和异步通讯微服务间通讯有同步和异步两种方式：同步通讯：就像打电话，需要实时响应。异步通讯：就像发邮件，不需要马上回复。\n" +
                    "RabbitMQ 死信交换机\n" +
                    "09-24\n" +
                    "RabbitMQ 死信交换机（Dead Letter Exchange）是一种用于处理消息路由失败的机制。当消息被拒绝（basic.reject/ basic.nack）或者在队列上过期时，它将被重新路由到一个特定的交换机，而不是直接被丢弃。这个特定的交换机就是死信交换机。 要使用死信交换机，您可以利用 RabbitMQ 的插件系统。RabbitMQ 官方提供了一个插件社区，您可以在 Community Plugins — RabbitMQ 找到相关插件。如果您需要了解如何安装和配置死信交换机，可以参考官方的安装指南：Scheduling Messages with RabbitMQ | RabbitMQ - Blog。\n" +
                    "关于我们\n" +
                    "招贤纳士\n" +
                    "商务合作\n" +
                    "寻求报道\n" +
                    "\n" +
                    "400-660-0108\n" +
                    "\n" +
                    "kefu@csdn.net\n" +
                    "\n" +
                    "在线客服\n" +
                    "工作时间 8:30-22:00\n" +
                    "公安备案号11010502030143\n" +
                    "京ICP备19004658号\n" +
                    "京网文〔2020〕1039-165号\n" +
                    "经营性网站备案信息\n" +
                    "北京互联网违法和不良信息举报中心\n" +
                    "家长监护\n" +
                    "网络110报警服务\n" +
                    "中国互联网举报中心\n" +
                    "Chrome商店下载\n" +
                    "账号管理规范\n" +
                    "版权与免责声明\n" +
                    "版权申诉\n" +
                    "出版物许可证\n" +
                    "营业执照\n" +
                    "©1999-2024北京创新乐知网络技术有限公司\n" +
                    "\n" +
                    "小依不秃头\n" +
                    "码龄4年\n" +
                    " 暂无认证\n" +
                    "80\n" +
                    "原创\n" +
                    "30万+\n" +
                    "周排名\n" +
                    "167万+\n" +
                    "总排名\n" +
                    "13万+\n" +
                    "访问\n" +
                    " \n" +
                    "等级\n" +
                    "927\n" +
                    "积分\n" +
                    "208\n" +
                    "粉丝\n" +
                    "97\n" +
                    "获赞\n" +
                    "29\n" +
                    "评论\n" +
                    "333\n" +
                    "收藏\n" +
                    "分享学徒\n" +
                    "新秀勋章\n" +
                    "签到新秀\n" +
                    "持续创作\n" +
                    "笔耕不辍\n" +
                    "创作能手\n" +
                    "新人勋章\n" +
                    "阅读者勋章\n" +
                    "私信\n" +
                    "关注\n" +
                    " \n" +
                    "写文章\n" +
                    "\n" +
                    "热门文章\n" +
                    "SpringBoot之自定义注解   21884\n" +
                    "SPA项目开发之登陆注册   19675\n" +
                    "Linux常用命令&&安装yum的命令   9751\n" +
                    "vue中的VueX详解及使用   7044\n" +
                    "linux上安装Docker(非常简单详细的安装)以及Docker的三要素和基本使用   6851\n" +
                    "分类专栏\n" +
                    "\n" +
                    "面试\n" +
                    "\n" +
                    "秒杀\n" +
                    "2篇\n" +
                    "\n" +
                    "微信小程序\n" +
                    "3篇\n" +
                    "\n" +
                    "微服务自动化\n" +
                    "3篇\n" +
                    "\n" +
                    "RabbiMQ\n" +
                    "3篇\n" +
                    "\n" +
                    "Docker\n" +
                    "3篇\n" +
                    "\n" +
                    "spring cloud\n" +
                    "1篇\n" +
                    "\n" +
                    "SpringBoot\n" +
                    "3篇\n" +
                    "\n" +
                    "Quartz\n" +
                    "1篇\n" +
                    "\n" +
                    "SpringMvc\n" +
                    "\n" +
                    "mybatis\n" +
                    "5篇\n" +
                    "\n" +
                    "vue\n" +
                    "9篇\n" +
                    "\n" +
                    "linux\n" +
                    "2篇\n" +
                    "\n" +
                    "Hibernate框架\n" +
                    "4篇\n" +
                    "\n" +
                    "vue+element-UI\n" +
                    "5篇\n" +
                    "\n" +
                    "Spring\n" +
                    "3篇\n" +
                    "\n" +
                    "SPA\n" +
                    "2篇\n" +
                    "\n" +
                    "J2EE基础之mvc框架\n" +
                    "12篇\n" +
                    "\n" +
                    "Struts框架\n" +
                    "5篇\n" +
                    "\n" +
                    "网上书城\n" +
                    "7篇\n" +
                    "\n" +
                    "EasyUI\n" +
                    "6篇\n" +
                    "\n" +
                    "MySQL\n" +
                    "5篇\n" +
                    "\n" +
                    "最新评论\n" +
                    "linux上安装Docker(非常简单详细的安装)以及Docker的三要素和基本使用\n" +
                    "马牛当不: yum makecache fast 是 centos 7 下的命令，并不适用 8 用这个 dnf makecache\n" +
                    "定时任务Quartz的基本使用\n" +
                    "上心www: 帮大忙了哥\n" +
                    "RabbitMQ交换机之死信交换机\n" +
                    "小依不秃头: 是的没有时间更新了\n" +
                    "linux上安装Docker(非常简单详细的安装)以及Docker的三要素和基本使用\n" +
                    "小依不秃头: 啊哈我安装的时候没有报错\n" +
                    "linux上安装Docker(非常简单详细的安装)以及Docker的三要素和基本使用\n" +
                    "普通网友: 少了一步对下载的docker.repo里的$releasever进行替换，例如centos7就用%s/$releasever/7/g替换，新手跟着做一直报错，后面才发现这个问题\n" +
                    "大家在看\n" +
                    "Jenkins实现自动拉取Git代码，构建镜像，并上传到远程镜像仓库   296\n" +
                    "谁说π难求？盘点圆周率的各种操作\n" +
                    "【计算机专业毕设选题推荐】基于协同过滤算法的的儿童图书推荐系统的设计与实现 【附源码+部署+讲解】\n" +
                    "首届能源科学工程国际会议 （IS-ESE 2025 ）\n" +
                    "算法学习笔记1：数据结构   413\n" +
                    "最新文章\n" +
                    "秒杀项目之用户验证&&全局session&&自定义redis&&参数解析器\n" +
                    "秒杀项目介绍&&登陆模块&&捕捉全局异常\n" +
                    "微信小程序开发之小程序框架&表单\n" +
                    "2022年19篇2021年61篇\n" +
                    "\n" +
                    "\n" +
                    "目录\n" +
                    "\n" +
                    "一、死信交换机（延迟队列）的概念 \n" +
                    "二、死信交换机（代码）\n" +
                    "①生产者，创建交换机\n" +
                    "②创建controller层模拟发送消息\n" +
                    "③运行\n" +
                    "创作活动\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "举报\n";
            socketClient.getOutputStream().write(a.getBytes());


    }
}
