import axios from "axios";

const baseURL = "http://localhost:4000";

const myAxios = axios.create({
    baseURL: baseURL,
});

myAxios.interceptors.response.use(function (response) {
    return response.data;
}, function (error) {
    return Promise.reject(error);
})

export default myAxios;