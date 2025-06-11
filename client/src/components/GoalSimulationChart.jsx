import {useEffect, useState} from "react";
import {Typography, Box} from "@mui/material";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    Legend
} from "recharts";
import axios from "axios";

const GoalSimulationChart = () => {
    const [chartData, setChartData] = useState([]);
    const [monthlyAmount, setMonthlyAmount] = useState(null);
    const [targetRate, setTargetRate] = useState(0);

    useEffect(() => {
        axios
            .get("/api/goals")
            .then((res) => {
                const goal = res.data;

                const firstValue = goal.firstValue; // 初期資産
                const targetAmount = goal.targetAmount; // 目標資産
                const targetYear = parseInt(goal.targetYear); // 目標年
                const targetRate = parseFloat(goal.targetRate); // 年利（例: 0.05）
                setTargetRate(targetRate)

                // シミュレーションロジック
                // 目標額(targetAmount) = 初期資産(firstValue)✖️(1+月利r）^n+毎月積立額P✖️((1+r)^n-1)/r
                //rは年利/12、nは期間（月数）
                const now = new Date();
                const startYear = now.getFullYear();
                const startMonth = now.getMonth() + 1;
                const totalMonths = (targetYear - startYear) * 12 - startMonth + 12;
                // console.log(totalMonths, startYear, startMonth, targetYear, targetRate);

                const r = targetRate / 100 / 12; // 月利
                const n = totalMonths;

                const fvGrowth = firstValue * Math.pow(1 + r, n);
                const monthlyFactor = (Math.pow(1 + r, n) - 1) / r;
                const monthly = (targetAmount - fvGrowth) / monthlyFactor; //積立額

                // console.log(monthly);
                setMonthlyAmount(Math.round(monthly));

                const data = [];
                let value = firstValue;

                for (let i = 0; i < n; i++) {
                    value = value * (1 + r) + monthly; //現在値✖️月利＋積立額
                    const date = new Date(now.getFullYear(), now.getMonth() + i, 1);
                    const label = `${date.getFullYear()}-${String(
                        date.getMonth() + 1
                    ).padStart(2, "0")}`;
                    data.push({month: label, amount: Math.round(value)});
                }

                setChartData(data);
            })
            .catch((err) => {
                console.error("目標取得エラー", err);
            });
    }, []);

    return (
        <Box sx={{mt: 5}}>
            <Typography variant="h6" gutterBottom>
                目標達成シミュレーション
            </Typography>
            {monthlyAmount !== null && (
                <Typography variant="body2" color="text.secondary">
                    計算年利：{targetRate} %
                    毎月必要な積立額：約 {monthlyAmount.toLocaleString()} 円
                </Typography>
            )}
            <ResponsiveContainer height={300}>
                <LineChart data={chartData}>
                    <XAxis dataKey="month"/>
                    <YAxis/>
                    <Tooltip/>
                    <Legend/>
                    <Line
                        type="monotone"
                        dataKey="amount"
                        stroke="#8884d8"
                        dot={false}
                        name="シミュレーション資産額"
                    />
                </LineChart>
            </ResponsiveContainer>
        </Box>
    );
};

export default GoalSimulationChart;