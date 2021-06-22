import { Helmet } from 'react-helmet';
import { toast } from 'react-toastify';
import {
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
import SupplierService from "../services/supplierService";
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





const AddSupplier = () => {
  const navigate = useNavigate();
  
  const handleSubmit = () => {
    //@ts-ignore
    let name: string = document.getElementById('supplier_name').value;
    //@ts-ignore
    let lat: number = document.getElementById('supplier_lat').value;
    //@ts-ignore
    let lon: number = document.getElementById('supplier_lon').value;
  
    SupplierService.addNewSupplier(name, lat, lon)
      .then( (res) => {
        return res.json();
      })
      .then( (res) => {
        if(res.error){
          notifyError("Failed adding a new supplier")
        } else{
          navigate('/app/dashboard', { replace: true });
          notifySuccess("Successfully added a new supplier")
        }
      })
      .catch( (error) => {
        notifyError("Failed adding a new supplier")
      })
  }

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
              lg={8}
              md={12}
              xs={12}
            >
              <form
                autoComplete="off"
                noValidate
              >
                <Card>
                  <CardHeader
                    subheader="Fill the form with supplier's information"
                    title="Add Supplier"
                  />
                  <Divider />
                  <CardContent>
                    <Grid
                      container
                      spacing={1}
                    >
                      <Grid
                        pt={2}
                        container
                        md={12}
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
                            label="Supplier Name"
                            name="name"
                            required
                            variant="outlined"
                            id="supplier_name"
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
                            label="Latitude"
                            name="latitude"
                            required
                            type="number"
                            variant="outlined"
                            id="supplier_lat"
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
                            label="Longitude"
                            name="longitude"
                            required
                            type="number"
                            variant="outlined"
                            id="supplier_lon"
                          />
                        </Grid>
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
                      onClick= {() => handleSubmit()}
                    >
                      Add Supplier
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

export default AddSupplier;
