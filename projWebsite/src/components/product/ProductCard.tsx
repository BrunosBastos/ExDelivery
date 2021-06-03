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
import Logo from 'src/components/Logo';


const toast_props = {
  position: toast.POSITION.TOP_RIGHT,
  autoClose: 5000,
  hideProgressBar: false,
  closeOnClick: true,
  draggable: true,
  pauseOnFocusLoss: false,
  pauseOnHover: false,
  progress: undefined,
}

const ProductCard = ({ product, ...rest }) => {
  const addToCart = (product) => {
    //@ts-ignore
    toast.dark(
      <Box sx={{ p: 2 }}>
        <Grid container spacing={2}
          sx={{ justifyContent: 'space-between' }}>
          <Grid item
            sx={{
              alignItems: 'center',
              display: 'flex'
            }}>
              <Logo />
          </Grid>
          <Grid
            item
            sx={{
              alignItems: 'center',
              display: 'flex'
            }}>
            {product.title}
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
            src={product.media}
            variant="square"
          />
        </Box>
        <Typography
          align="center"
          color="textPrimary"
          gutterBottom
          variant="h4"
        >
          {product.title}
        </Typography>
        <Typography
          align="center"
          color="textPrimary"
          variant="body1"
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
              19.99 €
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
