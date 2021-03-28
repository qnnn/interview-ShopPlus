import request from '@/utils/request'

export function getMenusTree(pid) {
  return request({
    url: '/system/menu/lazy?pid=' + pid,
    method: 'get'
  })
}

export function getMenus(params) {
  return request({
    url: '/system/menu',
    method: 'get',
    params
  })
}

export function getMenuParent(ids) {
  const data = ids.length || ids.length === 0 ? ids : Array.of(ids)
  return request({
    url: '/system/menu/parent',
    method: 'post',
    data
  })
}

export function getChild(id) {
  return request({
    url: '/system/menu/child?id=' + id,
    method: 'get'
  })
}

export function buildMenus() {
  return request({
    url: '/system/menu/build',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/system/menu/add',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/system/menu/delete',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: '/system/menu/edit',
    method: 'post',
    data
  })
}

export default { add, edit, del, getMenusTree, getMenuParent, getMenus, getChild }
