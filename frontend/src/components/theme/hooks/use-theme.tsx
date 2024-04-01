import {useContext} from "react";
import {ThemeProviderContext} from "@/components/theme/theme-provider.tsx";

export const useTheme = () => {
    const context = useContext(ThemeProviderContext);

    if (!context) {
        throw new Error("useTheme must be used within a ThemeProvider");
    }

    return context;
};
