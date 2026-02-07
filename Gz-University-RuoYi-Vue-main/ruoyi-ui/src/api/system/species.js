import request from '@/utils/request'

// 查询野生动物图鉴列表
export function listSpecies(query) {
  return request({
    url: '/system/species/list',
    method: 'get',
    params: query
  })
}

// 查询野生动物图鉴详细
export function getSpecies(id) {
  return request({
    url: '/system/species/' + id,
    method: 'get'
  })
}

// 新增野生动物图鉴
export function addSpecies(data) {
  return request({
    url: '/system/species',
    method: 'post',
    data: data
  })
}

// 修改野生动物图鉴
export function updateSpecies(data) {
  return request({
    url: '/system/species',
    method: 'put',
    data: data
  })
}

// 删除野生动物图鉴
export function delSpecies(id) {
  return request({
    url: '/system/species/' + id,
    method: 'delete'
  })
}
