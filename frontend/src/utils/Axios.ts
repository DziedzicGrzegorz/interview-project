import axios from 'axios';


axios.defaults.baseURL = 'http://localhost:8080';

axios.defaults.withCredentials = true;
axios.defaults.maxRedirects = 0;

axios.interceptors.response.use(
    response => {
        //set state to true
        return response;
    },
    error => {
        // const originalRequest = error.config;

        if (error?.response?.status === 401 || error?.response?.status === 403) {
            //@TODO clear cookies


        }
        return Promise.reject(error);
    }
);

export const Axios = axios;
