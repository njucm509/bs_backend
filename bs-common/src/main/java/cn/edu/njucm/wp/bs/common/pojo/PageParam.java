package cn.edu.njucm.wp.bs.common.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageParam {
    private Integer page = 1; // 当前页
    private Integer rows = 5; // 每页大小
    private String sortBy; // 排序字段
    private Boolean desc = false; // 是否降序
    private String key; // 搜索关键词
}
