import request from '@/utils/request'

// 查询实验设备信息列表
export function listEquipment(query) {
  return request({
    url: '/system/equipment/list',
    method: 'get',
    params: query
  })
}

// 查询实验设备信息详细
export function getEquipment(id) {
  return request({
    url: '/system/equipment/' + id,
    method: 'get'
  })
}

// 新增实验设备信息
export function addEquipment(data) {
  return request({
    url: '/system/equipment',
    method: 'post',
    data: data
  })
}

// 修改实验设备信息
export function updateEquipment(data) {
  return request({
    url: '/system/equipment',
    method: 'put',
    data: data
  })
}

// 删除实验设备信息
export function delEquipment(id) {
  return request({
    url: '/system/equipment/' + id,
    method: 'delete'
  })
}


// 审核实验设备
export function auditEquipment(data) {
  return request({
    url: '/system/equipment/audit', // 确保这个路径和你Controller上的RequestMapping一致
    method: 'put',
    data: data
  })
}
