import {useEffect, useState} from "react";
import {Typography, Box} from "@mui/material";
import {BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend, LineChart, Line} from "recharts";
import axios from "axios";

const GoalSimulationChart = () => {
    const [data, setData] = useState([]);
    //
    useEffect(() => {
        axios.get("/api/goals")
            .then(res => {
                setData(res.data);
            })
            .catch(err => console.error(err));
    }, []);
    console.log(data)

    // const data = [
    //     {"month": "Jan", "income": 5000},
    //     {"month": "Feb", "income": 5200},
    //     {"month": "Mar", "income": 5100},
    //     {"month": "Apr", "income": 2000},
    //     {"month": "May", "income": 6500},
    //     {"month": "Jun", "income": 3500}
    // ]

    return (
        <Box sx={{mt: 5}}>

            <ResponsiveContainer height={300}>
                <LineChart data={data}>
                    <XAxis dataKey="month"/>
                    <YAxis/>
                    <Tooltip/>
                    <Legend/>
                    <Line
                        type="monotone"
                        dataKey="amount"
                        dot={false}
                    />
                </LineChart>
            </ResponsiveContainer>
        </Box>
    );
};

export default GoalSimulationChart;
