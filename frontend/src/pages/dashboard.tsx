import {useEffect, useState} from "react";
import {EncryptedMessages} from "@/dto/EncryptedMessages";
import {Axios} from "@/utils/Axios.ts";

export function Dashboard() {
    const [messages, setMessages] = useState<EncryptedMessages[] | null>()
    useEffect(() => {
        try {
            const getMessages = async () => {
                const response = await Axios.get('/messages');
                setMessages(response.data);
            };
            getMessages();
        } catch (error) {
            console.log(error)
        }
        console.log(messages)

    }, []);

    return (
        <div>
            <h1>Protected</h1>
        </div>
    );
}