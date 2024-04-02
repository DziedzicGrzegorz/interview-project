import {Navigate, Outlet} from "react-router-dom";
import {useCheckAuthStatus} from "@/hooks/use-login-status.tsx";
import {FC, ReactNode} from "react";
import {SkeletonDemo} from "@/components/loading.tsx";

interface RequireAuthProps {
    children?: ReactNode;
}

export const RequireAuth: FC<RequireAuthProps> = ({children}) => {
    const userIsLogged = useCheckAuthStatus();


    if (userIsLogged === null) {
        return <SkeletonDemo/>;
    }


    if (!userIsLogged) {
        return <Navigate to="/login" replace/>;
    }

    return children ? <>{children}</> : <Outlet/>;
};