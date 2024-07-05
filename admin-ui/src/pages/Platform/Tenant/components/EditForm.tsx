import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { patterns } from '@/utils/bizUtils';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

interface EditTenantDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  tenant?: API.TenantDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditTenantDialog: React.FC<EditTenantDialogProps> = (props: EditTenantDialogProps) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.TenantDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.tenant.label-code' }),
      dataIndex: 'code',
      valueType: 'text',
      readonly: props.mode === 'edit',
      tooltip: intl.formatMessage({ id: 'admin.ui.public.pattern-tootip-userName' }),
      fieldProps: {
        maxLength: 64,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.tenant.rule-require-code' }),
          },
          {
            required: false,
            type: 'string',
            pattern: patterns.userName,
            message: intl.formatMessage({ id: 'admin.ui.pages.tenant.rule-pattern-code' }),
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
      title: intl.formatMessage({ id: 'admin.ui.pages.tenant.label-name' }),
      dataIndex: 'name',
      valueType: 'text',
      fieldProps: {
        maxLength: 128
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.tenant.rule-require-name' }),
          },
        ],
      },
    },
  ];

  if (props.mode === 'create') {
    columns.push({
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
      title: intl.formatMessage({ id: 'admin.ui.pages.tenant.label-admin-username' }),
      dataIndex: 'adminUserName',
      valueType: 'text',
      tooltip: intl.formatMessage({ id: 'admin.ui.public.pattern-tootip-userName' }),
      fieldProps: {
        maxLength: 64
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            pattern: patterns.userName,
            message: intl.formatMessage({ id: 'admin.ui.pages.tenant.rule-require-admin-username' }),
          },
          {
            required: false,
            type: 'string',
            pattern: patterns.userName,
            message: intl.formatMessage({ id: 'admin.ui.pages.tenant.rule-pattern-admin-username' }),
          },
        ],
      },
    },
  );
  }

  columns.push( {
      title: intl.formatMessage({ id: 'admin.ui.pages.tenant.label-memo' }),
      dataIndex: 'memo',
      valueType: 'textarea',
      fieldProps: {
        maxLength: 128,
        count: {show: true}
      },
  })

  return (
    <FormDraggableModal<API.TenantDTO>
      {...props}
      onInit={(form) => {
        if (props.tenant) {
          form.resetFields();
          form.setFieldsValue({
            ...props.tenant,
            enabled: props.tenant.enabled + '',
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditTenantDialog;
