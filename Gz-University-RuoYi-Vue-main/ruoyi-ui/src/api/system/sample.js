import request from '@/utils/request'

// 查询测序信息列表
export function listSample(query) {
  return request({
    url: '/system/sample/list',
    method: 'get',
    params: query
  })
}

// 查询测序信息详细
export function getSample(id) {
  return request({
    url: '/system/sample/' + id,
    method: 'get'
  })
}

// 新增测序信息
export function addSample(data) {
  return request({
    url: '/system/sample',
    method: 'post',
    data: data
  })
}

// 修改测序信息
export function updateSample(data) {
  return request({
    url: '/system/sample',
    method: 'put',
    data: data
  })
}

// 删除测序信息
export function delSample(id) {
  return request({
    url: '/system/sample/' + id,
    method: 'delete'
  })
}
