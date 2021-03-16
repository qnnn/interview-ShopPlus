import request from '@/utils/request'


/**
 * 查看日志信息
 * @returns {AxiosPromise}
 * @param id
 */
export function user(id) {
  return request({
    url: '/logs/user/' + id,
    method: 'get'
  })
}

export function getErrDetail(id) {
  return request({
    url: 'logs/error/' + id,
    method: 'get'
  })
}

export function delAllError() {
  return request({
    url: 'logs/del/error',
    method: 'delete'
  })
}

export function delAllInfo() {
  return request({
    url: 'logs/del/info',
    method: 'delete'
  })
}
