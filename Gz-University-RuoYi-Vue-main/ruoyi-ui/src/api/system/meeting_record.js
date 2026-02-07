import request from '@/utils/request'

// 查询参会记录列表
export function listMeeting_record(query) {
  return request({
    url: '/system/meeting_record/list',
    method: 'get',
    params: query
  })
}

// 查询参会记录详细
export function getMeeting_record(id) {
  return request({
    url: '/system/meeting_record/' + id,
    method: 'get'
  })
}

// 新增参会记录
export function addMeeting_record(data) {
  return request({
    url: '/system/meeting_record',
    method: 'post',
    data: data
  })
}

// 修改参会记录
export function updateMeeting_record(data) {
  return request({
    url: '/system/meeting_record',
    method: 'put',
    data: data
  })
}

// 删除参会记录
export function delMeeting_record(id) {
  return request({
    url: '/system/meeting_record/' + id,
    method: 'delete'
  })
}
// 下载文件（通用）
export function downloadFile(filePath, fileName) {
  window.open(
    `${process.env.VUE_APP_BASE_API}/system/meeting_record/downloadFile?filePath=${encodeURIComponent(filePath)}&fileName=${encodeURIComponent(fileName || '')}`,
    '_blank'
  )
}

// 审核参会记录
export function auditMeeting_record(data) {
  return request({
    url: '/system/meeting_record/audit',
    method: 'post', // 关键修改：之前这里应该是 'put'，一定要改成 'post'
    data: data
  })
}
