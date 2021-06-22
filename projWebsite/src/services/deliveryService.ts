import { EXDELIVERY_API_BASE_URL, MEDEX_API_BASE_URL } from '../config/index';
import useAuthStore from 'src/stores/useAuthStore';


class DeliveryService {

    getMyDeliveries(page, recent){
        return fetch(EXDELIVERY_API_BASE_URL + 'deliveries/me?page='+page+'&recent='+recent, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

    getDeliveries(page, recent, email){
        let url = email ? '&courierEmail='+email : ''
        return fetch(EXDELIVERY_API_BASE_URL + 'deliveries?page='+page+'&recent='+recent+url, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

    getDelivery(id) {
        return fetch(EXDELIVERY_API_BASE_URL + 'deliveries/'+id, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

    confirmDelivery(id) {
        return fetch(EXDELIVERY_API_BASE_URL + 'deliveries/'+id, {
            method: 'PUT',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

    getPurchaseDetails(purchaseHost, id: any) {
        console.log(purchaseHost)
        return fetch(MEDEX_API_BASE_URL + 'purchases/' + id, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }
}

export default new DeliveryService();