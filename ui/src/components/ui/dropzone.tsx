import React, { useCallback, useRef, useState } from "react";

type DropzoneProps = {
    onFiles: (files: File[]) => void;
    accept?: string;
    multiple?: boolean;
    children?: React.ReactNode;
};

export function Dropzone({ onFiles, accept = "application/pdf", multiple = true, children }: DropzoneProps) {
    const [isDragOver, setIsDragOver] = useState(false);
    const inputRef = useRef<HTMLInputElement | null>(null);

    const onDrop = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        setIsDragOver(false);
        const list = e.dataTransfer.files;
        if (list && list.length > 0) onFiles(Array.from(list));
    }, [onFiles]);

    const onDragOver = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        setIsDragOver(true);
    }, []);

    const onDragLeave = useCallback(() => setIsDragOver(false), []);

    const onChoose = useCallback(() => {
        inputRef.current?.click();
    }, []);

    return (
        <div>
            <div
                onDrop={onDrop}
                onDragOver={onDragOver}
                onDragLeave={onDragLeave}
                onClick={onChoose}
                className={`w-full min-h-40 border-2 rounded-md flex items-center justify-center cursor-pointer p-4 transition-colors ${isDragOver ? 'border-primary bg-muted' : 'border-dashed border-border'}`}
            >
                <input
                    ref={inputRef}
                    type="file"
                    accept={accept}
                    multiple={multiple}
                    onChange={(e) => e.target.files && onFiles(Array.from(e.target.files))}
                    className="hidden"
                />
                {children ?? <div className="text-center text-sm text-muted-foreground">Drop a PDF here or click to choose</div>}
            </div>
        </div>
    );
}

export default Dropzone;
