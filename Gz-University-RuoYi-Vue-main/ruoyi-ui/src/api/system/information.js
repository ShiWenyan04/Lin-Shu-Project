import request from '@/utils/request'

// 查询采样信息列表
export function listInformation(query) {
  return request({
    url: '/system/information/list',
    method: 'get',
    params: query
  })
}

// 查询采样信息详细
export function getInformation(id) {
  return request({
    url: '/system/information/' + id,
    method: 'get'
  })
}

// 新增采样信息
export function addInformation(data) {
  return request({
    url: '/system/information',
    method: 'post',
    data: data
  })
}

// 修改采样信息
export function updateInformation(data) {
  return request({
    url: '/system/information',
    method: 'put',
    data: data
  })
}

// 删除采样信息
export function delInformation(id) {
  return request({
    url: '/system/information/' + id,
    method: 'delete'
  })
}
// 用样操作
export function useSample(data) {
  return request({
    url: '/system/information/useSample',
    method: 'post',
    data: data
  })
}
// 检查样本编号是否唯一
export function checkSampleNameUnique(data) {
  return request({
    url: '/system/information/checkSampleNameUnique',
    method: 'post',
    data: data
  })
}
// 批量用样
export function batchUseSample(data) {
  return request({
    url: '/system/information/batchUseSample',
    method: 'post',
    data: data
  })
}
// 导入采样信息数据
export function importData(data) {
  return request({
    url: '/system/information/importData',
    method: 'post',
    data: data
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/system/information/importTemplate',
    method: 'get',
    responseType: 'blob'
  })
}
