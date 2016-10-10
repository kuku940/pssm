package cn.xiaoyu.ssm.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2016/9/30.
 * 测试nio中的文件channel
 */
public class TestFileChannel {
    @Test
    public void testReadFile() throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile("D:/test/1.txt","rw");
        FileChannel fileChannel = accessFile.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        int byteRead = fileChannel.read(buffer);
        while(byteRead != -1){
            buffer.flip();
            while(buffer.hasRemaining()){
                System.out.print((char)buffer.get());
            }
            buffer.clear();
            fileChannel.read(buffer);
        }
        accessFile.close();
        System.exit(0);
    }
}