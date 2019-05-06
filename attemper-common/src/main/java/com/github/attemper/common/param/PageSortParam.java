package com.github.attemper.common.param;

import com.github.attemper.common.constant.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * page sort param
 * @author ldang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageSortParam implements CommonParam {

    /**
     * current page
     */
    protected int currentPage;

    /**
     * data size of every page
     */
    protected int pageSize;

    /**
     * sort field
     */
    protected String sort;


    public String validate() {
        if(this.pageSize > CommonConstants.MAX_PAGE_SIZE){
            return "1502";
        }
        return null;
    }

    public void preHandle() {
        //preview handle currentPage
        if(this.currentPage == 0){
            this.currentPage = CommonConstants.DEF_CURRENT_PAGE;
        }
        //preview handle pageSize
        if(this.pageSize == 0){
            this.pageSize = CommonConstants.DEF_PAGE_SIZE;
        }
    }

}
