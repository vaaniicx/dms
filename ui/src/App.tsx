import { ThemeProvider } from "@/components/theme-provider";
import Header from "@/components/header";
import About from "./pages/About";
import Upload from "./pages/Upload";
import type { ReactNode } from "react";
import { useEffect, useState } from "react";

type AppProps = {
    children?: ReactNode;
};

function App({ children }: AppProps) {
    const [route, setRoute] = useState<string>(
        typeof window !== "undefined" ? window.location.pathname : "/browse"
    );

    useEffect(() => {
        const onPop = () => setRoute(window.location.pathname || "/browse");
        window.addEventListener("popstate", onPop);
        return () => window.removeEventListener("popstate", onPop);
    }, []);

    function renderRoute() {
        switch (route) {
            case "/about":
                return <About />;
            case "/upload":
                return <Upload />;
            default:
                return children ?? null;
        }
    }

    return (
        <ThemeProvider>
            <Header />
            {renderRoute()}
        </ThemeProvider>
    );
}

export default App;
