package cn.qihangerp.module.order.service.impl;


import cn.qihangerp.module.goods.domain.OGoodsSku;
import cn.qihangerp.module.goods.domain.OGoods;
import cn.qihangerp.module.goods.mapper.OGoodsMapper;

import cn.qihangerp.module.goods.mapper.OGoodsSkuMapper;
import cn.qihangerp.module.goods.service.OGoodsSkuService;
import cn.qihangerp.module.order.domain.OOrderItem;
import cn.qihangerp.module.order.domain.bo.OrderItemListBo;
import cn.qihangerp.module.order.domain.bo.OrderItemSpecIdUpdateBo;
import cn.qihangerp.module.order.domain.vo.OrderItemListVo;
import cn.qihangerp.module.order.domain.vo.SalesTopSkuVo;
import cn.qihangerp.module.order.mapper.OOrderItemMapper;
import cn.qihangerp.module.order.service.OOrderItemService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author TW
* @description 针对表【o_order_item(订单明细表)】的数据库操作Service实现
* @createDate 2024-03-11 14:24:49
*/
@AllArgsConstructor
@Service
public class OOrderItemServiceImpl extends ServiceImpl<OOrderItemMapper, OOrderItem>
    implements OOrderItemService {
    private final OOrderItemMapper mapper;
    private final OGoodsSkuMapper goodsSkuMapper;
    private final OGoodsSkuService goodsSkuService;
    private final OGoodsMapper goodsMapper;
    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);


    @Override
    public PageResult<OrderItemListVo> selectPageVo(PageQuery pageQuery, OrderItemListBo bo) {
        if(StringUtils.hasText(bo.getStartTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getStartTime());
            boolean b = matcher.find();
            if(b){
                bo.setStartTime(bo.getStartTime()+" 00:00:00");
            }
        }
        if(StringUtils.hasText(bo.getEndTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getEndTime());
            boolean b = matcher.find();
            if(b){
                bo.setEndTime(bo.getEndTime()+" 23:59:59");
            }
        }
        var pages = mapper.selectPageVo(pageQuery.build(), bo);
        return PageResult.build(pages);
    }

    @Override
    public List<OrderItemListVo> selectExportListVo(OrderItemListBo bo) {
        if(StringUtils.hasText(bo.getStartTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getStartTime());
            boolean b = matcher.find();
            if(b){
                bo.setStartTime(bo.getStartTime()+" 00:00:00");
            }
        }
        if(StringUtils.hasText(bo.getEndTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getEndTime());
            boolean b = matcher.find();
            if(b){
                bo.setEndTime(bo.getEndTime()+" 23:59:59");
            }
        }
        return mapper.selectListVo(bo);
    }

    @Override
    public PageResult<OOrderItem> queryPageList(OOrderItem bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OOrderItem> queryWrapper = new LambdaQueryWrapper<OOrderItem>()
//                .eq(bo.getShopId()!=null,OOrder::getShopId,bo.getShopId())
                .eq(org.springframework.util.StringUtils.hasText(bo.getOrderNum()),OOrderItem::getOrderNum,bo.getOrderNum())
//                .eq(bo.getOrderStatus()!=null,OOrder::getOrderStatus,bo.getOrderStatus())
//                .eq(org.springframework.util.StringUtils.hasText(bo.getShippingNumber()),OOrder::getShippingNumber,bo.getShippingNumber())
//                .eq(org.springframework.util.StringUtils.hasText(bo.getReceiverName()),OOrder::getReceiverName,bo.getReceiverName())
//                .like(org.springframework.util.StringUtils.hasText(bo.getReceiverMobile()),OOrder::getReceiverMobile,bo.getReceiverMobile())
                ;
        Page<OOrderItem> pages = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(pages);
    }

    @Override
    public ResultVo<Integer> updateErpSkuId(Long id, String skuCode) {
        OGoodsSku oGoodsSku = goodsSkuService.getGoodsSkuByCode(skuCode);
        if(oGoodsSku == null) return ResultVo.error("找不到skuCode");
        OOrderItem update = new OOrderItem();
        update.setId(id.toString());
        update.setGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        update.setGoodsSkuId(oGoodsSku.getId());
        update.setUpdateBy("手动修改ERP SKU ID");
        update.setUpdateTime(new Date());
        mapper.updateById(update);
        return ResultVo.success();
    }

    @Override
    public List<OOrderItem> getOrderItemListByOrderId(Long orderId) {
        if(orderId == null || orderId<=0)
            return null;
        else {
            return mapper.selectList(new LambdaQueryWrapper<OOrderItem>().eq(OOrderItem::getOrderId, orderId));
        }
    }

    @Override
    public List<OOrderItem> getOrderItemListByOrderNum(String orderId) {
        return mapper.selectList(new LambdaQueryWrapper<OOrderItem>().eq(OOrderItem::getOrderNum, orderId));
    }

    @Override
    public List<SalesTopSkuVo> selectTopSku(String startDate, String endDate) {
        return mapper.selectTopSku(startDate,endDate);
    }

    @Override
    public int orderItemSpecIdUpdate(OrderItemSpecIdUpdateBo bo) {
        OOrderItem erpSaleOrderItem = mapper.selectById(bo.getOrderItemId());
        if(erpSaleOrderItem == null )return -1;
        OGoodsSku oGoodsSku = goodsSkuMapper.selectById(bo.getErpGoodsSpecId());
        if(oGoodsSku== null) return -2;

        OGoods goods = goodsMapper.selectById(oGoodsSku.getGoodsId());

        if(goods==null) return -2;

        OOrderItem update = new OOrderItem();
        update.setId(bo.getOrderItemId().toString());
        update.setGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        update.setSkuId(oGoodsSku.getId());
        update.setSkuNum(oGoodsSku.getSkuCode());
//        update.setSupplierId(goods.getSupplierId());
        update.setUpdateBy("手动修改SkuId,原sku"+erpSaleOrderItem.getSkuNum());
        update.setUpdateTime(new Date());
        mapper.updateById(update);
        return 0;
    }

    /**
     * 获取待分配发货的订单（并且是商品存在供应商ID的）
     * @param limit
     * @return
     */
    @Override
    public String[] waitDistributeShip(Integer limit) {
        if(limit==null||limit<=0) limit=10;
        List<OOrderItem> oOrderItems = mapper.selectList(new LambdaQueryWrapper<OOrderItem>()
                .eq(OOrderItem::getShipStatus, 0)
//                        .eq(OOrderItem::getRefundStatus,1)
//                        .eq(OOrderItem::getOrderStatus,1)
                .gt(OOrderItem::getGoodsSkuId, 0)
                .select(OOrderItem::getId)
                .last(" ORDER BY id desc LIMIT "+limit)

        );
        return oOrderItems.stream().map(OOrderItem::getId).toArray(String[]::new);
    }
}




