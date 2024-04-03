import ReactDOM from 'react-dom/client'
import './index.css'
import {ModeToggle} from "@/components/mode-toogle.tsx";
import {createBrowserRouter, Navigate, RouterProvider} from "react-router-dom";
import {LoginPage} from "@/pages/login-page/login-page.tsx";
import {RequireAuth} from "@/components/auth/RequireAuth.tsx";
import {PageNotFound} from "@/pages/not-found.tsx";
import {ThemeProvider} from "@/components/theme/theme-provider.tsx";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Dashboard} from "@/pages/dashboard/dashboard.tsx";
import React from 'react';
import {Logout} from "@/components/logout/logout.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Navigate to="/login" replace/>,
    },
    {
        path: "/login",
        element: (
            <>
                <LoginPage/>
                <Toaster/>
            </>
        )
    },
    {
        path: "/dashboard",
        element: (
            <RequireAuth>
                <Dashboard/>
                <Toaster/>
                <div className="absolute left-5 top-5">
                    <Logout/>
                </div>
            </RequireAuth>
        ),
    },
    {
        path: "*",
        element: <PageNotFound/>,
    },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <ThemeProvider>
            <div className="h-full w-full m-0 p-0">
                <RouterProvider router={router}/>
                <div className="absolute right-5 top-5">
                    <ModeToggle/>
                </div>
            </div>
        </ThemeProvider>
    </React.StrictMode>,
)
