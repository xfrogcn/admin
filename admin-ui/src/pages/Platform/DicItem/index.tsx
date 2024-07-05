import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import { createDic, deleteDic, listDics, updateDic, getDic } from '@/services/swagger/dicApi';
import { convertCommonQueryParams } from '@/utils/bizUtils';
import { useMessageBox } from '@/utils/messageUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl, useParams } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';
import { usePageTabContext } from '@/components/PageTabs';

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

const DicItemList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newDicItem, setNewDicItem] = useState<API.DicItemDTO>({} as API.DicItemDTO);

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editDicItem, setEditDicItem] = useState<API.DicItemDTO | undefined>();

  const [dic, setDic] = useState<API.DicDTO | undefined>();

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const tabContext = usePageTabContext();

  const params = useParams();
  const queryDic = useCallback(async () => {
    const id = params.id;
    const result = await getDic({dicId: id as any});

    setDic(result)
    tabContext.setTabLabel(tabContext.tab.defaultLabel + ' - ' + result.name)

    return {
      data: result.dicItems ?? [],
      success: true,
      total: result.dicItems?.length
    };

  }, [params])

  const operationRender = useMemo(() => {
    return withAccessRender<API.DicDTO>(
      {
        'admin:platform:dic:edititem': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              setEditDicItem({ ...record } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
      },
      access,
    );
  }, [access]);

  const columns: ProColumns<API.DicItemDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-display-text" />,
      dataIndex: 'displayText',
      valueType: 'text',
      sorter: false,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-lang-code" />,
      dataIndex: 'langCode',
      valueType: 'text',
      sorter: false,
      copyable: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-value" />,
      dataIndex: 'value',
      valueType: 'text',
      sorter: false,
      copyable: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-display-order" />,
      dataIndex: 'displayOrder',
      valueType: 'text',
      width: '4em',
      align: 'center',
      sorter: false,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-memo" />,
      dataIndex: 'memo',
      valueType: 'text',
      sorter: false,
    },
    {
      title: <FormattedMessage id="admin.ui.public.created-time" />,
      dataIndex: 'createdTime',
      sorter: false,
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
      <ProTablePage<API.DicItemDTO, API.getDicParams>
        headerTitle={`${intl.formatMessage({
          id: 'admin.ui.pages.dic.table-title',
        })} - [${dic?.name}(${dic?.type})]`}
        actionRef={actionRef}
        rowKey="id"
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:dic:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                // setNewDic({
                //   type: '',
                //   name: '',
                //   memo: '',
                // } as any);
                handleCreateModalOpen(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
            </Button>
          </Access>,
        ]}
        request={queryDic as any}
        columns={columns as any}
        rowSelection={false}
        pagination={false}
        search={false}
      />
      {/* {
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
      } */}
    </PageContainer>
  );
};

export default DicItemList;
