import {useEffect, useState} from "react";
import {Typography, Box} from "@mui/material";
import {BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Legend} from "recharts";
import axios from "axios";

const IncomeExpenseChart = () => {
    const [data, setData] = useState([]);

    useEffect(() => {
        axios.get("/api/transactions/monthly-summary")
            .then(res => {
                console.log(res.data);
                const formatted = res.data.map((item) => ({
                    ...item,
                    month: item.month,
                    // month: new Date(item.month + "-01").toLocaleString("default", {month: "short"})
                })).slice(-12);
                setData(formatted)
            })
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
        <Box>
            <Typography variant="h6" fontWeight={"bold"} textAlign={"left"}
                        color="textSecondary" mb={1}>年間収支</Typography>
            <ResponsiveContainer height={300}>
                <BarChart data={data} margin={{left: 20}}>
                    <XAxis dataKey="month"/>
                    <YAxis domain={[0, "auto"]}
                           tickFormatter={(val) => {
                               // if (val >= 1_000_000) return `￥${val / 1_000_000}M`;
                               if (val >= 1_000_0) return `${val / 1_000_0}万`;
                               return `${val.toLocaleString()}`;
                           }
                           }/>
                    <Tooltip/>
                    <Legend/>
                    <Bar dataKey="income" name="収入" fill="#4caf50"/>
                    <Bar dataKey="expense" name="支出" fill="#f44336"/>
                </BarChart>
            </ResponsiveContainer>
        </Box>
    );
};

export default IncomeExpenseChart;
