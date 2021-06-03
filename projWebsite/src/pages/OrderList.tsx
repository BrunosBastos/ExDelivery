import { Helmet } from 'react-helmet';
import { v4 as uuid } from 'uuid';
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
import CourierListResults from 'src/components/courier/CourierListResults';
import couriers from 'src/__mocks__/customers';
//@ts-ignore
import LatestOrders from 'src/components/dashboard//LatestOrders.tsx';


const orders = [
  {
    id: uuid(),
    ref: 'CDD1049',
    amount: 30.5,
    product: {
      name: 'Ekaterina Tankova'
    },
    delLocation: 'Oliveira de Azeméis',
    createdAt: 1595016400000,
    status: 'pending'
  },
  {
    id: uuid(),
    ref: 'CDD1048',
    amount: 25.1,
    product: {
      name: 'Cao Yu'
    },
    delLocation: 'Oliveira de Azeméis',
    createdAt: 1555016400000,
    status: 'delivered'
  },
  {
    id: uuid(),
    ref: 'CDD1047',
    amount: 10.99,
    product: {
      name: 'Alexa Richardson'
    },
    delLocation: 'São João da Madeira',
    createdAt: 1554930000000,
    status: 'refunded'
  },
  {
    id: uuid(),
    ref: 'CDD1046',
    amount: 96.43,
    product: {
      name: 'Anje Keizer'
    },
    delLocation: 'São João da Madeira',
    createdAt: 1554757200000,
    status: 'pending'
  },
  {
    id: uuid(),
    ref: 'CDD1045',
    amount: 32.54,
    product: {
      name: 'Clarke Gillebert'
    },
    delLocation: 'Mangualde',
    createdAt: 1554670800000,
    status: 'delivered'
  },
  {
    id: uuid(),
    ref: 'CDD1044',
    amount: 16.76,
    product: {
      name: 'Adam Denisov'
    },
    delLocation: 'Casa do Leandro',
    createdAt: 1554172800000,
    status: 'delivered'
  }
];

const CourierList = () => (
  <>
    <Helmet>
      <title>My Orders History</title>
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
              <TextField
                fullWidth
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <SvgIcon
                        fontSize="small"
                        color="action"
                      >
                        <SearchIcon />
                      </SvgIcon>
                    </InputAdornment>
                  )
                }}
                placeholder="Search Order"
                variant="outlined"
              />
            </Box>
          </CardContent>
        </Card>
        <Box sx={{ pt: 3 }}>
          <LatestOrders orders={orders}/>
        </Box>
      </Container>
    </Box>
  </>
);

export default CourierList;
