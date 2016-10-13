package cn.xiaoyu.ssm.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by roin_zhang on 2016/10/13.
 * 简单测试protobuf编解码
 */
public class TestPersonInfoProtobuf {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        PersonInfo.Person person = create();
        System.out.println("加密前："+person.toString());

        PersonInfo.Person p = decode(encode(person));
        System.out.println("加密解密后："+p.toString());
    }

    //将SubscribeReq编码为byte数组，使用非常方便
    private static byte[] encode(PersonInfo.Person req){
        return req.toByteArray();
    }
    //解码parseFrom将二进制byte数组解码为原始的对象
    private static PersonInfo.Person decode(byte[] body) throws InvalidProtocolBufferException {
        return PersonInfo.Person.parseFrom(body);
    }

    //创建一个req的实体
    private static PersonInfo.Person create(){
        PersonInfo.Subject.Builder subject = PersonInfo.Subject.newBuilder();
        subject.setName("english");
        subject.setGrade(90);
        subject.setType(PersonInfo.SubjectType.ENGLISH);

        PersonInfo.Person.Builder person = PersonInfo.Person.newBuilder();
        person.setId(10086);
        person.setUsername("Roin");
        person.setEmail("Roin@qq.com");
        person.setSubject(subject);

        return person.build();
    }
}
