import './App.css'
import TotalAssetCard from "./components/TotalAssetCard.jsx";
import {Container, CssBaseline, Divider, ThemeProvider, Typography} from "@mui/material";
import DashboardActions from "./components/DashboardActions.jsx";
import IncomeExpenseChart from "./components/IncomeExpenseChart.jsx";
import AssetTrendChart from "./components/AssetTrendChart .jsx";

function App() {

    return (
        <>
            <Typography variant={"h4"} textAlign={"left"}>DashBoard</Typography>
            <TotalAssetCard/>
            <Divider sx={{my: 3}}/>

            <Typography variant={"h5"} textAlign={"left"}>Quick Action</Typography>
            <DashboardActions/>
            <Divider sx={{my: 3}}/>

            <Typography variant={"h5"} textAlign={"left"}>Financial Overview</Typography>
            <IncomeExpenseChart/>
            <AssetTrendChart/>
            <Divider sx={{my: 3}}/>


        </>

    )
}

export default App
