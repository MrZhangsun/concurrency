package site.zhangsun.concurrent.bio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * Function:
 *
 * @author zhangsunjiankun - 2019/5/4 下午4:09
 */
public class BIOReadFile {
    public static void main(String[] args) throws IOException {
        File fileIn = new File("/Users/zhangsunjiankun/Documents/workspace/idea/concurrency/src/main/resources/bio_in.txt");
        FileInputStream inputStream = new FileInputStream(fileIn);
        FileChannel channelIn = inputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);


//        channelIn.read(buffer);
//        buffer.put("abcd".getBytes());
//        System.out.println(buffer.toString());
//        System.out.println("=============");
//        System.out.println("capacity: " + buffer.capacity());
//        System.out.println("limit: "+buffer.limit());
//        System.out.println("position: "+buffer.position());
//        System.out.println("remain: "+buffer.remaining());
//        System.out.println("get: "+(char)buffer.get());

//        buffer.flip();
//        byte[] bytes = new byte[4];
//        System.out.println("***********************");
////        System.out.println("get: "+(char)buffer.get());
//        System.out.println("gets: " + buffer.get(bytes, 0, 3));
//        System.out.println("arrays: " + Arrays.toString(bytes));
//        System.out.println("capacity: " + buffer.capacity());
//        System.out.println("limit: "+buffer.limit());
//        System.out.println("position: "+buffer.position());
//        System.out.println("remain: "+buffer.remaining());
//        while (buffer.remaining() > 0) {
//            System.out.println("***********************");
//            System.out.println("get: "+(char)buffer.get());
//            System.out.println("capacity: " + buffer.capacity());
//            System.out.println("limit: "+buffer.limit());
//            System.out.println("position: "+buffer.position());
//            System.out.println("remain: "+buffer.remaining());
//        }


        FileOutputStream outputStream = new FileOutputStream("/Users/zhangsunjiankun/Documents/workspace/idea/concurrency/src/main/resources/bio_out.txt");
        FileChannel channelOut = outputStream.getChannel();

        while (true) {
            buffer.clear();
            int r = channelIn.read(buffer);
            if (r==-1) {
                break;
            }
            buffer.flip();
            channelOut.write( buffer );
        }
//        System.out.println(buffer.toString());
//        System.out.println("=============");
//        System.out.println("capacity: " + buffer.capacity());
//        System.out.println("limit: "+buffer.limit());
//        System.out.println("position: "+buffer.position());
//        System.out.println("remain: "+buffer.remaining());remaining
//        System.out.println("get: "+buffer.get());
    }
}
