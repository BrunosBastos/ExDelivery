import { Navigate } from 'react-router-dom';
import DashboardLayout from 'src/components/DashboardLayout';
import MainLayout from 'src/components/MainLayout';
import Account from 'src/pages/Account';
import CourierList from 'src/pages/CourierList';
import OrderList from 'src/pages/OrderList.tsx';
import Dashboard from 'src/pages/Dashboard';
import Login from 'src/pages/Login.tsx';
import NotFound from 'src/pages/NotFound';
import ProductList from 'src/pages/ProductList.tsx';
import Register from 'src/pages/Register.tsx';
import Settings from 'src/pages/Settings';
import AddProduct from './pages/AddProduct.tsx';
import AddSupplier from './pages/AddSupplier.tsx';
import PharmacyProductDetails from './pages/PharmarcyProductDetails.tsx';
import ShoppingCart from './pages/ShoppingCart.tsx';

const routes = [
  {
    path: 'app',
    element: <DashboardLayout />,
    children: [
      { path: 'account', element: <Account /> },
      { path: 'couriers', element: <CourierList /> },
      { path: 'orders', element: <OrderList /> },
      { path: 'dashboard', element: <Dashboard /> },
      { path: 'products', element: <ProductList /> },
      { path: 'settings', element: <Settings /> },
      { path: 'addProduct', element: <AddProduct />},
      { path: 'addSupplier', element: <AddSupplier />},
      { path: 'product/:id', element: <PharmacyProductDetails />},
      { path: 'shoppingCart', element: <ShoppingCart />},
      { path: '*', element: <Navigate to="/404" /> }
    ]
  },
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { path: 'login', element: <Login /> },
      { path: 'register', element: <Register /> },
      { path: '404', element: <NotFound /> },
      { path: '/', element: <Navigate to="/app/dashboard" /> },
      { path: '*', element: <Navigate to="/404" /> }
    ]
  }
];

export default routes;
