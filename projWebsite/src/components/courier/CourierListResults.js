import { useState } from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {
  Avatar,
  Box,
  Card,
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

const CourierListResults = ({ couriers, ...rest }) => {
  const [selectedCourierIds, setSelectedCourierIds] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);

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
    <Card {...rest}>
      <PerfectScrollbar>
        <Box sx={{ minWidth: 1050 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell padding="checkbox">
                  <Checkbox
                    checked={selectedCourierIds.length === couriers.length}
                    color="primary"
                    indeterminate={
                      selectedCourierIds.length > 0
                      && selectedCourierIds.length < couriers.length
                    }
                    onChange={handleSelectAll}
                  />
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
                  Phone
                </TableCell>
                <TableCell>
                  Registration date
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
                  <TableCell padding="checkbox">
                    <Checkbox
                      checked={selectedCourierIds.indexOf(courier.id) !== -1}
                      onChange={(event) => handleSelectOne(event, courier.id)}
                      value="true"
                    />
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
                        {getInitials(courier.name)}
                      </Avatar>
                      <Typography
                        color="textPrimary"
                        variant="body1"
                      >
                        {courier.name}
                      </Typography>
                    </Box>
                  </TableCell>
                  <TableCell>
                    {courier.email}
                  </TableCell>
                  <TableCell>
                    {`${courier.address.city}, ${courier.address.state}, ${courier.address.country}`}
                  </TableCell>
                  <TableCell>
                    {courier.phone}
                  </TableCell>
                  <TableCell>
                    {moment(courier.createdAt).format('DD/MM/YYYY')}
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
        rowsPerPageOptions={[5, 10, 25]}
      />
    </Card>
  );
};

CourierListResults.propTypes = {
  couriers: PropTypes.array.isRequired
};

export default CourierListResults;