import request from '@/utils/request'

// 查询红外相机数据列表
export function listStatistics(query) {
  return request({
    url: '/system/statistics/list',
    method: 'get',
    params: query
  })
}

// 查询红外相机数据详细
export function getStatistics(id) {
  return request({
    url: '/system/statistics/' + id,
    method: 'get'
  })
}

// 新增红外相机数据
export function addStatistics(data) {
  return request({
    url: '/system/statistics',
    method: 'post',
    data: data
  })
}

// 修改红外相机数据
export function updateStatistics(data) {
  return request({
    url: '/system/statistics',
    method: 'put',
    data: data
  })
}

// 删除红外相机数据
export function delStatistics(id) {
  return request({
    url: '/system/statistics/' + id,
    method: 'delete'
  })
}

