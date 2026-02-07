import request from '@/utils/request'

// 查询购买支出信息列表
export function listPurchase(query) {
  return request({
    url: '/system/purchase/list',
    method: 'get',
    params: query
  })
}

// 查询购买支出信息详细
export function getPurchase(id) {
  return request({
    url: '/system/purchase/' + id,
    method: 'get'
  })
}

// 新增购买支出信息
export function addPurchase(data) {
  return request({
    url: '/system/purchase',
    method: 'post',
    data: data
  })
}

// 修改购买支出信息
export function updatePurchase(data) {
  return request({
    url: '/system/purchase',
    method: 'put',
    data: data
  })
}

// 删除购买支出信息
export function delPurchase(id) {
  return request({
    url: '/system/purchase/' + id,
    method: 'delete'
  })
}
// 审核购买支出 (这是你缺少的那个！)
export function auditPurchase(data) {
  return request({
    url: '/system/purchase/audit',
    method: 'put',
    data: data
  })
}
