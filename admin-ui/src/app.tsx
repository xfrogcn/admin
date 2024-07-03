import { AvatarDropdown, AvatarName, Footer, Question, SelectLang } from '@/components';
import { LinkOutlined } from '@ant-design/icons';
import type { Settings as LayoutSettings } from '@ant-design/pro-components';
import { ProConfigProvider, SettingDrawer } from '@ant-design/pro-components';
import type { RequestOptions, RunTimeLayoutConfig } from '@umijs/max';
import { Link, history, useAppData, useIntl, useOutlet } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import { urls } from './config';
import { signinRedirectCallbackPath, signoutRedirectCallbackPath, userManager } from './oauth2';
import { errorConfig } from './requestErrorConfig';
import services from './services/swagger';
import { setIntl } from './utils/messageUtils';
import { valueTypeMap } from './components/ValueTypes';
import { getCurrentUserPermissionCodes } from './services/swagger/userApi';
import React from 'react';
import { UNSAFE_RouteContext, useRoutes } from 'react-router';
import { PageTabs } from './components/PageTabs';
const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';

const shouldAuth = true; // process.env.NODE_ENV === 'production';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUserInfoDTO;
  permissions?: Record<string, true>;
  loading?: boolean;
  fetchUserInfo?: () => Promise<any | undefined>;
}> {
  // 动态载入多语言
  // 语言代码必须是真实的，umi会自动根据语言代码设置数字、货币、布局等默认格式
  // 如果语言已存在，会执行语料合并操作
  // addLocale(
  //   'ar-LB',
  //   {},
  //   {
  //     momentLocale: 'zh-cn',
  //     antd: zhCN,
  //   },
  // );

  const fetchUserInfo = async () => {
    try {
      const currentPath = history.location.pathname;
      let user;
      if (currentPath === signinRedirectCallbackPath) {
        user = await userManager.signinRedirectCallback();
        // 此处跳转到登录前页面
        const path = localStorage.getItem('__path');
        console.log(path);
        if (path) {
          localStorage.removeItem('__path');
          const location = JSON.parse(path);
          history.push(location.pathname + location.hash + location.search);
        } else {
          history.push('/');
        }
      } else {
        user = await userManager.getUser();
      }

      console.log(user);

      if (user) {
        // 获取当前用户信息
        const userInfo: API.UserDTO = await services.userApi.getCurrentUserDetail();

        return userInfo;
      }
    } catch (error) {
      console.log(error);
      userManager.signinRedirect();
    }
    return undefined;
  };

  if (history.location.pathname === '/403') {
    return {
      fetchUserInfo,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }

  if (history.location.pathname === signoutRedirectCallbackPath) {
    await userManager.signoutRedirectCallback();
    history.replace("/")
  }

  const currentUser = await fetchUserInfo();
  if (currentUser) {
    // 获取当前用户权限
    const permissions = await getCurrentUserPermissionCodes();
    const permissionMap: Record<string, true> = {};
    permissions.forEach(item => {
      permissionMap[item] = true;
    })
    return {
      fetchUserInfo,
      currentUser,
      permissions: permissionMap,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }

  if (
    history.location.pathname === signinRedirectCallbackPath ||
    history.location.pathname === signoutRedirectCallbackPath
  ) {
    return {
      fetchUserInfo,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }

  localStorage.setItem('__path', JSON.stringify(history.location));
  history.push('/403');
  await userManager.signinRedirect();

  return {
    fetchUserInfo,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

const contents = []

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    actionsRender: () => [<Question key="doc" />, <SelectLang key="SelectLang" />],
    avatarProps: {
      // src: initialState?.currentUser?.avatar,
      title: <AvatarName />,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    // waterMarkProps: {
    //   content: initialState?.currentUser?.name,
    // },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser && location.pathname !== loginPath) {
        //  history.push(loginPath);
      }
    },
    bgLayoutImgList: [],
    links: isDev
      ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <LinkOutlined />
            <span>OpenAPI 文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    unAccessible: <div>unAccessible</div>,
    layout: 'top',
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      const intl = useIntl();
      setIntl(intl);

      return (
        <>
        <ProConfigProvider valueTypeMap={valueTypeMap}>
          <PageTabs />
          </ProConfigProvider>
          {isDev && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = {
  ...errorConfig,

  // 请求拦截器
  requestInterceptors: [
    async (config: RequestOptions) => {
      // 拦截请求配置，进行个性化处理。
      const userInfo = await userManager.getUser();
      console.log('in:', userInfo);
      if (userInfo && userInfo.id_token) {
        const header: any = config.headers?.common;
        if (header) {
          header['Authorization'] = `Bearer ${userInfo.id_token}`;
        }
      }
      const url = urls.api() + config?.url;
      return { ...config, url };
    },
  ],
  // 响应拦截器
  responseInterceptors: [
    (response: any) => {
      if (isDev) {
        console.log(response);
      }
      return response;
    },
  ],
};
