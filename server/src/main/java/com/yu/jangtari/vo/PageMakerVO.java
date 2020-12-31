package com.yu.jangtari.vo;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "pageList")
public class PageMakerVO<T> {

    private Page<T> result;

    private Pageable prevPage;
    private Pageable nextPage;

    private int currentPageNum;
    private int totalPageNum;

    private Pageable currentPage;
    private List<Pageable> pageList;

    public PageMakerVO(Page<T> result, long totalPageNumParam){
        this.result = result;
        this.currentPage = result.getPageable();
        this.currentPageNum = currentPage.getPageNumber() + 1;
        this.totalPageNum = (int)totalPageNumParam;
        this.pageList = new ArrayList<>();
        calcPages();
    }
    private void calcPages(){
        int tmpEndNum = (int)(Math.ceil(this.currentPageNum/10.0) * 10);
        int startNum = tmpEndNum - 9;

        Pageable startPage = this.currentPage;

        for(int i = startNum; i < this.currentPageNum; i++){
            startPage = startPage.previousOrFirst();
        }
        System.out.println("이전은 " + startPage.getPageNumber());

        this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

        System.out.println("중간은 " + this.totalPageNum + " " + tmpEndNum);

        if(this.totalPageNum < tmpEndNum){
            tmpEndNum = this.totalPageNum;
            this.nextPage = null;
        }
        for(int i = startNum; i <= tmpEndNum; i++){
            pageList.add(startPage);
            startPage = startPage.next();
            System.out.println("다음은 " + startPage.getPageNumber());

        }
        this.nextPage = startPage.getPageNumber() + 1 < totalPageNum ? startPage : null;
    }
}
