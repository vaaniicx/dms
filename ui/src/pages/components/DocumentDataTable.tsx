import { Space, Table, Tag, type TableProps } from "antd";

export interface DataType {
    key: number;
    name: string;
    type: string;
    author: string;
    uploadedAt: string;
    pageCount: number;
    size: string;
    sizeRaw?: number;
    uploaded: boolean;
    scanned: boolean;
    summarized: boolean;
    indexed: boolean;
    summary?: string | null;
}

interface DataTableProps {
    data: DataType[];
    loading?: boolean;
    onDelete?: (id: number) => void;
    onPreview?: (id: number) => void;
}

export default function DataTable(props: DataTableProps) {
    const { data, loading = false, onDelete, onPreview } = props;
    type StageKey = "uploaded" | "scanned" | "summarized" | "indexed";
    const pipelineStages: Array<{ key: StageKey; label: string; color: string }> = [
        { key: "uploaded", label: "Uploaded", color: "gold" },
        { key: "scanned", label: "Scanned", color: "cyan" },
        { key: "summarized", label: "Summarized", color: "green" },
        { key: "indexed", label: "Indexed", color: "blue" },
    ];
    const columns: TableProps<DataType>["columns"] = [
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
            defaultSortOrder: "ascend",
            sorter: (a: DataType, b: DataType) =>
                (a?.name ?? "").localeCompare(b?.name ?? "", undefined, {
                    sensitivity: "base",
                }),
            render: (text: string | null | undefined, record: DataType) => (
                <a onClick={() => onPreview?.(record.key)}>{text || "-"}</a>
            ),
        },
        {
            title: "Type",
            dataIndex: "type",
            key: "type",
            render: (_: unknown, { type }: DataType) => (
                <Tag color={"geekblue"} key={type}>
                    {type ? type.toUpperCase() : "-"}
                </Tag>
            ),
        },
        {
            title: "Summary",
            dataIndex: "summary",
            key: "summary",
            ellipsis: true,
            render: (summary: string | null | undefined) =>
                summary && summary.trim().length > 0 ? summary : "-",
        },
        {
            title: "Status",
            key: "status",
            render: (_: unknown, record: DataType) => (
                <Space size={[4, 4]} wrap>
                    {pipelineStages.map((stage) => (
                        <Tag
                            key={stage.key}
                            color={record[stage.key] ? stage.color : "default"}
                        >
                            {stage.label}
                        </Tag>
                    ))}
                </Space>
            ),
        },
        {
            title: "Date Uploaded",
            dataIndex: "uploadedAt",
            key: "uploadedAt",
            sorter: (a: DataType, b: DataType) =>
                (a?.uploadedAt ?? "").localeCompare(
                    b?.uploadedAt ?? "",
                    undefined,
                    { sensitivity: "base" },
                ),
            render: (value: string | undefined) => value || "-",
        },
        {
            title: "Author",
            dataIndex: "author",
            key: "author",
            sorter: (a: DataType, b: DataType) =>
                (a?.author ?? "").localeCompare(b?.author ?? "", undefined, {
                    sensitivity: "base",
                }),
            render: (_: unknown, record: DataType) => (
                <Tag color={"blue"} key={record.author}>
                    {record.author ? record.author.toUpperCase() : "-"}
                </Tag>
            ),
        },
        {
            title: "Pages",
            dataIndex: "pageCount",
            key: "pageCount",
            sorter: (a: DataType, b: DataType) => a?.pageCount - b?.pageCount,
            render: (n: number | undefined) => (n ?? 0).toLocaleString(),
            width: 100,
        },
        {
            title: "Size",
            dataIndex: "size",
            key: "size",
            sorter: (a: DataType, b: DataType) => (a?.sizeRaw ?? 0) - (b?.sizeRaw ?? 0),
        },
        {
            title: "Action",
            key: "action",
            render: (_: unknown, record: DataType) => (
                <Space size="middle">
                    {onDelete && (
                        <a
                            style={{ color: "red" }}
                            onClick={() => onDelete(record.key)}
                        >
                            Delete
                        </a>
                    )}
                </Space>
            ),
        },
    ];

    return (
        <Table<DataType>
            columns={columns}
            dataSource={data}
            loading={loading}
            size="small"
            pagination={{ pageSize: 5 }}
        />
    );
}
