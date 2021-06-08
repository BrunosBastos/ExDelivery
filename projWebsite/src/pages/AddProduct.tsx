import { Helmet } from 'react-helmet';
import {useState, useEffect} from 'react';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import { toast } from 'react-toastify';
import {
  Avatar,
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Container,
  Divider,
  Grid,
  makeStyles,
  TextField,
} from '@material-ui/core';
import ProductService from "../services/productService";
import SupplierService from "../services/supplierService";
import { number } from 'prop-types';
import { useNavigate } from 'react-router-dom';


const useStyles = makeStyles((theme) => ({
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing(2),
  },
  root: {
    textAlign: 'center',
    width: "100%",
    marginTop: "10px",
    marginBottom: "10px"
  },
  input: {
    display: 'none',
  },
}));

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





const AddProduct = () => {
  const [supplierslist, setSuppliersList] = useState([]); 
  const [suplier, setSupplier] = useState(1);
  const navigate = useNavigate();
  const [image, setImage] = useState("");

  const handleSubmit = (supplier) => {
    //@ts-ignore
    let name: string = document.getElementById('prodname').value;
    //@ts-ignore
    let description: string = document.getElementById('proddescription').value;
    //@ts-ignore
    let address: string = document.getElementById('prodaddress').value;
    //@ts-ignore
    let stock: number = document.getElementById('prodstock').value;
    //@ts-ignore
    let price: number = document.getElementById('prodprice').value;
    //@ts-ignore
    let photo: string = document.getElementById('prodphoto').value;
    ProductService.addnewProduct(name,description,address,price,stock,photo,supplier)
      .then( (res) => {
        return res.json();
      })
      .then( (res) => {
        
        if(res.error){
          notifyError("Error Creating Product")
        }
        else{
          navigate('/app/dashboard', { replace: true });
          notifySuccess("Success Adding new Product!")
        }
      })
      .catch( (error) => {
        notifyError("Something went wrong")
      })
  }

  
  const handleChange = (event) => {
    setSupplier(event.target.value);
  };
  
  const setImageUrl = (event) => {
    setImage(event.target.value);
  }
  
  useEffect( () => {
    SupplierService.getSuppliers()
      .then( (res) => {
        return res.json()
      })
      .then( (res) => {
        setSuppliersList(res)
      })
      .catch( () => {
        notifyError("Something went wrong")
      })
  }, [])

  const classes = useStyles();
  
  return (
    <>
      <Helmet>
        <title>Account</title>
      </Helmet>
      <Box
        sx={{
          backgroundColor: 'background.default',
          minHeight: '100%',
          py: 3
        }}
      >
        <Container maxWidth="lg">
          <Grid
            container
            spacing={3}
          >
            <Grid
              item
              lg={12}
              md={12}
              xs={12}
            >
              <form
                autoComplete="off"
                noValidate
              >
                <Card>
                  <CardHeader
                    subheader="Fill the form with product's information"
                    title="Add Product"
                  />
                  <Divider />
                  <CardContent>
                    <Grid
                      container
                      spacing={1}
                    >
                      <Grid item
                        md={4}
                        xs={12}
                        >
                        <Box
                          sx={{
                            alignItems: 'center',
                            display: 'flex',
                            flexDirection: 'column'
                          }}
                        >
                          <Avatar
                            sx={{
                              height: 160,
                              width: 160
                            }}
                            src={image}
                          />
                        </Box>
                        <div className={classes.root} >
                        <TextField
                            
                            label="Image URL"
                            name="image"
                            required
                            variant="outlined"
                            id="prodphoto"
                            onChange={setImageUrl}
                          />
                        </div>
                      </Grid>
                      <Grid
                        pt={2}
                        container
                        md={8}
                        xs={12}
                        >
                        <Grid
                          item
                          md={12}
                          xs={12}
                          mb={2}
                        >
                          <TextField
                            fullWidth
                            label="Product Name"
                            name="name"
                            required
                            variant="outlined"
                            id="prodname"
                          />
                        </Grid>
                        <Grid
                          item
                          md={12}
                          xs={12}
                          mb={2}
                          pb={2}
                        >
                          <TextField
                            fullWidth
                            label="Address"
                            name="address"
                            required
                            variant="outlined"
                            id="prodaddress"
                          />
                        </Grid>

                        <Grid
                          item
                          lg={4}
                          md={6}
                          xs={6}
                          mb={2}
                          pr={1}
                          pb={1}
                        >
                          <TextField
                            fullWidth
                            label="Stock"
                            name="stock"
                            type="number"
                            required
                            variant="outlined"
                            id="prodstock"
                          />
                        </Grid>
                        <Grid
                          item
                          lg={4}
                          md={6}
                          xs={6}
                          mb={2}
                        >
                          <TextField
                            fullWidth
                            label="Price (€)"
                            name="price"
                            type="number"
                            required
                            variant="outlined"
                            id="prodprice"
                          />
                        </Grid>
                        <Grid
                          item
                          lg={4}
                          md={6}
                          xs={6}
                          mb={2}
                        >
                          {supplierslist != null && supplierslist.length > 0 ? 
                          
                          <FormControl className={classes.formControl}>
                            <InputLabel htmlFor="age-native-simple">Pharmacy</InputLabel>
                            <Select
                              native
                              value={suplier}
                              onChange={handleChange}
                              inputProps={{
                                name: 'Pharmacy',
                                id: 'age-native-simple'
                              }}
                            >
                              {supplierslist.map((item) => {
                                return<option value={item.id}>{item.name}</option>
                              })}
                            </Select>
                          </FormControl>
                        : null  
                        }
                        </Grid>
                      </Grid>
                      <Grid
                      style={{paddingLeft: '0px'}}
                        item
                        md={12}
                        xs={12}
                      >
                        <TextField
                          fullWidth
                          
                          label="Description"
                          name="description"
                          SelectProps={{ native: true }}
                          variant="outlined"
                          multiline
                          rows="3"
                          id="proddescription"
                        >
                        </TextField>
                      </Grid>
                    </Grid>
                  </CardContent>
                  <Divider />
                  <Box
                    sx={{
                      display: 'flex',
                      justifyContent: 'flex-end',
                      p: 2
                    }}
                  >
                    <Button
                      color="primary"
                      variant="contained"
                      onClick= {() => handleSubmit(suplier)}
                    >
                      Add Product
          </Button>
                  </Box>
                </Card>
              </form>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </>
  );
}

export default AddProduct;
