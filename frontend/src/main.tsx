import ReactDOM from 'react-dom/client'
import './index.css'
import {ModeToggle} from "@/components/mode-toogle.tsx";
import {createBrowserRouter, Navigate, RouterProvider} from "react-router-dom";
import {LoginPage} from "@/pages/login-page/login-page.tsx";
import {RequireAuth} from "@/components/auth/RequireAuth.tsx";
import {Protected} from "@/pages/protected.tsx";
import {SwitchDemo} from "@/pages/not-found.tsx";
import {ThemeProvider} from "@/components/theme/theme-provider.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Navigate to="/login" replace/>,
    },
    {
        path: "/login",
        element: <LoginPage/>,
    },
    {
        path: "/dashboard",
        element: (
            <RequireAuth>
                <Protected/>
            </RequireAuth>
        ),
    },
    {
        path: "*",
        element: <SwitchDemo/>,
    },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
    //@TODO I disabled StrictMode because of useEffect runs twice in dev mode
    // <React.StrictMode>
    //   <App />
    // </React.StrictMode>,
    <ThemeProvider>
        <div className="h-full w-full m-0 p-0">
            <RouterProvider router={router}/>
            <div className="absolute right-5 top-5">
                <ModeToggle/>
            </div>
        </div>
    </ThemeProvider>
)
