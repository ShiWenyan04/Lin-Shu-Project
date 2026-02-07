import request from '@/utils/request'

// 查询外业调查设备列表
export function listSurvey(query) {
  return request({
    url: '/system/survey/list',
    method: 'get',
    params: query
  })
}

// 查询外业调查设备详细
export function getSurvey(id) {
  return request({
    url: '/system/survey/' + id,
    method: 'get'
  })
}

// 新增外业调查设备
export function addSurvey(data) {
  return request({
    url: '/system/survey',
    method: 'post',
    data: data
  })
}

// 修改外业调查设备
export function updateSurvey(data) {
  return request({
    url: '/system/survey',
    method: 'put',
    data: data
  })
}

// 删除外业调查设备
export function delSurvey(id) {
  return request({
    url: '/system/survey/' + id,
    method: 'delete'
  })
}
// 审核
// 审核外业调查设备
export function auditSurvey(data) {
  return request({
    url: '/system/survey/audit',
    method: 'put',
    data: data
  })
}
