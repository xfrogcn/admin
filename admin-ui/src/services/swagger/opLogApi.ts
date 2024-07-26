// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 查询操作日志 POST /api/oplogs/list */
export async function listOpLogs(body: API.QueryOpLogRequestDTO, options?: { [key: string]: any }) {
  return request<API.PageDTOOpLogDTO>('/api/oplogs/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
