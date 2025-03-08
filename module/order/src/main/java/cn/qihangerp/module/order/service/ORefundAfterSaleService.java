package cn.qihangerp.module.order.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;

import cn.qihangerp.module.order.domain.ORefundAfterSale;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【o_after_sale(OMS售后处理表)】的数据库操作Service
* @createDate 2024-09-15 21:30:30
*/
public interface ORefundAfterSaleService extends IService<ORefundAfterSale> {
    PageResult<ORefundAfterSale> queryPageList(ORefundAfterSale bo, PageQuery pageQuery);
}
