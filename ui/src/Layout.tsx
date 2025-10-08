import { FileOutlined, DashboardOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import type { BreadcrumbItemType } from 'antd/es/breadcrumb/Breadcrumb';
import type { ItemType } from 'antd/es/menu/interface';
import DocumentUpload from './pages/DocumentUpload';
import { Link, Route, Routes, useLocation, useNavigate } from 'react-router';
import DocumentDashboard from './pages/DocumentDashboard';
import DocumentSearch from './pages/DocumentSearch';

const { Header, Content, Footer, Sider } = Layout;

const breadCrumbs: Record<string, string> = {
  'dashboard': 'Dashboard',
  'document': 'Document',
  'document/upload': 'Upload Document',
  'document/search': 'Search Document',
}

function AppLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const path: string[] = location.pathname.split('/').filter(i => i);

  const extraBreadcrumbItems = path.map((_, index) => {
    const url = `${path.slice(0, index + 1).join('/')}`;
    const label = breadCrumbs[url] || url;

    return {
      title: label
    };
  });

  const breadcrumbItems: BreadcrumbItemType[] = [
    { title: <Link to="/">Home</Link> },
    ...extraBreadcrumbItems,
  ];
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const menuItems: MenuProps['items'] = [
    {
      key: 'dashboardMenu',
      icon: <DashboardOutlined />,
      label: 'Dashboard',
      onClick: () => navigate('dashboard')
    },
    {
      key: 'documentMenu',
      icon: <FileOutlined />,
      label: 'Document',
      children: [
        {
          key: 'documentSubMenu2',
          label: 'Upload Document',
          onClick: () => navigate('document/upload')
        },
        {
          key: 'documentSubMenu3',
          label: 'Search Document',
          onClick: () => navigate('document/search')
        }
      ]
    } as ItemType
  ];

  return (
    <Layout style={{ display: 'flex', minHeight: "100vh", width: '100vw' }}>
      <Header style={{ alignItems: 'center' }}>
        <div style={{ color: "white", fontWeight: "bold", marginRight: 20 }}>
          DMS - Document Management System
        </div>
      </Header>

      <Layout style={{
        padding: '0 48px',
      }}>
        <Breadcrumb
          style={{ margin: '16px 0' }}
          items={breadcrumbItems}
        />
        <Layout
          style={{
            padding: '24px 0',
            background: colorBgContainer,
            borderRadius: borderRadiusLG
          }}
        >
          <Sider width={216} style={{ background: colorBgContainer }}>
            <Menu
              mode="inline"
              defaultSelectedKeys={['dashboardMenu']}
              defaultOpenKeys={['dashboardMenu']}
              style={{ height: '100%', width: '100%' }}
              items={menuItems}
            />
          </Sider>
          <Content style={{
            display: 'flex',
            justifyContent: 'center',
            padding: '0 24px'
          }}>
            <Routes>
              <Route index element={<DocumentDashboard />} />
              <Route path='dashboard' element={<DocumentDashboard />} />
              <Route path='document/upload' element={<DocumentUpload />} />
              <Route path='document/search' element={<DocumentSearch />} />
            </Routes>
          </Content>
        </Layout>
      </Layout>

      <Footer style={{ textAlign: 'center' }}>
        Created by Group A, Winter-Semester 2025
      </Footer>
    </Layout>
  );
}

export default AppLayout;
