import createStore from "zustand";
import persist from 'src/stores/utils/persist.js';


interface User {
    name: string;
    email: string;
    superUser: boolean;
    userId: number;
}


const useAuthStore = createStore(
    persist(
        {
            key:"auth3",
        },
        (set) => ({
            token: null,
            user: null,
            login: (token: string, user: User) => {
                set((state) => ({
                    token: token,
                    user: user,
                }))
            },
            exit: () => {
                set((state) => ({
                    token: null,
                    user: null,
                }))
            }
        })
    ),
)

export default useAuthStore;