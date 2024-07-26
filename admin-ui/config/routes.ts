/**
 * @name umi 的路由配置
 * @description 只支持 path,component,routes,redirect,wrappers,name,icon 的配置
 * @param path  path 只支持两种占位符配置，第一种是动态参数 :id 的形式，第二种是 * 通配符，通配符只能出现路由字符串的最后。
 * @param component 配置 location 和 path 匹配后用于渲染的 React 组件路径。可以是绝对路径，也可以是相对路径，如果是相对路径，会从 src/pages 开始找起。
 * @param routes 配置子路由，通常在需要为多个路径增加 layout 组件时使用。
 * @param redirect 配置路由跳转
 * @param wrappers 配置路由组件的包装组件，通过包装组件可以为当前的路由组件组合进更多的功能。 比如，可以用于路由级别的权限校验
 * @param name 配置路由的标题，默认读取国际化文件 menu.ts 中 menu.xxxx 的值，如配置 name 为 login，则读取 menu.ts 中 menu.login 的取值作为标题
 * @param icon 配置路由的图标，取值参考 https://ant.design/components/icon-cn， 注意去除风格后缀和大小写，如想要配置图标为 <StepBackwardOutlined /> 则取值应为 stepBackward 或 StepBackward，如想要配置图标为 <UserOutlined /> 则取值应为 user 或者 User
 * @doc https://umijs.org/docs/guides/routes
 */
export default [
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'smile',
    component: './Welcome',
  },
  {
    path: '/system',
    name: 'system',
    icon: 'crown',
    access: 'canRoute',
    permissionCode: 'admin:system',
    routes: [
      {
        name: 'organization',
        path: '/system/organization',
        component: './System/Organization',
        access: 'canRoute',
        permissionCode: 'admin:system:organization',
      },
      {
        name: 'user',
        path: '/system/user',
        component: './System/User',
        access: 'canRoute',
        permissionCode: 'admin:system:user',
      },
      {
        name: 'role',
        path: '/system/role',
        component: './System/Role',
        access: 'canRoute',
        permissionCode: 'admin:system:role',
      },
      {
        name: 'oplog',
        path: '/system/oplog',
        component: './System/Oplog',
        access: 'canRoute',
        permissionCode: 'admin:system:oplog',
      },
    ],
  },
  {
    path: '/platform',
    name: 'platform',
    icon: 'crown',
    access: 'canRoute',
    permissionCode: 'admin:platform',
    routes: [
      {
        name: 'tenant',
        path: '/platform/tenant',
        component: './Platform/Tenant',
        access: 'canRoute',
        permissionCode: 'admin:platform:tenant',
      },
      {
        name: 'permissionItem',
        path: '/platform/permissionitem',
        component: './Platform/PermissionItem',
        access: 'canRoute',
        permissionCode: 'admin:platform:permissionitem',
      },
      {
        name: 'dic',
        path: '/platform/dic',
        component: './Platform/Dic',
        access: 'canRoute',
        permissionCode: 'admin:platform:dic',
      },
      {
        path: '/platform/dic/:id',
        name: 'dic-item',
        hideInMenu: true,
        component: './Platform/DicItem',
        access: 'canRoute',
        permissionCode: 'admin:platform:dic:items',
      },
      {
        name: 'lang',
        path: '/platform/lang',
        component: './Platform/Lang',
        access: 'canRoute',
        permissionCode: 'admin:platform:lang',
      },
      {
        name: 'lang-corpus',
        path: '/platform/corpus',
        component: './Platform/LangCorpus',
        access: 'canRoute',
        permissionCode: 'admin:platform:langcorpus',
      },
      {
        path: '/platform/corpus/new',
        name: 'create-lang-corpus',
        hideInMenu: true,
        component: './Platform/LangCorpus/CreateLangCorpus',
        access: 'canRoute',
        permissionCode: 'admin:platform:langcorpus:create',
      },
    ],
  },
  {
    path: '/maintenance',
    name: 'maintenance',
    icon: 'crown',
    access: 'canRoute',
    permissionCode: 'admin:maintenance',
    routes: [
      {
        name: 'cache',
        path: '/maintenance/cache',
        component: './Maintenance/Cache',
        access: 'canRoute',
        permissionCode: 'admin:maintenance:cache',
      },
    ],
  },
  {
    path: '/401',
    layout: false,
    component: './401',
  },
  {
    path: '*',
    layout: false,
    component: './404',
  },
];
