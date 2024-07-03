import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import {
  createPermissionItem,
  deletePermissionItem,
  listPermissionItemsFormPlatform,
  updatePermissionItem,
} from '@/services/swagger/permissionItemApi';
import { useMessageBox } from '@/utils/messageUtils';
import { generateTree, withChildrenType } from '@/utils/treeItemUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';

const handleAdd = useMessageBox<API.CreatePermissionItemRequestDTO, number>(createPermissionItem);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdatePermissionItemRequestDTO }, void>(
  (args) => updatePermissionItem({ permissionItemId: args.id }, args.body),
);
const handleDelete = useMessageBox<number, void>((id) =>
  deletePermissionItem({ permissionItemId: id }),
);

const PermissionItemList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newPermissionItem, setNewPermissionItem] = useState<API.PermissionItemDTO>({
    parentId: undefined,
    type: 'F',
    code: '',
    platform: false
  } as API.PermissionItemDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editPermissionItem, setEditPermissionItem] = useState<API.PermissionItemDTO | undefined>();
  const [permissionItemMap, setPermissionItemMap] = useState<Record<number, API.PermissionItemDTO>>(
    {},
  );

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const operationRender = useMemo(() => {
    return withAccessRender<withChildrenType<API.PermissionItemDTO>>(
      {
        'admin:platform:permissionitem:create': (_, record) => (
          <a
            key="add"
            // @ts-ignore
            disabled={record.type === 'B'}
            onClick={() => {
              if (record.type === 'B') {
                return;
              }
              const parent = permissionItemMap[record.id || 0];
              setNewPermissionItem({
                ...newPermissionItem,
                parentId: record.id,
                code: `${parent?.code ?? ''}:-`,
                parentName: parent?.name,
                platform: parent?.platform,
              } as any);
              handleCreateModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.new-button" />
          </a>
        ),
        'admin:platform:permissionitem:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              const parent = record.parentId ? permissionItemMap[record.parentId] : undefined;
              setEditPermissionItem({ ...record, parentName: parent?.name } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:platform:permissionitem:delete': (_, record) => (
          <Popconfirm
            disabled={record.children?.length > 0}
            title={<FormattedMessage id="admin.ui.public.delete-button" />}
            description={
              <FormattedMessage id="admin.ui.pages.permissionitem.delete-confirm-desc" />
            }
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
              disabled={record.children?.length > 0}
            >
              <FormattedMessage id="admin.ui.public.delete-button" />
            </a>
          </Popconfirm>
        ),
      },
      access,
    );
  }, [access, permissionItemMap]);

  const columns: ProColumns<withChildrenType<API.PermissionItemDTO>>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.permissionitem.label-name" />,
      dataIndex: 'name',
      sorter: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.permissionitem.label-type" />,
      dataIndex: 'type',
      valueType: 'text',
      width: '8em',
      align: 'center',
      sorter: false,
      hideInSearch: true,
      valueEnum: {
        F: {
          text: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type-folder' }),
        },
        M: {
          text: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type-menu' }),
        },
        B: {
          text: intl.formatMessage({ id: 'admin.ui.pages.permissionitem.label-type-button' }),
        },
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.permissionitem.label-code" />,
      dataIndex: 'code',
      valueType: 'text',
      sorter: false,
      hideInSearch: true,
      copyable: true,
    },
  ];

  if (operationRender.hasPermisions) {
    columns.push({
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: '10em',
      fixed: 'right',
      align: 'center',
      render: (dom, record) => (
        <Flex justify="space-around">{operationRender.render(dom, record)}</Flex>
      ),
    });
  }

  const queryPermissionItems = async (): Promise<{
    data: withChildrenType<API.PermissionItemDTO>[];
    success: boolean;
    total: number;
  }> => {
    const response = await listPermissionItemsFormPlatform();
    const map: Record<number, API.PermissionItemDTO> = {};
    response.forEach((item) => {
      map[item.id || 0] = item;
    });
    setPermissionItemMap(map);

    return {
      data: generateTree(
        response,
        (item) => item.id || 0,
        (item) => item.parentId,
      ),
      success: true,
      total: response.length,
    };
  };

  return (
    <PageContainer>
      <ProTablePage<API.PermissionItemDTO, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.permissionitem.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
        search={false}
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:permissionitem:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                setNewPermissionItem({
                  parentId: undefined,
                  type: 'F',
                  code: '',
                  parentName: '',
                  platform: false
                } as any);
                handleCreateModalOpen(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
            </Button>
          </Access>,
        ]}
        request={queryPermissionItems}
        columns={columns as any}
        rowSelection={false}
        pagination={false}
      />
      {
        <EditForm
          mode="create"
          permissionItem={newPermissionItem}
          onCancel={() => handleCreateModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.permissionitem.create-new-title' })}
          onFinish={async (values) => {
            const result = await handleAdd(values as API.CreatePermissionItemRequestDTO);
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
          permissionItem={editPermissionItem}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.permissionitem.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              id: editPermissionItem?.id ?? 0,
              body: values as API.UpdatePermissionItemRequestDTO,
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

export default PermissionItemList;
