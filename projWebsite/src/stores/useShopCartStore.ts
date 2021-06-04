import createStore from "zustand";
import persist from 'src/stores/utils/persist.js';

interface Product {
    name: string;
    id: number;
    price: number;
    description: string;
    image: string;
    quantity: number;
    supplier: string;
}

function containsObject(obj: Product, list: Product[]) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === obj.id) {
            return [true, i];
        }
    }
    return [false, 0];
}

const useShopCartStore = createStore(
    persist(
        {
            key: "shopCart",
        },
        (set) => ({
            products: [],
            
            addProduct: (product: Product) => {
                return set((s) => {
                    const new_products = [...s.products]
                    const found_obj = containsObject(product, new_products)
                    if (!found_obj[0]){ 
                        new_products.push(product)
                    } else {
                        //@ts-ignore
                        new_products[found_obj[1]].quantity += 1;
                    }
                    return { products: new_products};
                });
            },
            setProductQuantity: (product: Product, quantity: number) => {
                return set((s) => {
                    let new_products = [...s.products]
                    const found_obj = containsObject(product, new_products)
                    if (found_obj[0]) {
                        //@ts-ignore
                        new_products[found_obj[1]].quantity = quantity;
                    }
                    return { products: new_products};
                });
            },
            removeProduct: (product: Product) => {
                return set((s) => {
                    let new_products = [...s.products]
                    new_products = new_products.filter(p => p.id != product.id);
                    return { products: new_products};
                });
            },
            removeAllProducts: () => {
                return set((s) => {
                    return { products: []};
                });
            }
        })
    )
);

export default useShopCartStore;
