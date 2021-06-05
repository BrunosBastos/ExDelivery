import 'react-perfect-scrollbar/dist/css/styles.css';
import { useRoutes } from 'react-router-dom';
import { ThemeProvider } from '@material-ui/core';
import GlobalStyles from 'src/components/GlobalStyles';
import 'src/mixins/chartjs';
import theme from 'src/theme';
import routes from 'src/routes';
import { PersistGate } from 'zustand-persist'
import useAuthStore from 'src/stores/useAuthStore';
import useShopCartStore from 'src/stores/useShopCartStore';

const App = () => {
  useAuthStore();
  useShopCartStore();
  const routing = useRoutes(routes);

  return (
    <PersistGate>
      <ThemeProvider theme={theme}>
        <GlobalStyles />
        {routing}
      </ThemeProvider>
    </PersistGate>
  );
};

export default App;
