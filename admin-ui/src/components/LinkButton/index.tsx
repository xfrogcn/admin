import { ConfigProvider } from 'antd';
import classNames from 'classnames';
import { FC, HTMLProps, useContext } from 'react';
import useStyle from './style';

interface LinkButtonProps extends HTMLProps<HTMLAnchorElement> {
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'link';
}

const LinkButton: FC<LinkButtonProps> = (props: LinkButtonProps) => {
  const { className, type } = props;
  const { getPrefixCls } = useContext(ConfigProvider.ConfigContext);
  const prefixCls = getPrefixCls('link-button');
  const { hashId } = useStyle(prefixCls);

  const classString = classNames(
    prefixCls,
    type ? `${prefixCls}-${type}` : undefined,
    className,
    hashId,
  );

  return <a className={classString} {...props} />;
};

export default LinkButton;
