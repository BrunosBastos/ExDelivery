import {
    Avatar,
    Card,
    CardContent,
    Grid,
    Button,
    Typography,
    Box
} from '@material-ui/core';
import { useState, useEffect } from 'react';
import { indigo } from '@material-ui/core/colors';
import EuroOutlinedIcon from '@material-ui/icons/EuroOutlined';
import useShopCartStore from 'src/stores/useShopCartStore'


const Purchase = () => {
  const products = useShopCartStore(state => state.products)
  const [price, setPrice] = useState("0.00")

  const getTotalPrice = () => {
    let totalPrice = 0;
    for (let i = 0; i < products.length; i++) {
      totalPrice += products[i].price * products[i].quantity;
    }
    setPrice(totalPrice.toFixed(2))
  }

  useEffect(() => {
    getTotalPrice()
  }, [products])

  return (
    <Card sx={{minWidth: 300}}>
      <CardContent>
        <Grid
          container
          sx={{ justifyContent: 'space-between' }}
          spacing={2}
        >
          <Grid item xs={5}>
            <Avatar
              sx={{
                backgroundColor: indigo[600],
                height: 56,
                width: 56
              }}
            >
              <EuroOutlinedIcon />
            </Avatar>
          </Grid>
          <Grid item xs={7}>
            <Grid item xs={12}>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                }}
              >
                <Typography
                  color="textSecondary"
                  gutterBottom
                  variant="h6"
                >
                  Total Price
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={12}>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                }}
              >
                <Typography
                  color="textPrimary"
                  variant="h3"
                >
                  {price} €
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </Grid>
        <Grid
          container
          spacing={2}
          sx={{ justifyContent: 'space-between' }}
        >
          <Grid item xs={12}>
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'flex-end',
                pt: 2
              }}
            >
              <Button color="primary" variant="contained">
                Purchase
              </Button>
            </Box>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}
export default Purchase;
  