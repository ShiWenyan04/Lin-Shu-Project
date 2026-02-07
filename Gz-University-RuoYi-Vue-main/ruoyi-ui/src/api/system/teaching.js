import request from '@/utils/request'

// 查询研究生助教安排列表
export function listTeaching(query) {
  return request({
    url: '/system/teaching/list',
    method: 'get',
    params: query
  })
}

// 查询研究生助教安排详细
export function getTeaching(id) {
  return request({
    url: '/system/teaching/' + id,
    method: 'get'
  })
}

// 新增研究生助教安排
export function addTeaching(data) {
  return request({
    url: '/system/teaching',
    method: 'post',
    data: data
  })
}

// 修改研究生助教安排
export function updateTeaching(data) {
  return request({
    url: '/system/teaching',
    method: 'put',
    data: data
  })
}

// 删除研究生助教安排
export function delTeaching(id) {
  return request({
    url: '/system/teaching/' + id,
    method: 'delete'
  })
}

// 🚀 新增审核接口
export function auditTeaching(data) {
  return request({
    url: '/system/teaching/audit',
    method: 'post',
    data: data
  })
}
