import { Helmet } from 'react-helmet';
import { v4 as uuid } from 'uuid';
import { useState } from 'react';
import { 
  Box,
  Container, 
  Card,
  CardContent,
  TextField,
  InputAdornment,
  SvgIcon
 } from '@material-ui/core';
import { Search as SearchIcon } from 'react-feather';
//@ts-ignore
import LatestOrders from 'src/components/dashboard//LatestOrders.tsx';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';


const OrderList = () => {
  const [recent, setRecent] = useState("asc");
  const [open, setOpen] = useState(false);

  const handleChange = (event) => {
    setRecent(event.target.value)
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  return (
  <>
    <Helmet>
      <title>My Orders</title>
    </Helmet>
    <Box
      sx={{
        backgroundColor: 'background.default',
        minHeight: '100%',
        py: 3
      }}
    >
      <Container maxWidth={false}>
        <Card>
          <CardContent>
            <Box sx={{ maxWidth: 500 }}>
            <InputLabel id="demo-controlled-open-select-label">Order Deliveries</InputLabel>
            <Select
              labelId="demo-controlled-open-select-label"
              id="demo-controlled-open-select"
              open={open}
              onClose={handleClose}
              onOpen={handleOpen}
              value={recent}
              onChange={handleChange}
            >
              <MenuItem value={"asc"}>Ascending</MenuItem>
              <MenuItem value={"desc"}>Descending</MenuItem>
            </Select>
            </Box>
          </CardContent>
        </Card>
        <Box sx={{ pt: 3 }}>
          <LatestOrders recent={recent}/>
        </Box>
      </Container>
    </Box>
  </>
  );
}

export default OrderList;