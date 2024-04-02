import {Label} from "@/components/ui/label"
import {Switch} from "@/components/ui/switch"
import {useNavigate} from "react-router-dom";

export function PageNotFound() {
    const navigate = useNavigate();

    const handleToggle = () => {
        setTimeout(() => {
            navigate('/login');
        }, 500);
    };

    return (
        <div className="h-full flex flex-col items-center space-y-6 mt-[10%]">
            <div className="text-6xl font-bold">404</div>
            <div className="text-xm italic text-gray-600">Lost in the skies, aren't we?</div>
            <div className="flex items-center space-x-2">
                <div className="mt-2">
                    <Switch id="airplane-mode" onClick={handleToggle}/>
                </div>
                <Label className="text-2xl" htmlFor="airplane-mode">Back Home</Label>
            </div>
        </div>
    );
}