import PropTypes from 'prop-types';
import {
  Avatar,
  Box,
  Card,
  CardContent,
  Divider,
  Grid,
  Button,
  Typography
} from '@material-ui/core';
import LocalOfferIcon from '@material-ui/icons/LocalOffer';
import AddShoppingCartOutlinedIcon from '@material-ui/icons/AddShoppingCartOutlined';
import { toast } from 'react-toastify';
import useShopCartStore from 'src/stores/useShopCartStore';


const toast_props = {
  position: toast.POSITION.BOTTOM_RIGHT,
  hideProgressBar: false,
  draggable: true,
  pauseOnFocusLoss: false,
  pauseOnHover: false,
  progress: undefined,
}

const ProductCard = ({ product, ...rest }) => {
  const addToCart = (product) => {
    product.quantity = 1;
    useShopCartStore.getState().addProduct(product)
  
    //@ts-ignore
    toast.info(
      <Box sx={{ p: 2 }}>
        <Grid container spacing={2}
          sx={{ justifyContent: 'space-between' }}>
          <Grid item
            xs={3}
            sx={{
              alignItems: 'left',
              display: 'flex'
            }}>
              <Avatar
                alt="Product"
                src={product.image}
                variant="square"
              />
          </Grid>
          <Grid
            xs={9}
            item
            sx={{
              alignItems: 'right',
              display: 'flex'
            }}>
            {product.name} Added to Shopping Cart
          </Grid>
        </Grid>
      </Box>
      , toast_props
    );
  }

  return (
    <Card
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%'
      }}
      {...rest}
    >
      <CardContent>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            pb: 3
          }}
        >
          <Avatar
            alt="Product"
            src={"https://www.mecofarma.com/media/catalog/product/e/9/e9fa45f889e9fc4973a2ce5cd6af0eeafbef945c_000245.png?quality=80&bg-color=255,255,255&fit=bounds&height=700&width=700&canvas=700:700&format=jpeg"}
            variant="square"
          />
        </Box>
        <Typography
          align="center"
          color="textPrimary"
          gutterBottom
          variant="h4"
        >
          {product.name}
        </Typography>
        <Typography
          align="center"
          color="textPrimary"
          variant="body1"
        >
          {product.supplier.name}
        </Typography>
        <Typography
          align="center"
          color="textPrimary"
          variant="body2"
        >
          {product.description}
        </Typography>
      </CardContent>
      <Box sx={{ flexGrow: 1 }} />
      <Divider />
      <Box sx={{ p: 2 }}>
        <Grid
          container
          spacing={2}
          sx={{ justifyContent: 'space-between' }}
        >
          <Grid
            item
            sx={{
              alignItems: 'center',
              display: 'flex'
            }}
          >
            <LocalOfferIcon color="primary" />
            <Typography
              color="primary"
              display="inline"
              sx={{ pl: 1 }}
              style={{fontSize: '1rem'}}
              variant="body2"
            >
              {product.price} €
            </Typography>
          </Grid>
          <Grid
            item
            sx={{
              alignItems: 'center',
              display: 'flex'
            }}
          >
            <Button color="primary" variant="contained" onClick={() => addToCart(product)}>
              <AddShoppingCartOutlinedIcon style={{color: "white"}} />
              <Typography
                style={{color: "white"}}
                display="inline"
                sx={{ pl: 1 }}
                variant="body2"
              >
                Add
              </Typography>
            </Button>
          </Grid>
        </Grid>
      </Box>
    </Card>
  )
};

ProductCard.propTypes = {
  product: PropTypes.object.isRequired
};

export default ProductCard;
