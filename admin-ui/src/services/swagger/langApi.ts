// @ts-ignore
/* eslint-disable */
import request from '@/request';

/** 创建语言 POST /api/langs */
export async function createLanguage(
  body: API.CreateLangRequestDTO,
  options?: { [key: string]: any },
) {
  return request<number>('/api/langs', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 查询语言 GET /api/langs/${param0} */
export async function getLanguage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLanguageParams,
  options?: { [key: string]: any },
) {
  const { langId: param0, ...queryParams } = params;
  return request<API.LangDTO>(`/api/langs/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 更新语言 POST /api/langs/${param0} */
export async function updateLanguage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.updateLanguageParams,
  body: API.UpdateLangRequestDTO,
  options?: { [key: string]: any },
) {
  const { langId: param0, ...queryParams } = params;
  return request<any>(`/api/langs/${param0}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  });
}

/** 删除语言 DELETE /api/langs/${param0} */
export async function deleteLanguage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteLanguageParams,
  options?: { [key: string]: any },
) {
  const { langId: param0, ...queryParams } = params;
  return request<any>(`/api/langs/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  });
}

/** 启用或禁用语种 POST /api/langs/${param0}/${param1} */
export async function enableLanguage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.enableLanguageParams,
  options?: { [key: string]: any },
) {
  const { langId: param0, enabled: param1, ...queryParams } = params;
  return request<any>(`/api/langs/${param0}/${param1}`, {
    method: 'POST',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  });
}

/** 查询语种列表 POST /api/langs/list */
export async function listLanguages(
  body: API.QueryLangRequestDTO,
  options?: { [key: string]: any },
) {
  return request<API.PageDTOLangDTO>('/api/langs/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
