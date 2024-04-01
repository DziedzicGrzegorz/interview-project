import {useState, useEffect} from 'react';
import {Axios} from "@/utils/Axios.ts";

export const useCheckAuthStatus = () => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

    useEffect(() => {
        const checkAuthStatus = async () => {
            try {
                // Replace '/private-route' with the endpoint you use to verify authentication
                await Axios.get('/private');
                setIsAuthenticated(true);
            } catch (error) {

                const axiosError = error as {
                    response?: {
                        status: number
                    }
                };

                if (axiosError.response && (axiosError.response.status === 401 || axiosError.response.status === 403)) {
                    setIsAuthenticated(false);
                }
            }
        };

        checkAuthStatus();
    }, []);

    return isAuthenticated;
};

