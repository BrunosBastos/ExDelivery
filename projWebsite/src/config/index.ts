export let EXDELIVERY_API_BASE_URL: string = null;
if (process.env.REACT_APP_RUNNING_MODE != null && process.env.REACT_APP_RUNNING_MODE == 'PRODUCTION'){
    let HOST = "192.168.160.231"
    EXDELIVERY_API_BASE_URL = `http://${HOST}:8081/api/v1/`;
    console.log(EXDELIVERY_API_BASE_URL);
}
else {
    EXDELIVERY_API_BASE_URL = 'http://localhost:8081/api/v1/';
}

export let MEDEX_API_BASE_URL: string = null;
if (process.env.REACT_APP_RUNNING_MODE != null && process.env.REACT_APP_RUNNING_MODE == 'PRODUCTION'){
    let HOST = "192.168.160.231"
    MEDEX_API_BASE_URL = `http://${HOST}:8080/api/v1/`;
    console.log(MEDEX_API_BASE_URL);
}
else {
    MEDEX_API_BASE_URL = 'http://localhost:8080/api/v1/';
}