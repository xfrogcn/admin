import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import {
  createOrganization,
  deleteOrganization,
  getOrganization,
  listOrganizations,
  updateOrganization,
} from '@/services/swagger/organizationApi';
import { useMessageBox } from '@/utils/messageUtils';
import { generateTree, withChildrenType } from '@/utils/treeItemUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl, useModel } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';

const handleAdd = useMessageBox<API.CreateOrganizationRequestDTO, number>(createOrganization);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateOrganizationRequestDTO }, void>(
  (args) => updateOrganization({ organizationId: args.id }, args.body),
);
const handleDelete = useMessageBox<number, void>((id) =>
  deleteOrganization({ organizationId: id }),
);

interface queryCondition {
  name?: string;
  status?: API.OrganizationDTO['status'];
  pageSize: number;
  current: number;
}

const queryOrganizations = async (
  params: queryCondition,
  sort: any,
  filter: any,
): Promise<{ data: withChildrenType<API.OrganizationDTO>[]; success: boolean; total: number }> => {
  const response = await listOrganizations({
    arg0: {
      keyword: params.name,
      status: params.status,
    },
  });

  return {
    data: generateTree(
      response,
      (item) => item.id || 0,
      (item) => item.parentId,
      (a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0),
    ),
    success: true,
    total: response.length,
  };
};

const OrganizationList: React.FC = () => {
  const { initialState } = useModel('@@initialState');

  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newOrganization, setNewOrganization] = useState<API.OrganizationDTO>({
    parentId: initialState?.currentUser?.rootOrganizationId ?? 0,
    displayOrder: 1,
    status: 'NORMAL',
  } as API.OrganizationDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editOrganization, setEditOrganization] = useState<API.OrganizationDTO | undefined>();

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const operateRender = useMemo(() => {
    return withAccessRender<withChildrenType<API.OrganizationDTO>>(
      {
        'admin:system:organization:create': (_, record) => (
          <a
            key="add"
            onClick={() => {
              setNewOrganization({ ...newOrganization, parentId: record.id });
              handleCreateModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.new-button" />
          </a>
        ),
        'admin:system:organization:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              const organization = await getOrganization({ organizationId: record.id || 0 });
              if (organization) {
                setEditOrganization(organization);
                handleEditModalOpen(true);
              }
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:system:organization:delete': (_, record) => (
          <Popconfirm
            disabled={record.children?.length > 0 || !!!record.parentId}
            title={<FormattedMessage id="admin.ui.public.delete-confirm-title" />}
            description={<FormattedMessage id="admin.ui.pages.organization.delete-confirm-desc" />}
            onConfirm={async () => {
              const result = await handleDelete(record.id ?? 0);
              if (result.success) {
                actionRef.current?.reload();
              }
            }}
          >
            <a
              key="delete"
              // @ts-ignore
              disabled={record.children?.length > 0 || !!!record.parentId}
            >
              <FormattedMessage id="admin.ui.public.delete-button" />
            </a>
          </Popconfirm>
        ),
      },
      access,
    );
  }, [access]);

  const columns: ProColumns<withChildrenType<API.OrganizationDTO>>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.organization.label-name" />,
      dataIndex: 'name',
      sorter: false,
      hideInSearch: false,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.organization.label-level" />,
      dataIndex: 'level',
      valueType: 'text',
      width: '8em',
      align: 'center',
      sorter: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.organization.label-status" />,
      dataIndex: 'status',
      valueType: 'text',
      sorter: false,
      hideInSearch: false,
      width: '8em',
      align: 'center',
      valueEnum: {
        NORMAL: {
          text: <FormattedMessage id="admin.ui.pages.organization.label-status-normal" />,
          status: 'Success',
        },
        DISABLED: {
          text: <FormattedMessage id="admin.ui.pages.organization.label-status-disabled" />,
          status: 'Error',
        },
      },
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
  ];
  if (operateRender.hasPermisions) {
    columns.push({
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: '10em',
      fixed: 'right',
      align: 'center',
      render: (dom, record) => (
        <Flex justify="space-around">{operateRender.render(dom, record)}</Flex>
      ),
    });
  }

  return (
    <PageContainer>
      <ProTablePage<withChildrenType<API.OrganizationDTO>, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.organization.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:system:organization:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                handleCreateModalOpen(true);
              }}
            >
              <PlusOutlined />{' '}
              <FormattedMessage
                id="admin.ui.pages.organization.create-new-button"
                defaultMessage="New"
              />
            </Button>
          </Access>,
        ]}
        request={queryOrganizations as any}
        columns={columns}
        rowSelection={false}
        pagination={false}
      />
      {
        <EditForm
          mode="create"
          organization={newOrganization}
          onCancel={() => handleCreateModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.organization.create-new-title' })}
          onFinish={async (values) => {
            const result = await handleAdd(values as API.CreateOrganizationRequestDTO);
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
          organization={editOrganization}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.organization.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              id: editOrganization?.id ?? 0,
              body: values as API.UpdateOrganizationRequestDTO,
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

export default OrganizationList;
