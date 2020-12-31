package com.yu.jangtari.vo;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class PageMakerVO<T> {

    private Page<T> result;

    private Pageable prevPage;
    private Pageable nextPage;

    private int currentPageNum;
    private int totalPageNum;

    private Pageable currentPage;

    public PageMakerVO(Page<T> result, int totalPageNumParam){
        this.result = result;
        this.currentPage = result.getPageable();
        this.currentPageNum = currentPage.getPageNumber() + 1;
        this.totalPageNum = totalPageNumParam;
        calcPages();
    }
    private void calcPages(){
        int tmpEndNum = (int)(Math.ceil(this.currentPageNum/10.0) * 10);
        int startNum = tmpEndNum - 9;

        Pageable startPage = this.currentPage;

        System.out.println(tmpEndNum);
        System.out.println(startNum);

        for(int i = startNum; i < this.currentPageNum; i++){
            startPage = startPage.previousOrFirst();
        }

        this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

        if(this.totalPageNum < tmpEndNum){
            tmpEndNum = this.totalPageNum;
            this.nextPage = null;
        }
        for(int i = startNum; i <= tmpEndNum; i++){
            startPage = startPage.next();
        }
        this.nextPage = startPage.getPageNumber() + 1 < totalPageNum ? startPage : null;
    }
}
