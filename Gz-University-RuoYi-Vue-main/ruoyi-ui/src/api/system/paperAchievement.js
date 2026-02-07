import request from '@/utils/request'

// 查询论文成果管理列表
export function listPaperAchievement(query) {
  return request({
    url: '/system/paperAchievement/list',
    method: 'get',
    params: query
  })
}

// 查询论文成果管理详细
export function getPaperAchievement(id) {
  return request({
    url: '/system/paperAchievement/' + id,
    method: 'get'
  })
}

// 新增论文成果管理
export function addPaperAchievement(data) {
  return request({
    url: '/system/paperAchievement',
    method: 'post',
    data: data
  })
}

// 修改论文成果管理
export function updatePaperAchievement(data) {
  return request({
    url: '/system/paperAchievement',
    method: 'put',
    data: data
  })
}

// 删除论文成果管理
export function delPaperAchievement(id) {
  return request({
    url: '/system/paperAchievement/' + id,
    method: 'delete'
  })
}

// 审核论文成果管理
export function auditPaperAchievement(data) {
  return request({
    url: '/system/paperAchievement/audit',
    method: 'put',
    data: data
  })
}
