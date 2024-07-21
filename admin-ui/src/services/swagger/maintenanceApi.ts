// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 获取缓存列表 GET /api/maintenance/caches */
export async function listCaches(options?: { [key: string]: any }) {
  return request<API.CacheDTO[]>('/api/maintenance/caches', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 清空缓存 DELETE /api/maintenance/caches/clear/${param0} */
export async function clearCaches(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.clearCachesParams,
  options?: { [key: string]: any },
) {
  const { cacheName: param0, ...queryParams } = params;
  return request<any>(`/api/maintenance/caches/clear/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}
