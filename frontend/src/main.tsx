import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {ThemeProvider} from "@/components/theme-provider.tsx";
import {ModeToggle} from "@/components/mode-toogle.tsx";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import LoginPage from "@/pages/login-page/login-page.tsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <LoginPage/>,
    },
    {
        path: "/dashboard",
        element: <App/>,
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
