import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import { createTenant, enableTenant, listTenants, updateTenant } from '@/services/swagger/tenantApi';
import { convertCommonQueryParams, convertDateRange, enabledStatusEnum, stringToBoolean } from '@/utils/bizUtils';
import { useMessageBox } from '@/utils/messageUtils';
import { withChildrenType } from '@/utils/treeItemUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';

const handleAdd = useMessageBox<API.CreateTenantRequestDTO, number>(createTenant);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateTenantRequestDTO }, void>((args) =>
  updateTenant({ tenantId: args.id }, args.body),
);
const handleEnable = useMessageBox<{ id: number; enabled: boolean }, void>((args) =>
  enableTenant({ tenantId: args.id, enabled: args.enabled }),
);

const queryTenants = async (
  params: API.QueryTenantRequestDTO & {
    pageSize?: number | undefined;
    current?: number | undefined;
  },
  sorter: any,
): Promise<{ data: API.TenantDTO[]; success: boolean; total: number }> => {
  const response = await listTenants({
    keyword: params.keyword,
    enabled: stringToBoolean(params.enabled as any),
    createdTime: convertDateRange(params.createdTime as any),
    ...convertCommonQueryParams(params, sorter),
  } as any);

  return {
    data: response.data ?? [],
    success: true,
    total: response.total ?? 0,
  };
};

const TenantList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newTenant, setNewTenant] = useState<API.TenantDTO>({
    name: '',
    enabled: true,
  } as API.TenantDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editTenant, setEditTenant] = useState<API.TenantDTO | undefined>();

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const operationRender = useMemo(() => {
    return withAccessRender<withChildrenType<API.TenantDTO>>(
      {
        'admin:platform:tenant:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              setEditTenant({ ...record } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:platform:tenant:disable': (_, record) =>
          record.enabled ? (
            <Popconfirm
              title={<FormattedMessage id="admin.ui.public.label-enabled-false" />}
              description={<FormattedMessage id="admin.ui.pages.tenant.disable-confirm-desc" />}
              onConfirm={async () => {
                const result = await handleEnable({ id: record.id || 0, enabled: false });
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
              description={<FormattedMessage id="admin.ui.pages.tenant.enable-confirm-desc" />}
              onConfirm={async () => {
                const result = await handleEnable({ id: record.id || 0, enabled: true });
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

  const columns: ProColumns<withChildrenType<API.TenantDTO>>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.tenant.label-code" />,
      dataIndex: 'code',
      valueType: 'text',
      sorter: false,
      hideInSearch: true,
      width: '12em',
      ellipsis: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.tenant.label-name" />,
      dataIndex: 'name',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
      width: '12em',
      ellipsis: true,
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
      title: <FormattedMessage id="admin.ui.public.abel-enabled" />,
      dataIndex: 'enabled',
      valueType: 'text',
      sorter: false,
      hideInSearch: false,
      width: '8em',
      align: 'center',
      valueEnum: enabledStatusEnum(intl),
    },
    {
      title: <FormattedMessage id="admin.ui.pages.tenant.label-admin-username" />,
      dataIndex: 'adminUserName',
      valueType: 'text',
      sorter: false,
      hideInSearch: true,
      copyable: true,
      width: '10em',
    },

    {
      title: <FormattedMessage id="admin.ui.public.created-time" />,
      dataIndex: 'createdTime',
      sorter: true,
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
  ];

  if (operationRender.hasPermisions) {
    columns.push({
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: (operationRender.permissionCodes.length * 4 + 1) + 'em',
      fixed: 'right',
      align: 'center',
      render: (dom, record) => (
        <Flex justify="space-around">{operationRender.render(dom, record)}</Flex>
      ),
    });
  }

  return (
    <PageContainer pageHeaderRender={false}>
      <ProTablePage<API.TenantDTO, API.QueryTenantRequestDTO>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.tenant.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:tenant:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                setNewTenant({
                  enabled: true,
                } as any);
                handleCreateModalOpen(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
            </Button>
          </Access>,
        ]}
        request={queryTenants}
        columns={columns as any}
        rowSelection={false}
      />
      {
        <EditForm
          mode="create"
          tenant={newTenant}
          onCancel={() => handleCreateModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.tenant.create-new-title' })}
          onFinish={async (values) => {
            const result = await handleAdd(values as API.CreateTenantRequestDTO);
            if (result.success) {
              handleCreateModalOpen(false);
              actionRef.current?.reload();
            }
          }}
          width="600px"
          open={createModalOpen}
        ></EditForm>
      }
      {
        <EditForm
          mode="edit"
          tenant={editTenant}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.tenant.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              id: editTenant?.id ?? 0,
              body: values as API.UpdateTenantRequestDTO,
            });
            if (result.success) {
              handleEditModalOpen(false);
              actionRef.current?.reload();
            }
          }}
          width="600px"
          open={editModalOpen}
        ></EditForm>
      }
    </PageContainer>
  );
};

export default TenantList;
