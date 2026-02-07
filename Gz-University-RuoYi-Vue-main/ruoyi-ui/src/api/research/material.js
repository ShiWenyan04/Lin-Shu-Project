import request from '@/utils/request'

// 查询资料清单管理列表
export function listMaterial(query) {
  return request({
    url: '/research/material/list',
    method: 'get',
    params: query
  })
}

// 查询资料清单管理详细
export function getMaterial(id) {
  return request({
    url: '/research/material/' + id,
    method: 'get'
  })
}

// 新增资料清单管理
export function addMaterial(data) {
  return request({
    url: '/research/material',
    method: 'post',
    data: data
  })
}

// 修改资料清单管理
export function updateMaterial(data) {
  return request({
    url: '/research/material',
    method: 'put',
    data: data
  })
}

// 删除资料清单管理
export function delMaterial(id) {
  return request({
    url: '/research/material/' + id,
    method: 'delete'
  })
}
