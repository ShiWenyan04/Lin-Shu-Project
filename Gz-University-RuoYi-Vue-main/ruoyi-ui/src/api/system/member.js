import request from '@/utils/request'

// 查询团队成员列表
export function listMember(query) {
  return request({
    url: '/system/member/list',
    method: 'get',
    params: query
  })
}

// 查询团队成员详细
export function getMember(id) {
  return request({
    url: '/system/member/' + id,
    method: 'get'
  })
}

// 新增团队成员
export function addMember(data) {
  return request({
    url: '/system/member',
    method: 'post',
    data: data
  })
}

// 修改团队成员
export function updateMember(data) {
  return request({
    url: '/system/member',
    method: 'put',
    data: data
  })
}

// 删除团队成员
export function delMember(id) {
  return request({
    url: '/system/member/' + id,
    method: 'delete'
  })
}
