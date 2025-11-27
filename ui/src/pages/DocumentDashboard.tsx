import { Button, Col, Divider, Modal, Row, Statistic } from "antd";
import Title from "antd/es/typography/Title";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { deleteDocument, getDocuments } from "../api/services/DocumentService";
import type { DocumentResponse } from "../api/types/DocumentResponse";
import DataTable, { type DataType } from "./components/DocumentDataTable";

const pageStyles = {
    container: {
        display: "flex",
        flexDirection: "column" as const,
        width: "100%",
        gap: 24,
    },
    section: {
        display: "flex",
        flexDirection: "column" as const,
        gap: 16,
        width: "100%",
    },
};

function DocumentDashboard() {
    const navigate = useNavigate();

    const [documents, setDocuments] = useState<DocumentResponse[]>([]);
    const [selectedDocument, setSelectedDocument] = useState<number | null>(
        null,
    );
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [, setError] = useState<string | null>(null);
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
            setDocuments((prev) => prev.filter((doc) => doc.id !== selectedDocument));
        } catch (err) {
            console.error(err);
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
            name: doc.docTitle || doc.fileName || "-",
            type: doc.fileExtension || "-",
            author: doc.docAuthor || "",
            uploadedAt: doc.insertedAt
                ? new Date(doc.insertedAt).toLocaleString()
                : "-",
            sizeRaw: doc.fileSize ?? 0,
            size:
                doc.fileSize && doc.fileSizeUnit
                    ? `${doc.fileSize} ${doc.fileSizeUnit}`
                    : "-",
            pageCount: doc.docPageCount ?? 0,
            uploaded: Boolean(doc.uploaded),
            scanned: Boolean(doc.scanned),
            summarized: Boolean(doc.summarized),
            indexed: Boolean(doc.indexed),
            summary: doc.summary ?? "",
        }));
    }

    return (
        <section style={pageStyles.container}>
            <header>
                <Title style={{ margin: 0 }}>Dashboard</Title>
            </header>

            <section style={pageStyles.section}>
                <Row gutter={16}>
                    <Col span={12}>
                        <Statistic
                            title="Authors"
                            value={
                                new Set(documents.map((doc) => doc.docAuthor)).size
                            }
                        />
                    </Col>
                    <Col span={12}>
                        <Statistic title="Documents" value={documents.length} />
                        <Button
                            style={{ marginTop: 16 }}
                            type="primary"
                            onClick={() => navigate("/document/upload")}
                        >
                            Upload
                        </Button>
                    </Col>
                </Row>
            </section>

            <Divider style={{ margin: 0 }} />

            <section style={pageStyles.section}>
                <Title level={2} style={{ margin: 0 }}>
                    Documents
                </Title>
                <DataTable
                    data={mapDocuments(documents)}
                    loading={loading}
                    onDelete={showModal}
                />
            </section>

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
                    Are you sure you want to delete this document? This action
                    cannot be undone.
                </p>
            </Modal>
        </section>
    );
}

export default DocumentDashboard;
