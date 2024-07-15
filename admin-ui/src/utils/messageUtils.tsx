import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Modal, message } from 'antd';
import { MessageType } from 'antd/es/message/interface';
import { JointContent } from 'antd/lib/message/interface';
import { IntlShape } from 'react-intl';
const { confirm } = Modal;

let messageIntl: IntlShape;

export function setIntl(intl: IntlShape) {
  messageIntl = intl;
}

export const messageBox = {
  intl: null,
  loading: (content?: JointContent): MessageType => {
    const hide = message.loading(
      content || messageIntl.formatMessage({ id: 'admin.ui.public.processing-text' }),
    );
    return hide;
  },
  success: (content?: JointContent) => {
    message.success(
      content || messageIntl.formatMessage({ id: 'admin.ui.public.process-succeed-text' }),
    );
  },
  fail: (content?: JointContent) => {
    message.error(
      content || messageIntl.formatMessage({ id: 'admin.ui.public.process-failed-text' }),
    );
  },
};

export function useMessageBox<T1, R>(
  process: (arg:T1) => Promise<R>
): (arg: T1) => Promise<{ result?: Awaited<R>; success: boolean }> {
  return async (arg: T1) => {
    const result:{ result?: Awaited<R>; success: boolean } = { success: false };
    const hide = messageBox.loading();
    try {
      const response = await process(arg);
      result.result = response;
      result.success = true;
      hide();

      messageBox.success();
    } catch (error) {
      hide();
      messageBox.fail();
    }
    return result;
  };
}

export function confirmAction(shouldConfirm: boolean, action: () => void, messageLangId: string, cancel?: () => void ):void {
  if (shouldConfirm) {
    confirm({
      title: messageIntl.formatMessage({ id: 'admin.ui.public.confirm-title' }),
      icon: <ExclamationCircleOutlined />,
      content: messageIntl.formatMessage({ id: messageLangId }),
      okText: messageIntl.formatMessage({ id: 'admin.ui.public.confirm-ok-button' }),
      cancelText: messageIntl.formatMessage({ id: 'admin.ui.public.confirm-cancel-button' }),
      onOk: action,
      onCancel: () => {
        if (cancel) {
          cancel();
        }
      },
    });
  } else {
    action();
  }
}