package cn.xiaoyu.ssm.serialzable;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2016/10/2.
 * 考虑拆包和粘包 向服务器发送对象
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client received:"+msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo info = null;
        info = new UserInfo();
        info.setName("netty");
        info.setAge(5);

        ctx.writeAndFlush(info);
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

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //刷新挂起的数据到远端然后关闭Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
