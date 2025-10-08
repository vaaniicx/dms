import { Space, Table, Tag, type TableProps } from "antd";

export interface DataType {
    key: number;
    name: string;
    type: string;
    author: string;
    pageCount: number;
    size: string;
}

interface DataTableProps {
    data: DataType[];
    loading?: boolean;
    onDelete?: (id: number) => void;
}

export default function DataTable({
    data,
    loading = false,
    onDelete,
}: DataTableProps) {
    const columns: TableProps<DataType>["columns"] = [
        {
            title: "Name",
            dataIndex: "name",
            key: "name",
            defaultSortOrder: "ascend",
            sorter: (a, b) =>
                (a?.name ?? "").localeCompare(b?.name ?? "", undefined, {
                    sensitivity: "base",
                }),
            render: (text: string | null | undefined) => <a>{text || "-"}</a>,
        },
        {
            title: "Type",
            dataIndex: "type",
            key: "type",
            render: (_, { type }) => (
                <>
                    <Tag color={"geekblue"} key={type}>
                        {type ? type.toUpperCase() : "-"}
                    </Tag>
                </>
            ),
        },
        {
            title: "Author",
            dataIndex: "author",
            key: "author",
            sorter: (a, b) =>
                (a?.author ?? "").localeCompare(b?.author ?? "", undefined, {
                    sensitivity: "base",
                }),
            render: (_, record) => (
                <Tag color={"blue"} key={record.author}>
                    {record.author ? record.author.toUpperCase() : "-"}
                </Tag>
            ),
        },
        {
            title: "Pages",
            dataIndex: "pageCount",
            key: "pageCount",
            sorter: (a, b) => a?.pageCount - b?.pageCount,
            render: (n: number) => n.toLocaleString(),
            width: 100,
        },
        {
            title: "Size",
            dataIndex: "size",
            key: "size",
        },
        {
            title: "Action",
            key: "action",
            render: (_, record) => (
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
