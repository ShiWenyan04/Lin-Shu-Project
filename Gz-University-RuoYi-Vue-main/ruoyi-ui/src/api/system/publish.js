import request from '@/utils/request'

// 查询自媒体发布列表
export function listPublish(query) {
  return request({
    url: '/system/publish/list',
    method: 'get',
    params: query
  })
}

// 查询自媒体发布详细
export function getPublish(id) {
  return request({
    url: '/system/publish/' + id,
    method: 'get'
  })
}

// 新增自媒体发布
export function addPublish(data) {
  return request({
    url: '/system/publish',
    method: 'post',
    data: data
  })
}

// 修改自媒体发布
export function updatePublish(data) {
  return request({
    url: '/system/publish',
    method: 'put',
    data: data
  })
}

// 删除自媒体发布
export function delPublish(id) {
  return request({
    url: '/system/publish/' + id,
    method: 'delete'
  })
}
