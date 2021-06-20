import { EXDELIVERY_API_BASE_URL } from '../config/index';
import useAuthStore from 'src/stores/useAuthStore';


class CourierService {

    getCouriers(page){
        return fetch(EXDELIVERY_API_BASE_URL + 'couriers?page='+page, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

    fireCourier(id){
        return fetch(EXDELIVERY_API_BASE_URL + 'couriers/'+id, {
            method: 'DELETE',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }
}

export default new CourierService();