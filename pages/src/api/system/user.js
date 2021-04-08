import request from '@/utils/request'

export function getAll() {
  return request({
    url: '/system/user/',
    method: 'get'
  })
}

export function getById(id) {
  return request({
    url: '/system/user/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/system/user/add',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/system/user/delete',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: '/system/user/edit',
    method: 'post',
    data
  })
}

export default {getById, add, edit, del}
