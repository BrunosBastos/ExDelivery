import { Helmet } from 'react-helmet';
import { Box, Container } from '@material-ui/core';
import CourierListResults from 'src/components/courier/CourierListResults';
import couriers from 'src/__mocks__/customers';

const CourierList = () => (
  <>
    <Helmet>
      <title>Couriers</title>
    </Helmet>
    <Box
      sx={{
        backgroundColor: 'background.default',
        minHeight: '100%',
        py: 3
      }}
    >
      <Container maxWidth={false}>
        <Box sx={{ pt: 3 }}>
          <CourierListResults couriers={couriers} />
        </Box>
      </Container>
    </Box>
  </>
);

export default CourierList;
