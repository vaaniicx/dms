"use client";

import { useEffect, useState } from "react";
import type { PropsWithChildren } from "react";

type CenteredContainerProps = PropsWithChildren<{
    headerSelector?: string;
    headerMultiplier?: number; // multiplier applied to header height when subtracting from viewport
    innerClassName?: string;
}>;

export default function CenteredContainer({
    children,
    headerSelector = "header",
    headerMultiplier = 1,
    innerClassName = "max-w-2xl mx-auto w-full",
}: CenteredContainerProps) {
    const [minH, setMinH] = useState<number | undefined>(undefined);

    useEffect(() => {
        function update() {
            const header = document.querySelector(headerSelector) as HTMLElement | null;
            const headerHeight = header ? header.getBoundingClientRect().height : 0;
            const available = window.innerHeight - headerHeight * headerMultiplier;
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
    }, [headerSelector, headerMultiplier]);

    const containerStyle = minH ? { minHeight: `${minH}px` } : undefined;

    return (
        <div className="flex items-center" style={containerStyle}>
            <div className={innerClassName}>{children}</div>
        </div>
    );
}
