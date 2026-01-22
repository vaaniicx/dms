import {
    CheckCircleOutlined,
    CloseCircleOutlined,
    CloseOutlined,
    FileAddOutlined,
    FileSyncOutlined,
    InboxOutlined,
} from "@ant-design/icons";
import type { UploadProps } from "antd";
import { Button, List, Upload, type UploadFile, theme } from "antd";
import Title from "antd/es/typography/Title";
import type { RcFile } from "antd/es/upload/interface";
import { useEffect, useMemo, useRef, useState } from "react";
import { getDocument, uploadDocument } from "../api/services/DocumentService";
import type {
    DocumentResponse,
    DocumentStatus,
} from "../api/types/DocumentResponse";

const { Dragger } = Upload;

type UploadRow = {
    uid: string;
    name: string;
    status: DocumentStatus;
    documentId?: number;
};

const POLL_DELAY_MS = 2000;

const sleep = (ms: number) =>
    new Promise((resolve) => {
        setTimeout(resolve, ms);
    });

const hasCompletedProcessing = (document: DocumentResponse): boolean =>
    document.status.some(
        (s) =>
            s.status === "INDEXED" ||
            s.status === "SCANNED" ||
            s.status === "SUMMARIZED",
    );

const deriveUploadStatus = (document: DocumentResponse): DocumentStatus => {
    return hasCompletedProcessing(document) ? "SCANNED" : "UPLOADED";
};

type StatusPalette = {
    idle: string;
    uploading: string;
    success: string;
    error: string;
};

const getStatusMeta = (status: DocumentStatus, palette: StatusPalette) => {
    switch (status) {
        case "FAILED":
            return {
                label: "Failed",
                icon: (
                    <CloseCircleOutlined
                        style={{ color: palette.error, fontSize: 16 }}
                    />
                ),
                color: palette.error,
            };
        case "SCANNED":
            return {
                label: "Done",
                icon: (
                    <CheckCircleOutlined
                        style={{ color: palette.success, fontSize: 16 }}
                    />
                ),
                color: palette.success,
            };
        case "UPLOADED":
            return {
                label: "Uploaded",
                icon: (
                    <FileSyncOutlined
                        style={{ color: palette.uploading, fontSize: 16 }}
                    />
                ),
                color: palette.uploading,
            };
        case "SELECTED":
        default:
            return {
                label: "Selected",
                icon: (
                    <FileAddOutlined
                        style={{ color: palette.idle, fontSize: 16 }}
                    />
                ),
                color: palette.idle,
            };
    }
};

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

function DocumentUpload() {
    const { token } = theme.useToken();
    const palette = useMemo<StatusPalette>(
        () => ({
            uploading: token.colorPrimary,
            success: token.colorSuccess,
            error: token.colorError,
            idle:
                token.colorTextQuaternary ??
                token.colorTextTertiary ??
                token.colorTextSecondary,
        }),
        [token],
    );

    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const [uploads, setUploads] = useState<UploadRow[]>([]);
    const [uploading, setUploading] = useState(false);
    const isMounted = useRef(true);

    useEffect(() => {
        return () => {
            isMounted.current = false;
        };
    }, []);

    const addUploadRow = (file: UploadFile) => {
        setUploads((prev) =>
            prev.some((item) => item.uid === file.uid)
                ? prev
                : [
                      ...prev,
                      {
                          uid: file.uid,
                          name: file.name,
                          status: "SELECTED",
                      },
                  ],
        );
    };

    const updateUploadRow = (uid: string, updates: Partial<UploadRow>) => {
        if (!isMounted.current) return;

        setUploads((prev) =>
            prev.map((item) =>
                item.uid === uid ? { ...item, ...updates } : item,
            ),
        );
    };

    const removeUploadRow = (uid: string) => {
        setUploads((prev) => prev.filter((item) => item.uid !== uid));
        setFileList((prev) => prev.filter((file) => file.uid !== uid));
    };

    const clearAll = () => {
        setUploads([]);
        setFileList([]);
    };

    const pollDocumentStatus = async (
        documentId: number,
        uid: string,
    ): Promise<void> => {
        while (isMounted.current) {
            try {
                const document = await getDocument(documentId);
                const nextStatus = deriveUploadStatus(document);
                updateUploadRow(uid, { status: nextStatus });

                if (nextStatus === "SCANNED" || nextStatus === "FAILED") {
                    return;
                }
            } catch (error) {
                console.error("Failed to poll document status", error);
            }

            await sleep(POLL_DELAY_MS);
        }
    };

    const uploadProps: UploadProps = {
        multiple: true,
        fileList,
        showUploadList: false,
        beforeUpload: (file: RcFile) => {
            const uploadFile = file as UploadFile;
            setFileList((prev) => [...prev, uploadFile]);
            addUploadRow(uploadFile);
            return false;
        },
        onRemove: (file: UploadFile) => {
            if (uploading) {
                return false;
            }

            removeUploadRow(file.uid);
            return true;
        },
    };

    const handleUpload = async () => {
        if (!fileList.length || uploading) {
            return;
        }

        setUploading(true);
        const filesToUpload = [...fileList];

        const processFile = async (file: UploadFile) => {
            try {
                const withOrigin = file as UploadFile & {
                    originFileObj?: RcFile;
                };
                const source =
                    withOrigin.originFileObj ?? (file as unknown as RcFile);

                const documentId = await uploadDocument(
                    source as unknown as File,
                );

                updateUploadRow(file.uid, {
                    status: "UPLOADED",
                    documentId,
                });

                await pollDocumentStatus(documentId, file.uid);
            } catch (error) {
                console.error(error);
                updateUploadRow(file.uid, { status: "FAILED" });
            }
        };

        try {
            await Promise.allSettled(filesToUpload.map(processFile));
        } finally {
            if (isMounted.current) {
                setFileList([]);
                setUploading(false);
            }
        }
    };

    const hasUploads = uploads.length > 0;

    return (
        <section style={pageStyles.container}>
            <header>
                <Title style={{ margin: 0 }}>Document Upload</Title>
            </header>

            <section style={pageStyles.section}>
                <Dragger {...uploadProps} disabled={uploading}>
                    <p className="ant-upload-drag-icon">
                        <InboxOutlined />
                    </p>
                    <p className="ant-upload-text">
                        Click or drag file to this area to upload
                    </p>
                    <p className="ant-upload-hint">
                        Support for a single or bulk upload. Strictly prohibited
                        from uploading company data or other banned files.
                    </p>
                </Dragger>

                {hasUploads && (
                    <section style={pageStyles.section}>
                        <List
                            bordered
                            dataSource={uploads}
                            renderItem={(item) => {
                                const { label, icon, color } = getStatusMeta(
                                    item.status,
                                    palette,
                                );
                                const removeDisabled =
                                    uploading &&
                                    item.status !== "FAILED" &&
                                    item.status !== "SCANNED";

                                return (
                                    <List.Item key={item.uid}>
                                        <div
                                            style={{
                                                display: "flex",
                                                justifyContent: "space-between",
                                                alignItems: "center",
                                                gap: 12,
                                                width: "100%",
                                            }}
                                        >
                                            <span
                                                style={{
                                                    fontWeight: 500,
                                                    wordBreak: "break-word",
                                                }}
                                            >
                                                {item.name}
                                            </span>
                                            <div
                                                style={{
                                                    display: "flex",
                                                    alignItems: "center",
                                                    gap: 12,
                                                }}
                                            >
                                                <span
                                                    style={{
                                                        display: "inline-flex",
                                                        alignItems: "center",
                                                        gap: 6,
                                                        color,
                                                        fontWeight: 500,
                                                    }}
                                                >
                                                    {icon}
                                                    <span>{label}</span>
                                                </span>
                                                <Button
                                                    type="text"
                                                    size="small"
                                                    icon={<CloseOutlined />}
                                                    onClick={() =>
                                                        removeUploadRow(
                                                            item.uid,
                                                        )
                                                    }
                                                    aria-label={`Remove ${item.name}`}
                                                    disabled={removeDisabled}
                                                />
                                            </div>
                                        </div>
                                    </List.Item>
                                );
                            }}
                        />

                        <div
                            style={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                                gap: 12,
                            }}
                        >
                            <Button onClick={clearAll} disabled={uploading}>
                                Clear All
                            </Button>
                            <Button
                                type="primary"
                                onClick={handleUpload}
                                disabled={!fileList.length || uploading}
                                loading={uploading}
                            >
                                Upload
                            </Button>
                        </div>
                    </section>
                )}
            </section>
        </section>
    );
}

export default DocumentUpload;
