import { Helmet } from 'react-helmet';
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
  Typography,
  TextField
} from '@material-ui/core';
import getInitials from 'src/utils/getInitials';
import Purchase from 'src/components/shoppingCart/Purchase';
import useShopCartStore from 'src/stores/useShopCartStore'

const ShopCartList = () => {
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [limit, setLimit] = useState(5);
  const [page, setPage] = useState(0);
  const products = useShopCartStore(state => state.products)

  const handleSelectAll = (event) => {
    let newSelectedProducts;

    if (event.target.checked) {
      newSelectedProducts = products.map((product) => product);
    } else {
      newSelectedProducts = [];
    }

    setSelectedProducts(newSelectedProducts);
  };

  const handleSelectOne = (event, product) => {
    const selectedIndex = selectedProducts.indexOf(product);
    let newSelectedProducts = [];

    if (selectedIndex === -1) {
      newSelectedProducts = newSelectedProducts.concat(selectedProducts, product);
    } else if (selectedIndex === 0) {
      newSelectedProducts = newSelectedProducts.concat(selectedProducts.slice(1));
    } else if (selectedIndex === selectedProducts.length - 1) {
      newSelectedProducts = newSelectedProducts.concat(selectedProducts.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelectedProducts = newSelectedProducts.concat(
        selectedProducts.slice(0, selectedIndex),
        selectedProducts.slice(selectedIndex + 1)
      );
    }

    setSelectedProducts(newSelectedProducts);
  };

  const removeSelected = () => {
    selectedProducts.forEach(product => {
      useShopCartStore.getState().removeProduct(product);
    })
    setSelectedProducts([])
    //@ts-ignore
    document.getElementById("select_box").checked = false;
  }

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
    setPage(0);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  const changeQuantity = (product) => {
    //@ts-ignore
    useShopCartStore.getState().setProductQuantity(product, document.getElementById(product.id + 'quant').value)
  }

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
                            checked={selectedProducts.length === 0 ? false :
                                      selectedProducts.length === products.length}
                            color="primary"
                            indeterminate={
                              selectedProducts.length > 0
                              && selectedProducts.length < products.length
                            }
                            id="select_box"
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
                          Supplier
                        </TableCell>
                        <TableCell>
                          Price
                        </TableCell>
                        <TableCell>
                          Quantity
                        </TableCell>
                        <TableCell>
                          Total
                        </TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {products.slice(page*limit, page*limit + limit).map((product) => (
                        <TableRow
                          hover
                          key={product.id}
                          selected={selectedProducts.indexOf(product) !== -1}
                        >
                          <TableCell padding="checkbox">
                            <Checkbox
                              checked={selectedProducts.indexOf(product) !== -1}
                              onChange={(event) => handleSelectOne(event, product)}
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
                                src={product.image}
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
                            {product.id}
                          </TableCell>
                          <TableCell>
                            {product.supplier}
                          </TableCell>
                          <TableCell>
                            {product.price}
                          </TableCell>
                          <TableCell>
                            <TextField
                              required
                              name="quantity"
                              onChange={() => changeQuantity(product)}
                              type="number"
                              InputProps={{ inputProps: { min: 1, max: 1000} }}
                              defaultValue={product.quantity}
                              variant="outlined"
                              id={product.id+'quant'}
                              style={{maxWidth: 100}}
                            />
                          </TableCell>
                          <TableCell>
                            {(product.quantity * product.price).toFixed(2)}
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
                { selectedProducts.length > 0 ? 
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
                      onClick={() => removeSelected()}
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