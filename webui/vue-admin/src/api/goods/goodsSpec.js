import request from '@/utils/request'

// 查询商品规格库存管理列表
export function listGoodsSpec(query) {
  return request({
    url: '/api/ams-api/goods/sku_list',
    method: 'get',
    params: query
  })
}

// 查询商品规格库存管理详细
export function getGoodsSpec(id) {
  return request({
    url: '/api/ams-api/goods/sku/' + id,
    method: 'get'
  })
}

// 新增商品规格库存管理
export function addGoodsSpec(data) {
  return request({
    url: '/api/ams-api/goods/goodsSku',
    method: 'post',
    data: data
  })
}

// 修改商品规格库存管理
export function updateGoodsSpec(data) {
  return request({
    url: '/api/ams-api/goods/sku',
    method: 'put',
    data: data
  })
}
