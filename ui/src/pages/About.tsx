import { useEffect, useState } from "react";

export default function About() {
    const [minH, setMinH] = useState<number | undefined>(undefined);

    useEffect(() => {
        const headerSelector = "header"; 

        function update() {
            const header = document.querySelector(headerSelector) as HTMLElement | null;
            const headerHeight = header ? header.getBoundingClientRect().height : 0;
            const available = window.innerHeight - headerHeight;
            setMinH(available > 0 ? Math.floor(available) : 0);
        }

        update();
        window.addEventListener("resize", update);

        const headerEl = document.querySelector(headerSelector);
        const observer = headerEl ? new MutationObserver(() => update()) : null;
        if (observer && headerEl) {
            observer.observe(headerEl, { attributes: true, childList: true, subtree: true });
        }

        return () => {
            window.removeEventListener("resize", update);
            if (observer) observer.disconnect();
        };
    }, []);

    const containerStyle = minH ? { minHeight: `${minH}px` } : undefined;

    return (
        <div className="flex items-center" style={containerStyle}>
            <div className="max-w-2xl mx-auto w-full">
                <h1 className="scroll-m-20 text-center text-4xl font-extrabold tracking-tight text-balance">
                    Document Management System
                </h1>
                <p className="text-muted-foreground text-xl text-justify pt-6 pb-12">
                    Document Management System (DMS) is an application for document archiving, text extraction, and
                    intelligent search. It automates document storage, OCR scanning, AI summarization, and full-text
                    indexing using a distributed microservice setup.
                </p>
                <p className="leading-7 text-center">Developed by Group-A, Winter Semester 2025</p>
            </div>
        </div>
    );
}
