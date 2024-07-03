// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建租户 POST /api/tenants */
export async function createTenant(
  body: API.CreateTenantRequestDTO,
  options?: { [key: string]: any },
) {
  return request<number>('/api/tenants', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新租户 PUT /api/tenants/${param0} */
export async function updateTenant(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateTenantParams,
  body: API.UpdateTenantRequestDTO,
  options?: { [key: string]: any },
) {
  const { tenantId: param0, ...queryParams } = params;
  return request<any>(`/api/tenants/${param0}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 启用或禁用租户 POST /api/tenants/${param0}/${param1} */
export async function enableTenant(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.enableTenantParams,
  options?: { [key: string]: any },
) {
  const { tenantId: param0, enabled: param1, ...queryParams } = params;
  return request<any>(`/api/tenants/${param0}/${param1}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 查询租户列表 POST /api/tenants/list */
export async function listTenants(
  body: API.QueryTenantRequestDTO,
  options?: { [key: string]: any },
) {
  return request<API.PageDTOTenantDTO>('/api/tenants/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
