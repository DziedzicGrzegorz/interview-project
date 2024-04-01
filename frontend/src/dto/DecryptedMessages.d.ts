export interface EncryptedMessage {
    id: string;
    createdDateTime: Date;
    message: string;
    messageIdentifier: string;
    decrypted: boolean;
}