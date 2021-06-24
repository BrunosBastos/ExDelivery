import { EXDELIVERY_API_BASE_URL} from '../config/index';
import useAuthStore from 'src/stores/useAuthStore';


class SupplierService {
    getSuppliers() {
        return fetch( EXDELIVERY_API_BASE_URL + "suppliers", {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            },
        })
    }

    addNewSupplier(name: string, lat: number, lon: number) {
        return fetch( EXDELIVERY_API_BASE_URL + "suppliers", {
            method: 'Post',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            },
            body: JSON.stringify({name: name, lat: lat, lon: lon})
        })
    }
}
export default new SupplierService()