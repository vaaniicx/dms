import { Button, Divider, Form, Input, Modal } from "antd";
import Title from "antd/es/typography/Title";
import { useEffect, useState } from "react";
import { deleteDocument, getDocuments } from "../api/services/DocumentService";
import type { DocumentResponse } from "../api/types/DocumentResponse";
import DataTable, { type DataType } from "./components/DocumentDataTable";

function DocumentSearch() {
    const [documents, setDocuments] = useState<DocumentResponse[]>([]);
    const [selectedDocument, setSelectedDocument] = useState<number | null>(null);
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getDocuments()
            .then((data) => setDocuments(data))
            .catch((err) => setError(err))
            .finally(() => setLoading(false))
    }, []);

    const showModal = (id: number) => {
        setSelectedDocument(id);
        setOpen(true);
    }

    const handleOk = async () => {
        if (!selectedDocument) return;

        setConfirmLoading(true);

        try {
            await deleteDocument(selectedDocument);
        } catch (err) {
            console.log(error);
        } finally {
            setConfirmLoading(false);
            setOpen(false);
            setSelectedDocument(null);
        }
    };

    const handleCancel = () => {
        setOpen(false);
        setSelectedDocument(null);
    };

    function mapDocuments(docs: DocumentResponse[]): DataType[] {
        return docs.map((doc) => ({
            key: doc.id,
            name: doc.docTitle ? doc.docTitle : doc.fileName,
            type: doc.fileExtension,
            author: doc.docAuthor,
            tags: [doc.fileType],
            size: `${doc.fileSize} ${doc.fileSizeUnit}`,
            pageCount: doc.docPageCount,
        }));
    }

    return <>
        <div style={{ display: 'flex', flexDirection: 'column', width: '66%' }}>
            <div style={{ marginBottom: '48px' }}>
                <Title>Search Document</Title>
            </div>

            <div style={{ marginBottom: '24px', display: 'flex', flexDirection: 'column' }}>
                <div>
                    <Form name="layout-multiple-horizontal" layout="horizontal" style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap' }}>
                        <Form.Item layout="vertical" label="Name" name="vertical" rules={[{ required: true }]} style={{ width: '30%' }}>
                            <Input />
                        </Form.Item>
                        <Form.Item layout="vertical" label="File Type" name="nameSearch" rules={[{ required: true }]} style={{ width: '30%' }}>
                            <Input />
                        </Form.Item>
                        <Form.Item layout="vertical" label="Author" name="authorSearch" rules={[{ required: true }]} style={{ width: '30%' }}>
                            <Input />
                        </Form.Item>
                    </Form>
                </div>

                <div style={{ marginLeft: 'auto' }}>
                    <Button type="primary">Search</Button>
                </div>
            </div>

            <Divider />

            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'stretch', gap: '24px' }}>
                <Title level={2}>Documents</Title>
                <DataTable
                    data={mapDocuments(documents)}
                    loading={loading}
                    onDelete={showModal}
                />
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

export default DocumentSearch;