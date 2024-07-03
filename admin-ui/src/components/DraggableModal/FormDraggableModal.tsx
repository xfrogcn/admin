import DraggableModal, { DraggableModalProps } from '@/components/DraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { BetaSchemaForm, ProFormInstance, ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Col, Divider, Row, Space } from 'antd';
import { useRef } from 'react';

interface FormDraggableModalProps<T> extends DraggableModalProps {
  mode?: 'edit' | 'create';
  columns: ExProFormColumnsType<T>[];
  onInit?: (ref: ProFormInstance) => void;
  request?: ProFormProps['request'];
  onFinish?: ProFormProps['onFinish'];
}

function FormDraggableModal<T>(props: FormDraggableModalProps<T>): JSX.Element {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const { mode, onInit, ...formProps } = props;

  return (
    <DraggableModal
      maskClosable={false}
      {...props}
      footer={false}
      onOpen={() => {
        if (formRef && formRef.current) {
          if (onInit) {
            onInit(formRef.current);
          }
        }
      }}
    >
      <Divider />
 
      <BetaSchemaForm<T>
        formRef={formRef}
        layout="horizontal"
        grid={true}
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 18 }}
        layoutType="Form"
        
        submitter={{
          resetButtonProps: props.mode==='create' ? undefined: false,
          render: (_, doms) => {
            return (
              <>
                <Divider />
                <Row>
                  <Col span={14} offset={4}>
                    <Space>{doms}</Space>
                  </Col>
                </Row>
              </>
            );
          },
        }}
        {...formProps as any}
      />
    </DraggableModal>
  );
}

export default FormDraggableModal;
