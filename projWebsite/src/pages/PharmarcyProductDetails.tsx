import React from 'react';
import { Helmet } from 'react-helmet';
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

  const [editing, setEditing] = React.useState(false);
  const [save, setSave] = React.useState(false);

  // cannot call it delete
  const [deleteD, setDeleteD] = React.useState(false);


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
                    />
                  </Box>
                </CardContent>
                <Divider />
                { editing ? 
                <CardActions>
                  <Button
                    color="primary"
                    fullWidth
                    variant="text"
                  >
                    Upload picture
                  </Button>
                </CardActions>
                : <></>}
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
                          required
                          label="Product Name"
                          variant="outlined"
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
                          label="Address"
                          name="address"
                          required
                          variant="outlined"
                          disabled={!editing}
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
                          disabled={!editing}

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
                          type="number"
                          variant="outlined"
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
                              <Button onClick={handleClose} color="primary">
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
        </Container>
      </Box>
    </>
  );
}

export default Account;
