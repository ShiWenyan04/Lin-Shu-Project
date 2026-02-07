import request from '@/utils/request'

// 查询红外相机安装项目列表
export function listCameraInstall(query) {
  return request({
    url: '/system/CameraInstall/list',
    method: 'get',
    params: query
  })
}

// 查询红外相机安装项目详细
export function getCameraInstall(id) {
  return request({
    url: '/system/CameraInstall/' + id,
    method: 'get'
  })
}

// 新增红外相机安装项目
export function addCameraInstall(data) {
  return request({
    url: '/system/CameraInstall',
    method: 'post',
    data: data
  })
}

// 修改红外相机安装项目
export function updateCameraInstall(data) {
  return request({
    url: '/system/CameraInstall',
    method: 'put',
    data: data
  })
}

// 删除红外相机安装项目
export function delCameraInstall(id) {
  return request({
    url: '/system/CameraInstall/' + id,
    method: 'delete'
  })
}
// 6. 新增：导入KML文件
export function importCameraKml(data) {
  return request({
    url: '/system/CameraInstall/importKml', // 对应后端写的 @PostMapping("/importKml")
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' } // 必须加，用于传文件
  })
}

// 7. 新增：查询某项目下的所有点位
export function listCameraPoints(query) {
  return request({
    url: '/system/point/list', // <--- 必须改成和后端 Controller 一致！
    method: 'get',
    params: query
  })
}

// 【新增】导入物种监测数据 Excel
export function importSpeciesExcel(data) {
  return request({
    url: '/system/CameraInstall/importExcel',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 【新增】手动添加数据
export function addManualData(data) {
  return request({
    url: '/system/CameraInstall/addManual',
    method: 'post',
    data: data
  })
}
// 【新增】审核接口
export function auditCameraInstall(data) {
  return request({
    url: '/system/CameraInstall/audit',
    method: 'put',
    data: data
  })
}
