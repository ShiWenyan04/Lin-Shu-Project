import request from '@/utils/request'

// 查询红外相机位点信息列表
export function listPoint(query) {
  return request({
    url: '/system/point/list',
    method: 'get',
    params: query
  })
}

// 查询红外相机位点信息详细
export function getPoint(id) {
  return request({
    url: '/system/point/' + id,
    method: 'get'
  })
}

// 新增红外相机位点信息
export function addPoint(data) {
  return request({
    url: '/system/point',
    method: 'post',
    data: data
  })
}

// 修改红外相机位点信息
export function updatePoint(data) {
  return request({
    url: '/system/point',
    method: 'put',
    data: data
  })
}

// 删除红外相机位点信息
export function delPoint(id) {
  return request({
    url: '/system/point/' + id,
    method: 'delete'
  })
}
