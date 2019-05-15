package site.zhangsun.concurrent.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/4 下午6:31
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        // 创建选择器
        Selector selector = Selector.open();
        InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), 20000);
        // 绑定服务端地址
        server.socket().bind(serverAddress);
        // 配置通道为非阻塞模式
        server.configureBlocking(false);
        // 注册选择器并绑定选择器事件为读就绪事件
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server has been started...");

        while (true) {
            int select = selector.select();
            System.out.println("select result: " + select);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    System.out.println("Received Client Connection.." + next.attachment());
                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    channel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_ACCEPT);
                }

                if (next.isReadable()) {
                    System.out.println("Readable Client Connection..");
                    SocketChannel client = (SocketChannel) next.channel();
//                    ByteBuffer buffer = new By
//                    client.read()
                }
            }
        }
    }
}
