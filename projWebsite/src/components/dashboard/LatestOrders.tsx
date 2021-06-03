import { useState } from 'react';
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

interface Order {
  id: any;
  ref: string;
  amount: number;
  product: {
    name: string;
  };
  delLocation: string;
  createdAt: number;
  status: string;
}

interface LatestOrdersProps {
  orders: Order[];
}


const LatestOrders: React.FC<LatestOrdersProps> = ({orders}) => {
  const [limit, setLimit] = useState(5);
  const [page, setPage] = useState(0);
  const [orderDir, setOrderDir] = useState('asc')

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
    setPage(0);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  const sortByDate = () => {
    const isAsc = orderDir === 'asc';
    setOrderDir(isAsc ? 'desc' : 'asc');
  }


  function descendingComparator(a, b) {
    if (b.createdAt < a.createdAt) {
      return -1;
    }
    if (b.createdAt > a.createdAt) {
      return 1;
    }
    return 0;
  }
  
  function getComparator() {
    return orderDir === 'asc'
      ? (a, b) => descendingComparator(a, b)
      : (a, b) => -descendingComparator(a, b);
  }

  function stableSort(comparator) {
    const stabilizedThis = orders.map((el, index) => [el, index]);
    stabilizedThis.sort((a, b) => {
      const order = comparator(a[0], b[0]);
      if (order !== 0) return order;
      //@ts-ignore
      return a[1] - b[1];
    });
    return stabilizedThis.map((el) => el[0]);
  }

  return (
    <Card>
      <CardHeader title="Latest Orders" />
      <Divider />
      <PerfectScrollbar>
        <Box sx={{ minWidth: 800 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>
                  Order Ref
                </TableCell>
                <TableCell>
                  courier
                </TableCell>
                <TableCell>
                  Delivery Location
                </TableCell>
                <TableCell sortDirection={orderDir === 'asc'  ? 'asc' : 'desc'}>
                  <Tooltip
                    enterDelay={300}
                    title={orderDir === 'desc' ? 'Get Newest' : 'Get Oldest'}
                  >
                    <TableSortLabel
                      active={true}
                      direction={orderDir === 'asc'  ? 'asc' : 'desc'}
                      onClick={sortByDate}
                    >
                      Date
                    </TableSortLabel>
                  </Tooltip>
                </TableCell>
                <TableCell>
                  Status
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {orders && stableSort(getComparator()).slice(page*limit, page*limit + limit).map((order: Order) => (
                <TableRow
                  hover
                  key={order.id}
                >
                  <TableCell>
                    {order.ref}
                  </TableCell>
                  <TableCell>
                    {order.product.name}
                  </TableCell>
                  <TableCell>
                    {order.delLocation}
                  </TableCell>
                  <TableCell>
                    {moment(order.createdAt).format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    <Chip
                      color="primary"
                      label={order.status}
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
        <TablePagination
          component="div"
          count={orders.length}
          onPageChange={handlePageChange}
          onRowsPerPageChange={handleLimitChange}
          page={page}
          rowsPerPage={limit}
          rowsPerPageOptions={[5, 10, 25]}
        />
      </Box>
    </Card>
  )
}

export default LatestOrders;
