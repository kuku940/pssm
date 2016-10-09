package cn.xiaoyu.ssm.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/2.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    /**
     * 表明一个ChannelHandler可以被多个Channel安全地共享
     * 每次接收消息时被调用
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf in = (ByteBuf)msg; //使用了LineBasedFrameDecoder和StringDecoder解码器不要这么读了
//        // 将消息打印到控制台
//        System.out.println("Server received:"+in.toString(CharsetUtil.UTF_8));
        String body = (String) msg;
        System.out.println("Server received:"+body);

        // 将当前时间写入发送方，不刷新输出消息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTime = "currentTime is "+sdf.format(new Date());
        ByteBuf resp = Unpooled.copiedBuffer(currentTime,CharsetUtil.UTF_8);
        ctx.write(resp);
    }

    @Override
    /**
     * 用来通知handler上一个ChannelRead()是被这批消息中的最后一个消息调用
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //刷新挂起的数据到远端然后关闭Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
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
