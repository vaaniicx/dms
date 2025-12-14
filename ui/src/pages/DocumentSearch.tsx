import { Button, Divider, Form, Input, Modal, Radio } from "antd";
import Title from "antd/es/typography/Title";
import { useEffect, useState } from "react";
import {
    deleteDocument,
    getDocuments,
    searchDocuments,
} from "../api/services/DocumentService";
import type { SearchScope } from "../api/services/DocumentService";
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
    formRow: {
        display: "flex",
        justifyContent: "space-between",
        gap: 16,
        flexWrap: "wrap" as const,
        width: "100%",
    },
    formItem: {
        flex: "1 1 200px",
        minWidth: 180,
    },
};

function DocumentSearch() {
    const [documents, setDocuments] = useState<DocumentResponse[]>([]);
    const [selectedDocument, setSelectedDocument] = useState<number | null>(
        null,
    );
    const [open, setOpen] = useState(false);
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    const [query, setQuery] = useState("");
    const [scope, setScope] = useState<SearchScope>("content");

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

    const handleSearch = async () => {
        setLoading(true);
        try {
            if (!query.trim()) {
                const all = await getDocuments();
                setDocuments(all);
            } else {
                const results = await searchDocuments(query.trim(), scope);
                setDocuments(results);
            }
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
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
                <Title style={{ margin: 0 }}>Document Search</Title>
            </header>

            <section style={pageStyles.section}>
                <Form
                    layout="vertical"
                    style={{ width: "100%" }}
                    onFinish={handleSearch}
                >
                    <div style={pageStyles.formRow}>
                        <Form.Item
                            label="Search text"
                            style={pageStyles.formItem}
                        >
                            <Input
                                placeholder="Search document content"
                                allowClear
                                value={query}
                                onChange={(e) => setQuery(e.target.value)}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Search in"
                            style={pageStyles.formItem}
                        >
                            <Radio.Group
                                value={scope}
                                onChange={(e) =>
                                    setScope(e.target.value as SearchScope)
                                }
                                optionType="button"
                                buttonStyle="solid"
                            >
                                <Radio.Button value="content">
                                    Content
                                </Radio.Button>
                                <Radio.Button value="name">
                                    Filename / Title
                                </Radio.Button>
                                <Radio.Button value="all">All</Radio.Button>
                            </Radio.Group>
                        </Form.Item>
                    </div>
                    <div
                        style={{
                            display: "flex",
                            justifyContent: "flex-end",
                        }}
                    >
                        <Button type="primary" htmlType="submit">
                            Search
                        </Button>
                    </div>
                </Form>
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
                    onPreview={(id: number) => {
                        window.location.href = `/api/v1/documents/${id}/download?inline=true`;
                    }}
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

export default DocumentSearch;
