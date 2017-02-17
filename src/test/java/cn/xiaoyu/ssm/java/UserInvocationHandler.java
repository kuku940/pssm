package cn.xiaoyu.ssm.java;

import cn.xiaoyu.ssm.messagepack.UserInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 用户代理类
 *
 * @author xiaoyu
 * @date 2016/12/17
 */
public class UserInvocationHandler implements InvocationHandler{
    public <T> T newInstance(Class<T> clz){
        return (T) Proxy.newProxyInstance(clz.getClassLoader(),new Class[]{clz},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                // 诸如hashCode()、toString()、equals()等方法，将target指向当前对象this
                return method.invoke(this, args);
            } catch (Throwable t) {
            }
        }
        // 投鞭断流
        return "Hello world!";
    }

    public static void main(String[] args) {
        UserInvocationHandler proxy = new UserInvocationHandler();
        UserInterface userInterface = proxy.newInstance(UserInterface.class);
        System.out.println(userInterface.getUserName());
        System.out.println(userInterface.toString());
    }
}
