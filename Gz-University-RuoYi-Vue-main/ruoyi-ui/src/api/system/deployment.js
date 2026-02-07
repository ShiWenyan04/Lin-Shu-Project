import request from '@/utils/request'

// 查询红外相机布设管理列表
export function listDeployment(query) {
  return request({
    url: '/system/deployment/list',
    method: 'get',
    params: query
  })
}

// 查询红外相机布设管理详细
export function getDeployment(id) {
  return request({
    url: '/system/deployment/' + id,
    method: 'get'
  })
}

// 新增红外相机布设管理
export function addDeployment(data) {
  return request({
    url: '/system/deployment',
    method: 'post',
    data: data
  })
}

// 修改红外相机布设管理
export function updateDeployment(data) {
  return request({
    url: '/system/deployment',
    method: 'put',
    data: data
  })
}

// 删除红外相机布设管理
export function delDeployment(id) {
  return request({
    url: '/system/deployment/' + id,
    method: 'delete'
  })
}
