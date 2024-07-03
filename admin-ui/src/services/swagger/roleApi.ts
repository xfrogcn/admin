// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建角色 POST /api/roles */
export async function createRole(body: API.CreateRoleRequestDTO, options?: { [key: string]: any }) {
  return request<number>('/api/roles', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新角色 POST /api/roles/${param0} */
export async function updateRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateRoleParams,
  body: API.UpdateRoleRequestDTO,
  options?: { [key: string]: any },
) {
  const { roleId: param0, ...queryParams } = params;
  return request<any>(`/api/roles/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 删除角色 DELETE /api/roles/${param0} */
export async function deleteRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteRoleParams,
  options?: { [key: string]: any },
) {
  const { roleId: param0, ...queryParams } = params;
  return request<any>(`/api/roles/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 启用或禁用角色 POST /api/roles/${param0}/${param1} */
export async function enableRole(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.enableRoleParams,
  options?: { [key: string]: any },
) {
  const { roleId: param0, enabled: param1, ...queryParams } = params;
  return request<any>(`/api/roles/${param0}/${param1}`, {
    method: 'POST',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 获取角色权限列表 GET /api/roles/${param0}/permission-items */
export async function getRolePermissionItems(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRolePermissionItemsParams,
  options?: { [key: string]: any },
) {
  const { roleId: param0, ...queryParams } = params;
  return request<API.PermissionItemDTO[]>(`/api/roles/${param0}/permission-items`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 为角色授予权限 PUT /api/roles/grant-permissions/${param0} */
export async function grantPermissionItems(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.grantPermissionItemsParams,
  body: number[],
  options?: { [key: string]: any },
) {
  const { roleId: param0, ...queryParams } = params;
  return request<any>(`/api/roles/grant-permissions/${param0}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 查询角色列表 GET /api/roles/list */
export async function listRoles(options?: { [key: string]: any }) {
  return request<API.RoleDTO[]>('/api/roles/list', {
    method: 'GET',
    ...(options || {}),
  });
}
