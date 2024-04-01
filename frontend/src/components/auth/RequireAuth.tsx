import {Navigate, Outlet} from "react-router-dom";
import {useCheckAuthStatus} from "@/hooks/use-login-status.tsx";
import {FC, ReactNode} from "react";
import {SkeletonDemo} from "@/components/loading.tsx";

interface RequireAuthProps {
    children?: ReactNode; // Definiuje, że children może być dowolnym elementem Reacta
}

export const RequireAuth: FC<RequireAuthProps> = ({children}) => {
    const userIsLogged = useCheckAuthStatus(); // Your hook to get login status


    // Render a loading skeleton if the auth status is still being determined
    if (userIsLogged === null) {
        return <SkeletonDemo/>;
    }

    // If not logged in, redirect to the login page
    if (!userIsLogged) {
        return <Navigate to="/login" replace/>;
    }

    // Render children if provided, otherwise render the Outlet (for nested routes)
    return children ? <>{children}</> : <Outlet/>;
};