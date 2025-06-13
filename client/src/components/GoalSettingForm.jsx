import {useState} from "react";
import axios from "axios";
import {
    Alert,
    Box,
    Button, Snackbar,
    Stack,
    TextField,
} from "@mui/material";


function GoalSettingForm({onSaved}) {
    const [firstValue, setFirstValue] = useState("");
    const [targetAmount, setTargetAmount] = useState("");
    const [targetYear, setTargetYear] = useState("");
    const [targetRate, setTargetRate] = useState("");

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState("");
    const [snackbarSeverity, setSnackbarSeverity] = useState("success");

    const showSnackbar = (message, severity = "success") => {
        setSnackbarMessage(message);
        setSnackbarSeverity(severity);
        setSnackbarOpen(true);
    };


    const processingRegister = async () => {
        try {
            const year = Number(targetYear);
            if (isNaN(year) || year < 2025 || year > 2200) {
                alert("到達年は2025-2200年の範囲で指定してください");
                return
            }
            //backendでDTOで型指定するとamountがStringでもIntに直してくれる
            await axios.post("/api/goals", {
                userId: 1,
                firstValue,
                targetAmount,
                targetYear,
                targetRate,
            });
            // alert("登録に成功しました。");
            showSnackbar("Successfully saved asset goal", "success");

            onSaved?.();//定義されていれば呼び出し?.
        } catch (err) {
            // alert("登録失敗");
            console.error(err);
            showSnackbar("Error saved asset goal", "error");

        }
    };

    return (
<>
    <Box
            component="form"
            onSubmit={(e) => {
                e.preventDefault();
                processingRegister();
            }}
            sx={{
                maxWidth: 300,
                mx: "auto",
                p: 4,
            }}
        >

            <Stack spacing={3}>
                <Stack spacing={3}>
                    <TextField
                        label="目標額"
                        placeholder="100000000"
                        type="text"
                        value={(Number(targetAmount) || "").toLocaleString()}
                        onChange={(e) => {
                            const raw = e.target.value.replaceAll(",", "");
                            if (/^\d*$/.test(raw)) setTargetAmount(raw);
                        }}
                        fullWidth
                    />

                    <TextField
                        label="初期費用"
                        placeholder="100000"
                        type="text"
                        value={(Number(firstValue) || "").toLocaleString()}
                        onChange={(e) => {
                            const raw = e.target.value.replaceAll(",", "");
                            if (/^\d*$/.test(raw)) setFirstValue(raw);
                        }}
                        fullWidth
                    />

                    <TextField
                        label="到達年"
                        placeholder="2025"
                        type="number"
                        value={targetYear}
                        onChange={(e) => {
                            const value = e.target.value;
                            if (/^\d{0,4}$/.test(value)) setTargetYear(value)
                        }}
                        fullWidth
                    />

                    <TextField
                        label="想定年利 (%)"
                        placeholder="5%"
                        type="text"
                        value={targetRate}
                        onChange={(e) => {
                            const raw = e.target.value;
                            if (/^\d*\.?\d*$/.test(raw)) setTargetRate(raw);
                        }}
                        fullWidth
                    />
                </Stack>
                <Box textAlign="right" mt={4}>
                    <Button type="submit" variant="outlined"
                            disabled={!firstValue || !targetAmount || !targetYear || !targetRate}
                    >
                        Set Target
                    </Button>
                </Box>
            </Stack>
        </Box>
    <Snackbar
        open={snackbarOpen}
        autoHideDuration={3000}
        onClose={() => setSnackbarOpen(false)}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
    >
        <Alert onClose={() => setSnackbarOpen(false)} severity={snackbarSeverity} sx={{ width: '100%' }}>
            {snackbarMessage}
        </Alert>
    </Snackbar>
</>

    );
}

export default GoalSettingForm;
