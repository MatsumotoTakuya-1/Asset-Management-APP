import './App.css'
import TotalAssetCard from "./components/TotalAssetCard.jsx";
import {Container, CssBaseline, Divider, ThemeProvider, Typography} from "@mui/material";
import DashboardActions from "./components/DashboardActions.jsx";

function App() {

    return (
        <>
            <Typography variant={"h4"} textAlign={"left"}>DashBoard</Typography>
            <TotalAssetCard/>
            <Divider sx={{ my: 3 }} />

            <Typography variant={"h5"} textAlign={"left"}>Quick Action</Typography>
            <DashboardActions/>
        </>

    )
}

export default App
