import { useEffect } from 'react';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import PropTypes from 'prop-types';
import {
  Avatar,
  Box,
  Button,
  Divider,
  Drawer,
  useMediaQuery,
  useTheme,
  List,
  Typography
} from '@material-ui/core';
import {
  AlertCircle as AlertCircleIcon,
  BarChart as BarChartIcon,
  Lock as LockIcon,
  Package as PackageIcon,
  PlusCircle as PlusCircleIcon,
  Settings as SettingsIcon,
  ShoppingBag as ShoppingBagIcon,
  User as UserIcon,
  UserPlus as UserPlusIcon,
  Users as UsersIcon
} from 'react-feather';
import NavItem from './NavItem';
import HistoryIcon from '@material-ui/icons/History';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import LocalPharmacyIcon from '@material-ui/icons/LocalPharmacy';
import { UserType, isUserType } from "src/consts/userType";
import useAuthStore from 'src/stores/useAuthStore';

const user = {
  avatar: '/static/images/avatars/default_avatar.jpg',
  jobTitle: '',
  name: useAuthStore.getState().user?.name
};

const items = [
  {
    href: '/app/dashboard',
    icon: BarChartIcon,
    title: 'Dashboard',
    type: UserType.ANY,
  },
  // {
  //   href: '/app/couriers',
  //   icon: UsersIcon,
  //   title: 'Couriers',
  //   type: UserType.ADMIN,
  // },
  {
    href: '/app/orders',
    icon: HistoryIcon,
    title: 'Orders',
    type: UserType.CLIENT,
  },
  {
    href: '/app/adminOrders',
    icon: HistoryIcon,
    title: 'Platform Orders',
    type: UserType.ADMIN,
  },
  // {
  //   href: '/app/shoppingCart',
  //   icon: ShoppingCartIcon,
  //   title: 'Shopping Cart',
  //   type: UserType.ANY,
  // },
  // {
  //   href: '/app/products',
  //   icon: ShoppingBagIcon,
  //   title: 'Products',
  //   type: UserType.ANY,
  // },
  // {
  //   href: '/app/account',
  //   icon: UserIcon,
  //   title: 'Account',
  //   type: UserType.ANY,
  // },
  // {
  //   href: '/app/settings',
  //   icon: SettingsIcon,
  //   title: 'Settings',
  //   type: UserType.ANY,
  // },
  // {
  //   href: '/app/addProduct',
  //   icon: PlusCircleIcon,
  //   title: 'Add Product',
  //   type: UserType.ANY,
  // },
  // {
  //   href: '/app/addSupplier',
  //   icon: LocalPharmacyIcon,
  //   title: 'Add Supplier',
  //   type: UserType.ANY,
  // },
  // {
  //   href: '/app/product/1',
  //   icon: PackageIcon,
  //   title: 'Product Details',
  //   type: UserType.ANY,
  // },
];

const DashboardSidebar = ({ onMobileClose, openMobile }) => {
  const location = useLocation();
  const theme = useTheme();
  const user = useAuthStore(state => state.user);
  const hidden = useMediaQuery(theme => theme.breakpoints.up('lg'));
  const hiddenDown = useMediaQuery(theme => theme.breakpoints.down('lg'));

  useEffect(() => {
    if (openMobile && onMobileClose) {
      onMobileClose();
    }
  }, [location.pathname]);

  const content = (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%'
      }}
    >
      <Box
        sx={{
          alignItems: 'center',
          display: 'flex',
          flexDirection: 'column',
          p: 2
        }}
      >
        <Avatar
          component={RouterLink}
          src={user.avatar}
          sx={{
            cursor: 'pointer',
            width: 64,
            height: 64
          }}
          to="/app/account"
        />
        <Typography
          color="textPrimary"
          variant="h5"
        >
          {user.name}
        </Typography>
        <Typography
          color="textSecondary"
          variant="body2"
        >
          {user.jobTitle}
        </Typography>
      </Box>
      <Divider />
      <Box sx={{ p: 2 }}>
        <List>
          {items.filter((item) => isUserType(user.superUser, item.type))
            .map((item) => (
              <NavItem
                href={item.href}
                key={item.title}
                title={item.title}
                icon={item.icon}
              />
            ))}
        </List>
      </Box>
    </Box>
  );

  return (
    <>
      {hidden ? null :
        <Drawer
          anchor="left"
          onClose={onMobileClose}
          open={openMobile}
          variant="temporary"
          PaperProps={{
            sx: {
              width: 256
            }
          }}
        >
          {content}
        </Drawer>
      }
      {hiddenDown ? null :
        <Drawer
          anchor="left"
          open
          variant="persistent"
          PaperProps={{
            sx: {
              width: 256,
              top: 64,
              height: 'calc(100% - 64px)'
            }
          }}
        >
          {content}
        </Drawer>
      }
    </>
  );
};

DashboardSidebar.propTypes = {
  onMobileClose: PropTypes.func,
  openMobile: PropTypes.bool
};

DashboardSidebar.defaultProps = {
  onMobileClose: () => { },
  openMobile: false
};

export default DashboardSidebar;
