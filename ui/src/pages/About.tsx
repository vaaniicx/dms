import CenteredContainer from "@/components/centered-container";

export default function About() {
    return (
        <CenteredContainer headerMultiplier={3}>
            <h1 className="scroll-m-20 text-center text-4xl font-extrabold tracking-tight text-balance">
                Document Management System
            </h1>
            <p className="text-muted-foreground text-xl text-justify pt-6 pb-12">
                Document Management System (DMS) is an application for document archiving, text extraction, and
                intelligent search. It automates document storage, OCR scanning, AI summarization, and full-text
                indexing using a distributed microservice setup.
            </p>
            <p className="leading-7 text-center">Developed by Group-A, Winter Semester 2025</p>
        </CenteredContainer>
    );
}
