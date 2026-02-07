import request from '@/utils/request'

// 查询物种百科资料库列表
export function listLibrary(query) {
  return request({
    url: '/system/library/list',
    method: 'get',
    params: query
  })
}

// 查询物种百科资料库详细
export function getLibrary(id) {
  return request({
    url: '/system/library/' + id,
    method: 'get'
  })
}

// 新增物种百科资料库
export function addLibrary(data) {
  return request({
    url: '/system/library',
    method: 'post',
    data: data
  })
}

// 修改物种百科资料库
export function updateLibrary(data) {
  return request({
    url: '/system/library',
    method: 'put',
    data: data
  })
}

// 删除物种百科资料库
export function delLibrary(id) {
  return request({
    url: '/system/library/' + id,
    method: 'delete'
  })
}
