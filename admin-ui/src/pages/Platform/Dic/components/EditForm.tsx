import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { patterns } from '@/utils/bizUtils';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

interface EditDicDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  dic?: API.DicDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditDicDialog: React.FC<EditDicDialogProps> = (props: EditDicDialogProps) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.DicDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.dic.label-name' }),
      dataIndex: 'name',
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
            message: intl.formatMessage({ id: 'admin.ui.pages.dic.rule-require-name' }),
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
      title: intl.formatMessage({ id: 'admin.ui.pages.dic.label-type' }),
      dataIndex: 'type',
      valueType: 'text',
      tooltip: intl.formatMessage({id: 'admin.ui.public.pattern-tootip-userName'}),
      fieldProps: {
        maxLength: 128
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.dic.rule-require-type' }),
          },
          {
            pattern: patterns.userName,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.dic.rule-pattern-type' }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.dic.label-memo' }),
      dataIndex: 'memo',
      valueType: 'textarea',
      fieldProps: {
        maxLength: 128,
        count: {show: true}
      },
  }
  ];

  
  return (
    <FormDraggableModal<API.DicDTO>
      {...props}
      onInit={(form) => {
        if (props.dic) {
          form.resetFields();
          form.setFieldsValue({
            ...props.dic,
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditDicDialog;