import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { patterns } from '@/utils/bizUtils';

interface EditRoleDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  role?: API.RoleDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditRoleDialog: React.FC<EditRoleDialogProps> = (
  props: EditRoleDialogProps,
) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.RoleDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.role.label-name' }),
      dataIndex: 'name',
      valueType: 'text',
      fieldProps: {
        maxLength: 64,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.role.rule-require-name' }),
          },
          {
            type: 'string',
            pattern: patterns.noneWhiteSpace,
            message: intl.formatMessage({ id: 'admin.ui.pages.role.rule-pattern-name' }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.role.label-memo' }),
      dataIndex: 'memo',
      valueType: 'textarea',
      fieldProps: {
        maxLength: 128,
        count: {show: true}
      },
    },
    {
      dataIndex: 'id',
      valueType: 'text',
      formItemProps: {
        hidden: true,
      }
    },
  ];

  if (props.mode === 'create') {
    columns.push({
      title: intl.formatMessage({ id: 'admin.ui.public.abel-enabled' }),
      dataIndex: 'enabled',
      valueType: 'radio',
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
    },);
  }

  return (
    <FormDraggableModal<API.PermissionItemDTO>
      {...props}
      onInit={(form) => {
        if (props.role) {
          form.resetFields();
          form.setFieldsValue({
            ...props.role,
            enabled: props.role?.enabled + ''
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditRoleDialog;
