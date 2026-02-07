import request from '@/utils/request'

// 查询学术论文投稿列表
export function listSubmission(query) {
  return request({
    url: '/system/submission/list',
    method: 'get',
    params: query
  })
}

// 查询学术论文投稿详细
export function getSubmission(id) {
  return request({
    url: '/system/submission/' + id,
    method: 'get'
  })
}

// 新增学术论文投稿
export function addSubmission(data) {
  return request({
    url: '/system/submission',
    method: 'post',
    data: data
  })
}

// 修改学术论文投稿
export function updateSubmission(data) {
  return request({
    url: '/system/submission',
    method: 'put',
    data: data
  })
}

// 删除学术论文投稿
export function delSubmission(id) {
  return request({
    url: '/system/submission/' + id,
    method: 'delete'
  })
}
