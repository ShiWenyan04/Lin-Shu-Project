import request from '@/utils/request'

// 查询外业数据归档列表
export function listArchive(query) {
  return request({
    url: '/system/archive/list',
    method: 'get',
    params: query
  })
}

// 查询外业数据归档详细
export function getArchive(archiveId) {
  return request({
    url: '/system/archive/' + archiveId,
    method: 'get'
  })
}

// 新增外业数据归档
export function addArchive(data) {
  return request({
    url: '/system/archive',
    method: 'post',
    data: data
  })
}

// 修改外业数据归档
export function updateArchive(data) {
  return request({
    url: '/system/archive',
    method: 'put',
    data: data
  })
}

// 删除外业数据归档
export function delArchive(archiveId) {
  return request({
    url: '/system/archive/' + archiveId,
    method: 'delete'
  })
}

// -------------------------------------------------
// 🚀 新增：审核接口
// -------------------------------------------------
export function auditArchive(data) {
  return request({
    url: '/system/archive/audit',
    method: 'post',
    data: data
  })
}

// -------------------------------------------------
// 🚀 新增：查询“我已完成行程”且“尚未归档”的任务 (用于下拉框)
// -------------------------------------------------
export function listMyFinishedStarts() {
  return request({
    url: '/system/archive/my-finished-starts',
    method: 'get'
  })
}
