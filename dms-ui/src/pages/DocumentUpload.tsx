import { FileAddOutlined, FileSyncOutlined, InboxOutlined, SmileOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { Button, message, Steps, Upload } from 'antd';
import Title from 'antd/es/typography/Title';

const { Dragger } = Upload;

const props: UploadProps = {
    name: 'file',
    multiple: true,
    action: 'https://660d2bd96ddfa2943b33731c.mockapi.io/api/upload',
    onChange(info) {
        const { status } = info.file;
        if (status !== 'uploading') {
            console.log(info.file, info.fileList);
        }
        if (status === 'done') {
            message.success(`${info.file.name} file uploaded successfully.`);
        } else if (status === 'error') {
            message.error(`${info.file.name} file upload failed.`);
        }
    },
    onDrop(e) {
        console.log('Dropped files', e.dataTransfer.files);
    },
};

function DocumentUpload() {


    return <>
        <div style={{ display: 'flex', flexDirection: 'column', width: '66%' }}>
            <div style={{ marginBottom: '48px' }}>
                <Title>Dokumente hochladen</Title>
            </div>

            <div style={{ marginBottom: '24px' }}>
                <Steps
                    items={[
                        {
                            title: 'Dokumente hochladen',
                            status: 'finish',
                            icon: <FileAddOutlined />,
                        },
                        {
                            title: 'OCR & Indexierung',
                            status: 'wait',
                            icon: <FileSyncOutlined />,
                        },
                        {
                            title: 'Upload abgeschlossen',
                            status: 'wait',
                            icon: <SmileOutlined />,
                        },
                    ]}
                />
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'stretch', gap: '24px' }}>
                <Dragger {...props}>
                    <p className="ant-upload-drag-icon">
                        <InboxOutlined />
                    </p>
                    <p className="ant-upload-text">Click or drag file to this area to upload</p>
                    <p className="ant-upload-hint">
                        Support for a single or bulk upload. Strictly prohibited from uploading company data or other
                        banned files.
                    </p>
                </Dragger>

                <div style={{ marginLeft: 'auto' }}>
                    <Button type="primary">Dokumente hochladen</Button>
                </div>
            </div>
        </div>
    </>;
}

export default DocumentUpload;