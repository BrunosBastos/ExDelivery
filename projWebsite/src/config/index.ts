export let EXDELIVERY_API_BASE_URL: string = null;
if (process.env.REACT_APP_RUNNING_MODE != null && process.env.REACT_APP_RUNNING_MODE == 'PRODUCTION'){
    let HOST = "192.168.160.231"
    EXDELIVERY_API_BASE_URL = `http://${HOST}:8081/api/v1/`;
    console.log(EXDELIVERY_API_BASE_URL);
}
else {
    EXDELIVERY_API_BASE_URL = 'http://localhost:8081/api/v1/';
}
