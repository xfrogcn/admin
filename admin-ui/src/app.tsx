import { AvatarDropdown, AvatarName, Question, SelectLang } from '@/components';
import { LinkOutlined } from '@ant-design/icons';
import type { Settings as LayoutSettings } from '@ant-design/pro-components';
import { ProConfigProvider, SettingDrawer } from '@ant-design/pro-components';
import type { RequestOptions, RunTimeLayoutConfig } from '@umijs/max';
import { Link, addLocale, getLocale, history, setLocale, useIntl } from '@umijs/max';
import { ConfigProvider } from 'antd';
import defaultSettings from '../config/defaultSettings';
import { PageTabs } from './components/PageTabs';
import SettingDropdown from './components/RightContent/SettingDropdown';
import { valueTypeMap } from './components/ValueTypes';
import { applicationCode, urls } from './config';
import { signinRedirectCallbackPath, signoutRedirectCallbackPath, userManager } from './oauth2';
import { errorConfig } from './requestErrorConfig';
import services from './services/swagger';
import { getLangLocal } from './services/swagger/langCorpusApi';
import { getCurrentUserPermissionCodes } from './services/swagger/userApi';
import { getUserSettings } from './services/swagger/userParameterApi';
import { setIntl } from './utils/messageUtils';
const isDev = process.env.NODE_ENV === 'development';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUserInfoDTO;
  permissions?: Record<string, true>;
  loading?: boolean;
  langs?: API.LangDTO[];
  userParameters?: Record<string, string>;
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

  if (history.location.pathname === '/401') {
    return {
      fetchUserInfo,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }

  if (history.location.pathname === signoutRedirectCallbackPath) {
    await userManager.signoutRedirectCallback();
    history.replace('/');
  }

  const currentUser = await fetchUserInfo();
  if (currentUser) {
    // 获取当前用户权限
    const permissions = await getCurrentUserPermissionCodes();
    const permissionMap: Record<string, true> = {};
    permissions.forEach((item) => {
      permissionMap[item] = true;
    });

    const userSettings = await getUserSettings({ application: applicationCode() });
    const userLang = userSettings.parameters?.language || getLocale() || 'zh-CN';
    const userLayoutSettings: Partial<LayoutSettings> = {};
    if (userSettings.parameters?.navTheme) {
      userLayoutSettings.navTheme = userSettings.parameters?.navTheme;
    }
    if (userSettings.parameters?.colorPrimary) {
      userLayoutSettings.colorPrimary = userSettings.parameters?.colorPrimary;
    }

    const langLocale = await getLangLocal({ application: applicationCode(), langCode: userLang });
    console.log(userLang, langLocale, getLocale());
    addLocale(userLang, langLocale, {
      momentLocale: userLang,
      antd: { locale: userLang },
    });
    if (userLang !== getLocale()) {
      setLocale(userLang, false);
    }

    return {
      fetchUserInfo,
      currentUser,
      permissions: permissionMap,
      settings: {...defaultSettings, ...userLayoutSettings} as Partial<LayoutSettings>,
      langs: userSettings.langs,
      userParameters: userSettings.parameters,
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
  history.push('/401');
  await userManager.signinRedirect();

  return {
    fetchUserInfo,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    actionsRender: () => [
      <Question key="doc" />,
      <SelectLang langs={initialState?.langs} key="SelectLang" />,
      <SettingDropdown
        settings={initialState?.settings}
        onSettingChange={(settings) => {
          setInitialState((preInitialState) => ({
            ...preInitialState,
            settings,
          }));
        }}
      />,
    ],
    avatarProps: {
      //src: initialState?.currentUser?.avatar,
      title: <AvatarName />,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    // waterMarkProps: {
    //   content: initialState?.currentUser?.name,
    // },
    footerRender: () => null,
    onPageChange: () => {},
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
    // 自定义 401 页面
    unAccessible: <div>unAccessible</div>,
    layout: 'top',
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      const intl = useIntl();
      setIntl(intl);

      return (
        <>
          <ConfigProvider theme={{}}>
            <ProConfigProvider valueTypeMap={valueTypeMap}>
              <PageTabs />
            </ProConfigProvider>
          </ConfigProvider>
        
            {/* <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            /> */}
          
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
