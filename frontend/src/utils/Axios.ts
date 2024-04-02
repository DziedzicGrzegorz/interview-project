import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080';

axios.defaults.withCredentials = true;
axios.defaults.maxRedirects = 0;

axios.interceptors.response.use(
    response => {
        return response;
    },
    error => {

        if (error?.response?.status === 401 || error?.response?.status === 403) {
            console.error('Unauthorized')
        }
        return Promise.reject(error);
    }
);

export const Axios = axios;
