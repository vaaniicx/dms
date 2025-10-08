import { Button, Col, Divider, Modal, Row, Statistic } from "antd";
import Title from "antd/es/typography/Title";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { deleteDocument, getDocuments } from "../api/services/DocumentService";
import type { DocumentResponse } from "../api/types/DocumentResponse";
import DataTable, { type DataType } from "./components/DocumentDataTable";

function DocumentDashboard() {
    const navigate = useNavigate();

    const [documents, setDocuments] = useState<DocumentResponse[]>([]);
    const [selectedDocument, setSelectedDocument] = useState<number | null>(
        null,
    );
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getDocuments()
            .then((data) => setDocuments(data))
            .catch((err) => setError(err))
            .finally(() => setLoading(false));
    }, []);

    const showModal = (id: number) => {
        setSelectedDocument(id);
        setOpen(true);
    };

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

    return (
        <>
            <div
                style={{
                    display: "flex",
                    flexDirection: "column",
                    width: "66%",
                }}
            >
                <div style={{ marginBottom: "48px" }}>
                    <Title>Dashboard</Title>
                </div>

                <div style={{ marginBottom: "24px" }}>
                    <Row gutter={16}>
                        <Col span={12}>
                            <Statistic
                                title="Authors"
                                value={
                                    new Set(
                                        documents.map((doc) => doc.docAuthor),
                                    ).size
                                }
                            />
                        </Col>
                        <Col span={12}>
                            <Statistic
                                title="Documents"
                                value={documents.length}
                            />
                            <Button
                                style={{ marginTop: 16 }}
                                type="primary"
                                onClick={() => navigate("/document/upload")}
                            >
                                Upload
                            </Button>
                        </Col>
                    </Row>
                </div>

                <Divider />

                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "stretch",
                        gap: "24px",
                    }}
                >
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
                    okText="Delete"
                    okButtonProps={{ danger: true }}
                    confirmLoading={confirmLoading}
                    onCancel={handleCancel}
                >
                    <p>
                        Are you sure you want to delete this document? This
                        action cannot be undone.
                    </p>
                </Modal>
            </div>
        </>
    );
}

export default DocumentDashboard;
