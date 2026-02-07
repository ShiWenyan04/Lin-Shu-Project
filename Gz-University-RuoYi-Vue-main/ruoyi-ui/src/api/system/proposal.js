import request from '@/utils/request'

// 查询开题与学位论文列表
export function listProposal(query) {
  return request({
    url: '/system/proposal/list',
    method: 'get',
    params: query
  })
}

// 查询开题与学位论文详细
export function getProposal(id) {
  return request({
    url: '/system/proposal/' + id,
    method: 'get'
  })
}

// 新增开题与学位论文
export function addProposal(data) {
  return request({
    url: '/system/proposal',
    method: 'post',
    data: data
  })
}

// 修改开题与学位论文
export function updateProposal(data) {
  return request({
    url: '/system/proposal',
    method: 'put',
    data: data
  })
}

// 删除开题与学位论文
export function delProposal(id) {
  return request({
    url: '/system/proposal/' + id,
    method: 'delete'
  })
}
