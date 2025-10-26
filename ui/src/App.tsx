import { ThemeProvider } from "@/components/theme-provider";
import Header from "@/components/header";
import type { ReactNode } from "react";

type AppProps = {
    children?: ReactNode;
};

function App({ children }: AppProps) {
    return (
        <ThemeProvider>
            <Header />
            {children}
        </ThemeProvider>
    );
}

export default App;
