import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { ProFormInstance, ProFormProps } from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import { useCallback, useRef, useState } from 'react';

interface EditLangCorpusDialogProps extends DraggableModalProps {
  langCorpus?: API.LangCorpusDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditLangCorpusDialog: React.FC<EditLangCorpusDialogProps> = (props: EditLangCorpusDialogProps) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const [groupCondition, setGroupCondition] = useState<API.LangCorpusDTO>({});


  const corpusGroupFilter = useCallback(
    (items: API.DicItemDTO[]) => {
      return items.filter((item) => {
        return (
          (!item.extValue1 || item.extValue1 === props.langCorpus?.application) &&
          (!item.extValue2 || item.extValue2 === groupCondition.application)
        );
      });
    },
    [groupCondition],
  );


  const columns: ExProFormColumnsType<API.UpdateLangCorpusRequestDTO>[] = [
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
    {
      title: <FormattedMessage id="admin.ui.pages.langcorpus.label-memo" />,
      dataIndex: 'memo',
      valueType: 'textarea',
      fieldProps: {
        maxLength: 128,
      },
    },
  ];

  return (
    <FormDraggableModal<API.UpdateLangCorpusRequestDTO>
      {...props}
      formRef={formRef}
      onInit={(form) => {
        if (props.langCorpus) {
          form.resetFields();
          form.setFieldsValue({
            ...props.langCorpus,
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditLangCorpusDialog;
