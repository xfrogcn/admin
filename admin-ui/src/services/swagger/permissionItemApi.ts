// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建权限项 POST /api/permission-items */
export async function createPermissionItem(
  body: API.CreatePermissionItemRequestDTO,
  options?: { [key: string]: any },
) {
  return request<number>('/api/permission-items', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新权限项 POST /api/permission-items/${param0} */
export async function updatePermissionItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updatePermissionItemParams,
  body: API.UpdatePermissionItemRequestDTO,
  options?: { [key: string]: any },
) {
  const { permissionItemId: param0, ...queryParams } = params;
  return request<any>(`/api/permission-items/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 删除权限项 DELETE /api/permission-items/${param0} */
export async function deletePermissionItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deletePermissionItemParams,
  options?: { [key: string]: any },
) {
  const { permissionItemId: param0, ...queryParams } = params;
  return request<any>(`/api/permission-items/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 获取权限项列表 GET /api/permission-items/list */
export async function listPermissionItems(options?: { [key: string]: any }) {
  return request<API.PermissionItemDTO[]>('/api/permission-items/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取权限项列表（包含平台权限项） GET /api/permission-items/list/platform */
export async function listPermissionItemsFormPlatform(options?: { [key: string]: any }) {
  return request<API.PermissionItemDTO[]>('/api/permission-items/list/platform', {
    method: 'GET',
    ...(options || {}),
  });
}
