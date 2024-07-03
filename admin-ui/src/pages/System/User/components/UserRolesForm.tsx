import { DraggableModalProps } from '@/components/DraggableModal';
import FormDraggableModal from '@/components/DraggableModal/FormDraggableModal';
import { patterns } from '@/utils/bizUtils';
import { ProFormColumnsType, ProFormProps } from '@ant-design/pro-components';
import { listRoles } from '@/services/swagger/roleApi';
import { useIntl } from '@umijs/max';

interface RolesDialogProps extends DraggableModalProps {
  userRoles: {id: number, roleIds: number[]},
  onFinish?: ProFormProps['onFinish'];
}

const UserRolesDialog: React.FC<RolesDialogProps> = (props: RolesDialogProps) => {
  const intl = useIntl();

  const columns: ProFormColumnsType<API.UserDTO>[] = [
    {
      dataIndex: 'id',
      valueType: 'text',
      formItemProps: {
        hidden: true,
      }
    },
    {
      title: intl.formatMessage({ id: 'admin.ui.pages.user.label-roles' }),
      dataIndex: 'roleIds',
      valueType: 'select',
      request: async () => {
        const response = await listRoles();
        return response.filter(it => it.enabled).map(it => ({label: it.name, value: it.id}));
      },
      fieldProps: {
        mode: 'tags', 
        tokenSeparators: [','],
      },
    }
  ];


  return (
    <FormDraggableModal<API.UserDTO>
      {...props}
      onInit={(form) => {
        if (props.userRoles) {
          form.resetFields();
          console.log(props.userRoles)
          form.setFieldsValue({
            ...props.userRoles,
          });
        }
      }}
      columns={columns}
    />
  );
};

export default UserRolesDialog;
