import {useEffect, useState} from "react";
import {Box, Typography} from "@mui/material";
import {
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    Legend,
    LineChart,
    Line,
    AreaChart,
    CartesianGrid,
    Area
} from "recharts";
import axios from "axios";

const AssetTrendChart = () => {
    const [data, setData] = useState([]);
    //
    useEffect(() => {
        axios.get("/api/assets/monthly-summary")
            .then(res => {
                const formatted = res.data.map((item) => ({
                    ...item,
                    month: item.month,
                    // month: new Date(item.month + "-01").toLocaleString("default", {month: "short"})
                })).slice(-12);//直近１年だけ表示
                setData(formatted)
                // console.log(formatted)
            })
            .catch(err => console.error(err));
    }, []);

    // const data = [
    //     {"month": "Jan", "income": 5000},
    //     {"month": "Feb", "income": 5200},
    //     {"month": "Mar", "income": 5100},
    //     {"month": "Apr", "income": 2000},
    //     {"month": "May", "income": 6500},
    //     {"month": "Jun", "income": 3500}
    // ]

    return (
        <Box>
            <Typography variant="h6" fontWeight={"bold"} textAlign={"left"}
                        color="textSecondary" mb={1}>総資産推移</Typography>

            <ResponsiveContainer height={300}>
                <AreaChart data={data} margin={{left: 20}}>
                    <CartesianGrid strokeDasharray="3 3"/>
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
                    <Area
                        type="monotone"
                        dataKey="amount"
                        stroke="#8884d8"
                        fill="#8884d8"
                        fillOpacity={0.3}
                        name="資産額"
                    />
                </AreaChart>
            </ResponsiveContainer>
        </Box>
    );
};

export default AssetTrendChart;
