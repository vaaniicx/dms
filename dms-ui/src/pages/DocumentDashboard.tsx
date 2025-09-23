import Title from "antd/es/typography/Title";
import { Button, Col, Row, Statistic, Space, Table, Tag, Divider, Modal } from 'antd';
import type { TableProps } from 'antd';
import { useNavigate } from "react-router";
import { useState } from "react";

interface DataType {
    key: string;
    name: string;
    type: string;
    author: string;
    tags: string[];
    size: string;
}

function DocumentDashboard() {
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);

    const showModal = () => {
        setOpen(true);
    }

    const handleOk = () => {
        setConfirmLoading(true);
        setTimeout(() => {
            setOpen(false);
            setConfirmLoading(false);
        }, 2000);
    };

    const handleCancel = () => {
        setOpen(false);
    };

    const columns: TableProps<DataType>['columns'] = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            defaultSortOrder: 'ascend',
            sorter: (a, b) => a.name.localeCompare(b.name),
            render: (text) => <a>{text}</a>,
        },
        {
            title: 'Type',
            dataIndex: 'type',
            key: 'type',
            onFilter: (value, record) => record.type.indexOf(value as string) === 0,
            filters: [
                {
                    text: 'PDF',
                    value: 'pdf',
                },
                {
                    text: 'Text',
                    value: 'txt',
                },
            ],
        },
        {
            title: 'Author',
            dataIndex: 'author',
            key: 'author',
            sorter: (a, b) => a.author.localeCompare(b.author),
            render: (_, record) => (
                <Tag color={'blue'} key={record.author}>
                    {record.author.toUpperCase()}
                </Tag>
            ),
        },
        {
            title: 'Tags',
            key: 'tags',
            dataIndex: 'tags',
            render: (_, { tags }) => (
                <>
                    {tags.map((tag) => {
                        let color = tag.length > 5 ? 'geekblue' : 'green';
                        return (
                            <Tag color={color} key={tag}>
                                {tag.toUpperCase()}
                            </Tag>
                        );
                    })}
                </>
            ),
        },
        {
            title: 'Size',
            dataIndex: 'size',
            key: 'size',
        },
        {
            title: 'Action',
            key: 'action',
            render: () => (
                <Space size="middle">
                    <a>Edit</a>
                    <a onClick={showModal} style={{ color: 'red' }}>Delete</a>
                </Space>
            ),
        },
    ];

    const data: DataType[] = [
        {
            key: '1',
            name: "A - Build a Backend with Java Spring Boot 3",
            type: 'pdf',
            author: 'Alex Mustermann',
            tags: ['java', 'spring boot'],
            size: '64 MB',
        },
        {
            key: '2',
            name: "B - Build a Backend with Java Spring Boot 3",
            type: 'pdf',
            author: 'Bernd Mustermann',
            tags: ['java', 'spring boot'],
            size: '64 MB',
        },
        {
            key: '3',
            name: "C - Build a Backend with Java Spring Boot 3",
            type: 'pdf',
            author: 'Cedric Mustermann',
            tags: ['java', 'spring boot'],
            size: '64 MB',
        },
    ];

    return <>
        <div style={{ display: 'flex', flexDirection: 'column', width: '66%' }}>
            <div style={{ marginBottom: '48px' }}>
                <Title>Dashboard</Title>
            </div>

            <div style={{ marginBottom: '24px' }}>
                <Row gutter={16}>
                    <Col span={12}>
                        <Statistic title="Authors" value={1} />
                    </Col>
                    <Col span={12}>
                        <Statistic title="Total Documents" value={1} />
                        <Button style={{ marginTop: 16 }} type="primary" onClick={() => navigate('/document/upload')}>
                            Upload
                        </Button>
                    </Col>
                </Row>
            </div>

            <Divider />

            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'stretch', gap: '24px' }}>
                <Title level={2}>All Documents</Title>
                <Table<DataType> columns={columns} dataSource={data} />
            </div>

            <Modal
                title="Delete Document?"
                centered
                open={open}
                onOk={handleOk}
                okType="primary"
                okText='Delete'
                okButtonProps={{ danger: true }}
                confirmLoading={confirmLoading}
                onCancel={handleCancel}
            >
                <p>Are you sure you want to delete this document? This action cannot be undone.</p>
            </Modal>
        </div>
    </>;
}

export default DocumentDashboard;