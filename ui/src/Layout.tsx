import {
    FileOutlined,
    DashboardOutlined,
    MoonFilled,
    SunFilled,
    InfoCircleOutlined,
    UploadOutlined,
    SearchOutlined,
} from "@ant-design/icons";
import type { MenuProps } from "antd";
import { Breadcrumb, Layout, Menu, Switch, theme } from "antd";
import type { BreadcrumbItemType } from "antd/es/breadcrumb/Breadcrumb";
import { Link, Route, Routes, useLocation, useNavigate } from "react-router";
import DocumentDashboard from "./pages/DocumentDashboard";
import DocumentUpload from "./pages/DocumentUpload";
import DocumentSearch from "./pages/DocumentSearch";
import About from "./pages/About";
import type { Dispatch, SetStateAction } from "react";

const { Header, Content, Sider } = Layout;

const breadCrumbs: Record<string, string> = {
    dashboard: "Dashboard",
    document: "Document",
    "document/upload": "Upload",
    "document/search": "Search",
    about: "About",
};

interface AppLayoutProps {
    isDarkMode: boolean;
    onToggleDarkMode: Dispatch<SetStateAction<boolean>>;
}

function AppLayout({ isDarkMode, onToggleDarkMode }: AppLayoutProps) {
    const navigate = useNavigate();
    const location = useLocation();
    const path: string[] = location.pathname.split("/").filter((i) => i);

    const extraBreadcrumbItems = path.map((_, index) => {
        const url = `${path.slice(0, index + 1).join("/")}`;
        const label = breadCrumbs[url] || url;

        return {
            title: label,
        };
    });

    const breadcrumbItems: BreadcrumbItemType[] = [
        { title: <Link to="/">Home</Link> },
        ...extraBreadcrumbItems,
    ];
    const { token } = theme.useToken();

    const menuItems: MenuProps["items"] = [
        {
            key: "dashboardMenu",
            icon: <DashboardOutlined />,
            label: "Dashboard",
            onClick: () => navigate("dashboard"),
        },
        {
            key: "documentMenu",
            icon: <FileOutlined />,
            label: "Document",
            children: [
                {
                    key: "documentSearch",
                    label: "Search",
                    icon: <SearchOutlined />,
                    onClick: () => navigate("document/search"),
                },
                {
                    key: "documentUpload",
                    label: "Upload",
                    icon: <UploadOutlined />,
                    onClick: () => navigate("document/upload"),
                },
            ],
        },
        {
            key: "aboutMenu",
            icon: <InfoCircleOutlined />,
            label: "About",
            onClick: () => navigate("about"),
        },
    ];

    const selectedKeys: string[] = (() => {
        if (path.length === 0 || path[0] === "dashboard") {
            return ["dashboardMenu"];
        }

        if (path[0] === "document") {
            if (path[1] === "upload") {
                return ["documentUpload"];
            }
            if (path[1] === "search") {
                return ["documentSearch"];
            }
            return ["documentMenu"];
        }

        if (path[0] === "about") {
            return ["aboutMenu"];
        }

        return ["dashboardMenu"];
    })();

    return (
        <Layout style={{ display: "flex", minHeight: "100vh", width: "100vw" }}>
            <Header
                style={{
                    alignItems: "center",
                    background: isDarkMode
                        ? token.colorPrimaryActive
                        : token.colorPrimary,
                    color: token.colorTextLightSolid,
                    display: "flex",
                    justifyContent: "space-between",
                    padding: "0 24px",
                }}
            >
                <div
                    style={{
                        alignItems: "center",
                        display: "flex",
                        fontWeight: "bold",
                        gap: 8,
                    }}
                >
                    <FileOutlined /> Document Management System
                </div>
                <Switch
                    checked={isDarkMode}
                    onChange={(checked) => onToggleDarkMode(checked)}
                    checkedChildren={<MoonFilled />}
                    unCheckedChildren={<SunFilled />}
                    aria-label="Toggle dark mode"
                />
            </Header>
            <Layout>
                <Sider
                    width={275}
                    style={{ background: token.colorBgContainer }}
                >
                    <Menu
                        mode="inline"
                        defaultOpenKeys={["documentMenu"]}
                        selectedKeys={selectedKeys}
                        style={{ height: "100%", width: "100%" }}
                        rootClassName="app-sider-menu"
                        items={menuItems}
                    />
                </Sider>
                <Layout style={{ padding: "0 24px 24px" }}>
                    <Breadcrumb
                        style={{ margin: "16px 0" }}
                        items={breadcrumbItems}
                    />
                    <Layout
                        style={{
                            padding: "24px 0",
                            background: token.colorBgContainer,
                            borderRadius: token.borderRadiusLG,
                        }}
                    >
                        <Content
                            style={{
                                display: "flex",
                                justifyContent: "center",
                                padding: "0 24px",
                            }}
                        >
                            <Routes>
                                <Route index element={<DocumentDashboard />} />
                                <Route
                                    path="dashboard"
                                    element={<DocumentDashboard />}
                                />
                                <Route
                                    path="document/upload"
                                    element={<DocumentUpload />}
                                />
                                <Route
                                    path="document/search"
                                    element={<DocumentSearch />}
                                />
                                <Route path="about" element={<About />} />
                            </Routes>
                        </Content>
                    </Layout>
                </Layout>
            </Layout>
        </Layout>
    );
}

export default AppLayout;
