import request from '@/utils/request'

// 查询学术论文写作列表
export function listPaper(query) {
  return request({
    url: '/system/paper/list',
    method: 'get',
    params: query
  })
}

// 查询学术论文写作详细
export function getPaper(id) {
  return request({
    url: '/system/paper/' + id,
    method: 'get'
  })
}

// 新增学术论文写作
export function addPaper(data) {
  return request({
    url: '/system/paper',
    method: 'post',
    data: data
  })
}

// 修改学术论文写作
export function updatePaper(data) {
  return request({
    url: '/system/paper',
    method: 'put',
    data: data
  })
}

// 删除学术论文写作
export function delPaper(id) {
  return request({
    url: '/system/paper/' + id,
    method: 'delete'
  })
}
