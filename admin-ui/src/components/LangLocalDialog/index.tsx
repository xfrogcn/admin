import { ExProColumnsType } from '@/components/ValueTypes';
import { EditableFormInstance, EditableProTable } from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { Descriptions, DescriptionsProps, Flex } from 'antd';
import React, { useEffect, useRef, useState } from 'react';
import DraggableModal, { DraggableModalProps } from '../DraggableModal';

interface LangLocalDialogProps extends DraggableModalProps {
  langCorpus?: API.LangCorpusDTO;
  langLocal: Record<string, string>;
  langs: API.LangDTO[];
  readonly: boolean;
  onFinish?: (values: Record<string, string>) => Promise<void>;
}

interface LangLocalItem {
  code: string;
  value: string;
}

const LangLocalDialog: React.FC<LangLocalDialogProps> = (props: LangLocalDialogProps) => {
  const { langCorpus, langLocal, readonly, style, onFinish, ...modalProps } = props;
  const [langList, setLangList] = useState<readonly LangLocalItem[]>([]);
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);

  useEffect(() => {
    const items = [];
    for (const k in props.langLocal) {
      items.push({
        code: k,
        value: props.langLocal[k],
      });
    }
    setLangList(items);
    if (!props.readonly) {
      setEditableRowKeys(items.map(it => it.code));
    }
  }, [props.langLocal]);

  const tableRef = useRef<EditableFormInstance>();
  const intl = useIntl();

  const columns: ExProColumnsType<LangLocalItem>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label_lang_code" />,
      dataIndex: 'code',
      valueType: 'text',
      width: '6em',
      readonly: true,
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label_lang_name" />,
      dataIndex: 'name',
      valueType: 'text',
      width: '8em',
      ellipsis: true,
      readonly: true,
      renderText(text, record, index, action) {
        const lang = props.langs.findLast((it) => it.code === record.code);
        return lang?.name || '-';
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label_lang_local_name" />,
      dataIndex: 'localName',
      valueType: 'text',
      width: '10em',
      ellipsis: true,
      readonly: true,
      renderText(text, record, index, action) {
        const lang = props.langs.findLast((it) => it.code === record.code);
        return lang?.localName || '-';
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.lang.label_lang_local_value" />,
      dataIndex: 'value',
      valueType: 'textarea',
      fieldProps: {
        maxLength: 255,
      },
    },
  ];


  const descItems: DescriptionsProps['items'] = [
    {
      key: 'corpus-code',
      label: intl.formatMessage({ id: 'admin.ui.pages.langcorpus.label-code' }),
      children: props.langCorpus?.corpusCode || '-',
    },
    {
      key: 'corpus-memo',
      label: intl.formatMessage({ id: 'admin.ui.pages.langcorpus.label-memo' }),
      children: props.langCorpus?.memo || '-',
    },
  ];

  return (
    <DraggableModal
      {...modalProps}
      width={800}
      okButtonProps={props.readonly ? { style: { display: 'none' } } : {}}
      onOk={() => {
        if (onFinish) {
          const values: Record<string, string> = {};
          langList.forEach((lang) => {
            values[lang.code] = lang.value;
          });
          onFinish(values);
        }
      }}
      destroyOnClose={true}
      getContainer={document.body}
    >
      <div style={{ height: 400, ...style }}>
        <Descriptions items={descItems} />
        <EditableProTable<LangLocalItem>
          style={{ marginTop: 16 }}
          editableFormRef={tableRef}
          headerTitle={<FormattedMessage id="admin.ui.pages.lang.lang_local_list" />}
          rowKey="code"
          search={false}
          pagination={false}
          scroll={{ y: 240 }}
          columns={columns as any}
          value={langList}
          onChange={setLangList}
          maxLength={10000}
          options={{ reload: false, setting: false, density: false }}
          recordCreatorProps={false}
          editable={{
            type: 'multiple',
            editableKeys,
            saveText: <FormattedMessage id="admin.ui.public.save-button" />,
            cancelText: <FormattedMessage id="admin.ui.public.cancel-button" />,
            actionRender: (row, config, defaultDoms) => {
              return [defaultDoms.save, defaultDoms.cancel];
            },
            onValuesChange: (record, recordList) => {
              setLangList(recordList);
            },
            onChange: setEditableRowKeys,
          }}
        />
      </div>
    </DraggableModal>
  );
};

export default LangLocalDialog;
