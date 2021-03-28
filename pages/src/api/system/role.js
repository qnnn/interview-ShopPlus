import request from '@/utils/request'

// 获取所有的Role
export function getAll() {
  return request({
    url: '/system/role/',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/system/role/add',
    method: 'post',
    data
  })
}

export function get(id) {
  return request({
    url: '/system/role/' + id,
    method: 'get'
  })
}

export function del(ids) {
  return request({
    url: '/system/role/delete',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: '/system/role/update',
    method: 'post',
    data
  })
}

export function editMenu(data) {
  return request({
    url: '/system/role/menu',
    method: 'post',
    data
  })
}

export default { add, edit, del, get, editMenu }
