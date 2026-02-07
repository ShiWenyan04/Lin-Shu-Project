import request from '@/utils/request'

// 查询外业行程结束列表
export function listEnd(query) {
  return request({
    url: '/system/end/list',
    method: 'get',
    params: query
  })
}

// 查询外业行程结束详细
export function getEnd(endId) {
  return request({
    url: '/system/end/' + endId,
    method: 'get'
  })
}

// 新增外业行程结束
export function addEnd(data) {
  return request({
    url: '/system/end',
    method: 'post',
    data: data
  })
}

// 修改外业行程结束
export function updateEnd(data) {
  return request({
    url: '/system/end',
    method: 'put',
    data: data
  })
}

// 删除外业行程结束
export function delEnd(endId) {
  return request({
    url: '/system/end/' + endId,
    method: 'delete'
  })
}

// ---------------------------------------------
// 🚀 新增：审核接口
// ---------------------------------------------
export function auditEnd(data) {
  return request({
    url: '/system/end/audit',
    method: 'post',
    data: data
  })
}

// ---------------------------------------------
// 🚀 新增：查询我“已通过”且“未填报”的启动申请（下拉框用）
// ---------------------------------------------
export function listMyAvailableStart() {
  return request({
    url: '/system/end/my-starts',
    method: 'get'
  })
}
