import request from '@/utils/request'

//加入购物车
// goodsId =>商品 
// goodsSkuId => 商品规格id
export const addCart = (goodsId, goodsNum, goodsSkuId) => {
  return request.post('/cart/add', {
    goodsId,
    goodsNum,
    goodsSkuId
  })
}

//获取购物车列表
export const getCartList = () => {
  return request.get('/api/mall-api/cart/list')
}

//更新购物车商品数量
export const changeCount = (goodsId, goodsNum, goodsSkuId) => {
  return request.post('/cart/update', {
    goodsId,
    goodsNum,
    goodsSkuId
  })
}

//删除购物车商品
export const delSelect = (cartIds) => {
  return request.post('/cart/clear', {
    cartIds
  })
}