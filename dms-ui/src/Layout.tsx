import { FileOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import type { BreadcrumbItemType } from 'antd/es/breadcrumb/Breadcrumb';
import type { ItemType } from 'antd/es/menu/interface';
import DocumentUpload from './pages/DocumentUpload';
import { Route, Routes, useNavigate } from 'react-router';
import DocumentDashboard from './pages/DocumentDashboard';
import DocumentSearch from './pages/DocumentSearch';

const { Header, Content, Footer, Sider } = Layout;

const breadCrumbs: BreadcrumbItemType[] = [
  {
    title: 'Start'
  },
  {
    title: 'Dokumente'
  },
  {
    title: 'Upload'
  }
]

function AppLayout() {
  const navigate = useNavigate();

  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const menuItems: MenuProps['items'] = [
    {
      key: 'documentMenu',
      icon: <FileOutlined />,
      label: 'Dokumente',
      onTitleClick: () => navigate(''),
      children: [
        {
          key: 'documentSubMenu1',
          label: 'Dokumente hochladen',
          onClick: () => navigate('document/upload')
        },
        {
          key: 'documentSubMenu2',
          label: 'Dokumente suchen',
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
          items={breadCrumbs}
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
              defaultSelectedKeys={['documentMenu']}
              defaultOpenKeys={['documentMenu']}
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
              <Route path='document/upload' element={<DocumentUpload />} />
              <Route path='document/search' element={<DocumentSearch />} />
            </Routes>
          </Content>
        </Layout>
      </Layout>

      <Footer style={{ textAlign: 'center' }}>
        Created by Group A, Winter-Semester {new Date().getFullYear()}
      </Footer>
    </Layout>
  );
}

export default AppLayout;
