import request from '@/utils/request'

// 查询本科论文指导列表
export function listGuidance(query) {
  return request({
    url: '/system/guidance/list',
    method: 'get',
    params: query
  })
}

// 查询本科论文指导详细
export function getGuidance(id) {
  return request({
    url: '/system/guidance/' + id,
    method: 'get'
  })
}

// 新增本科论文指导
export function addGuidance(data) {
  return request({
    url: '/system/guidance',
    method: 'post',
    data: data
  })
}

// 修改本科论文指导
export function updateGuidance(data) {
  return request({
    url: '/system/guidance',
    method: 'put',
    data: data
  })
}

// 删除本科论文指导
export function delGuidance(id) {
  return request({
    url: '/system/guidance/' + id,
    method: 'delete'
  })
}

// 🚀 新增审核接口
export function auditGuidance(data) {
  return request({
    url: '/system/guidance/audit',
    method: 'post',
    data: data
  })
}
