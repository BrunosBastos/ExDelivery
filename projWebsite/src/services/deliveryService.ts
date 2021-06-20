import { EXDELIVERY_API_BASE_URL } from '../config/index';
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


    getProduct(id: any){
        return fetch(EXDELIVERY_API_BASE_URL + 'products/' + id, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

    addnewProduct(name:string, description: string,address:string, price: number, stock: number, photo: string, supplier: number ){
        return fetch(EXDELIVERY_API_BASE_URL + 'products', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            },
            body: JSON.stringify({
                name: name, 
                description: description, 
                address: address,
                price: price, 
                stock: stock, 
                photo:photo, 
                supplier: supplier})
        })
    }

    updateProduct(prodid:number ,name:string, description: string, price: number, stock: number, photo: string, supplier: number ){

            return fetch(EXDELIVERY_API_BASE_URL + 'products/' + prodid, {
                method: 'PUT',
                mode: 'cors',
                headers: {
                    'accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization' : "Bearer "+ useAuthStore.getState().token
                },
                body: JSON.stringify({
                    name: name, 
                    description: description, 
                    price: price, 
                    stock: stock, 
                    imageUrl:photo, 
                    supplier: supplier})
            })
}
}

export default new DeliveryService();