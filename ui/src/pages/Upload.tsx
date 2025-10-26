import { Button } from "@/components/ui/button";
import { Dropzone, DropzoneContent, DropzoneEmptyState } from "@/components/ui/shadcn-io/dropzone";
import { useState } from "react";
import CenteredContainer from "@/components/centered-container";

export default function Upload() {
    const [files, setFiles] = useState<File[] | undefined>();

    const handleDrop = (files: File[]) => {
        console.log(files);
        setFiles(files);
    };

    return (
        <CenteredContainer headerMultiplier={3}>
            <h1 className="scroll-m-20 text-center text-4xl font-extrabold tracking-tight text-balance py-6">
                Upload Documents
            </h1>
            <Dropzone
                accept={{ "application/pdf": [] }}
                maxFiles={10}
                maxSize={1024 * 1024 * 10}
                minSize={1024}
                onDrop={handleDrop}
                onError={console.error}
                src={files}
            >
                <DropzoneEmptyState />
                <DropzoneContent />
            </Dropzone>
            <div className="mt-6 flex justify-end">
                <Button disabled={!files || files.length === 0}>
                    Upload {files ? `${files.length} file${files.length > 1 ? 's' : ''}` : ""}
                </Button>
            </div>
        </CenteredContainer>
    );
}
