import request from '@/utils/request'

// 查询外业调查设备借用列表
export function listBorrowing(query) {
  return request({
    url: '/system/borrowing/list',
    method: 'get',
    params: query
  })
}

// 查询外业调查设备借用详细
export function getBorrowing(id) {
  return request({
    url: '/system/borrowing/' + id,
    method: 'get'
  })
}

// 新增外业调查设备借用
export function addBorrowing(data) {
  return request({
    url: '/system/borrowing',
    method: 'post',
    data: data
  })
}

// 修改外业调查设备借用
export function updateBorrowing(data) {
  return request({
    url: '/system/borrowing',
    method: 'put',
    data: data
  })
}

// 删除外业调查设备借用
export function delBorrowing(id) {
  return request({
    url: '/system/borrowing/' + id,
    method: 'delete'
  })
}

// 审核
export function auditBorrowing(data) {
  return request({
    url: '/system/borrowing/audit', // 必须对应 Controller 的 @RequestMapping + /audit
    method: 'put',
    data: data
  })
}
