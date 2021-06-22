import { Navigate } from 'react-router-dom';
import DashboardLayout from 'src/components/DashboardLayout';
import MainLayout from 'src/components/MainLayout';
import Account from 'src/pages/Account';
import CourierList from 'src/pages/CourierList';
import OrderList from 'src/pages/OrderList.tsx';
import AdminOrderList from 'src/pages/AdminOrderList.tsx';
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
import DeliveryDetails from './pages/DeliveryDetails.tsx'

const routes = (token, isSuperUser) => [
  {
    path: 'app',
    element: token ? <DashboardLayout /> : <Navigate to="/login" />,
    children: [
      // Admin Only
      { path: 'couriers', element: isSuperUser ? <CourierList />: <Navigate to="/404" /> },
      { path: 'adminOrders', element: isSuperUser ? <AdminOrderList />: <Navigate to="/404" /> },
      // Clients Only
      { path: 'orders', element: !isSuperUser ? <OrderList />: <Navigate to="/404" /> },
      // Anyone
      { path: 'dashboard', element: <Navigate to={ isSuperUser ? "/app/adminOrders" : "/app/orders"} />/*<Dashboard />*/ },
      { path: 'delivery/:id', element: <DeliveryDetails />},
      { path: '*', element: <Navigate to="/404" /> },
    ]
  },
  {
    path: '/',
    element: !token ? <MainLayout /> : <Navigate to="/app/dashboard" />,
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
