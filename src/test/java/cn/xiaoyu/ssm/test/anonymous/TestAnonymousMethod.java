package cn.xiaoyu.ssm.test.anonymous;

/**
 * Java的匿名类的两种实现方式
 * 1、
 * 2、
 */
public class TestAnonymousMethod{
    public void print(){
        System.out.println("普通方法...");
    }

    public void sayHi(AnonymousInterface ai){
        ai.sayHi();
    }

    public static void main(String[] args) {
        // 通过重写类的方法
        new TestAnonymousMethod(){
            public void print(){
                System.out.println("覆写的方法...");
            }
        }.print();

        TestAnonymousMethod tam = new TestAnonymousMethod();

        // 通过接口实现匿名类的实例
        tam.sayHi(new AnonymousInterface() {
            @Override
            public void sayHi() {
                System.out.println("hi,everybody!");
            }
        });
    }
}
