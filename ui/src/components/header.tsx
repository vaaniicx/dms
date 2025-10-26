import { FileText } from "lucide-react";
import { ThemeToggle } from "@/components/theme-toggle";
import { Button } from "@/components/ui/button";

export function Header() {
    return (
        <header className="py-4">
            <div className="container flex justify-between mx-auto">
                <div className="flex gap-2">
                    <Button asChild variant="ghost">
                        <a href="#/browse"><FileText /></a>
                    </Button>
                    <Button asChild variant="ghost">
                        <a href="#/browse">Browse</a>
                    </Button>
                    <Button asChild variant="ghost">
                        <a href="#/upload">Upload</a>
                    </Button>
                    <Button asChild variant="ghost">
                        <a href="#/about">About</a>
                    </Button>
                </div>

                <div className="flex">
                    <ThemeToggle variant="ghost" />
                </div>
            </div>
        </header>
    );
}

export default Header;