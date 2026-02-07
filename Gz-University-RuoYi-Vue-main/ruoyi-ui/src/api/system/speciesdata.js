import request from '@/utils/request'

// 查询物种监测数据列表
export function listSpeciesdata(query) {
  return request({
    url: '/system/speciesdata/list',
    method: 'get',
    params: query
  })
}

// 查询物种监测数据详细
export function getSpeciesdata(id) {
  return request({
    url: '/system/speciesdata/' + id,
    method: 'get'
  })
}

// 新增物种监测数据
export function addSpeciesdata(data) {
  return request({
    url: '/system/speciesdata',
    method: 'post',
    data: data
  })
}

// 修改物种监测数据
export function updateSpeciesdata(data) {
  return request({
    url: '/system/speciesdata',
    method: 'put',
    data: data
  })
}

// 删除物种监测数据
export function delSpeciesdata(id) {
  return request({
    url: '/system/speciesdata/' + id,
    method: 'delete'
  })
}
