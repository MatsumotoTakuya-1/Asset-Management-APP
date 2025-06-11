import {useState} from "react";
import axios from "axios";

import {
    Box,
    Button,
    Stack,
    TextField,
} from "@mui/material";

function GoalSettingForm() {
    const [firstValue, setFirstValue] = useState(0);
    const [targetAmount, setTargetAmount] = useState(0);
    const [targetYear, setTargetYear] = useState("");
    const [targetRate, setTargetRate] = useState(0);


    const processingRegister = async () => {
        try {
            await axios.post("/api/goals", {
                userId: 1,
                firstValue,
                targetAmount,
                targetYear,
                targetRate,
            });
            alert("登録に成功しました。");


        } catch (err) {
            alert("登録失敗");
            console.error(err);
        }
    };

    return (

        <Box
            component="form"
            onSubmit={(e) => {
                e.preventDefault();
                processingRegister();
            }}
        >

            <Stack spacing={3}>
                <TextField
                    label="first value"
                    placeholder="￥100"
                    value={firstValue}
                    onChange={(e) => setFirstValue(e.target.value)}
                    fullWidth
                    sx={{color: "text.secondary"}}
                ></TextField>

                <TextField
                    label="Target Amount"
                    placeholder="￥100000000"
                    value={targetAmount}
                    onChange={(e) => setTargetAmount(e.target.value)}
                    fullWidth
                    sx={{color: "text.secondary"}}
                ></TextField>

                <TextField
                    label="Target Year"
                    placeholder="2025"
                    value={targetYear}
                    onChange={(e) => setTargetYear(e.target.value)}
                    fullWidth
                    sx={{color: "text.secondary"}}
                ></TextField>

                <TextField
                    label="Annual Rate"
                    placeholder="5%"
                    value={targetRate}
                    onChange={(e) => setTargetRate(e.target.value)}
                    fullWidth
                    sx={{color: "text.secondary"}}
                ></TextField>
                <Box mt={8}>
                    <Button type="submit" fullWidth>
                        SetTarget
                    </Button>
                </Box>
            </Stack>
        </Box>

    );
}

export default GoalSettingForm;
