import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import PerfectScrollbar from 'react-perfect-scrollbar';
import { toast } from 'react-toastify';
import {
  Avatar,
  Box,
  Card,
  Button,
  Checkbox,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  Typography
} from '@material-ui/core';
import getInitials from 'src/utils/getInitials';
import CourierService from 'src/services/courierService';

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


const CourierListResults = () => {
  const [selectedCourierIds, setSelectedCourierIds] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);
  const [couriers, setCouriers] = useState([])

  useEffect(() => {
    CourierService.getCouriers(page)
      .then((res) => {
        console.log(res)
        if (res.status == 200) {
          return res.json()
        }
        return null
      })
      .then((res) => {
        console.log(res)
        if (res) {
          setCouriers(res)
        }
      })
      .catch(() => {
        console.log("Something went wrong")
      })
  }, [])

  const fireCourier = (id) => {
    CourierService.fireCourier(id)
      .then((res) => {
        console.log(res)
        if (res.status == 200) {
          return res.json()
        }
        notifyError("Something went wrong")
        return null
      })
      .then((res) => {
        console.log(res)
        if (res) {
          setCouriers(couriers => couriers.filter( courier => courier.id != res.id ))
          notifySuccess("Successfully fired this courier.")
        }
      })
      .catch(() => {
        console.log("Something went wrong")
      })
  }

  const handleSelectAll = (event) => {
    let newSelectedCourierIds;

    if (event.target.checked) {
      newSelectedCourierIds = couriers.map((courier) => courier.id);
    } else {
      newSelectedCourierIds = [];
    }

    setSelectedCourierIds(newSelectedCourierIds);
  };

  const handleSelectOne = (event, id) => {
    const selectedIndex = selectedCourierIds.indexOf(id);
    let newSelectedCourierIds = [];

    if (selectedIndex === -1) {
      newSelectedCourierIds = newSelectedCourierIds.concat(selectedCourierIds, id);
    } else if (selectedIndex === 0) {
      newSelectedCourierIds = newSelectedCourierIds.concat(selectedCourierIds.slice(1));
    } else if (selectedIndex === selectedCourierIds.length - 1) {
      newSelectedCourierIds = newSelectedCourierIds.concat(selectedCourierIds.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelectedCourierIds = newSelectedCourierIds.concat(
        selectedCourierIds.slice(0, selectedIndex),
        selectedCourierIds.slice(selectedIndex + 1)
      );
    }

    setSelectedCourierIds(newSelectedCourierIds);
  };

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <Card>
      <PerfectScrollbar>
        <Box sx={{ minWidth: 750 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>
                  Reference
                </TableCell>
                <TableCell>
                  Name
                </TableCell>
                <TableCell>
                  Email
                </TableCell>
                <TableCell>
                  Location
                </TableCell>
                <TableCell>
                  Reputation
                </TableCell>
                <TableCell>
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {couriers.slice(0, limit).map((courier) => (
                <TableRow
                  hover
                  key={courier.id}
                  selected={selectedCourierIds.indexOf(courier.id) !== -1}
                >
                  <TableCell>
                    {courier.id}
                  </TableCell>
                  <TableCell>
                    <Box
                      sx={{
                        alignItems: 'center',
                        display: 'flex'
                      }}
                    >
                      <Avatar
                        src={courier.avatarUrl}
                        sx={{ mr: 2 }}
                      >
                        {getInitials(courier.user?.name)}
                      </Avatar>
                      <Typography
                        color="textPrimary"
                        variant="body1"
                      >
                        {courier.user?.name}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    {courier.user?.email}
                  </TableCell>
                  <TableCell>
                    <p>Latitude: {courier.lat}</p>
                    <p>Longitude: {courier.lon}</p>
                  </TableCell>
                  <TableCell>
                    {courier.reputation}
                  </TableCell>
                  <TableCell>
                    <Button
                      color="secondary"
                      variant="contained"
                      onClick={() => fireCourier(courier.id)}
                    >
                      Fire
                    </Button> 
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </PerfectScrollbar>
      <TablePagination
        component="div"
        count={couriers.length}
        onPageChange={handlePageChange}
        onRowsPerPageChange={handleLimitChange}
        page={page}
        rowsPerPage={limit}
        rowsPerPageOptions={[10]}
      />
    </Card>
  );
};

CourierListResults.propTypes = {
  couriers: PropTypes.array.isRequired
};

export default CourierListResults;