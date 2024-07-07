import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { enabledStatusEnum } from '@/utils/bizUtils';
import { ProFormInstance, ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { useRef } from 'react';

interface EditLangDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  lang?: API.LangDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditDicDialog: React.FC<EditLangDialogProps> = (props: EditLangDialogProps) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const columns: ExProFormColumnsType<API.LangDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.lang.label-application' }),
      dataIndex: 'application',
      valueType: 'dic',
      fieldProps: {
        dictype: 'application',
        autoSelectFirst: true,
        readonly: props.mode === 'edit',
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
      dataIndex: 'id',
      valueType: 'text',
      readonly: true,
      formItemProps: {
        hidden: true,
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.lang.label-lang' }),
      dataIndex: 'code',
      valueType: 'dic',
      fieldProps: {
        dictype: 'lang',
        autoSelectFirst: true,
        readonly: props.mode === 'edit',
        labelGetter: (item: API.DicItemDTO) => {
          return `${item.displayText} - (${item.value})`
        },
        onSelect: (item: API.DicItemDTO) => {
          if (!item) {
            return;
          }
          formRef.current?.setFieldsValue({
            name: item.extValue1,
            localName: item.extValue2,
          });

        }
      },
      formItemProps: {
        rules: [
          {
            required: true,
            // type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.lang.rule-require-lang' }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.lang.label-name' }),
      dataIndex: 'name',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.lang.label-local-name' }),
      dataIndex: 'localName',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
    },
  ];

  if (props.mode === 'create') {
    columns.push({
      title: intl.formatMessage({ id: 'admin.ui.public.abel-enabled' }),
      dataIndex: 'enabled',
      valueType: 'radio',
      resetValue: 'true',
      valueEnum: enabledStatusEnum(intl),
      formItemProps: {
        rules: [],
      },
    });
  }

  return (
    <FormDraggableModal<API.DicDTO>
      {...props}
      formRef={formRef}
      onInit={(form) => {
        if (props.lang) {
          form.resetFields();
          form.setFieldsValue({
            ...props.lang,
            enabled: props.lang.enabled + ''
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditDicDialog;
