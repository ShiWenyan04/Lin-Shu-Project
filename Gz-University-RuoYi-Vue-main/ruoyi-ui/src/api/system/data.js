import request from '@/utils/request'

// 查询毕业生资料管理列表
export function listData(query) {
  return request({
    url: '/system/data/list',
    method: 'get',
    params: query
  })
}

// 查询毕业生资料管理详细
export function getData(id) {
  return request({
    url: '/system/data/' + id,
    method: 'get'
  })
}

// 新增毕业生资料管理
export function addData(data) {
  return request({
    url: '/system/data',
    method: 'post',
    data: data
  })
}

// 修改毕业生资料管理
export function updateData(data) {
  return request({
    url: '/system/data',
    method: 'put',
    data: data
  })
}

// 删除毕业生资料管理
export function delData(id) {
  return request({
    url: '/system/data/' + id,
    method: 'delete'
  })
}
