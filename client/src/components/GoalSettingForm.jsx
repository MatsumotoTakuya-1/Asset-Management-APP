import {useState} from "react";
import axios from "axios";
import {
    Box,
    Button,
    Stack,
    TextField,
} from "@mui/material";


function GoalSettingForm() {
    const [firstValue, setFirstValue] = useState("");
    const [targetAmount, setTargetAmount] = useState("");
    const [targetYear, setTargetYear] = useState("");
    const [targetRate, setTargetRate] = useState("");


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
            sx={{
                maxWidth: 300,
                mx: "auto",
                mt: 3,
                p: 4,
            }}
        >

            <Stack spacing={3}>
                <Stack spacing={3}>
                    <TextField
                        label="目標額"
                        placeholder="100000000"
                        type="number"
                        value={targetAmount}
                        onChange={(e) => setTargetAmount(e.target.value)}
                        fullWidth
                    />

                    <TextField
                        label="初期費用"
                        placeholder="100000"
                        type="number"
                        value={firstValue}
                        onChange={(e) => setFirstValue(e.target.value)}
                        fullWidth
                    />

                    <TextField
                        label="到達年"
                        placeholder="2025"
                        value={targetYear}
                        onChange={(e) => setTargetYear(e.target.value)}
                        fullWidth
                    />

                    <TextField
                        label="想定年利 (%)"
                        placeholder="5%"
                        type="number"
                        value={targetRate}
                        onChange={(e) => setTargetRate(e.target.value)}
                        fullWidth
                    />
                </Stack>
                <Box textAlign="right" mt={4}>
                    <Button type="submit" variant="outlined">
                        Set Target
                    </Button>
                </Box>
            </Stack>
        </Box>

    );
}

export default GoalSettingForm;
