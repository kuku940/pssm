package cn.xiaoyu.ssm.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2016/10/2.
 */
public class EchoClient {
    private String host;
    private int port;
    public EchoClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("localhost",6888).start();
    }

    public void start() throws Exception{
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            // 指定EventLoopgroup来处理客户端时间，需要EventLoopgroup的NIO实现
            bootstrap.group(workGroup)
                    // 用于NIO传输的Channel类型
                    .channel(NioSocketChannel.class)
                    // 设置服务器的InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host,port))
                    // 当一个Channel创建时，把一个EchoClinet加入他的pipeline中
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 链接到远端，一直等到链接完成
            ChannelFuture future = bootstrap.connect().sync();
            // 关闭连接池，释放所有资源
            future.channel().closeFuture().sync();
        }finally{
            workGroup.shutdownGracefully().sync();
        }
    }
}
