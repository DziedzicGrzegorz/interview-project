import {useNavigate} from "react-router-dom";
import {LogOut} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {Axios} from "@/utils/Axios.ts";
import {toast} from "@/components/ui/use-toast.ts";


export function Logout() {
    const navigate = useNavigate();
    //
    const logout = async () => {
        try {
            await Axios.get('/auth/logout');
            // After successfully logging out, navigate to the login page.
            navigate('/login');
        } catch (error) {
            console.error('Logout failed:', error);
            toast(
                {
                    variant: "destructive",
                    title: "Logout failed",
                    description: "Failed to logout"
                }
            )
        }
    };
    return (
        <Button variant="outline" size="icon">
            <LogOut size={24} strokeWidth={1} onClick={logout}/>
        </Button>
    );
}