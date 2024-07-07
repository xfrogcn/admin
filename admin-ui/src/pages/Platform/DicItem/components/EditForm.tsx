import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { patterns } from '@/utils/bizUtils';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

interface EditDicItemDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  dicItem?: API.DicItemDTO;
  dic: API.DicDTO;
  onFinish?: ProFormProps['onFinish'];
}

function getLangCode(dic: API.DicDTO, key: 'labelLangCodeValue' | 'labelLangCodeExtValue1' | 'labelLangCodeExtValue2', defaultLangCode: string) {
  let langCode = dic[key];
  return langCode ? langCode : defaultLangCode;
}

const EditDicItemDialog: React.FC<EditDicItemDialogProps> = (props: EditDicItemDialogProps) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.DicDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.dicitem.label-display-text' }),
      dataIndex: 'displayText',
      valueType: 'text',
      fieldProps: {
        min: 1,
        maxLength: 64,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.dicitem.rule-require-name' }),
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
      title: intl.formatMessage({ id: 'admin.ui.pages.dicitem.label-lang-code' }),
      dataIndex: 'langCode',
      valueType: 'text',
      tooltip: intl.formatMessage({ id: 'admin.ui.public.pattern-tooltip-code' }),
      fieldProps: {
        maxLength: 128,
      },
      formItemProps: {
        rules: [
          {
            pattern: patterns.permissionCode,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.public.pattern-tooltip-code' }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.dicitem.label-display-order' }),
      dataIndex: 'displayOrder',
      valueType: 'digit',
      resetValue: 100,
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'integer',
            min: 0,
            message: intl.formatMessage({ id: 'admin.ui.pages.dicitem.rule-display-order' }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: getLangCode(props.dic, 'labelLangCodeValue', 'admin.ui.pages.dicitem.label-value') }),
      dataIndex: 'value',
      valueType: 'text',
      fieldProps: {
        maxLength: 64,
      },
    },
    {
      title: intl.formatMessage({ id: getLangCode(props.dic, 'labelLangCodeExtValue1', 'admin.ui.pages.dicitem.label-ext-value1') }),
      dataIndex: 'extValue1',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
    },
    {
      title: intl.formatMessage({ id: getLangCode(props.dic, 'labelLangCodeExtValue2', 'admin.ui.pages.dicitem.label-ext-value2') }),
      dataIndex: 'extValue2',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.public.abel-enabled' }),
      dataIndex: 'enabled',
      valueType: 'radio',
      resetValue: 'true',
      valueEnum: {
        true: {
          text: intl.formatMessage({ id: 'admin.ui.public.label-enabled-true' }),
        },
        false: {
          text: intl.formatMessage({ id: 'admin.ui.public.label-enabled-false' }),
        },
      },
      formItemProps: {
        rules: [],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.dicitem.label-memo' }),
      dataIndex: 'memo',
      valueType: 'textarea',
      fieldProps: {
        maxLength: 128,
        count: { show: true },
      },
    },
  ];

  return (
    <FormDraggableModal<API.DicDTO>
      {...props}
      onInit={(form) => {
        if (props.dicItem) {
          form.resetFields();
          form.setFieldsValue({
            ...props.dicItem,
            enabled: props.dicItem.enabled + '',
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditDicItemDialog;
