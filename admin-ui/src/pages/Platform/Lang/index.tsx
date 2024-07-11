import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import {
  createLanguage,
  deleteLanguage,
  enableLanguage,
  listLanguages,
  updateLanguage,
} from '@/services/swagger/langApi';
import { convertCommonQueryParams, enabledStatusEnum } from '@/utils/bizUtils';
import { stopEvent } from '@/utils/commonUtils';
import { useMessageBox } from '@/utils/messageUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm'
import { ExProColumnsType } from '@/components/ValueTypes';

const handleAdd = useMessageBox<API.CreateLangRequestDTO, number>(createLanguage);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateLangRequestDTO }, void>((args) =>
  updateLanguage({ langId: args.id }, args.body),
);
const handleEnable = useMessageBox<
  { id: number; enabled: boolean; referenceLangId?: number },
  void
>((args) =>
  enableLanguage({
    langId: args.id,
    enabled: args.enabled,
    referenceLangId: args.referenceLangId || 0,
  }),
);
const handleDelete = useMessageBox<{ id: number }, void>((args) =>
  deleteLanguage({ langId: args.id }),
);

const queryLangs = async (
  params: API.QueryLangRequestDTO & {
    pageSize?: number | undefined;
    current?: number | undefined;
  },
  sorter: any,
): Promise<{ data: API.LangDTO[]; success: boolean; total: number }> => {
  const response = await listLanguages({
    application: params.application,
    ...convertCommonQueryParams(params, sorter),
  } as API.QueryLangRequestDTO);

  return {
    data: response.data ?? [],
    success: true,
    total: response.total ?? 0,
  };
};

const LangList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newLang, setNewLang] = useState<API.LangDTO>({enabled: true});

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editLang, setEditLang] = useState<API.LangDTO | undefined>();

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();

  const operationRender = useMemo(() => {
    return withAccessRender<API.LangDTO>(
      {
        'admin:platform:lang:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              setEditLang({ ...record } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:platform:lang:enable': (_, record) =>
          record.enabled ? (
            <Popconfirm
              key="disable"
              onPopupClick={stopEvent}
              title={<FormattedMessage id="admin.ui.public.label-enabled-true" />}
              description={<FormattedMessage id="admin.ui.pages.lang.disable-confirm-desc" />}
              onConfirm={async () => {
                const result = await handleEnable({ id: record.id || 0, enabled: false });
                if (result.success) {
                  actionRef.current?.reload();
                }
              }}
            >
              <a onClick={stopEvent}>
                <FormattedMessage id="admin.ui.public.label-enabled-false" />
              </a>
            </Popconfirm>
          ) : (
            <Popconfirm
              key="enable"
              onPopupClick={stopEvent}
              title={<FormattedMessage id="admin.ui.public.label-enabled-true" />}
              description={<FormattedMessage id="admin.ui.pages.lang.enable-confirm-desc" />}
              onConfirm={async () => {
                const result = await handleEnable({ id: record.id || 0, enabled: true });
                if (result.success) {
                  actionRef.current?.reload();
                }
              }}
            >
              <a onClick={stopEvent}>
                <FormattedMessage id="admin.ui.public.label-enabled-true" />
              </a>
            </Popconfirm>
          ),
        'admin:platform:lang:delete': (_, record) => (
          <Popconfirm
            title={<FormattedMessage id="admin.ui.public.confirm-ok-button" />}
            description={<FormattedMessage id="admin.ui.pages.lang.delete-confirm-desc" />}
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

  const columns: ExProColumnsType<API.LangDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label-application" />,
      dataIndex: 'application',
      valueType: 'dic',
      sorter: true,
      width: '8em',
      hideInSearch: true,
      fieldProps: {
        dictype: 'application'
      }
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label-lang-code" />,
      dataIndex: 'code',
      valueType: 'text',
      width: '7em',
      align: 'center',
      sorter: true,
      copyable: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label-application" />,
      dataIndex: 'application',
      sorter: false,
      valueType: 'dic',
      hideInTable: true,
      hideInSearch: false,
      fieldProps: {
        dictype: 'application',
        allowClear: true,
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label-name" />,
      dataIndex: 'name',
      valueType: 'text',
      ellipsis: { showTitle: true },
      sorter: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label-local-name" />,
      dataIndex: 'localName',
      valueType: 'text',
      ellipsis: { showTitle: true },
      sorter: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label-enabled" />,
      dataIndex: 'enabled',
      valueType: 'text',
      valueEnum: enabledStatusEnum(intl),
      width: '7em',
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
      width: operationRender.permissionCodes.length * 4 + 1 + 'em',
      fixed: 'right',
      align: 'center',
      render: (dom, record) => (
        <Flex justify="space-around">{operationRender.render(dom, record)}</Flex>
      ),
    });
  }

  return (
    <PageContainer pageHeaderRender={false}>
      <ProTablePage<API.LangDTO, API.QueryLangRequestDTO>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.lang.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:lang:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                setNewLang({enabled: true} as any);
                handleCreateModalOpen(true);
              }}
            >
              <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
            </Button>
          </Access>,
        ]}
        request={queryLangs}
        columns={columns as any}
        rowSelection={false}
      />
      {
        <EditForm
          mode="create"
          lang={newLang}
          onCancel={() => handleCreateModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.lang.create-new-title' })}
          onFinish={async (values) => {
            const result = await handleAdd(values as API.CreateLangRequestDTO);
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
          lang={editLang}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.lang.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              id: editLang?.id ?? 0,
              body: values as API.UpdateLangRequestDTO,
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

export default LangList;
