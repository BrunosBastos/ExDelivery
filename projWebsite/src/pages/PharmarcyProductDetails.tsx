import {useEffect,useState} from 'react';
import { Helmet } from 'react-helmet';
import ProductService from "../services/productService";
import { toast } from 'react-toastify';

import {
  Avatar,
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
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle
} from '@material-ui/core';

const Account = () => {
  const [editing, setEditing] = useState(false);
  const [save, setSave] = useState(false);
  const [product, setProduct] = useState(null);
  const [image, setImage] = useState("");
  const prod_id = window.location.pathname.split('/').pop();

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

  const setImageUrl = (event) => {
    setImage(event.target.value);
  }
  useEffect ( () => {
    ProductService.getProduct(prod_id)
    .then( (res) => {
        return res.json();
    })
    .then ( (res) => {
      if(!res.errors){
        console.log(res)
          setProduct(res); 
          setImage(res.imageUrl);    
      }
      else {
        notifyError("Error obtaining product");
      }
    })
    .catch ( (error) => {
      notifyError("Something Went Wrong.");
    } )
  }, [] )

  const handleSubmit = () => {
    //@ts-ignore
    let name: string = document.getElementById('prodname').value;
    //@ts-ignore
    let description: string = document.getElementById('proddescription').value;
    //@ts-ignore
    let stock: number = document.getElementById('prodstock').value;
    //@ts-ignore
    let price: number = document.getElementById('prodprice').value;
    let supplier: number = product.supplier.id;
    //@ts-ignore
    ProductService.updateProduct(prod_id,name,description,price,stock,image,supplier)
      .then( (res) => {
        return res.json();
      })
      .then( (res) => {
        
        if(res.error){
          notifyError("Error updating product");
        }
        else{
          notifySuccess("Succesfully updated product");   
        }
        handleClose();
      })
      .catch( (error) => {
        notifyError("Something Went Wrong.");
      })
  }


  // cannot call it delete
  const [deleteD, setDeleteD] = useState(false);

  const handleClickOpen = () => {
    setSave(true);
  };

  const handleClose = () => {
    setSave(false);
    setEditing(false);
  };

  const handleDeleteOpen = () => {
    setDeleteD(true);
  }

  const handleDeleteClose = () => {
    setDeleteD(false);
  }
  

  return (
    <>
      <Helmet>
        <title>Product Details</title>
      </Helmet>
      <Box
        sx={{
          backgroundColor: 'background.default',
          minHeight: '100%',
          py: 3
        }}
      >
        <Container maxWidth="lg">
          { product != null  ?  
          <Grid
            container
            spacing={3}
          >
            <Grid
              item
              lg={4}
              md={6}
              xs={12}
            >
              <Card >
          
                <CardContent>
                  <Box
                    sx={{
                      alignItems: 'center',
                      display: 'flex',
                      flexDirection: 'column'
                    }}
                  >
                    <Avatar
                      sx={{
                        height: 300,
                        width: 300
                      }}
                      src={image}
                    />
                  </Box>
                </CardContent>
                <Divider />
              </Card>
            </Grid>
            <Grid
              item
              lg={8}
              md={6}
              xs={12}
            >
              <form
                autoComplete="off"
                noValidate
              >
                <Card>
                  <CardHeader
                    title="Product Details"
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
                        <TextField
                          fullWidth
                          name="name"
                          id="prodname"
                          required
                          label="Product Name"
                          variant="outlined"
                          defaultValue= {product.name}
                          disabled={!editing}
                        />
                      </Grid>
                      <Grid
                        item
                        md={12}
                        xs={12}
                      >
                        <TextField
                          fullWidth
                          label="Image URL"
                          name="image"
                          required
                          variant="outlined"
                          disabled={!editing}
                          defaultValue={product.imageUrl}
                          onChange={setImageUrl}
                        />
                      </Grid>
                      <Grid
                        item
                        md={6}
                        xs={12}
                      >
                        <TextField
                          fullWidth
                          label="Price"
                          name="price"
                          type="number"
                          required
                          variant="outlined"
                          defaultValue={product.price}
                          disabled={!editing}
                          id="prodprice"

                        />
                      </Grid>
                      <Grid
                        item
                        md={6}
                        xs={12}
                      >
                        <TextField
                          fullWidth
                          label="Stock"
                          name="stock"
                          type="number"
                          variant="outlined"
                          id="prodstock"
                          defaultValue={product.stock}
                          required
                          disabled={!editing}
                        />
                      </Grid>
                      <Grid
                        item
                        md={12}
                        xs={12}
                      >
                        <TextField
                          fullWidth
                          label="Description"
                          name="description"
                          id="proddescription"
                          variant="outlined"
                          defaultValue= { product.description}
                          disabled={!editing}
                          multiline
                          rows={3}
                        />
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
                    {editing ?
                      <>
                        <Button
                          color="primary"
                          variant="contained"
                          onClick={() => { handleClickOpen(); }}
                        >
                          Save Changes
                      </Button>
                        <div>
                          <Dialog
                            open={save}
                            onClose={handleClose}
                            aria-labelledby="dialog-title-edit"
                            aria-describedby="dialog-description-edit"
                          >
                            <DialogTitle id="dialog-title-edit">{"Save Changes"}</DialogTitle>
                            <DialogContent>
                              <DialogContentText id="dialog-description-edit">
                                Are you sure that you want to make these changes?
                          </DialogContentText>
                            </DialogContent>
                            <DialogActions>
                              <Button onClick={handleSubmit} color="primary">
                                Confirm
                              </Button>
                              <Button onClick={handleClose} color="primary" autoFocus>
                                Cancel
                          </Button>
                            </DialogActions>
                          </Dialog>
                        </div>
                      </>
                      :
                      <>
                        <Button
                          style={{ marginRight: '10px' }}
                          color="secondary"
                          variant="contained"
                          onClick={handleDeleteOpen}
                        >
                          Remove
                        </Button>
                        <div>
                          <Dialog
                            open={deleteD}
                            onClose={handleDeleteClose}
                            aria-labelledby="dialog-title-remove"
                            aria-describedby="dialog-description-remove"
                          >
                            <DialogTitle id="dialog-title-remove">{"Remove Product"}</DialogTitle>
                            <DialogContent>
                              <DialogContentText id="dialog-description-remove">
                                Are you sure that you want to remove this product?
                              </DialogContentText>
                            </DialogContent>
                            <DialogActions>
                              <Button onClick={handleDeleteClose} color="secondary">
                                Delete
                              </Button>
                              <Button onClick={handleDeleteClose} color="primary" autoFocus>
                                Cancel
                              </Button>
                            </DialogActions>
                          </Dialog>
                        </div>
                        <Button
                          color="primary"
                          variant="contained"
                          onClick={() => { setEditing(true) }}
                        >
                          Edit
                      </Button>
                      </>
                    }
                  </Box>
                </Card>
              </form>
            </Grid>
          </Grid>
          : ""}
        </Container>
      </Box>
    </>
  );
}

export default Account;
