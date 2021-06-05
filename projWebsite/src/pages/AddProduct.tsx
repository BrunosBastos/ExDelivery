import { Helmet } from 'react-helmet';
import {
  Avatar,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Container,
  Divider,
  Grid,
  makeStyles,
  TextField,
  Typography
} from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
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

const AddProduct = () => {
  const classes = useStyles();
  return (
    <>
      <Helmet>
        <title>Account | Material Kit</title>
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
                    subheader="Fill the form with product information"
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
                          />
                        </Box>
                        <div className={classes.root} >
                          <input
                            accept="*.png"
                            className={classes.input}
                            id="upload-input"
                            type="file"
                          />
                          <label htmlFor="upload-input">
                            <Button
                              color="primary"
                              variant="contained"
                              component="span"
                            >
                              Upload Photo
                    </Button>
                          </label>
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
                          />
                        </Grid>
                        <Grid
                          item
                          md={12}
                          xs={12}
                          mb={2}

                        >
                          <TextField
                            fullWidth
                            label="Address"
                            name="address"
                            required
                            variant="outlined"
                          />
                        </Grid>

                        <Grid
                          item
                          lg={4}
                          md={6}
                          xs={6}
                          mb={2}
                          pr={1}
                        >
                          <TextField
                            fullWidth
                            label="Stock"
                            name="stock"
                            type="number"
                            required
                            variant="outlined"
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
                          />
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
