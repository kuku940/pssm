package cn.xiaoyu.ssm.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by Administrator on 2016/10/1.
 * 丢弃任何进入的数据
 */
public class DiscardServer {
    private int port;
    public DiscardServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        //用来接受进来的链接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //用来处理已经被接受的链接，一旦boss接受到链接，就会把链接信息注册到worker上
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            // ServerBootStrap是一个启动NIO服务的辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception{
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

                    // 绑定端口，开始接受进来的链接
                    ChannelFuture f = bootstrap.bind(port).sync();

                    // 等待服务器 socket关闭
                    // 在这个例子中，这个不会发生，但你可以优雅地关闭你的服务器
                    f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 6889;
        new DiscardServer(port).run();
    }
}

/**
 * 处理服务端channel
 */
class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try {
            //do something with msg
            while(in.isReadable()){
                System.out.print((char)in.readByte());
                System.out.flush();
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


}
