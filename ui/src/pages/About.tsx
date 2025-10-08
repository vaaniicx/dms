import { Layout } from "antd";

function About() {
    return (
        <Layout
            style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "transparent",
                padding: "48px 24px",
            }}
        >
            <div
                style={{
                    maxWidth: 720,
                    textAlign: "center",
                    display: "flex",
                    flexDirection: "column",
                    gap: 16,
                }}
            >
                <h1 style={{ margin: 0 }}>Document Management System</h1>
                <p style={{ margin: 0 }}>
                    Created by Group A, Winter-Semester 2025.
                </p>
            </div>
        </Layout>
    );
}

export default About;
