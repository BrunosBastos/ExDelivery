import {
    Avatar,
    Card,
    CardContent,
    Grid,
    Button,
    Typography,
    Box
  } from '@material-ui/core';
  import { indigo } from '@material-ui/core/colors';
  import EuroOutlinedIcon from '@material-ui/icons/EuroOutlined';
  
  const Purchase = () => (
    <Card sx={{minWidth: 300}}>
      <CardContent>
        <Grid
          container
          sx={{ justifyContent: 'space-between' }}
          spacing={2}
          xs={12}
        >
          <Grid item xs={5}>
            <Avatar
              sx={{
                backgroundColor: indigo[600],
                height: 56,
                width: 56
              }}
            >
              <EuroOutlinedIcon />
            </Avatar>
          </Grid>
          <Grid item xs={7}>
            <Grid item xs={12}>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                }}
              >
                <Typography
                  color="textSecondary"
                  gutterBottom
                  variant="h6"
                >
                  Total Price
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={12}>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                }}
              >
                <Typography
                  color="textPrimary"
                  variant="h3"
                >
                  23,20 €
                </Typography>
              </Box>
            </Grid>
          </Grid>
        </Grid>
        <Grid
          container
          spacing={2}
          sx={{ justifyContent: 'space-between' }}
          xs={12}
        >
          <Grid item xs={12}>
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'flex-end',
                pt: 2
              }}
            >
              <Button color="primary" variant="contained">
                Purchase
              </Button>
            </Box>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
  
  export default Purchase;
  