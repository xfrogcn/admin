import { accessOperate, withAccessRender } from '@/access';
import DataScopeDialog from '@/components/DataScopeDialog';
import ProTablePage from '@/components/ProTablePage';
import TreeSelector, { TreeItem } from '@/components/TreeSelector';
import { ExProColumnsType } from '@/components/ValueTypes';
import { getDataScopes, grantDataScope } from '@/services/swagger/dataScopeApi';
import { listOrganizations } from '@/services/swagger/organizationApi';
import {
  createUser,
  disableUser,
  enableUser,
  getUserDetail,
  grantRoles,
  listUsers,
  resetPassword,
  updateUser,
} from '@/services/swagger/userApi';
import { convertCommonQueryParams, convertDateRange, stringToBoolean } from '@/utils/bizUtils';
import { confirmAction, useMessageBox } from '@/utils/messageUtils';
import { generateTreeItems } from '@/utils/treeItemUtils';
import { DownOutlined, PlusOutlined } from '@ant-design/icons';
import type { ActionType } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Button, Col, Dropdown, Flex, Popconfirm, Row, Typography } from 'antd';
import { ItemType } from 'antd/es/menu/interface';
import React, { useCallback, useMemo, useRef, useState } from 'react';
import EditUserDialog from './components/EditForm';
import UserRolesDialog from './components/UserRolesForm';
import ViewUserDataScopeDialog from './components/ViewUserDataScopeDialog';
import './index.less';

const handleAdd = useMessageBox<API.CreateUserRequestDTO, number>(createUser);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateUserRequestDTO }, void>((args) =>
  updateUser({ userId: args.id }, args.body),
);
const handleEnable = useMessageBox<number, void>((id) => enableUser({ userId: id }));
const handleDisabled = useMessageBox<number, void>((id) => disableUser({ userId: id }));
const handleGrantRoles = useMessageBox<{ id: number; roleIds: number[] }, void>((args) =>
  grantRoles({ userId: args.id }, args.roleIds),
);
const handleResetPassword = useMessageBox<number, void>((id) => resetPassword({ userId: id }));
const handleGrantDataScope = useMessageBox<
  { userId: number; scopeItems: API.DataScopeItem[] },
  void
>((args) =>
  grantDataScope({ targetId: args.userId, targetType: 'USER', scopeItems: args.scopeItems }),
);

interface queryCondition {
  keyword?: string;
  enabled?: string;
  createdTime?: [string?, string?];
  organizationId: number;
  pageSize: number;
  current: number;
}

const queryUsers = async (
  params: queryCondition,
  sort: any,
  filter: any,
): Promise<{ data: API.UserDTO[]; success: boolean; total: number }> => {
  const response = await listUsers({
    keyword: params.keyword,
    enabled: stringToBoolean(params.enabled),
    organizationId: params.organizationId,
    createdTime: convertDateRange(params.createdTime),
    ...convertCommonQueryParams(params, sort),
  });

  return {
    data: response.data ?? [],
    success: true,
    total: response.total ?? 0,
  };
};

const UserList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newUser, setNewUser] = useState<API.CreateUserRequestDTO>({
    password: '123456',
    enabled: true,
  } as API.CreateUserRequestDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editUser, setEditUser] = useState<API.UserDTO | undefined>();

  const [treeData, setTreeData] = useState<TreeItem<API.OrganizationDTO>[]>([]);
  const [defaultOrganizationId, setDefaultOrganizationId] = useState<number[] | undefined>();
  const [currentOrganizationId, setCurrentOrganizationId] = useState<number | undefined>();
  const [currentUser, setCurrentUser] = useState<{ id: number; roleIds: number[] } | undefined>();
  const [userRolesModalOpen, handleUserRolesModalOpen] = useState<boolean>(false);

  const [dataScopeDialogOpen, setDataScopeDialogOpen] = useState<boolean>(false);
  const [currentDataScopeUser, setCurrentDataScopeUser] = useState<API.UserDTO>();
  const [userDataScopes, setUserDataScopes] = useState<API.DataScopeDTO[]>([]);
  const [viewUserDataScopeDialogOpen, setViewUserDataScopeDialogOpen] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const intl = useIntl();

  const listUsers = useCallback(
    (params: queryCondition, sort: any, filter: any) => {
      params.organizationId = currentOrganizationId ?? 1;
      console.log(params);
      return queryUsers(params, sort, filter);
    },
    [currentOrganizationId],
  );

  // 初始化组织树
  useMemo(async () => {
    const response = await listOrganizations({ arg0: {} });
    const organizations = generateTreeItems<API.OrganizationDTO>(
      response || [],
      (item) => item.id || 0,
      (item) => item.parentId,
      (item) => item.name || '',
      (a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0),
    );
    setTreeData(organizations);
    if (organizations.length > 0) {
      setDefaultOrganizationId([organizations[0].id]);
      setCurrentOrganizationId(organizations[0].id);
      actionRef.current?.reload(true);
    }
  }, []);

  const access = useAccess();
  const operationRender = useMemo(() => {
    return withAccessRender<API.UserDTO>(
      {
        'admin:system:user:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              const user = await getUserDetail({ userId: record.id || 0 });
              if (user) {
                setEditUser(user);
                handleEditModalOpen(true);
              }
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:system:user:disable': (_, record) =>
          record.enabled ? (
            <Popconfirm
              title={<FormattedMessage id="admin.ui.public.label-enabled-false" />}
              description={<FormattedMessage id="admin.ui.pages.user.disable-confirm-desc" />}
              onConfirm={async () => {
                const result = await handleDisabled(record.id ?? 0);
                if (result.success) {
                  actionRef.current?.reload();
                }
              }}
            >
              <a key="disabled">
                <FormattedMessage id="admin.ui.public.label-enabled-false" />
              </a>
            </Popconfirm>
          ) : (
            <Popconfirm
              title={<FormattedMessage id="admin.ui.public.label-enabled-true" />}
              description={<FormattedMessage id="admin.ui.pages.user.enable-confirm-desc" />}
              onConfirm={async () => {
                const result = await handleEnable(record.id ?? 0);
                if (result.success) {
                  actionRef.current?.reload();
                }
              }}
            >
              <a key="enabled">
                <FormattedMessage id="admin.ui.public.label-enabled-true" />
              </a>
            </Popconfirm>
          ),
      },
      access,
    );
  }, [access]);

  const moreAction = useMemo(() => {
    return accessOperate<ItemType>(
      {
        'admin:system:user:grantrole': {
          label: intl.formatMessage({ id: 'admin.ui.pages.user.label-grant-role' }),
          key: 'grantrole',
        },
        'admin:system:user:grantdatascope': {
          label: intl.formatMessage({ id: 'admin.ui.pages.user.label-grant-data-scope' }),
          key: 'grantdatascope',
        },
        'admin:system:user:datascope': {
          label: intl.formatMessage({ id: 'admin.ui.pages.user.label-view-data-scope' }),
          key: 'viewdatascope',
        },
        'admin:system:user:resetpassword': {
          label: intl.formatMessage({ id: 'admin.ui.pages.user.label-reset-password' }),
          key: 'resetpassword',
          danger: true,
        },
      },
      access,
    );
  }, [access]);

  const moreActionClick = async (e: ItemType, record: API.UserDTO) => {
    if (e?.key === 'resetpassword') {
      confirmAction(
        true,
        async () => {
          await handleResetPassword(record.id ?? 0);
        },
        'admin.ui.pages.user.reset-password-confirm-desc',
      );
    } else if (e?.key === 'grantdatascope') {
      setCurrentDataScopeUser(record);
      const dataScopes = await getDataScopes({ targetId: record.id || 0, targetType: 'USER' });
      setUserDataScopes(dataScopes || []);
      setDataScopeDialogOpen(true);
    } else if (e?.key === 'viewdatascope') {
      setCurrentDataScopeUser(record);
      setViewUserDataScopeDialogOpen(true);
    } else if (e?.key === 'grantrole') {
      setCurrentUser({
        id: record.id || 0,
        roleIds: record.roles ? record.roles.map((it) => it.id || 0) : [],
      });
      handleUserRolesModalOpen(true);
    }
  };

  const columns: ExProColumnsType<API.UserDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.user.label-userName" />,
      dataIndex: 'userName',
      width: '8em',
      align: 'center',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.public.keyword" />,
      dataIndex: 'keyword',
      sorter: false,
      valueType: 'text',
      hideInTable: true,
      hideInSearch: false,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.user.label-name" />,
      dataIndex: 'name',
      valueType: 'text',
      width: '8em',
      align: 'center',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.user.label-organizationName" />,
      dataIndex: 'organizationName',
      valueType: 'text',
      width: '8em',
      align: 'center',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.user.label-roles" />,
      dataIndex: 'roles',
      valueType: 'tags',
      align: 'center',
      sorter: false,
      hideInSearch: true,
      width: '10em',
      fieldProps: {
        key: 'id',
        title: 'name',
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.user.label-lastLoginTime" />,
      dataIndex: 'lastLoginTime',
      valueType: 'dateTime',
      width: '14em',
      align: 'center',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.public.created-time" />,
      dataIndex: 'createdTime',
      sorter: false,
      hideInForm: true,
      hideInSearch: true,
      width: '14em',
      align: 'center',
      valueType: 'dateTime',
    },
    {
      title: <FormattedMessage id="admin.ui.public.created-time" />,
      dataIndex: 'createdTime',
      sorter: false,
      hideInTable: true,
      hideInSearch: false,
      width: '14em',
      align: 'center',
      valueType: 'dateRange',
    },
    {
      title: <FormattedMessage id="admin.ui.public.abel-enabled" />,
      dataIndex: 'enabled',
      valueType: 'text',
      sorter: false,
      hideInSearch: false,
      width: '8em',
      align: 'center',
      valueEnum: {
        true: {
          text: <FormattedMessage id="admin.ui.pages.organization.label-status-normal" />,
          status: 'Success',
        },
        false: {
          text: <FormattedMessage id="admin.ui.pages.organization.label-status-disabled" />,
          status: 'Error',
        },
      },
    },
  ];

  if (operationRender.hasPermisions || moreAction.length > 0) {
    columns.push({
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: '11em',
      fixed: 'right',
      align: 'center',
      render: (dom, record) => {
        const buttons: JSX.Element[] = [];
        buttons.push(...operationRender.render(dom, record));
        if (moreAction.length > 0) {
          buttons.push(
            <Dropdown menu={{ items: moreAction, onClick: (e) => moreActionClick(e, record) }}>
              <a>
                <FormattedMessage id="admin.ui.public.label-more" /> <DownOutlined />
              </a>
            </Dropdown>,
          );
        }
        return <Flex justify="space-around">{buttons}</Flex>;
      },
    });
  }

  return (
    <PageContainer pageHeaderRender={false}>
      <Row gutter={16} style={{ flex: 1 }}>
        <Col xs={0} sm={6} md={6} lg={6} xl={6}>
          <Flex style={{ height: '100%' }} vertical>
            <Typography.Title level={5}>
              <FormattedMessage id="admin.ui.pages.user.organization-title" />
            </Typography.Title>

            <TreeSelector<API.OrganizationDTO>
              style={{ flex: 1, display: 'flex', flexDirection: 'column' }}
              className="user-organization-tree-body"
              treeData={treeData}
              selectedKeys={defaultOrganizationId}
              treeClassName="user-organization-tree"
              onSelect={(keys: number[]) => {
                if (keys && keys.length > 0) {
                  // 显示组织下用户
                  setCurrentOrganizationId(keys[0]);
                  actionRef.current?.reload(true);
                }
              }}
            />
          </Flex>
        </Col>
        <Col xs={24} sm={18} md={18} lg={18} xl={18}>
          <ProTablePage<API.UserDTO, API.PageParams>
            bordered
            headerTitle={intl.formatMessage({
              id: 'admin.ui.pages.user.table-title',
            })}
            actionRef={actionRef}
            rowKey="id"
            search={{
              labelWidth: 100,
            }}
            toolBarRender={() => [
              <Access accessible={access.hasPermission('admin:system:user:create')}>
                <Button
                  type="primary"
                  key="primary"
                  onClick={() => {
                    setNewUser({ ...newUser, organizationId: currentOrganizationId });
                    handleCreateModalOpen(true);
                  }}
                >
                  <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
                </Button>
              </Access>,
            ]}
            request={listUsers as any}
            columns={columns as any}
            rowSelection={false}
          />
          {
            <EditUserDialog
              mode="create"
              user={newUser}
              rootOrganizationId={currentOrganizationId || 1}
              onCancel={() => handleCreateModalOpen(false)}
              title={intl.formatMessage({ id: 'admin.ui.pages.user.create-new-title' })}
              onFinish={async (values) => {
                const result = await handleAdd(values as API.CreateUserRequestDTO);
                if (result.success) {
                  handleCreateModalOpen(false);
                  actionRef.current?.reload();
                }
              }}
              width="600px"
              open={createModalOpen}
            ></EditUserDialog>
          }
          {
            <EditUserDialog
              mode="edit"
              user={editUser}
              rootOrganizationId={currentOrganizationId || 1}
              onCancel={() => handleEditModalOpen(false)}
              title={intl.formatMessage({ id: 'admin.ui.pages.user.edit-title' })}
              onFinish={async (values) => {
                const result = await handleUpdate({
                  id: editUser?.id ?? 0,
                  body: values as API.UpdateUserRequestDTO,
                });
                if (result.success) {
                  handleEditModalOpen(false);
                  actionRef.current?.reload();
                }
              }}
              width="600px"
              open={editModalOpen}
            ></EditUserDialog>
          }
          {
            <UserRolesDialog
              userRoles={currentUser as any}
              title={intl.formatMessage({ id: 'admin.ui.pages.user.label-roles' })}
              open={userRolesModalOpen}
              onFinish={async (values) => {
                const result = await handleGrantRoles(values as any);
                if (result.success) {
                  actionRef.current?.reload();
                  handleUserRolesModalOpen(false);
                }
              }}
              onCancel={() => handleUserRolesModalOpen(false)}
            />
          }
          {
            <DataScopeDialog
              open={dataScopeDialogOpen}
              dataScope={userDataScopes}
              onCancel={() => setDataScopeDialogOpen(false)}
              targetType="USER"
              target={currentDataScopeUser as any}
              onSave={async (dataScope) => {
                const scopeItems: API.DataScopeItem[] = dataScope.map(
                  (it) => ({ scopeType: it.scopeType, scopeId: it.scopeId } as API.DataScopeItem),
                );
                const result = await handleGrantDataScope({
                  userId: currentDataScopeUser?.id ?? 0,
                  scopeItems: scopeItems,
                });
                if (result.success) {
                  setDataScopeDialogOpen(false);
                }
              }}
            />
          }
          {
            <ViewUserDataScopeDialog
              open={viewUserDataScopeDialogOpen}
              onCancel={() => setViewUserDataScopeDialogOpen(false)}
              target={currentDataScopeUser as any}
              onOk={() => setViewUserDataScopeDialogOpen(false)}
            />
          }
        </Col>
      </Row>
    </PageContainer>
  );
};

export default UserList;
