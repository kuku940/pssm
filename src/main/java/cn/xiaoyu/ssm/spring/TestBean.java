package cn.xiaoyu.ssm.spring;

/**
 * Created by roin_zhang on 2016/9/21.
 */
public class TestBean {
    private String testStr = "testStr";

    public String getTestStr() {
        return testStr;
    }

    public void setTestStr(String testStr) {
        this.testStr = testStr;
    }

    public void printTest(){
        System.out.println("test Bean");
    }
}
