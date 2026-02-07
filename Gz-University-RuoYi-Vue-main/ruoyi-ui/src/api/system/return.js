import request from '@/utils/request'

// 查询红外相机归还列表
export function listReturn(query) {
  return request({
    url: '/system/return/list',
    method: 'get',
    params: query
  })
}

// 查询红外相机归还详细
export function getReturn(id) {
  return request({
    url: '/system/return/' + id,
    method: 'get'
  })
}

// 新增红外相机归还
export function addReturn(data) {
  return request({
    url: '/system/return',
    method: 'post',
    data: data
  })
}

// 修改红外相机归还
export function updateReturn(data) {
  return request({
    url: '/system/return',
    method: 'put',
    data: data
  })
}

// 删除红外相机归还
export function delReturn(id) {
  return request({
    url: '/system/return/' + id,
    method: 'delete'
  })
}

// === 新增：审核接口 ===
export function auditReturn(data) {
  return request({
    url: '/system/return/audit',
    method: 'put',
    data: data
  })
}
