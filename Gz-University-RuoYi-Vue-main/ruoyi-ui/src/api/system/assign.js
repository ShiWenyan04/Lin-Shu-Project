import request from '@/utils/request'

// 查询组务分工列表
export function listAssign(query) {
  return request({
    url: '/system/assign/list',
    method: 'get',
    params: query
  })
}

// 查询详细
export function getAssign(id) {
  return request({
    url: '/system/assign/' + id,
    method: 'get'
  })
}

// 管理员直接新增
export function addAssign(data) {
  return request({
    url: '/system/assign',
    method: 'post',
    data: data
  })
}

// 学生申请 (对应后端 /apply 接口)
export function applyAssign(data) {
  return request({
    url: '/system/assign/apply',
    method: 'post',
    data: data
  })
}

// 修改
export function updateAssign(data) {
  return request({
    url: '/system/assign',
    method: 'put',
    data: data
  })
}

// 删除
export function delAssign(id) {
  return request({
    url: '/system/assign/' + id,
    method: 'delete'
  })
}

// 审核 (对应后端 /audit 接口)
export function auditAssign(data) {
  return request({
    url: '/system/assign/audit',
    method: 'post',
    data: data
  })
}

// 用户卸任角色
export function cancelRole(roleId) {
  return request({
    url: '/system/assign/cancelRole',
    method: 'post',
    data: { roleId: roleId }
  })
}

export function optionSelectRole() {
  return request({
    // 原来是: url: '/system/role/optionselect'
    // 改为我们刚才在 GroupTaskAssignController 里新写的路径:
    url: '/system/assign/roleOptionSelect',
    method: 'get'
  })

}
