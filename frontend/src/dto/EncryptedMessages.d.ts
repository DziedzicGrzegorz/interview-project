export interface EncryptedMessages {
    id: string;
    createdDateTime: Date;
    encryptedMessage: string;
    messageIdentifier: string;
    decrypted: boolean;
}