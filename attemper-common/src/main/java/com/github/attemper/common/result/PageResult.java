package com.github.attemper.common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页号
     */
    protected int currentPage;

    /**
     * 每页条数
     */
    protected int pageSize;

    /**
     * 总条数
     * @return
     */
    protected long total;

}
