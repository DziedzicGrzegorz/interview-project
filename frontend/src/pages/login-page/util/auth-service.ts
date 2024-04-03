import axios from "axios";

const API_URL = "http://localhost:8080/";

const register = (username: string, email: string, password: string) => {
    return axios.post(API_URL + "auth/register", {
        username,
        email,
        password,
    });
};

const login = (username: string, password: string) => {
    return axios
        .post(API_URL + "auth/login", {
            username,
            password,
        })
        .then((response) => {
            if (response.data.username) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }

            return response.data;
        });
};
const privateRoute = () => {
    return axios.get(API_URL + "private").then((response) => {
        return response;
    });

}

export const AuthService = {
    register,
    login,
    privateRoute
}

