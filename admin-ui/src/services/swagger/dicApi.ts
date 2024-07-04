// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建字典 POST /api/dics */
export async function createDic(body: API.CreateDicRequestDTO, options?: { [key: string]: any }) {
  return request<number>('/api/dics', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询字典 GET /api/dics/${param0} */
export async function getDic(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getDicParams,
  options?: { [key: string]: any },
) {
  const { dicId: param0, ...queryParams } = params;
  return request<API.DicDTO>(`/api/dics/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新字典 POST /api/dics/${param0} */
export async function updateDic(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateDicParams,
  body: API.UpdateDicRequestDTO,
  options?: { [key: string]: any },
) {
  const { dicId: param0, ...queryParams } = params;
  return request<any>(`/api/dics/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 删除字典 DELETE /api/dics/${param0} */
export async function deleteDic(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteDicParams,
  options?: { [key: string]: any },
) {
  const { dicId: param0, ...queryParams } = params;
  return request<any>(`/api/dics/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 创建字典项 POST /api/dics/${param0}/items */
export async function createDicItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.createDicItemParams,
  body: API.CreateDicItemRequestDTO,
  options?: { [key: string]: any },
) {
  const { dicId: param0, ...queryParams } = params;
  return request<number>(`/api/dics/${param0}/items`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 更新字典项 PUT /api/dics/${param0}/items/${param1} */
export async function updateDicItem(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateDicItemParams,
  body: API.UpdateDicItemRequestDTO,
  options?: { [key: string]: any },
) {
  const { dicId: param0, itemId: param1, ...queryParams } = params;
  return request<any>(`/api/dics/${param0}/items/${param1}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 查询字典列表 POST /api/dics/list */
export async function listDics(body: API.QueryDicRequestDTO, options?: { [key: string]: any }) {
  return request<API.PageDTODicDTO>('/api/dics/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
