import { Space, Table, Tag, type TableProps } from "antd";

export interface DataType {
    key: number;
    name: string;
    type: string;
    author: string;
    status: string;
    uploadedAt: string;
    pageCount: number;
    size: string;
    sizeRaw?: number;
}

interface DataTableProps {
    data: DataType[];
    loading?: boolean;
    onDelete?: (id: number) => void;
    onPreview?: (id: number) => void;
}

export default function DataTable(props: DataTableProps) {
    const { data, loading = false, onDelete, onPreview } = props;
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
            title: "Status",
            dataIndex: "status",
            key: "status",
            sorter: (a: DataType, b: DataType) =>
                (a?.status ?? "").localeCompare(b?.status ?? "", undefined, {
                    sensitivity: "base",
                }),
            render: (status: string | undefined) => {
                const normalized = status?.toUpperCase?.() ?? "-";
                const colorMap: Record<string, string> = {
                    UPLOADED: "gold",
                    SCANNED: "green",
                    INDEXED: "blue",
                    FAILED: "red",
                    SELECTED: "default",
                };
                const color = colorMap[normalized] || "default";
                return (
                    <Tag color={color} key={normalized}>
                        {normalized || "-"}
                    </Tag>
                );
            },
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
