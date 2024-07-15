import { withAccessRender } from '@/access';
import LinkButton from '@/components/LinkButton';
import { usePageTabContext } from '@/components/PageTabs';
import ProTablePage from '@/components/ProTablePage';
import { removeDicCache } from '@/services/DicCache';
import { createDicItem, getDic, updateDicItem } from '@/services/swagger/dicApi';
import { useMessageBox } from '@/utils/messageUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl, useParams } from '@umijs/max';
import { Button, Flex } from 'antd';
import React, { useCallback, useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';

const handleAdd = useMessageBox<{ dicId: number; body: API.CreateDicItemRequestDTO }, number>(
  (args) => createDicItem({ dicId: args.dicId }, args.body),
);
const handleUpdate = useMessageBox<
  { dicId: number; itemId: number; body: API.UpdateDicItemRequestDTO },
  void
>((args) => updateDicItem({ dicId: args.dicId, itemId: args.itemId }, args.body));

const defaultDicItem: API.DicItemDTO = {
  enabled: true,
  displayOrder: 100,
};

const DicItemList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newDicItem, setNewDicItem] = useState<API.DicItemDTO>({ ...defaultDicItem });

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
    const result = await getDic({ dicId: id as any });

    setDic(result);
    tabContext.setTabLabel(tabContext.tab.defaultLabel + ' - ' + result.name);

    return {
      data: result.dicItems ?? [],
      success: true,
      total: result.dicItems?.length,
    };
  }, [params]);

  const operationRender = useMemo(() => {
    return withAccessRender<API.DicDTO>(
      {
        'admin:platform:dic:edititem': (_, record) => (
          <LinkButton
            type="primary"
            key="edit"
            onClick={async () => {
              setEditDicItem({ ...record } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </LinkButton>
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
      width: '10em',
      ellipsis: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-lang-code" />,
      dataIndex: 'langCode',
      valueType: 'text',
      sorter: false,
      copyable: true,
      ellipsis: true,
      width: '15rm',
    },
    {
      title: <FormattedMessage id="admin.ui.pages.dicitem.label-value" />,
      dataIndex: 'value',
      valueType: 'text',
      sorter: false,
      copyable: true,
      width: '15em',
      ellipsis: true,
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
      width: operationRender.columnWidth,
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
        width="80em"
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:dic:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                setNewDicItem({ ...defaultDicItem, dicId: dic?.id });
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
      {
        <EditForm
          mode="create"
          dicItem={newDicItem}
          dic={dic || {}}
          onCancel={() => handleCreateModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.dicitem.create-new-title' })}
          onFinish={async (values) => {
            const result = await handleAdd({
              dicId: dic?.id || 0,
              body: values as API.CreateDicItemRequestDTO,
            });
            if (result.success) {
              handleCreateModalOpen(false);
              removeDicCache(dic?.type ?? '');
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
          dicItem={editDicItem}
          dic={dic || {}}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.dicitem.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              dicId: editDicItem?.dicId ?? 0,
              itemId: editDicItem?.id ?? 0,
              body: values as API.UpdateDicItemRequestDTO,
            });
            if (result.success) {
              handleEditModalOpen(false);
              removeDicCache(dic?.type ?? '');
              actionRef.current?.reload();
            }
          }}
          width="600px"
          height="600px"
          open={editModalOpen}
        ></EditForm>
      }
    </PageContainer>
  );
};

export default DicItemList;
