import { Helmet } from 'react-helmet';
import products from 'src/__mocks__/customers';
import { useState } from 'react';
import moment from 'moment';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {
  Avatar,
  Box,
  Container,
  Card,
  Checkbox,
  Button,
  Table,
  TableBody,
  Grid,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  Typography
} from '@material-ui/core';
import getInitials from 'src/utils/getInitials';
import Purchase from 'src/components/shoppingCart/Purchase';


const ShopCartList = () => {
  const [selectedProductIds, setSelectedProductIds] = useState([]);
  const [limit, setLimit] = useState(5);
  const [page, setPage] = useState(0);

  const handleSelectAll = (event) => {
    let newSelectedProductIds;

    if (event.target.checked) {
      newSelectedProductIds = products.map((product) => product.id);
    } else {
      newSelectedProductIds = [];
    }

    setSelectedProductIds(newSelectedProductIds);
  };

  const handleSelectOne = (event, id) => {
    const selectedIndex = selectedProductIds.indexOf(id);
    let newSelectedProductIds = [];

    if (selectedIndex === -1) {
      newSelectedProductIds = newSelectedProductIds.concat(selectedProductIds, id);
    } else if (selectedIndex === 0) {
      newSelectedProductIds = newSelectedProductIds.concat(selectedProductIds.slice(1));
    } else if (selectedIndex === selectedProductIds.length - 1) {
      newSelectedProductIds = newSelectedProductIds.concat(selectedProductIds.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelectedProductIds = newSelectedProductIds.concat(
        selectedProductIds.slice(0, selectedIndex),
        selectedProductIds.slice(selectedIndex + 1)
      );
    }

    setSelectedProductIds(newSelectedProductIds);
  };

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
    setPage(0);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <>
      <Helmet>
        <title>My Shopping Cart</title>
      </Helmet>
      <Box
        sx={{
          backgroundColor: 'background.default',
          minHeight: '100%',
          py: 3
        }}
      >
        <Container maxWidth={false}>
          <Box sx={{ pt: 3 }}>
            <Card>
              <PerfectScrollbar>
                <Box sx={{ minWidth: 1050 }}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell padding="checkbox">
                          <Checkbox
                            checked={selectedProductIds.length === products.length}
                            color="primary"
                            indeterminate={
                              selectedProductIds.length > 0
                              && selectedProductIds.length < products.length
                            }
                            onChange={handleSelectAll}
                          />
                        </TableCell>
                        <TableCell>
                          Name
                        </TableCell>
                        <TableCell>
                          Reference
                        </TableCell>
                        <TableCell>
                          Price
                        </TableCell>
                        <TableCell>
                          Quantity
                        </TableCell>
                        <TableCell>
                          Supplier
                        </TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {products.slice(page*limit, page*limit + limit).map((product) => (
                        <TableRow
                          hover
                          key={product.id}
                          selected={selectedProductIds.indexOf(product.id) !== -1}
                        >
                          <TableCell padding="checkbox">
                            <Checkbox
                              checked={selectedProductIds.indexOf(product.id) !== -1}
                              onChange={(event) => handleSelectOne(event, product.id)}
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
                                src={product.avatarUrl}
                                sx={{ mr: 2 }}
                              >
                                {getInitials(product.name)}
                              </Avatar>
                              <Typography
                                color="textPrimary"
                                variant="body1"
                              >
                                {product.name}
                              </Typography>
                            </Box>
                          </TableCell>
                          <TableCell>
                            {product.email}
                          </TableCell>
                          <TableCell>
                            {`${product.address.city}, ${product.address.state}, ${product.address.country}`}
                          </TableCell>
                          <TableCell>
                            {product.phone}
                          </TableCell>
                          <TableCell>
                            {moment(product.createdAt).format('DD/MM/YYYY')}
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </Box>
              </PerfectScrollbar>
              <TablePagination
                component="div"
                count={products.length}
                onPageChange={handlePageChange}
                onRowsPerPageChange={handleLimitChange}
                page={page}
                rowsPerPage={limit}
                rowsPerPageOptions={[5, 10, 25]}
              />
            </Card>
            <Grid
              container
              spacing={1}
            >
              <Grid item
                lg={6}
                md={6}
                sm={4}
                >
                { selectedProductIds.length > 0 ? 
                  <Box
                    sx={{
                      display: 'flex',
                      justifyContent: 'flex-start',
                      pt: 2
                    }}
                  >
                    <Button
                      color="secondary"
                      variant="contained"
                    >
                      Remove Selected
                    </Button>
                  </Box>
                : '' }
              </Grid>
              <Grid item
                lg={6}
                md={6}
                sm={8}
              >
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'flex-end',
                    pt: 2
                  }}
                >
                  <Purchase />
                </Box>
              </Grid>
            </Grid>
          </Box>
        </Container>
      </Box>
    </>
  )
}

export default ShopCartList;
