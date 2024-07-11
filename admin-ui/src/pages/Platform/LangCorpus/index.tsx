import { withAccessRender } from '@/access';
import LangLocalDialog from '@/components/LangLocalDialog';
import { usePageTabReload } from '@/components/PageTabs';
import ProTablePage from '@/components/ProTablePage';
import { ExProColumnsType } from '@/components/ValueTypes';
import { listLanguages } from '@/services/swagger/langApi';
import {
  configLangLocal,
  deleteLangCorpus,
  enableLangCorpus,
  getLangCorpus,
  listLangCorpus,
  updateLangCorpus,
} from '@/services/swagger/langCorpusApi';
import { convertCommonQueryParams, enabledStatusEnum } from '@/utils/bizUtils';
import { stopEvent } from '@/utils/commonUtils';
import { useMessageBox } from '@/utils/messageUtils';
import { EyeOutlined, PlusOutlined } from '@ant-design/icons';
import type { ActionType } from '@ant-design/pro-components';
import { PageContainer } from '@ant-design/pro-components';
import { Access, FormattedMessage, useAccess, useIntl, useNavigate } from '@umijs/max';
import { Button, Flex, Popconfirm } from 'antd';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import EditForm from './components/EditForm';

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
const handleConfigLangLocal = useMessageBox<{ id: number; body: Record<string, any> }, void>(
  (args) => configLangLocal({ langCorpusId: args.id }, args.body),
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
  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editLangCorpus, setEditLangCorpus] = useState<API.LangCorpusDTO | undefined>();
  const [groupCondition, setGroupCondition] = useState<API.LangCorpusDTO>({});

  const [editLocalOpen, handleEditLocalOpen] = useState<boolean>(false);
  const [langLocal, setLangLocal] = useState<Record<string, string>>({});
  const [langs, setLangs] = useState<API.LangDTO[]>([]);
  const [lineLangs, setLineLangs] = useState<API.LangDTO[]>([]);
  const [readonly, setReadonly] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();
  const navigate = useNavigate();

  usePageTabReload(() => {
    actionRef.current?.reload();
  });

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

  const openLangLocalDialog = useCallback(
    async (record: API.LangCorpusDTO, readonly: boolean) => {
      const applicationLangs = langs.filter((it) => it.application === record.application);
      setLineLangs(applicationLangs);

      const corpus = await getLangCorpus({ langCorpusId: record.id || 0 });

      let localMap: Record<string, string> = {};
      // @ts-ignore
      applicationLangs.forEach((l) => (localMap[l.code] = null));

      if (corpus && corpus.langLocales) {
        localMap = { ...localMap, ...corpus.langLocales };
      }

      setEditLangCorpus(record);
      setLangLocal(localMap);
      setReadonly(readonly);
      handleEditLocalOpen(true);
    },
    [langs],
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
          <a key="local" onClick={() => openLangLocalDialog(record, false)}>
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
            key="delete"
            title={<FormattedMessage id="admin.ui.public.confirm-ok-button" />}
            description={<FormattedMessage id="admin.ui.pages.lang.delete-confirm-desc" />}
            onConfirm={async () => {
              const result = await handleDelete({ id: record.id || 0 });
              if (result.success) {
                actionRef.current?.reload();
              }
            }}
          >
            <a>
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
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-code" />,
      dataIndex: 'corpusCode',
      valueType: 'text',
      sorter: true,
      width: '15em',
      ellipsis: { showTitle: true },
      copyable: true,
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
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-application" />,
      dataIndex: 'application',
      valueType: 'dic',
      sorter: true,
      hideInSearch: false,
      width: '8em',
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
      width: '8em',
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
      width: '8em',
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
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-search-local" />,
      dataIndex: 'configured',
      valueType: 'text',
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
      render: (dom, record) => {
        return [
          <Button key="edit" type="link" onClick={() => openLangLocalDialog(record, true)}>
            <EyeOutlined />
          </Button>,
          dom,
        ];
      },
      width: '14em',
      align: 'center',
      sorter: false,
      fixed: 'right',
      hideInSearch: true,
    },
  ];

  if (operationRender.hasPermisions) {
    columns.push({
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: operationRender.permissionCodes.length * 4 + 'em',
      fixed: 'right',
      align: 'center',
      render: (dom, record) => (
        <Flex justify="space-around">{operationRender.render(dom, record)}</Flex>
      ),
    });
  }

  useEffect(() => {
    listLanguages({ pageSize: 300, pageNum: 1 }).then((res) => {
      setLangs(res.data || []);
    });
  }, []);

  return (
    <PageContainer pageHeaderRender={false}>
      <ProTablePage<API.LangCorpusDTO, API.QueryLangCorpusRequestDTO>
        headerTitle={intl.formatMessage({
          id: 'admin.ui.pages.langcorpus.table-title',
        })}
        actionRef={actionRef}
        rowKey="id"
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
                navigate('/platform/corpus/new');
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
      {
        <EditForm
          langCorpus={editLangCorpus}
          onCancel={() => handleEditModalOpen(false)}
          title={intl.formatMessage({ id: 'admin.ui.pages.langcorpus.edit-title' })}
          onFinish={async (values) => {
            const result = await handleUpdate({
              id: editLangCorpus?.id ?? 0,
              body: values as API.UpdateLangCorpusRequestDTO,
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
      {
        <LangLocalDialog
          langs={lineLangs}
          onCancel={() => handleEditLocalOpen(false)}
          langCorpus={editLangCorpus}
          title={intl.formatMessage({ id: 'admin.ui.pages.lang.label_lang_local_value' })}
          langLocal={langLocal}
          readonly={readonly}
          onFinish={async (values) => {
            const result = await handleConfigLangLocal({
              id: editLangCorpus?.id || 0,
              body: values,
            });
            if (result.success) {
              actionRef.current?.reload();
              handleEditLocalOpen(false);
            }
          }}
          width="600px"
          open={editLocalOpen}
        />
      }
    </PageContainer>
  );
};

export default LangCorpusList;
