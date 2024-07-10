import { withAccessRender } from '@/access';
import ProTablePage from '@/components/ProTablePage';
import { ExProColumnsType } from '@/components/ValueTypes';
import {
  createLangCorpus,
  deleteLangCorpus,
  enableLangCorpus,
  listLangCorpus,
  updateLangCorpus,
} from '@/services/swagger/langCorpusApi';
import { convertCommonQueryParams, enabledStatusEnum } from '@/utils/bizUtils';
import { stopEvent } from '@/utils/commonUtils';
import { useMessageBox } from '@/utils/messageUtils';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl, useNavigate } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useCallback, useMemo, useRef, useState } from 'react';

const handleAdd = useMessageBox<API.CreateLangCorpusRequestDTO, number[]>(createLangCorpus);
const handleUpdate = useMessageBox<{ id: number; body: API.UpdateLangCorpusRequestDTO }, void>(
  (args) => updateLangCorpus({ langCorpusId: args.id }, args.body),
);
const handleEnable = useMessageBox<{ id: number; enabled: boolean }, void>((args) =>
  enableLangCorpus({
    langCorpusId: args.id,
    enabled: args.enabled,
  }),
);
const handleDelete = useMessageBox<{ id: number }, void>((args) =>
  deleteLangCorpus({ langCorpusId: args.id }),
);

const queryLangCorpus = async (
  params: API.QueryLangCorpusRequestDTO & {
    pageSize?: number | undefined;
    current?: number | undefined;
  },
  sorter: any,
): Promise<{ data: API.LangCorpusDTO[]; success: boolean; total: number }> => {
  const response = await listLangCorpus({
    application: params.application,
    corpusType: params.corpusType,
    corpusGroup: params.corpusGroup,
    keyword: params.keyword,
    configured: params.configured,
    ...convertCommonQueryParams(params, sorter),
  } as API.QueryLangCorpusRequestDTO);

  return {
    data: response.data ?? [],
    success: true,
    total: response.total ?? 0,
  };
};

const LangCorpusList: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newLangCorpus, setNewLangCorpus] = useState<API.LangCorpusDTO>({ enabled: true });

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editLangCorpus, setEditLangCorpus] = useState<API.LangCorpusDTO | undefined>();
  const [groupCondition, setGroupCondition] = useState<API.LangCorpusDTO>({});

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();
  const navigate = useNavigate();

  const corpusGroupFilter = useCallback(
    (items: API.DicItemDTO[]) => {
      return items.filter((item) => {
        return (
          (!item.extValue1 || item.extValue1 === groupCondition.corpusType) &&
          (!item.extValue2 || item.extValue2 === groupCondition.application)
        );
      });
    },
    [groupCondition],
  );

  const operationRender = useMemo(() => {
    return withAccessRender<API.LangCorpusDTO>(
      {
        'admin:platform:langcorpus:edit': (_, record) => (
          <a
            key="edit"
            onClick={async () => {
              setEditLangCorpus({ ...record } as any);
              handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.public.edit-button" />
          </a>
        ),
        'admin:platform:langcorpus:local': (_, record) => (
          <a
            key="local"
            onClick={async () => {
              // setEditLangCorpus({ ...record } as any);
              // handleEditModalOpen(true);
            }}
          >
            <FormattedMessage id="admin.ui.pages.langcorpus.label-local" />
          </a>
        ),
        'admin:platform:langcorpus:enable': (_, record) =>
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
        'admin:platform:langcorpus:delete': (_, record) => (
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

  const columns: ExProColumnsType<API.LangCorpusDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-application" />,
      dataIndex: 'application',
      valueType: 'dic',
      sorter: true,
      hideInSearch: false,
      width: '14em',
      fieldProps: {
        dictype: 'application',
        allowClear: true,
        onChange: (value: string) => {
          setGroupCondition({ ...groupCondition, application: value });
        },
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-type" />,
      dataIndex: 'corpusType',
      valueType: 'dic',
      width: '10em',
      align: 'center',
      sorter: true,
      hideInSearch: false,
      fieldProps: {
        dictype: 'langcorpus-type',
        allowClear: true,
        onChange: (value: string) => {
          setGroupCondition({ ...groupCondition, corpusType: value });
        },
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-group" />,
      dataIndex: 'corpusGroup',
      valueType: 'dic',
      width: '10em',
      align: 'center',
      sorter: true,
      hideInSearch: false,
      fieldProps: {
        dictype: 'langcorpus-group',
        allowClear: true,
        filter: corpusGroupFilter,
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-search-local" />,
      dataIndex: 'configured',
      valueType: 'select',
      colSize: 1,
      sorter: false,
      hideInSearch: false,
      hideInTable: true,
      valueEnum: {
        true: {
          text: intl.formatMessage({ id: 'admin.ui.pages.langcorpus.label-search-local-true' }),
          status: 'Success',
        },
        false: {
          text: intl.formatMessage({ id: 'admin.ui.pages.langcorpus.label-search-local-false' }),
          status: 'Error',
        },
      },
    },
    {
      title: <FormattedMessage id="admin.ui.public.keyword" />,
      dataIndex: 'keyword',
      valueType: 'text',
      colSize: 1,
      sorter: false,
      hideInSearch: false,
      hideInTable: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-code" />,
      dataIndex: 'corpusCode',
      valueType: 'text',
      sorter: true,
      ellipsis: { showTitle: true },
      copyable: true,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-enabled" />,
      dataIndex: 'enabled',
      valueType: 'text',
      valueEnum: enabledStatusEnum(intl),
      width: '7em',
      align: 'center',
      sorter: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-memo" />,
      dataIndex: 'memo',
      valueType: 'text',
      width: '14em',
      sorter: false,
      ellipsis: { showTitle: true },
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
      <ProTablePage<API.LangCorpusDTO, API.QueryLangCorpusRequestDTO>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.langcorpus.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
        width="110em"
        search={{
          defaultColsNumber: 8,
          span: 8,
          collapsed: false,
        }}
        toolBarRender={() => [
          <Access accessible={access.hasPermission('admin:platform:lang:create')}>
            <Button
              type="primary"
              key="primary"
              onClick={() => {
                navigate('/platform/corpus/new')
              }}
            >
              <PlusOutlined /> <FormattedMessage id="admin.ui.public.new-button" />
            </Button>
          </Access>,
        ]}
        request={queryLangCorpus}
        columns={columns as any}
        rowSelection={false}
      />
      {/* {
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
      } */}
    </PageContainer>
  );
};

export default LangCorpusList;
