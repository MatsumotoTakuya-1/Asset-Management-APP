import {forwardRef, useEffect, useImperativeHandle, useState} from "react";
import {Typography, Box} from "@mui/material";
import {
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    Legend, Area, AreaChart
} from "recharts";
import axios from "axios";
//forwardRef((props,ref) =>{としないとrefをpropsで受け取れない
const GoalSimulationChart = forwardRef((props, ref) => {
    const [chartData, setChartData] = useState([]);
    const [monthlyAmount, setMonthlyAmount] = useState(null);//積立額
    const [rate, setRate] = useState(0);
    const [first, setFirst] = useState(0);

    const fetchGoal = () => {
        axios
            .get("/api/goals")
            .then((res) => {
                const goal = res.data;

                const firstValue = goal.firstValue; // 初期資産
                const targetAmount = goal.targetAmount; // 目標資産
                const targetYear = parseInt(goal.targetYear); // 目標年
                const targetRate = parseFloat(goal.targetRate); // 年利（例: 0.05）
                setRate(targetRate)
                setFirst(firstValue)

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
    }

    //refreshで親からfetchGoal呼び出し(chartRef.current.refresh()で呼び出すメソッド定義）
    useImperativeHandle(ref, () => ({
        refresh: fetchGoal
    }));

    useEffect(() => {
        fetchGoal()
    }, []);

    return (
        <Box sx={{mt: 1}}>
            <Typography variant="h6" gutterBottom>
                目標達成シミュレーション
            </Typography>
            {monthlyAmount !== null && (
                <Typography variant="body2" color="text.secondary">
                    計算年利：{rate} % , 初期資産：{first}円<br/>
                    毎月必要な積立額：約 {monthlyAmount.toLocaleString()} 円
                </Typography>
            )}
            <ResponsiveContainer height={300}>
                <AreaChart data={chartData} margin={{left: 20}}>
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
                        name="シミュレーション資産額"
                    />
                </AreaChart>
            </ResponsiveContainer>
        </Box>
    );
});

export default GoalSimulationChart;