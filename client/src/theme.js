// src/theme.ts
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        background: {
            default: '#f9fbfd'
        },
        primary: {
            main: '#007bff'
        },
        secondary: {
            main: '#6c757d'
        }
    },
    typography: {
        fontFamily: `'Inter', sans-serif`,
        h4: { fontWeight: 600 },
        body2: { color: '#666' }
    },
    components: {
        MuiCard: {
            styleOverrides: {
                root: {
                    borderRadius: 12,
                    padding: 16,
                }
            }
        }
    }
});

export default theme;