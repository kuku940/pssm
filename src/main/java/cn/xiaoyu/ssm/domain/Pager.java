package cn.xiaoyu.ssm.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 *
 * @author xiaoyu
 * @date 2017/2/20
 */
public class Pager<T> {
    private List pageData = new ArrayList<T>();
    private int pageNum;
    private int totalPageNum;
    private int totalRecord;
    private int firstPageNum;
    private int lastPageNum;
    public List getPageData() {
        return pageData;
    }
    public void setPageData(List pageData) {
        this.pageData = pageData;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getTotalPageNum() {
        return totalPageNum;
    }
    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }
    public int getTotalRecord() {
        return totalRecord;
    }
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
    public int getfirstPageNum() {
        return firstPageNum;
    }
    public void setfirstPageNum(int firstPageNum) {
        this.firstPageNum = firstPageNum;
    }
    public int getLastPageNum() {
        return lastPageNum;
    }
    public void setLastPageNum(int lastPageNum) {
        this.lastPageNum = lastPageNum;
    }
}