import request from '@/utils/request'

// 查询店铺订单列表
export function listOrder(query) {
  return request({
    url: '/api/oms-api/order/list',
    method: 'get',
    params: query
  })
}


// 查询待分配发货订单列表
export function waitDistOrderList(query) {
  return request({
    url: '/api/oms-api/order/wait_dist_order_list',
    method: 'get',
    params: query
  })
}
// 查询店铺订单详细
export function getOrder(id) {
  return request({
    url: '/api/oms-api/order/' + id,
    method: 'get'
  })
}

// 订单明细list
export function listOrderItem(query) {
  return request({
    url: '/api/oms-api/order/item_list',
    method: 'get',
    params: query
  })
}

export function updateErpSkuId(data) {
  return request({
    url: '/api/oms-api/order/updateErpSkuId',
    method: 'post',
    data: data
  })
}

export function shipOrder(data) {
  return request({
    url: '/api/order/ship',
    method: 'post',
    data: data
  })
}

export function pushErp(id) {
  return request({
    url: '/api/oms-api/order/pushErp/' + id,
    method: 'post'
  })
}

// 新增店铺订单
export function addOrder(data) {
  return request({
    url: '/api/oms-api/order',
    method: 'post',
    data: data
  })
}

export function orderItemSpecIdUpdate(data) {
  return request({
    url: '/api/oms-api/order/order_item_spec_id_update',
    method: 'post',
    data: data
  })
}
