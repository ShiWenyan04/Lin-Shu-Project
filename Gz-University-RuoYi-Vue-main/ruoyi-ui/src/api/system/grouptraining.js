import request from '@/utils/request'

// 查询实训与竞赛活动列表
export function listGrouptraining(query) {
  return request({
    url: '/system/grouptraining/list',
    method: 'get',
    params: query
  })
}

// 查询实训与竞赛活动详细
export function getGrouptraining(id) {
  return request({
    url: '/system/grouptraining/' + id,
    method: 'get'
  })
}

// 新增实训与竞赛活动
export function addGrouptraining(data) {
  return request({
    url: '/system/grouptraining',
    method: 'post',
    data: data
  })
}

// 修改实训与竞赛活动
export function updateGrouptraining(data) {
  return request({
    url: '/system/grouptraining',
    method: 'put',
    data: data
  })
}

// 删除实训与竞赛活动
export function delGrouptraining(id) {
  return request({
    url: '/system/grouptraining/' + id,
    method: 'delete'
  })
}
