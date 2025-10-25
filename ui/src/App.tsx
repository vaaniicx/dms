import { ThemeProvider } from "@/components/theme-provider";
import { ThemeToggle } from "@/components/theme-toggle";
import type { ReactNode } from "react";

type AppProps = {
    children?: ReactNode;
};

function App({ children }: AppProps) {
    return (
        <ThemeProvider>
            <ThemeToggle />
            {children}
        </ThemeProvider>
    );
}

export default App;
