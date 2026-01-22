import { Button, Card, Col, Divider, Modal, Row, Statistic } from "antd";
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
            setDocuments((prev) =>
                prev.filter((doc) => doc.id !== selectedDocument),
            );
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

    const openDocument = (id: number) => {
        window.location.href = `/api/v1/documents/${id}/download?inline=true`;
    };

    function mapDocuments(docs: DocumentResponse[]): DataType[] {
        return docs.map((doc) => ({
            key: doc.id,
            name: doc.title || doc.file.name || "-",
            type: doc.file.type || "-",
            author: doc.author || "",
            uploadedAt: doc.file.creationDate
                ? new Date(doc.file.creationDate).toLocaleString()
                : "-",
            sizeRaw: doc.file.size ?? 0,
            size: doc.file.size
                ? `${(doc.file.size / 1024).toFixed(2)} KB`
                : "-",
            pageCount: doc.file.pageCount ?? 0,
            uploaded: doc.status.some((s) => s.status === "UPLOADED"),
            scanned: doc.status.some((s) => s.status === "SCANNED"),
            summarized: doc.status.some((s) => s.status === "SUMMARIZED"),
            indexed: doc.status.some((s) => s.status === "INDEXED"),
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
                                new Set(documents.map((doc) => doc.author)).size
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

                <section
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: 16,
                    }}
                >
                    <Title level={4} style={{ margin: 0 }}>
                        Highlights
                    </Title>
                    <Row gutter={[16, 16]}>
                        {documents.length === 0 && !loading && (
                            <Col span={24}>
                                <Card
                                    bordered
                                    style={{ background: "transparent" }}
                                >
                                    No documents available yet.
                                </Card>
                            </Col>
                        )}
                        {documents.map((doc) => (
                            <Col xs={24} sm={12} lg={8} key={doc.id}>
                                <Card
                                    title={
                                        doc.title || doc.file.name || "Untitled"
                                    }
                                    hoverable
                                    onClick={() => openDocument(doc.id)}
                                    style={{ height: "100%" }}
                                    bodyStyle={{
                                        minHeight: 120,
                                        display: "flex",
                                    }}
                                >
                                    <p style={{ margin: 0 }}>
                                        {doc.summary?.trim() ||
                                            "No summary available."}
                                    </p>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </section>

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
