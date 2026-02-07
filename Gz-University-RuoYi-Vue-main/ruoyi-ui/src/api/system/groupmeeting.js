import request from '@/utils/request'

// 查询大组会安排列表
export function listGroupmeeting(query) {
  return request({
    url: '/system/groupmeeting/list',
    method: 'get',
    params: query
  })
}

// 查询大组会安排详细
export function getGroupmeeting(id) {
  return request({
    url: '/system/groupmeeting/' + id,
    method: 'get'
  })
}
// 新增大组会安排
export function addGroupmeeting(data) {
  return request({
    url: '/system/groupmeeting',
    method: 'post',
    data: data
  })
}

// 修改大组会安排
export function updateGroupmeeting(data) {
  return request({
    url: '/system/groupmeeting',
    method: 'put',
    data: data
  })
}

// 删除大组会安排
export function delGroupmeeting(id) {
  return request({
    url: '/system/groupmeeting/' + id,
    method: 'delete'
  })
}
