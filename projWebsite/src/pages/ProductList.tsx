import { Helmet } from 'react-helmet';
import {useEffect, useState} from 'react'
import {
  Box,
  Container,
  Grid,
  Pagination
} from '@material-ui/core';
import ProductListToolbar from 'src/components/product/ProductListToolbar';
import ProductCard from 'src/components/product/ProductCard';
import ProductService from "../services/productService";

const ProductList = () => {

  const [products,setProductsList] = useState([]);

  useEffect( () => {
    ProductService.getAllProducts()
      .then( (res) => {
        return res.json()
      })
      .then( (res) => {
        setProductsList(res)
      })
      .catch( () => {
        console.log("Something went wrong")
      })
  }, [])

  return(
  <>
    <Helmet>
      <title>Products</title>
    </Helmet>
    <Box
      sx={{
        backgroundColor: 'background.default',
        minHeight: '100%',
        py: 3
      }}
    >
      <Container maxWidth={false}>
        <ProductListToolbar />
        <Box sx={{ pt: 3 }}>
          <Grid
            container
            spacing={3}
          >
            {products && 
              products.map((prod) => {
                
              return (
                <Grid
                  item
                  key={prod.id}
                  lg={4}
                  md={6}
                  xs={12}
                >
                  <ProductCard product={prod} />
                </Grid>
                )
              }
              )
            }
            
          </Grid>
        </Box>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            pt: 3
          }}
        >
          <Pagination
            color="primary"
            count={3}
            size="small"
          />
        </Box>
      </Container>
    </Box>
  </>
);
}

export default ProductList;
