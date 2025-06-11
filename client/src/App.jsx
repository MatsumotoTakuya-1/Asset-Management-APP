import './App.css'
import TotalAssetCard from "./components/TotalAssetCard.jsx";
import {Box, Divider, Typography} from "@mui/material";
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
            <Box display={"flex"} gap={2} mt={1}>
                <Box flex={1}>
                    <IncomeExpenseChart/>
                </Box>
                <Box flex={1}>
                    <AssetTrendChart/>
                </Box>
            </Box>
            <Divider sx={{my: 3}}/>

            <Typography variant={"h5"} fontWeight={"bold"} textAlign={"left"}>Asset Target</Typography>
            <Box display={"flex"} gap={2} mt={1}>
                <Box flex={1}>
                    <GoalSettingForm/>
                </Box>
                <Box flex={1}>
                    <GoalSimulationChart/>
                </Box>
            </Box>


        </>

    )
}

export default App
