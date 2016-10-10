package cn.xiaoyu.ssm.nio;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created by Administrator on 2016/10/2.
 * 考虑拆包和粘包
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private int counter = 0;
    private final int sendTime;
    public EchoClientHandler(int sendTime){
        this.sendTime = sendTime;
    }
    //向服务器端输入的数据
    private String req = "Query time order"+System.getProperty("line.separator");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client received:"+msg+";this counter is:"+ ++counter);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 循环向服务器端发送消息
        for(int i=0;i<sendTime;i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer(req,CharsetUtil.UTF_8));
        }
    }

    @Override
    /**
     * 在读操作异常被抛出时被调用
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常堆栈跟踪信息
        cause.printStackTrace();
        // 关闭这个channel
        ctx.close();
    }
}
