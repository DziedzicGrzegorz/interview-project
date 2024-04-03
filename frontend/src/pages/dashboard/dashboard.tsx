import {useEffect, useState} from "react";
import {Axios} from "@/utils/Axios.ts";
import {SkeletonDemo} from "@/components/loading.tsx";
import {DataTable} from "@/pages/dashboard/components/data-table.tsx";
import {columnsWrapper} from "@/pages/dashboard/columns.tsx";
import {ShowDecryptedMessage} from "@/pages/dashboard/components/show-decrypted-message.tsx";
import {DecryptedMessageResponse, EncryptedMessage} from "@/dto/CypherMessage";
import {toast} from "@/components/ui/use-toast.ts";
import {EncryptMessage} from "@/pages/dashboard/components/encrypt-message.tsx";
import {PasswordDialog} from "@/pages/dashboard/components/password-dialog.tsx";

export function Dashboard() {
    const [messages, setMessages] = useState<EncryptedMessage[]>()
    const [openDialog, setOpenDialog] = useState(false)
    const [alertDialogOpen, setAlertDialogOpen] = useState(false)
    const [password, setPassword] = useState("")

    const [messageId, setMessageId] = useState("")

    const [decryptedMessage, setDecryptedMessage] = useState<DecryptedMessageResponse>()

    const getMessages = async () => {
        try {
            const response = await Axios.get('/message');
            setMessages(response.data);
        } catch (error) {
            console.error(error)
        }
    }

    useEffect(() => {
        try {
            getMessages();
        } catch (error) {
            console.error(error)
        }

    }, []);

    useEffect(() => {
        const performDecryption = async () => {
            if (password && messageId) {
                const messageToDecrypt = messages?.find(message => message.id === messageId);
                if (messageToDecrypt) {
                    try {
                        const response = await Axios.post('/message/decrypt', {
                            id: messageId,
                            encryptedMessage: messageToDecrypt.encryptedMessage,
                            password
                        });

                        setDecryptedMessage(response.data)
                        setAlertDialogOpen(true);


                    } catch (error) {
                        setPassword("")
                        let errorMessage = "An unknown error occurred.";

                        if (Axios.isAxiosError(error) && error.response?.data?.error) {
                            console.error(error)
                            errorMessage = error.response.data.error;
                        }

                        toast({
                            variant: "destructive",
                            title: "Decryption failed",
                            description: errorMessage
                        });
                    }
                }
            }
        }
        performDecryption();

    }, [password, messageId, messages]);
    const handleDialogClose = () => {
        setAlertDialogOpen(false);
        setDecryptedMessage(undefined);
        setPassword("");
        setMessageId("");
        getMessages();
    };

    if (messages === undefined) {
        return <SkeletonDemo/>;
    }

    return (
        <div className="pt-14 lg:pt-8">
            <DataTable<EncryptedMessage, string>
                columns={columnsWrapper({setOpenDialog, setMessageId})}
                data={messages}/>
            <div className="w-full flex justify-center mt-2 ">
                <EncryptMessage onFormSubmitSuccess={getMessages}/>
            </div>
            <PasswordDialog dialogOpen={openDialog} setOpenDialog={setOpenDialog} setPassword={setPassword}
                            descriptionToCipher={messages.find(
                                message => message.id === messageId)?.messageIdentifier}/>

            <ShowDecryptedMessage alertDialogOpen={alertDialogOpen} setAlertDialogOpen={setAlertDialogOpen}
                                  decryptedMessage={decryptedMessage} handleDialogClose={handleDialogClose}
            />
        </div>
    );
}