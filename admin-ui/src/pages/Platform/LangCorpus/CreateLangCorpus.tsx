import { withAccessRender } from '@/access';
import { ExProColumnsType, ExProFormColumnsType } from '@/components/ValueTypes';
import {
  createLangCorpus,
  deleteLangCorpus,
  enableLangCorpus,
  listLangCorpus,
  updateLangCorpus,
} from '@/services/swagger/langCorpusApi';
import { convertCommonQueryParams, patterns } from '@/utils/bizUtils';
import { stopEvent } from '@/utils/commonUtils';
import { useMessageBox } from '@/utils/messageUtils';
import type { ActionType, ProFormInstance } from '@ant-design/pro-components';
import {
  BetaSchemaForm,
  EditableProTable,
  FooterToolbar,
  ListToolBar,
  PageContainer,
} from '@ant-design/pro-components';
import { FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Card, Flex, Popconfirm, Space } from 'antd';
import React, { useCallback, useMemo, useRef, useState } from 'react';
import EditableProTablePage from '@/components/EditableProTablePage';

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

let LINE_ID: number = 0;

const CreateLangCorpusPage: React.FC = () => {
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  const [newLangCorpus, setNewLangCorpus] = useState<API.LangCorpusDTO>({ enabled: true });

  const [editModalOpen, handleEditModalOpen] = useState<boolean>(false);
  const [editLangCorpus, setEditLangCorpus] = useState<API.LangCorpusDTO | undefined>();
  const [groupCondition, setGroupCondition] = useState<API.LangCorpusDTO>({});
  const [corpusLines, setCorpusLines] = useState<readonly API.LangCorpusDTO[]>([]);

  const actionRef = useRef<ActionType>();
  const intl = useIntl();
  const access = useAccess();
  const formRef = useRef<ProFormInstance>();

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

  const columns: ExProFormColumnsType<API.CreateLangCorpusRequestDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-application" />,
      dataIndex: 'application',
      valueType: 'dic',
      fieldProps: {
        span: 12,
        dictype: 'application',
        allowClear: true,
        onChange: (value: string) => {
          setGroupCondition({ ...groupCondition, application: value });
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.lang.rule-require-application' }),
          },
        ],
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-type" />,
      dataIndex: 'corpusType',
      valueType: 'dic',
      fieldProps: {
        dictype: 'langcorpus-type',
        allowClear: true,
        onChange: (value: string) => {
          setGroupCondition({ ...groupCondition, corpusType: value });
        },
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.langcorpus.rule-require-corpus-type',
            }),
          },
        ],
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-group" />,
      dataIndex: 'corpusGroup',
      valueType: 'dic',
      fieldProps: {
        dictype: 'langcorpus-group',
        allowClear: true,
        filter: corpusGroupFilter,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.langcorpus.rule-require-corpus-group',
            }),
          },
        ],
      },
    },
  ];

  const corpusTableColumns: ExProColumnsType<API.LangCorpusDTO>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-code" />,
      dataIndex: 'corpusCode',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
      tooltip: <FormattedMessage id="admin.ui.public.pattern-tooltip-code" />,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.langcorpus.rule-require-corpus-code',
            }),
          },
          {
            type: 'string',
            pattern: patterns.permissionCode,
            message: intl.formatMessage({ id: 'admin.ui.public.pattern-tooltip-code' }),
          },
        ],
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-memo" />,
      dataIndex: 'memo',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
    },
    {
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: '8em',
      fixed: 'right',
      align: 'center',
      render: (text, record, _, action) => (<Flex justify="space-around">
        <a
          key="editable"
          onClick={() => {
            action?.startEditable?.(record.id || 0);
          }}
        >
          <FormattedMessage id="admin.ui.public.edit-button" />
        </a>
        <a
          key="delete"
          onClick={() => {
            setCorpusLines(corpusLines.filter((item) => item.id !== record.id));
          }}
        >
          <FormattedMessage id="admin.ui.public.delete-button" />
        </a></Flex>)
    },
  ];

  return (
    <PageContainer pageHeaderRender={false} className='none-ending-space'>
        <Card>
          <ListToolBar
            title={<FormattedMessage id="admin.ui.pages.langcorpus.create-basic-info" />}
          />

          <BetaSchemaForm<API.CreateLangCorpusRequestDTO>
            formRef={formRef}
            layout="horizontal"
            grid={true}
            rowProps={{
              gutter: [16, 0],
            }}
            colProps={{ lg: 12, md: 12, xl: 8 }}
            labelCol={{ span: 6 }}
            wrapperCol={{ span: 18 }}
            layoutType="Form"
            columns={columns as any}
            submitter={{
              render: (_, dom) => <FooterToolbar>{dom}</FooterToolbar>,
            }}
          />
        </Card>

        <EditableProTablePage<API.LangCorpusDTO>
          style={{marginTop: 16}}
          headerTitle={<FormattedMessage id="admin.ui.pages.langcorpus.create-corpus-list" />}
          rowKey="id"
          search={false}
          pagination={false}
          columns={corpusTableColumns as any}
          value={corpusLines}
          onChange={setCorpusLines}
          maxLength={10000}
          options={{reload: false, setting: false, density: false}}
          recordCreatorProps={{
            position: 'top',
            record: () => ({ id: ++LINE_ID }),
          }}
        />
    </PageContainer>
  );
};

export default CreateLangCorpusPage;
