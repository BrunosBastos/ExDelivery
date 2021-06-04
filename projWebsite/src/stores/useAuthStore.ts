import createStore from "zustand";
import persist from 'src/stores/utils/persist.js';


const useAuthStore = createStore(
    persist(
        {
            key:"auth",
        },
        (set) => ({
            token: String as null,
            expire_date: String as null,
            login: (token: string, expire_date: string) => {
                set((state) => ({
                    token:token,
                    expire_date:expire_date
                }))
            },
            leave: () => {
                set((state) => ({
                    token: null,
                    expire_date: null,
                    guest_uuid: null
                }))
            },
            updateToken: (token: string, expire_date: string) => {
                set((state) => ({
                        token: token,
                        expire_date: expire_date
                }))
            }
        })
    ),
)

export default useAuthStore;