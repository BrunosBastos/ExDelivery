import {useEffect,useState} from 'react';
import { Helmet } from 'react-helmet';
import getInitials from 'src/utils/getInitials';
import products from 'src/__mocks__/products'
import order from 'src/__mocks__/order';
import { makeStyles } from '@material-ui/core/styles';
import StarRatings from 'react-star-ratings';
import {Avatar,
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Divider,
    Typography,
    Container,
    Grid,
    TextField,
    CardHeader,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
} from '@material-ui/core';
import { toast } from 'react-toastify';
import DeliveryService from "src/services/deliveryService";
import useAuthStore from 'src/stores/useAuthStore';

const styles = makeStyles ({
    root: {
      width: "100%",
      overflowX: "auto",
      
    },
    table: {
        minWidth: 820,
        marginLeft: "5%",
    }
  });


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

  
const DeliveryDetails = () => {
    const [price, setPrice] = useState("0.00");
    // maybe useful to check later on, if a user has already reviewed a product
    const [review, setReview] = useState(null);
    const  classes  = styles();
    const [rating, setRating] = useState(0);
    const [reviewdescription, setReviewDescription] = useState("");
    const [delivery, setDelivery] = useState(null);
    const url = window.location.pathname;
    const delivery_id = url.substring(url.lastIndexOf('/') + 1);

    useEffect(() => {
        console.log(useAuthStore.getState())
        DeliveryService.getDelivery(delivery_id)
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
              setDelivery(res)
            }
          })
          .catch(() => {
            console.log("Something went wrong")
          })
    }, [])

    const confirmDelivery = () => {
        DeliveryService.confirmDelivery(delivery_id)
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
                    setDelivery(res)
                    notifySuccess("Successfully confirmed the delivery!")
                }
            })
            .catch(() => {
                console.log("Something went wrong")
            })
    }

    const getTotalPrice = () => {
        let totalPrice = 0;
        for (let i = 0; i < products.length; i++) {
            totalPrice += products[i].price * products[i].quantity;
        }
        setPrice(totalPrice.toFixed(2))
    }
    const changeRating = ( newRating, name ) => {
        setRating(newRating);
    }
   
    const handleReviewText = (event) => {
        setReviewDescription(event.target.value)
    } 

    useEffect(() => {
        getTotalPrice()
    }, [products])
    return(
        <>
            <Helmet>
            <title>Delivery Details</title>
        </Helmet>
        <Box
            sx={{
            backgroundColor: 'background.default',
            minHeight: '100%',
            py: 6
            }}
        >
            <Grid container spacing={8}>
        <Grid
                item
                lg={8}
                md={8}
                xs={8}
            className={classes.root}
            >
                <form
                autoComplete="off"
                noValidate
                >
                <Card className={classes.table}>
                    <CardHeader
                    title="Ordered Products"
                    />
                    <Divider />
                    <CardContent>
                    <Grid
                        container
                        spacing={3}
                    >
                        <Grid
                        item
                        md={12}
                        xs={12}
                        >
                    <Table>
                        <TableHead>
                        <TableRow>
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
                            {products.map((product) => (
                                <>
                                <TableRow
                                hover
                                key={product.id}
                                >
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
                                    
                                    {product.quantity}
                                </TableCell>
                                <TableCell>
                                    {(product.quantity * product.price).toFixed(2)}
                                </TableCell>
                                </TableRow>
                                </>
                                
                            ))}
                            <TableRow>
                                    <TableCell colSpan={5}>
                                    <Typography
                                            alignContent='left'
                                            color="textPrimary"
                                            variant="h6"
                                            >
                                            Total
                                    </Typography>
                                    </TableCell>
                                    <TableCell align="left"  style={{'marginRight': '10%'}}>
                                    <Typography
                                            alignContent='left'
                                            color="textPrimary"
                                            variant="h5"
                                            >
                                                    {price}€
                                                    </Typography>
                                    </TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </Grid>
                    </Grid>
                    </CardContent>
                    <Divider />
                    
                </Card>
                </form>
            </Grid>
            <Grid
                item
                lg={4}
                md={4}
                xs={4}
            >
            <Card style= {{'marginRight': '10%'}}>
            <CardHeader
                    title="Order Details"
                    />
                    <Divider />
                <CardContent>
                    <Grid
                        item
                        sm={12}
                        md={12}
                        xs={12}
                        >
                        <div>
                            <Typography variant="overline" display="block" gutterBottom>
                                Delivery reference
                            </Typography>
                            <Typography variant="body2" display="block" gutterBottom>
                                {delivery?.id}
                            </Typography>
                        </div>
                    </Grid>
                    
                        <div>
                            <Typography variant="overline" display="block" gutterBottom>
                                Delivery State
                            </Typography>
                            <Typography variant="body2" display="block" gutterBottom>
                                {delivery?.state}
                            </Typography>
                        </div>
                        <div>
                            <Typography variant="overline" display="block" gutterBottom>
                                Delivery Location
                            </Typography>
                            <Typography variant="body2" display="block" gutterBottom>
                                <p>Latitude: {delivery?.lat}</p>
                                <p>Longitude: {delivery?.lon}</p>
                            </Typography>
                        </div>
                    {delivery?.state == "assigned" && !useAuthStore.getState().user?.superUser ?
                        <div>
                            <Button  
                                color="primary"
                                variant="contained"
                                onClick={() => confirmDelivery()}
                            >
                                Confirm your delivery!
                            </Button>
                        </div>
                    : ""}
                </CardContent>
            </Card>
            </Grid>
            </Grid>
            </Box>
        </>
    )
}
export default DeliveryDetails;