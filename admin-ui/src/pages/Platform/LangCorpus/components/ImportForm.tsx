import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { ProFormInstance } from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { useMemo, useRef } from 'react';

interface ImportCorpusDialogProps extends DraggableModalProps {
  langs: readonly API.LangDTO[];
  onFinish?: (values: (API.LangCorpusItemDTO & { id: number })[]) => Promise<void>;
}

type ImportValues = {
  langCode: string;
  json: string;
};

const ImportCorpusDialog: React.FC<ImportCorpusDialogProps> = (props: ImportCorpusDialogProps) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const options = useMemo(() => {
    return (props.langs || []).map((it) => ({
      label: `${it.code} - ${it.name} - ${it.localName}`,
      value: it.code,
    }));
  }, [props.langs]);

  const columns: ExProFormColumnsType<ImportValues>[] = [
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-import-lang" />,
      dataIndex: 'langCode',
      valueType: 'select',
      fieldProps: {
        options: options,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.langcorpus.rule-require-import-lang',
            }),
          },
        ],
      },
    },
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-import-json" />,
      dataIndex: 'json',
      valueType: 'textarea',
      fieldProps: {
        rows: 10,
        placeholder: '{\n  "admin.ui.public.option-button": \'操作\'\n}',
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.langcorpus.rule-require-import-json',
            }),
          },
          {
            type: 'string',
            validator: (rule, value, callback) => {
              try {
                const json = JSON.parse(value);
                callback();
              } catch (e) {
                callback(
                  intl.formatMessage({ id: 'admin.ui.pages.langcorpus.invalid-import-json' }),
                );
              }
            },
          },
        ],
      },
    },
  ];

  return (
    <FormDraggableModal<ImportValues>
      destroyOnClose={true}
      getContainer={document.body}
      {...props}
      formRef={formRef}
      onInit={(form) => {
        form.resetFields();
      }}
      columns={columns}
      onFinish={async (values) => {
        if (!props.onFinish) {
          return;
        }
        const json = JSON.parse(values.json);
        const items = [];
        for (const key in json) {
          const local: Record<string, string> = {};
          local[values.langCode] = json[key];
          const corpusItem: API.LangCorpusItemDTO & { id: number } = {
            id: 0,
            langLocales: local,
            enabled: true,
            corpusCode: key,
            memo: json[key],
          };
          items.push(corpusItem);
        }
        await props.onFinish(items);
      }}
    />
  );
};

export default ImportCorpusDialog;
