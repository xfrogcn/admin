import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import { createDic, deleteDic, listDics, updateDic } from '@/services/swagger/dicApi';
import { convertCommonQueryParams } from '@/utils/bizUtils';
import { useMessageBox } from '@/utils/messageUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl, useNavigate } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';

const handleAdd = useMessageBox<API.CreateDicRequestDTO, number>(createDic);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateDicRequestDTO }, void>((args) =>
  updateDic({ dicId: args.id }, args.body),
);
const handleDelete = useMessageBox<{ id: number }, void>((args) => deleteDic({ dicId: args.id }));

const queryDics = async (
  params: API.QueryDicRequestDTO & {
    pageSize?: number | undefined;
    current?: number | undefined;
  },
  sorter: any,
): Promise<{ data: API.DicDTO[]; success: boolean; total: number }> => {
  const response = await listDics({
    keyword: params.keyword,
    ...convertCommonQueryParams(params, sorter),
  } as API.QueryDicRequestDTO);

  return {
    data: response.data ?? [],
    success: true,
    total: response.total ?? 0,
  };
};

const DicList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newDic, setNewDic] = useState<API.DicDTO>({
    name: '',
    type: '',
    memo: '',
  } as API.DicDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editDic, setEditDic] = useState<API.DicDTO | undefined>();

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();
  const navigation = useNavigate();

  const operationRender = useMemo(() => {
    return withAccessRender<API.DicDTO>(
      {
        'admin:platform:dic:items': (_, record) => (
          <a key="items" onClick={() => {
            navigation(`/platform/dic/${record.id}`, { replace: false});
          }}>
            <FormattedMessage id="admin.ui.pages.dic.button-items" />
          </a>
        ),
        'admin:platform:dic:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              setEditDic({ ...record } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:platform:dic:delete': (_, record) => (
          <Popconfirm
            title={<FormattedMessage id="admin.ui.public.confirm-ok-button" />}
            description={<FormattedMessage id="admin.ui.pages.dic.delete-confirm-desc" />}
            onConfirm={async () => {
              const result = await handleDelete({ id: record.id || 0 });
              if (result.success) {
                actionRef.current?.reload();
              }
            }}
          >
            <a key="delete">
              <FormattedMessage id="admin.ui.public.delete-button" />
            </a>
          </Popconfirm>
        ),
      },
      access,
    );
  }, [access]);

  const columns: ProColumns<API.DicDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.dic.label-name" />,
      dataIndex: 'name',
      valueType: 'text',
      sorter: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dic.label-type" />,
      dataIndex: 'type',
      valueType: 'text',
      ellipsis: true,
      sorter: true,
      copyable: true,
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
      title: <FormattedMessage id="admin.ui.pages.dic.label-memo" />,
      dataIndex: 'memo',
      valueType: 'text',
      ellipsis: {showTitle: true},
      sorter: false,
      hideInSearch: true,
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
      <ProTablePage<API.DicDTO, API.QueryDicRequestDTO>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.dic.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:dic:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                setNewDic({
                  type: '',
                  name: '',
                  memo: '',
                } as any);
                handleCreateModalOpen(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
            </Button>
          </Access>,
        ]}
        request={queryDics}
        columns={columns as any}
        rowSelection={false}
      />
      {
        <EditForm
          mode="create"
          dic={newDic}
          onCancel={() => handleCreateModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.dic.create-new-title' })}
          onFinish={async (values) => {
            const result = await handleAdd(values as API.CreateDicRequestDTO);
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
          dic={editDic}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.dic.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              id: editDic?.id ?? 0,
              body: values as API.UpdateDicRequestDTO,
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

export default DicList;
