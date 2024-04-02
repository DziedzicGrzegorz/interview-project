import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import {DecryptedMessageResponse} from "@/dto/CypherMessage";

interface ShowDecryptedMessageProps {
    alertDialogOpen: boolean;
    setAlertDialogOpen: (open: boolean) => void;
    decryptedMessage?: DecryptedMessageResponse;
    handleDialogClose: () => void;
}

export function ShowDecryptedMessage({
                                         decryptedMessage,
                                         setAlertDialogOpen,
                                         alertDialogOpen,
                                         handleDialogClose,
                                     }: ShowDecryptedMessageProps) {
    return (
        <AlertDialog
            open={alertDialogOpen}
            onOpenChange={setAlertDialogOpen}

        >
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Your Decrypted Message</AlertDialogTitle>
                    <AlertDialogDescription
                    >
                        {decryptedMessage?.message}
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogAction
                        onClick={handleDialogClose}
                    >Continue</AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}
