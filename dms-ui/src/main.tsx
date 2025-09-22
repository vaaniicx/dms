import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router'

import AppLayout from './Layout';
import './index.css'
import { ConfigProvider, theme } from 'antd';

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <ConfigProvider theme={{ algorithm: theme.defaultAlgorithm }}>
      <AppLayout />
    </ConfigProvider>
  </BrowserRouter>,
);
