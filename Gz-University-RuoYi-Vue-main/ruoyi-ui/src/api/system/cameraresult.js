import request from '@/utils/request'

// 查询红外相机成果管理列表
export function listCameraresult(query) {
  return request({
    url: '/system/cameraresult/list',
    method: 'get',
    params: query
  })
}

// 查询红外相机成果管理详细
export function getCameraresult(id) {
  return request({
    url: '/system/cameraresult/' + id,
    method: 'get'
  })
}

// 新增红外相机成果管理
export function addCameraresult(data) {
  return request({
    url: '/system/cameraresult',
    method: 'post',
    data: data
  })
}

// 修改红外相机成果管理
export function updateCameraresult(data) {
  return request({
    url: '/system/cameraresult',
    method: 'put',
    data: data
  })
}

// 【新增】审核接口
export function auditCameraresult(data) {
  return request({
    url: '/system/cameraresult/audit',
    method: 'put',
    data: data
  })
}

// 删除红外相机成果管理
export function delCameraresult(id) {
  return request({
    url: '/system/cameraresult/' + id,
    method: 'delete'
  })
}
