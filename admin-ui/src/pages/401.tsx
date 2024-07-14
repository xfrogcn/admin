import { Result } from 'antd';
import React from 'react';

const NoFoundPage: React.FC = () => (
  <Result
    // @ts-ignore
    status="401"
    title="401"
    subTitle="Login..."
  />
);

export default NoFoundPage;
