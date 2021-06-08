import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { Helmet } from 'react-helmet';
import * as Yup from 'yup';
import { Formik } from 'formik';
import {
  Box,
  Button,
  Checkbox,
  Container,
  FormHelperText,
  Link,
  TextField,
  Typography,
} from '@material-ui/core';
import React from 'react';
import { makeStyles, Theme, useTheme } from '@material-ui/core/styles';
import AuthentService from 'src/services/authentService';
import useAuthStore from 'src/stores/useAuthStore';
import { toast } from 'react-toastify';

const useStyles = makeStyles((theme: Theme) => ({
  root: {
    width: 500,
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

const Register = () => {
  const navigate = useNavigate();

  const theme = useTheme();
  const classes = useStyles(theme);

  return (
    <>
      <Helmet>
        <title>Register</title>
      </Helmet>
      <Box
        sx={{
          backgroundColor: 'background.default',
          display: 'flex',
          flexDirection: 'column',
          height: '100%',
          justifyContent: 'center'
        }}
      >
        <Container maxWidth="sm">
          <div className={classes.root}>
            <Formik
              initialValues={{
                email: '',
                name: '',
                password: '',
              }}
              validationSchema={
                Yup.object().shape({
                  email: Yup.string().email('Must be a valid email').max(255).required('Email is required'),
                  name: Yup.string().max(255).required('Name is required'),
                  password: Yup.string().max(255).required('password is required'),
                })
              }
              onSubmit={(values) => {
                AuthentService.register(values.email, values.password, values.name)
                .then((res) => {
                  if (res.status === 200) {
                    return res.json()
                  }
                })
                .then((res) => {
                  if (res && res.user) {
                    useAuthStore.getState().login(res.accessToken, res.user)
                    notifySuccess("Successfully registered")
                    navigate('/app/dashboard', { replace: true });
                  }
                  if(res.error)
                    notifyError("Registration failed")
                })
                .catch( (error) => {
                  notifyError("Registration failed")
                })
              }}
            >
              {({
                errors,
                handleBlur,
                handleChange,
                handleSubmit,
                isSubmitting,
                touched,
                values
              }) => (
                <form onSubmit={handleSubmit}>
                  <Box sx={{ mb: 3 }}>
                    <Typography
                      color="textPrimary"
                      variant="h2"
                    >
                      Create new account
                    </Typography>
                    <Typography
                      color="textSecondary"
                      gutterBottom
                      variant="body2"
                    >
                      Use your email to create new account
                    </Typography>
                  </Box>
                  <TextField
                    error={Boolean(touched.name && errors.name)}
                    fullWidth
                    helperText={touched.name && errors.name}
                    label="Name"
                    margin="normal"
                    name="name"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    value={values.name}
                    variant="outlined"
                  />
                  <TextField
                    error={Boolean(touched.email && errors.email)}
                    fullWidth
                    helperText={touched.email && errors.email}
                    label="Email Address"
                    margin="normal"
                    name="email"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    type="email"
                    value={values.email}
                    variant="outlined"
                  />
                  <TextField
                    error={Boolean(touched.password && errors.password)}
                    fullWidth
                    helperText={touched.password && errors.password}
                    label="Password"
                    margin="normal"
                    name="password"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    type="password"
                    value={values.password}
                    variant="outlined"
                  />
                  <Box sx={{ py: 2 }}>
                    <Button
                      color="primary"
                      fullWidth
                      // disabled={isSubmitting}
                      size="large"
                      type="submit"
                      variant="contained"
                    >
                      Sign up now
                    </Button>
                  </Box>
                  <Typography
                    color="textSecondary"
                    variant="body1"
                  >
                    Have an account?
                    {' '}
                    <Link
                      component={RouterLink}
                      to="/login"
                      variant="h6"
                    >
                      Sign in
                    </Link>
                  </Typography>
                </form>
              )}
            </Formik>
          </div>
        </Container>
      </Box>
    </>
  );
};

export default Register;