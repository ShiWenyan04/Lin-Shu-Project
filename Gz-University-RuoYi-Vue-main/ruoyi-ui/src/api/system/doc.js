import request from '@/utils/request'

// 查询外业文档模板列表
export function listDoc(query) {
  return request({
    url: '/system/doc/list',
    method: 'get',
    params: query
  })
}

// 查询外业文档模板详细
export function getDoc(docId) {
  return request({
    url: '/system/doc/' + docId,
    method: 'get'
  })
}

// 新增外业文档模板
export function addDoc(data) {
  return request({
    url: '/system/doc',
    method: 'post',
    data: data
  })
}

// 修改外业文档模板
export function updateDoc(data) {
  return request({
    url: '/system/doc',
    method: 'put',
    data: data
  })
}

// 删除外业文档模板
export function delDoc(docId) {
  return request({
    url: '/system/doc/' + docId,
    method: 'delete'
  })
}
