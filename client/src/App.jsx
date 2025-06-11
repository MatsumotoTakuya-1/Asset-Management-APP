import './App.css'
import TotalAssetCard from "./components/TotalAssetCard.jsx";
import {Divider, Typography} from "@mui/material";
import DashboardActions from "./components/DashboardActions.jsx";
import IncomeExpenseChart from "./components/IncomeExpenseChart.jsx";
import AssetTrendChart from "./components/AssetTrendChart .jsx";
import GoalSettingForm from "./components/GoalSettingForm.jsx";
import GoalSimulationChart from "./components/GoalSimulationChart.jsx";

function App() {

    return (
        <>
            <Typography variant={"h4"} textAlign={"left"} fontWeight={"bold"} mb={2}>DashBoard</Typography>
            <TotalAssetCard/>
            <Divider sx={{my: 3}}/>

            <Typography variant={"h5"} fontWeight={"bold"} textAlign={"left"}>Quick Action</Typography>
            <DashboardActions/>
            <Divider sx={{my: 3}}/>

            <Typography variant={"h5"} fontWeight={"bold"} textAlign={"left"}>Financial Overview</Typography>
            <IncomeExpenseChart/>
            <AssetTrendChart/>
            <Divider sx={{my: 3}}/>

            <Typography variant={"h5"} fontWeight={"bold"} textAlign={"left"}>Asset Target</Typography>
            <GoalSettingForm/>
            <GoalSimulationChart/>


        </>

    )
}

export default App
