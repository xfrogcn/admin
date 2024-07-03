// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建组织 POST /api/organizations */
export async function createOrganization(
  body: API.CreateOrganizationRequestDTO,
  options?: { [key: string]: any },
) {
  return request<number>('/api/organizations', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询组织 GET /api/organizations/${param0} */
export async function getOrganization(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getOrganizationParams,
  options?: { [key: string]: any },
) {
  const { organizationId: param0, ...queryParams } = params;
  return request<API.OrganizationDTO>(`/api/organizations/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新组织 POST /api/organizations/${param0} */
export async function updateOrganization(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateOrganizationParams,
  body: API.UpdateOrganizationRequestDTO,
  options?: { [key: string]: any },
) {
  const { organizationId: param0, ...queryParams } = params;
  return request<any>(`/api/organizations/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 删除组织 DELETE /api/organizations/${param0} */
export async function deleteOrganization(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteOrganizationParams,
  options?: { [key: string]: any },
) {
  const { organizationId: param0, ...queryParams } = params;
  return request<any>(`/api/organizations/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 查询组织列表 GET /api/organizations/list */
export async function listOrganizations(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listOrganizationsParams,
  options?: { [key: string]: any },
) {
  return request<API.OrganizationDTO[]>('/api/organizations/list', {
    method: 'GET',
    params: {
      ...params,
      arg0: undefined,
      ...params['arg0'],
    },
    ...(options || {}),
  });
}
