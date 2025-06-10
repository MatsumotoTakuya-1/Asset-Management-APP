import { Dialog, DialogTitle, DialogContent, DialogActions, Button, Typography } from "@mui/material";

const TransactionHistoryModal = ({
                                     open,
                                     onClose,
                                     category,
                                     yearMonth
                                 }) => {
    return (
        <Dialog open={open} onClose={onClose} fullWidth>
            <DialogTitle>{category} - {yearMonth} History</DialogTitle>
            <DialogContent>
                <Typography>ここに履歴一覧を表示（未実装）</Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
};

export default TransactionHistoryModal;