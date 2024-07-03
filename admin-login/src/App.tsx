// Login.js
import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import axios from 'axios';

const Login = () => {
  const [form] = Form.useForm();

  const _csrfToken = document.querySelector('meta[name=csrf-token]')?.getAttribute("content")
  const [csrfToken] = useState(_csrfToken || '');

  const handleSubmit = async (values: any) => {
    
      (document.getElementById("login_form") as any)?.submit();

  }

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
      <Form
        form={form}
        layout="vertical"
        action="/login"
        method="POST"
        id='login_form'
        onFinish={handleSubmit}
        style={{ width: 300 }}
      >
        <Form.Item
          label="用户名"
          name="username"
          rules={[
            { required: true, message: '请输入用户名' },
            // 可以添加更多验证规则，如长度限制等
          ]}
        >
          <Input name='username'/>
        </Form.Item>
        <Form.Item
          label="密码"
          name="password"
          rules={[
            { required: true, message: '请输入密码' },
            // 密码强度验证等
          ]}
        >
          <Input.Password name='password'/>
        </Form.Item>
        
        <Input name='_csrf' type='hidden' value={csrfToken} />
        <Form.Item>
          <Button type="primary" htmlType="submit">
            登录
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Login;