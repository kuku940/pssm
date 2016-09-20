package cn.xiaoyu.ssm.java;

/**
 * Created by roin_zhang on 2016/9/18.
 * 简单使用断言，并测试异常
 *
 * assert需要在运行时显式开启-ea，assert断言失败将面临程序的退出
 *
 * assert的判断和if语句差不多，但两者有着本质的区别：assert关键字本意上是为测试调试程序时使用的，
 * 但如果用assert来控制了程序的业务流程，那在测试调试结束后去掉assert关键字就意味着修改了程序的正常的逻辑。
 */
public class TestAssert {
    public static void main(String[] args) {
        /**
         * 默认执行程序，没有开启-ea开关，需要在运行时开启
         * java -ea TestAssert  //使断言生效
         * java -da TestAssert  //使断言失效（默认失效）
         *
         * 记得在VM参数配置中添加 -ea 开启断言
         */
        assert true;
        System.out.println("断言1没有问题");

        assert false:"断言失败，抛异常！";
        System.out.println("断言2没有问题");
    }
}
