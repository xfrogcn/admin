import EditableProTablePage from '@/components/EditableProTablePage';
import { ExProColumnsType, ExProFormColumnsType } from '@/components/ValueTypes';
import { listLanguages } from '@/services/swagger/langApi';
import { createLangCorpus } from '@/services/swagger/langCorpusApi';
import { patterns } from '@/utils/bizUtils';
import { useMessageBox } from '@/utils/messageUtils';
import type { ActionType, ProFormInstance } from '@ant-design/pro-components';
import {
  BetaSchemaForm,
  EditableFormInstance,
  FooterToolbar,
  ListToolBar,
  PageContainer,
} from '@ant-design/pro-components';
import { FormattedMessage, useAccess, useIntl } from '@umijs/max';
import { Card, Flex, message, Popconfirm } from 'antd';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';

const handleAdd = useMessageBox<API.CreateLangCorpusRequestDTO, number[]>(createLangCorpus);

let LINE_ID: number = 0;

const CreateLangCorpusPage: React.FC = () => {
  const [langs, setLangs] = useState<readonly API.LangDTO[]>([]);
  const [groupCondition, setGroupCondition] = useState<API.LangCorpusDTO>({});
  const [corpusLines, setCorpusLines] = useState<
    readonly (API.LangCorpusItemDTO & { id: number })[]
  >([]);
  const currentEditLineId = useRef<number>(0);

  const tableRef = useRef<EditableFormInstance>();
  const intl = useIntl();
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

  useEffect(() => {
    if (!groupCondition.application) {
      setLangs([]);
      return;
    }
    listLanguages({ pageSize: 300, pageNum: 1, application: groupCondition.application }).then(
      (res) => {
        setLangs(res.data || []);
      },
    );
  }, [groupCondition.application]);


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
          {
            validator: (rule, value, callback) => {
              if (
                corpusLines.filter(
                  (it) => it.id !== currentEditLineId.current && it.corpusCode === value,
                ).length >= 1
              ) {
                callback(
                  intl.formatMessage({ id: 'admin.ui.pages.langcorpus.rule-corpus-code-unique' }),
                );
                return;
              }
              callback();
            },
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
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-search-local" />,
      dataIndex: 'langLocales',
      valueType: 'lang-local',
      fieldProps: {
        langs: langs,
        corpus: () => {
          let row = undefined;
          if (tableRef.current && tableRef.current.getRowsData) {
            const rows = tableRef.current?.getRowsData();
            if (rows?.length === 1) {
              row = rows[0];
            }
          }
          return row;
        },
      },
    },
    {
      title: <FormattedMessage id="admin.ui.public.option-button" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      width: '8em',
      fixed: 'right',
      align: 'center',
      render: (text, record, _, action) => (
        <Flex justify="space-around">
          <a
            key="editable"
            onClick={() => {
              currentEditLineId.current = record.id || 0;
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
          </a>
        </Flex>
      ),
    },
  ];

  const onSubmit = useCallback(
    async (values: API.CreateLangCorpusRequestDTO) => {
      if (
        tableRef.current &&
        tableRef.current.getRowsData &&
        // @ts-ignore
        tableRef.current.getRowsData().length > 0
      ) {
        message.error(intl.formatMessage({ id: 'admin.ui.pages.langcorpus.corpus-list-editing' }));
        return;
      }
      if (corpusLines.length === 0) {
        message.error(intl.formatMessage({ id: 'admin.ui.pages.langcorpus.corpus-list-empty' }));
        return;
      }

      const request: API.CreateLangCorpusRequestDTO = {
        ...values,
        corpusItems: corpusLines.map((it) => ({
          enabled: true,
          corpusCode: it.corpusCode,
          langLocales: it.langLocales,
          memo: it.memo,
        })),
      };
      console.log(request);
      const result = await handleAdd(request);
      if (result.success) {
      }
    },
    [corpusLines],
  );

  return (
    <PageContainer pageHeaderRender={false} className="none-ending-space">
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
          onFinish={onSubmit}
          submitter={{
            render: (_, dom) => <FooterToolbar>{dom}</FooterToolbar>,
          }}
        />
      </Card>

      <EditableProTablePage<API.LangCorpusItemDTO & { id: number }>
        style={{ marginTop: 16 }}
        editableFormRef={tableRef}
        headerTitle={<FormattedMessage id="admin.ui.pages.langcorpus.create-corpus-list" />}
        rowKey="id"
        search={false}
        pagination={false}
        columns={corpusTableColumns as any}
        value={corpusLines}
        onChange={setCorpusLines}
        maxLength={10000}
        options={{ reload: false, setting: false, density: false }}
        recordCreatorProps={{
          position: 'top',
          record: () => {
            LINE_ID++;
            currentEditLineId.current = LINE_ID;
            return { id: LINE_ID, langLocales: {}, enabled: true, corpusCode: '' };
          },
        }}
        editable={{
          type: 'single',
          saveText: <FormattedMessage id="admin.ui.public.save-button" />,
          cancelText: <FormattedMessage id="admin.ui.public.cancel-button" />,
          actionRender: (row, config, defaultDoms) => {
            return [defaultDoms.save, defaultDoms.cancel];
          },
        }}
      />
    </PageContainer>
  );
};

export default CreateLangCorpusPage;