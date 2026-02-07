import request from '@/utils/request'

// 查询外业信息列表
export function listWork(query) {
  return request({
    url: '/system/work/list',
    method: 'get',
    params: query
  })
}

// 查询外业信息详细
export function getWork(id) {
  return request({
    url: '/system/work/' + id,
    method: 'get'
  })
}

// 新增外业信息
export function addWork(data) {
  return request({
    url: '/system/work',
    method: 'post',
    data: data
  })
}

// 修改外业信息
export function updateWork(data) {
  return request({
    url: '/system/work',
    method: 'put',
    data: data
  })
}

// 删除外业信息
export function delWork(id) {
  return request({
    url: '/system/work/' + id,
    method: 'delete'
  })
}
