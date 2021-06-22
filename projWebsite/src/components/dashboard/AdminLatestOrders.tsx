import { useState, useEffect } from 'react';
import moment from 'moment';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {
  Box,
  Button,
  Card,
  CardHeader,
  Chip,
  Divider,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TableSortLabel,
  TablePagination,
  Tooltip
} from '@material-ui/core';
import DeliveryService from "src/services/deliveryService";
import { toast } from 'react-toastify';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import Paginator from "src/components/paginator/Paginator";



interface Order {
  id: any;
  lat: number;
  lon: number;
  purchaseHost: string;
  purchaseId: number;
  state: string;
  courier: {
    id: any;
    lat: number;
    lon: number;
    reputation: number;
    user: {
      email: string;
      name: string;
      superUser: boolean;
      userId: any;
    }
  };
}

const notifySuccess = (msg) => {
  toast.success(msg, {
    position: toast.POSITION.TOP_CENTER
    });
}

const notifyError = (msg) => {
  toast.error(msg, {
    position: toast.POSITION.TOP_CENTER
    });
}

interface AdminLatestOrdersProps {
  recent: string;
  email: string;
}

const AdminLatestOrders: React.FC<AdminLatestOrdersProps> = ({recent, email}) => {
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    DeliveryService.getDeliveries(page, recent == 'desc', email)
      .then( (res) => {
        if (res.status === 200) {
          return res.json()
        }
        notifyError("Something went wrong")
        return null;
      })
      .then((res) => {
        console.log(res)
        if (res) {
          setOrders(res)
        }
      })
      .catch(() => {
        console.log("Something went wrong")
      })
  }, [recent, email, page])

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
    setPage(0);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <Card>
      <CardHeader title="Latest Orders" />
      <Divider />
      <PerfectScrollbar>
        <Box sx={{ minWidth: 800 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sortDirection={recent === 'asc'  ? 'asc' : 'desc'}>
                  Reference
                </TableCell>
                <TableCell>
                  Delivery Location
                </TableCell>
                <TableCell>
                  Courier Email
                </TableCell>
                <TableCell>
                  Purchase Host
                </TableCell>
                <TableCell>
                  Purchase Id
                </TableCell>
                <TableCell>
                  Status
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {orders && orders.slice(page*limit, page*limit + limit).map((order: Order, index) => (
                <TableRow
                  hover
                  key={order.id+recent}
                  style={{cursor:'pointer'}}
                  onClick={() => navigate('/app/delivery/'+order.id, { replace: true })}
                >
                  <TableCell>
                    {order.id}
                  </TableCell>
                  <TableCell>
                    <p>Latitude {order.lat}</p>
                    <p>Longitude {order.lon}</p>
                  </TableCell>
                  <TableCell>
                    {order.courier?.user?.email}
                  </TableCell>
                  <TableCell>
                    {order.purchaseHost}
                  </TableCell>
                  <TableCell>
                    {order.purchaseId}
                  </TableCell>
                  <TableCell>
                    <Chip
                      color="primary"
                      label={order.state}
                      size="small"
                    />
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </PerfectScrollbar>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'flex-end',
          p: 2
        }}
      >
        <Paginator hasNext={orders.length == limit} page={page} changePage={(page) => setPage(page)}/>
      </Box>
    </Card>
  )
}

export default AdminLatestOrders;