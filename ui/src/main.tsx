import { useState } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router';

import AppLayout from './Layout';
import './index.css';
import { ConfigProvider, theme } from 'antd';

function getSystemPrefersDark() {
  if (typeof window === 'undefined' || typeof window.matchMedia !== 'function') {
    return false;
  }

  return window.matchMedia('(prefers-color-scheme: dark)').matches;
}

function RootApp() {
  const [isDarkMode, setIsDarkMode] = useState(() => getSystemPrefersDark());

  return (
    <ConfigProvider
      theme={{ algorithm: isDarkMode ? theme.darkAlgorithm : theme.defaultAlgorithm }}
    >
      <AppLayout
        isDarkMode={isDarkMode}
        onToggleDarkMode={setIsDarkMode}
      />
    </ConfigProvider>
  );
}

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <RootApp />
  </BrowserRouter>,
);
