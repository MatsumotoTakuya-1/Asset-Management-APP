import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {BrowserRouter, Routes, Route} from "react-router-dom";
import theme from "./theme.js";
import AppLayout from "./layout/AppLayout.jsx";
import './index.css'
import App from './App.jsx'
import TransactionInputPage from "./components/TransactionInputPage.jsx";
import {CssBaseline, ThemeProvider} from "@mui/material";
import AssetInputPage from "./components/AssetInputPage.jsx";
import AssetHistoryPage from "./components/AssetHistoryPage.jsx";


createRoot(document.getElementById('root')).render(
    <ThemeProvider theme={theme}>
        <CssBaseline/>
        <AppLayout>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<App/>}/>
                    <Route path="/input/transactions/:yearMonth" element={<TransactionInputPage/>}/>
                    <Route path="/input/assets/:yearMonth" element={<AssetInputPage/>}/>
                    <Route path="/input/assets/:assetId/history" element={<AssetHistoryPage/>}/>
                </Routes>
            </BrowserRouter>
        </AppLayout>
    </ThemeProvider>
)
