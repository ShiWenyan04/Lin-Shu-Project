import request from '@/utils/request'

// 查询外业启动申请列表
export function listStart(query) {
  return request({
    url: '/system/start/list',
    method: 'get',
    params: query
  })
}

// 查询外业启动申请详细
export function getStart(startId) {
  return request({
    url: '/system/start/' + startId,
    method: 'get'
  })
}

// 新增外业启动申请
export function addStart(data) {
  return request({
    url: '/system/start',
    method: 'post',
    data: data
  })
}

// 修改外业启动申请
export function updateStart(data) {
  return request({
    url: '/system/start',
    method: 'put',
    data: data
  })
}

// 删除外业启动申请
export function delStart(startId) {
  return request({
    url: '/system/start/' + startId,
    method: 'delete'
  })

}
// 审核接口
export function auditStart(data) {
  return request({
    url: '/system/start/audit',
    method: 'post',
    data: data
  })
}
