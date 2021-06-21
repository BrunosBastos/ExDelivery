import { useState, useEffect } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
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
import useShopCartStore from 'src/stores/useShopCartStore';
import useAuthStore from 'src/stores/useAuthStore';

const DashboardNavbar = ({ onMobileNavOpen, ...rest }) => {
  const [notifications] = useState([]);
  const products = useShopCartStore(state => state.products);
  const theme = useTheme();
  const navigate = useNavigate();
  const hidden = useMediaQuery(theme => theme.breakpoints.up('lg'));
  const hiddenDown = useMediaQuery(theme => theme.breakpoints.down('lg'));

  const logout = () => {
    useAuthStore.getState().exit();
    navigate('/login', { replace: true });
  }

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
            <RouterLink to="/app/shoppingCart">
              <IconButton style={{ color: 'white' }}>
                <Badge
                  badgeContent={products.length}
                  color="secondary"
                  variant="number"
                >
                  <ShoppingCartIcon />
                </Badge>
              </IconButton>
            </RouterLink>
            <IconButton color="inherit">
              <Badge
                badgeContent={notifications.length}
                color="secondary"
                variant="dot"
              >
                <NotificationsIcon />
              </Badge>
            </IconButton>
          </>
        }
        <IconButton
          color="inherit"
          onClick={logout}
        >
          <InputIcon />
        </IconButton>
        {hidden ? null :
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
