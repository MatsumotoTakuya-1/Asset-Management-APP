import {useEffect, useState} from "react";
import {Typography, Box} from "@mui/material";
import {BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend} from "recharts";
import axios from "axios";

const IncomeExpenseChart = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        axios.get("/api/transactions/monthly-summary")
            .then(res => setData(res.data))
            .catch(err => console.error(err));
    }, []);

    // const data = [
    //     { "month": "Jan", "income": 5000, "expense": 4000 },
    //     { "month": "Feb", "income": 5200, "expense": 4500 },
    //     { "month": "Mar", "income": 5100, "expense": 4600 },
    //     { "month": "Apr", "income": 2000, "expense": 1800 },
    //     { "month": "May", "income": 6500, "expense": 5000 },
    //     { "month": "Jun", "income": 3500, "expense": 3200 }
    // ]

    return (
        <Box sx={{mt: 5}}>

            <ResponsiveContainer height={300}>
                <BarChart data={data}>
                    <XAxis dataKey="month"/>
                    <YAxis/>
                    <Tooltip/>
                    <Legend/>
                    <Bar dataKey="income" name="Income" fill="#4caf50"/>
                    <Bar dataKey="expense" name="Expense" fill="#f44336"/>
                </BarChart>
            </ResponsiveContainer>
        </Box>
    );
};

export default IncomeExpenseChart;
