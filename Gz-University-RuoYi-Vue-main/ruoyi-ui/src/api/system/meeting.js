import request from '@/utils/request'

// 查询学术会议信息列表
export function listMeeting(query) {
  return request({
    url: '/system/meeting/list',
    method: 'get',
    params: query
  })
}

// 查询学术会议信息详细
export function getMeeting(id) {
  return request({
    url: '/system/meeting/' + id,
    method: 'get'
  })
}

// 新增学术会议信息
export function addMeeting(data) {
  return request({
    url: '/system/meeting',
    method: 'post',
    data: data
  })
}

// 修改学术会议信息
export function updateMeeting(data) {
  return request({
    url: '/system/meeting',
    method: 'put',
    data: data
  })
}

// 删除学术会议信息
export function delMeeting(id) {
  return request({
    url: '/system/meeting/' + id,
    method: 'delete'
  })
}

// ================== 以下是你缺失的接口 ==================

// 对应后端: @GetMapping("/system/meeting/signup/list/{meetingId}")
export function getSignupList(meetingId) {
  return request({
    url: '/system/meeting/signup/list/' + meetingId,
    method: 'get'
  })
}

// 2. 学生报名
// 对应后端: @PostMapping("/system/meeting/signup/student/{meetingId}")
export function studentSignup(meetingId) {
  return request({
    url: '/system/meeting/signup/student/' + meetingId,
    method: 'post'
  })
}

// 3. 学生取消报名
// 对应后端: @PostMapping("/system/meeting/signup/cancel/{meetingId}")
export function studentCancelSignup(meetingId) {
  return request({
    url: '/system/meeting/signup/cancel/' + meetingId,
    method: 'post'
  })
}
