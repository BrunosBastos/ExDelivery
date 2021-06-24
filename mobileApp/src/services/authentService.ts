import { EXDELIVERY_API_BASE_URL } from '../config/index';

class AuthentService {
    login(email, password) {
        return fetch(EXDELIVERY_API_BASE_URL + 'login', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: email, password: password })
        })
    }

    register(email, password, name) {
        return fetch(EXDELIVERY_API_BASE_URL + 'register', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: name, email: email, password: password })
        })
    }
}
export default new AuthentService()