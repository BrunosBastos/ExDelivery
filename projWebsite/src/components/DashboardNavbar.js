import { useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import PropTypes from 'prop-types';
import {
  AppBar,
  Badge,
  Box,
  useTheme,
  useMediaQuery,
  IconButton,
  Toolbar
} from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import NotificationsIcon from '@material-ui/icons/NotificationsOutlined';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import InputIcon from '@material-ui/icons/Input';
import Logo from './Logo';

const DashboardNavbar = ({ onMobileNavOpen, ...rest }) => {
  const [notifications] = useState([]);
  const [items] = useState([]);
  const theme = useTheme();
  const hidden = useMediaQuery(theme => theme.breakpoints.up('lg'));
  const hiddenDown = useMediaQuery(theme => theme.breakpoints.down('lg'));

  return (
    <AppBar
      elevation={0}
      {...rest}
    >
      <Toolbar>
        <RouterLink to="/">
          <Logo />
        </RouterLink>
        <Box sx={{ flexGrow: 1 }} />
        {hiddenDown ? null :
          <>
            <IconButton color="inherit">
              <Badge
                badgeContent={items.length}
                color="primary"
                variant="dot"
              >
                <ShoppingCartIcon />
              </Badge>
            </IconButton>
            <IconButton color="inherit">
              <Badge
                badgeContent={notifications.length}
                color="primary"
                variant="dot"
              >
                <NotificationsIcon />
              </Badge>
            </IconButton>
            <IconButton color="inherit">
              <InputIcon />
            </IconButton>
          </>
        }
        { hidden ? null :
          <IconButton
            color="inherit"
            onClick={onMobileNavOpen}
          >
            <MenuIcon />
          </IconButton>
        }
      </Toolbar>
    </AppBar>
  );
};

DashboardNavbar.propTypes = {
  onMobileNavOpen: PropTypes.func
};

export default DashboardNavbar;
