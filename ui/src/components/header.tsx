import { FileText } from "lucide-react";
import { ThemeToggle } from "@/components/theme-toggle";
import { Button } from "@/components/ui/button";

export function Header() {
    return (
        <header className="py-4">
            <div className="container flex justify-between mx-auto">
                <div className="flex gap-2">
                    <Button asChild variant="ghost">
                        <a href="/browse"><FileText /></a>
                    </Button>
                    <Button asChild variant="ghost">
                        <a href="/browse">Browse</a>
                    </Button>
                    <Button asChild variant="ghost">
                        <a href="/upload">Upload</a>
                    </Button>
                    <Button asChild variant="ghost">
                        <a href="/about">About</a>
                    </Button>
                </div>

                <div className="flex items-center gap-2">
                    <Button asChild variant="ghost" size="icon">
                        <a href="https://github.com/vaaniicx/dms" target="_blank" rel="noopener noreferrer" aria-label="Open GitHub repository">
                            <svg viewBox="0 0 24 24" className="h-[1.2rem] w-[1.2rem]" fill="currentColor" aria-hidden="true">
                                <path d="M12 .297c-6.63 0-12 5.373-12 12 0 5.303 3.438 9.8 8.205 11.387.6.113.82-.258.82-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.757-1.333-1.757-1.089-.744.084-.729.084-.729 1.205.084 1.838 1.236 1.838 1.236 1.07 1.834 2.809 1.304 3.495.998.108-.776.418-1.305.762-1.605-2.665-.305-5.466-1.332-5.466-5.93 0-1.31.468-2.381 1.235-3.221-.135-.303-.54-1.523.105-3.176 0 0 1.005-.322 3.3 1.23.96-.267 1.98-.399 3-.405 1.02.006 2.04.138 3 .405 2.28-1.552 3.285-1.23 3.285-1.23.645 1.653.24 2.873.12 3.176.765.84 1.23 1.911 1.23 3.221 0 4.61-2.805 5.625-5.475 5.922.429.369.81 1.096.81 2.214v3.293c0 .319.21.694.825.576C20.565 22.092 24 17.592 24 12.297 24 5.67 18.627.297 12 .297z" />
                            </svg>
                            <span className="sr-only">GitHub</span>
                        </a>
                    </Button>
                    <ThemeToggle variant="ghost" />
                </div>
            </div>
        </header>
    );
}

export default Header;