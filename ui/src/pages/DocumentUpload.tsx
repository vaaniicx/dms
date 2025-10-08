import {
    FileAddOutlined,
    FileSyncOutlined,
    InboxOutlined,
    SmileOutlined,
} from "@ant-design/icons";
import type { UploadProps } from "antd";
import { Button, Steps, Upload, type UploadFile } from "antd";
import Title from "antd/es/typography/Title";
import type { RcFile } from "antd/es/upload/interface";
import { useState } from "react";
import { uploadDocuments } from "../api/services/DocumentService";

const { Dragger } = Upload;

function DocumentUpload() {
    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const [, setUploading] = useState(false);

    const props: UploadProps = {
        multiple: true,
        fileList,
        beforeUpload: (file: RcFile) => {
            setFileList((prev) => [...prev, file]);
            return false;
        },
        onRemove: (file: UploadFile) => {
            setFileList((prev) => prev.filter((f) => f.uid !== file.uid));
        },
    };

    const handleUpload = async () => {
        if (!fileList.length) return;

        try {
            setUploading(true);
            await uploadDocuments(fileList as any);
            setFileList([]);
        } catch (err) {
            console.log(err);
        } finally {
            setUploading(false);
        }
    };

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
                    <Title>Upload Document</Title>
                </div>

                <div style={{ marginBottom: "24px" }}>
                    <Steps
                        items={[
                            {
                                title: "Upload",
                                status: "finish",
                                icon: <FileAddOutlined />,
                            },
                            {
                                title: "OCR & Indexing",
                                status: "wait",
                                icon: <FileSyncOutlined />,
                            },
                            {
                                title: "Finish",
                                status: "wait",
                                icon: <SmileOutlined />,
                            },
                        ]}
                    />
                </div>

                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "stretch",
                        gap: "24px",
                    }}
                >
                    <Dragger {...props}>
                        <p className="ant-upload-drag-icon">
                            <InboxOutlined />
                        </p>
                        <p className="ant-upload-text">
                            Click or drag file to this area to upload
                        </p>
                        <p className="ant-upload-hint">
                            Support for a single or bulk upload. Strictly
                            prohibited from uploading company data or other
                            banned files.
                        </p>
                    </Dragger>

                    <div style={{ marginLeft: "auto" }}>
                        <Button type="primary" onClick={handleUpload}>
                            Upload
                        </Button>
                    </div>
                </div>
            </div>
        </>
    );
}

export default DocumentUpload;
