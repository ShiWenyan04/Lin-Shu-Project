import request from '@/utils/request'

// 查询学位论文管理列表
export function listThesis(query) {
  return request({
    url: '/system/thesis/list',
    method: 'get',
    params: query
  })
}

// 查询学位论文管理详细
export function getThesis(id) {
  return request({
    url: '/system/thesis/' + id,
    method: 'get'
  })
}

// 新增学位论文管理
export function addThesis(data) {
  return request({
    url: '/system/thesis',
    method: 'post',
    data: data
  })
}

// 修改学位论文管理
export function updateThesis(data) {
  return request({
    url: '/system/thesis',
    method: 'put',
    data: data
  })
}

// 删除学位论文管理
export function delThesis(id) {
  return request({
    url: '/system/thesis/' + id,
    method: 'delete'
  })
}

// 审核学位论文管理
export function auditThesis(data) {
  return request({
    url: '/system/thesis/audit',
    method: 'put',
    data: data
  })
}
