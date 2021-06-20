import products  from './products' 
import { v4 as uuid } from 'uuid';

export default {
    id: uuid(),
    products: products,
    order_date: '2021-06-01',
    lat: 60.789,
    lon: 50.284,
    delivered: true
}