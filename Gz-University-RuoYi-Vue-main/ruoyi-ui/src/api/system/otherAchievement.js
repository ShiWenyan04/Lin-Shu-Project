import request from '@/utils/request'

// 查询其他成果管理列表
export function listOtherAchievement(query) {
  return request({
    url: '/system/otherAchievement/list',
    method: 'get',
    params: query
  })
}

// 查询其他成果管理详细
export function getOtherAchievement(id) {
  return request({
    url: '/system/otherAchievement/' + id,
    method: 'get'
  })
}

// 新增其他成果管理
export function addOtherAchievement(data) {
  return request({
    url: '/system/otherAchievement',
    method: 'post',
    data: data
  })
}

// 修改其他成果管理
export function updateOtherAchievement(data) {
  return request({
    url: '/system/otherAchievement',
    method: 'put',
    data: data
  })
}

// 删除其他成果管理
export function delOtherAchievement(id) {
  return request({
    url: '/system/otherAchievement/' + id,
    method: 'delete'
  })
}
// 审核其他成果管理 (新增这一段)
export function auditOtherAchievement(data) {
  return request({
    url: '/system/otherAchievement/audit',
    method: 'put',
    data: data
  })
}
