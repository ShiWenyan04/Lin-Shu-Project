import request from '@/utils/request'

// 查询特种设备考证信息列表
export function listInfo(query) {
  return request({
    url: '/system/specialinfo/list',
    method: 'get',
    params: query
  })
}

// 查询特种设备考证信息详细
export function getInfo(id) {
  return request({
    url: '/system/specialinfo/' + id,
    method: 'get'
  })
}

// 新增特种设备考证信息
export function addInfo(data) {
  return request({
    url: '/system/specialinfo',
    method: 'post',
    data: data
  })
}

// 修改特种设备考证信息
export function updateInfo(data) {
  return request({
    url: '/system/specialinfo',
    method: 'put',
    data: data
  })
}

// 删除特种设备考证信息
export function delInfo(id) {
  return request({
    url: '/system/specialinfo/' + id,
    method: 'delete'
  })
}
