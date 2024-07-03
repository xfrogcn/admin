// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 获取数据权限 GET /api/data-scopes/${param0}/${param1} */
export async function getDataScopes(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDataScopesParams,
  options?: { [key: string]: any },
) {
  const { targetType: param0, targetId: param1, ...queryParams } = params;
  return request<API.DataScopeDTO[]>(`/api/data-scopes/${param0}/${param1}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 数据赋权 PUT /api/data-scopes/grant */
export async function grantDataScope(
  body: API.GrantDataScopeRequestDTO,
  options?: { [key: string]: any },
) {
  return request<any>('/api/data-scopes/grant', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取用户数据权限 GET /api/data-scopes/users/${param0} */
export async function getUserDataScopes(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserDataScopesParams,
  options?: { [key: string]: any },
) {
  const { userId: param0, ...queryParams } = params;
  return request<API.DataScopeDTO[]>(`/api/data-scopes/users/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}
