import DraggableModal, { DraggableModalProps } from '@/components/DraggableModal';
import { ExProFormColumnsType } from '@/components/ValueTypes';
import { BetaSchemaForm, ProFormInstance, ProFormProps } from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Col, Divider, Row, Space } from 'antd';
import { FieldData } from 'rc-field-form/es/interface';
import { MutableRefObject, useRef } from 'react';
import './index.less';

interface FormDraggableModalProps<T> extends DraggableModalProps {
  mode?: 'edit' | 'create';
  columns: ExProFormColumnsType<T>[];
  labelCol?: ProFormProps['labelCol'];
  wrapperCol?: ProFormProps['wrapperCol'];
  onInit?: (ref: ProFormInstance) => void;
  request?: ProFormProps['request'];
  onFinish?: ProFormProps['onFinish'];
  formRef?: MutableRefObject<ProFormInstance | undefined>;
}

function FormDraggableModal<T>(props: FormDraggableModalProps<T>): JSX.Element {
  const intl = useIntl();
  const formRef = props.formRef || useRef<ProFormInstance>();
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
        labelCol={props.labelCol ?? { span: 5 }}
        wrapperCol={props.wrapperCol ?? { span: 18 }}
        layoutType="Form"
        className="draggable-modal-form"
        onReset={(e) => {
          const resetValues: FieldData[] = [];
          props.columns
            .filter((c) => c.resetValue !== undefined)
            .forEach((c) => {
              resetValues.push({
                name: c.dataIndex,
                value: c.resetValue,
              });
            });
          formRef.current?.setFields(resetValues);
        }}
        submitter={{
          resetButtonProps: props.mode === 'create' ? undefined : false,
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
        {...(formProps as any)}
      />
    </DraggableModal>
  );
}

export default FormDraggableModal;
