import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { patterns } from '@/utils/bizUtils';
import { ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';

interface EditUserDialogProps extends DraggableModalProps {
  mode: 'edit' | 'create';
  rootOrganizationId: number;
  user?: API.UserDTO;
  onFinish?: ProFormProps['onFinish'];
}

const EditUserDialog: React.FC<EditUserDialogProps> = (props: EditUserDialogProps) => {
  const intl = useIntl();

  const columns: ExProFormColumnsType<API.UserDTO>[] = [
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-organizationName' }),
      dataIndex: 'organizationId',
      valueType: 'organizationSelector',
      fieldProps: {
        rootOrganizationId: props.rootOrganizationId,
      },
      formItemProps: {
        rules:
          props.mode === 'create'
            ? [
                {
                  required: true,
                  message: intl.formatMessage({
                    id: 'admin.ui.pages.organization.rule-require-parent-id',
                  }),
                },
              ]
            : [],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-userName' }),
      dataIndex: 'userName',
      valueType: 'text',
      tooltip: intl.formatMessage({ id: 'admin.ui.public.pattern-tootip-userName' }),
      readonly: props.mode === 'edit',
      fieldProps: {
        maxLength: 64,
        minLength: 3,
      },
      formItemProps: {
        rules: [
          {
            required: true,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.user.rule-require-userName' }),
          },
          {
            type: 'string',
            pattern: patterns.userName,
            message: intl.formatMessage({ id: 'admin.ui.pages.user.rule-pattern-userName' }),
          },
        ],
      },
    },
  ];

  columns.push({
    title: intl.formatMessage({ id: 'admin.ui.pages.user.label-name' }),
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
          message: intl.formatMessage({ id: 'admin.ui.pages.user.rule-require-name' }),
        },
      ],
    },
  });
  if (props.mode === 'create') {
    columns.push({
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-password' }),
      dataIndex: 'password',
      valueType: 'password',
      tooltip: intl.formatMessage({ id: 'admin.ui.pages.user.tooltip-password' }),
      formItemProps: {
        rules: [
          {
            required: true,
            pattern: patterns.password,
            type: 'string',
            message: intl.formatMessage({ id: 'admin.ui.pages.user.rule-pattern-password' }),
          },
        ],
      },
    });
  }

  columns.push(
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-sex' }),
      dataIndex: 'sex',
      valueType: 'radio',
      valueEnum: {
        M: {
          text: intl.formatMessage({ id: 'admin.ui.pages.user.label-sex-male' }),
        },
        F: {
          text: intl.formatMessage({ id: 'admin.ui.pages.user.label-sex-female' }),
        },
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-mobilePhone' }),
      dataIndex: 'mobilePhone',
      valueType: 'text',
      fieldProps: {
        maxLength: 32,
      },
      formItemProps: {
        rules: [
          {
            required: false,
            pattern: patterns.telphone,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.pages.organization.rule-pattern-telephone',
            }),
          },
        ],
      },
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-email' }),
      dataIndex: 'email',
      valueType: 'text',
      fieldProps: {
        maxLength: 128,
      },
      formItemProps: {
        rules: [
          {
            required: false,
            pattern: patterns.email,
            type: 'string',
            message: intl.formatMessage({
              id: 'admin.ui.public.rule-pattern-email',
            }),
          },
        ],
      },
    },
  );
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
    });
  }

  return (
    <FormDraggableModal<API.UserDTO>
      {...props}
      onInit={(form) => {
        if (props.user) {
          form.resetFields();
          form.setFieldsValue({
            ...props.user,
            enabled: props.user.enabled + '',
          });
        }
      }}
      columns={columns}
    />
  );
};

export default EditUserDialog;
