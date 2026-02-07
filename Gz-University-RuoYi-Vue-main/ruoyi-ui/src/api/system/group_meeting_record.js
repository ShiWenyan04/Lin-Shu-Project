import request from '@/utils/request'

// 查询大组会实施记录页列表
export function listGroup_meeting_record(query) {
  return request({
    url: '/system/group_meeting_record/list',
    method: 'get',
    params: query
  })
}

// 查询大组会实施记录页详细
export function getGroup_meeting_record(id) {
  return request({
    url: '/system/group_meeting_record/' + id,
    method: 'get'
  })
}

// 新增大组会实施记录页
export function addGroup_meeting_record(data) {
  return request({
    url: '/system/group_meeting_record',
    method: 'post',
    data: data
  })
}

// 修改大组会实施记录页
export function updateGroup_meeting_record(data) {
  return request({
    url: '/system/group_meeting_record',
    method: 'put',
    data: data
  })
}

// 删除大组会实施记录页
export function delGroup_meeting_record(id) {
  return request({
    url: '/system/group_meeting_record/' + id,
    method: 'delete'
  })
}
// 审核大组会实施记录页
export function auditGroup_meeting_record(data) {
  return request({
    url: '/system/group_meeting_record/audit',
    method: 'put',
    data: data
  })
}
