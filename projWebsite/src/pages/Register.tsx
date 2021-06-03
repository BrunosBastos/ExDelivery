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
  Tabs,
  Tab,
  AppBar
} from '@material-ui/core';
import React from 'react';
import SwipeableViews from 'react-swipeable-views';
import { makeStyles, Theme, useTheme } from '@material-ui/core/styles';



interface TabPanelProps {
  children?: React.ReactNode;
  dir?: string;
  index: any;
  value: any;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`full-width-tabpanel-${index}`}
      aria-labelledby={`full-width-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

function a11yProps(index: any) {
  return {
    id: `full-width-tab-${index}`,
    'aria-controls': `full-width-tabpanel-${index}`,
  };
}

const useStyles = makeStyles((theme: Theme) => ({
  root: {
    backgroundColor: theme.palette.background.paper,
    width: 500,
  },
}));

const Register = () => {
  const navigate = useNavigate();

  const classes = useStyles();
  const theme = useTheme();
  const [value, setValue] = React.useState(0);

  const handleChange = (event: React.ChangeEvent<{}>, newValue: number) => {
    setValue(newValue);
  };

  const handleChangeIndex = (index: number) => {
    setValue(index);
  };

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
            <AppBar position="static" color="default">
              <Tabs value={value} onChange={handleChange}
                indicatorColor="primary" textColor="primary"
                variant="fullWidth" aria-label="full width tabs example">
                <Tab label="Client" {...a11yProps(0)} />
                <Tab label="Courier" {...a11yProps(1)} />
                <Tab label="Pharmacy" {...a11yProps(2)} />
              </Tabs>
            </AppBar>
            <SwipeableViews axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
              index={value} onChangeIndex={handleChangeIndex}>
              <TabPanel value={value} index={0} dir={theme.direction}>
                
                <Formik
                  initialValues={{
                    email: '',
                    firstName: '',
                    lastName: '',
                    password: '',
                    policy: false
                  }}
                  validationSchema={
                    Yup.object().shape({
                      email: Yup.string().email('Must be a valid email').max(255).required('Email is required'),
                      firstName: Yup.string().max(255).required('First name is required'),
                      lastName: Yup.string().max(255).required('Last name is required'),
                      password: Yup.string().max(255).required('password is required'),
                      policy: Yup.boolean().oneOf([true], 'This field must be checked')
                    })
                  }
                  onSubmit={() => {
                    navigate('/app/dashboard', { replace: true });
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
                        error={Boolean(touched.firstName && errors.firstName)}
                        fullWidth
                        helperText={touched.firstName && errors.firstName}
                        label="First name"
                        margin="normal"
                        name="firstName"
                        onBlur={handleBlur}
                        onChange={handleChange}
                        value={values.firstName}
                        variant="outlined"
                      />
                      <TextField
                        error={Boolean(touched.lastName && errors.lastName)}
                        fullWidth
                        helperText={touched.lastName && errors.lastName}
                        label="Last name"
                        margin="normal"
                        name="lastName"
                        onBlur={handleBlur}
                        onChange={handleChange}
                        value={values.lastName}
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
                      <Box
                        sx={{
                          alignItems: 'center',
                          display: 'flex',
                          ml: -1
                        }}
                      >
                        <Checkbox
                          checked={values.policy}
                          name="policy"
                          onChange={handleChange}
                        />
                        <Typography
                          color="textSecondary"
                          variant="body1"
                        >
                          I have read the
                          {' '}
                          <Link
                            color="primary"
                            component={RouterLink}
                            to="#"
                            underline="always"
                            variant="h6"
                          >
                            Terms and Conditions
                          </Link>
                        </Typography>
                      </Box>
                      {Boolean(touched.policy && errors.policy) && (
                        <FormHelperText error>
                          {errors.policy}
                        </FormHelperText>
                      )}
                      <Box sx={{ py: 2 }}>
                        <Button
                          color="primary"
                          disabled={isSubmitting}
                          fullWidth
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

              </TabPanel>
              <TabPanel value={value} index={1} dir={theme.direction}>

              <Formik
                  initialValues={{
                    email: '',
                    firstName: '',
                    lastName: '',
                    password: '',
                    policy: false
                  }}
                  validationSchema={
                    Yup.object().shape({
                      email: Yup.string().email('Must be a valid email').max(255).required('Email is required'),
                      firstName: Yup.string().max(255).required('First name is required'),
                      lastName: Yup.string().max(255).required('Last name is required'),
                      password: Yup.string().max(255).required('password is required'),
                      policy: Yup.boolean().oneOf([true], 'This field must be checked')
                    })
                  }
                  onSubmit={() => {
                    navigate('/app/dashboard', { replace: true });
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
                        error={Boolean(touched.firstName && errors.firstName)}
                        fullWidth
                        helperText={touched.firstName && errors.firstName}
                        label="First name"
                        margin="normal"
                        name="firstName"
                        onBlur={handleBlur}
                        onChange={handleChange}
                        value={values.firstName}
                        variant="outlined"
                      />
                      <TextField
                        error={Boolean(touched.lastName && errors.lastName)}
                        fullWidth
                        helperText={touched.lastName && errors.lastName}
                        label="Last name"
                        margin="normal"
                        name="lastName"
                        onBlur={handleBlur}
                        onChange={handleChange}
                        value={values.lastName}
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
                      <Box
                        sx={{
                          alignItems: 'center',
                          display: 'flex',
                          ml: -1
                        }}
                      >
                        <Checkbox
                          checked={values.policy}
                          name="policy"
                          onChange={handleChange}
                        />
                        <Typography
                          color="textSecondary"
                          variant="body1"
                        >
                          I have read the
                          {' '}
                          <Link
                            color="primary"
                            component={RouterLink}
                            to="#"
                            underline="always"
                            variant="h6"
                          >
                            Terms and Conditions
                          </Link>
                        </Typography>
                      </Box>
                      {Boolean(touched.policy && errors.policy) && (
                        <FormHelperText error>
                          {errors.policy}
                        </FormHelperText>
                      )}
                      <Box sx={{ py: 2 }}>
                        <Button
                          color="primary"
                          disabled={isSubmitting}
                          fullWidth
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


              </TabPanel>
              <TabPanel value={value} index={2} dir={theme.direction}>


              <Formik
                  initialValues={{
                    email: '',
                    firstName: '',
                    lastName: '',
                    password: '',
                    policy: false
                  }}
                  validationSchema={
                    Yup.object().shape({
                      email: Yup.string().email('Must be a valid email').max(255).required('Email is required'),
                      firstName: Yup.string().max(255).required('First name is required'),
                      lastName: Yup.string().max(255).required('Last name is required'),
                      password: Yup.string().max(255).required('password is required'),
                      policy: Yup.boolean().oneOf([true], 'This field must be checked')
                    })
                  }
                  onSubmit={() => {
                    navigate('/app/dashboard', { replace: true });
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
                        error={Boolean(touched.firstName && errors.firstName)}
                        fullWidth
                        helperText={touched.firstName && errors.firstName}
                        label="First name"
                        margin="normal"
                        name="firstName"
                        onBlur={handleBlur}
                        onChange={handleChange}
                        value={values.firstName}
                        variant="outlined"
                      />
                      <TextField
                        error={Boolean(touched.lastName && errors.lastName)}
                        fullWidth
                        helperText={touched.lastName && errors.lastName}
                        label="Last name"
                        margin="normal"
                        name="lastName"
                        onBlur={handleBlur}
                        onChange={handleChange}
                        value={values.lastName}
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
                      <Box
                        sx={{
                          alignItems: 'center',
                          display: 'flex',
                          ml: -1
                        }}
                      >
                        <Checkbox
                          checked={values.policy}
                          name="policy"
                          onChange={handleChange}
                        />
                        <Typography
                          color="textSecondary"
                          variant="body1"
                        >
                          I have read the
                          {' '}
                          <Link
                            color="primary"
                            component={RouterLink}
                            to="#"
                            underline="always"
                            variant="h6"
                          >
                            Terms and Conditions
                          </Link>
                        </Typography>
                      </Box>
                      {Boolean(touched.policy && errors.policy) && (
                        <FormHelperText error>
                          {errors.policy}
                        </FormHelperText>
                      )}
                      <Box sx={{ py: 2 }}>
                        <Button
                          color="primary"
                          disabled={isSubmitting}
                          fullWidth
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

              </TabPanel>
            </SwipeableViews>
          </div>

        </Container>
      </Box>
    </>
  );
};

export default Register;